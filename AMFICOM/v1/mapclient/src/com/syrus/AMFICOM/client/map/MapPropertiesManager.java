/**
 * $Id: MapPropertiesManager.java,v 1.1 2004/09/13 12:02:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.io.IniFile;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.geom.Point2D;

/**
 * Класс управляет инициализацией картографического отображения.
 * Считывает данные из файла Map.properties и в зависимости от считанных
 * данных создает объекты отображения:
 * 	NetMapViewer
 *  MapConnection
 *  
 * при завершении работы сохраняет настройки в Map.properties
 * 	type
 * 	center
 *  zoom
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:02:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapPropertiesManager 
{
	protected static IniFile iniFile;
	protected static String iniFileName = "Map.properties";//Фаил откуда загружаются данные

	public static final double DEFAULT_ZOOM = 1.0D;

	protected static final String OFX_TYPE = "ofx";
	protected static final String OFX_CONNECTION = "com.syrus.AMFICOM.Client.Map.ObjectFX.OfxConnection";
	protected static final String OFX_VIEWER = "com.syrus.AMFICOM.Client.Map.ObjectFX.OfxNetMapViewer";

	protected static final String MAPINFO_TYPE = "mapinfo";
	protected static final String MAPINFO_CONNECTION = "com.syrus.AMFICOM.Client.Map.Mapinfo.MapInfoConnection";
	protected static final String MAPINFO_VIEWER = "com.syrus.AMFICOM.Client.Map.Mapinfo.MapInfoNetMapViewer";

	/* values read from inifile */
	protected static String mapType = OFX_TYPE;
	protected static String dataBasePath = "";
	protected static String dataBaseView = "";
	protected static String lastLong = "";
	protected static String lastLat = "";
	protected static String lastZoom = "";
	protected static String lastView = "";

	/* display constants */
	public static final Color DEFAULT_TEXT_BACKGROUND = SystemColor.window;
	public static final Color DEFAULT_TEXT_COLOR = SystemColor.controlText;
	public static final Color DEFAULT_BORDER_COLOR = SystemColor.activeCaptionBorder;
	public static final String DEFAULT_METRIC = LangModelMap.getString("metric");
	public static final int DEFAULT_THICKNESS = 1;
	public static final int DEFAULT_BORDER_THICKNESS = 1;
	public static final String DEFAULT_STYLE = "Solid line";
	public static final BasicStroke DEFAULT_STROKE = new BasicStroke(2);
	public static final Color DEFAULT_COLOR = Color.blue;
	public static final int DEFAULT_ALARMED_THICKNESS = 3;
	public static final String DEFAULT_ALARMED_STYLE = "Solid line";
	public static final BasicStroke DEFAULT_ALARMED_STROKE = new BasicStroke(3);
	public static final Color DEFAULT_ALARMED_COLOR = Color.red;
	public static final String DEFAULT_ALARMED_ANIMATION = "blink";
	public static final int DEFAULT_SELECTION_THICKNESS = 1;
	public static final String DEFAULT_SELECTION_STYLE = "Solid line";
	public static final BasicStroke DEFAULT_SELECTION_STROKE = new BasicStroke( 
			1,
			BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_BEVEL,
			(float)0.0,
			new float[] {5, 5},
			(float)0.0);
	public static final Color DEFAULT_SELECTION_COLOR = Color.GREEN;
	public static final Color DEFAULT_FIRST_SELECTION_COLOR = Color.BLACK;
	public static final Color DEFAULT_SECOND_SELECTION_COLOR = Color.RED;
	public static final String DEFAULT_FONT_ID = "Arial_1_12";
	public static final Font DEFAULT_FONT = new Font ("Arial", 1, 12);

	public static final int DEFAULT_UNBOUND_THICKNESS = 4;
	public static final Color DEFAULT_UNBOUND_LINK_COLOR = Color.PINK;
	public static final Color DEFAULT_UNBOUND_LINK_POSITION_COLOR = Color.ORANGE;
	public static final Color DEFAULT_UNBOUND_ELEMENT_COLOR = Color.MAGENTA;
	public static final Color DEFAULT_CAN_BIND_COLOR = Color.CYAN;

	public static final double DEFAULT_SPARE_LENGTH = 1.5D;

	public static final int DEFAULT_MOUSE_TOLERANCY = 3;

	/* display variables */
	protected static Color textBackground = DEFAULT_TEXT_BACKGROUND;
	protected static Color textColor = DEFAULT_TEXT_COLOR;
	protected static Font font = DEFAULT_FONT;

	protected static String metric = DEFAULT_METRIC;

	protected static int borderThickness = DEFAULT_BORDER_THICKNESS;
	protected static Color borderColor = DEFAULT_BORDER_COLOR;

	protected static int thickness = DEFAULT_THICKNESS;
	protected static String style = DEFAULT_STYLE;
	protected static BasicStroke stroke = DEFAULT_STROKE;
	protected static Color color = DEFAULT_COLOR;

	protected static int alarmedThickness = DEFAULT_ALARMED_THICKNESS;
	protected static String alarmedStyle = DEFAULT_ALARMED_STYLE;
	protected static BasicStroke alarmedStroke = DEFAULT_ALARMED_STROKE;
	protected static Color alarmedColor = DEFAULT_ALARMED_COLOR;
	protected static String alarmedAnimation = DEFAULT_ALARMED_ANIMATION;

	protected static int selectionThickness = DEFAULT_SELECTION_THICKNESS;
	protected static String selectionStyle = DEFAULT_SELECTION_STYLE;
	protected static BasicStroke selectionStroke = DEFAULT_SELECTION_STROKE;
	protected static Color selectionColor = DEFAULT_SELECTION_COLOR;
	protected static Color firstSelectionColor = DEFAULT_FIRST_SELECTION_COLOR;
	protected static Color secondSelectionColor = DEFAULT_SECOND_SELECTION_COLOR;

	protected static int unboundThickness = DEFAULT_UNBOUND_THICKNESS;
	protected static Color unboundLinkColor = DEFAULT_UNBOUND_LINK_COLOR;
	protected static Color unboundLinkPositionColor = DEFAULT_UNBOUND_LINK_POSITION_COLOR;
	protected static Color unboundElementColor = DEFAULT_UNBOUND_ELEMENT_COLOR;
	protected static Color canBindColor = DEFAULT_CAN_BIND_COLOR;

	protected static double spareLength = DEFAULT_SPARE_LENGTH;

	protected static int mouseTolerancy = DEFAULT_MOUSE_TOLERANCY;

	/* show modes */

	/**
	 * флаг рисования элемента тревожным цветом при наличии сигнала
	 * тревоги на элементе. Если есть сигнал тревоги, то нить (thread)
	 * анимации меняет этот флаг и дает команду на перерисовку,
	 * в зависимости от этого флага элемент "анимирует"
	 */
	protected static boolean showAlarmState = false;

	/**
	 * флаг режима отображения топологических узлов
	 */	
	protected static boolean showPhysicalNodes = true;

	/**
	 * режим отображения длины
	 */
	protected static boolean showLength = true;

	protected static boolean showNodesNames = false;
	protected static boolean showLinkNames = false;
	
	static
	{
		try
		{
			iniFile = new IniFile(iniFileName);
			setFromIniFile();
		}
		catch(java.io.IOException e)
		{
			setDefaults();
		}
	}
	
	private MapPropertiesManager()
	{
	}

	public static String getNetMapViewerClassName()
	{
		String viewerClass = "";

		if(mapType.equalsIgnoreCase(OFX_TYPE))
			viewerClass = OFX_VIEWER;
		else
		if(mapType.equalsIgnoreCase(MAPINFO_TYPE))
			viewerClass = MAPINFO_VIEWER;
			
		return viewerClass;
	}
