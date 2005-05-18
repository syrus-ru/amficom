/*
 * $Id: NameServiceIORPrinter.java,v 1.5 2005/05/18 10:49:17 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.corba;

import org.omg.CORBA.*;

/**
 * The class is intended for use primarily with the NetBeans IDE. It just prints
 * out the IOR of the CORBA Naming Service, according to current user
 * preferences.
 *
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/05/18 10:49:17 $
 * @module util
 */
final class NameServiceIORPrinter {
	private NameServiceIORPrinter() {
		assert false;
	}

	public static void main(String args[]) throws UserException {
		ORB orb = JavaSoftORBUtil.getInstance().getORB();
		System.out.println(orb.object_to_string(
			orb.resolve_initial_references("NameService")));
	}
}
