import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.awt.Color;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;

import java.util.Date;

// MapInfo classes
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
 * @version $Revision: 1.3 $, $Date: 2005/02/02 13:51:03 $
 * @module mapper-servlet
 */
public class MapperControllableServlet
	extends HttpServlet
{
	// TODO: Specify the physical path to the directory containing maps.
	// Or you can specify this value using an init parameter called 'mappath'.
	// Include a path separator at the end.

	private static String mapPath = "E:\\Map\\Mif"; //$NON-NLS-1$

	// TODO: Specify the path & name of desired map definition to load.
	// Or you can specify this value using an init parameter called 'filetoload'.
	private static String fileToLoad = "E:\\Map\\Mif\\mif.mdf"; //$NON-NLS-1$

	// TODO: Specify the URL of the MapXtremeServlet that will
	// service our mapping requests.
	// Or you can specify this value using an init parameter called 'mapxtremeurl'.
	private static String mapXtremeURL =
		"http://localhost:8081/mapxtreme45/servlet/mapxtreme"; //$NON-NLS-1$

	// TODO (optional): Set m_debug to true if you want error messages to include
	// debugging information, or false otherwise.  Or you can set this variable
	// by setting a 'debug' init parameter to 'true'.
	private boolean toDebug = false;

	private MapJ mapJObject = null;

	// Define constants to control various rendering options
	public static final int NUM_OF_COLORS = 256;
	public static final Color BACKGROUND_COLOR = Color.blue;

	public static final String ERROR_INVALID_PARAMETERS = "invalid_params"; //$NON-NLS-1$
	public static final String ERROR_NO_PARAMETERS = "no_params"; //$NON-NLS-1$
	public static final String ERROR_MAP_EXCEPTION = "map_exception"; //$NON-NLS-1$

	public static final String INIT_PARAM_MAPPATH = "mappath"; //$NON-NLS-1$
	public static final String INIT_PARAM_FILETOLOAD = "filetoload"; //$NON-NLS-1$
	public static final String INIT_PARAM_MAPXTREME_URL = "mapxtremeurl"; //$NON-NLS-1$
	public static final String INIT_PARAM_TODEBUG = "toDebug"; //$NON-NLS-1$	
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

		log("\n\n========New session (" +
			 new Date(System.currentTimeMillis()).toString() + ")========\n\n");

		log("MCS - Initializing parameters...");

		// If the servlet set-up has provided initialization parameters,
		// use those parameters to override the hard-coded values
		// declared above.
		String strParam = getInitParameter(INIT_PARAM_MAPPATH);
		if (strParam != null)
		{
			mapPath = strParam;
		}

		strParam = getInitParameter(INIT_PARAM_FILETOLOAD);
		if (strParam != null)
		{
			fileToLoad = strParam;
		}

		strParam = getInitParameter(INIT_PARAM_MAPXTREME_URL);
		if (strParam != null && strParam.length() > 0)
		{
			mapXtremeURL = strParam;
		}

		strParam = getInitParameter(INIT_PARAM_TODEBUG);
		if (strParam != null)
		{
			this.toDebug = true;
		}

		log("Using servlet parameters:");
		log(INIT_PARAM_MAPPATH + "mapPath: " + mapPath);
		log(INIT_PARAM_FILETOLOAD + ": " + fileToLoad);
		log(INIT_PARAM_MAPXTREME_URL + ": " + mapXtremeURL);
		log(INIT_PARAM_TODEBUG + ": " + Boolean.toString(this.toDebug));
	}

	/**
	 * This method sets previous state information, checks if any map
	 * tools were used, resets the map extents if necessary, renders the map
	 * and renders the map and then finally encodes the output HTML with a
	 * link to the rendered gif file.
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
		// Draw the map and encode the URL
		log("MCS - Got URL..." + req.getRequestURI());
		log("MCS - Creating ObjectOutputStream...");
		ObjectOutputStream oos = new ObjectOutputStream(resp.getOutputStream());

		if (this.mapJObject == null)
		{
			this.mapJObject = this.initMapJ();
		}

		try
		{
			//Setting size of picture to return
			int width = Integer.parseInt(req.getParameter(ServletCommandNames.
				WIDTH));
			log("MCS - width = " + Integer.toString(width));
			int height = Integer.parseInt(req.getParameter(ServletCommandNames.
				HEIGHT));
			log("MCS - height = " + Integer.toString(height));

			log("MCS - Setting device bounds");

			this.mapJObject.setDeviceBounds(new DoubleRect(0, 0, width, height));
			if (! ( (width > 0) && (height > 0)))
			{
				writeNLogError(MapperControllableServlet.ERROR_INVALID_PARAMETERS, oos);
				return;
			}

			//Setting zoom and center point
			double centerX = Double.parseDouble(req.getParameter(
				ServletCommandNames.
				CENTER_X));
			log("MCS - centerX = " + Double.toString(centerX));
			double centerY = Double.parseDouble(req.getParameter(
				ServletCommandNames.
				CENTER_Y));
			log("MCS - centerY = " + Double.toString(centerY));
			double zoom = Double.parseDouble(req.getParameter(
				ServletCommandNames.ZOOM_FACTOR));
			log("MCS - zoom = " + Double.toString(zoom));

			log("MCS - Setting center");
			setCenter(new DoublePoint(centerX, centerY));
			log("MCS - Setting scale");
			setScale(zoom);

			//Getting layers' parameters
			for (int i = 0; ; i++)
			{
				String lvString = req.getParameter(ServletCommandNames.
															  LAYER_VISIBLE +
															  Integer.toString(i));
				String lvvString = req.getParameter(ServletCommandNames.
																LAYER_LABELS_VISIBLE +
																Integer.toString(i));

				if (lvString == null)
					break;

				boolean layerVisible = (Integer.parseInt(lvString) > 0) ? true : false; // to hold request parameter
				boolean layerLabelsVisible = (Integer.parseInt(lvvString) > 0) ? true : false; // to hold request parameter

				log("MCS - " + "layer's Index/Visible/Labels" + " = " + i +
					 " / " + layerVisible + " / " + layerLabelsVisible);

				setLayerVisibility(i, layerVisible, layerLabelsVisible);
			}

			log("MCS - Rendering map");
			writeMap(oos);
		}

		catch (NullPointerException npExc)
		{
			writeNLogError(MapperControllableServlet.ERROR_NO_PARAMETERS, oos);
			return;
		}
		catch (NumberFormatException nfException)
		{
			writeNLogError(MapperControllableServlet.ERROR_INVALID_PARAMETERS, oos);
			return;
		}
	}

	/**
	 *  This method returns an About string for this servlet
	 */
	public String getServletInfo()
	{
		return "Client-side controllable map servlet";
	}

	/**
	 * Создаёт объект MapJ и загружает картографические данные. 
	 * @return Готовый к использованию объект MapJ для работы с
	 *  картографическими данными
	 * @throws IOException
	 */
	public MapJ initMapJ() throws IOException
	{
		log("MCS - Initializing MapJ instance...");
		// instantiate a MapJ and set the bounds
		MapJ myMap = new MapJ(); // this MapJ object

		// Query for image locations and load the geoset
		try
		{
			log("MCS - Loading geoset...");
			if (fileToLoad.endsWith(".gst"))
			{
				myMap.loadGeoset(fileToLoad, mapPath, null);
				log("MCS - Geoset " + fileToLoad + " has been loaded.");
			}
			else
			{
				myMap.loadMapDefinition(fileToLoad);
				log("MCS - Map definition " + fileToLoad + " has been loaded.");
			}
		}
		catch (IOException e)
		{
			log("MCS - Can't load geoset: " + fileToLoad);
			throw e;
		}
		
		myMap.setDistanceUnits(LinearUnit.meter);
		
		return myMap;
	}

