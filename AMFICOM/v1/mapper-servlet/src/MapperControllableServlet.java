import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.awt.Color;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// MapInfo classes
import com.mapinfo.dp.Feature;
import com.mapinfo.dp.FeatureSet;
import com.mapinfo.mapj.MapJ;
import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.LayerType;

import com.mapinfo.util.DoublePoint;
import com.mapinfo.util.DoubleRect;

import com.mapinfo.unit.LinearUnit;

import com.mapinfo.mapxtreme.client.MapXtremeImageRenderer;
import com.mapinfo.xmlprot.mxtj.ImageRequestComposer;

/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.8 $, $Date: 2005/05/04 13:05:43 $
 * @module mapper-servlet
 */
public class MapperControllableServlet
	extends HttpServlet
{
	// Define constants to control various rendering options
	public static final int NUM_OF_COLORS = 256;
	public static final Color BACKGROUND_COLOR = Color.WHITE;
	public static final int MAXIMUM_LAYERS_COUNT = 100;	

	public static final String INIT_PARAMNAME_MAPPATH = "mappath"; //$NON-NLS-1$
	public static final String INIT_PARAMNAME_FILETOLOAD = "filetoload"; //$NON-NLS-1$
	public static final String INIT_PARAMNAME_MAPXTREME_URL = "mapxtremeurl"; //$NON-NLS-1$
	public static final String INIT_PARAMNAME_TODEBUG = "toDebug"; //$NON-NLS-1$	
	
	// TODO: Specify the physical path to the directory containing maps.
	// Or you can specify this value using an init parameter called 'mappath'.
	// Include a path separator at the end.
	private String mapPath = "";
	
	// TODO: Specify the path & name of desired map definition to load.
	// Or you can specify this value using an init parameter called 'filetoload'.
	private String fileToLoad = "";

	// TODO: Specify the URL of the MapXtremeServlet that will
	// service our mapping requests.
	// Or you can specify this value using an init parameter called 'mapxtremeurl'.
	private String mapXtremeURL = "";

	private boolean toDebug = false;
	
//	private FileOutputStream logFOS = null;	
//	private SimpleDateFormat sdFormat = new SimpleDateFormat("E M d H:m:s:S");	

	private ObjectOutputStream currentRequestOS = null;
	
	private RenderingThread renderingThread = null;
	
	/**
	 * This method initializes the servlet and then reads and sets
	 * initialization parameters
	 *
	 * @param config ServletConfig servlet initialization parameter
	 *
	 * @see com.mapinfo.mapj.MapJ
	 *
	 * @exception ServletException if detected when handling the request
	 */
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);

		Logger.log("\n\n========Servlet started at " +
			 new Date(System.currentTimeMillis()).toString() + "========\n\n");

		Logger.log("MCS - Initializing parameters...");

		// If the servlet set-up has provided initialization parameters,
		// use those parameters to override the hard-coded values
		// declared above.
		String strParam = getInitParameter(INIT_PARAMNAME_MAPPATH);
		if (strParam != null)
		{
			this.mapPath = strParam;
		}

		strParam = getInitParameter(INIT_PARAMNAME_FILETOLOAD);
		if (strParam != null)
		{
			this.fileToLoad = strParam;
		}

		strParam = getInitParameter(INIT_PARAMNAME_MAPXTREME_URL);
		if (strParam != null && strParam.length() > 0)
		{
			this.mapXtremeURL = strParam;
		}

		strParam = getInitParameter(INIT_PARAMNAME_TODEBUG);
		if (strParam != null && strParam.equals("true"))
		{
			this.toDebug = true;
//			try
//			{
//				this.logFOS = new FileOutputStream("mcs_log.txt", true);
//			} catch (FileNotFoundException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

		Logger.log("Using servlet parameters:");
		Logger.log(INIT_PARAMNAME_MAPPATH + "mapPath: " + this.mapPath);
		Logger.log(INIT_PARAMNAME_FILETOLOAD + ": " + this.fileToLoad);
		Logger.log(INIT_PARAMNAME_MAPXTREME_URL + ": " + this.mapXtremeURL);
		Logger.log(INIT_PARAMNAME_TODEBUG + ": " + Boolean.toString(this.toDebug));
	}

	/**
	 * Processes the request.
	 *
	 * @param req HttpServletRequest that encapsulates the request to
	 * the servlet
	 * @param resp HttpServletResponse that encapsulates the response
	 * from the servlet
	 *
	 * @see javax.servlet.http.HttpSession
	 * @see com.mapinfo.util , com.mapinfo.mapj.MapJ , com.mapinfo.util.graphics
	 */

	public void service(HttpServletRequest req, HttpServletResponse resp) throws
		IOException
	{
		Logger.log(null);
		Logger.log("Creating ObjectOutputStream...");
		this.currentRequestOS = new ObjectOutputStream(resp.getOutputStream());
		
		String queryString = null;
		try
		{
			// Draw the map and encode the URL
			java.net.URI uri = new java.net.URI (
					req.getRequestURL() + "?" + req.getQueryString());
			
			queryString = uri.getQuery();
			Logger.log("Decoded query: " + queryString);
		}
		catch (URISyntaxException e)
		{
			writeNLogError(ServletCommandNames.ERROR_INVALID_PARAMETERS);
			return;
		}
		
		Map requestParameters = this.searchParameters(queryString);
		
		if (this.renderingThread == null)
		{
			Logger.log("Creating RenderingThread instance at MCS.");
			this.renderingThread = new RenderingThread();
			Logger.log("RenderingThread instance is created at MCS. " + this.renderingThread);			
		}

		try
		{
			//Getting command name
			String commandName = this.getParameter(requestParameters,ServletCommandNames.COMMAND_NAME);
			Logger.log("commandName = " + commandName);
			
			if (commandName.equals(ServletCommandNames.CN_START_RENDER_IMAGE))			
			{
				Logger.log ("Before getting rendition parameters");				
				String widthString = this.getParameter(requestParameters,ServletCommandNames.PAR_WIDTH);
				String heightString = this.getParameter(requestParameters,ServletCommandNames.PAR_HEIGHT);
				String centerXString = this.getParameter(requestParameters,ServletCommandNames.PAR_CENTER_X);
				String centerYString = this.getParameter(requestParameters,ServletCommandNames.PAR_CENTER_Y);
				String zoomString = this.getParameter(requestParameters,ServletCommandNames.PAR_ZOOM_FACTOR);
				
				if (		widthString.equals("NaN")
						||	heightString.equals("NaN")
						||	centerXString.equals("NaN")
						||	centerYString.equals("NaN")						
						||	zoomString.equals("NaN"))
				{
					writeNLogError(ServletCommandNames.ERROR_INVALID_PARAMETERS);
					return;
				}
					
				int width = Integer.parseInt(widthString);
				Logger.log("width = " + Integer.toString(width));
				int height = Integer.parseInt(heightString);
				Logger.log("height = " + Integer.toString(height));
				if (! ( (width > 0) && (height > 0)))
				{
					writeNLogError(ServletCommandNames.ERROR_INVALID_PARAMETERS);
					return;
				}

				double centerX = Double.parseDouble(centerXString);
				Logger.log("centerX = " + Double.toString(centerX));
				double centerY = Double.parseDouble(centerYString);
				Logger.log("centerY = " + Double.toString(centerY));
				double zoom = Double.parseDouble(zoomString);
				Logger.log("zoom = " + Double.toString(zoom));
				
				//Getting layers' parameters
				boolean[] layersVisibilities = new boolean[MapperControllableServlet.MAXIMUM_LAYERS_COUNT * 2];
				int layerParamIndex = 0;
				for (;;layerParamIndex++)
				{
					String lvString = this.getParameter(requestParameters,ServletCommandNames.PAR_LAYER_VISIBLE +
																  Integer.toString(layerParamIndex));
					String lvvString = this.getParameter(requestParameters,ServletCommandNames.PAR_LAYER_LABELS_VISIBLE +
																	Integer.toString(layerParamIndex));

					if (lvString == null)
						break;

					layersVisibilities[2 * layerParamIndex] = (Integer.parseInt(lvString) > 0) ? true : false; // to hold request parameter
					layersVisibilities[2 * layerParamIndex + 1] = (Integer.parseInt(lvvString) > 0) ? true : false; // to hold request parameter
					Logger.log("layer's Index/Visible/Labels" + " = " + layerParamIndex +
							 " / " + layersVisibilities[2 * layerParamIndex] +
							 " / " + layersVisibilities[2 * layerParamIndex + 1]);
				}
				
				Logger.log ("Before starting rendition " + this.renderingThread);				
				this.renderingThread.startProcessing(
						width,
						height,
						zoom,
						centerX,
						centerY,
						layerParamIndex,layersVisibilities);
				Logger.log ("Rendition started.");				
			}
			else if (commandName.equals(ServletCommandNames.CN_CANCEL_RENDERING))			
			{
				Logger.log("Before canceling rendition");				
				this.renderingThread.cancelRendering();
			}
			else if (commandName.equals(ServletCommandNames.CN_GET_RENDITION))			
			{
				Logger.log("Before getting rendition");
				if (this.renderingThread.isMapRendered())
					this.renderingThread.getMapRenditionInto(this.currentRequestOS);
				else
					writeNLogError(ServletCommandNames.ERROR_NO_MAP_RENDERED);
			}
			else if (commandName.equals(ServletCommandNames.CN_SEARCH_NAME))			
			{
				String nameToSearch = this.getParameter(requestParameters,ServletCommandNames.PAR_NAME_TO_SEARCH);
				Logger.log("nameToSearch = " + nameToSearch);
				
				this.renderingThread.writeNamesNCenters(nameToSearch,this.currentRequestOS);
			}
			else
			{
				Logger.log(commandName + " is not a valid command name!");
				writeNLogError(ServletCommandNames.ERROR_INVALID_PARAMETERS);
			}
		}
		catch (NullPointerException npExc)
		{
			Logger.log("Null Pointer Exception was detected!");
			writeNLogError(ServletCommandNames.ERROR_NO_PARAMETERS);
			return;
		}
		catch (NumberFormatException nfException)
		{
			Logger.log("Number Format Exception was detected!");			
			writeNLogError(ServletCommandNames.ERROR_INVALID_PARAMETERS);
			return;
		}
	}

	/**
	 *  This method returns an About string for this servlet
	 */
	public String getServletInfo()
	{
		return "Server-side controllable map servlet";
	}

	/**
	 * Выделяет имена параметров и их значений в строке запроса и помещает
	 * их в структуру HashMap 
	 * @param queryString строка запроса
	 * @return HashMap с парами "имя параметра - значение"
	 */
	private Map searchParameters(String queryString)
	{
		Map result = new HashMap();
		
		int lastParamEndIndex = -1;
		for (;;)
		{
			int dividerIndex = queryString.indexOf('=',lastParamEndIndex);
			String paramName = queryString.substring(lastParamEndIndex + 1,dividerIndex);
			lastParamEndIndex = queryString.indexOf('&',dividerIndex);
			
			String paramValue = null;
			
			if (lastParamEndIndex < 0)
			{
				paramValue = queryString.substring(dividerIndex + 1,queryString.length());
				result.put(paramName,paramValue);
				break;
			}

			paramValue = queryString.substring(dividerIndex + 1,lastParamEndIndex);
			result.put(paramName,paramValue);
		}
		
		return result;
	}
	
	private String getParameter(Map table, String paramName)
	{
		Object paramValue = table.get(paramName);
		if (paramValue != null)
			return (String) paramValue;
		
		return null;
	}
	
