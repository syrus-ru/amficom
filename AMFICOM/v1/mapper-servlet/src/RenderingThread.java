import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mapinfo.dp.Feature;
import com.mapinfo.dp.FeatureSet;
import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.LayerType;
import com.mapinfo.mapj.MapJ;
import com.mapinfo.mapxtreme.client.MapXtremeImageRenderer;
import com.mapinfo.unit.LinearUnit;
import com.mapinfo.util.DoublePoint;
import com.mapinfo.util.DoubleRect;
import com.mapinfo.xmlprot.mxtj.ImageRequestComposer;

/**
 * $Id: RenderingThread.java,v 1.1 2005/05/04 14:53:45 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */

class RenderingThread extends Thread
{
	/**
	 * 
	 */
	private MapperControllableServlet servlet = null;
	private MapXtremeImageRenderer renderer = null;
	private MapJ mapJObject = null;	
	
	/**
	 * Буфер для хранения готового изображения
	 */
	private ByteArrayOutputStream readyImageOutputStream = null;
	
	/**
	 * Буфер для рендеринга
	 */
	private ByteArrayOutputStream currentlyRenderingOutputStream = null;
	
	
	/**
	 * Флаг показывает - происходит ли рендеринг в текущий момент
	 */
	private boolean isProcessing = false;

	/**
	 * Флаг показывает есть ли в буфере отображённая карта
	 */
	boolean isMapRendered = false;
	
//	/**
//	 * Флаг окончания работы потока
//	 */
//	private boolean toBreak = false;
	
	public RenderingThread (MapperControllableServlet servlet)
	{
		this.servlet = servlet;
		try
		{
			Logger.log("RunningThread - Constructor - Initializing MapJ.");			
			this.mapJObject = this.initMapJ();
			
			Logger.log("RunningThread - Constructor - Creating MapXtreme renderer.");			
			this.renderer = new MapXtremeImageRenderer(this.servlet.getMapXtremeURL());
			Logger.log("RunningThread - Constructor - MapXtreme renderer created.");
		}catch (Throwable e)
		{
			Logger.log(e.getMessage());
		}
	}
	
	public void run()
	{
//		while (!this.toBreak)
//		{
//			while (this.isProcessing == false)
//				try
//				{
//					Thread.sleep(10);
//				} catch (InterruptedException e1)
//				{
//					// TODO Auto-generated catch block
//					Logger.log(e1.getMessage());
//				}
			
			//Отображаем карту и записывает её в поток данных
			Logger.log("RunningThread - run - Rendering map.");
			
			this.currentlyRenderingOutputStream = new ByteArrayOutputStream();
			
			try
			{
				this.renderer.render(ImageRequestComposer.create(
					this.mapJObject,
					MapperControllableServlet.NUM_OF_COLORS,
					MapperControllableServlet.BACKGROUND_COLOR,
					"image/gif"));
			
				Logger.log("RunningThread - run - Writing rendition to buffer.");
				
				//Output the map
				this.renderer.toStream(this.currentlyRenderingOutputStream);
			}
			catch (Exception e)
			{
				Logger.log(e.getMessage());
			}

			this.readyImageOutputStream = this.currentlyRenderingOutputStream;			
			
			this.isProcessing = false;
			this.isMapRendered = true;
			
			Logger.log("RunningThread - run - Map rendered.");
			synchronized(this.servlet.getState()) {
				this.servlet.setState(MapperControllableServlet.STATE_RENDERED);
			}
//		}
	}
	
	public void cancel()
	{
//		this.toBreak = true;
	}
	
	public void cancelRendering()
	{
		Logger.log("RunningThread - cancelRendering - Stopping the rendering of map.");		
		try
		{
			if (!this.renderer.isDone())
				this.renderer.interrupt();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			Logger.log(e.getMessage());
		}
		this.servlet.log("RunningThread - cancelRendering - Rendering stopped.");		
	}

	public boolean isMapRendered()
	{
		return this.isMapRendered;
	}
	
	public void getMapRenditionInto(OutputStream out)
	{
		Logger.log("RunningThread - getMapRendition - Writing the rendition to stream.");		
		try
		{
			this.readyImageOutputStream.writeTo(out);
			Logger.log("RunningThread - getMapRendition - Succesfully written.");			
			this.isMapRendered = false;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			Logger.log(e.getMessage());			
		}
	}
	
	public void startProcessing(
			int width,
			int height,
			double scale,
			double centerX,
			double centerY,
			int layersCount,
			boolean[] layersVisibilities)
	{
		while (this.isProcessing == true)
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				Logger.log(e1.getMessage());				
			}

		this.isProcessing = true;
		
		//Setting size, zoom and center point	
		this.setSize(width, height);
		this.setCenter(new DoublePoint(centerX, centerY));
		this.setScale(scale);
		
		for (int i = 0; i < layersCount; i++)
			this.setLayerVisibility(
					i,
					layersVisibilities[2*i],
					layersVisibilities[2*i + 1]);
		
