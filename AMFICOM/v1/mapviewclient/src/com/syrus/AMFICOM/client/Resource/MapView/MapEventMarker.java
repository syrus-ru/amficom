package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Map.Map;

import com.syrus.AMFICOM.general.Identifier;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class MapEventMarker extends MapMarker
{
	static final public String typ = "mapeventmarker";

	public static final String IMAGE_NAME = "eventmarker";
	public static final String IMAGE_PATH = "images/eventmarker.gif";

	static
	{
		MapPropertiesManager.setOriginalImage(IMAGE_NAME, new ImageIcon(IMAGE_PATH).getImage());
	}

	public MapEventMarker(
			String id, 
			MapView mapView,
			double len, 
			MapMeasurementPathElement path,
			Identifier meId)
	{
		super(id, mapView, len, path, meId);

		this.setImageId(IMAGE_NAME);
		this.name = LangModelMap.getString("Event");
	}

	public String getToolTipText()
	{
		String s1 = LangModelMap.getString("Event") 
			+ " " + getName() 
			+ " (" + LangModelMap.getString("Path_lowercase")
			+ " " + measurementPath.getName() + ")";

		return s1;
	}

	public String getTyp()
	{
		return typ;
	}

}
