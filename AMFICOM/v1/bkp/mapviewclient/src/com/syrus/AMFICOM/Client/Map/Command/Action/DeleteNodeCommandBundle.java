/**
 * $Id: DeleteNodeCommandBundle.java,v 1.11 2004/12/23 16:57:59 krupenn Exp $
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
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *  Команда удаления элемента наследника класса MapNodeElement. Команда
 * состоит из  последовательности атомарных действий
 * 
 * 
 * 
 * @version $Revision: 1.11 $, $Date: 2004/12/23 16:57:59 $
 * @module
 * @author $Author: krupenn $
 * @see
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
	protected void deleteSite(SiteNode node)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
		{
			return;
		}

		MapView mapView = logicalNetLayer.getMapView();
		
		// если удаляется сетевой узел (не непривязанный элемент),
		// необходимо проверить все кабельные пути, включающие его
		for(Iterator it = mapView.getCablePaths(node).iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			
			// если удаляемый узел содержит привязку концевого элемента
			// кабельного пути, кабельный путь убирается с карты
			if(cpath.getStartNode().equals(node)
				|| cpath.getEndNode().equals(node))
			{
				super.removeCablePathLinks(cpath);
				super.removeCablePath(cpath);
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
				for(Iterator it2 = cpath.getLinks().iterator(); it2.hasNext();)
				{
					PhysicalLink le = (PhysicalLink)it2.next();
					if(le.getStartNode().equals(node)
						|| le.getEndNode().equals(node))
					{
						if(left == null)
							left = le;
						else
							right = le;
					}
				}
				
				// удаляются линии
				cpath.removeLink(left);
				cpath.removeLink(right);

				// вместо них создается новая непривязанная
				MapUnboundLinkElement unbound = 
					super.createUnboundLinkWithNodeLink(
						left.getOtherNode(node),
						right.getOtherNode(node));
				unbound.setCablePath(cpath);
				cpath.addLink(unbound);

				// если "левая" была непмривязанной, она удаляется (вместе 
				// со своими фрагментами
				if(left instanceof MapUnboundLinkElement)
				{
					super.removeUnboundLink((MapUnboundLinkElement )left);
				}
				// если "правая" была непмривязанной, она удаляется (вместе 
				// со своими фрагментами
				if(right instanceof MapUnboundLinkElement)
				{
					super.removeUnboundLink((MapUnboundLinkElement )right);
				}
			}
		}

		//При удалении узла удаляются все фрагменты линий, исходящие из него
		java.util.List nodeLinksToDelete = node.getNodeLinks();
		Iterator e = nodeLinksToDelete.iterator();

		// бежим по списку удаляемых фрагментов
		while(e.hasNext())
		{
			NodeLink nodeLink = (NodeLink)e.next();
			PhysicalLink physicalLink = nodeLink.getPhysicalLink();
					
			if(physicalLink instanceof MapUnboundLinkElement)
			{
				// данный случай обработан в предыдущем цикле
				// (непривязанная линия удалена)
				continue;
			}

			// узел на другом конце фрагмента линии
			AbstractNode oppositeNode = nodeLink.getOtherNode(node);

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

					if(physicalLink.getEndNode() == node)
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

		super.removeNode(node);
	}
	
	/**
	 * удаление топологического узла
	 */
	public void deletePhysicalNode(TopologicalNode node)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			return;

		PhysicalLink physicalLink = node.getPhysicalLink();

		// если узел не активен, то есть является концевым узлом физической линии
		if ( !node.isActive() )
		{
			//При удалении узла удаляются все фрагменты линий, исходящие из него
			java.util.List nodeLinksToDelete = node.getNodeLinks();
			Iterator e = nodeLinksToDelete.iterator();
	
			// бежим по списку удаляемых фрагментов (фактически там только 
			// один элемент)
			while(e.hasNext())
			{
				NodeLink nodeLink = (NodeLink)e.next();
				AbstractNode oppositeNode = nodeLink.getOtherNode(node);
						
				// если фрагмент соединяет топологический узел и узел сети
				// то он составляет одну физическую линию, ее необходимо 
				// удалить, а также конечный топологический узел
				if(oppositeNode instanceof SiteNode)
				{
					super.removeNode(node);
					super.removeNodeLink(nodeLink);
					super.removePhysicalLink(physicalLink);
				}//if(oppositeNode instanceof MapSiteNodeElement)
				else
				// в противном случае топологический узел - концевой для
				// физической линии. удалить его
				{
					TopologicalNode mpne = (TopologicalNode)oppositeNode;

					super.removeNode(node);
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
						
						if(physicalLink.getEndNode() == node)
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
		if ( node.isActive() )
		{
			// получить смежные фрагменты линии
			NodeLink nodeLinkLeft = 
					(NodeLink)node.getNodeLinks().get(0);
			NodeLink nodeLinkRight = 
					(NodeLink)node.getNodeLinks().get(1);
			
			// получить концевые узлы смежных фрагментов
			AbstractNode nodeLeft =
					(AbstractNode)nodeLinkLeft.getOtherNode(node);
			AbstractNode nodeRight =
					(AbstractNode)nodeLinkRight.getOtherNode(node);

			// удалить два фрагмента и связывающий их узел с карты
			super.removeNodeLink(nodeLinkLeft);
			super.removeNodeLink(nodeLinkRight);
			super.removeNode(node);

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
	protected void deleteUnbound(MapUnboundNodeElement unbound)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_BINDING))
			return;

		MapView mapView = logicalNetLayer.getMapView();
	
		super.removeNode(unbound);

		// отдельный список для удаления		
		List cablePaths = new LinkedList();
		cablePaths.addAll(mapView.getCablePaths(unbound));
		
		for(Iterator it = cablePaths.iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			super.removeCablePathLinks(cpath);
			super.removeCablePath(cpath);
		}
	}

	/**
	 * Удалить метку на физической линии
	 */
	public void deleteMark(Mark node)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			return;

		super.removeNode(node);
	}

	/**
	 * Удалить маркер
	 */
	public void deleteMarker(MapMarker node)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_USE_MARKER))
			return;

		node.getMapView().removeMarker(node);
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
		if(node.isRemoved())
			return;
			
		map = logicalNetLayer.getMapView().getMap();
		
		//В зависимости от того какого типа node и от флагов разрешения удаляем
		if ( node instanceof MapUnboundNodeElement)
		{
			this.deleteUnbound((MapUnboundNodeElement )node);
		}
		else
		if ( node instanceof SiteNode)
		{
			this.deleteSite((SiteNode)node);
		}
		else
		if ( node instanceof TopologicalNode)
		{
			this.deletePhysicalNode((TopologicalNode)node);
		}
		else
		if ( node instanceof Mark)
		{
			this.deleteMark((Mark)node);
		}
		else
		if ( node instanceof MapMarker)
		{
			this.deleteMarker((MapMarker )node);
		}

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}

}

