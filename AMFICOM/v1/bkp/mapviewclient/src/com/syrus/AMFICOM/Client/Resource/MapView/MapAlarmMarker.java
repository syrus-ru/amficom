package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.general.Identifier;

public class MapAlarmMarker extends MapMarker
{
	static final public String typ = "mapalarmmarker";

	public static final String IMAGE_NAME = "alarmmarker";

	public static final String IMAGE2_NAME = "alarmmarker2";

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

	public String getTyp()
	{
		return typ;
	}

}
