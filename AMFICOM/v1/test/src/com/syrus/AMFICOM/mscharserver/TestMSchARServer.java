/*
 * $Id: TestMSchARServer.java,v 1.1 2005/06/28 10:08:32 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mscharserver;

import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.AMFICOM.map.corba.IdlRenderedImage;
import com.syrus.AMFICOM.mscharserver.corba.MscharServer;
import com.syrus.AMFICOM.mscharserver.corba.MscharServerHelper;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Application;

public final class TestMSchARServer extends TestCase {

	public void testConnect() throws ApplicationException, AMFICOMRemoteException {
		Application.init("test");

		final String hostname = "aldan";
		final String servantName = "MSchARServer";

		final String rootContextName = ContextNameFactory.generateContextName(hostname);
		final CORBAServer corbaServer = new CORBAServer(rootContextName);
		final MscharServer mscharServerRef = MscharServerHelper.narrow(corbaServer.resolveReference(servantName));

		boolean[] layerVis = {false,true,false};
		final IdlSessionKey idlSessionKey = new IdlSessionKey("asdasd");
		final TopologicalImageQuery tiq = new TopologicalImageQuery(1000, 10000, 37, 55, 2000, layerVis, layerVis);

		final IdlRenderedImage idlRenderedImage = mscharServerRef.transmitTopologicalImage(tiq.getTransferable(), idlSessionKey);
		System.out.println("Loaded: " + idlRenderedImage.image.length + " bytes");
	}
}
