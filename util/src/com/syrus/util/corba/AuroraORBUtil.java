package com.syrus.util.corba;

import com.visigenic.vbroker.orb.ORB;
import oracle.aurora.jndi.orb_dep.Orb;

/**
 * @version $Revision: 1.1 $, $Date: 2004/05/01 17:45:22 $
 * @author $Author: cvsadmin $
 * @module util
 */
public final class AuroraORBUtil {
	/**
	 * Since we need a reference to the ORB singleton, this variable is static.
	 */
	private static ORB orb = null;

	private AuroraORBUtil() {
	}

	/**
	 * Stores the reference to client ORB in a class variable.
	 */
	private static void initORB() {
		if (orb == null)
			orb = Orb.init();
	}

	public static ORB getORB() {
		initORB();
		return orb;
	}
}
