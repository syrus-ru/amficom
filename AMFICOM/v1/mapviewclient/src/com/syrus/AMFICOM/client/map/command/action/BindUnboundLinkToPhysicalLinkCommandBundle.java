/**
 * $Id: BindUnboundLinkToPhysicalLinkCommandBundle.java,v 1.1 2004/10/14 15:39:05 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLinkBinding;
import java.util.Iterator;
import java.util.List;

/**
 *  Команда удаления элемента наследника класса MapNodeElement. Команда
 * состоит из  последовательности атомарных действий
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/10/14 15:39:05 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class BindUnboundLinkToPhysicalLinkCommandBundle extends MapActionCommandBundle
{
	/**
	 * Удаляемый узел
	 */
	MapUnboundLinkElement unbound;
	MapPhysicalLinkElement link;
	
	/**
	 * Карта, на которой производится операция
	 */
	Map map;

	public BindUnboundLinkToPhysicalLinkCommandBundle(MapUnboundLinkElement unbound, MapPhysicalLinkElement link)
	{
		this.unbound = unbound;
		this.link = link;
	}
	
	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");

		DataSourceInterface dataSource =
				getLogicalNetLayer()
						.getContext()
							.getDataSource();

		MapView mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();
		
		List cablePaths = logicalNetLayer.getMapView().getCablePaths(unbound);

		super.removeUnboundLink(unbound);
		
		for(Iterator it = cablePaths.iterator(); it.hasNext();)
		{
			MapCablePathElement cp = (MapCablePathElement )it.next();
			cp.removeLink(unbound);
			cp.addLink(link);
			link.getBinding().add(cp);
		}

		logicalNetLayer.repaint();
	}
	
}

