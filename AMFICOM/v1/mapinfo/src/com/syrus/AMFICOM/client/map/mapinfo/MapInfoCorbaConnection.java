/*
 * $Id: MapInfoCorbaConnection.java,v 1.8 2006/06/27 13:48:16 arseniy Exp $
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
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.map.corba.IdlMapDescriptor;
import com.syrus.AMFICOM.mscharserver.corba.MscharServer;
import com.syrus.AMFICOM.mscharserver.corba.MscharServerHelper;

/**
 * @version $Revision: 1.8 $, $Date: 2006/06/27 13:48:16 $
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
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.Client.Map.MapConnection#getAvailableViews()
	 */
	@Override
	public List<String> getAvailableViews() throws MapDataException {
		final List<String> listToReturn = new ArrayList<String>();
		try {
			IdlMapDescriptor[] mapDescriptors = this.mscharServer.getMapDescriptors(LoginManager.getSessionKey().getIdlTransferable());
			for (int i = 0; i < mapDescriptors.length; i++){
				listToReturn.add(mapDescriptors[i].name);
			}
		} catch (AMFICOMRemoteException e) {
			throw new MapDataException("Failed getting map descriptors list");
		}

		return listToReturn;
	}
}
