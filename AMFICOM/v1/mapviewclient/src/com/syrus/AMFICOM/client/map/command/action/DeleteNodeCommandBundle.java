/**
 * $Id: DeleteNodeCommandBundle.java,v 1.17 2005/02/08 15:11:09 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.mapview.MapView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *  Команда удаления элемента наследника класса MapNodeElement. Команда
 * состоит из  последовательности атомарных действий
 * @author $Author: krupenn $
 * @version $Revision: 1.17 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
 */
public class DeleteNodeCommandBundle extends MapActionCommandBundle
{
	/**
	 * Удаляемый узел
	 */
	AbstractNode node;
	
	/**
	 * Карта, на которой производится операция
	 */
	Map map;

	public DeleteNodeCommandBundle(AbstractNode node)
	{
		this.node = node;
	}
	
	/**
	 * Удалить узел сети
	 */
	protected void deleteSite(SiteNode site)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
		{
			return;
		}

		MapView mapView = this.logicalNetLayer.getMapView();
		
		// если удаляется сетевой узел (не непривязанный элемент),
		// необходимо проверить все кабельные пути, включающие его
		for(Iterator it = mapView.getCablePaths(site).iterator(); it.hasNext();)
		{
			CablePath cablePath = (CablePath)it.next();
			
			// если удаляемый узел содержит привязку концевого элемента
			// кабельного пути, кабельный путь убирается с карты
			if(cablePath.getStartNode().equals(site)
				|| cablePath.getEndNode().equals(site))
			{
				super.removeCablePathLinks(cablePath);
				super.removeCablePath(cablePath);
			}
			else
			// в противном случае прохождение кабельного пути через узел
			// заменяется на непривязанную линию
			{
				PhysicalLink left = null;
				PhysicalLink right = null;
				
				// находятся "левая" и "крайняя" линия пути относительно
				// удаляемого узла. Их будет две, поскольку случай
				// одной линии рассмотрен предыдущим ифом
				for(Iterator it2 = cablePath.getLinks().iterator(); it2.hasNext();)
				{
					PhysicalLink le = (PhysicalLink)it2.next();
					if(le.getStartNode().equals(site)
						|| le.getEndNode().equals(site))
					{
						if(left == null)
							left = le;
						else
							right = le;
					}
				}
				
				// удаляются линии
				cablePath.removeLink(left);
				cablePath.removeLink(right);

				// вместо них создается новая непривязанная
				UnboundLink unbound = 
					super.createUnboundLinkWithNodeLink(
						left.getOtherNode(site),
						right.getOtherNode(site));
				unbound.setCablePath(cablePath);
				cablePath.addLink(unbound, CableController.generateCCI(unbound));

				// если "левая" была непмривязанной, она удаляется (вместе 
				// со своими фрагментами
				if(left instanceof UnboundLink)
				{
					super.removeUnboundLink((UnboundLink)left);
				}
				// если "правая" была непмривязанной, она удаляется (вместе 
				// со своими фрагментами
				if(right instanceof UnboundLink)
				{
					super.removeUnboundLink((UnboundLink)right);
				}
			}
		}

		//При удалении узла удаляются все фрагменты линий, исходящие из него
		java.util.List nodeLinksToDelete = site.getNodeLinks();
		Iterator e = nodeLinksToDelete.iterator();

