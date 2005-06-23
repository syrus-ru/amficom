/*
 * $Id: MapJLocalRenderer.java,v 1.1.2.3 2005/06/23 11:12:39 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mapinfo.graphics.LocalRenderer;
import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.LayerType;
import com.mapinfo.mapj.MapJ;
import com.mapinfo.unit.LinearUnit;
import com.mapinfo.util.DoublePoint;
import com.mapinfo.util.DoubleRect;
import com.mapinfo.xmlprot.mxtj.ImageRequestComposer;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.util.Log;

/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.1.2.3 $, $Date: 2005/06/23 11:12:39 $
 * @module mapinfo_v1
 */
public class MapJLocalRenderer
{
		public static final int NUM_OF_COLORS = 256;
		public static final Color BACKGROUND_COLOR = Color.WHITE;
	
    /**
     * MapInfo data to image renderer
     */
    private LocalRenderer renderer = null;
    
    /**
     * Image where the map will be renderered
     */
    private BufferedImage imageBuffer = null;
    
    /**
     * MapInfo topological information container
     */
    private MapJ mapJObject = null;
    
    /**
     * Is set into true to prevent creating image from data that is not rendered up to finish
     */
    private boolean stopRendering = false;
    
    public MapJLocalRenderer(
            String fileToLoad)
        throws IOException  
    {
        Log.debugMessage("RunningThread - Constructor - Initializing MapJ.",Log.DEBUGLEVEL10);         
        this.mapJObject = MapJLocalRenderer.createMapJ(fileToLoad);
    }
    
    /**
     * Создаёт объект MapJ и загружает картографические данные. 
     * @return Готовый к использованию объект MapJ для работы с
     *  картографическими данными
     * @throws IOException
     */
    public static MapJ createMapJ(String fileToLoad)
            throws IOException {
        Log.debugMessage("RunningThread - Initializing MapJ instance...",Log.DEBUGLEVEL10);
        // instantiate a MapJ and set the bounds
        MapJ returnValue = new MapJ(); // this MapJ object

        // Query for image locations and load the geoset
        try {
            Log.debugMessage("RunningThread - Loading geoset...",Log.DEBUGLEVEL10);
            returnValue.loadMapDefinition(fileToLoad);
            Log.debugMessage("RunningThread - Map definition " + fileToLoad
                    + " has been loaded.",Log.DEBUGLEVEL10);
        } catch (IOException e) {
            Log.debugMessage("RunningThread - ERROR!!! - Can't load geoset: "
                    + fileToLoad,Log.DEBUGLEVEL10);
            throw e;
        }

        returnValue.setDistanceUnits(LinearUnit.meter);

        return returnValue;
    }
    
    public MapJ getMapJObject()
    {
        return this.mapJObject;
    }
    public void setMapJObject(MapJ mapJObject)
    {
        this.mapJObject = mapJObject;
    }
    public LocalRenderer getRenderer()
    {
        return this.renderer;
    }
    public void setRenderer(LocalRenderer renderer)
    {
        this.renderer = renderer;
    }
    
