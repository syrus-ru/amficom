/**
 * $Id: UnPlaceSchemeCableLinkCommand.java,v 1.2 2004/10/18 15:33:00 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;

/**
 * ���������� ������� ���� mpe �� �����. ������������ ��� �������� 
 * (drag/drop), � ����� point (� �������� �����������)
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/18 15:33:00 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class UnPlaceSchemeCableLinkCommand extends MapActionCommandBundle
{
	MapCablePathElement cablePath = null;
	
//	Map map;
//	MapView mapView;

	public UnPlaceSchemeCableLinkCommand(MapCablePathElement cablePath)
	{
		super();
		this.cablePath = cablePath;
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

//		mapView = logicalNetLayer.getMapView();
//		map = mapView.getMap();

		super.removeCablePathLinks(cablePath);

		super.removeCablePath(cablePath);

		// �������� ��������� - ���������� ����������
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
//		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
//				cablePath, 
//				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
//		logicalNetLayer.setCurrentMapElement(cablePath);
//		logicalNetLayer.notifySchemeEvent(cablePath);
//		logicalNetLayer.notifyCatalogueEvent(cablePath);
	}
}
