/*
 * $Id: JavaSoftORBUtil.java,v 1.1 2004/05/06 11:48:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.corba;

import com.syrus.util.prefs.IIOPConnectionManager;
import java.util.Properties;
import org.omg.CORBA.ORB;

/**
 * @version $Revision: 1.1 $, $Date: 2004/05/06 11:48:10 $
 * @author $Author: bass $
 * @module util
 */
public final class JavaSoftORBUtil extends ORBUtil {
	private static final ORBUtil INSTANCE = new JavaSoftORBUtil();

	private static final String ORB_CLASS;

	private static final String ORB_SINGLETON_CLASS;

	static {
		String javaSpecificationVersion
			= System.getProperty("java.specification.version");
		if ((javaSpecificationVersion != null)
				&& (javaSpecificationVersion.length() == 3)
				&& (javaSpecificationVersion.charAt(0) == '1')
				&& (javaSpecificationVersion.charAt(1) == '.')) {
			char minor = javaSpecificationVersion.charAt(2);
			if (minor == '4') {
				ORB_CLASS = "com.sun.corba.se.internal.Interceptors.PIORB";
				ORB_SINGLETON_CLASS
					= "com.sun.corba.se.internal.corba.ORBSingleton";
			} else if (minor == '5') {
				ORB_CLASS = "com.sun.corba.se.impl.orb.ORBImpl";
				ORB_SINGLETON_CLASS = "com.sun.corba.se.impl.orb.ORBSingleton";
			} else
				throw new UnsupportedOperationException(
					"Only java specifications v1.4 and 1.5 are supported.");
		} else
			throw new UnsupportedOperationException(
				"Java specification major version is not 1.");

		Class clazz = IIOPConnectionManager.class;
	}

	private JavaSoftORBUtil() {
	}

	public static ORBUtil getInstance() {
		return INSTANCE;
	}

	synchronized void initORB() {
		if (orb != null)
			return;
		Properties properties = new Properties();
		properties.put("org.omg.CORBA.ORBClass", ORB_CLASS);
		properties.put("org.omg.CORBA.ORBSingletonClass", ORB_SINGLETON_CLASS);
		properties.put("org.omg.CORBA.ORBInitialHost",
			IIOPConnectionManager.getORBInitialHost());
		properties.put("org.omg.CORBA.ORBInitialPort",
			String.valueOf(IIOPConnectionManager.getORBInitialPort()));
		orb = ORB.init(new String[0], properties);
		Thread thread = new Thread() {
			public void run() {
				orb.run();
			}
		};
		thread.setDaemon(true);
		thread.setName(ORB_CLASS);
		thread.start();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				orb.shutdown(true);
			}
		});
	}

	synchronized void initORBSingleton() {
		if (orbSingleton != null)
			return;
		System.setProperty("org.omg.CORBA.ORBClass", ORB_CLASS);
		System.setProperty("org.omg.CORBA.ORBSingletonClass",
			ORB_SINGLETON_CLASS);
		System.setProperty("org.omg.CORBA.ORBInitialHost",
			IIOPConnectionManager.getORBInitialHost());
		System.setProperty("org.omg.CORBA.ORBInitialPort",
			String.valueOf(IIOPConnectionManager.getORBInitialPort()));
		orbSingleton = ORB.init();
	}
}
