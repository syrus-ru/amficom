package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.general.Identifier;

public class MapEventMarker extends MapMarker
{
	public static final String IMAGE_NAME = "eventmarker";

	public MapEventMarker(
			Identifier id, 
			MapView mapView,
			double len, 
			MapMeasurementPathElement path,
			Identifier meId)
	{
		super(id, mapView, len, path, meId);

//		this.setIconName(IMAGE_NAME);
		this.setName(LangModelMap.getString("Event"));
	}

//	public static MapEventMarker createInstance(
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
//			return new MapEventMarker(
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