    public byte[] renderToStream(TopologicalImageQuery query)
        throws Exception
    {
    	this.stopRendering = false;
    	
    	int miWidth = query.getMapImageWidth();
    	int miHeight = query.getMapImageHeight();
      //Setting size, zoom and center point
      if (  (this.imageBuffer == null)
          ||(this.imageBuffer.getWidth() != miWidth)
          ||(this.imageBuffer.getHeight() != miHeight))
      {
         this.imageBuffer = new BufferedImage(
         		miWidth,
         		miHeight,
         		BufferedImage.TYPE_USHORT_565_RGB);
         Log.debugMessage("RunningThread - Constructor - Creating MapXtreme renderer.",Log.DEBUGLEVEL10);           
         this.renderer = new LocalRenderer(this.imageBuffer);
         Log.debugMessage("RunningThread - Constructor - MapXtreme renderer created.",Log.DEBUGLEVEL10);
      }
      
      this.setSize(miWidth,miHeight);
      this.setCenter(new DoublePoint(query.getTopoCenterX(), query.getTopoCenterY()));
      this.setScale(query.getTopoScale());
      
      boolean[] layerVisibilities = query.getLayerVisibilities();
      boolean[] labelVisibilities = query.getLabelVisibilities();      
      
      for (int i = 0; i < query.getLayerVisibilities().length; i++)
          this.setLayerVisibility(
                  i,
                  layerVisibilities[i],
                  labelVisibilities[i]);
      
    	
      if (this.renderer == null)
      {
          Log.debugMessage("RunningThread - Constructor - Renderer is not initialized. " +
                     "Run setRenderingParameters first.",Log.DEBUGLEVEL10);
          throw new Exception();
      }
      
      Log.debugMessage("MapJRenderer - renderToStream - Before rendering.",Log.DEBUGLEVEL10);
      
      this.renderer.render(ImageRequestComposer.create(
              this.mapJObject,
              NUM_OF_COLORS,
              BACKGROUND_COLOR,
              "image/png"));
      
      if (this.stopRendering)
      	return null;
      
      //Output the map as a GIF Image
      ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
      Log.debugMessage("MapJRenderer - renderToStream - Encoding image.",Log.DEBUGLEVEL10);        
      ImageIO.write(this.imageBuffer,"PNG",resultStream);
      
      resultStream.flush();        
      resultStream.close();
      
      byte[] result = resultStream.toByteArray();
      Log.debugMessage("MapJRenderer - renderToStream - Successfully rendered.",Log.DEBUGLEVEL10);
      return result;
    }
    
    public void cancelRendering()
        throws Exception
    {
        Log.debugMessage("MapJRenderer - cancelRendering - Stopping the rendering of map.",Log.DEBUGLEVEL10);  
        
        this.stopRendering = true;
        this.renderer.interrupt();
        
        Log.debugMessage("MapJRenderer - cancelRendering - Rendering stopped.",Log.DEBUGLEVEL10);      
    }
    
//---------------------------------------Функции, работающие с MapJ --------------------------
    /**
     * Установить размер выходного изображения
     * @param width Ширина
     * @param height Высота
     */
    public void setSize(int width, int height)
    {
        synchronized (this.mapJObject)
        {
            Log.debugMessage("RunningThread - Setting size",Log.DEBUGLEVEL10);     
            this.mapJObject.setDeviceBounds(new DoubleRect(0, 0, width, height));
        }
    }
    
    /**
     * Установить центральную точку вида карты
     * @param center Топологические координаты центральной точки
     */
    public void setCenter(DoublePoint center)
    {
        synchronized (this.mapJObject)
        {
            Log.debugMessage("RunningThread - Setting center",Log.DEBUGLEVEL10);       
            try
            {
                this.mapJObject.setCenter(new com.mapinfo.util.DoublePoint(center.x, center.y));
            }
            catch (Exception e)
            {
                Log.debugMessage("RunningThread - ERROR!!! - Failed setting center.",Log.DEBUGLEVEL10);
            }
        }
    }

    /**
     * Установить заданный масштаб вида карты
     * @param scale Массштаб для карты
     */
    public void setScale(double scale)
    {
        synchronized (this.mapJObject)
        {
            Log.debugMessage("RunningThread - Setting scale",Log.DEBUGLEVEL10);        
            try
            {
                if (scale != 0.0D)
                    this.mapJObject.setZoom(scale);
            }
            catch (Exception e)
            {
                Log.debugMessage("RunningThread - ERROR!!! - Failed setting scale.",Log.DEBUGLEVEL10);
            }
        }
    }

    /**
     * @param layerIndex Индекс слоя, которому устанавливаются параметры видимости
     * @param layerVisible Видимость слоя
     * @param layerLabelsVisible Видимость надписей данного слоя
     */
    public void setLayerVisibility(
        int layerIndex,
        boolean layerVisible,
        boolean layerLabelsVisible)
    {
        synchronized (this.mapJObject)
        {
            try
            {
                FeatureLayer layer = (FeatureLayer)this.mapJObject.getLayers().get(
                    layerIndex, LayerType.FEATURE);
                
                Log.debugMessage("RunningThread - Setting visibility for layer " + layer.getName(),Log.DEBUGLEVEL10);
                
                layer.setEnabled(layerVisible);
                layer.setAutoLabel(layerLabelsVisible);
            }
            catch (Exception exc)
            {
                Log.debugMessage("RunningThread - ERROR!!! - Failed setting layer visibility.",Log.DEBUGLEVEL10);
            }
        }
    }
}
