package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;

public class EventMarker extends Marker
{
	public EventMarker(
			Identifier id, 
			MapView mapView,
			double len, 
			MeasurementPath path,
			Identifier meId,
			String name)
	{
		super(id, mapView, len, path, meId, name);
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
