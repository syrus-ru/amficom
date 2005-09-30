/*-
 * $$Id: MapUnboundNodeElementStrategy.java,v 1.32 2005/09/30 16:08:41 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.strategy;

import java.awt.Point;
import java.util.Iterator;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.command.action.BindUnboundNodeToSiteCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.MapActionCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.client.map.controllers.SiteNodeController;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.UnboundNode;

/**
 * —тратеги€ управлени€ неприв€занным узлом.
 * 
 * @version $Revision: 1.32 $, $Date: 2005/09/30 16:08:41 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapUnboundNodeElementStrategy extends AbstractMapStrategy 
{
	/**
	 * Ќеприв€занный узел.
	 */
	UnboundNode unbound;
	/**
	 *  оманда, выполн€ема€ в соответствии со стратегией действий на узлом.
	 */
	MapActionCommandBundle command;

	/**
	 * Instance.
	 */
	private static MapUnboundNodeElementStrategy instance = new MapUnboundNodeElementStrategy();

	/**
	 * Private constructor.
	 */
	private MapUnboundNodeElementStrategy()
	{//empty
	}

	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapUnboundNodeElementStrategy getInstance()
	{
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMapElement(MapElement me)
	{
		this.unbound = (UnboundNode)me;
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
				sel.add(this.unbound);
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
		super.logicalNetLayer.getMapView().getMap().setSelected(this.unbound, true);
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

		if (actionMode == MapState.MOVE_ACTION_MODE)
		{
			if (super.aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_BINDING))
			{
				if (this.command == null)
				{
					this.command = new MoveSelectionCommandBundle(point);
					((MoveSelectionCommandBundle)this.command).setNetMapViewer(super.netMapViewer);
				}
				this.command.setParameter(MoveSelectionCommandBundle.END_POINT, point);
			}
		}//MapState.MOVE_ACTION_MODE

		this.unbound.setCanBind(false);
		for (Iterator it = super.logicalNetLayer.getMapView().getMap().getAllSiteNodes().iterator();it.hasNext();)
		{
			SiteNode sit = (SiteNode)it.next();
			SiteNodeController snc = (SiteNodeController)super.logicalNetLayer.getMapViewController().getController(sit);
			if (!(sit instanceof UnboundNode)
				&& snc.isMouseOnElement(sit, point))
			{
				this.unbound.setCanBind(true);
				break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void leftMouseReleased(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();

		if (actionMode == MapState.MOVE_ACTION_MODE)
		{
			if (this.command != null)
			{
				super.logicalNetLayer.getCommandList().add(this.command);
				super.logicalNetLayer.getCommandList().execute();
				this.command = null;
			}
			if (this.unbound.getCanBind())
			{
				for (Iterator it = super.logicalNetLayer.getMapView().getMap().getAllSiteNodes().iterator();it.hasNext();)
				{
					SiteNode site = (SiteNode)it.next();
					SiteNodeController snc = (SiteNodeController)super.logicalNetLayer.getMapViewController().getController(site);
					if (!(site instanceof UnboundNode)
						&& snc.isMouseOnElement(site, point))
					{
						this.command = new BindUnboundNodeToSiteCommandBundle(this.unbound, site);
						((BindUnboundNodeToSiteCommandBundle)this.command).setNetMapViewer(super.netMapViewer);
						super.logicalNetLayer.getCommandList().add(this.command);
						super.logicalNetLayer.getCommandList().execute();
						if(!this.command.isUndoable()) {
							super.logicalNetLayer.getCommandList().flush();
						}
						this.command = null;
						break;
					}
				}
			}
			this.command = null;
		}//MapState.MOVE_ACTION_MODE
		mapState.setActionMode(MapState.NULL_ACTION_MODE);
	}
}

