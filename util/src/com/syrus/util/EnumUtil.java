/*-
 * $Id: EnumUtil.java,v 1.3 2005/08/28 16:39:52 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/28 16:39:52 $
 * @author $Author: arseniy $
 * @module util
 */
public final class EnumUtil {
	private static final String METHOD_NAME_FROM_INT = "fromInt";

	private static final Map<Class, Method> FROM_INT_METHODS = new HashMap<Class, Method>();

	private EnumUtil() {
		//singleton
		assert false;
	}

	public static int getCode(final Enum<?> e) {
		return (e instanceof Codeable) ? ((Codeable) e).getCode() : e.ordinal();
	}

	public static <E extends Enum<E>> E reflectFromInt(final Class<E> enumClass, final int intValue) {
		if (!enumClass.isEnum()) {
			throw new IllegalArgumentException("Class not enum");
		}

		Method fromIntMethod = FROM_INT_METHODS.get(enumClass);
		if (fromIntMethod == null) {
			fromIntMethod = reflectMethodFromInt(enumClass);
			FROM_INT_METHODS.put(enumClass, fromIntMethod);
		}

		try {
			return (E) fromIntMethod.invoke(null, new Integer(intValue));
		} catch (Exception e) {
			Log.errorException(e);
			throw new IllegalArgumentException(e.getMessage(), e);
		}
			
	}

	private static Method reflectMethodFromInt(final Class enumClass) {
		try {
			return enumClass.getDeclaredMethod(METHOD_NAME_FROM_INT, Integer.TYPE);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}
}
