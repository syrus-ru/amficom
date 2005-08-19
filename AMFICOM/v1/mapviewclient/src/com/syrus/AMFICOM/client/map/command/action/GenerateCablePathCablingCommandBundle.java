/**
 * $Id: GenerateCablePathCablingCommandBundle.java,v 1.32 2005/08/19 15:43:32 krupenn Exp $
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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;

/**
 *  Команда генерации тоннелей в соответствии с прохождением кабеля.
 *  из непроложенных линий генерируются тоннели и кабель привязывается к ним.
 *  Уже существующая привязка сохраняется. По непривязанным элементам 
 *  генерируются сетевые узла и схемные элементы привязываются к ним.
 * @author $Author: krupenn $
 * @version $Revision: 1.32 $, $Date: 2005/08/19 15:43:32 $
 * @module mapviewclient
 */
public class GenerateCablePathCablingCommandBundle extends MapActionCommandBundle
{
	/**
	 * Удаляемый узел
	 */
	CablePath cablePath;
	
	/**
	 * тип узлов для генерации вместо непривязанных элементов
	 */
	SiteNodeType proto;

	/**
	 * Карта, на которой производится операция
	 */
	MapView mapView;

	Map map;

	public GenerateCablePathCablingCommandBundle(
			CablePath cablePath, 
			SiteNodeType proto)
	{
		this.cablePath = cablePath;
		this.proto = proto;
	}
	
	@Override
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		this.mapView = this.logicalNetLayer.getMapView();
		this.map = this.mapView.getMap();
		
		try {
			// для последующего цикла необходима последовательность
			// узлов от начального к конечному
			SiteNode startsite = (SiteNode)this.cablePath.getStartNode();
			SiteNode endsite = null;
			// проверить, что узел является сетевым узлом (если это непривязанный
			// элемент, сгенерировать на его месте сетевой узел)
			startsite = this.checkSite(startsite);
			// отдельный список, поскольку используется удаление
			List list  = new LinkedList();
			list.addAll(this.cablePath.getLinks());
			// цикл по всем линиям, участвующим в кабельном пути
			// по непривязанным линиям генерировать тоннели
			for(Iterator it = list.iterator(); it.hasNext();)
			{
				PhysicalLink link = (PhysicalLink)it.next();

				// перейти к следующему узлу
				if(startsite == link.getEndNode())
					endsite = (SiteNode)link.getStartNode();
				else
					endsite = (SiteNode)link.getEndNode();

				// проверить, что узел является сетевым узлом (если это непривязанный
				// элемент, сгенерировать на его месте сетевой узел)
				endsite = this.checkSite(endsite);

				// если непривязанная линия, генерировать тоннель
				if(link instanceof UnboundLink)
				{
					UnboundLink unbound = (UnboundLink)link;

					link = super.createPhysicalLink(startsite, endsite);
					// фрагменты переносятся в новый сгенерированный тоннель
					for(Iterator it2 = new LinkedList(unbound.getNodeLinks()).iterator(); it2.hasNext();)
					{
						NodeLink mnle = (NodeLink)it2.next();
						mnle.setPhysicalLink(link);
					}

					CableChannelingItem cableChannelingItem = this.cablePath.getFirstCCI(unbound);
					CableChannelingItem newCableChannelingItem = 
						CableController.generateCCI(
								this.cablePath, 
								link,
								startsite,
								endsite);
					newCableChannelingItem.insertSelfBefore(cableChannelingItem);
					cableChannelingItem.setParentPathOwner(null, false);
					this.cablePath.removeLink(cableChannelingItem);
					this.cablePath.addLink(link, newCableChannelingItem);

					super.removePhysicalLink(unbound);
					link.getBinding().add(this.cablePath);
				}

				startsite = endsite;
			}
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}

	/**
	 * проверить, что узел явзяется сетевым узлом.
	 * если он является непривязанным элементом, сгенерировать на его месте
	 * сетевой узел
	 */
	private SiteNode checkSite(SiteNode site)
	{
		SiteNode site2 = site;
		if(site instanceof UnboundNode)
		{
			CreateSiteCommandAtomic command = 
					new CreateSiteCommandAtomic(
						this.proto, 
						site.getLocation());
			command.setLogicalNetLayer(this.logicalNetLayer);
			command.execute();
			super.add(command);
			
			site2 = command.getSite();
	
			BindUnboundNodeToSiteCommandBundle command2 = 
					new BindUnboundNodeToSiteCommandBundle(
						(UnboundNode)site, 
						site2);
			command2.setNetMapViewer(this.netMapViewer);
			command2.execute();
			super.add(command2);

		}
		return site2;
	}

}

