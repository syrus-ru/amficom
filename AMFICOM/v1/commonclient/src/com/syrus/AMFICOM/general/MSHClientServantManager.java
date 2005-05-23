/*
 * $Id: MSHClientServantManager.java,v 1.1 2005/05/23 07:52:14 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/23 07:52:14 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
public final class MSHClientServantManager extends VerifiedConnectionManager implements ClientServantManager, MSHServerConnectionManager {
	private String loginServerServantName;
	private String eventServerServantName;
	private String mshServerServantName;

	
}
