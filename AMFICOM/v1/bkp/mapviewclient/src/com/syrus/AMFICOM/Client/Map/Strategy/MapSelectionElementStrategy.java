/**
 * $Id: MapSelectionElementStrategy.java,v 1.3 2004/10/20 12:38:40 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.MapView.*;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления топологическим узлом
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/20 12:38:40 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapSelectionElementStrategy implements  MapStrategy 
{
	LogicalNetLayer logicalNetLayer;
	ApplicationContext aContext;

	MapSelection sel;
	Command command;

	private static MapSelectionElementStrategy instance = new MapSelectionElementStrategy();

	private MapSelectionElementStrategy()
	{
	}

	public static MapSelectionElementStrategy getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
		this.sel = (MapSelection )me;
	}

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.aContext = logicalNetLayer.getContext();
	}

	public void doContextChanges(MouseEvent me)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "doContextChanges()");
		
		MapState mapState = logicalNetLayer.getMapState();

		int mouseMode = mapState.getMouseMode();
		int actionMode = mapState.getActionMode();

		Point point = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
			MapElement mel = logicalNetLayer.getMapElementAtPoint(point);

			if(mouseMode == MapState.MOUSE_PRESSED)
			{
				if ((actionMode == MapState.SELECT_ACTION_MODE))
				{
					if(mel instanceof VoidMapElement)
					{
						logicalNetLayer.deselectAll();
						logicalNetLayer.setCurrentMapElement(mel);
					}
					else
					{
						if(mel.isSelected())
						{
							mel.setSelected(false);
							sel.remove(mel);
							if(sel.getElements().size() == 0)
							{
								logicalNetLayer.setCurrentMapElement(
										VoidMapElement.getInstance(
												logicalNetLayer.getMapView()));
							}
							else
							if(sel.getElements().size() == 1)
							{
								logicalNetLayer.setCurrentMapElement(
									(MapElement )sel.getElements().get(0));
							}
						}//if(mel.isSelected()
						else
						{
							mel.setSelected(true);
							sel.add(mel);
						}
					}
				}
				else
				{
					if(mel instanceof VoidMapElement)
					{
						logicalNetLayer.deselectAll();
						logicalNetLayer.setCurrentMapElement(mel);
					}
					else
					{
						if(!sel.getElements().contains(mel))
						{
							logicalNetLayer.deselectAll();
							logicalNetLayer.setCurrentMapElement(mel);
							mel.setSelected(true);
						}
					}
				}
			}
			else
			if(mouseMode == MapState.MOUSE_DRAGGED)
			{
				if (actionMode == MapState.MOVE_ACTION_MODE)
				{
					//Если разрешено то перемещаем объект
					if ( aContext.getApplicationModel().isEnabled(
						MapApplicationModel.ACTION_EDIT_MAP))
					{
						if(command == null)
						{
							command = new MoveSelectionCommandBundle(point);
							((MoveSelectionCommandBundle)command).setLogicalNetLayer(logicalNetLayer);
						}
						command.setParameter(com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle.END_POINT, point);
					}
				}//if (actionMode == MapState.MOVE_ACTION_MODE)
			}//if(mouseMode == MapState.MOUSE_DRAGGED)
			else
			if(mouseMode == MapState.MOUSE_RELEASED)
			{
				if (actionMode == MapState.MOVE_ACTION_MODE)
				{
					logicalNetLayer.getCommandList().add(command);
					logicalNetLayer.getCommandList().execute();
					command = null;
				}//if (actionMode == MapState.MOVE_ACTION_MODE)
//				mapState.setActionMode(MapState.NULL_ACTION_MODE);
			}//if(mouseMode == MapState.MOUSE_RELEASED)
		}//if(SwingUtilities.isLeftMouseButton(me))
	}
}
