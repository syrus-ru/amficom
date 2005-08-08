/**
 * $Id: MapPropertiesManager.java,v 1.33 2005/08/08 10:12:26 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.resource.AbstractImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.util.Log;

/**
 * ����� ��������� �������������� ����������������� �����������.
 * ��������� ������ �� ����� Map.properties � � ����������� �� ���������
 * ������ ������� ������� �����������:
 * <li>NetMapViewer
 * <li>MapConnection
 *  
 * <br>��� ���������� ������ ��������� ��������� � Map.properties
 * <li>type
 * <li>center
 * <li>zoom
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.33 $, $Date: 2005/08/08 10:12:26 $
 * @module mapviewclient_v1
 */
public final class MapPropertiesManager 
{
	private static final String FONT_DELIMITER = " ";

	/**
	 * ���� ������ ����������� ������.
	 */
	protected static String iniFileName = "Map.properties";
	
	public static final String MAP_CLONED_IDS = "mapclonedids";

	/** ������ �����, ������� ������ ���� � ����� ��������. */

 	protected static final String KEY_CONNECTION_CLASS = "connectionClass";
 	protected static final String KEY_VIEWER_CLASS = "viewerClass"; 	
 	protected static final String KEY_RENDERER_CLASS = "rendererClass"; 	
	protected static final String KEY_DATA_BASE_PATH = "dataBasePath";
	protected static final String KEY_DATA_BASE_VIEW = "dataBaseView";
	protected static final String KEY_DATA_BASE_URL = "dataBaseURL";
	protected static final String KEY_LAST_LONGITUDE = "lastLong";
	protected static final String KEY_LAST_LATITUDE = "lastLat";
	protected static final String KEY_LAST_ZOOM = "lastZoom";
	protected static final String KEY_LAST_VIEW = "lastView";
	protected static final String KEY_LAST_DIRECTORY = "lastDirectory";
	protected static final String KEY_DESCRETE_NAVIGATION = "descreteNavigation";
 	protected static final String KEY_TOPOLOGICAL_IMAGE_CACHE = "useTopologicalImageCache";
 	protected static final String KEY_OPTIMIZE_LINKS = "optimizeLinks";
 	protected static final String KEY_TOPO_IMAGE_MAX_TIMEWAIT = "topoImageMaxTimeWait";
 	protected static final String KEY_MOVE_MOUSE_NAVIGATING = "moveMouseNavigating";
	protected static final String KEY_NAVIGATE_AREA_SIZE = "navigateAreaSize";

 	
	public static final double DEFAULT_ZOOM = 1.0D;

	private static final String DEFAULT_LAST_LONGITUDE = "0";
	private static final String DEFAULT_LAST_LATITUDE = "0";
	private static final String DEFAULT_LAST_ZOOM = "1";
	private static final String DEFAULT_LAST_DIRECTORY = ".";
	private static final String DEFAULT_DESCRETE_NAVIGATION = "true";
	private static final String DEFAULT_TOPOLOGICAL_IMAGE_CACHE = "true";
	private static final String DEFAULT_OPTIMIZE_LINKS = "true";
	private static final String DEFAULT_TOPO_IMAGE_MAX_TIMEWAIT = "30000";
	private static final String DEFAULT_MOVE_MOUSE_NAVIGATING = "false";
	private static final String DEFAULT_NAVIGATE_AREA_SIZE = "0.03";

	/* values read from inifile. */
	protected static String dataBasePath = "";
	protected static String dataBaseView = "";
	protected static String dataBaseURL = "";
	protected static double lastLong = 0.D;
	protected static double lastLat = 0.D;
	protected static double lastZoom = 1.D;
	protected static String lastView = "";
	protected static String lastDirectory = ".";
	protected static boolean descreteNavigation = false;
	protected static boolean useTopologicalImageCache = false;
	protected static boolean optimizeLinks = false;
	protected static long topoImageMaxTimeWait = 30000;
	protected static boolean moveMouseNavigating = true;
	/**
	 * �������� �������� ������� ������� (��� ����� � �� ���������� �������� ������)
	 * � ��������� �� �������� ���� �����
	 */
	protected static double navigateAreaSize = 0.03;
	
	protected static String connectionClass = "";
	protected static String viewerClass = "";
	protected static String rendererClass = "";

