/*
 * $Id: NameServiceIORPrinter.java,v 1.3 2004/06/01 14:09:15 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.corba;

import org.omg.CORBA.*;

/**
 * The class is intended for use primarily with the NetBeans IDE. It just prints
 * out the IOR of the CORBA Naming Service, according to current user
 * preferences.
 *
 * @version $Revision: 1.3 $, $Date: 2004/06/01 14:09:15 $
 * @author $Author: bass $
 * @module util-test
 */
final class NameServiceIORPrinter {
	private NameServiceIORPrinter() {
	}

	public static void main(String args[]) throws UserException {
		ORB orb = JavaSoftORBUtil.getInstance().getORB();
		System.out.println(orb.object_to_string(
			orb.resolve_initial_references("NameService")));
	}
}
