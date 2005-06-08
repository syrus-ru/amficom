/*
 * $Id: ORBUtil.java,v 1.5 2005/06/08 13:49:06 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.corba;

import org.omg.CORBA.ORB;

/**
 * @version $Revision: 1.5 $, $Date: 2005/06/08 13:49:06 $
 * @author $Author: bass $
 * @deprecated
 * @module util
 */
public abstract class ORBUtil {
	volatile ORB orb;

	volatile ORB orbSingleton;

	ORBUtil() {
		// empty
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