		// бежим по списку удаляемых фрагментов
		while(e.hasNext())
		{
			NodeLink nodeLink = (NodeLink)e.next();
			PhysicalLink physicalLink = nodeLink.getPhysicalLink();
					
			if(physicalLink instanceof UnboundLink)
			{
				// данный случай обработан в предыдущем цикле
				// (непривязанная линия удалена)
				continue;
			}

			// узел на другом конце фрагмента линии
			AbstractNode oppositeNode = nodeLink.getOtherNode(site);

			// если фрагмент соединяет два узла сети, он составляет одну
			// физическую линию, которую, следовательно, необходимо удалить
			if(oppositeNode instanceof SiteNode)
			{
				super.removeNodeLink(nodeLink);
				super.removePhysicalLink(physicalLink);
			}//if(oppositeNode instanceof MapSiteNodeElement)
			else
			{
				TopologicalNode physicalNode = (TopologicalNode)oppositeNode;
				
				// если топологический узел активен, то фрагмент линии удаляется
				// из физической линии
				if(physicalNode.isActive())
				{
					super.changePhysicalNodeActivity(physicalNode, false);
					
					super.removeNodeLink(nodeLink);

					MapElementState pls = physicalLink.getState();

					physicalLink.removeNodeLink(nodeLink);

					if(physicalLink.getEndNode() == site)
						physicalLink.setEndNode(oppositeNode);
					else
						physicalLink.setStartNode(oppositeNode);

					super.registerStateChange(physicalLink, pls, physicalLink.getState());
				}//if(mpne.isActive())
				else
				// в противном случае фрагмент составляет одну
				// физическую линию, ее необходимо удалить, а также
				// конечный топологический узел
				{
					super.removeNode(physicalNode);
					super.removeNodeLink(nodeLink);
					super.removePhysicalLink(physicalLink);
				}
			}//if ! (oppositeNode instanceof MapSiteNodeElement)
		}//while(e.hasNext())

