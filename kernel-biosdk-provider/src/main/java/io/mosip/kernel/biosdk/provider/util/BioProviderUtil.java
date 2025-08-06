package io.mosip.kernel.biosdk.provider.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.util.ReflectionUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

import io.mosip.kernel.core.bioapi.exception.BiometricException;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.logger.spi.Logger;

/**
 * Utility class for Biometric Provider management.
 *
 * This class provides methods for creating and managing instances of Biometric
 * SDKs. It utilizes a singleton pattern to ensure only one instance of each SDK
 * is created based on the provided modality parameters.
 */
public class BioProviderUtil {
	private static final Logger logger = BioSDKProviderLoggerFactory.getLogger(BioProviderUtil.class);

	private BioProviderUtil() {
		throw new IllegalStateException("BioProviderUtil class");
	}

	/**
	 * A map to store instances of Biometric SDKs keyed by a unique identifier
	 * derived from the modality parameters. This ensures efficient retrieval of
	 * previously created instances based on the same configuration.
	 */
	private static Map<String, Object> sdkInstances = new HashMap<>();

	private static final Map<String, Class<?>> CLASS_CACHE = new java.util.concurrent.ConcurrentHashMap<>();
	private static final Map<String, Constructor<?>> CONSTRUCTOR_CACHE = new java.util.concurrent.ConcurrentHashMap<>();
	private static final Map<String, Method> METHOD_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

