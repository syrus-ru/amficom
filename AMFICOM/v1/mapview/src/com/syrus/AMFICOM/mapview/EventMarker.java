/**
 * $Id: EventMarker.java,v 1.2 2005/02/01 15:11:28 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.general.Identifier;

/**
 * Стационарный маркер собития на пути.
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/02/01 15:11:28 $
 * @module mapviewclient_v1
 * @todo EventMarker functionality
 */
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
