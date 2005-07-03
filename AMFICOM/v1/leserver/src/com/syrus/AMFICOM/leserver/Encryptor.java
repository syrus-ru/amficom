/*
 * $Id: Encryptor.java,v 1.2 2005/05/18 13:29:31 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/18 13:29:31 $
 * @author $Author: bass $
 * @module leserver_v1
 */
final class Encryptor {
	private Encryptor() {
		//Singleton
		assert false;
	}

	protected static String crypt(String password) {
		//TODO Implement
		return password;
	}
}
