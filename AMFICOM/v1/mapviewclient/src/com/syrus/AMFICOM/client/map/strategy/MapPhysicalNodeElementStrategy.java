/**
 * $Id: MapPhysicalNodeElementStrategy.java,v 1.15 2005/02/02 08:57:28 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Command.Action.BindPhysicalNodeToSiteCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateNodeLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveFixedDistanceCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.Client.Map.Controllers.SiteNodeController;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;

import java.awt.Point;

import java.util.Iterator;

/**
 * Стратегия управления топологическим узлом.
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/02/02 08:57:28 $
 * @module mapviewclient_v1
 */
public final class MapPhysicalNodeElementStrategy extends MapStrategy 
{
	/**
	 * Топологический узел.
	 */
	TopologicalNode node;
	/**
	 * Команда, выполняемая в соответствии со стратегией действий на узлом.
	 */
	Command command;

	/**
	 * Instance.
	 */
	private static MapPhysicalNodeElementStrategy instance = new MapPhysicalNodeElementStrategy();

	/**
	 * Private constructor.
	 */
	private MapPhysicalNodeElementStrategy()
	{
	}

	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapPhysicalNodeElementStrategy getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
		this.node = (TopologicalNode)me;
	}

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.aContext = logicalNetLayer.getContext();
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMousePressed(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();

		if ((actionMode == MapState.SELECT_ACTION_MODE))
		{
			MapElement mel = logicalNetLayer.getCurrentMapElement();
			if (mel instanceof Selection)
			{
				Selection sel = (Selection)mel;
				sel.add(node);
			}
			else
			{
				Selection sel = new Selection(node.getMap());
				sel.addAll(logicalNetLayer.getSelectedElements());
				logicalNetLayer.setCurrentMapElement(sel);
			}
		}//MapState.SELECT_ACTION_MODE
		if ((actionMode != MapState.SELECT_ACTION_MODE) && (actionMode != MapState.MOVE_ACTION_MODE))
		{
			logicalNetLayer.deselectAll();
		}// ! MapState.SELECT_ACTION_MODE && ! MapState.MOVE_ACTION_MODE
		node.setSelected(true);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMouseDragged(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();
		int operationMode = mapState.getOperationMode();

		if (operationMode == MapState.MOVE_FIXDIST)
		{
			if (command == null)
			{
				command = new MoveFixedDistanceCommand(point, logicalNetLayer.getFixedNode(), node);
				((MoveSelectionCommandBundle)command).setLogicalNetLayer(logicalNetLayer);
			}
			command.setParameter(MoveSelectionCommandBundle.END_POINT, point);
		}//MapState.MOVE_FIXDIST
		else if (actionMode == MapState.MOVE_ACTION_MODE)
		{
			if (aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			{
				if (command == null)
				{
					command = new MoveSelectionCommandBundle(logicalNetLayer.getStartPoint());
					((MoveSelectionCommandBundle)command).setLogicalNetLayer(logicalNetLayer);
				}
				command.setParameter(com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle.END_POINT, point);
			}
			node.setCanBind(false);
			for (Iterator it = node.getMap().getSiteNodes().iterator();it.hasNext();)
			{
				SiteNode sit = (SiteNode)it.next();
				SiteNodeController snc = (SiteNodeController)logicalNetLayer.getMapViewController().getController(sit);
				if (!(sit instanceof UnboundNode))
					if (snc.isMouseOnElement(sit, point))
					{
						node.setCanBind(true);
						break;
					}
			}
		}//MapState.MOVE_ACTION_MODE
		else if (actionMode == MapState.NULL_ACTION_MODE)
		{
			if (!node.isActive())
			{
				mapState.setActionMode(MapState.DRAW_LINES_ACTION_MODE);
			}
		}//MapState.NULL_ACTION_MODE
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMouseReleased(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();
		int operationMode = mapState.getOperationMode();

		if (operationMode == MapState.MOVE_FIXDIST)
		{
			logicalNetLayer.getCommandList().add(command);
			logicalNetLayer.getCommandList().execute();
			command = null;
		}//MapState.MOVE_FIXDIST
		else if (actionMode == MapState.MOVE_ACTION_MODE)
		{
			logicalNetLayer.getCommandList().add(command);
			logicalNetLayer.getCommandList().execute();
			command = null;
			PhysicalLink link = node.getPhysicalLink();
			if (link instanceof UnboundLink)
			{
				if (node.isCanBind())
				{
					for (Iterator it = node.getMap().getSiteNodes().iterator();it.hasNext();)
					{
						SiteNode site = (SiteNode)it.next();
						SiteNodeController snc = (SiteNodeController)logicalNetLayer.getMapViewController().getController(site);
						if (!(site instanceof UnboundNode))
							if (snc.isMouseOnElement(site, point))
							{
								command = new BindPhysicalNodeToSiteCommandBundle(node, site);
								((BindPhysicalNodeToSiteCommandBundle)command).setLogicalNetLayer(logicalNetLayer);
								logicalNetLayer.getCommandList().add(command);
								logicalNetLayer.getCommandList().execute();
								command = null;
								break;
							}
					}
				}
			}
		}//MapState.MOVE_ACTION_MODE
		else if (actionMode == MapState.DRAW_LINES_ACTION_MODE)
		{
			mapState.setActionMode(MapState.NULL_ACTION_MODE);
			if (command == null)
			{
				command = new CreateNodeLinkCommandBundle(node);
				((CreateNodeLinkCommandBundle)command).setLogicalNetLayer(logicalNetLayer);
			}
			command.setParameter(CreateNodeLinkCommandBundle.END_POINT, point);
			logicalNetLayer.getCommandList().add(command);
			logicalNetLayer.getCommandList().execute();
			command = null;
		}//MapState.DRAW_LINES_ACTION_MODE

		mapState.setActionMode(MapState.NULL_ACTION_MODE);
	}
}
