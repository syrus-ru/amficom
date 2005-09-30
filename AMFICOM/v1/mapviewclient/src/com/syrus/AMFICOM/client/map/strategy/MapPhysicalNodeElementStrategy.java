/*-
 * $$Id: MapPhysicalNodeElementStrategy.java,v 1.33 2005/09/30 16:08:41 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.strategy;

import java.awt.Point;
import java.util.Iterator;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.command.action.BindPhysicalNodeToSiteCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.CreateNodeLinkCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.MapActionCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.MoveFixedDistanceCommand;
import com.syrus.AMFICOM.client.map.command.action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.client.map.controllers.SiteNodeController;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;

/**
 * —тратеги€ управлени€ топологическим узлом.
 * 
 * @version $Revision: 1.33 $, $Date: 2005/09/30 16:08:41 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapPhysicalNodeElementStrategy extends AbstractMapStrategy 
{
	/**
	 * “опологический узел.
	 */
	TopologicalNode node;
	/**
	 *  оманда, выполн€ема€ в соответствии со стратегией действий на узлом.
	 */
	MapActionCommandBundle command;

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
	
	@Override
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
	@Override
	protected void leftMousePressed(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
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
				Selection sel = new Selection();
				sel.addAll(super.logicalNetLayer.getSelectedElements());
				super.logicalNetLayer.setCurrentMapElement(sel);
			}
		}//MapState.SELECT_ACTION_MODE
		if ((actionMode != MapState.SELECT_ACTION_MODE) && (actionMode != MapState.MOVE_ACTION_MODE))
		{
			super.logicalNetLayer.deselectAll();
		}// ! MapState.SELECT_ACTION_MODE && ! MapState.MOVE_ACTION_MODE
		super.logicalNetLayer.getMapView().getMap().setSelected(this.node, true);
//		this.netMapViewer.getLogicalNetLayer().sendSelectionChangeEvent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void leftMouseDragged(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();
		int operationMode = mapState.getOperationMode();

		if (operationMode == MapState.MOVE_FIXDIST)
		{
			if (this.command == null)
			{
				this.command = new MoveFixedDistanceCommand(point, super.logicalNetLayer.getFixedNode(), this.node);
				((MoveSelectionCommandBundle)this.command).setNetMapViewer(super.netMapViewer);
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
					((MoveSelectionCommandBundle)this.command).setNetMapViewer(super.netMapViewer);
				}
				this.command.setParameter(MoveSelectionCommandBundle.END_POINT, point);
			}
			this.node.setCanBind(false);
			for (Iterator it = super.logicalNetLayer.getMapView().getMap().getAllSiteNodes().iterator();it.hasNext();)
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
	@Override
	protected void leftMouseReleased(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
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
					for (Iterator it = super.logicalNetLayer.getMapView().getMap().getAllSiteNodes().iterator();it.hasNext();)
					{
						SiteNode site = (SiteNode)it.next();
						SiteNodeController snc = (SiteNodeController)super.logicalNetLayer.getMapViewController().getController(site);
						if (!(site instanceof UnboundNode))
							if (snc.isMouseOnElement(site, point))
							{
								this.command = new BindPhysicalNodeToSiteCommandBundle(this.node, site);
								((BindPhysicalNodeToSiteCommandBundle)this.command).setNetMapViewer(super.netMapViewer);
								super.logicalNetLayer.getCommandList().add(this.command);
								super.logicalNetLayer.getCommandList().execute();
								if(!this.command.isUndoable()) {
									super.logicalNetLayer.getCommandList().flush();
								}
								this.command = null;
								this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
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
				((CreateNodeLinkCommandBundle)this.command).setNetMapViewer(super.netMapViewer);
			}
			this.command.setParameter(CreateNodeLinkCommandBundle.END_POINT, point);
			super.logicalNetLayer.getCommandList().add(this.command);
			super.logicalNetLayer.getCommandList().execute();
			this.command = null;
		}//MapState.DRAW_LINES_ACTION_MODE

		mapState.setActionMode(MapState.NULL_ACTION_MODE);
	}
}
