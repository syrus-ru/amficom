/*
 * $Id: XMLSessionEnvironment.java,v 1.3 2005/08/01 14:07:44 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.3 $, $Date: 2005/08/01 14:07:44 $
 * @author $Author: arseniy $
 * @module commonclient_v1
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
