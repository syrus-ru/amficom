/*
 * $Id: AuroraORBUtil.java,v 1.3 2004/06/01 14:09:14 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.corba;

import java.util.Properties;
import oracle.aurora.jndi.orb_dep.Orb;

/**
 * @version $Revision: 1.3 $, $Date: 2004/06/01 14:09:14 $
 * @author $Author: bass $
 * @module util
 */
public final class AuroraORBUtil extends VisiBrokerORBUtil {
	private static final ORBUtil INSTANCE = new AuroraORBUtil();

	private AuroraORBUtil() {
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
		orb = Orb.init(properties);
	}

	synchronized void initORBSingleton() {
		if (orbSingleton != null)
			return;
		System.setProperty("org.omg.CORBA.ORBClass",
			"com.visigenic.vbroker.orb.ORB");
		System.setProperty("org.omg.CORBA.ORBSingletonClass",
			"com.visigenic.vbroker.orb.ORB");
		orbSingleton = Orb.init();
	}
}