		super.removeNode(site);
	}
	
	/**
	 * удаление топологического узла
	 */
	public void deletePhysicalNode(TopologicalNode topologicalNode)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			return;

		PhysicalLink physicalLink = topologicalNode.getPhysicalLink();

		// если узел не активен, то есть является концевым узлом физической линии
		if ( !topologicalNode.isActive() )
		{
			//При удалении узла удаляются все фрагменты линий, исходящие из него
			java.util.List nodeLinksToDelete = topologicalNode.getNodeLinks();
			Iterator e = nodeLinksToDelete.iterator();
	
			// бежим по списку удаляемых фрагментов (фактически там только 
			// один элемент)
			while(e.hasNext())
			{
				NodeLink nodeLink = (NodeLink)e.next();
				AbstractNode oppositeNode = nodeLink.getOtherNode(topologicalNode);
						
				// если фрагмент соединяет топологический узел и узел сети
				// то он составляет одну физическую линию, ее необходимо 
				// удалить, а также конечный топологический узел
				if(oppositeNode instanceof SiteNode)
				{
					super.removeNode(topologicalNode);
					super.removeNodeLink(nodeLink);
					super.removePhysicalLink(physicalLink);
				}//if(oppositeNode instanceof MapSiteNodeElement)
				else
				// в противном случае топологический узел - концевой для
				// физической линии. удалить его
				{
					TopologicalNode mpne = (TopologicalNode)oppositeNode;

					super.removeNode(topologicalNode);
					super.removeNodeLink(nodeLink);
					
					MapElementState pls = physicalLink.getState();

					physicalLink.removeNodeLink(nodeLink);

					super.registerStateChange(physicalLink, pls, physicalLink.getState());
					
					// если второй топологический узел тоже не активен, то 
					// имеется физическая лниия из одного фрагмента и двух
					// узлов, все это хозяйство надо удалить
					if(!mpne.isActive())
					{
						super.removeNode(mpne);
						super.removePhysicalLink(physicalLink);
					}
					else
					// в противном случае обновить концевой узел физической линии
					{
						super.changePhysicalNodeActivity(mpne, false);

						MapElementState pls2 = physicalLink.getState();
						
						if(physicalLink.getEndNode() == topologicalNode)
							physicalLink.setEndNode(mpne);
						else
							physicalLink.setStartNode(mpne);

						super.registerStateChange(physicalLink, pls2, physicalLink.getState());
					}
				}// if ! (oppositeNode instanceof MapSiteNodeElement)
			}//while(e.hasNext())
		}//if ( !node.isActive() )
		else
		// если узел активен, то при его удалении два фрагмента сливаются в один
		if ( topologicalNode.isActive() )
		{
			// получить смежные фрагменты линии
			NodeLink nodeLinkLeft = 
					(NodeLink)topologicalNode.getNodeLinks().get(0);
			NodeLink nodeLinkRight = 
					(NodeLink)topologicalNode.getNodeLinks().get(1);
			
			// получить концевые узлы смежных фрагментов
			AbstractNode nodeLeft =
					nodeLinkLeft.getOtherNode(topologicalNode);
			AbstractNode nodeRight =
					nodeLinkRight.getOtherNode(topologicalNode);

			// удалить два фрагмента и связывающий их узел с карты
			super.removeNodeLink(nodeLinkLeft);
			super.removeNodeLink(nodeLinkRight);
			super.removeNode(topologicalNode);

			// В случае, если физическая линия состоит из двух фрагментов,
			// возможны 5 комбинаций:
			// 1. топ узел между двумя сайтами
			// 2. топ узел между топ узлом и сайтом
			// 3. топ узел между двумя концевыми топ узлами
			// 4. топ узел соединенный двумя фрагментами с сайтом
			// 5. топ узел соединенный двумя фрагментами с топ узлом
			//
			// В случаях 1 и 2 обработка такая же, как и в обычном случае
			// (когда больше 2-х фрагментов).
			// В слечаях 3, 4, 5 удаляется физическая линия и все топ. узлы.
			// За управление этой проверкой отвечает переменная do_remove_link
			// и дополнительная соответствующая проверка
			
			boolean doRemoveLink = (nodeLeft == nodeRight)
					|| ((physicalLink.getNodeLinks().size() == 2)
						&& nodeLeft instanceof TopologicalNode
						&& nodeRight instanceof TopologicalNode);

			MapElementState pls = physicalLink.getState();

			//Уделяем два смежных фрагмента из линии и добавить вместо них новый
			physicalLink.removeNodeLink(nodeLinkLeft);
			physicalLink.removeNodeLink(nodeLinkRight);
			
			if(doRemoveLink)
			{
				// удаляется линия
				super.removePhysicalLink(physicalLink);
				if(nodeLeft instanceof TopologicalNode)
				{
					super.removeNode(nodeLeft);
					if(nodeLeft != nodeRight)
						super.removeNode(nodeRight);
				}
			}
			else
			{
				// создать новый фрагмент линии и добавить на карту и в линию
				NodeLink newNodeLink = super.createNodeLink(physicalLink, nodeLeft, nodeRight);
				newNodeLink.setPhysicalLink(physicalLink);

				physicalLink.addNodeLink(newNodeLink);
			}

			super.registerStateChange(physicalLink, pls, physicalLink.getState());

		}//if ( node.isActive() )
	}

	/**
	 * удаление непривязанного узла.
	 * при этом с карты убираются все кабельные пути, содержащие 
	 * удаляемый элемент
	 */
	protected void deleteUnbound(UnboundNode unbound)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_BINDING))
			return;

		MapView mapView = this.logicalNetLayer.getMapView();
	
		super.removeNode(unbound);

		// отдельный список для удаления		
		List cablePaths = new LinkedList();
		cablePaths.addAll(mapView.getCablePaths(unbound));
		
		for(Iterator it = cablePaths.iterator(); it.hasNext();)
		{
			CablePath cpath = (CablePath)it.next();
			super.removeCablePathLinks(cpath);
			super.removeCablePath(cpath);
		}
	}

	/**
	 * Удалить метку на физической линии
	 */
	public void deleteMark(Mark mark)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			return;

		super.removeNode(mark);
	}

	/**
	 * Удалить маркер
	 */
	public void deleteMarker(Marker marker)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_USE_MARKER))
			return;

		getLogicalNetLayer().getMapView().removeMarker(marker);
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		// узел может быть удален в результате атомарной команда в составе
		// другой команды удаления, в этом случае у него будет выставлен
		// флаг isRemoved
		if(this.node.isRemoved())
			return;
			
		this.map = this.logicalNetLayer.getMapView().getMap();
		
		//В зависимости от того какого типа node и от флагов разрешения удаляем
		if ( this.node instanceof UnboundNode)
		{
			this.deleteUnbound((UnboundNode)this.node);
		}
		else
		if ( this.node instanceof SiteNode)
		{
			this.deleteSite((SiteNode)this.node);
		}
		else
		if ( this.node instanceof TopologicalNode)
		{
			this.deletePhysicalNode((TopologicalNode)this.node);
		}
		else
		if ( this.node instanceof Mark)
		{
			this.deleteMark((Mark)this.node);
		}
		else
		if ( this.node instanceof Marker)
		{
			this.deleteMarker((Marker)this.node);
		}

		this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}

}

