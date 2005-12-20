/*-
 * $Id: MServerImpl.java,v 1.5 2005/12/20 09:21:25 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import com.syrus.AMFICOM.general.IdentifierGeneratorServerCore;
import com.syrus.AMFICOM.mserver.corba.MServerOperations;

/**
 * @version $Revision: 1.5 $, $Date: 2005/12/20 09:21:25 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mserver
 */
final class MServerImpl extends IdentifierGeneratorServerCore implements MServerOperations {
	private static final long serialVersionUID = -2000155795996275898L;

	MServerImpl(final MServerServantManager mServerServantManager) {
		super(mServerServantManager, mServerServantManager.getCORBAServer().getOrb());
	}

}
