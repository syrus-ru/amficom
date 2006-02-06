/*-
 * $Id: EnumUtil.java,v 1.8.2.1 2006/02/06 14:46:30 arseniy Exp $
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
 * @version $Revision: 1.8.2.1 $, $Date: 2006/02/06 14:46:30 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public final class EnumUtil {
	private static final String VALUE_OF_METHOD_NAME = "valueOf";

	private static final Map<Class, Method> VALUE_OF_METHODS = new HashMap<Class, Method>();

	private EnumUtil() {
		//singleton
		assert false;
	}

	public static <E extends Enum<E>> E valueOf(final Class<E> enumClass, final int intValue) {
		if (!enumClass.isEnum()) {
			throw new IllegalArgumentException("Class not enum");
		}

		Method valueOfMethod = VALUE_OF_METHODS.get(enumClass);
		if (valueOfMethod == null) {
			valueOfMethod = reflectMethodFromInt(enumClass);
			VALUE_OF_METHODS.put(enumClass, valueOfMethod);
		}

		try {
			return (E) valueOfMethod.invoke(null, new Integer(intValue));
		} catch (Exception e) {
			Log.errorMessage(e);
			throw new IllegalArgumentException(e.getMessage(), e);
		}
			
	}

	private static Method reflectMethodFromInt(final Class enumClass) {
		try {
			return enumClass.getDeclaredMethod(VALUE_OF_METHOD_NAME, int.class);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}
}
