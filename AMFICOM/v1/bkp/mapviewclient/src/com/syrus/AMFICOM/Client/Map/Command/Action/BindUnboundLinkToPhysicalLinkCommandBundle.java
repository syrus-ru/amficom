/**
 * $Id: BindUnboundLinkToPhysicalLinkCommandBundle.java,v 1.3 2004/10/20 10:14:39 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

/**
 *  команда привязывания непривязанной линии к тоннелю. концевые узлы
 *  неправязанной линии и тоннеля должны совпадать
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/20 10:14:39 $
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
	MapPhysicalLinkElement link;
	
	/**
	 * Карта, на которой производится операция
	 */
	Map map;

	public BindUnboundLinkToPhysicalLinkCommandBundle(
		MapUnboundLinkElement unbound, 
		MapPhysicalLinkElement link)
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

