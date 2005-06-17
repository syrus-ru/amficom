/**
 * $Id: AlarmMarker.java,v 1.7 2005/06/17 11:01:07 bass Exp $
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
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/06/17 11:01:07 $
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
