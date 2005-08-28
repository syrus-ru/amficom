/*-
 * $Id: EnumUtil.java,v 1.2 2005/08/28 13:45:59 arseniy Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/08/28 13:45:59 $
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
			return (E) fromIntMethod.invoke(new Integer(intValue));
		} catch (Exception e) {
			Log.errorException(e);
			throw new IllegalArgumentException(e.getMessage(), e);
		}
			
	}

	private static Method reflectMethodFromInt(final Class enumClass) {
		try {
			return enumClass.getMethod(METHOD_NAME_FROM_INT);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}
}