//---------------------------------------Функции, работающие с MapJ --------------------------

	/**
	 * Установить центральную точку вида карты
	 * @param center Топологические координаты центральной точки
	 */
	public void setCenter(DoublePoint center)
	{
		System.out.println("Set center (" + center.x + ", " + center.y + ")");
		try
		{
			this.mapJObject.setCenter(new com.mapinfo.util.DoublePoint(center.x, center.y));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Установить заданный масштаб вида карты
	 * @param scale Массштаб для карты
	 */
	public void setScale(double scale)
	{
		System.out.println("Set scale " + scale);
		try
		{
			if (scale != 0.0D)
				this.mapJObject.setZoom(scale);
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
		try
		{
			FeatureLayer layer = (FeatureLayer)this.mapJObject.getLayers().get(
				layerIndex, LayerType.FEATURE);
			layer.setEnabled(layerVisible);
			layer.setAutoLabel(layerLabelsVisible);
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}

	/**
	 * Отображает карту и записывает её в поток данных
	 * @param os 
	 */
	public void writeMap(ObjectOutputStream os)
	{
		// Set up the renderer for this mapJ
		try
		{
			MapXtremeImageRenderer rr = new MapXtremeImageRenderer(mapXtremeURL);
			rr.render(ImageRequestComposer.create(
				this.mapJObject, NUM_OF_COLORS, BACKGROUND_COLOR, "image/gif"));

			//Output the map
			rr.toStream(os);
		}
		catch (Exception e)
		{
			log(e.getMessage());
		}
	}

	public void log(String msg)
	{
		if (!this.toDebug)
			return;

		try
		{
			FileOutputStream fos = new FileOutputStream("mcs_log.txt", true);
			fos.write( (msg + "\n").getBytes());
			fos.close();
		}
		catch (IOException exc)
		{
			//Ошибка при выводе логов
		}
	}

	public void writeNLogError(String error, ObjectOutputStream oos) throws
		IOException
	{
		log("MCS - ERROR!!! - " + error);
		oos.writeObject(error);
	}
}