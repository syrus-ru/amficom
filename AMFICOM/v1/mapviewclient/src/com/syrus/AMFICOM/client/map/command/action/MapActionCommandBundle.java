/**
 * $Id: MapActionCommandBundle.java,v 1.3 2004/09/23 10:07:14 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;

import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import java.awt.geom.Point2D;

/**
 *  
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/09/23 10:07:14 $
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
	 * Создается топологический конечный узел в неактивном состоянии
	 */
	protected MapSiteNodeElement createSite(Point2D.Double point,  MapNodeProtoElement proto)
	{
		CreateSiteCommand cmd = new CreateSiteCommand(proto, point);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getSite();
	}

	/**
	 * Создается топологический конечный узел в неактивном состоянии
	 */
	protected MapPhysicalNodeElement createPhysicalNode(Point2D.Double point)
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
	 * Создается линия связи
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
	 * Создается линия связи
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
	 * Удаляется линия связи
	 */
	protected void removeCablePath(MapCablePathElement mcpe)
	{
		RemoveCablePathCommandAtomic cmd = new RemoveCablePathCommandAtomic(mcpe);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * Создается линия связи
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
	 * Удаляется линия связи
	 */
	protected void removeMeasurementPath(MapMeasurementPathElement mpe)
	{
		RemoveMeasurementPathCommandAtomic cmd = new RemoveMeasurementPathCommandAtomic(mpe);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * Создается линия связи
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
	 * Удаляется линия связи
	 */
	protected void removeNodeLink(MapNodeLinkElement mple)
	{
		RemoveNodeLinkCommandAtomic cmd = new RemoveNodeLinkCommandAtomic(mple);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * Удаляется линия связи
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

}
