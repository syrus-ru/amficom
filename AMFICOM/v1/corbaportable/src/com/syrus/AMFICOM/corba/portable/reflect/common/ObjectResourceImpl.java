/*
 * $Id: ObjectResourceImpl.java,v 1.2 2004/09/25 18:06:32 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.reflect.common;

import com.syrus.AMFICOM.corba.portable.common.*;
import com.syrus.util.corba.JavaSoftORBUtil;
import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.*;
import org.omg.CORBA.UserException;
import org.omg.CosNaming.NamingContextExtHelper;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/09/25 18:06:32 $
 * @module corbaportable_v1
 */
public class ObjectResourceImpl extends ObjectResource {
	private static final Class EMPTY_FORMAL_ARGS[] = new Class[0];

	private static final Object EMPTY_ACTUAL_ARGS[] = new Object[0];

	private static final StackTraceElement EMPTY_STACK_TRACE[]
		= new StackTraceElement[0];

	private static final PortableStackTraceElement EMPTY_PORTABLE_STACK_TRACE[]
		= new PortableStackTraceElement[0]; 

	private static Class connectionClass = null;

	private static Class stackTraceElementClass = null;

	ObjectResourceImpl() {
	}

	public Identifier getId() {
		return id;
	}

	/**
	 * @todo Implement inheritance and make this method protected instance
	 *       instead of private static.
	 */
	private static org.omg.CORBA.Object getFallbackObject(final String key) throws UserException, InvocationTargetException {
		try {
			if (connectionClass == null)
				synchronized (ObjectResourceImpl.class) {
					if (connectionClass == null) {
						ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
						final String className = "com.syrus.AMFICOM.Client.General.ConnectionInterface";
						if (classLoader != null)
							connectionClass = Class.forName(className, true, classLoader);
						else
							connectionClass = Class.forName(className);
					}
				}
			return NamingContextExtHelper.narrow(JavaSoftORBUtil.getInstance().getORB().resolve_initial_references("NameService")).resolve_str(((String) (connectionClass.getMethod("getServerName", EMPTY_FORMAL_ARGS).invoke(connectionClass.getMethod("getInstance", EMPTY_FORMAL_ARGS).invoke(connectionClass, EMPTY_ACTUAL_ARGS), EMPTY_ACTUAL_ARGS))).replace('.', '_') + '/' + key);
		} catch (ClassNotFoundException cnfe) {
			/**
			 * @todo Running server-side. If necessary, get
			 *       the utility class instance directly
			 *       from the running server. 
			 */
			return null;
		} catch (NoSuchMethodException nsme) {
			/*
			 * Never.
			 */
			nsme.printStackTrace();
			return null;
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			iae.printStackTrace();
			return null;
		}
	}

	/**
	 * @todo Rewrite to make use of servers/features map.
	 * @todo Implement inheritance and make this method public instance
	 *       instead of private static.
	 */
	public static org.omg.CORBA.Object getObject(final String key) throws UserException, InvocationTargetException {
		return getFallbackObject(key);
	}

	private static PortableStackTraceElement[] box(final StackTraceElement stackTrace[]) {
		if (stackTrace == null)
			return EMPTY_PORTABLE_STACK_TRACE;
		final int stackTraceLength = stackTrace.length;
		if (stackTraceLength == 0)
			return EMPTY_PORTABLE_STACK_TRACE;
		PortableStackTraceElement returnValue[] = new PortableStackTraceElement[stackTraceLength];
		for (int i = 0; i < stackTraceLength; i++) {
			final StackTraceElement stackTraceElement = stackTrace[i];
			final String declaringClass = stackTraceElement.getClassName();
			final String methodName = stackTraceElement.getMethodName();
			final String fileName = stackTraceElement.getFileName();
			returnValue[i] = new PortableStackTraceElement(
				(declaringClass == null) ? "" : declaringClass,
				(methodName == null) ? "" : methodName,
				(fileName == null) ? "" : fileName,
				stackTraceElement.getLineNumber());
		}
		return returnValue;
	}

	private static StackTraceElement[] unbox(final PortableStackTraceElement stackTrace[]) throws Exception {
		if (stackTrace == null)
			return EMPTY_STACK_TRACE;
		final int stackTraceLength = stackTrace.length;
		if (stackTraceLength == 0)
			return EMPTY_STACK_TRACE;
		StackTraceElement returnValue[] = new StackTraceElement[stackTraceLength];
		for (int i = 0; i < stackTraceLength; i++) {
			final PortableStackTraceElement stackTraceElement = stackTrace[i];
			returnValue[i] = newStackTraceElement(stackTraceElement.declaringClass,
				stackTraceElement.methodName,
				stackTraceElement.fileName,
				stackTraceElement.lineNumber);
		}
		return returnValue;
	}

	/**
	 * Boxes an <code>SQLException</code> into
	 * a <code>DatabaseAccessException</code>. 
	 */
	public static DatabaseAccessException box(final SQLException sqle) {
		final Collection exceptionChain = new LinkedList();
		for (SQLException chainElement = sqle.getNextException(); chainElement != null; chainElement = chainElement.getNextException()) {
			final String reason = chainElement.getLocalizedMessage();
			final String sqlState = chainElement.getSQLState();
			exceptionChain.add(new DatabaseAccessException(
				(reason == null) ? "" : reason,
				box(chainElement.getStackTrace()),
				(sqlState == null) ? "" : sqlState,
				chainElement.getErrorCode(),
				new DatabaseAccessException[0]));
		}
		final String reason = sqle.getLocalizedMessage();
		final String sqlState = sqle.getSQLState();
		return new DatabaseAccessException(
			(reason == null) ? "" : reason,
			box(sqle.getStackTrace()),
			(sqlState == null) ? "" : sqlState,
			sqle.getErrorCode(),
			(DatabaseAccessException[]) (exceptionChain.toArray(new DatabaseAccessException[exceptionChain.size()])));
	}

