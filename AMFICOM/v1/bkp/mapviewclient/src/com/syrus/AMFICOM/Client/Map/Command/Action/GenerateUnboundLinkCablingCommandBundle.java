/**
 * $Id: GenerateUnboundLinkCablingCommandBundle.java,v 1.1 2004/10/14 15:39:05 krupenn Exp $
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

import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Scheme.CableChannelingItem;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLinkBinding;
import java.util.Iterator;
import java.util.LinkedList;
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
public class GenerateUnboundLinkCablingCommandBundle extends MapActionCommandBundle
{
	/**
	 * Удаляемый узел
	 */
	MapCablePathElement path;
	MapUnboundLinkElement unbound;
	MapPhysicalLinkElement link;

	/**
	 * Карта, на которой производится операция
	 */
	MapView mapView;
	Map map;

	public GenerateUnboundLinkCablingCommandBundle(MapUnboundLinkElement unbound)
	{
		this.unbound = unbound;
		this.path = unbound.getCablePath();
	}
	
	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");

		DataSourceInterface dataSource = getLogicalNetLayer().getContext().getDataSource();

		mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();
		
		path.removeLink(unbound);
		link = createPhysicalLink(unbound.getStartNode(), unbound.getEndNode());
		super.removePhysicalLink(unbound);
		for(Iterator it2 = unbound.getNodeLinks().iterator(); it2.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it2.next();
			mnle.setPhysicalLinkId(link.getId());
			link.addNodeLink(mnle);
		}
		path.addLink(link);
		link.getBinding().add(path);

	}

}

