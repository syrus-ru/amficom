/**
 * $Id: MapSelectionElementStrategy.java,v 1.19 2005/04/13 11:27:33 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.VoidElement;

import java.awt.Point;

/**
 * Стратегия управления выделенными объектами.
 * @author $Author: krupenn $
 * @version $Revision: 1.19 $, $Date: 2005/04/13 11:27:33 $
 * @module mapviewclient_v1
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
	public void setMapElement(MapElement me)
	{
		this.selection = (Selection)me;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMousePressed(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();
		Map map = super.logicalNetLayer.getMapView().getMap(); 

		MapElement mapElement = super.logicalNetLayer.getMapElementAtPoint(point);
		if ((actionMode == MapState.SELECT_ACTION_MODE))
		{
			if (!(mapElement instanceof VoidElement))
			{
				if (mapElement.isSelected())
				{
					map.setSelected(mapElement, false);
					this.selection.remove(mapElement);
					
					super.logicalNetLayer.sendMapEvent(new MapNavigateEvent(mapElement, MapNavigateEvent.MAP_ELEMENT_DESELECTED_EVENT));
					
					if (this.selection.getElements().size() == 0)
					{
						super.logicalNetLayer.setCurrentMapElement(com.syrus.AMFICOM.mapview.VoidElement.getInstance(super.logicalNetLayer.getMapView()));
					}
					else if (this.selection.getElements().size() == 1)
					{
						super.logicalNetLayer.setCurrentMapElement((MapElement)this.selection.getElements().iterator().next());
					}
				}// mel.isSelected()
				else
				{
					map.setSelected(mapElement, true);
					this.selection.add(mapElement);
					
					super.logicalNetLayer.sendMapEvent(new MapNavigateEvent(mapElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
				}// ! mel.isSelected()
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
		}// ! MapState.SELECT_ACTION_MODE
	}

	/**
	 * {@inheritDoc}
	 */
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
					((MoveSelectionCommandBundle)this.command).setLogicalNetLayer(super.logicalNetLayer);
				}
				this.command.setParameter(MoveSelectionCommandBundle.END_POINT, point);
			}
		}//MapState.MOVE_ACTION_MODE
	}

	/**
	 * {@inheritDoc}
	 */
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
