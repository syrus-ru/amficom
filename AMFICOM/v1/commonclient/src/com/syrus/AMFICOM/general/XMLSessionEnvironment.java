/*
 * $Id: XMLSessionEnvironment.java,v 1.2 2005/07/29 14:33:41 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.2 $, $Date: 2005/07/29 14:33:41 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
public final class XMLSessionEnvironment {
	private XMLPoolContext poolContext;

	private static XMLSessionEnvironment instance;

	private XMLSessionEnvironment(final XMLPoolContext poolContext) {
		this.poolContext = poolContext;
		this.poolContext.init();

		//NOTE No initialization of IdentifierPool. Cannot generate identifiers.
	}

	public void openSession() {
		this.poolContext.deserialize();
	}

	public void closeSession() {
		this.poolContext.serialize();
	}

	public static void createInstance() {
		instance = new XMLSessionEnvironment(new XMLPoolContext());
	}

	public static XMLSessionEnvironment getInstance() {
		return instance;
	}
	
}