	/* display constants. */
	public static final String DEFAULT_TEXT_BACKGROUND = String.valueOf(Color.YELLOW.getRGB());
	public static final String DEFAULT_TEXT_COLOR = String.valueOf(SystemColor.controlText.getRGB());
	public static final String DEFAULT_BORDER_COLOR = String.valueOf(SystemColor.activeCaptionBorder.getRGB());
	public static final String DEFAULT_METRIC = LangModelMap.getString("metric");
	public static final String DEFAULT_THICKNESS = String.valueOf(1);
	public static final String DEFAULT_BORDER_THICKNESS = String.valueOf(1);
	public static final String DEFAULT_STYLE = "Solid line";
	public static final BasicStroke DEFAULT_STROKE = new BasicStroke(2);
	public static final String DEFAULT_COLOR = String.valueOf(Color.BLUE.getRGB());
	public static final String DEFAULT_ALARMED_THICKNESS = String.valueOf(3);
	public static final String DEFAULT_ALARMED_STYLE = "Solid line";
	public static final BasicStroke DEFAULT_ALARMED_STROKE = new BasicStroke(3);
	public static final String DEFAULT_ALARMED_COLOR = String.valueOf(Color.RED.getRGB());
	public static final String DEFAULT_ALARMED_ANIMATION = "blink";
	public static final String DEFAULT_SELECTION_THICKNESS = String.valueOf(4);
	public static final String DEFAULT_SELECTION_STYLE = "Solid line";
	public static final BasicStroke DEFAULT_SELECTION_STROKE = new BasicStroke( 
			1,
			BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_BEVEL,
			(float)0.0,
			new float[] {5, 5},
			(float)0.0);
	public static final String DEFAULT_SELECTION_COLOR = String.valueOf(Color.GREEN.getRGB());
	public static final String DEFAULT_SELECTION_FIRST_COLOR = String.valueOf(Color.BLACK.getRGB());
	public static final String DEFAULT_SELECTION_SECOND_COLOR = String.valueOf(Color.RED.getRGB());
	public static final Font DEFAULT_FONT = new Font ("Arial", 1, 12);
	public static final String DEFAULT_FONT_ID = DEFAULT_FONT.getName() + MapPropertiesManager.FONT_DELIMITER + DEFAULT_FONT.getStyle() + MapPropertiesManager.FONT_DELIMITER + DEFAULT_FONT.getSize();
	public static final String DEFAULT_UNBOUND_THICKNESS = String.valueOf(4);
	public static final String DEFAULT_UNBOUND_LINK_COLOR = String.valueOf(Color.PINK.getRGB());
	public static final String DEFAULT_UNBOUND_LINK_POSITION_COLOR = String.valueOf(Color.ORANGE.getRGB());
	public static final String DEFAULT_UNBOUND_ELEMENT_COLOR = String.valueOf(Color.MAGENTA.getRGB());
	public static final String DEFAULT_CAN_BIND_COLOR = String.valueOf(Color.CYAN.getRGB());
	public static final String DEFAULT_SPARE_LENGTH = String.valueOf(1.5D);
	public static final String DEFAULT_MOUSE_TOLERANCY = String.valueOf(3);

	protected static final String KEY_TEXT_BACKGROUND = "textBackground";
	protected static final String KEY_TEXT_COLOR = "textForeground";
	protected static final String KEY_FONT = "font";
	protected static final String KEY_METRIC = "metric";
	protected static final String KEY_BORDER_THICKNESS = "borderThickness";
	protected static final String KEY_BORDER_COLOR = "borderColor";
	protected static final String KEY_THICKNESS = "thickness";
	protected static final String KEY_STYLE = "style";
	protected static final String KEY_COLOR = "color";
	protected static final String KEY_ALARMED_THICKNESS = "alarmedThickness";
	protected static final String KEY_ALARMED_STYLE = "alarmedStyle";
	protected static final String KEY_ALARMED_COLOR = "alarmedColor";
	protected static final String KEY_ALARMED_ANIMATION = "alarmedAnimation";
	protected static final String KEY_SELECTION_THICKNESS = "selectionThickness";
	protected static final String KEY_SELECTION_STYLE = "selectionStyle";
	protected static final String KEY_SELECTION_COLOR = "selectionColor";
	protected static final String KEY_SELECTION_FIRST_COLOR = "selectionFirstColor";
	protected static final String KEY_SELECTION_SECOND_COLOR = "selectionSecondColor";
	protected static final String KEY_UNBOUND_THICKNESS = "unboundThickness";
	protected static final String KEY_UNBOUND_ELEMENT_COLOR = "unboundElementColor";
	protected static final String KEY_UNBOUND_LINK_POSITION_COLOR = "linkPositionColor";
	protected static final String KEY_UNBOUND_LINK_COLOR = "unboundLinkColor";
	protected static final String KEY_CAN_BIND_COLOR = "canBindColor";
	protected static final String KEY_SPARE_LENGTH = "spareLength";
	protected static final String KEY_MOUSE_TOLERANCY = "mouseTolerancy";

	/* display variables. */
	protected static Color textBackground = null;
	protected static Color textColor = null;
	protected static Font font = DEFAULT_FONT;

	protected static String metric = null;

	protected static int borderThickness = 0;
	protected static Color borderColor = null;

	protected static int thickness = 0;
	protected static String style = null;
	protected static BasicStroke stroke = DEFAULT_STROKE;
	protected static Color color = null;

	protected static int alarmedThickness = 0;
	protected static String alarmedStyle = null;
	protected static BasicStroke alarmedStroke = DEFAULT_ALARMED_STROKE;
	protected static Color alarmedColor = null;
	protected static String alarmedAnimation = null;

	protected static int selectionThickness = 0;
	protected static String selectionStyle = null;
	protected static BasicStroke selectionStroke = DEFAULT_SELECTION_STROKE;
	protected static Color selectionColor = null;
	protected static Color firstSelectionColor = null;
	protected static Color secondSelectionColor = null;

	protected static int unboundThickness = 0;
	protected static Color unboundLinkColor = null;
	protected static Color unboundLinkPositionColor = null;
	protected static Color unboundElementColor = null;
	protected static Color canBindColor = null;

	protected static double spareLength = 0D;

	protected static int mouseTolerancy = 0;

	/* show modes. */

	/**
	 * ���� ��������� �������� ��������� ������ ��� ������� �������
	 * ������� �� ��������. ���� ���� ������ �������, �� ���� (thread)
	 * �������� ������ ���� ���� � ���� ������� �� �����������,
	 * � ����������� �� ����� ����� ������� "���������"
	 */
	protected static boolean drawAlarmed = false;

	/**
	 * ���� ��������� ��� ������� �������� �������.
	 */	
	protected static boolean showAlarmIndication = true;

	/**
	 * ���� ������ ����������� �������������� �����.
	 */	
	protected static boolean showPhysicalNodes = true;

	/**
	 * ����� ����������� �����.
	 */
	protected static boolean showLength = true;

