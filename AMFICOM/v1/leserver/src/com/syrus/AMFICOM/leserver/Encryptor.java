/*
 * $Id: Encryptor.java,v 1.1 2005/05/03 10:33:06 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/03 10:33:06 $
 * @author $Author: arseniy $
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
