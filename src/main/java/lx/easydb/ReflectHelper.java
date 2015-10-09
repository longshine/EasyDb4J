package lx.easydb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("rawtypes")
public final class ReflectHelper {
	public static final Class[] EMPTY_CLASSES = new Class[0];
	public static final Object[] EMPTY_PARAMS = EMPTY_CLASSES;
	
	public static Object invokeSilent(Object obj, String methodName,
			Class[] parameterTypes, Object[] args) {
		try {
			return invoke(obj, methodName, parameterTypes, args);
		} catch (Exception e) {
			// ignore
			return null;
		} 
	}

	public static Object invoke(Object obj, String methodName,
			Class[] parameterTypes, Object[] args)
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (parameterTypes == null)
			parameterTypes = EMPTY_CLASSES;
		Method method = obj.getClass().getMethod(methodName, parameterTypes);
		if (args == null)
			args = EMPTY_PARAMS;
		return method.invoke(obj, args);
	}
}
