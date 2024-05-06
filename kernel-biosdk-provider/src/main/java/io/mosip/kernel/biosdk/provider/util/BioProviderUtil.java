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

public class BioProviderUtil {

	private static Map<String, Object> sdkInstances = new HashMap<>();

	private static final Logger LOGGER = BioSDKProviderLoggerFactory.getLogger(BioProviderUtil.class);

	public static Object getSDKInstance(Map<String, String> modalityParams) throws BiometricException {
		try {
			String instanceKey = modalityParams.entrySet().stream().sorted(Map.Entry.comparingByKey())
					.map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("-"));
			if (sdkInstances.containsKey(instanceKey)) {
				LOGGER.debug("SDK instance reused for modality class >>> {}", modalityParams.get(ProviderConstants.CLASSNAME));
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
				LOGGER.debug("SDK instance created with params >>> {}", modalityParams);
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
	
	public static Method findRequiredMethod(Class<?> type, String name, Class<?>... parameterTypes) {

		Assert.notNull(type, "Class must not be null");
		Assert.notNull(name, "Method name must not be null");

		Method result = null;
		Class<?> searchType = type;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods()
					: getDeclaredMethods(searchType));
			for (Method method : methods) {
				if (name.equals(method.getName()) && hasSameParams(method, parameterTypes)) {
					if (result == null || result.isSynthetic() || result.isBridge()) {
						result = method;
					}
				}
			}
			searchType = searchType.getSuperclass();
		}

		if (result == null) {

			String parameterTypeNames = Arrays.stream(parameterTypes) //
					.map(Object::toString) //
					.collect(Collectors.joining(", "));

			throw new IllegalArgumentException(
					String.format("Unable to find method %s(%s) on %s", name, parameterTypeNames, type));
		}

		return result;
	}

	private static boolean hasSameParams(Method method, Class<?>[] paramTypes) {
		return (paramTypes.length == method.getParameterCount() && Arrays.equals(paramTypes, method.getParameterTypes()));
	}

	private static final Map<Class<?>, Method[]> declaredMethodsCache = new ConcurrentReferenceHashMap<>(256);
	private static final Method[] NO_METHODS = {};
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
				}
				else {
					result = declaredMethods;
				}
				declaredMethodsCache.put(clazz, (result.length == 0 ? NO_METHODS : result));
			}
			catch (Throwable ex) {
				throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() +
						"] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
			}
		}
		return result;
	}
	
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
