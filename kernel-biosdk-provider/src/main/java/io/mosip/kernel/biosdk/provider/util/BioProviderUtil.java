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
			String instanceKey = modalityParams.entrySet().stream().sorted(Map.Entry.comparingByKey())
					.map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("-"));
			if (sdkInstances.containsKey(instanceKey)) {
				logger.debug("SDK instance reused for modality class : {}",
						modalityParams.get(ProviderConstants.CLASSNAME));
				return sdkInstances.get(instanceKey);
			}
			Class<?> object = Class.forName(modalityParams.get(ProviderConstants.CLASSNAME));
			Object[] args = new Object[0];
			if (modalityParams.get(ProviderConstants.ARGUMENTS) != null
					&& !modalityParams.get(ProviderConstants.ARGUMENTS).isEmpty())
				args = modalityParams.get(ProviderConstants.ARGUMENTS).split(",");

			Optional<Constructor<?>> result = ReflectionUtils.findConstructor(object, args);
			if (result.isPresent()) {
				Constructor<?> constructor = result.get();
				constructor.setAccessible(true);
				logger.debug("SDK instance created with params : {}", modalityParams);
				Object newInstance = constructor.newInstance(args);
				sdkInstances.put(instanceKey, newInstance);
				return newInstance;
			} else {
				throw new BiometricException(ErrorCode.NO_CONSTRUCTOR_FOUND.getErrorCode(),
						String.format(ErrorCode.NO_CONSTRUCTOR_FOUND.getErrorMessage(),
								modalityParams.get(ProviderConstants.CLASSNAME),
								modalityParams.get(ProviderConstants.ARGUMENTS)));
			}
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

		Method result = null;
		Class<?> searchType = type;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType));
			for (Method method : methods) {
				if (name.equals(method.getName()) && hasSameParams(method, parameterTypes)
						&& (result == null || result.isSynthetic() || result.isBridge())) {
					result = method;
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
	 * Checks if a method has the same parameter types as the provided array of
	 * parameter types.
	 *
	 * This method compares the number of parameters in the method and the length of
	 * the `paramTypes` array. It then uses `Arrays.equals` to ensure the
	 * element-wise types of parameters in both sources are identical.
	 *
	 * @param method     The method object to be inspected.
	 * @param paramTypes The expected parameter types of the method.
	 * @return True if the method has the same parameter types as specified, false
	 *         otherwise.
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
	 * Retrieves the declared methods (including public, private, protected) for a
	 * given class.
	 *
	 * This method first checks the `declaredMethodsCache` for the methods
	 * associated with the provided `clazz`. - If found in the cache, the cached
	 * methods are returned directly. - If not found: 1. Calls
	 * `clazz.getDeclaredMethods()` to retrieve all declared methods. 2. Optionally
	 * calls `findConcreteMethodsOnInterfaces` (not shown here) to get concrete
	 * methods from implemented interfaces. 3. Combines the declared methods and
	 * concrete methods from interfaces into a single array `result`. 4. Stores the
	 * `result` in the cache for `clazz`. 5. Returns the `result` array containing
	 * all methods for the class and its implemented interfaces. In case of
	 * exceptions during reflection, it throws an `IllegalStateException` with
	 * details.
	 *
	 * @param clazz The class for which to retrieve declared methods.
	 * @return An array of `Method` objects representing all declared methods
	 *         (including those from implemented interfaces).
	 * @throws IllegalStateException If there's an issue during reflection.
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
	 * Finds concrete methods declared in implemented interfaces of a class.
	 *
	 * This method iterates through all implemented interfaces
	 * (`clazz.getInterfaces()`) of the provided `clazz`. For each interface
	 * (`ifc`), it examines all its methods (`ifc.getMethods()`) and checks if they
	 * are concrete (not abstract) using
	 * `Modifier.isAbstract(ifcMethod.getModifiers())`. If a concrete method is
	 * found, it's added to the `result` list.
	 *
	 * @param clazz The class for which to find concrete methods from implemented
	 *              interfaces.
	 * @return A list of `Method` objects representing concrete methods declared in
	 *         implemented interfaces, or null if no concrete methods are found.
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