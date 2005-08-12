/**
 * $Id: BindPhysicalNodeToSiteCommandBundle.java,v 1.30 2005/08/12 10:33:30 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;

/**
 *  Команда привязывания топологического узла, принадлежащего
 *  непривязанному кабелю, к элементу узла. При этом линия, которой 
 *  принадлежит данный узел, делится на 2 части
 * @author $Author: krupenn $
 * @version $Revision: 1.30 $, $Date: 2005/08/12 10:33:30 $
 * @module mapclient_v1
 */
public class BindPhysicalNodeToSiteCommandBundle extends MapActionCommandBundle
{
	/**
	 * Перетаскиваемый узел
	 */
	TopologicalNode node;
	
	/** Узел, к которому привязывается топологический узел */
	SiteNode site;

	/**
	 * Вид, на котором производится операция
	 */
	MapView mapView;

	/**
	 * Карта, на которой производится операция
	 */
	Map map;

	public BindPhysicalNodeToSiteCommandBundle(
			TopologicalNode node, 
			SiteNode site)
	{
		this.node = node;
		this.site = site;
	}
	
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		try
		{
			this.mapView = this.logicalNetLayer.getMapView();
			this.map = this.mapView.getMap();
			UnboundLink link = (UnboundLink)this.node.getPhysicalLink();
			// находим "ливый" и "правый" узлы, одновременно обновляем
			// концевые узлы фрагментов
			for(Iterator it = this.map.getNodeLinks(this.node).iterator(); it.hasNext();)
			{
				NodeLink nodeLink = (NodeLink)it.next();

				if(nodeLink.getStartNode().equals(this.node))
					nodeLink.setStartNode(this.site);
				else
					nodeLink.setEndNode(this.site);
			}
			// топологический узел удаляется
			super.removeNode(this.node);
			// обновляются концевые узлы линии
			if(link.getStartNode().equals(this.node))
				link.setStartNode(this.site);
			else
			if(link.getEndNode().equals(this.node))
				link.setEndNode(this.site);
			// создается вторая линия
			UnboundLink newLink = super.createUnboundLink(link.getStartNode(), this.site);
			newLink.setType(link.getType());
			// single cpath, as long as link is UnboundLink
			CablePath cablePath = link.getCablePath();

//			CableChannelingItem cableChannelingItem = cablePath.getFirstCCI(link);
			for(CableChannelingItem cableChannelingItem : cablePath.getSchemeCableLink().getPathMembers()) {
				if(cablePath.getBinding().get(cableChannelingItem) == link) {
					CableChannelingItem newCableChannelingItem = 
						CableController.generateCCI(
								cablePath, 
								newLink,
								cableChannelingItem.getStartSiteNode(),
								this.site);
					newCableChannelingItem.insertSelfBefore(cableChannelingItem);
					// новая линия добавляется в кабельный путь
					cablePath.addLink(newLink, newCableChannelingItem);
					cableChannelingItem.setStartSiteNode(this.site);
				}
			}

			newLink.setCablePath(cablePath);
			// переносим фрагменты в новую линию пока не наткнемся на
			// созданный узел
			super.moveNodeLinks(
					this.map,
					link,
					newLink,
					false,
					this.site,
					null);
			link.setStartNode(this.site);
		}
		catch(Throwable e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
}

