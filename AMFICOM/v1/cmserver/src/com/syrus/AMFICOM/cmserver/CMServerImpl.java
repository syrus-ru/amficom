/*
 * $Id: CMServerImpl.java,v 1.115 2005/07/28 10:22:45 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;


import com.syrus.AMFICOM.cmserver.corba.CMServerOperations;
import com.syrus.AMFICOM.general.ServerCore;

/**
 * @version $Revision: 1.115 $, $Date: 2005/07/28 10:22:45 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */

public final class CMServerImpl extends ServerCore implements CMServerOperations {
	private static final long serialVersionUID = 3760563104903672628L;

	CMServerImpl() {
		super(CMServerSessionEnvironment.getInstance().getConnectionManager(),
				CMServerSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb());
	}

}
