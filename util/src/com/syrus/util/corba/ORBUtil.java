/*
 * $Id: ORBUtil.java,v 1.2 2004/06/01 14:09:15 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.corba;

import org.omg.CORBA.ORB;

/**
 * @version $Revision: 1.2 $, $Date: 2004/06/01 14:09:15 $
 * @author $Author: bass $
 * @module util
 */
public abstract class ORBUtil {
	volatile ORB orb;

	volatile ORB orbSingleton;

	ORBUtil() {
	}

	public final ORB getORB() {
		if (orb == null)
			initORB();
		return orb;
	}

	abstract void initORB();

	public final ORB getORBSingleton() {
		if (orbSingleton == null)
			initORBSingleton();
		return orbSingleton;
	}

	abstract void initORBSingleton();
}
