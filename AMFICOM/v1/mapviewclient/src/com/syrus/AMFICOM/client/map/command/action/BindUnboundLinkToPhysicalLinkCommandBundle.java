/**
 * $Id: BindUnboundLinkToPhysicalLinkCommandBundle.java,v 1.7 2005/01/31 12:19:18 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;

/**
 *  команда привязывания непривязанной линии к тоннелю. концевые узлы
 *  неправязанной линии и тоннеля должны совпадать
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2005/01/31 12:19:18 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class BindUnboundLinkToPhysicalLinkCommandBundle extends MapActionCommandBundle
{
	/**
	 * привязываемая линия
	 */
	UnboundLink unbound;
	
	/**
	 * тоннель
	 */
	PhysicalLink link;
	
	/**
	 * Карта, на которой производится операция
	 */
	Map map;

	public BindUnboundLinkToPhysicalLinkCommandBundle(
		UnboundLink unbound, 
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
		CablePath cablePath = unbound.getCablePath();
		cablePath.removeLink(unbound);
		CableController cableController = (CableController )
			getLogicalNetLayer().getMapViewController().getController(cablePath);
		cablePath.addLink(link, cableController.generateCCI(link));
		link.getBinding().add(cablePath);

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
	
}

