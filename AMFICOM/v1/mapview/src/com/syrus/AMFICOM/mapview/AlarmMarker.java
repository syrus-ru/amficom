/**
 * $Id: AlarmMarker.java,v 1.9 2005/08/09 16:28:40 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.general.Identifier;

/**
 * ������������ ������ ������� �������.
 * @author $Author: arseniy $
 * @version $Revision: 1.9 $, $Date: 2005/08/09 16:28:40 $
 * @module mapview
 * @todo AlarmMarker functionality
 */
public final class AlarmMarker extends Marker {
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
//				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.SITENODE_CODE);
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
