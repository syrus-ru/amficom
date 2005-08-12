/*
 * $Id: XMLSessionEnvironment.java,v 1.5 2005/08/12 10:09:32 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.5 $, $Date: 2005/08/12 10:09:32 $
 * @author $Author: bob $
 * @module commonclient
 */
public final class XMLSessionEnvironment {
	private XMLPoolContext poolContext;

	private static XMLSessionEnvironment instance;

	private XMLSessionEnvironment(final XMLPoolContext poolContext) {
		this.poolContext = poolContext;
		this.poolContext.init();

		IdentifierPool.init(new XMLIdentifierGeneratorServer(poolContext.getObjectLoader()));
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
