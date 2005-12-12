/*-
* $Id: AbstractExtensionHandler.java,v 1.2 2005/12/12 13:40:13 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.extensions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.syrus.util.Log;


/**
 * @version $Revision: 1.2 $, $Date: 2005/12/12 13:40:13 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module extensions
 */
public abstract class AbstractExtensionHandler<T extends ExtensionPoint> implements ExtensionHandler<T> {
	
	private static final String INVALID_UNDERLYING_IMPLEMENTATION = 
		"Invalid underlying implementation: ";
	
	protected Object loadHandler(final String clazz,
			final Class[] parameterTypes,
			final Object[] initargs) {
    	final String className = clazz;
    	try {
			final Constructor ctor = Class.forName(className).getDeclaredConstructor(parameterTypes);
			return ctor.newInstance(initargs);
		} catch (final ClassNotFoundException cnfe) {
			Log.errorMessage("Class " + className
					+ " not found on the classpath");
		} catch (final ClassCastException cce) {
			Log.errorMessage("Caught an ClassCastException");
		} catch (final NoSuchMethodException nsme) {
			Log.errorMessage(INVALID_UNDERLYING_IMPLEMENTATION 
					+ "class "
					+ className 
					+ " doesn't have the constructor expected");
		} catch (final InstantiationException ie) {
			Log.errorMessage(INVALID_UNDERLYING_IMPLEMENTATION 
					+ "class "
					+ className 
					+ " is abstract");
		} catch (final InvocationTargetException ite) {
			ite.printStackTrace();
			final Throwable cause = ite.getCause();
			Log.errorMessage(cause);
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else {
				Log.errorMessage(INVALID_UNDERLYING_IMPLEMENTATION
						+ "constructor throws an exception in class "
						+ className);
			}
		} catch (final IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.errorException(iae);
			Log.errorMessage("Caught an IllegalAccessException");
		} catch (final IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.errorException(iae);			
			Log.errorMessage("Caught an IllegalArgumentException");
		} catch (final SecurityException se) {
			/*
			 * Never.
			 */
			Log.errorException(se);
			Log.errorMessage("Caught a SecurityException");
		}
		return null;
    }
}