		Logger.log("Starting RenderingThread.");			
		this.start();
		Logger.log("RenderingThread started.");			
	}
	
//---------------------------------------Функции, работающие с MapJ --------------------------
	
	/**
	 * Создаёт объект MapJ и загружает картографические данные. 
	 * @return Готовый к использованию объект MapJ для работы с
	 *  картографическими данными
	 * @throws IOException
	 */
	public MapJ initMapJ() throws IOException
	{
		Logger.log("RunningThread - Initializing MapJ instance...");
		// instantiate a MapJ and set the bounds
		MapJ returnValue = new MapJ(); // this MapJ object

		// Query for image locations and load the geoset
		try
		{
			Logger.log("RunningThread - Loading geoset...");
			if (this.servlet.getFileToLoad().endsWith(".gst"))
			{
				returnValue.loadGeoset(this.servlet.getFileToLoad(), this.servlet.getMapPath(), null);
				Logger.log("RunningThread - Geoset " + this.servlet.getFileToLoad() + " has been loaded.");
			}
			else
			{
				returnValue.loadMapDefinition(this.servlet.getFileToLoad());
				Logger.log("RunningThread - Map definition " + this.servlet.getFileToLoad() + " has been loaded.");
			}
		}
		catch (IOException e)
		{
			Logger.log("RunningThread - ERROR!!! - Can't load geoset: " + this.servlet.getFileToLoad());
			throw e;
		}
		
		returnValue.setDistanceUnits(LinearUnit.meter);
		
		return returnValue;
	}
	
	/**
	 * Установить размер выходного изображения
	 * @param width Ширина
	 * @param height Высота
	 */
	public void setSize(int width, int height)
	{
		synchronized (this.mapJObject)
		{
			Logger.log("RunningThread - Setting size");		
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
			Logger.log("RunningThread - Setting center");		
			try
			{
				this.mapJObject.setCenter(new com.mapinfo.util.DoublePoint(center.x, center.y));
			}
			catch (Exception e)
			{
				Logger.log("RunningThread - ERROR!!! - Failed setting center.");
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
			Logger.log("RunningThread - Setting scale");		
			try
			{
				if (scale != 0.0D)
					this.mapJObject.setZoom(scale);
			}
			catch (Exception e)
			{
				Logger.log("RunningThread - ERROR!!! - Failed setting scale.");
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
				
				Logger.log("RunningThread - Setting visibility for layer " + layer.getName());
				
				layer.setEnabled(layerVisible);
				layer.setAutoLabel(layerLabelsVisible);
			}
			catch (Exception exc)
			{
				Logger.log("RunningThread - ERROR!!! - Failed setting layer visibility.");
			}
		}
	}
	
	/**
	 * Пишет в выходной поток результаты case-insensitive поиска по подстроке.
	 * @param nameToSearch искомая подстрока
	 */
	public void writeNamesNCenters(String nameToSearch,ObjectOutputStream out)
	{
		synchronized (this.mapJObject)
		{
			Logger.log ("Starting search procedure.");
			Iterator layersIt = this.mapJObject.getLayers().iterator(
					LayerType.FEATURE);
			
			Logger.log ("Got layers Iterator.");		
			for(;layersIt.hasNext();)
			{
				FeatureLayer currLayer = (FeatureLayer )layersIt.next();
	
				Logger.log ("Searching at FeatureLayer: " + currLayer.getName());	
				
				try
				{
					// Названия всех колонок - чтобы достать инфу об объекте
					// Может они и не понадобятся!!!!!!!!
					List resultColumnNames = new ArrayList();
					
					// Название колонки с надписями
					List labelColumnsList = currLayer.getLabelProperties().getLabelColumns();
					
					if (labelColumnsList.isEmpty())
					{
						Logger.log ("No labels' column at the layer.");					
						continue;
					}
					
					String labelColumnName = (String )labelColumnsList.get(0);
					
					Logger.log ("Got labels' column name: " + labelColumnName);
					
					resultColumnNames.add(labelColumnName);
	
					FeatureSet fs = currLayer.searchAll(
						resultColumnNames,
						null);
		
					Logger.log ("Got feature set.");
			
					Feature feature = null;
					// Loop until FeatureSet.getNextFeature() returns null
					while((feature = fs.getNextFeature()) != null)
					{
						String featureName = feature.getAttribute(0).getString();
						
						if (featureName.toLowerCase().indexOf(nameToSearch.toLowerCase()) < 0)
							continue;
							
						Logger.log ("Got feature name: " + featureName);
						
						DoublePoint featureCentre = feature.getGeometry().getBounds().center();
						
						out.writeDouble(featureCentre.x);
						out.writeDouble(featureCentre.y);					
						out.writeObject(featureName);
					}
					
					Logger.log ("Results for the layer succesfully wrote.");				
				}
				catch(Exception exc)
				{
					Logger.log("ERROR!!! - Failed searching at layer \"" +	currLayer.getName() +
							"\" with message \"" + exc.getMessage() + "\".");
				}
			}
		}
	}
	
	public String toString()
	{
		return "RenderingThread exists.";
	}
}