	/**
	 * ���� ����������� �������� � �����.
	 */	
	protected static boolean showNodesNames = false;

	/**
	 * ���� ����������� �������� � ������.
	 */	
	protected static boolean showLinkNames = false;
	
	private static DecimalFormat scaleFormat;
	private static DecimalFormat distanceFormat;
	private static DecimalFormat coordinatesFormat;
	
	private static Map<Object,Boolean> layerVisibilityMap = new HashMap<Object,Boolean>();
	private static Map<Object,Boolean> layerLabelVisibilityMap = new HashMap<Object,Boolean>();
	
	public static boolean isLayerVisible(Object layer) {
		Boolean value = layerVisibilityMap.get(layer);
		if(value == null) {
			value = new Boolean(true);
			layerVisibilityMap.put(layer, value);
		}
		return value.booleanValue();
	}
	
	public static void setLayerVisible(Object layer, boolean visible) {
		Boolean value = new Boolean(visible);
		layerVisibilityMap.put(layer, value);
	}
	
	public static boolean isLayerLabelVisible(Object layer) {
		Boolean value = layerLabelVisibilityMap.get(layer);
		if(value == null) {
			value = new Boolean(false);
			layerLabelVisibilityMap.put(layer, value);
		}
		return value.booleanValue();
	}
	
	public static void setLayerLabelVisible(Object layer, boolean visible) {
		Boolean value = new Boolean(visible);
		layerLabelVisibilityMap.put(layer, value);
	}
	
	/**
	 * ��� ������ ������� � ���������� ����������
	 */
	private static DateFormat logDateFormat = new SimpleDateFormat("E M d H:m:s:S");

	private static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	private static Properties defaults = new Properties();

	static {
		defaults.put(KEY_TEXT_BACKGROUND, DEFAULT_TEXT_BACKGROUND);
		defaults.put(KEY_TEXT_COLOR, DEFAULT_TEXT_COLOR);
		defaults.put(KEY_FONT, DEFAULT_FONT);
		defaults.put(KEY_METRIC, DEFAULT_METRIC);
		defaults.put(KEY_BORDER_THICKNESS, DEFAULT_BORDER_THICKNESS);
		defaults.put(KEY_BORDER_COLOR, DEFAULT_BORDER_COLOR);
		defaults.put(KEY_THICKNESS, DEFAULT_THICKNESS);
		defaults.put(KEY_STYLE, DEFAULT_STYLE);
		defaults.put(KEY_COLOR, DEFAULT_COLOR);
		defaults.put(KEY_ALARMED_THICKNESS, DEFAULT_ALARMED_THICKNESS);
		defaults.put(KEY_ALARMED_STYLE, DEFAULT_ALARMED_STYLE);
		defaults.put(KEY_ALARMED_COLOR, DEFAULT_ALARMED_COLOR);
		defaults.put(KEY_ALARMED_ANIMATION, DEFAULT_ALARMED_ANIMATION);
		defaults.put(KEY_SELECTION_THICKNESS, DEFAULT_SELECTION_THICKNESS);
		defaults.put(KEY_SELECTION_STYLE, DEFAULT_SELECTION_STYLE);
		defaults.put(KEY_SELECTION_COLOR, DEFAULT_SELECTION_COLOR);
		defaults.put(KEY_SELECTION_FIRST_COLOR, DEFAULT_SELECTION_FIRST_COLOR);
		defaults.put(KEY_SELECTION_SECOND_COLOR, DEFAULT_SELECTION_SECOND_COLOR);
		defaults.put(KEY_UNBOUND_THICKNESS, DEFAULT_UNBOUND_THICKNESS);
		defaults.put(KEY_UNBOUND_ELEMENT_COLOR, DEFAULT_UNBOUND_ELEMENT_COLOR);
		defaults.put(KEY_UNBOUND_LINK_POSITION_COLOR, DEFAULT_UNBOUND_LINK_POSITION_COLOR);
		defaults.put(KEY_UNBOUND_LINK_COLOR, DEFAULT_UNBOUND_LINK_COLOR);
		defaults.put(KEY_CAN_BIND_COLOR, DEFAULT_CAN_BIND_COLOR);
		defaults.put(KEY_SPARE_LENGTH, DEFAULT_SPARE_LENGTH);
		defaults.put(KEY_MOUSE_TOLERANCY, DEFAULT_MOUSE_TOLERANCY);

		defaults.put(KEY_LAST_LONGITUDE, DEFAULT_LAST_LONGITUDE);
		defaults.put(KEY_LAST_LATITUDE, DEFAULT_LAST_LATITUDE);
		defaults.put(KEY_LAST_ZOOM, DEFAULT_LAST_ZOOM);
		defaults.put(KEY_LAST_DIRECTORY, DEFAULT_LAST_DIRECTORY);
		defaults.put(KEY_DESCRETE_NAVIGATION, DEFAULT_DESCRETE_NAVIGATION);
	 	defaults.put(KEY_TOPOLOGICAL_IMAGE_CACHE, DEFAULT_TOPOLOGICAL_IMAGE_CACHE);
	 	defaults.put(KEY_OPTIMIZE_LINKS, DEFAULT_OPTIMIZE_LINKS);
	 	defaults.put(KEY_TOPO_IMAGE_MAX_TIMEWAIT, DEFAULT_TOPO_IMAGE_MAX_TIMEWAIT);
	 	defaults.put(KEY_MOVE_MOUSE_NAVIGATING, DEFAULT_MOVE_MOUSE_NAVIGATING);
		defaults.put(KEY_NAVIGATE_AREA_SIZE, DEFAULT_NAVIGATE_AREA_SIZE);
	}
	
