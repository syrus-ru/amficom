/*-
 * $Id: MapInfoCorbaImageLoader.java,v 1.1.2.1 2005/06/20 15:31:23 peskovsky Exp $
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
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.AMFICOM.map.corba.RenderedImage_Transferable;
import com.syrus.AMFICOM.map.corba.TopologicalImageQuery_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2005/06/20 15:31:23 $
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
		SessionKey_Transferable keyt = LoginManager.getSessionKeyTransferable();
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
		SessionKey_Transferable keyt = LoginManager.getSessionKeyTransferable();
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