/*
	protected static NetMapViewer mapViewer = null;
	
	public static NetMapViewer getNetMapViewer()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call MapPropertiesManager.getNetMapViewer()");

		if(mapViewer == null)
		{
			String viewerClass = "";

			if(mapType.equalsIgnoreCase(OFX_TYPE))
				viewerClass = OFX_VIEWER;
			else
			if(mapType.equalsIgnoreCase(MAPINFO_TYPE))
				viewerClass = MAPINFO_VIEWER;

			try
			{
				mapViewer = (NetMapViewer )Class.forName(viewerClass).newInstance();
			}
			catch(ClassNotFoundException cnfe)
			{
				cnfe.printStackTrace();
			}
			catch(InstantiationException ie)
			{
				ie.printStackTrace();
			}
			catch(IllegalAccessException iae)
			{
				iae.printStackTrace();
			}
		}
		return mapViewer;
	}
*/
	public static String getConnectionClassName()
	{
		String connectionClass = "";
		
		if(mapType.equalsIgnoreCase(OFX_TYPE))
			connectionClass = OFX_CONNECTION;
		else
		if(mapType.equalsIgnoreCase(MAPINFO_TYPE))
			connectionClass = MAPINFO_CONNECTION;
	
		return connectionClass;
	}
/*
	protected static MapConnection connection = null;
	
	public static MapConnection getConnection()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call MapPropertiesManager.getConnection()");
		
		if(connection == null)
		{
			String connectionClass = "";
			
			if(mapType.equalsIgnoreCase(OFX_TYPE))
				connectionClass = OFX_CONNECTION;
			else
			if(mapType.equalsIgnoreCase(MAPINFO_TYPE))
				connectionClass = MAPINFO_CONNECTION;

			try
			{
				connection = (MapConnection )Class.forName(connectionClass).newInstance();
			}
			catch(ClassNotFoundException cnfe)
			{
				cnfe.printStackTrace();
			}
			catch(InstantiationException ie)
			{
				ie.printStackTrace();
			}
			catch(IllegalAccessException iae)
			{
				iae.printStackTrace();
			}
			
			connection.setPath(dataBasePath);
			connection.setView(dataBaseView);
		}
		return connection;
	}
*/	
	public static double getZoom()
	{
		try
		{
			double zoom = Double.parseDouble(lastZoom);
			return zoom;
		}
		catch(Exception e)
		{
			return DEFAULT_ZOOM;
		}
	}
	
	public static void setZoom(double zoom)
	{
		String oldval = lastZoom;
		try
		{
			lastZoom = String.valueOf(zoom);
		}
		catch(Exception e)
		{
			lastZoom = oldval;
		}
	}

	public static Point2D.Double getCenter()
	{
		try
		{
			double lng = Double.parseDouble(lastLong);
			double lat = Double.parseDouble(lastLat);
			
			return new Point2D.Double(lng, lat);
		}
		catch(Exception e)
		{
			return null;
		}
	}

	public static void setCenter(Point2D.Double center)
	{
		String longoldval = lastLong;
		String latoldval = lastLat;
		try
		{
			lastLong = String.valueOf(center.x);
			lastLat = String.valueOf(center.y);
		}
		catch(Exception e)
		{
			lastLong = longoldval;
			lastLat = latoldval;
		}
	}

	//Установить значения из инициализационного файла
	protected static void setFromIniFile()
	{
		dataBasePath = iniFile.getValue("dataBasePath");
		dataBaseView = iniFile.getValue("dataBaseView");

		mapType = iniFile.getValue("mapType");
		lastLong = iniFile.getValue("lastLong");
		lastLat = iniFile.getValue("lastLat");
		lastZoom = iniFile.getValue("lastZoom");
//		selectionColor = iniFile.getValue("selectionColor");
//		selectionStyle = iniFile.getValue("selectionStyle");
//		showPhysicalNodes = iniFile.getValue("showNodes");
		lastView = iniFile.getValue("lastView");
	}

	protected static void setDefaults()
	{
		mapType = OFX_TYPE;
		dataBasePath = "";
		dataBaseView = "";
	}

	public static void saveIniFile()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call MapPropertiesManager.saveIniFile()");
		
		iniFile.setValue( "last_long", lastLong  );
        iniFile.setValue( "last_lat", lastLat );

        if ( iniFile.saveKeys() )
        {
			System.out.println("Params saved");
        }
	}


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


	public static void setShowAlarmState(boolean _showAlarmState)
	{
		showAlarmState = _showAlarmState;
	}


	public static boolean isShowAlarmState()
	{
		return showAlarmState;
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
}
