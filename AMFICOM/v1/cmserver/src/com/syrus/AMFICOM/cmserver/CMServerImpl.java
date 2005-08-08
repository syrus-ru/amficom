/*
 * $Id: CMServerImpl.java,v 1.118 2005/08/08 11:44:39 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;


import com.syrus.AMFICOM.cmserver.corba.CMServerOperations;
import com.syrus.AMFICOM.general.IdentifierGeneratorServerCore;

/**
 * @version $Revision: 1.118 $, $Date: 2005/08/08 11:44:39 $
 * @author $Author: arseniy $
 * @module cmserver
 */

final class CMServerImpl extends IdentifierGeneratorServerCore implements CMServerOperations {
	private static final long serialVersionUID = 3760563104903672628L;

	CMServerImpl() {
		super(CMServerSessionEnvironment.getInstance().getConnectionManager(),
				CMServerSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb());
	}

}
