package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Resource.Map.Map;

import java.awt.Rectangle;

public class MapEventMarker extends MapMarker
{
	static final public String typ = "mapeventmarker";

	public Object descriptor;

	public MapEventMarker(
			String id, 
			Map map,
			Rectangle bounds, 
			String imageId,
			double len, 
			MapMeasurementPathElement path)
	{
		super(id, map, bounds, imageId, len, path);

		setImageId("images/eventmarker.gif");
		this.name = LangModelMap.getString("Event");
	}

	public boolean isMovable()
	{
		return false;
	}

	public String getToolTipText()
	{
		String s1 = LangModelMap.getString("Event") + " " + getName() 
			+ " (" + LangModelMap.getString("Path_lowercase")
			+ " " + measurementPath.getName() + ")";

		return s1;
	}

	public String getTyp()
	{
		return typ;
	}

}
