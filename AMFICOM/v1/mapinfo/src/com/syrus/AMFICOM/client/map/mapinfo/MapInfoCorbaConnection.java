/*
 * $Id: MapInfoCorbaConnection.java,v 1.8.2.1 2006/06/27 17:07:13 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.general.ClientServantManager;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.MapClientServantManager;
import com.syrus.AMFICOM.general.MapServerConnectionManager;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.map.corba.IdlMapDescriptor;
import com.syrus.AMFICOM.systemserver.corba.MapServer;

/**
 * @version $Revision: 1.8.2.1 $, $Date: 2006/06/27 17:07:13 $
 * @author $Author: arseniy $
 * @module mapinfo
 */
public class MapInfoCorbaConnection extends MapInfoConnection {
	private MapServerConnectionManager mapServerConnectionManager;

	@Override
	public boolean connect() throws MapConnectionException {
		final boolean flag = super.connect();
		if (flag) {
			final ClientServantManager clientServantManager = ClientSessionEnvironment.getInstance().getConnectionManager();
			if (!(clientServantManager instanceof MapClientServantManager)) {
				throw new IllegalStateException("Must establish Map session");
			}
			this.mapServerConnectionManager = (MapServerConnectionManager) clientServantManager;
		}
		return flag;
	}

	@Override
	public MapImageLoader createImageLoader() throws MapConnectionException {
		return new MapInfoCorbaImageLoader(this);
	}

	public MapServerConnectionManager getMapServerConnectionManager() {
		return this.mapServerConnectionManager;
	}

	@Override
	public List<String> getAvailableViews() throws MapDataException {
		try {
			final List<String> mapDescriptors = new ArrayList<String>();
			final MapServer mapServer = this.mapServerConnectionManager.getMapServerReference();
			final IdlMapDescriptor[] idlMapDescriptors = mapServer.getMapDescriptors(LoginManager.getSessionKey().getIdlTransferable());
			for (int i = 0; i < idlMapDescriptors.length; i++){
				mapDescriptors.add(idlMapDescriptors[i].name);
			}
			return mapDescriptors;
		} catch (CommunicationException ce) {
			throw new MapDataException("Failed getting map descriptors list", ce);
		} catch (AMFICOMRemoteException are) {
			throw new MapDataException("Failed getting map descriptors list -- " + are.message);
		}
	}
}
