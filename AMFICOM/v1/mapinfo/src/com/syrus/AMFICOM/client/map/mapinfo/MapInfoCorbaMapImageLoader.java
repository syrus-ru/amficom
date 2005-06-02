/*
 * $Id: MapInfoCorbaMapImageLoader.java,v 1.1.2.1 2005/06/02 12:14:04 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Map.Mapinfo;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.MapImageLoader;
import com.syrus.AMFICOM.Client.Map.ServletCommandNames;
import com.syrus.AMFICOM.Client.Map.SpatialLayer;
import com.syrus.AMFICOM.Client.Map.TopologicalRequest;

import com.syrus.AMFICOM.map.TopologicalImageQuery;

/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.1.2.1 $, $Date: 2005/06/02 12:14:04 $
 * @module mapinfo_v1
 */
public class MapInfoCorbaMapImageLoader
    implements MapImageLoader {

    private MapJLocalRenderer renderer = null;
    private MapInfoLogicalNetLayer logicalNetLayer = null;
    
    /**
     * Buffer for visibility of layers (to prevent creating array each time we render map)
     */
    private boolean[] layerVisibilities = null;
    /**
     * Buffer for visibility of labels at the layers
     */
    private boolean[] labelVisibilities = null;    
    
    public MapInfoCorbaMapImageLoader(MapInfoLogicalNetLayer lnLayer)
        throws IOException, MapDataException
    {
        this.logicalNetLayer = lnLayer;

        int layersCount = this.logicalNetLayer.getMapViewer().getLayers().size();
        this.layerVisibilities = new boolean[layersCount];
        this.labelVisibilities = new boolean[layersCount];        
        
        //Setting logger to log nothing.
        System.setProperty("org.apache.commons.logging.Log","com.syrus.AMFICOM.Client.Map.EmptyLog");
        
        MapInfoConnection connection = (MapInfoConnection)this.logicalNetLayer.getMapViewer().getConnection();
        this.renderer = new MapJLocalRenderer(connection.getPath() + connection.getView());
        
        
    }
    
    /* (non-Javadoc)
     * @see com.syrus.AMFICOM.Client.Map.MapImageLoader#stopRenderingAtServer()
     */
    public void stopRenderingAtServer() {
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
    public ImageIcon renderMapImageAtServer(TopologicalRequest request)
        throws MapConnectionException,MapDataException {
        //The size of result image
        Dimension size = this.logicalNetLayer.getMapViewer().getVisualComponent().getSize();
        
        //Setting layers' and labels' visibilities
        Iterator layersIt = this.logicalNetLayer.getMapViewer().getLayers().iterator();
        int index = 0;
        for(; layersIt.hasNext();)
        {
            SpatialLayer spL = (SpatialLayer )layersIt.next();
            
            // ��������� ���� ������� �� ����, ����� �� ��� ������ ������, ����� �� ��
            // ��� ������� �������� �� �������
            // � ���� �� ���������� ������� ��� �������� �������
            this.layerVisibilities[index] =    spL.isVisible()
                && spL.isVisibleAtScale(this.logicalNetLayer.getScale());
        
            // �� �� ����� ��� ��������
            this.labelVisibilities[index] = spL.isLabelVisible()
                && spL.isVisibleAtScale(this.logicalNetLayer.getScale());
            
            index++;
        }

        //����� ������ ������������� ������ serverQuery � ����
        TopologicalImageQuery serverQuery = new TopologicalImageQuery(
                size.width,
                size.height,
                request.getTopoCenter().getX(),
                request.getTopoCenter().getY(),
                request.getTopoScale(),
                this.layerVisibilities,
                this.labelVisibilities,
                this.logicalNetLayer.getContext().getSessionInterface().getAccessIdentity().sess_id,
                ServletCommandNames.CN_START_RENDER_IMAGE);

//        //���� ���������� ��� �������� ��������� ��������
//        this.renderer.setRenderingParameters(
//                size.width,
//                size.height,
//                request.getTopoScale(),
//                request.getTopoCenter().getX(),
//                request.getTopoCenter().getY(),
//                this.layerVisibilities,
//                this.labelVisibilities);
        
        byte[] imageArray = null;        
        try {
        	imageArray = this.renderer.renderToStream(serverQuery);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new MapDataException("Failed rendering to stream");
        }
        
        if (imageArray == null)
        	return null;
        
        Image imageReceived = Toolkit.getDefaultToolkit().createImage(imageArray);
        
        return new ImageIcon(imageReceived);
    }

}
