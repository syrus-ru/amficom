/**
 * $Id: GenerateCablePathCablingCommandBundle.java,v 1.15 2005/02/08 15:11:09 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;

/**
 *  Команда генерации тоннелей в соответствии с прохождением кабеля.
 *  из непроложенных линий генерируются тоннели и кабель привязывается к ним.
 *  Уже существующая привязка сохраняется. По непривязанным элементам 
 *  генерируются сетевые узла и схемные элементы привязываются к ним.
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
 */
public class GenerateCablePathCablingCommandBundle extends MapActionCommandBundle
{
	/**
	 * Удаляемый узел
	 */
	CablePath path;
	
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
			CablePath path, 
			SiteNodeType proto)
	{
		this.path = path;
		this.proto = proto;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		this.mapView = this.logicalNetLayer.getMapView();
		this.map = this.mapView.getMap();
		
		// для последующего цикла необходима последовательность
		// узлов от начального к конечному
		SiteNode startsite = (SiteNode)this.path.getStartNode();
		SiteNode endsite = null;
		
		// проверить, что узел является сетевым узлом (если это непривязанный
		// элемент, сгенерировать на его месте сетевой узел)
		startsite = this.checkSite(startsite);

		// отдельный список, поскольку используется удаление
		List list  = new LinkedList();
		list.addAll(this.path.getLinks());

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
				this.path.removeLink(link);
				UnboundLink un = (UnboundLink)link;
				super.removePhysicalLink(un);

				link = super.createPhysicalLink(startsite, endsite);
				// фрагменты переносятся в новый сгенерированный тоннель
				for(Iterator it2 = un.getNodeLinks().iterator(); it2.hasNext();)
				{
					NodeLink mnle = (NodeLink)it2.next();
					mnle.setPhysicalLink(link);
					link.addNodeLink(mnle);
				}
				this.path.addLink(link, CableController.generateCCI(link));
				link.getBinding().add(this.path);
			}

			startsite = endsite;
		}

		this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
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
			command2.setLogicalNetLayer(this.logicalNetLayer);
			command2.execute();
			super.add(command2);

		}
		return site2;
	}

}

