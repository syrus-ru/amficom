/**
 * $Id: MapPhysicalNodeElementStrategy.java,v 1.16 2005/02/07 16:09:27 krupenn Exp $
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
 * @version $Revision: 1.16 $, $Date: 2005/02/07 16:09:27 $
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
	{//empty
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
		super.logicalNetLayer = logicalNetLayer;
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
			MapElement mel = super.logicalNetLayer.getCurrentMapElement();
			if (mel instanceof Selection)
			{
				Selection sel = (Selection)mel;
				sel.add(this.node);
			}
			else
			{
				Selection sel = new Selection(this.node.getMap());
				sel.addAll(super.logicalNetLayer.getSelectedElements());
				super.logicalNetLayer.setCurrentMapElement(sel);
			}
		}//MapState.SELECT_ACTION_MODE
		if ((actionMode != MapState.SELECT_ACTION_MODE) && (actionMode != MapState.MOVE_ACTION_MODE))
		{
			super.logicalNetLayer.deselectAll();
		}// ! MapState.SELECT_ACTION_MODE && ! MapState.MOVE_ACTION_MODE
		this.node.setSelected(true);
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
			if (this.command == null)
			{
				this.command = new MoveFixedDistanceCommand(point, super.logicalNetLayer.getFixedNode(), this.node);
				((MoveSelectionCommandBundle)this.command).setLogicalNetLayer(super.logicalNetLayer);
			}
			this.command.setParameter(MoveSelectionCommandBundle.END_POINT, point);
		}//MapState.MOVE_FIXDIST
		else if (actionMode == MapState.MOVE_ACTION_MODE)
		{
			if (super.aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			{
				if (this.command == null)
				{
					this.command = new MoveSelectionCommandBundle(super.logicalNetLayer.getStartPoint());
					((MoveSelectionCommandBundle)this.command).setLogicalNetLayer(super.logicalNetLayer);
				}
				this.command.setParameter(com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle.END_POINT, point);
			}
			this.node.setCanBind(false);
			for (Iterator it = this.node.getMap().getSiteNodes().iterator();it.hasNext();)
			{
				SiteNode sit = (SiteNode)it.next();
				SiteNodeController snc = (SiteNodeController)super.logicalNetLayer.getMapViewController().getController(sit);
				if (!(sit instanceof UnboundNode))
					if (snc.isMouseOnElement(sit, point))
					{
						this.node.setCanBind(true);
						break;
					}
			}
		}//MapState.MOVE_ACTION_MODE
		else if (actionMode == MapState.NULL_ACTION_MODE)
		{
			if (!this.node.isActive())
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
			super.logicalNetLayer.getCommandList().add(this.command);
			super.logicalNetLayer.getCommandList().execute();
			this.command = null;
		}//MapState.MOVE_FIXDIST
		else if (actionMode == MapState.MOVE_ACTION_MODE)
		{
			super.logicalNetLayer.getCommandList().add(this.command);
			super.logicalNetLayer.getCommandList().execute();
			this.command = null;
			PhysicalLink link = this.node.getPhysicalLink();
			if (link instanceof UnboundLink)
			{
				if (this.node.isCanBind())
				{
					for (Iterator it = this.node.getMap().getSiteNodes().iterator();it.hasNext();)
					{
						SiteNode site = (SiteNode)it.next();
						SiteNodeController snc = (SiteNodeController)super.logicalNetLayer.getMapViewController().getController(site);
						if (!(site instanceof UnboundNode))
							if (snc.isMouseOnElement(site, point))
							{
								this.command = new BindPhysicalNodeToSiteCommandBundle(this.node, site);
								((BindPhysicalNodeToSiteCommandBundle)this.command).setLogicalNetLayer(super.logicalNetLayer);
								super.logicalNetLayer.getCommandList().add(this.command);
								super.logicalNetLayer.getCommandList().execute();
								this.command = null;
								break;
							}
					}
				}
			}
		}//MapState.MOVE_ACTION_MODE
		else if (actionMode == MapState.DRAW_LINES_ACTION_MODE)
		{
			mapState.setActionMode(MapState.NULL_ACTION_MODE);
			if (this.command == null)
			{
				this.command = new CreateNodeLinkCommandBundle(this.node);
				((CreateNodeLinkCommandBundle)this.command).setLogicalNetLayer(super.logicalNetLayer);
			}
			this.command.setParameter(CreateNodeLinkCommandBundle.END_POINT, point);
			super.logicalNetLayer.getCommandList().add(this.command);
			super.logicalNetLayer.getCommandList().execute();
			this.command = null;
		}//MapState.DRAW_LINES_ACTION_MODE

		mapState.setActionMode(MapState.NULL_ACTION_MODE);
	}
}