	static
	{
		try
		{
			Properties properties = new Properties(defaults);
			properties.load(new FileInputStream(iniFileName));
			MapPropertiesManager.setFromIniFile(properties);
		}
		catch(java.io.IOException e)
		{
			MapPropertiesManager.setDefaults();
		}

		DecimalFormatSymbols dfs;

		coordinatesFormat = (DecimalFormat )NumberFormat.getNumberInstance();
		coordinatesFormat.setMaximumFractionDigits(8);
		coordinatesFormat.setMinimumFractionDigits(8);
		coordinatesFormat.setMinimumIntegerDigits(1);
		dfs = coordinatesFormat.getDecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		coordinatesFormat.setDecimalFormatSymbols(dfs);

		distanceFormat = (DecimalFormat )NumberFormat.getNumberInstance();
		distanceFormat.setMaximumFractionDigits(1);
		distanceFormat.setMinimumFractionDigits(1);
		distanceFormat.setMinimumIntegerDigits(1);
		dfs = distanceFormat.getDecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		distanceFormat.setDecimalFormatSymbols(dfs);

		scaleFormat = (DecimalFormat )NumberFormat.getNumberInstance();
		scaleFormat.setMinimumIntegerDigits(1);
		scaleFormat.setMinimumFractionDigits(1);
		scaleFormat.setGroupingUsed(false);
		scaleFormat.setMaximumFractionDigits(8);
		dfs = scaleFormat.getDecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		scaleFormat.setDecimalFormatSymbols(dfs);
	}
	
	/**
	 * ��������� �����������.
	 */
	private MapPropertiesManager()
	{//empty
	}

	/**
	 * �������� ��� ������, ������������� ��� �����.
	 */
	public static String getNetMapViewerClassName() {
		return viewerClass;
	}

	/**
	 * �������� ��� ������, ������������ ������������� � ������ ���
	 * ����������� �������������� ��������.
	 */
	public static String getConnectionClassName() {
		return connectionClass;
	}

	/**
	 * �������� ��� ������, ������������� ��� �����.
	 */
	public static String getMapImageRendererClassName() {
		return rendererClass;
	}

	public static DateFormat getLogDateFormat() {
		return logDateFormat;
	}
	
	public static DateFormat getDateFormat() {
		return dateFormat;
	}
	
	public static double getZoom()
	{
		return lastZoom;
	}
	
	public static void setZoom(double zoom)
	{
		lastZoom = zoom;
	}

	public static DoublePoint getCenter()
	{
		return new DoublePoint(lastLong, lastLat);
	}

	public static void setCenter(DoublePoint center)
	{
		lastLong = center.getX();
		lastLat = center.getY();
	}

	public static boolean isDescreteNavigation() {
		return descreteNavigation;
	}
	
 	public static boolean isTopologicalImageCache() {
		return useTopologicalImageCache;
	}

	public static boolean isOptimizeLinks() {
		return optimizeLinks;
	}
	
	public static long getTopoImageMaxTimeWait() {
		return topoImageMaxTimeWait;
	}
	
	public static boolean isMoveMouseNavigating() {
		return moveMouseNavigating;
	}

	public static double getNavigateAreaSize() {
		return navigateAreaSize;
	}
	
