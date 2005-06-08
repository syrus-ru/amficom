/*-
 * $Id: MapInfoCorbaMapImageLoader.java,v 1.1.2.4 2005/06/08 12:10:28 peskovsky Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.mapinfo;

//import com.syrus.AMFICOM.general.MSHClientServantManager;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Iterator;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.client.map.TopologicalRequest;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.MscharClientServantManager;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.AMFICOM.map.corba.RenderedImage_Transferable;
import com.syrus.AMFICOM.map.corba.TopologicalImageQuery_Transferable;
import com.syrus.AMFICOM.mscharserver.corba.MscharServer;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1.2.4 $, $Date: 2005/06/08 12:10:28 $
 * @author $Author: peskovsky $
 * @module map_v1
 */

public class MapInfoCorbaMapImageLoader
	implements MapImageLoader
{
	private MscharServer mscharServer;
	
  private MapInfoLogicalNetLayer logicalNetLayer = null;
  
  /**
   * Buffer for visibility of layers (to prevent creating array each time we render map)
   */
  private boolean[] layerVisibilities = null;
  /**
   * Buffer for visibility of labels at the layers
   */
  private boolean[] labelVisibilities = null;    
	
	
	MapInfoCorbaMapImageLoader(MapInfoLogicalNetLayer layer)
		throws CommunicationException, MapDataException
	{
		this.logicalNetLayer = layer;
		
    int layersCount = this.logicalNetLayer.getMapViewer().getLayers().size();
    this.layerVisibilities = new boolean[layersCount];
    this.labelVisibilities = new boolean[layersCount];        
		
		MscharClientServantManager mscharClientServantManager = MscharClientServantManager.create();
		CommonServer commonServer = mscharClientServantManager.getServerReference();
		this.mscharServer = (MscharServer) commonServer;
	}
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapImageLoader#stopRenderingAtServer()
	 */
	public void stopRenderingAtServer()
	{
		SessionKey_Transferable keyt = new SessionKey_Transferable();
		try {
			this.mscharServer.stopRenderTopologicalImage(LoginManager.getSessionKey().hashCode(), keyt);
		} catch (AMFICOMRemoteException e) {
			Log.errorMessage("TopologicalImageLoader.loadTopologicalImage | loading has been cancelled" + e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapImageLoader#renderMapImageAtServer(com.syrus.AMFICOM.client.map.TopologicalRequest)
	 */
	public ImageIcon renderMapImageAtServer(TopologicalRequest request) throws MapConnectionException, MapDataException
	{
    Dimension size = this.logicalNetLayer.getMapViewer().getVisualComponent().getSize();
    
    //Setting layers' and labels' visibilities
    Iterator layersIt = this.logicalNetLayer.getMapViewer().getLayers().iterator();
    int index = 0;
    for(; layersIt.hasNext();)
    {
        SpatialLayer spL = (SpatialLayer )layersIt.next();
        
        // Видимость слоя зависит от того, хочет ли его видеть клиент, виден ли он
        // при текущем масштабе на сервере
        // и надо ли отображать объекты для текущего запроса
        this.layerVisibilities[index] =    spL.isVisible()
            && spL.isVisibleAtScale(this.logicalNetLayer.getScale());
    
        // то же самое для надписей
        this.labelVisibilities[index] = spL.isLabelVisible()
            && spL.isVisibleAtScale(this.logicalNetLayer.getScale());
        
        index++;
    }

    //Формируется запрос serverQuery к пулу
    TopologicalImageQuery serverQuery = new TopologicalImageQuery(
            size.width,
            size.height,
            request.getTopoCenter().getX(),
            request.getTopoCenter().getY(),
            request.getTopoScale(),
            this.layerVisibilities,
            this.labelVisibilities,
            LoginManager.getSessionKey().hashCode());
		
		TopologicalImageQuery_Transferable transf = serverQuery.getTransferable();
		SessionKey_Transferable keyt = new SessionKey_Transferable();
		RenderedImage_Transferable rit;
		try {
			rit = this.mscharServer.transmitTopologicalImage(transf,keyt);
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

		return new ImageIcon(imageReceived);

	}
	
	
}
