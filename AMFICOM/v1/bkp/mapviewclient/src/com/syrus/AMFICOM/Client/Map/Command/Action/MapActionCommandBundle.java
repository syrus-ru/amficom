/**
 * $Id: MapActionCommandBundle.java,v 1.10 2004/12/07 17:05:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Command.CommandBundle;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

import java.awt.geom.Point2D;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *  
 * 
 * 
 * 
 * @version $Revision: 1.10 $, $Date: 2004/12/07 17:05:54 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapActionCommandBundle extends CommandBundle
{
	LogicalNetLayer logicalNetLayer = null;
	ApplicationContext aContext = null;
	
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.aContext = logicalNetLayer.getContext();
	}
	
	protected ApplicationContext getContext()
	{
		return aContext;
	}
	
	protected LogicalNetLayer getLogicalNetLayer()
	{
		return logicalNetLayer;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("logicalNetLayer"))
			logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("applicationContext"))
			aContext = (ApplicationContext )value;
		super.setParameter(field, value);
	}
	
	/**
	 * Создается сетевой узел
	 */
	protected MapSiteNodeElement createSite(DoublePoint point,  MapNodeProtoElement proto)
	{
		CreateSiteCommandAtomic cmd = new CreateSiteCommandAtomic(proto, point);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getSite();
	}

	/**
	 * Создается непривязанный элемент
	 */
	protected MapUnboundNodeElement createUnboundNode(DoublePoint point, SchemeElement se)
	{
		CreateUnboundNodeCommandAtomic cmd = new CreateUnboundNodeCommandAtomic(se, point);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getUnbound();
	}

	/**
	 * Создается топологический конечный узел в неактивном состоянии
	 */
	protected MapPhysicalNodeElement createPhysicalNode(DoublePoint point)
	{
		CreatePhysicalNodeCommandAtomic cmd = new CreatePhysicalNodeCommandAtomic(point);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getNode();
	}

	/**
	 * Создается фрагмент линии, не включенный ни в какую линию
	 */
	protected MapNodeLinkElement createNodeLink(
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		CreateNodeLinkCommandAtomic cmd = new CreateNodeLinkCommandAtomic(startNode, endNode);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getNodeLink();
	}

	/**
	 * Создается элемент линии связи
	 */
	protected MapPhysicalLinkElement createPhysicalLink(
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		CreatePhysicalLinkCommandAtomic cmd = new CreatePhysicalLinkCommandAtomic(startNode, endNode);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getLink();
	}
	
	/**
	 * Создается кабельный путь
	 */
	protected MapCablePathElement createCablePath(
			SchemeCableLink scl,
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		CreateCablePathCommandAtomic cmd = new CreateCablePathCommandAtomic(scl, startNode, endNode);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getPath();
	}
	
	/**
	 * Удаляется кабельный путь
	 */
	protected void removeCablePath(MapCablePathElement mcpe)
	{
		RemoveCablePathCommandAtomic cmd = new RemoveCablePathCommandAtomic(mcpe);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * Создается измерительный путь
	 */
	protected MapMeasurementPathElement createMeasurementPath(
			SchemePath path,
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		CreateMeasurementPathCommandAtomic cmd = new CreateMeasurementPathCommandAtomic(path, startNode, endNode);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getPath();
	}
	
	/**
	 * Удаляется измерительный путь
	 */
	protected void removeMeasurementPath(MapMeasurementPathElement mpe)
	{
		RemoveMeasurementPathCommandAtomic cmd = new RemoveMeasurementPathCommandAtomic(mpe);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * Создается непривязанная линия
	 */
	protected MapUnboundLinkElement createUnboundLink(
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		CreateUnboundLinkCommandAtomic cmd = new CreateUnboundLinkCommandAtomic(startNode, endNode);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getLink();
	}
	
	/**
	 * Создается непривязанная линия, состоящая из одного фрагмента
	 */
	protected MapUnboundLinkElement createUnboundLinkWithNodeLink(
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		MapUnboundLinkElement unbound = this.createUnboundLink(startNode, endNode);

		MapNodeLinkElement nodeLink = this.createNodeLink(startNode, endNode);
		unbound.addNodeLink(nodeLink);
		nodeLink.setPhysicalLinkId(unbound.getId());
		
		return unbound;
	}

	
	/**
	 * Удаляется линия связи
	 */
	protected void removePhysicalLink(MapPhysicalLinkElement mple)
	{
		RemovePhysicalLinkCommandAtomic cmd = new RemovePhysicalLinkCommandAtomic(mple);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * Удаляется фрагмент линии
	 */
	protected void removeNodeLink(MapNodeLinkElement mple)
	{
		RemoveNodeLinkCommandAtomic cmd = new RemoveNodeLinkCommandAtomic(mple);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * Удаляется точечный объект
	 */
	protected void removeNode(MapNodeElement mne)
	{
		RemoveNodeCommandAtomic cmd = new RemoveNodeCommandAtomic(mne);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * Изменение статуса топологического узла
	 */
	protected void changePhysicalNodeActivity(MapPhysicalNodeElement mpne, boolean active)
	{
		ChangePhysicalNodeActivityCommandAtomic cmd = new ChangePhysicalNodeActivityCommandAtomic(mpne, active);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}
	
	/**
	 * Изменить состояние объекта, запомнив предыдущее
	 */
	protected void registerStateChange(MapElement me, MapElementState initialState, MapElementState finalState)
	{
		MapElementStateChangeCommand cmd = new MapElementStateChangeCommand(me, initialState, finalState);
		cmd.setLogicalNetLayer(logicalNetLayer);
		add(cmd);
	}

	/**
	 * удаляется непривязанная линия, включая ее внутренние
	 * топологические узлы и фрагменты
	 */
	protected void removeUnboundLink(MapUnboundLinkElement link)
	{
		this.removePhysicalLink(link);
		link.sortNodes();
		List sortedNodes = new LinkedList();
		sortedNodes.addAll(link.getSortedNodes());
		for(Iterator it2 = sortedNodes.iterator(); it2.hasNext();)
		{
			MapNodeElement node = (MapNodeElement )it2.next();
			if(node instanceof MapPhysicalNodeElement)
				this.removeNode(node);
		}
		List sortedNodeLinks = new LinkedList();
		sortedNodeLinks.addAll(link.getNodeLinks());
		for(Iterator it3 = sortedNodeLinks.iterator(); it3.hasNext();)
		{
			this.removeNodeLink((MapNodeLinkElement )it3.next());
		}
	}

	/**
	 * удаляется информация о привязке кабеля. от тоннелей кабель
	 * отвязывается, непривязанные линии удаляются
	 */
	protected void removeCablePathLinks(MapCablePathElement cablePath)
	{
		for(Iterator it = cablePath.getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			if(link instanceof MapUnboundLinkElement)
			{
				this.removeUnboundLink((MapUnboundLinkElement )link);
			}
			else
			{
				link.getBinding().remove(cablePath);
			}
		}
		cablePath.clearLinks();
	}

	/**
	 * удаляется измерительный путь
	 */
	protected void removeMeasurementPathCables(MapMeasurementPathElement mPath)
	{
//		mPath.clearCablePaths();
	}


	/**
	 * фрагменты линии link переносятся в линию newLink начиная от начального
	 * фрагмента, пока не встретится узел firstConditionalNodeExit или
	 * secondConditionalNodeExit. Возвращается встреченный узел.
	 * 
	 * @param link
	 * @param newLink
	 * @param registerStateChange
	 * @param firstConditionalNodeExit
	 * @param secondConditionalNodeExit
	 * @return 
	 */
	protected MapNodeElement moveNodeLinks(
		MapPhysicalLinkElement link, 
		MapPhysicalLinkElement newLink,
		boolean registerStateChange,
		MapNodeElement firstConditionalNodeExit,
		MapNodeElement secondConditionalNodeExit)
	{
		MapNodeElement foundNode = null;
		MapElementState state = null;
		
		// определить начальный узел и начальный фрагмент физической линии
		MapNodeLinkElement startNodeLink = link.getStartNodeLink();
		MapNodeElement startNode = link.getStartNode();

		// неявный цикл по фракментам линии - перекидывать фрагменты в новую 
		// физическую линию. движемся по фрагментам от первого пока не
		// наткнемся на фрагмент, соседний с удаленным
		for(;;)
		{
			// перекинуть фрагмент в новую линию
			link.removeNodeLink(startNodeLink);
			newLink.addNodeLink(startNodeLink);
			
			if(registerStateChange)
				state = startNodeLink.getState();
			
			startNodeLink.setPhysicalLinkId(newLink.getId());
			
			if(registerStateChange)
				this.registerStateChange(
					startNodeLink, 
					state, 
					startNodeLink.getState());
			
			// перейти к следующему узлу
			startNode = startNodeLink.getOtherNode(startNode);
			
			// если наткнулись на разрыв линии связи, то обновить
			// концевые узлы и закончить
			if(startNode.equals(firstConditionalNodeExit))
			{
				foundNode = firstConditionalNodeExit;
				newLink.setEndNode(firstConditionalNodeExit);
				break;
			}
			else
			if(startNode.equals(secondConditionalNodeExit))
			{
				foundNode = secondConditionalNodeExit;
				newLink.setEndNode(secondConditionalNodeExit);
				break;
			}

			startNodeLink = startNode.getOtherNodeLink(startNodeLink);
		}//for(;;)
		
		return foundNode;
	}
}
