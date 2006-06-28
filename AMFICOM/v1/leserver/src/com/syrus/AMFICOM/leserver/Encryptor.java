/*
 * $Id: Encryptor.java,v 1.4 2005/09/14 18:18:39 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

/**
 * @version $Revision: 1.4 $, $Date: 2005/09/14 18:18:39 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
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
