/**
 * $Id: DeleteNodeCommandBundle.java,v 1.3 2004/10/09 13:33:40 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;

import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import java.util.Iterator;

/**
 *  Команда удаления элемента наследника класса MapNodeElement. Команда
 * состоит из  последовательности атомарных действий
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/09 13:33:40 $
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

		for(Iterator it = mapView.getCablePaths(node).iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			mapView.scanCable(cpath.getSchemeCableLink());
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
			MapNodeElement oppositeNode = nodeLink.getOtherNode(node);

			// если фрагмент соединяет два узла сети, он составляет одну
			// физическую линию, которую, следовательно, необходимо удалить
			if(oppositeNode instanceof MapSiteNodeElement)
			{
				removeNodeLink(nodeLink);
				removePhysicalLink(physicalLink);
			}//if(oppositeNode instanceof MapSiteNodeElement)
			else
			{
				MapPhysicalNodeElement physicalNode = (MapPhysicalNodeElement )oppositeNode;
				
				// если топологический узел активен, то фрагмент линии удаляется
				// из физической линии
				if(physicalNode.isActive())
				{
					changePhysicalNodeActivity(physicalNode, false);
					
					removeNodeLink(nodeLink);

					MapElementState pls = physicalLink.getState();

					physicalLink.removeNodeLink(nodeLink);

					if(physicalLink.getEndNode() == node)
						physicalLink.setEndNode(oppositeNode);
					else
						physicalLink.setStartNode(oppositeNode);

					registerStateChange(physicalLink, pls, physicalLink.getState());
				}//if(mpne.isActive())
				else
				// в противном случае фрагмент составляет одну
				// физическую линию, ее необходимо удалить, а также
				// конечный топологический узел
				{
					removeNode(physicalNode);
					removeNodeLink(nodeLink);
					removePhysicalLink(physicalLink);
				}
			}//if ! (oppositeNode instanceof MapSiteNodeElement)
		}//while(e.hasNext())

		removeNode(node);

		for(Iterator it = mapView.getCablePaths(node).iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			mapView.scanCable(cpath.getSchemeCableLink());
		}
	}
	
	/**
	 * удаление топологического узла
	 */
	public void deletePhysicalNode(MapPhysicalNodeElement node)
	{
		DataSourceInterface dataSource = getContext().getDataSource();

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
					removeNode(node);
					removeNodeLink(nodeLink);
					removePhysicalLink(physicalLink);
				}//if(oppositeNode instanceof MapSiteNodeElement)
				else
				// в противном случае топологический узел - концевой для
				// физической линии. удалить его
				{
					MapPhysicalNodeElement mpne = (MapPhysicalNodeElement )oppositeNode;

					removeNode(node);
					removeNodeLink(nodeLink);
					
					MapElementState pls = physicalLink.getState();

					physicalLink.removeNodeLink(nodeLink);

					registerStateChange(physicalLink, pls, physicalLink.getState());
					
					// если второй топологический узел тоже не активен, то 
					// имеется физическая лниия из одного фрагмента и двух
					// узлов, все это хозяйство надо удалить
					if(!mpne.isActive())
					{
						removeNode(mpne);
						removePhysicalLink(physicalLink);
					}
					else
					// в противном случае обновить концевой узел физической линии
					{
						changePhysicalNodeActivity(mpne, false);

						MapElementState pls2 = physicalLink.getState();
						
						if(physicalLink.getEndNode() == node)
							physicalLink.setEndNode(mpne);
						else
							physicalLink.setStartNode(mpne);

						registerStateChange(physicalLink, pls2, physicalLink.getState());
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
			removeNodeLink(nodeLinkLeft);
			removeNodeLink(nodeLinkRight);
			removeNode(node);

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
			
			boolean do_remove_link = (nodeLeft == nodeRight)
					|| ((physicalLink.getNodeLinks().size() == 2)
						&& nodeLeft instanceof MapPhysicalNodeElement
						&& nodeRight instanceof MapPhysicalNodeElement);

			MapElementState pls = physicalLink.getState();

			//Уделяем два смежных фрагмента из линии и добавить вместо них новый
			physicalLink.removeNodeLink(nodeLinkLeft);
			physicalLink.removeNodeLink(nodeLinkRight);
			
			if(do_remove_link)
			{
				removePhysicalLink(physicalLink);
				if(nodeLeft instanceof MapPhysicalNodeElement)
				{
					removeNode(nodeLeft);
					if(nodeLeft != nodeRight)
						removeNode(nodeRight);
				}
			}
			else
			{
				// создать новый фрагмент линии и добавить на карту и в линию
				MapNodeLinkElement newNodeLink = createNodeLink(nodeLeft, nodeRight);
				newNodeLink.setPhysicalLinkId(physicalLink.getId());

				physicalLink.addNodeLink(newNodeLink);
			}

			registerStateChange(physicalLink, pls, physicalLink.getState());

		}//if ( node.isActive() )
	}

	protected void deleteUnbound(MapUnboundNodeElement unbound)
	{
		MapView mapView = logicalNetLayer.getMapView();
	
		deleteSite(unbound);
		
		for(Iterator it = mapView.getCablePaths(unbound).iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			mapView.scanCable(cpath.getSchemeCableLink());
		}
	}

	/**
	 * Удалить метку на физической линии
	 */
	public void deleteMark(MapMarkElement node)
	{
		if ( !getContext().getApplicationModel().isEnabled("mapActionMarkDelete"))
			return;

		removeNode(node);
	}

	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");

		// узел может быть удален в результате атомарной команда в составе
		// другой команды удаления, в этом случае у него будет выставлен
		// флаг isRemoved
		if(node.isRemoved())
			return;
			
		map = logicalNetLayer.getMapView().getMap();
		
		//В зависимости от того какого типа node и от флагов разрешения удаляем
		if ( node instanceof MapUnboundNodeElement)
		{
			deleteUnbound((MapUnboundNodeElement )node);
		}
		else
		if ( node instanceof MapSiteNodeElement)
		{
			deleteSite((MapSiteNodeElement )node);
		}
		else
		if ( node instanceof MapPhysicalNodeElement)
		{
			deletePhysicalNode((MapPhysicalNodeElement )node);
		}
		else
		if ( node instanceof MapMarkElement)
		{
			deleteMark((MapMarkElement )node);
		}
	}

}

