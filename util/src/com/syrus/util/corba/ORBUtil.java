/*
 * $Id: ORBUtil.java,v 1.3 2004/07/30 10:40:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.corba;

import org.omg.CORBA.ORB;

/**
 * @version $Revision: 1.3 $, $Date: 2004/07/30 10:40:58 $
 * @author $Author: bass $
 * @module util
 */
public abstract class ORBUtil {
	volatile ORB orb;

	volatile ORB orbSingleton;

	ORBUtil() {
	}

	public final ORB getORB() {
		if (this.orb == null)
			initORB();
		return this.orb;
	}

	abstract void initORB();

	public final ORB getORBSingleton() {
		if (this.orbSingleton == null)
			initORBSingleton();
		return this.orbSingleton;
	}

	abstract void initORBSingleton();
}
