import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.awt.Color;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// MapInfo classes




/**
 * @author $Author: krupenn $
 * @version $Revision: 1.9 $, $Date: 2005/05/04 14:54:35 $
 * @module mapper-servlet
 */
public class MapperControllableServlet
	extends HttpServlet
{
	class State {
		int value;
		public State(int value) {
			this.value = value;
		}
		public int getValue() {
			return this.value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		
	}
	// Define constants to control various rendering options
	public static final int NUM_OF_COLORS = 256;
	public static final Color BACKGROUND_COLOR = Color.WHITE;
	public static final int MAXIMUM_LAYERS_COUNT = 100;	

	public static final String INIT_PARAMNAME_MAPPATH = "mappath"; //$NON-NLS-1$
	public static final String INIT_PARAMNAME_FILETOLOAD = "filetoload"; //$NON-NLS-1$
	public static final String INIT_PARAMNAME_MAPXTREME_URL = "mapxtremeurl"; //$NON-NLS-1$
	public static final String INIT_PARAMNAME_TODEBUG = "toDebug"; //$NON-NLS-1$	
	
	static final int STATE_IDLE = 0;
	static final int STATE_RENDERING = 1;
	static final int STATE_RENDERED = 2;
	
	static final String[] states = {"IDLE", "RENDERING", "RENDERED"};

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

	private State state = new State(STATE_IDLE);
	
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
		if (strParam != null)
		{
			this.toDebug = strParam.equals("true");
		}

		Logger.log("Using servlet parameters:");
		Logger.log(INIT_PARAMNAME_MAPPATH + ": " + this.mapPath);
		Logger.log(INIT_PARAMNAME_FILETOLOAD + ": " + this.fileToLoad);
		Logger.log(INIT_PARAMNAME_MAPXTREME_URL + ": " + this.mapXtremeURL);
		Logger.log(INIT_PARAMNAME_TODEBUG + ": " + Boolean.toString(this.toDebug));

		Logger.log("Creating RenderingThread instance at MCS.");
		this.renderingThread = new RenderingThread(this);
		Logger.log("RenderingThread instance is created at MCS. " + this.renderingThread);			
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
			writeNLogError(currentRequestOS, ServletCommandNames.ERROR_INVALID_PARAMETERS);
			return;
		}
		
		Map requestParameters = this.searchParameters(queryString);

		//Getting command name
		String commandName = this.getParameter(requestParameters,ServletCommandNames.COMMAND_NAME);
		Logger.log("commandName = " + commandName);
		Logger.log("state = " + states[this.state.getValue()]);

		synchronized(this.state) {
			switch(this.state.getValue()) {
				case STATE_IDLE:
					processIdleState(requestParameters, currentRequestOS, commandName);
					break;
				case STATE_RENDERING:
					processRenderingState(requestParameters, currentRequestOS, commandName);
					break;
				case STATE_RENDERED:
					processRenderedState(requestParameters, currentRequestOS, commandName);
					break;
				default:
					writeNLogError(currentRequestOS, ServletCommandNames.ERROR_WRONG_STATE);
			}
		}
	}
	
	void processIdleState(Map requestParameters, ObjectOutputStream currentRequestOS, String commandName) throws IOException {
		if (commandName.equals(ServletCommandNames.CN_START_RENDER_IMAGE))			
		{
			render(requestParameters, currentRequestOS);
			setState(STATE_RENDERING);
		}
		else if (commandName.equals(ServletCommandNames.CN_CANCEL_RENDERING))			
		{
			Logger.log("Before canceling rendition");				
			writeNLogError(currentRequestOS, ServletCommandNames.ERROR_WRONG_REQUEST);
		}
		else if (commandName.equals(ServletCommandNames.CN_GET_RENDITION))			
		{
			Logger.log("Before getting rendition");
			writeNLogError(currentRequestOS, ServletCommandNames.ERROR_WRONG_REQUEST);
		}
		else if (commandName.equals(ServletCommandNames.CN_SEARCH_NAME))			
		{
			search(requestParameters, currentRequestOS);
		}
		else
			writeNLogError(currentRequestOS, ServletCommandNames.ERROR_INVALID_PARAMETERS);
	}

	void processRenderingState(Map requestParameters, ObjectOutputStream currentRequestOS, String commandName) throws IOException {
		if (commandName.equals(ServletCommandNames.CN_START_RENDER_IMAGE))			
		{
			writeNLogError(currentRequestOS, ServletCommandNames.ERROR_WRONG_REQUEST);
		}
		else if (commandName.equals(ServletCommandNames.CN_CANCEL_RENDERING))			
		{
			cancel();
			setState(STATE_IDLE);
		}
		else if (commandName.equals(ServletCommandNames.CN_GET_RENDITION))			
		{
			Logger.log("Before getting rendition");
			writeNLogError(currentRequestOS, ServletCommandNames.ERROR_NO_MAP_RENDERED);
		}
		else if (commandName.equals(ServletCommandNames.CN_SEARCH_NAME))			
		{
			search(requestParameters, currentRequestOS);
		}
		else
			writeNLogError(currentRequestOS, ServletCommandNames.ERROR_INVALID_PARAMETERS);
	}

	void processRenderedState(Map requestParameters, ObjectOutputStream currentRequestOS, String commandName) throws IOException {
		if (commandName.equals(ServletCommandNames.CN_START_RENDER_IMAGE))			
		{
			returnRendition(currentRequestOS);
			render(requestParameters, currentRequestOS);
			setState(STATE_RENDERING);
		}
		else if (commandName.equals(ServletCommandNames.CN_CANCEL_RENDERING))			
		{
			returnRendition(currentRequestOS);
			setState(STATE_IDLE);
		}
		else if (commandName.equals(ServletCommandNames.CN_GET_RENDITION))			
		{
			Logger.log("Before getting rendition");
			writeNLogError(currentRequestOS, ServletCommandNames.ERROR_NO_MAP_RENDERED);
		}
		else if (commandName.equals(ServletCommandNames.CN_SEARCH_NAME))			
		{
			search(requestParameters, currentRequestOS);
		}
		else {
			Logger.log(commandName + " is not a valid command name!");
			writeNLogError(currentRequestOS, ServletCommandNames.ERROR_INVALID_PARAMETERS);
		}
	}
	
	void search(Map requestParameters, ObjectOutputStream currentRequestOS) {
		String nameToSearch = this.getParameter(requestParameters,ServletCommandNames.PAR_NAME_TO_SEARCH);
		Logger.log("nameToSearch = " + nameToSearch);
		
		this.renderingThread.writeNamesNCenters(nameToSearch,currentRequestOS);
	}

	void cancel() {
		Logger.log("Before canceling rendition");
		this.renderingThread.cancelRendering();
	}

	void render(Map requestParameters, ObjectOutputStream currentRequestOS) throws IOException {
		try {
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
				writeNLogError(currentRequestOS, ServletCommandNames.ERROR_INVALID_PARAMETERS);
				return;
			}
			int width = Integer.parseInt(widthString);
			Logger.log("width = " + Integer.toString(width));
			int height = Integer.parseInt(heightString);
			Logger.log("height = " + Integer.toString(height));
			if (! ( (width > 0) && (height > 0)))
			{
				writeNLogError(currentRequestOS, ServletCommandNames.ERROR_INVALID_PARAMETERS);
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
		} catch(NumberFormatException e) {
			Logger.log("Number Format Exception was detected!");			
			writeNLogError(currentRequestOS, ServletCommandNames.ERROR_INVALID_PARAMETERS);
		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		catch (NullPointerException npExc) {
			Logger.log("Null Pointer Exception was detected!");
			writeNLogError(currentRequestOS, ServletCommandNames.ERROR_NO_PARAMETERS);
		}
	}
	
	void returnRendition(ObjectOutputStream currentRequestOS) {
		if (this.renderingThread.isMapRendered())
			this.renderingThread.getMapRenditionInto(currentRequestOS);
	}

	void setState(int stateValue) {
		this.state.setValue(stateValue);
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

	public void writeNLogError(ObjectOutputStream currentRequestOS, String error) throws
		IOException
	{
		Logger.log("ERROR!!! - " + error);
		currentRequestOS.writeObject(error);
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

	public State getState() {
		return this.state;
	}
}

