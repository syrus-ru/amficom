/**
 * $Id: AlarmMarker.java,v 1.6 2005/05/18 12:37:39 bass Exp $
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
 * Стационарный маркер сигнала тревоги.
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/05/18 12:37:39 $
 * @module mapviewclient_v1
 * @todo AlarmMarker functionality
 */
public class AlarmMarker extends Marker {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	public AlarmMarker(
			Identifier id,
			Identifier creatorId,
			MapView mapView,
			double len,
			MeasurementPath path,
			Identifier meId,
			String name) {
		super(id, creatorId, mapView, len, path, meId, name);
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
