/*-
 * $Id: MapInfoCorbaImageLoader.java,v 1.1.2.4 2005/06/28 11:23:09 peskovsky Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.List;

import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.AMFICOM.map.corba.IdlRenderedImage;
import com.syrus.AMFICOM.map.corba.IdlTopologicalImageQuery;
import com.syrus.AMFICOM.mscharserver.corba.MscharServer;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1.2.4 $, $Date: 2005/06/28 11:23:09 $
 * @author $Author: peskovsky $
 * @module map_v1
 */

public class MapInfoCorbaImageLoader implements MapImageLoader
{
	private final MapInfoCorbaConnection connection;
	
	MapInfoCorbaImageLoader(MapInfoConnection connection)
		throws MapConnectionException
	{
		this.connection = (MapInfoCorbaConnection)connection;
	}
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapImageLoader#stopRenderingAtServer()
	 */
	public void stopRendering()
	{
		try {
			this.connection.getMscharServer().stopRenderTopologicalImage(LoginManager.getSessionKey().getTransferable());
		} catch (AMFICOMRemoteException e) {
			Log.errorMessage("TopologicalImageLoader.loadTopologicalImage | loading has been cancelled" + e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapImageLoader#renderMapImageAtServer(com.syrus.AMFICOM.client.map.TopologicalRequest)
	 */
	public Image renderMapImage(TopologicalImageQuery query) throws MapConnectionException, MapDataException
	{
		long t0=0,t1=0,t2=0,t3=0,t4=0;
		t0 = System.currentTimeMillis();
		IdlTopologicalImageQuery transf = query.getTransferable();
		IdlSessionKey keyt = LoginManager.getSessionKeyTransferable();
		IdlRenderedImage rit;
		
		try {
			t1 = System.currentTimeMillis();
			MscharServer mscharServer= this.connection.getMscharServer();
			t2 = System.currentTimeMillis();
			rit = mscharServer.transmitTopologicalImage(transf,keyt);
			t3 = System.currentTimeMillis();
		} catch (AMFICOMRemoteException e) {
			Log.errorMessage("TopologicalImageLoader.loadTopologicalImage |" + e.getMessage());
			return null;
		}
		byte[] image = rit.image;
		if(image.length == 1) {			
			Log.debugMessage("TopologicalImageLoader.loadTopologicalImage | loading has been cancelled", Log.DEBUGLEVEL03);
			return null;
		}

		Image imageReceived = Toolkit.getDefaultToolkit().createImage(image);
		t4 = System.currentTimeMillis();
		Log.debugMessage("MapInfoCorbaImageLoader.renderMapImage | "
				+ (t1 - t0) + " (creating transf and session key)"
				+ (t2 - t1) + " (getting session ref)"
				+ (t3 - t2) + "rendering"
				+ (t4 - t3) + "creating image",
				Log.FINEST);

		return imageReceived;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapImageLoader#getMapConnection()
	 */
	public MapConnection getMapConnection() throws MapConnectionException
	{
	  return this.connection;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapImageLoader#findSpatialObjects(java.lang.String)
	 */
	public List findSpatialObjects(String searchText) throws MapConnectionException, MapDataException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
