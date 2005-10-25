/*-
 * $$Id: MapSelectionElementStrategy.java,v 1.32 2005/10/25 08:02:45 krupenn Exp $$
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
import com.syrus.AMFICOM.client.map.command.action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.VoidElement;

/**
 * Стратегия управления выделенными объектами.
 * 
 * @version $Revision: 1.32 $, $Date: 2005/10/25 08:02:45 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapSelectionElementStrategy extends AbstractMapStrategy 
{
	/**
	 * Набор выделенных элементов.
	 */
	Selection selection;
	/**
	 * Команда, выполняемая в соответствии со стратегией действий над 
	 * выделенными объектами.
	 */
	Command command;

	/**
	 * instance.
	 */
	private static MapSelectionElementStrategy instance = new MapSelectionElementStrategy();

	/**
	 * Private constructor.
	 */
	private MapSelectionElementStrategy()
	{//empty
	}

	/**
	 * get instance.
	 * @return instance
	 */
	public static MapSelectionElementStrategy getInstance()
	{
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMapElement(MapElement me)
	{
		this.selection = (Selection)me;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void leftMousePressed(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();
		Map map = super.logicalNetLayer.getMapView().getMap(); 

		MapElement mapElement = super.logicalNetLayer.getVisibleMapElementAtPoint(point, super.netMapViewer.getVisibleBounds());
		if ((actionMode == MapState.SELECT_ACTION_MODE))
		{
			if (!(mapElement instanceof VoidElement))
			{
				if (mapElement.isSelected())
				{
					map.setSelected(mapElement, false);
					this.selection.remove(mapElement);
					
					if (this.selection.getElements().size() == 0) {
						super.logicalNetLayer.setCurrentMapElement(VoidElement.getInstance(super.logicalNetLayer.getMapView()));
					}
					else if (this.selection.getElements().size() == 1) {
						super.logicalNetLayer.setCurrentMapElement(this.selection.getElements().iterator().next());
					}
				}// mel.isSelected()
				else
				{
					map.setSelected(mapElement, true);
					this.selection.add(mapElement);
				}// ! mel.isSelected()
//				this.netMapViewer.getLogicalNetLayer().sendSelectionChangeEvent();
			}// ! mel instanceof VoidElement
		}//MapState.SELECT_ACTION_MODE
		else
		{
			if (mapElement instanceof VoidElement)
			{
				super.logicalNetLayer.deselectAll();
				super.logicalNetLayer.setCurrentMapElement(mapElement);
			}
			else
			{
				if (!this.selection.getElements().contains(mapElement))
				{
					super.logicalNetLayer.deselectAll();
					super.logicalNetLayer.setCurrentMapElement(mapElement);
					map.setSelected(mapElement, true);
				}
			}
//			this.netMapViewer.getLogicalNetLayer().sendSelectionChangeEvent();
		}// ! MapState.SELECT_ACTION_MODE
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
			if (super.aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			{
				if (this.command == null)
				{
					this.command = new MoveSelectionCommandBundle(super.logicalNetLayer.getStartPoint());
					((MoveSelectionCommandBundle)this.command).setNetMapViewer(super.netMapViewer);
				}
				this.command.setParameter(MoveSelectionCommandBundle.END_POINT, point);
			}
		}//MapState.MOVE_ACTION_MODE
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
			super.logicalNetLayer.getCommandList().add(this.command);
			super.logicalNetLayer.getCommandList().execute();
			this.command = null;
		}//MapState.MOVE_ACTION_MODE
		mapState.setActionMode(MapState.NULL_ACTION_MODE);
	}

}
