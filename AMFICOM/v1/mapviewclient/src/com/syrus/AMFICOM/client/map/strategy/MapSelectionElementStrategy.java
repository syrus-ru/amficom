/**
 * $Id: MapSelectionElementStrategy.java,v 1.14 2005/02/07 16:09:27 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.VoidElement;

import java.awt.Point;

/**
 * Стратегия управления выделенными объектами.
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/02/07 16:09:27 $
 * @module mapviewclient_v1
 */
public final class MapSelectionElementStrategy extends MapStrategy 
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
	{
		int actionMode = mapState.getActionMode();

		MapElement mel = super.logicalNetLayer.getMapElementAtPoint(point);
		if ((actionMode == MapState.SELECT_ACTION_MODE))
		{
			if (mel instanceof VoidElement)
			{
				super.logicalNetLayer.deselectAll();
				super.logicalNetLayer.setCurrentMapElement(mel);
			}//mel instanceof VoidElement
			else
			{
				if (mel.isSelected())
				{
					mel.setSelected(false);
					this.selection.remove(mel);
					if (this.selection.getElements().size() == 0)
					{
						super.logicalNetLayer.setCurrentMapElement(com.syrus.AMFICOM.mapview.VoidElement.getInstance(super.logicalNetLayer.getMapView()));
					}
					else if (this.selection.getElements().size() == 1)
					{
						super.logicalNetLayer.setCurrentMapElement((MapElement)this.selection.getElements().get(0));
					}
				}// mel.isSelected()
				else
				{
					mel.setSelected(true);
					this.selection.add(mel);
				}// ! mel.isSelected()
			}// ! mel instanceof VoidElement
		}//MapState.SELECT_ACTION_MODE
		else
		{
			if (mel instanceof VoidElement)
			{
				super.logicalNetLayer.deselectAll();
				super.logicalNetLayer.setCurrentMapElement(mel);
			}
			else
			{
				if (!this.selection.getElements().contains(mel))
				{
					super.logicalNetLayer.deselectAll();
					super.logicalNetLayer.setCurrentMapElement(mel);
					mel.setSelected(true);
				}
			}
		}// ! MapState.SELECT_ACTION_MODE
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMouseDragged(MapState mapState, Point point)
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
