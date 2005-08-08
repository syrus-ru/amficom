/*
 * $Id: Encryptor.java,v 1.3 2005/08/08 11:42:21 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:42:21 $
 * @author $Author: arseniy $
 * @module leserver
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
