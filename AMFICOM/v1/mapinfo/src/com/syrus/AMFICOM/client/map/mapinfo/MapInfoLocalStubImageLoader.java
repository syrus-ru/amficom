/*
 * $Id: MapInfoLocalStubImageLoader.java,v 1.1.2.1 2005/06/20 15:31:23 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.List;

import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.map.TopologicalImageQuery;

/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.1.2.1 $, $Date: 2005/06/20 15:31:23 $
 * @module mapinfo_v1
 */
public class MapInfoLocalStubImageLoader implements MapImageLoader {

    private final MapJLocalRenderer renderer;
		private final MapInfoConnection connection;
    
    public MapInfoLocalStubImageLoader(MapInfoConnection connection)
        throws MapConnectionException
    {
        this.connection = connection;

        //Setting logger to log nothing.
        System.setProperty("org.apache.commons.logging.Log","com.syrus.AMFICOM.Client.Map.EmptyLog");

        try {
        	this.renderer = new MapJLocalRenderer(this.connection.getPath() + this.connection.getView());
        } catch (IOException e) {
				e.printStackTrace();
				throw new MapConnectionException(e);
			}
    }
    
    /* (non-Javadoc)
     * @see com.syrus.AMFICOM.Client.Map.MapImageLoader#stopRenderingAtServer()
     */
    public void stopRendering() {
        try {
            this.renderer.cancelRendering();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
    }

    /* (non-Javadoc)
     * @see com.syrus.AMFICOM.Client.Map.MapImageLoader#renderMapImageAtServer(com.syrus.AMFICOM.Client.Map.TopologicalRequest)
     */
    public Image renderMapImage(TopologicalImageQuery query)
        throws MapConnectionException,MapDataException {
        //Здесь должен формироваться запрос serverQuery к пулу
        byte[] imageArray = null;        
        try {
        	imageArray = this.renderer.renderToStream(query);
        } catch (Exception e) {
            throw new MapDataException("Failed rendering to stream");
        }
        
        if (imageArray == null)
        	return null;
        
        Image imageReceived = Toolkit.getDefaultToolkit().createImage(imageArray);
        
        return imageReceived;
    }

		/* (non-Javadoc)
		 * @see com.syrus.AMFICOM.client.map.MapImageLoader#getMapConnection()
		 */
		public MapConnection getMapConnection() throws MapConnectionException
		{
			// TODO Auto-generated method stub
			return null;
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
