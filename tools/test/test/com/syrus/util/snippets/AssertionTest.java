/*
 * $Id: AssertionTest.java,v 1.2 2005/06/07 18:01:50 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.snippets;

import java.lang.reflect.*;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/06/07 18:01:50 $
 * @module tools
 */
final class AssertionTest {
	private AssertionTest() {
		assert false;
	}

	static void doSmth() {
		assert false: "We're definitely screwed...";
	}

	public static void main(final String args[]) {
		final Class clazz = AssertionTest.class;
		try {
			final Method method = clazz.getDeclaredMethod("doSmth", new Class[0]);
			method.invoke(clazz, new Object[0]);
		} catch (final InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false: message;
			} else
				ite.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
