/*
 * $Id: VisiBrokerORBUtil.java,v 1.1 2004/05/06 11:48:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.corba;

import java.util.Properties;
import org.omg.CORBA.ORB;

/**
 * @version $Revision: 1.1 $, $Date: 2004/05/06 11:48:10 $
 * @author $Author: bass $
 * @module util
 */
public class VisiBrokerORBUtil extends ORBUtil {
	private static final ORBUtil INSTANCE = new VisiBrokerORBUtil();

	VisiBrokerORBUtil() {
	}

	public static ORBUtil getInstance() {
		return INSTANCE;
	}

	synchronized void initORB() {
		if (orb != null)
			return;
		Properties properties = new Properties();
		properties.put("org.omg.CORBA.ORBClass",
			"com.visigenic.vbroker.orb.ORB");
		properties.put("org.omg.CORBA.ORBSingletonClass",
			"com.visigenic.vbroker.orb.ORB");
		orb = ORB.init(new String[0], properties);
	}

	synchronized void initORBSingleton() {
		if (orbSingleton != null)
			return;
		System.setProperty("org.omg.CORBA.ORBClass",
			"com.visigenic.vbroker.orb.ORB");
		System.setProperty("org.omg.CORBA.ORBSingletonClass",
			"com.visigenic.vbroker.orb.ORB");
		orbSingleton = ORB.init();
	}
}