	/**
	 * ���������� �������� �� ������������������ �����.
	 */
	protected static void setFromIniFile(Properties properties)
	{
		dataBasePath = properties.getProperty(KEY_DATA_BASE_PATH);
		dataBaseView = properties.getProperty(KEY_DATA_BASE_VIEW);
		dataBaseURL = properties.getProperty(KEY_DATA_BASE_URL);

		viewerClass = properties.getProperty(KEY_VIEWER_CLASS);
		connectionClass = properties.getProperty(KEY_CONNECTION_CLASS);		
		rendererClass = properties.getProperty(KEY_RENDERER_CLASS);
		
		try {
			lastLong = Double.parseDouble(properties.getProperty(KEY_LAST_LONGITUDE));
		} catch (Exception e) {
			lastLong = Double.parseDouble(defaults.getProperty(KEY_LAST_LONGITUDE));
		}

		try {
			lastLat = Double.parseDouble(properties.getProperty(KEY_LAST_LATITUDE));
		} catch (Exception e) {
			lastLat = Double.parseDouble(defaults.getProperty(KEY_LAST_LATITUDE));
		}

		try {
			lastZoom = Double.parseDouble(properties.getProperty(KEY_LAST_ZOOM));
		} catch (Exception e) {
			lastZoom = Double.parseDouble(defaults.getProperty(KEY_LAST_ZOOM));
		}
		
		try {
			lastDirectory = properties.getProperty(KEY_LAST_DIRECTORY);
		} catch (Exception e) {
			lastDirectory = defaults.getProperty(KEY_LAST_DIRECTORY);
		}
		
		try {
			descreteNavigation = Boolean.parseBoolean(properties.getProperty(KEY_DESCRETE_NAVIGATION));
		} catch (Exception e) {
			descreteNavigation = Boolean.parseBoolean(defaults.getProperty(KEY_DESCRETE_NAVIGATION));
		}

		try {
			useTopologicalImageCache = Boolean.parseBoolean(properties.getProperty(KEY_TOPOLOGICAL_IMAGE_CACHE));
		} catch (Exception e) {
			useTopologicalImageCache = Boolean.parseBoolean(defaults.getProperty(KEY_TOPOLOGICAL_IMAGE_CACHE));
		}
		
		try {
			topoImageMaxTimeWait = Long.parseLong(properties.getProperty(KEY_TOPO_IMAGE_MAX_TIMEWAIT));
		} catch (Exception e) {
			topoImageMaxTimeWait = Long.parseLong(defaults.getProperty(KEY_TOPO_IMAGE_MAX_TIMEWAIT));
		}
		
		try {
			optimizeLinks = Boolean.parseBoolean(properties.getProperty(KEY_OPTIMIZE_LINKS));
		} catch (Exception e) {
			optimizeLinks = Boolean.parseBoolean(defaults.getProperty(KEY_OPTIMIZE_LINKS));
		}
		
		try {
			moveMouseNavigating = Boolean.parseBoolean(properties.getProperty(KEY_MOVE_MOUSE_NAVIGATING));
		} catch (Exception e) {
			moveMouseNavigating = Boolean.parseBoolean(defaults.getProperty(KEY_MOVE_MOUSE_NAVIGATING));
		}
		
		try {
			navigateAreaSize = Double.parseDouble(properties.getProperty(KEY_NAVIGATE_AREA_SIZE));
		} catch (Exception e) {
			navigateAreaSize = Double.parseDouble(defaults.getProperty(KEY_NAVIGATE_AREA_SIZE));
		}

		try {
			textBackground = new Color(Integer.parseInt(properties.getProperty(KEY_TEXT_BACKGROUND)));
		} catch (Exception e) {
			textBackground = new Color(Integer.parseInt(defaults.getProperty(KEY_TEXT_BACKGROUND)));
		}

		try {
			textColor = new Color(Integer.parseInt(properties.getProperty(KEY_TEXT_COLOR)));
		} catch (Exception e) {
			textColor = new Color(Integer.parseInt(defaults.getProperty(KEY_TEXT_COLOR)));
		}

		try {
			String fontId = properties.getProperty(KEY_FONT);
			StringTokenizer tokenizer = new StringTokenizer(fontId, "\n");
			String fontName = tokenizer.nextToken();
			int fontStyle = Integer.parseInt(tokenizer.nextToken());
			int fontSize = Integer.parseInt(tokenizer.nextToken());
			font = new Font(fontName, fontStyle, fontSize);
		} catch (Exception e) {
			font = DEFAULT_FONT;
		}

		try {
			metric = properties.getProperty(KEY_METRIC);
		} catch (Exception e) {
			metric = defaults.getProperty(KEY_METRIC);
		}

		try {
			borderThickness = Integer.parseInt(properties.getProperty(KEY_BORDER_THICKNESS));
		} catch (Exception e) {
			borderThickness = Integer.parseInt(defaults.getProperty(KEY_BORDER_THICKNESS));
		}
		
		try {
			borderColor = new Color(Integer.parseInt(properties.getProperty(KEY_BORDER_COLOR)));
		} catch (Exception e) {
			borderColor = new Color(Integer.parseInt(defaults.getProperty(KEY_BORDER_COLOR)));
		}

		try {
			thickness = Integer.parseInt(properties.getProperty(KEY_THICKNESS));
		} catch (Exception e) {
			thickness = Integer.parseInt(defaults.getProperty(KEY_THICKNESS));
		}

		try {
			style = properties.getProperty(KEY_STYLE);
		} catch (Exception e) {
			style = defaults.getProperty(KEY_STYLE);
		}

		try {
			color = new Color(Integer.parseInt(properties.getProperty(KEY_COLOR)));
		} catch (Exception e) {
			color = new Color(Integer.parseInt(defaults.getProperty(KEY_COLOR)));
		}

		try {
			alarmedThickness =Integer.parseInt(properties.getProperty(KEY_ALARMED_THICKNESS));
		} catch (Exception e) {
			alarmedThickness =Integer.parseInt(defaults.getProperty(KEY_ALARMED_THICKNESS));
		}

		try {
			alarmedStyle = properties.getProperty(KEY_ALARMED_STYLE);
		} catch (Exception e) {
			alarmedStyle = defaults.getProperty(KEY_ALARMED_STYLE);
		}

		try {
			alarmedColor = new Color(Integer.parseInt(properties.getProperty(KEY_ALARMED_COLOR)));
		} catch (Exception e) {
			alarmedColor = new Color(Integer.parseInt(defaults.getProperty(KEY_ALARMED_COLOR)));
		}

		try {
			alarmedAnimation = new String(properties.getProperty(KEY_ALARMED_ANIMATION));
		} catch (Exception e) {
			alarmedAnimation = new String(defaults.getProperty(KEY_ALARMED_ANIMATION));
		}

		try {
			selectionThickness = Integer.parseInt(properties.getProperty(KEY_SELECTION_THICKNESS));
		} catch (Exception e) {
			selectionThickness = Integer.parseInt(defaults.getProperty(KEY_SELECTION_THICKNESS));
		}

		try {
			selectionStyle = properties.getProperty(KEY_SELECTION_STYLE);
		} catch (Exception e) {
			selectionStyle = defaults.getProperty(KEY_SELECTION_STYLE);
		}

		try {
			selectionColor = new Color(Integer.parseInt(properties.getProperty(KEY_SELECTION_COLOR)));
		} catch (Exception e) {
			selectionColor = new Color(Integer.parseInt(defaults.getProperty(KEY_SELECTION_COLOR)));
		}

		try {
			firstSelectionColor = new Color(Integer.parseInt(properties.getProperty(KEY_SELECTION_FIRST_COLOR)));
		} catch (Exception e) {
			firstSelectionColor = new Color(Integer.parseInt(defaults.getProperty(KEY_SELECTION_FIRST_COLOR)));
		}

		try {
			secondSelectionColor = new Color(Integer.parseInt(properties.getProperty(KEY_SELECTION_SECOND_COLOR)));
		} catch (Exception e) {
			secondSelectionColor = new Color(Integer.parseInt(defaults.getProperty(KEY_SELECTION_SECOND_COLOR)));
		}

		try {
			unboundThickness = Integer.parseInt(properties.getProperty(KEY_UNBOUND_THICKNESS));
		} catch (Exception e) {
			unboundThickness = Integer.parseInt(defaults.getProperty(KEY_UNBOUND_THICKNESS));
		}

		try {
			unboundLinkColor = new Color(Integer.parseInt(properties.getProperty(KEY_UNBOUND_LINK_COLOR)));
		} catch (Exception e) {
			unboundLinkColor = new Color(Integer.parseInt(defaults.getProperty(KEY_UNBOUND_LINK_COLOR)));
		}

		try {
			unboundLinkPositionColor = new Color(Integer.parseInt(properties.getProperty(KEY_UNBOUND_LINK_POSITION_COLOR)));
		} catch (Exception e) {
			unboundLinkPositionColor = new Color(Integer.parseInt(defaults.getProperty(KEY_UNBOUND_LINK_POSITION_COLOR)));
		}

		try {
			unboundElementColor = new Color(Integer.parseInt(properties.getProperty(KEY_UNBOUND_ELEMENT_COLOR)));
		} catch (Exception e) {
			unboundElementColor = new Color(Integer.parseInt(defaults.getProperty(KEY_UNBOUND_ELEMENT_COLOR)));
		}

		try {
			canBindColor = new Color(Integer.parseInt(properties.getProperty(KEY_CAN_BIND_COLOR)));
		} catch (Exception e) {
			canBindColor = new Color(Integer.parseInt(defaults.getProperty(KEY_CAN_BIND_COLOR)));
		}

		try {
			spareLength = Double.parseDouble(properties.getProperty(KEY_SPARE_LENGTH));
		} catch (Exception e) {
			spareLength = Double.parseDouble(defaults.getProperty(KEY_SPARE_LENGTH));
		}

		try {
			mouseTolerancy = Integer.parseInt(properties.getProperty(KEY_MOUSE_TOLERANCY));
		} catch (Exception e) {
			mouseTolerancy = Integer.parseInt(defaults.getProperty(KEY_MOUSE_TOLERANCY));
		}

		try {
			lastView = properties.getProperty(KEY_LAST_VIEW);
		} catch (Exception e) {
			lastView = defaults.getProperty(KEY_LAST_VIEW);
		}
	}

