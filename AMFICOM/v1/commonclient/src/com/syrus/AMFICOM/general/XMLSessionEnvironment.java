/*
 * $Id: XMLSessionEnvironment.java,v 1.4 2005/08/02 13:03:22 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.4 $, $Date: 2005/08/02 13:03:22 $
 * @author $Author: arseniy $
 * @module commonclient
 */
public final class XMLSessionEnvironment {
	private XMLPoolContext poolContext;

	private static XMLSessionEnvironment instance;

	private XMLSessionEnvironment(final XMLPoolContext poolContext) {
		this.poolContext = poolContext;
		this.poolContext.init();

		IdentifierPool.init(new XMLIdentifierGeneratorServer());
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
