/**
 * $Id: BindUnboundLinkToPhysicalLinkCommandBundle.java,v 1.4 2004/12/22 16:38:39 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;

/**
 *  команда привязывания непривязанной линии к тоннелю. концевые узлы
 *  неправязанной линии и тоннеля должны совпадать
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/22 16:38:39 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class BindUnboundLinkToPhysicalLinkCommandBundle extends MapActionCommandBundle
{
	/**
	 * привязываемая линия
	 */
	MapUnboundLinkElement unbound;
	
	/**
	 * тоннель
	 */
	PhysicalLink link;
	
	/**
	 * Карта, на которой производится операция
	 */
	Map map;

	public BindUnboundLinkToPhysicalLinkCommandBundle(
		MapUnboundLinkElement unbound, 
		PhysicalLink link)
	{
		this.unbound = unbound;
		this.link = link;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		MapView mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();
		
		// удаляется непривязанная линия
		super.removeUnboundLink(unbound);
		
		// одновляется информация о привязке кабульного пути
		MapCablePathElement cp = unbound.getCablePath();
		cp.removeLink(unbound);
		cp.addLink(link);
		link.getBinding().add(cp);

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
	
}

