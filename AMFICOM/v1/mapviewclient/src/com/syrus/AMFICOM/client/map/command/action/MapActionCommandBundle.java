/**
 * $Id: MapActionCommandBundle.java,v 1.14 2005/01/21 16:19:57 krupenn Exp $
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
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundLink;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;
import com.syrus.AMFICOM.scheme.corba.SchemePath;

import java.awt.geom.Point2D;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;

/**
 *  
 * 
 * 
 * 
 * @version $Revision: 1.14 $, $Date: 2005/01/21 16:19:57 $
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
	protected SiteNode createSite(DoublePoint point,  SiteNodeType proto)
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
	protected UnboundNode createUnboundNode(DoublePoint point, SchemeElement se)
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
	protected TopologicalNode createPhysicalNode(PhysicalLink physicalLink, DoublePoint point)
	{
		CreatePhysicalNodeCommandAtomic cmd = new CreatePhysicalNodeCommandAtomic(physicalLink, point);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getNode();
	}

	/**
	 * Создается фрагмент линии, не включенный ни в какую линию
	 */
	protected NodeLink createNodeLink(
			PhysicalLink physicalLink,
			AbstractNode startNode,
			AbstractNode endNode)
	{
		CreateNodeLinkCommandAtomic cmd = new CreateNodeLinkCommandAtomic(physicalLink, startNode, endNode);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getNodeLink();
	}

	/**
	 * Создается элемент линии связи
	 */
	protected PhysicalLink createPhysicalLink(
			AbstractNode startNode,
			AbstractNode endNode)
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
	protected CablePath createCablePath(
			SchemeCableLink scl,
			AbstractNode startNode,
			AbstractNode endNode)
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
	protected void removeCablePath(CablePath mcpe)
	{
		RemoveCablePathCommandAtomic cmd = new RemoveCablePathCommandAtomic(mcpe);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * Создается измерительный путь
	 */
	protected MeasurementPath createMeasurementPath(
			SchemePath path,
			AbstractNode startNode,
			AbstractNode endNode)
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
	protected void removeMeasurementPath(MeasurementPath mpe)
	{
		RemoveMeasurementPathCommandAtomic cmd = new RemoveMeasurementPathCommandAtomic(mpe);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * Создается непривязанная линия
	 */
	protected UnboundLink createUnboundLink(
			AbstractNode startNode,
			AbstractNode endNode)
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
	protected UnboundLink createUnboundLinkWithNodeLink(
			AbstractNode startNode,
			AbstractNode endNode)
	{
		UnboundLink unbound = this.createUnboundLink(startNode, endNode);

		NodeLink nodeLink = this.createNodeLink(unbound, startNode, endNode);
		unbound.addNodeLink(nodeLink);
		nodeLink.setPhysicalLink(unbound);
		
		return unbound;
	}

	
	/**
	 * Удаляется линия связи
	 */
	protected void removePhysicalLink(PhysicalLink mple)
	{
		RemovePhysicalLinkCommandAtomic cmd = new RemovePhysicalLinkCommandAtomic(mple);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * Удаляется фрагмент линии
	 */
	protected void removeNodeLink(NodeLink mple)
	{
		RemoveNodeLinkCommandAtomic cmd = new RemoveNodeLinkCommandAtomic(mple);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * Удаляется точечный объект
	 */
	protected void removeNode(AbstractNode mne)
	{
		RemoveNodeCommandAtomic cmd = new RemoveNodeCommandAtomic(mne);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * Изменение статуса топологического узла
	 */
	protected void changePhysicalNodeActivity(TopologicalNode mpne, boolean active)
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
	protected void removeUnboundLink(UnboundLink link)
	{
		this.removePhysicalLink(link);
		link.sortNodes();
		List sortedNodes = new LinkedList();
		sortedNodes.addAll(link.getSortedNodes());
		for(Iterator it2 = sortedNodes.iterator(); it2.hasNext();)
		{
			AbstractNode node = (AbstractNode)it2.next();
			if(node instanceof TopologicalNode)
				this.removeNode(node);
		}
		List sortedNodeLinks = new LinkedList();
		sortedNodeLinks.addAll(link.getNodeLinks());
		for(Iterator it3 = sortedNodeLinks.iterator(); it3.hasNext();)
		{
			this.removeNodeLink((NodeLink)it3.next());
		}
	}

	/**
	 * удаляется информация о привязке кабеля. от тоннелей кабель
	 * отвязывается, непривязанные линии удаляются
	 */
	protected void removeCablePathLinks(CablePath cablePath)
	{
		for(Iterator it = cablePath.getLinks().iterator(); it.hasNext();)
		{
			PhysicalLink link = (PhysicalLink)it.next();
			if(link instanceof UnboundLink)
			{
				this.removeUnboundLink((UnboundLink)link);
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
	protected void removeMeasurementPathCables(MeasurementPath mPath)
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
	protected AbstractNode moveNodeLinks(
		PhysicalLink link, 
		PhysicalLink newLink,
		boolean registerStateChange,
		AbstractNode firstConditionalNodeExit,
		AbstractNode secondConditionalNodeExit)
	{
		AbstractNode foundNode = null;
		MapElementState state = null;
		
		// определить начальный узел и начальный фрагмент физической линии
		NodeLink startNodeLink = link.getStartNodeLink();
		AbstractNode startNode = link.getStartNode();

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
			
			startNodeLink.setPhysicalLink(newLink);
			
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
