/**
 * $Id: MapSelectionElementStrategy.java,v 1.13 2005/02/02 08:57:28 krupenn Exp $
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
 * @version $Revision: 1.13 $, $Date: 2005/02/02 08:57:28 $
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
	{
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

		MapElement mel = logicalNetLayer.getMapElementAtPoint(point);
		if ((actionMode == MapState.SELECT_ACTION_MODE))
		{
			if (mel instanceof VoidElement)
			{
				logicalNetLayer.deselectAll();
				logicalNetLayer.setCurrentMapElement(mel);
			}//mel instanceof VoidElement
			else
			{
				if (mel.isSelected())
				{
					mel.setSelected(false);
					selection.remove(mel);
					if (selection.getElements().size() == 0)
					{
						logicalNetLayer.setCurrentMapElement(com.syrus.AMFICOM.mapview.VoidElement.getInstance(logicalNetLayer.getMapView()));
					}
					else if (selection.getElements().size() == 1)
					{
						logicalNetLayer.setCurrentMapElement((MapElement)selection.getElements().get(0));
					}
				}// mel.isSelected()
				else
				{
					mel.setSelected(true);
					selection.add(mel);
				}// ! mel.isSelected()
			}// ! mel instanceof VoidElement
		}//MapState.SELECT_ACTION_MODE
		else
		{
			if (mel instanceof VoidElement)
			{
				logicalNetLayer.deselectAll();
				logicalNetLayer.setCurrentMapElement(mel);
			}
			else
			{
				if (!selection.getElements().contains(mel))
				{
					logicalNetLayer.deselectAll();
					logicalNetLayer.setCurrentMapElement(mel);
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
			if (aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			{
				if (command == null)
				{
					command = new MoveSelectionCommandBundle(logicalNetLayer.getStartPoint());
					((MoveSelectionCommandBundle)command).setLogicalNetLayer(logicalNetLayer);
				}
				command.setParameter(MoveSelectionCommandBundle.END_POINT, point);
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
			logicalNetLayer.getCommandList().add(command);
			logicalNetLayer.getCommandList().execute();
			command = null;
		}//MapState.MOVE_ACTION_MODE
		mapState.setActionMode(MapState.NULL_ACTION_MODE);
	}

}