	/**
	 * Retrieves an instance of the Biometric SDK based on the provided modality
	 * parameters.
	 *
	 * This method follows these steps: 1. Generates a unique key from the
	 * `modalityParams` to identify the SDK instance. 2. Checks if an instance for
	 * this key already exists in the `sdkInstances` map. - If found, the existing
	 * instance is returned for reuse. 3. If not found, it reflects on the class
	 * name specified in the `modalityParams`. 4. Attempts to find a constructor
	 * matching the provided arguments using `ReflectionUtils`. 5. If a suitable
	 * constructor is found, it creates a new instance of the Biometric SDK class.
	 * 6. Stores the newly created instance in the `sdkInstances` map for future
	 * retrieval. 7. Returns the Biometric SDK instance.
	 *
	 * @param modalityParams A map containing parameters required for Biometric SDK
	 *                       initialization. Expected keys include: -
	 *                       {@link ProviderConstants#CLASSNAME}: The fully
	 *                       qualified class name of the Biometric SDK. -
	 *                       {@link ProviderConstants#ARGUMENTS} (optional): A
	 *                       comma-separated string of constructor arguments for the
	 *                       Biometric SDK class (if required).
	 * @return An instance of the Biometric SDK or throws a
	 *         {@link BiometricException} if an error occurs.
	 * @throws BiometricException If there's an issue creating the Biometric SDK
	 *                            instance.
	 */
	@SuppressWarnings({ "java:S3011" })
	public static Object getSDKInstance(Map<String, String> modalityParams) throws BiometricException {
		try {
			StringBuilder sb = new StringBuilder();
			modalityParams.entrySet().stream()
					.sorted(Map.Entry.comparingByKey())
					.forEach(entry -> sb.append(entry.getKey()).append('=').append(entry.getValue()).append('-'));
			String instanceKey = sb.toString();

			Object cachedInstance = sdkInstances.get(instanceKey);
			if (cachedInstance != null) {
				logger.debug("SDK instance reused for modality class : {}",
						modalityParams.get(ProviderConstants.CLASSNAME));
				return cachedInstance;
			}

			String className = modalityParams.get(ProviderConstants.CLASSNAME);
			if (className == null || className.isEmpty()) {
				throw new BiometricException(ErrorCode.SDK_INITIALIZATION_FAILED.getErrorCode(),
						"Class name for SDK is missing in modality parameters");
			}

			Class<?> clazz = CLASS_CACHE.computeIfAbsent(className, key -> {
				try {
					return Class.forName(key);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			});

			String argsStr = modalityParams.get(ProviderConstants.ARGUMENTS);
			Object[] args = (argsStr == null || argsStr.isEmpty()) ? new Object[0] : argsStr.split(",");

			String ctorKey = className + "|" + args.length;
			Constructor<?> constructor = CONSTRUCTOR_CACHE.computeIfAbsent(ctorKey, key -> {
				try {
					if (args.length == 0) {
						return clazz.getDeclaredConstructor();
					} else {
						// Assuming single String[] constructor fallback
						return clazz.getDeclaredConstructor(String[].class);
					}
				} catch (Exception e) {
					return null;
				}
			});

			constructor.setAccessible(true);
			Object newInstance = (args.length == 0) ? constructor.newInstance() : constructor.newInstance((Object) args);

			sdkInstances.put(instanceKey, newInstance);
			return newInstance;
		} catch (Exception e) {
			throw new BiometricException(ErrorCode.SDK_INITIALIZATION_FAILED.getErrorCode(),
					String.format(ErrorCode.SDK_INITIALIZATION_FAILED.getErrorMessage(),
							modalityParams.get(ProviderConstants.CLASSNAME), ExceptionUtils.getStackTrace(e)));
		}
	}

	/**
	 * Finds a required method on a class or its superclasses/interfaces.
	 *
	 * This method searches for a public method with the specified name and
	 * parameter types on the given class and all its superclasses and implemented
	 * interfaces. It prioritizes non-synthetic and non-bridge methods.
	 *
	 * @param type           The class to search for the method.
	 * @param name           The name of the method to find.
	 * @param parameterTypes The expected parameter types of the method.
	 * @return The `Method` object representing the found method, or throws an
	 *         `IllegalArgumentException` if the method is not found.
	 * @throws IllegalArgumentException If the method with the specified name and
	 *                                  parameter types cannot be found.
	 */
	public static Method findRequiredMethod(Class<?> type, String name, Class<?>... parameterTypes) {
		Assert.notNull(type, "Class must not be null");
		Assert.notNull(name, "Method name must not be null");

		String key = buildMethodKey(type, name, parameterTypes);
		Method cachedMethod = METHOD_CACHE.get(key);
		if (cachedMethod != null) {
			return cachedMethod;
		}

		Method result = null;
		Class<?> searchType = type;

		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType));
			for (Method method : methods) {
				if (name.equals(method.getName()) && hasSameParams(method, parameterTypes)
						&& (result == null || result.isSynthetic() || result.isBridge())) {
					result = method;
					if (!result.isSynthetic() && !result.isBridge()) {
						METHOD_CACHE.put(key, result);
						return result;
					}
				}
			}
			searchType = searchType.getSuperclass();
		}

		if (result == null) {
			String parameterTypeNames = Arrays.stream(parameterTypes).map(Object::toString)
					.collect(Collectors.joining(", "));

			throw new IllegalArgumentException(
					String.format("Unable to find method %s(%s) on %s", name, parameterTypeNames, type));
		}

		return result;
	}

	/**
	 * Builds a unique cache key for identifying a method based on its declaring class,
	 * method name, and parameter types.
	 * <p>
	 * This key is used in caching mechanisms to avoid repeated reflection lookups
	 * for the same method. The generated key format is:
	 * <pre>
	 *     ClassName|methodName|paramType1|paramType2|...|paramTypeN
	 * </pre>
	 * Example:
	 * <pre>
	 *     buildMethodKey(MyClass.class, "doSomething", new Class<?>[]{String.class, int.class})
	 *     // Result: "com.example.MyClass|doSomething|java.lang.String|int"
	 * </pre>
	 *
	 * @param type           The class in which the method is declared. Must not be {@code null}.
	 * @param name           The name of the method. Must not be {@code null}.
	 * @param parameterTypes The parameter types of the method. If no parameters, an empty array can be provided.
	 * @return A unique string key representing the combination of class, method name, and parameter types.
	 */
	private static String buildMethodKey(Class<?> type, String name, Class<?>[] parameterTypes) {
		StringBuilder sb = new StringBuilder(type.getName()).append('|').append(name);
		for (Class<?> paramType : parameterTypes) {
			sb.append('|').append(paramType.getName());
		}
		return sb.toString();
	}

	/**
	 * Checks whether a given {@link Method} has the same parameter types as the provided array of parameter types.
	 * <p>
	 * This utility method is primarily used during reflection-based method lookups to ensure that
	 * the discovered method matches the expected method signature exactly. The comparison considers:
	 * <ul>
	 *     <li>The number of parameters in the method and in the provided parameter type array.</li>
	 *     <li>The type and order of parameters using {@link Arrays#equals(Object[], Object[])}.</li>
	 * </ul>
	 * <p>
	 * Example:
	 * <pre>
	 *     Method m = MyClass.class.getMethod("processData", String.class, int.class);
	 *     boolean matches = hasSameParams(m, new Class<?>[]{String.class, int.class});
	 *     // matches = true
	 * </pre>
	 *
	 * @param method      The {@link Method} to check. Must not be {@code null}.
	 * @param paramTypes  The expected parameter types to match against. Must not be {@code null}.
	 * @return {@code true} if the method has the same parameter count and parameter types (in order),
	 *         {@code false} otherwise.
	 */
	private static boolean hasSameParams(Method method, Class<?>[] paramTypes) {
		return (paramTypes.length == method.getParameterCount()
				&& Arrays.equals(paramTypes, method.getParameterTypes()));
	}

	/**
	 * Cache for storing the declared methods of a class.
	 *
	 * This `ConcurrentReferenceHashMap` is used to cache the results of
	 * `getDeclaredMethods` for different classes. It improves performance by
	 * avoiding redundant reflection calls for the same class. The concurrent nature
	 * ensures thread-safe access.
	 */
	private static final Map<Class<?>, Method[]> declaredMethodsCache = new ConcurrentReferenceHashMap<>(256);

	/**
	 * Empty method array representing a class with no declared methods.
	 *
	 * This static final array `NO_METHODS` is used as a placeholder value in the
	 * `declaredMethodsCache` when a class has no declared methods. It avoids
	 * creating a new empty array for each such case.
	 */
	private static final Method[] NO_METHODS = {};

	/**
	 * Retrieves all declared methods of the specified class, including any concrete methods
	 * defined in its implemented interfaces, and caches the result for improved performance.
	 * <p>
	 * This method:
	 * <ul>
	 *     <li>Validates that the input {@code clazz} is not null.</li>
	 *     <li>Checks if the declared methods for the given class are already cached in
	 *     {@code declaredMethodsCache} to avoid repeated reflection lookups.</li>
	 *     <li>If not cached:
	 *         <ul>
	 *             <li>Uses {@link Class#getDeclaredMethods()} to retrieve all declared methods
	 *             (including private, protected, and public methods, excluding inherited ones).</li>
	 *             <li>Additionally retrieves concrete (non-abstract) methods from all interfaces
	 *             implemented by the class using {@link #findConcreteMethodsOnInterfaces(Class)}.</li>
	 *             <li>Combines both sets of methods into a single array and stores it in the cache
	 *             for future lookups.</li>
	 *         </ul>
	 *     </li>
	 * </ul>
	 * <p>
	 * This approach ensures that method lookups using reflection are performed only once per class,
	 * significantly improving efficiency in repeated calls.
	 * </p>
	 *
	 * <pre>
	 * Example:
	 *   Method[] methods = getDeclaredMethods(MyService.class);
	 *   for (Method method : methods) {
	 *       System.out.println("Method: " + method.getName());
	 *   }
	 * </pre>
	 *
	 * @param clazz The class for which declared methods need to be retrieved. Must not be {@code null}.
	 * @return An array of {@link Method} objects representing all declared methods of the class,
	 *         including concrete interface methods. Returns an empty array if no methods are found.
	 * @throws IllegalStateException If reflection fails to retrieve the declared methods of the class.
	 */
	private static Method[] getDeclaredMethods(Class<?> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		Method[] result = declaredMethodsCache.get(clazz);
		if (result == null) {
			try {
				Method[] declaredMethods = clazz.getDeclaredMethods();
				List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
				if (defaultMethods != null) {
					result = new Method[declaredMethods.length + defaultMethods.size()];
					System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
					int index = declaredMethods.length;
					for (Method defaultMethod : defaultMethods) {
						result[index] = defaultMethod;
						index++;
					}
				} else {
					result = declaredMethods;
				}
				declaredMethodsCache.put(clazz, (result.length == 0 ? NO_METHODS : result));
			} catch (Exception ex) {
				throw new IllegalStateException("Failed to introspect Class [" + clazz.getName()
						+ "] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
			}
		}
		return result;
	}

	/**
	 * Retrieves all concrete (non-abstract) methods defined in the interfaces
	 * implemented by the specified class.
	 * <p>
	 * This method iterates over each interface implemented by the given class
	 * and inspects its methods using {@link Class#getMethods()}.
	 * Any method that is not abstract (i.e., has an implementation, such as
	 * a default method in an interface) is collected into a result list.
	 * <p>
	 * This utility is particularly useful for reflection-based operations
	 * where default interface methods should be considered along with the
	 * class's declared methods.
	 * <p>
	 * Steps performed:
	 * <ul>
	 *     <li>Retrieve all interfaces implemented by the given class.</li>
	 *     <li>Inspect each method in these interfaces.</li>
	 *     <li>Check if the method is non-abstract using
	 *     {@link Modifier#isAbstract(int)}.</li>
	 *     <li>Add concrete methods to the result list.</li>
	 * </ul>
	 *
	 * <pre>
	 * Example:
	 *   List&lt;Method&gt; methods = findConcreteMethodsOnInterfaces(MyClass.class);
	 *   for (Method method : methods) {
	 *       System.out.println("Concrete interface method: " + method.getName());
	 *   }
	 * </pre>
	 *
	 * @param clazz the class whose implemented interfaces should be scanned
	 *              for concrete (default) methods; must not be {@code null}.
	 * @return a list of {@link Method} objects representing all concrete
	 *         methods from implemented interfaces, or {@code null} if none are found.
	 */
	@Nullable
	private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
		List<Method> result = null;
		for (Class<?> ifc : clazz.getInterfaces()) {
			for (Method ifcMethod : ifc.getMethods()) {
				if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
					if (result == null) {
						result = new LinkedList<>();
					}
					result.add(ifcMethod);
				}
			}
		}
		return result;
	}
}