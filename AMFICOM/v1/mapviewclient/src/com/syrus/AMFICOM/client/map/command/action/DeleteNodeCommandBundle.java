/**
 * $Id: DeleteNodeCommandBundle.java,v 1.6 2004/10/18 15:33:00 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
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
 * @version $Revision: 1.6 $, $Date: 2004/10/18 15:33:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class DeleteNodeCommandBundle extends MapActionCommandBundle
{
	/**
	 * Удаляемый узел
	 */
	MapNodeElement node;
	
	/**
	 * Карта, на которой производится операция
	 */
	Map map;

	public DeleteNodeCommandBundle(MapNodeElement node)
	{
		this.node = node;
	}
	
	/**
	 * Удалить узел сети
	 */
	protected void deleteSite(MapSiteNodeElement node)
	{
		if ( !getContext().getApplicationModel().isEnabled("mapActionDeleteEquipment"))
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
				MapPhysicalLinkElement left = null;
				MapPhysicalLinkElement right = null;
				
				// находятся "левая" и "крайняя" линия пути относительно
				// удаляемого узла. Их будет две, поскольку случай
				// одной линии рассмотрен предыдущим ифом
				for(Iterator it2 = cpath.getLinks().iterator(); it2.hasNext();)
				{
					MapPhysicalLinkElement le = (MapPhysicalLinkElement )it2.next();
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
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )e.next();
			MapPhysicalLinkElement physicalLink = map
					.getPhysicalLink(nodeLink.getPhysicalLinkId());
					
			if(physicalLink instanceof MapUnboundLinkElement)
			{
				// данный случай обработан в предыдущем цикле
				// (непривязанная линия удалена)
				continue;
			}

			// узел на другом конце фрагмента линии
			MapNodeElement oppositeNode = nodeLink.getOtherNode(node);

			// если фрагмент соединяет два узла сети, он составляет одну
			// физическую линию, которую, следовательно, необходимо удалить
			if(oppositeNode instanceof MapSiteNodeElement)
			{
				super.removeNodeLink(nodeLink);
				super.removePhysicalLink(physicalLink);
			}//if(oppositeNode instanceof MapSiteNodeElement)
			else
			{
				MapPhysicalNodeElement physicalNode = (MapPhysicalNodeElement )oppositeNode;
				
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
	public void deletePhysicalNode(MapPhysicalNodeElement node)
	{
		if ( !getContext().getApplicationModel().isEnabled("mapActionDeleteNode"))
			return;

		MapPhysicalLinkElement physicalLink = map
				.getPhysicalLink(node.getPhysicalLinkId());

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
				MapNodeLinkElement nodeLink = (MapNodeLinkElement )e.next();
				MapNodeElement oppositeNode = nodeLink.getOtherNode(node);
						
				// если фрагмент соединяет топологический узел и узел сети
				// то он составляет одну физическую линию, ее необходимо 
				// удалить, а также конечный топологический узел
				if(oppositeNode instanceof MapSiteNodeElement)
				{
					super.removeNode(node);
					super.removeNodeLink(nodeLink);
					super.removePhysicalLink(physicalLink);
				}//if(oppositeNode instanceof MapSiteNodeElement)
				else
				// в противном случае топологический узел - концевой для
				// физической линии. удалить его
				{
					MapPhysicalNodeElement mpne = (MapPhysicalNodeElement )oppositeNode;

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
			MapNodeLinkElement nodeLinkLeft = 
					(MapNodeLinkElement )node.getNodeLinks().get(0);
			MapNodeLinkElement nodeLinkRight = 
					(MapNodeLinkElement )node.getNodeLinks().get(1);
			
			// получить концевые узлы смежных фрагментов
			MapNodeElement nodeLeft =
					(MapNodeElement )nodeLinkLeft.getOtherNode(node);
			MapNodeElement nodeRight =
					(MapNodeElement )nodeLinkRight.getOtherNode(node);

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
						&& nodeLeft instanceof MapPhysicalNodeElement
						&& nodeRight instanceof MapPhysicalNodeElement);

			MapElementState pls = physicalLink.getState();

			//Уделяем два смежных фрагмента из линии и добавить вместо них новый
			physicalLink.removeNodeLink(nodeLinkLeft);
			physicalLink.removeNodeLink(nodeLinkRight);
			
			if(doRemoveLink)
			{
				// удаляется линия
				super.removePhysicalLink(physicalLink);
				if(nodeLeft instanceof MapPhysicalNodeElement)
				{
					super.removeNode(nodeLeft);
					if(nodeLeft != nodeRight)
						super.removeNode(nodeRight);
				}
			}
			else
			{
				// создать новый фрагмент линии и добавить на карту и в линию
				MapNodeLinkElement newNodeLink = super.createNodeLink(nodeLeft, nodeRight);
				newNodeLink.setPhysicalLinkId(physicalLink.getId());

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
	public void deleteMark(MapMarkElement node)
	{
		if ( !getContext().getApplicationModel().isEnabled("mapActionMarkDelete"))
			return;

		super.removeNode(node);
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
		if ( node instanceof MapSiteNodeElement)
		{
			this.deleteSite((MapSiteNodeElement )node);
		}
		else
		if ( node instanceof MapPhysicalNodeElement)
		{
			this.deletePhysicalNode((MapPhysicalNodeElement )node);
		}
		else
		if ( node instanceof MapMarkElement)
		{
			this.deleteMark((MapMarkElement )node);
		}
	}

}

