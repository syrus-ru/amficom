/*-
 * $$Id: MapNodeLinkElementStrategy.java,v 1.27 2005/09/30 16:08:41 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.strategy;

import java.awt.Point;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalNodeCommandBundle;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.AMFICOM.mapview.Selection;

/**
 * Стратегия управления фрагментом линии.
 * 
 * @version $Revision: 1.27 $, $Date: 2005/09/30 16:08:41 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapNodeLinkElementStrategy extends AbstractMapStrategy 
{
	/**
	 * Фрагмент лниии.
	 */
	NodeLink nodeLink;

	/**
	 * Команда вставки нового топологического узла.
	 */
	CreatePhysicalNodeCommandBundle command;

	/**
	 * Instance.
	 */
	private static MapNodeLinkElementStrategy instance = new MapNodeLinkElementStrategy();

	/**
	 * Private constructor.
	 */
	private MapNodeLinkElementStrategy()
	{//empty
	}

	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapNodeLinkElementStrategy getInstance()
	{
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMapElement(MapElement me)
	{
		this.nodeLink = (NodeLink)me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void leftMousePressed(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();

		super.logicalNetLayer.getMapView().getMap().setSelected(this.nodeLink, true);
		if ((actionMode == MapState.SELECT_ACTION_MODE))
		{
			MapElement mel = super.logicalNetLayer.getCurrentMapElement();
			if (mel instanceof Selection)
			{
				Selection sel = (Selection)mel;
				sel.add(this.nodeLink);
			}
			else
			{
				Selection sel = new Selection();
				sel.addAll(super.logicalNetLayer.getSelectedElements());
				super.logicalNetLayer.setCurrentMapElement(sel);
			}
		}//MapState.SELECT_ACTION_MODE
		else if (actionMode == MapState.ALT_LINK_ACTION_MODE) {
			// prohibit creation of topological nodes for indoor cabling 
			if(this.nodeLink.getPhysicalLink().getType().getSort().value() != PhysicalLinkTypeSort._INDOOR) {
				this.command = new CreatePhysicalNodeCommandBundle(this.nodeLink, point);
				this.command.setNetMapViewer(super.netMapViewer);
				super.logicalNetLayer.getCommandList().add(this.command);
				super.logicalNetLayer.getCommandList().execute();
				if(!this.command.isUndoable()) {
					super.logicalNetLayer.getCommandList().flush();
				}
			}
		}//MapState.ALT_LINK_ACTION_MODE
		else if (actionMode != MapState.MOVE_ACTION_MODE)
		{
			super.logicalNetLayer.deselectAll();
			super.logicalNetLayer.getMapView().getMap().setSelected(this.nodeLink, true);
		}//MapState.MOVE_ACTION_MODE
	}
}

