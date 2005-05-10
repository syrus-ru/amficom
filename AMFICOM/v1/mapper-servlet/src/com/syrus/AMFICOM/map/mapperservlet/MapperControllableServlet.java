package com.syrus.AMFICOM.map.mapperservlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.mapinfo.mapj.MapJ;
import com.mapinfo.unit.LinearUnit;

import java.awt.Color;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.2 $, $Date: 2005/05/10 07:08:44 $
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

//	private State state = new State(STATE_IDLE);
	
	private Map stateMap = new HashMap();

	/**
	 * Объект для поиска топографических объектов
	 */
	private Searcher searcherInstance = null;
	
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
		if (strParam != null)
		{
			this.toDebug = strParam.equals("true");
		}

		Logger.log("Using servlet parameters:");
		Logger.log(INIT_PARAMNAME_MAPPATH + ": " + this.mapPath);
		Logger.log(INIT_PARAMNAME_FILETOLOAD + ": " + this.fileToLoad);
		Logger.log(INIT_PARAMNAME_MAPXTREME_URL + ": " + this.mapXtremeURL);
		Logger.log(INIT_PARAMNAME_TODEBUG + ": " + Boolean.toString(this.toDebug));
		
		try
		{
			this.searcherInstance = new Searcher(this.getFileToLoad(),this.getMapPath());
		}
		catch	(IOException exc)
		{
			logError("Failed creating searching instance.");
			throw new ServletException();
		}
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
		Logger.logSeparator();
		Logger.log("Creating ObjectOutputStream...");
		ObjectOutputStream currentRequestOS = new ObjectOutputStream(resp.getOutputStream());
		
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
			logError(ServletCommandNames.ERROR_INVALID_PARAMETERS);
			return;
		}
		
		Map requestParameters = this.searchParameters(queryString);

		String userId = this.getParameter(requestParameters,ServletCommandNames.USER_ID);
		
		SessionState sessionState = (SessionState )this.stateMap.get(userId);
		if(sessionState == null) 
		{
			sessionState = new SessionState(userId);
			this.stateMap.put(userId, sessionState);
		}
		
		processSession(sessionState, requestParameters,currentRequestOS,resp);
	}

	void processSession(
			SessionState sessionState,
			Map requestParameters,
			ObjectOutputStream currentRequestOS,
			HttpServletResponse resp) {
		
		synchronized(sessionState)
		{
			//Getting command name
			String commandName = this.getParameter(requestParameters,ServletCommandNames.COMMAND_NAME);
			if (commandName == null)
			{
				logError("Command name parameter is not given");
				resp.addHeader(
						ServletCommandNames.STATUS_FIELD_NAME,
						ServletCommandNames.ERROR_NO_PARAMETERS);
				
				return;
			}
			
			Logger.log("userId = " + sessionState.getUserId());
			Logger.log("commandName = " + commandName);
			Logger.log("state = " + State.states[sessionState.getState().getValue()]);
			
			switch(sessionState.getState().getValue())
			{
				case State.STATE_IDLE:
					processIdleState(sessionState,requestParameters,commandName,currentRequestOS,resp);
					break;
				case State.STATE_RENDERING:
					processRenderingState(sessionState,requestParameters,commandName,currentRequestOS,resp);
					break;
				case State.STATE_RENDERED:
					processRenderedState(sessionState,requestParameters,commandName,currentRequestOS,resp);
					break;
				default:
					logError(ServletCommandNames.ERROR_WRONG_STATE);
			}
		}
	}
	
	void processIdleState(
			SessionState sessionState,
			Map requestParameters,
			String commandName,
			ObjectOutputStream currentRequestOS,
			HttpServletResponse resp)
	{
		if (commandName.equals(ServletCommandNames.CN_START_RENDER_IMAGE))			
		{
			try
			{
				render(sessionState, requestParameters);
				sessionState.setState(State.STATE_RENDERING);
				resp.addHeader(
						ServletCommandNames.STATUS_FIELD_NAME,
						ServletCommandNames.STATUS_SUCCESS);
			} catch (IOException e)
			{
				logError ("Failed creating MapJ");
				sessionState.setState(State.STATE_IDLE);
				resp.addHeader(
						ServletCommandNames.STATUS_FIELD_NAME,
						ServletCommandNames.ERROR_MAP_EXCEPTION);
			}
		}
		else if (commandName.equals(ServletCommandNames.CN_CANCEL_RENDERING))			
		{
			logError(ServletCommandNames.ERROR_WRONG_REQUEST);
			resp.addHeader(
					ServletCommandNames.STATUS_FIELD_NAME,
					ServletCommandNames.ERROR_WRONG_REQUEST);
		}
		else if (commandName.equals(ServletCommandNames.CN_GET_RENDITION))			
		{
			logError(ServletCommandNames.ERROR_WRONG_REQUEST);
			resp.addHeader(
					ServletCommandNames.STATUS_FIELD_NAME,
					ServletCommandNames.ERROR_WRONG_REQUEST);
		}
		else if (commandName.equals(ServletCommandNames.CN_SEARCH_NAME))			
		{
			search(requestParameters, currentRequestOS);
			resp.addHeader(
					ServletCommandNames.STATUS_FIELD_NAME,
					ServletCommandNames.STATUS_SUCCESS);
		}
		else
		{
			logError(ServletCommandNames.ERROR_INVALID_PARAMETERS);
			Logger.log(commandName + " is not a valid command name!");			
			resp.addHeader(
					ServletCommandNames.STATUS_FIELD_NAME,
					ServletCommandNames.ERROR_INVALID_PARAMETERS);
		}
	}

	void processRenderingState(
			SessionState sessionState,
			Map requestParameters,
			String commandName,
			ObjectOutputStream currentRequestOS,
			HttpServletResponse resp)
	{
		if (commandName.equals(ServletCommandNames.CN_START_RENDER_IMAGE))			
		{
			logError(ServletCommandNames.ERROR_WRONG_REQUEST);
			resp.addHeader(
					ServletCommandNames.STATUS_FIELD_NAME,
					ServletCommandNames.ERROR_WRONG_REQUEST);
		}
		else if (commandName.equals(ServletCommandNames.CN_CANCEL_RENDERING))			
		{
			cancel(sessionState);
			sessionState.setState(State.STATE_IDLE);
			resp.addHeader(
					ServletCommandNames.STATUS_FIELD_NAME,
					ServletCommandNames.STATUS_SUCCESS);
		}
		else if (commandName.equals(ServletCommandNames.CN_GET_RENDITION))			
		{
			Logger.log("No map rendered.");
			resp.addHeader(
					ServletCommandNames.STATUS_FIELD_NAME,
					ServletCommandNames.ERROR_NO_MAP_RENDERED);
		}
		else if (commandName.equals(ServletCommandNames.CN_SEARCH_NAME))			
		{
			search(requestParameters, currentRequestOS);
			resp.addHeader(
					ServletCommandNames.STATUS_FIELD_NAME,
					ServletCommandNames.STATUS_SUCCESS);
		}
		else
		{
			logError(ServletCommandNames.ERROR_INVALID_PARAMETERS);
			Logger.log(commandName + " is not a valid command name!");			
			resp.addHeader(
					ServletCommandNames.STATUS_FIELD_NAME,
					ServletCommandNames.ERROR_INVALID_PARAMETERS);
		}
	}

	void processRenderedState(
			SessionState sessionState,
			Map requestParameters,
			String commandName,
			ObjectOutputStream currentRequestOS,
			HttpServletResponse resp)
	{
		
		//В текущей идеологии сначала надо забрать изображение,
		//а потом уже запускать рендеринг
		
		if (commandName.equals(ServletCommandNames.CN_START_RENDER_IMAGE))			
		{
//			returnRendition(currentRequestOS);
//			render(requestParameters, currentRequestOS);
//			setState(STATE_RENDERING);
//			resp.addHeader(
//					ServletCommandNames.STATUS_FIELD_NAME,
//					ServletCommandNames.STATUS_SUCCESS);
			logError(ServletCommandNames.ERROR_WRONG_REQUEST);
			resp.addHeader(
					ServletCommandNames.STATUS_FIELD_NAME,
					ServletCommandNames.ERROR_WRONG_REQUEST);
		}
		else if (commandName.equals(ServletCommandNames.CN_CANCEL_RENDERING))			
		{
//			setState(STATE_IDLE);
			logError(ServletCommandNames.ERROR_WRONG_REQUEST);			
			resp.addHeader(
					ServletCommandNames.STATUS_FIELD_NAME,
					ServletCommandNames.ERROR_WRONG_REQUEST);
		}
		else if (commandName.equals(ServletCommandNames.CN_GET_RENDITION))			
		{
			resp.addHeader(
					ServletCommandNames.STATUS_FIELD_NAME,
					ServletCommandNames.STATUS_SUCCESS);
			returnRendition(sessionState, currentRequestOS);
			sessionState.setState(State.STATE_IDLE);			
		}
		else if (commandName.equals(ServletCommandNames.CN_SEARCH_NAME))			
		{
			search(requestParameters, currentRequestOS);
			resp.addHeader(
					ServletCommandNames.STATUS_FIELD_NAME,
					ServletCommandNames.STATUS_SUCCESS);
		}
		else
		{
			logError(ServletCommandNames.ERROR_INVALID_PARAMETERS);
			Logger.log(commandName + " is not a valid command name!");			
			resp.addHeader(
					ServletCommandNames.STATUS_FIELD_NAME,
					ServletCommandNames.ERROR_INVALID_PARAMETERS);
		}
	}

	void search(Map requestParameters,ObjectOutputStream currentRequestOS)
	{
		String nameToSearch = this.getParameter(requestParameters,ServletCommandNames.PAR_NAME_TO_SEARCH);
		Logger.log("nameToSearch = " + nameToSearch);
	
		this.searcherInstance.writeNamesNCenters(nameToSearch,currentRequestOS);
	}

	void cancel(SessionState sessionState) {
		Logger.log("Before canceling rendition");
		sessionState.getRenderingThread().cancelRendering();
	}

	void render(SessionState sessionState, Map requestParameters)
		throws IOException
	{
		try
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
				logError(ServletCommandNames.ERROR_INVALID_PARAMETERS);
				return;
			}
			int width = Integer.parseInt(widthString);
			Logger.log("width = " + Integer.toString(width));
			int height = Integer.parseInt(heightString);
			Logger.log("height = " + Integer.toString(height));
			if (! ( (width > 0) && (height > 0)))
			{
				logError(ServletCommandNames.ERROR_INVALID_PARAMETERS);
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
			Logger.log ("Before starting rendition");

			MapJRenderer mapJRenderer = MapJRendererPool.lockRenderer(
					this.getMapXtremeURL(),
					this.getFileToLoad(),
					this.getMapPath());
			
			Logger.log ("Before creating RenderingThread");			
			RenderingThread renderingThread = new RenderingThread(sessionState,mapJRenderer);
			
			sessionState.setRenderingThread(renderingThread);
			
			Logger.log ("Before starting processing RenderingThread");			
			sessionState.getRenderingThread().startProcessing(
					width,
					height,
					zoom,
					centerX,
					centerY,
					layerParamIndex,layersVisibilities);
			Logger.log ("Rendition started.");
		}
		catch(NumberFormatException e)
		{
			Logger.log("Number Format Exception was detected!");			
			logError(ServletCommandNames.ERROR_INVALID_PARAMETERS);
			
		}
		catch (NullPointerException npExc)
		{
			Logger.log("Null Pointer Exception was detected!");
			logError(ServletCommandNames.ERROR_NO_PARAMETERS);
		}
	}
	
	void returnRendition(SessionState sessionState, ObjectOutputStream currentRequestOS) {
		if (sessionState.getOutputStream() != null)
			try
			{
				sessionState.getOutputStream().writeTo(currentRequestOS);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				Logger.log(e.getMessage());			
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
			
			if (dividerIndex < 0)
				break;
			
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
	
	public void logError(String error)
	{
		Logger.log("ERROR!!! - " + error);
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
	
	/**
	 * Создаёт объект MapJ и загружает картографические данные. 
	 * @return Готовый к использованию объект MapJ для работы с
	 *  картографическими данными
	 * @throws IOException
	 */
	public static MapJ createMapJ(String fileToLoad, String mapPath) throws IOException
	{
		Logger.log("RunningThread - Initializing MapJ instance...");
		// instantiate a MapJ and set the bounds
		MapJ returnValue = new MapJ(); // this MapJ object

		// Query for image locations and load the geoset
		try
		{
			Logger.log("RunningThread - Loading geoset...");
			if (fileToLoad.endsWith(".gst"))
			{
				returnValue.loadGeoset(fileToLoad, mapPath, null);
				Logger.log("RunningThread - Geoset " + fileToLoad + " has been loaded.");
			}
			else
			{
				returnValue.loadMapDefinition(fileToLoad);
				Logger.log("RunningThread - Map definition " + fileToLoad + " has been loaded.");
			}
		}
		catch (IOException e)
		{
			Logger.log("RunningThread - ERROR!!! - Can't load geoset: " + fileToLoad);
			throw e;
		}
		
		returnValue.setDistanceUnits(LinearUnit.meter);
		
		return returnValue;
	}
}