//	/**
//	 * Записывает сообщения в конец файла "mcs_log.txt", указывая дату их записи.
//	 * Файл расположен в $MapXtremeHome/bin.
//	 */
//	public void log(String msg)
//	{
//		if (!this.toDebug)
//			return;
//
//		synchronized (this.logFOS)
//		{
//			try
//			{
//				if (msg == null)
//				{
//					this.logFOS.write("\n".getBytes());
//				}
//				else
//				{
//					String dateString = this.sdFormat.format(new Date(System.currentTimeMillis()));
//					this.logFOS.write((dateString + "  MCS - " + msg + "\n").getBytes());
//				}
//				this.logFOS.flush();
//			}
//			catch (IOException exc)
//			{
//				//Ошибка при выводе логов
//			}
//		}
//	}

	public void writeNLogError(String error) throws
		IOException
	{
		Logger.log("ERROR!!! - " + error);
		this.currentRequestOS.writeObject(error);
	}
	
	/**
	 * @return Returns the mapXtremeURL.
	 */
	public String getMapXtremeURL()
	{
		return this.mapXtremeURL;
	}
	public String getFileToLoad()
	{
		return this.fileToLoad;
	}
	public void setFileToLoad(String fileToLoad)
	{
		this.fileToLoad = fileToLoad;
	}
	public String getMapPath()
	{
		return this.mapPath;
	}
	public void setMapPath(String mapPath)
	{
		this.mapPath = mapPath;
	}
}

class RenderingThread extends Thread
{
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
	private boolean isMapRendered = false;
	
//	/**
//	 * Флаг окончания работы потока
//	 */
//	private boolean toBreak = false;
	
	public RenderingThread ()
	{
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

class Logger
{
	private static SimpleDateFormat sdFormat = new SimpleDateFormat("E M d H:m:s:S");
	private static FileOutputStream logFOS = null;	
	
	public static synchronized void log(String msg)
	{
		if (Logger.logFOS == null)
		{
			try
			{
				Logger.logFOS = new FileOutputStream("mcs_log.txt", true);
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
			}
		}
		
		try
		{
			if (msg == null)
			{
				Logger.logFOS.write("\n".getBytes());
			}
			else
			{
				String dateString = Logger.sdFormat.format(new Date(System.currentTimeMillis()));
				Logger.logFOS.write((dateString + "  MCS - " + msg + "\n").getBytes());
			}
			Logger.logFOS.flush();
			
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException exc)
		{
			//Ошибка при выводе логов
		}
	}
}