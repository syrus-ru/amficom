/*
 * $Id: MapInfoCorbaConnection.java,v 1.4 2005/08/11 12:36:44 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.MscharClientServantManager;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.mscharserver.corba.MscharServer;
import com.syrus.AMFICOM.mscharserver.corba.MscharServerHelper;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/11 12:36:44 $
 * @author $Author: arseniy $
 * @module mapinfo
 */
public class MapInfoCorbaConnection extends MapInfoConnection {
	private MscharServer mscharServer;

	@Override
	public boolean connect() throws MapConnectionException {
		final boolean flag = super.connect();
		if (flag) {
			try {
				final MscharClientServantManager mscharClientServantManager = MscharClientServantManager.create();
				final CommonServer commonServer = mscharClientServantManager.getServerReference();
				this.mscharServer = MscharServerHelper.narrow(commonServer);
			} catch (CommunicationException e) {
				throw new MapConnectionException("MapInfoCorbaConnection - failed initializing MscharClientServantManager.", e);
			}
		}
		return flag;
	}

	@Override
	public boolean release() throws MapConnectionException {
		final boolean flag = super.release();
		return flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapConnection#createImageLoader()
	 */
	@Override
	public MapImageLoader createImageLoader() throws MapConnectionException {
		return new MapInfoCorbaImageLoader(this);
	}

	public MscharServer getMscharServer() {
		return this.mscharServer;
	}
}