	/**
	 * ���������� �������� �� ���������.
	 */
	protected static void setDefaults()
	{
		dataBasePath = "";
		dataBaseView = "";
		dataBaseURL = "";
		lastDirectory = ".";
		descreteNavigation = false;
		useTopologicalImageCache = false;
		optimizeLinks = false;
		moveMouseNavigating = true;
		topoImageMaxTimeWait = 30000;
		navigateAreaSize = 0.03;
		viewerClass = "";
		connectionClass = "";		
		rendererClass = "";
	}

	/**
	 * ��������� ������� ��������� ����������� � ����.
	 */
	public static void saveIniFile()
	{
		Log.debugMessage("method call MapPropertiesManager.saveIniFile()", Level.FINER);

		try
		{
			Properties properties = new Properties(defaults);

			properties.load(new FileInputStream(iniFileName));
			properties.setProperty(KEY_LAST_LONGITUDE, Double.toString(lastLong));
			properties.setProperty(KEY_LAST_LATITUDE, Double.toString(lastLat));
			properties.setProperty(KEY_LAST_ZOOM, Double.toString(lastZoom));			
			properties.setProperty(KEY_LAST_DIRECTORY, lastDirectory);

			properties.setProperty(KEY_TEXT_BACKGROUND, Integer.toString(textBackground.getRGB()));
			properties.setProperty(KEY_TEXT_COLOR, Integer.toString(textColor.getRGB()));
			properties.setProperty(KEY_FONT, font.getName() + MapPropertiesManager.FONT_DELIMITER + font.getStyle() + MapPropertiesManager.FONT_DELIMITER + font.getSize());
			properties.setProperty(KEY_METRIC, metric.toString());
			properties.setProperty(KEY_BORDER_THICKNESS, Integer.toString(borderThickness));
			properties.setProperty(KEY_BORDER_COLOR, Integer.toString(borderColor.getRGB()));
			properties.setProperty(KEY_THICKNESS, Integer.toString(thickness));
			properties.setProperty(KEY_STYLE, style);
			properties.setProperty(KEY_COLOR, Integer.toString(color.getRGB()));
			properties.setProperty(KEY_ALARMED_THICKNESS, Integer.toString(alarmedThickness));
			properties.setProperty(KEY_ALARMED_STYLE, alarmedStyle);
			properties.setProperty(KEY_ALARMED_COLOR, Integer.toString(alarmedColor.getRGB()));
			properties.setProperty(KEY_ALARMED_ANIMATION, alarmedAnimation);
			properties.setProperty(KEY_SELECTION_THICKNESS, Integer.toString(selectionThickness));
			properties.setProperty(KEY_SELECTION_STYLE, selectionStyle);
			properties.setProperty(KEY_SELECTION_COLOR, Integer.toString(selectionColor.getRGB()));
			properties.setProperty(KEY_SELECTION_FIRST_COLOR, Integer.toString(firstSelectionColor.getRGB()));
			properties.setProperty(KEY_SELECTION_SECOND_COLOR, Integer.toString(secondSelectionColor.getRGB()));
			properties.setProperty(KEY_UNBOUND_THICKNESS, Integer.toString(unboundThickness));
			properties.setProperty(KEY_UNBOUND_LINK_COLOR, Integer.toString(unboundLinkColor.getRGB()));
			properties.setProperty(KEY_UNBOUND_LINK_POSITION_COLOR, Integer.toString(unboundLinkPositionColor.getRGB()));
			properties.setProperty(KEY_UNBOUND_ELEMENT_COLOR, Integer.toString(unboundElementColor.getRGB()));
			properties.setProperty(KEY_CAN_BIND_COLOR, Integer.toString(canBindColor.getRGB()));
			properties.setProperty(KEY_SPARE_LENGTH, Double.toString(spareLength));
			properties.setProperty(KEY_MOUSE_TOLERANCY, Integer.toString(mouseTolerancy));

			properties.store(new FileOutputStream(iniFileName), null);
		}
		catch(java.io.IOException e)
		{
			System.out.println("Params not saved");
		}
	}

