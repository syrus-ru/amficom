/*-
 * $Id: EnumUtil.java,v 1.1 2005/08/26 18:02:24 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/08/26 18:02:24 $
 * @author $Author: arseniy $
 * @module general
 */
public final class EnumUtil {
	private static final String METHOD_NAME_FROM_INT = "fromInt";

	private static final Map<Class, Method> FROM_INT_METHODS = new HashMap<Class, Method>();

	private EnumUtil() {
		//singleton
		assert false;
	}

	public static <E extends Enum<E>> E reflectFromInt(final Class<E> enumClass, final int intValue) throws IllegalDataException {
		if (!enumClass.isEnum()) {
			throw new IllegalDataException("Class not enum");
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
			throw new IllegalDataException(e.getMessage(), e);
		}
			
	}

	private static Method reflectMethodFromInt(final Class enumClass) throws IllegalDataException {
		try {
			return enumClass.getMethod(METHOD_NAME_FROM_INT);
		} catch (Exception e) {
			throw new IllegalDataException(e.getMessage(), e);
		}
	}
}
