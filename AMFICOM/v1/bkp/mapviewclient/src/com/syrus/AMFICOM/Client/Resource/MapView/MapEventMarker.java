package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.general.Identifier;

public class MapEventMarker extends MapMarker
{
	static final public String typ = "mapeventmarker";

	public static final String IMAGE_NAME = "eventmarker";

	public MapEventMarker(
			String id, 
			MapView mapView,
			double len, 
			MapMeasurementPathElement path,
			Identifier meId)
	{
		super(id, mapView, len, path, meId);

		this.setImageId(IMAGE_NAME);
		this.setName(LangModelMap.getString("Event"));
	}

	public String getTyp()
	{
		return typ;
	}

}
