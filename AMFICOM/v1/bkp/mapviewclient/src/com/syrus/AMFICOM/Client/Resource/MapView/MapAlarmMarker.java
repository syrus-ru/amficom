package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.general.Identifier;

public class MapAlarmMarker extends MapMarker
{
	public static final String IMAGE_NAME = "alarmmarker";

	public static final String IMAGE2_NAME = "alarmmarker2";

	public MapAlarmMarker(
			Identifier id, 
			MapView mapView,
			double len, 
			MapMeasurementPathElement path,
			Identifier meId)
	{
		super(id, mapView, len, path, meId);

//		this.setImageId(IMAGE_NAME);
		this.setName(LangModelMap.getString("Alarm"));
	}

//	public static MapAlarmMarker createInstance(
//			MapView mapView,
//			double opticalDistance, 
//			MapMeasurementPathElement path,
//			Identifier meId)
//	{
//		if (meId == null || mapView == null || path == null)
//			throw new IllegalArgumentException("Argument is 'null'");
//		
//		try
//		{
//			Identifier ide =
//				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE);
//			return new MapAlarmMarker(
//				ide,
//				mapView,
//				opticalDistance, 
//				path,
//				meId);
//		}
//		catch (IdentifierGenerationException e)
//		{
//			e.printStackTrace();
//		}
//		catch (IllegalObjectEntityException e) 
//		{
//			e.printStackTrace();
//		}
//	}

}