	/** �������, ����������� ��� ��������� ����������. */

    private static Component component = new Component() {/*empty*/};
    private static MediaTracker tracker = new MediaTracker(component);
    private static int mediaTrackerID = 0;

	/**
	 * ��� ����������� ��������� �����. ��������� �� ����� ����� ������������
	 * �������� ������� ���������� ��������� � ���������� ��������, ��������
	 * ��������� ���� ������ �������. �������� ������������������� �������
	 * �������� ��� ������������ ��� ���������������
	 */
	private static Map<Identifier,Image> originalImages = new HashMap<Identifier,Image>();
	
	/**
	 * ��� ���������������� ����������� ��������� �����. ��� ����, �����
	 * �� ��������� ���������������� �����������, ��� ����� �������� � ����
	 */
	private static Map<Identifier,Image> scaledImages = new HashMap<Identifier,Image>();

	public static void setOriginalImage(Identifier imageId, Image image)
	{
		originalImages.put(imageId, image);
	}

	public static void setScaledImageSize(Identifier imageId, int width, int height)
	{
		Image img = MapPropertiesManager.getScaledImage(imageId);
		if(img.getWidth(null) != width
			|| img.getHeight(null) != height)
		{
			img = MapPropertiesManager.getImage(imageId);
			img = img.getScaledInstance(
				width,
				height,
				Image.SCALE_SMOOTH);
			scaledImages.put(imageId, img);
			MapPropertiesManager.loadImage(img);
		}
	}

	public static Image getImage(Identifier imageId)
	{
		Image img = originalImages.get(imageId);
		if(img == null)
		{
			try
			{
				AbstractImageResource ir = StorableObjectPool.getStorableObject(imageId, true);
				if(ir instanceof FileImageResource)
				{
					img = Toolkit.getDefaultToolkit().createImage(((FileImageResource )ir).getFileName());
				}
				else
				{
					img = new ImageIcon(ir.getImage()).getImage();
				}
				originalImages.put(imageId, img);
				MapPropertiesManager.loadImage(img);
			}
			catch (ApplicationException e)
			{
				e.printStackTrace();
			}
		}
		return img;
	}

	public static Image getScaledImage(Identifier imageId)
	{
		Image img = scaledImages.get(imageId);
		if(img == null)
		{
			img = MapPropertiesManager.getImage(imageId);
			scaledImages.put(imageId, img);
		}
		return img;
	}

	/**
	 * ������������ ��������� ����������� ��� ����������� �� �����������.
	 */
    private static final void loadImage(Image image) 
	{
		synchronized(tracker) 
		{
            int id = MapPropertiesManager.getNextID();

			tracker.addImage(image, id);
			try 
			{
				tracker.waitForID(id, 0);
			} 
			catch (InterruptedException e) 
			{
				System.out.println("INTERRUPTED while loading Image");
			}
			tracker.removeImage(image, id);
		}
    }

	/**
	 * �������� ������������� ��� ��������� �����������.
	 */
    private static final int getNextID() 
	{
        synchronized(tracker) 
		{
            return ++mediaTrackerID;
        }
    }


////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////

	public static void setTextBackground(Color _textBackground)
	{
		textBackground = _textBackground;
	}


	public static Color getTextBackground()
	{
		return textBackground;
	}


	public static void setMetric(String _metric)
	{
		metric = _metric;
	}


	public static String getMetric()
	{
		return metric;
	}


	public static void setThickness(int _thickness)
	{
		thickness = _thickness;
	}


	public static int getThickness()
	{
		return thickness;
	}


	public static void setStyle(String _style)
	{
		style = _style;
	}


	public static String getStyle()
	{
		return style;
	}


	public static void setStroke(BasicStroke _stroke)
	{
		stroke = _stroke;
	}


	public static BasicStroke getStroke()
	{
		return stroke;
	}


	public static void setColor(Color _color)
	{
		color = _color;
	}


	public static Color getColor()
	{
		return color;
	}


	public static void setAlarmedThickness(int _alarmedThickness)
	{
		alarmedThickness = _alarmedThickness;
	}


	public static int getAlarmedThickness()
	{
		return alarmedThickness;
	}


	public static void setAlarmedStyle(String _alarmedStyle)
	{
		alarmedStyle = _alarmedStyle;
	}


	public static String getAlarmedStyle()
	{
		return alarmedStyle;
	}


	public static void setAlarmedStroke(BasicStroke _alarmedStroke)
	{
		alarmedStroke = _alarmedStroke;
	}


	public static BasicStroke getAlarmedStroke()
	{
		return alarmedStroke;
	}


	public static void setAlarmedColor(Color _alarmedColor)
	{
		alarmedColor = _alarmedColor;
	}


	public static Color getAlarmedColor()
	{
		return alarmedColor;
	}


	public static void setAlarmedAnimation(String _alarmedAnimation)
	{
		alarmedAnimation = _alarmedAnimation;
	}


