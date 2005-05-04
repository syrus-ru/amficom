/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.4 $, $Date: 2005/05/04 13:05:43 $
 * @module mapper-servlet
 */
public class ServletCommandNames
{
	//Parameters for rendering image
	public static final String COMMAND_NAME = "command";

	//Values for parameter 	
	public static final String CN_START_RENDER_IMAGE = "start_render";
	public static final String CN_CANCEL_RENDERING = "stop_render";
	public static final String CN_GET_RENDITION = "get_rendition";	
	public static final String CN_SEARCH_NAME = "search";	
	
	//Parameters for rendering image
	public static final String PAR_WIDTH = "width";
	public static final String PAR_HEIGHT = "height";
	public static final String PAR_CENTER_X = "centerX";
	public static final String PAR_CENTER_Y = "centerY";
	public static final String PAR_ZOOM_FACTOR = "zoom";

	public static final String PAR_LAYER_INDEX = "LI";
	public static final String PAR_LAYER_VISIBLE = "LV";
	public static final String PAR_LAYER_LABELS_VISIBLE = "LLV";
	
	//Parameters for searching name
	public static final String PAR_NAME_TO_SEARCH = "name";
	
	//Errors available
	public static final String ERROR_INVALID_PARAMETERS = "invalid_params";
	public static final String ERROR_NO_PARAMETERS = "no_params";
	public static final String ERROR_MAP_EXCEPTION = "map_exception";
	public static final String ERROR_NO_MAP_RENDERED = "no_map_available";	
}