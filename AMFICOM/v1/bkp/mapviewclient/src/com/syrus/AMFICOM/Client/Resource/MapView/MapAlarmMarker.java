package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Map.Map;

import com.syrus.AMFICOM.general.Identifier;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class MapAlarmMarker extends MapMarker
{
	static final public String typ = "mapalarmmarker";

	public static final String IMAGE_NAME = "alarmmarker";
	public static final String IMAGE_PATH = "images/alarm_bell_green.gif";

	public static final String IMAGE2_NAME = "alarmmarker2";
	public static final String IMAGE2_PATH = "images/alarm_bell_red.gif";

	static
	{
		MapPropertiesManager.setOriginalImage(IMAGE_NAME, new ImageIcon(IMAGE_PATH).getImage());
		MapPropertiesManager.setOriginalImage(IMAGE2_NAME, new ImageIcon(IMAGE2_PATH).getImage());
	}

	public MapAlarmMarker(
			String id, 
			MapView mapView,
			double len, 
			MapMeasurementPathElement path,
			Identifier meId)
	{
		super(id, mapView, len, path, meId);

		this.setImageId(IMAGE_NAME);
		this.setName(LangModelMap.getString("Alarm"));
	}

	public String getToolTipText()
	{
		String s1 = LangModelMap.getString("Alarm") 
				+ " " + getName() 
				+ " (" + LangModelMap.getString("Path_lowercase") 
				+ " " + measurementPath.getName() 
				+ ") " + LangModelMap.getString("Distance_lowercase") 
				+ " - " + getFromStartLengthLf();

		return s1;
	}

	public String getTyp()
	{
		return typ;
	}

	public Image getAlarmedImage()
	{
		return MapPropertiesManager.getScaledImage(IMAGE2_NAME);
	}
}
