/*-
 * $Id: MapInfoCorbaImageLoader.java,v 1.1.2.3 2005/06/22 08:55:01 peskovsky Exp $
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
import com.syrus.AMFICOM.map.corba.RenderedImage_Transferable;
import com.syrus.AMFICOM.map.corba.TopologicalImageQuery_Transferable;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1.2.3 $, $Date: 2005/06/22 08:55:01 $
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
		IdlSessionKey keyt = LoginManager.getSessionKeyTransferable();
		try {
			this.connection.getMscharServer().stopRenderTopologicalImage(LoginManager.getSessionKey().hashCode(), keyt);
		} catch (AMFICOMRemoteException e) {
			Log.errorMessage("TopologicalImageLoader.loadTopologicalImage | loading has been cancelled" + e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapImageLoader#renderMapImageAtServer(com.syrus.AMFICOM.client.map.TopologicalRequest)
	 */
	public Image renderMapImage(TopologicalImageQuery query) throws MapConnectionException, MapDataException
	{
		query.setUserID(LoginManager.getSessionKey().hashCode());
		
		TopologicalImageQuery_Transferable transf = query.getTransferable();
		IdlSessionKey keyt = LoginManager.getSessionKeyTransferable();
		RenderedImage_Transferable rit;
		try {
			rit = this.connection.getMscharServer().transmitTopologicalImage(transf,keyt);
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
