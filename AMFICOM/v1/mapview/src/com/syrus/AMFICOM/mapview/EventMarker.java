/**
 * $Id: EventMarker.java,v 1.4 2005/02/02 15:17:30 krupenn Exp $
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
 * @version $Revision: 1.4 $, $Date: 2005/02/02 15:17:30 $
 * @module mapviewclient_v1
 * @todo EventMarker functionality
 */
public class EventMarker extends Marker
{
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3834312821583655217L;

	public EventMarker(
			Identifier id, 
			Identifier creatorId,
			MapView mapView,
			double len, 
			MeasurementPath path,
			Identifier meId,
			String name)
	{
		super(id, creatorId, mapView, len, path, meId, name);
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
