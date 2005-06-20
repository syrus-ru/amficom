package com.syrus.AMFICOM.client.map.mapinfo;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.MscharClientServantManager;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.mscharserver.corba.MscharServer;

public class MapInfoCorbaConnection extends MapInfoConnection
{
	private MscharServer mscharServer;

	public boolean connect() throws MapConnectionException
	{
		boolean flag = super.connect();
		if(flag) {
			try {
				MscharClientServantManager mscharClientServantManager = MscharClientServantManager.create();
				CommonServer commonServer = mscharClientServantManager.getServerReference();
				this.mscharServer = (MscharServer) commonServer;
			} catch (CommunicationException e) {
				throw new MapConnectionException("MapInfoCorbaConnection - failed initializing MscharClientServantManager.", e);
			}
		}
		return flag;
	}

	public boolean release() throws MapConnectionException
	{
		boolean flag = super.release();

		return flag;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapConnection#createImageLoader()
	 */
	public MapImageLoader createImageLoader() throws MapConnectionException
	{
		return new MapInfoCorbaImageLoader(this);
	}
	public MscharServer getMscharServer()
	{
		return this.mscharServer;
	}
}
