/*
 * $Id: Encryptor.java,v 1.1 2006/06/22 12:55:11 cvsadmin Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.systemserver;

/**
 * @version $Revision: 1.1 $, $Date: 2006/06/22 12:55:11 $
 * @author $Author: cvsadmin $
 * @author Tashoyan Arseniy Feliksovich
 * @module systemserver
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