	public static String getAlarmedAnimation()
	{
		return alarmedAnimation;
	}


	public static void setSelectionThickness(int _selectionThickness)
	{
		selectionThickness = _selectionThickness;
	}


	public static int getSelectionThickness()
	{
		return selectionThickness;
	}


	public static void setSelectionStyle(String _selectionStyle)
	{
		selectionStyle = _selectionStyle;
	}


	public static String getSelectionStyle()
	{
		return selectionStyle;
	}


	public static void setSelectionStroke(BasicStroke _selectionStroke)
	{
		selectionStroke = _selectionStroke;
	}


	public static BasicStroke getSelectionStroke()
	{
		return selectionStroke;
	}


	public static void setSelectionColor(Color _selectionColor)
	{
		selectionColor = _selectionColor;
	}


	public static Color getSelectionColor()
	{
		return selectionColor;
	}


	public static void setFirstSelectionColor(Color _firstSelectionColor)
	{
		firstSelectionColor = _firstSelectionColor;
	}


	public static Color getFirstSelectionColor()
	{
		return firstSelectionColor;
	}


	public static void setSecondSelectionColor(Color _secondSelectionColor)
	{
		secondSelectionColor = _secondSelectionColor;
	}


	public static Color getSecondSelectionColor()
	{
		return secondSelectionColor;
	}


	public static void setFont(Font _font)
	{
		font = _font;
	}


	public static Font getFont()
	{
		return font;
	}


	public static void setUnboundLinkColor(Color _unboundLinkColor)
	{
		unboundLinkColor = _unboundLinkColor;
	}


	public static Color getUnboundLinkColor()
	{
		return unboundLinkColor;
	}


	public static void setUnboundLinkPositionColor(Color _unboundLinkPositionColor)
	{
		unboundLinkPositionColor = _unboundLinkPositionColor;
	}


	public static Color getUnboundLinkPositionColor()
	{
		return unboundLinkPositionColor;
	}


	public static void setUnboundElementColor(Color _unboundElementColor)
	{
		unboundElementColor = _unboundElementColor;
	}


	public static Color getUnboundElementColor()
	{
		return unboundElementColor;
	}


	public static void setCanBindColor(Color _canBindColor)
	{
		canBindColor = _canBindColor;
	}


	public static Color getCanBindColor()
	{
		return canBindColor;
	}


	public static void setSpareLength(double _spareLength)
	{
		spareLength = _spareLength;
	}


	public static double getSpareLength()
	{
		return spareLength;
	}


	public static void setDrawAlarmed(boolean _drawAlarmed)
	{
		drawAlarmed = _drawAlarmed;
	}


	public static boolean isDrawAlarmed()
	{
		return drawAlarmed;
	}

	public static boolean isShowAlarmIndication() {
		return showAlarmIndication;
	}

	public static void setShowAlarmIndication(boolean _showAlarmIndication) {
		showAlarmIndication = _showAlarmIndication;
	}

	public static void setShowPhysicalNodes(boolean _showPhysicalNodes)
	{
		showPhysicalNodes = _showPhysicalNodes;
	}


	public static boolean isShowPhysicalNodes()
	{
		return showPhysicalNodes;
	}


	public static void setShowLength(boolean _showLength)
	{
		showLength = _showLength;
	}


	public static boolean isShowLength()
	{
		return showLength;
	}


	public static void setShowNodesNames(boolean _showNodesNames)
	{
		showNodesNames = _showNodesNames;
	}


	public static boolean isShowNodesNames()
	{
		return showNodesNames;
	}


	public static void setShowLinkNames(boolean _showLinkNames)
	{
		showLinkNames = _showLinkNames;
	}


	public static boolean isShowLinkNames()
	{
		return showLinkNames;
	}


	public static void setTextColor(Color _textColor)
	{
		textColor = _textColor;
	}


	public static Color getTextColor()
	{
		return textColor;
	}


	public static void setBorderThickness(int _borderThickness)
	{
		borderThickness = _borderThickness;
	}


	public static int getBorderThickness()
	{
		return borderThickness;
	}


	public static void setBorderColor(Color _borderColor)
	{
		borderColor = _borderColor;
	}


	public static Color getBorderColor()
	{
		return borderColor;
	}


	public static void setUnboundThickness(int _unboundThickness)
	{
		unboundThickness = _unboundThickness;
	}


	public static int getUnboundThickness()
	{
		return unboundThickness;
	}


	public static void setMouseTolerancy(int _mouseTolerancy)
	{
		mouseTolerancy = _mouseTolerancy;
	}

	public static int getMouseTolerancy()
	{
		return mouseTolerancy;
	}

	public static void setDataBaseURL(String _dataBaseURL)
	{
		dataBaseURL = _dataBaseURL;
	}

	public static String getDataBaseURL()
	{
		return dataBaseURL;
	}

	public static void setDataBasePath(String _dataBasePath)
	{
		dataBasePath = _dataBasePath;
	}

	public static String getDataBasePath()
	{
		return dataBasePath;
	}

	public static void setDataBaseView(String _dataBaseView)
	{
		dataBaseView = _dataBaseView;
	}

	public static String getDataBaseView()
	{
		return dataBaseView;
	}


	public static void setLastDirectory(String _lastDirectory)
	{
		lastDirectory = _lastDirectory;
	}


	public static String getLastDirectory()
	{
		return lastDirectory;
	}


	public static NumberFormat getDistanceFormat()
	{
		return distanceFormat;
	}


	public static NumberFormat getCoordinatesFormat()
	{
		return coordinatesFormat;
	}


	public static NumberFormat getScaleFormat()
	{
		return scaleFormat;
	}
}
