/*
 * $Id: VisiBrokerORBUtil.java,v 1.3 2004/07/30 10:40:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.corba;

import java.util.Properties;
import org.omg.CORBA.ORB;

/**
 * @version $Revision: 1.3 $, $Date: 2004/07/30 10:40:58 $
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
		if (this.orb != null)
			return;
		Properties properties = new Properties();
		properties.put("org.omg.CORBA.ORBClass",
			"com.visigenic.vbroker.orb.ORB");
		properties.put("org.omg.CORBA.ORBSingletonClass",
			"com.visigenic.vbroker.orb.ORB");
		this.orb = ORB.init(new String[0], properties);
	}

	synchronized void initORBSingleton() {
		if (this.orbSingleton != null)
			return;
		System.setProperty("org.omg.CORBA.ORBClass",
			"com.visigenic.vbroker.orb.ORB");
		System.setProperty("org.omg.CORBA.ORBSingletonClass",
			"com.visigenic.vbroker.orb.ORB");
		this.orbSingleton = ORB.init();
	}
}
