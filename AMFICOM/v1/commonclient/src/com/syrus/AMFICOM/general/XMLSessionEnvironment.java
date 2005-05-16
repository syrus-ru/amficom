/*
 * $Id: XMLSessionEnvironment.java,v 1.1 2005/05/16 15:56:28 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/16 15:56:28 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
public final class XMLSessionEnvironment {
	private XMLPoolContext poolContext;

	private static XMLSessionEnvironment instance;

	private XMLSessionEnvironment(XMLPoolContext poolContext) {
		this.poolContext = poolContext;

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
