/**
 * $Id: MapPropertiesManager.java,v 1.5 2005/01/21 16:19:57 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.resource.AbstractImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;

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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.ImageIcon;

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
 * @version $Revision: 1.5 $, $Date: 2005/01/21 16:19:57 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapPropertiesManager 
{
	/**
	 * Фаил откуда загружаются данные
	 */
	protected static String iniFileName = "Map.properties";
	
	public static final String MAP_CLONED_IDS = "mapclonedids";

	/** Список полей, которые должны быть в файле настроек */

	protected static final String KEY_MAP_TYPE = "mapType";
	protected static final String KEY_DATA_BASE_PATH = "dataBasePath";
	protected static final String KEY_DATA_BASE_VIEW = "dataBaseView";
	protected static final String KEY_DATA_BASE_URL = "dataBaseURL";
	protected static final String KEY_LAST_LONGITUDE = "lastLong";
	protected static final String KEY_LAST_LATITUDE = "lastLat";
	protected static final String KEY_LAST_ZOOM = "lastZoom";
	protected static final String KEY_LAST_VIEW = "lastView";
	protected static final String KEY_LAST_DIRECTORY = "lastDirectory";


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
	protected static String dataBaseURL = "";
	protected static String lastLong = "";
	protected static String lastLat = "";
	protected static String lastZoom = "";
	protected static String lastView = "";
	protected static String lastDirectory = ".";

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
	public static final int DEFAULT_SELECTION_THICKNESS = 4;
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

	/**
	 * флаг отображения подписей к узлам
	 */	
	protected static boolean showNodesNames = false;

	/**
	 * флаг отображения подписей к линиям
	 */	
	protected static boolean showLinkNames = false;
	
	private static DecimalFormat scaleFormat;
	private static DecimalFormat distanceFormat;
	private static DecimalFormat coordinatesFormat;
	
	static
	{
		try
		{
			Properties properties = new Properties();
			properties.load(new FileInputStream(iniFileName));
			MapPropertiesManager.setFromIniFile(properties);
		}
		catch(java.io.IOException e)
		{
			MapPropertiesManager.setDefaults();
		}

		DecimalFormatSymbols dfs;

		coordinatesFormat = (DecimalFormat )NumberFormat.getNumberInstance();
		coordinatesFormat.setMaximumFractionDigits(4);
		coordinatesFormat.setMinimumFractionDigits(4);
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
		scaleFormat.setMaximumFractionDigits(340);
		dfs = scaleFormat.getDecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		scaleFormat.setDecimalFormatSymbols(dfs);
	}
	
	/**
	 * приватный конструктор
	 */
	private MapPropertiesManager()
	{
	}

	/**
	 * получить имя класса, отображающего вид карты
	 */
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

	/**
	 * получить имя класса, реализующего подсоединение к данным ГИС
	 * отображения географических объектов
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

	public static DoublePoint getCenter()
	{
		try
		{
			double lng = Double.parseDouble(lastLong);
			double lat = Double.parseDouble(lastLat);
			
			return new DoublePoint(lng, lat);
		}
		catch(Exception e)
		{
			return null;
		}
	}

	public static void setCenter(DoublePoint center)
	{
		String longoldval = lastLong;
		String latoldval = lastLat;
		try
		{
			lastLong = String.valueOf(center.getX());
			lastLat = String.valueOf(center.getY());
		}
		catch(Exception e)
		{
			lastLong = longoldval;
			lastLat = latoldval;
		}
	}

	/**
	 * Установить значения из инициализационного файла
	 */
	protected static void setFromIniFile(Properties properties)
	{
		dataBasePath = properties.getProperty(KEY_DATA_BASE_PATH);
		dataBaseView = properties.getProperty(KEY_DATA_BASE_VIEW);
		dataBaseURL = properties.getProperty(KEY_DATA_BASE_URL);

		mapType = properties.getProperty(KEY_MAP_TYPE);
		lastLong = properties.getProperty(KEY_LAST_LONGITUDE);
		lastLat = properties.getProperty(KEY_LAST_LATITUDE);
		lastZoom = properties.getProperty(KEY_LAST_ZOOM);
		lastDirectory = properties.getProperty(KEY_LAST_DIRECTORY);
//		selectionColor = iniFile.getValue("selectionColor");
//		selectionStyle = iniFile.getValue("selectionStyle");
//		showPhysicalNodes = iniFile.getValue("showNodes");
		lastView = properties.getProperty(KEY_LAST_VIEW);
	}

	/**
	 * установить значения по умолчанию
	 */
	protected static void setDefaults()
	{
		mapType = OFX_TYPE;
		dataBasePath = "";
		dataBaseView = "";
		dataBaseURL = "";
		lastDirectory = ".";
	}

	/**
	 * Сохранить текущие настройки отображения в файл
	 */
	public static void saveIniFile()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call MapPropertiesManager.saveIniFile()");

		try
		{
			Properties properties = new Properties();
			properties.load(new FileInputStream(iniFileName));
			properties.setProperty(KEY_LAST_LONGITUDE, lastLong);
			properties.setProperty(KEY_LAST_LATITUDE, lastLat);
			properties.setProperty(KEY_LAST_DIRECTORY, lastDirectory);
			properties.store(new FileOutputStream(iniFileName), null);
		}
		catch(java.io.IOException e)
		{
			System.out.println("Params not saved");
		}
	}

	/** объекты, необходимые для отрисовки пиктограмм */

    private static Component component = new Component() {};
    private static MediaTracker tracker = new MediaTracker(component);
    private static int mediaTrackerID = 0;

	/**
	 * Кэш изображений элементов карты. Поскольку на карте может отображаться
	 * довольно большое количество элементов с одинаковым рисунком, элементы
	 * разделяют один объект рисунка. Оригинал немасштабированного рисунка
	 * хранится для последующего его масштабирования
	 */
	private static Map originalImages = new HashMap();
	
	/**
	 * Кэш масштабированных изображений элементов карты. Для того, чтобы
	 * не плодились масштабированные изображения, они также хранятся в кэше
	 */
	private static Map scaledImages = new HashMap();

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
		Image img = (Image )originalImages.get(imageId);
		if(img == null)
		{
			try
			{
				AbstractImageResource ir = (AbstractImageResource )ResourceStorableObjectPool.getStorableObject(imageId, true);
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
			catch (CommunicationException e)
			{
				e.printStackTrace();
			}
			catch (DatabaseException e)
			{
				e.printStackTrace();
			}
		}
		return img;
	}

	public static Image getScaledImage(Identifier imageId)
	{
		Image img = (Image )scaledImages.get(imageId);
		if(img == null)
		{
			img = MapPropertiesManager.getImage(imageId);
			scaledImages.put(imageId, img);
		}
		return img;
	}

	/**
	 * обеспечивает подгрузку пиктограммы для мгновенного ее отображения
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
	 * получить идентификатор для подгрузки пиктограммы
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
