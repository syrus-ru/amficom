/*
 * $Id: CMServerImpl.java,v 1.120 2005/11/28 12:35:02 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;


import com.syrus.AMFICOM.cmserver.corba.CMServerOperations;
import com.syrus.AMFICOM.general.IdentifierGeneratorServerCore;

/**
 * @version $Revision: 1.120 $, $Date: 2005/11/28 12:35:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module cmserver
 */

final class CMServerImpl extends IdentifierGeneratorServerCore implements CMServerOperations {
	private static final long serialVersionUID = 3760563104903672628L;

	CMServerImpl(final CMServerServantManager cmServerServantManager) {
		super(cmServerServantManager, cmServerServantManager.getCORBAServer().getOrb());
	}

}
