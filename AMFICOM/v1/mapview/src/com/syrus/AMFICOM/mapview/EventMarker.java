/*-
 * $Id: EventMarker.java,v 1.11 2005/08/24 15:00:30 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.general.Identifier;

/**
 * Стационарный маркер собития на пути.
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2005/08/24 15:00:30 $
 * @module mapview
 * @todo EventMarker functionality
 */
public final class EventMarker extends Marker {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3834312821583655217L;

	public EventMarker(
			Identifier id,
			Identifier creatorId,
			MapView mapView,
			MeasurementPath path,
			Identifier meId,
			String name) {
		super(id, creatorId, mapView, path, meId, name);
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
//				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.SITENODE_CODE);
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
