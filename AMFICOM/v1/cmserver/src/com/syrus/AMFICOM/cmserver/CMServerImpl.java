/*
 * $Id: CMServerImpl.java,v 1.117 2005/07/28 16:02:12 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;


import com.syrus.AMFICOM.cmserver.corba.CMServerOperations;
import com.syrus.AMFICOM.general.IdentifierGeneratorServerCore;

/**
 * @version $Revision: 1.117 $, $Date: 2005/07/28 16:02:12 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */

final class CMServerImpl extends IdentifierGeneratorServerCore implements CMServerOperations {
	private static final long serialVersionUID = 3760563104903672628L;

	CMServerImpl() {
		super(CMServerSessionEnvironment.getInstance().getConnectionManager(),
				CMServerSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb());
	}

}