	/**
	 * Unboxes an <code>SQLException</code> from the corresponding
	 * <code>DatabaseAccessException</code>.
	 */
	public static SQLException unbox(final DatabaseAccessException dae) {
		SQLException rootException = new SQLException(dae.reason, dae.sqlState, dae.vendorCode);
		StackTraceElement stackTrace[];
		try {
			stackTrace = unbox(dae.stackTrace);
		} catch (Exception e) {
			stackTrace = new StackTraceElement[0];
		}
		rootException.setStackTrace(stackTrace);
		
		SQLException previousException = rootException;
		DatabaseAccessException exceptionChain[] = dae.next;
		for (int i = 0; i < exceptionChain.length; i++) {
			DatabaseAccessException chainElement = exceptionChain[i];
			SQLException nextException = new SQLException(chainElement.reason, chainElement.sqlState, chainElement.vendorCode);
			StackTraceElement chainElementStackTrace[];
			try {
				chainElementStackTrace = unbox(chainElement.stackTrace);
			} catch (Exception e) {
				chainElementStackTrace = new StackTraceElement[0];
			}
			nextException.setStackTrace(chainElementStackTrace);

			previousException.setNextException(nextException);
			previousException = nextException;
		}

		return rootException;
	}

	private static StackTraceElement newStackTraceElement(final String declaringClass, final String methodName, final String fileName, final int lineNumber)
			throws ClassNotFoundException, IllegalAccessException,
			IllegalArgumentException, InstantiationException,
			InvocationTargetException, NoSuchFieldException,
			NoSuchMethodException, SecurityException {
		if (stackTraceElementClass == null)
			synchronized (ObjectResourceImpl.class) {
				if (stackTraceElementClass == null) {
					final String className = "java.lang.StackTraceElement";
					ClassLoader loader = Thread.currentThread().getContextClassLoader();
			 		if (loader == null) {
						ClassLoader.getSystemClassLoader().setClassAssertionStatus(className, false);
						stackTraceElementClass = Class.forName(className);
					} else {
						loader.setClassAssertionStatus(className, false);
						stackTraceElementClass = Class.forName(className, true, loader);
					}
				}
			}

		final String javaSpecificationVersion
			= System.getProperty("java.specification.version");

		if ((javaSpecificationVersion != null)
				&& (javaSpecificationVersion.length() == 3)
				&& (javaSpecificationVersion.charAt(0) == '1')
				&& (javaSpecificationVersion.charAt(1) == '.')) {
			char minor = javaSpecificationVersion.charAt(2);
			if (minor == '4')
				return newStackTraceElement14(declaringClass, methodName, fileName, lineNumber);
			else if (minor == '5')
				return newStackTraceElement15(declaringClass, methodName, fileName, lineNumber);
			throw new UnsupportedOperationException("Only java specifications v1.4 and 1.5 are supported.");
		}
		throw new UnsupportedOperationException("Java specification major version is not 1.");
	}

	/**
	 * Creates a stack trace element, using the parameters specified
	 * (via Reflection API, Java 1.4 only).
	 */
	private static StackTraceElement newStackTraceElement14(final String declaringClass, final String methodName, final String fileName, final int lineNumber)
			throws IllegalAccessException, IllegalArgumentException,
			InstantiationException, InvocationTargetException,
			NoSuchFieldException, NoSuchMethodException,
			SecurityException {
		Constructor ctor = stackTraceElementClass.getDeclaredConstructor(new Class[0]);
		ctor.setAccessible(true);
		StackTraceElement returnValue = (StackTraceElement) (ctor.newInstance(new Object[0]));

		Field declaringClassField = stackTraceElementClass.getDeclaredField("declaringClass");
		declaringClassField.setAccessible(true);
		declaringClassField.set(returnValue, declaringClass);

		Field methodNameField = stackTraceElementClass.getDeclaredField("methodName");
		methodNameField.setAccessible(true);
		methodNameField.set(returnValue, methodName);

		Field fileNameField = stackTraceElementClass.getDeclaredField("fileName");
		fileNameField.setAccessible(true);
		fileNameField.set(returnValue, fileName);

		Field lineNumberField = stackTraceElementClass.getDeclaredField("lineNumber");
		lineNumberField.setAccessible(true);
		lineNumberField.setInt(returnValue, lineNumber);

		return returnValue; 
	}

	/**
	 * Creates a stack trace element, using the parameters specified
	 * (via Reflection API, Java 1.5 only).
	 */
	private static StackTraceElement newStackTraceElement15(final String declaringClass, final String methodName, final String fileName, final int lineNumber)
			throws IllegalAccessException, IllegalArgumentException,
			InstantiationException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		return (StackTraceElement) (stackTraceElementClass
			.getConstructor(new Class[]{String.class, String.class, String.class, int.class})
			.newInstance(new Object[]{declaringClass, methodName, fileName, new Integer(lineNumber)}));
	}
}
