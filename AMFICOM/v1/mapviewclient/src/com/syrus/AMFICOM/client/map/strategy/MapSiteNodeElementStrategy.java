/**
 * $Id: MapSiteNodeElementStrategy.java,v 1.7 2004/12/24 15:42:13 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateNodeLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveFixedDistanceCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.mapview.Selection;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления узлом
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2004/12/24 15:42:13 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapSiteNodeElementStrategy implements  MapStrategy 
{
	LogicalNetLayer logicalNetLayer;
	ApplicationContext aContext;

	SiteNode site;
	Command command;

	private static MapSiteNodeElementStrategy instance = new MapSiteNodeElementStrategy();

	private MapSiteNodeElementStrategy()
	{
	}

	public static MapSiteNodeElementStrategy getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
		this.site = (SiteNode)me;
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
		int operationMode = mapState.getOperationMode();

		Point point = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
			if(mouseMode == MapState.MOUSE_PRESSED)
			{
				if ((actionMode == MapState.SELECT_ACTION_MODE))
				{
					MapElement mel = logicalNetLayer.getCurrentMapElement();
					if(mel instanceof Selection)
					{
						Selection sel = (Selection)mel;
						sel.add(site);
					}
					else
					{
						Selection sel = new Selection(logicalNetLayer);
						sel.addAll(logicalNetLayer.getSelectedElements());
						logicalNetLayer.setCurrentMapElement(sel);
					}
				}
				if ((actionMode != MapState.SELECT_ACTION_MODE) &&
					(actionMode != MapState.MOVE_ACTION_MODE) )
				{
					logicalNetLayer.deselectAll();
				}
				site.setSelected(true);
			}
			else
			if(mouseMode == MapState.MOUSE_DRAGGED)
			{
				if(operationMode == MapState.MOVE_FIXDIST)
				{
					if(command == null)
					{
						command = new MoveFixedDistanceCommand(point, logicalNetLayer.getFixedNode(), site);
						((MoveSelectionCommandBundle )command).setLogicalNetLayer(logicalNetLayer);
					}
					command.setParameter(MoveSelectionCommandBundle.END_POINT, point);
				}//if(operationMode == MapState.MOVE_FIXDIST)
				else
				if (actionMode == MapState.MOVE_ACTION_MODE)
				{
					//Если разрешено то перемещаем объект
					if ( aContext.getApplicationModel().isEnabled(
						MapApplicationModel.ACTION_EDIT_MAP))
					{
						if(command == null)
						{
							command = new MoveSelectionCommandBundle(logicalNetLayer.getStartPoint());
							((MoveSelectionCommandBundle)command).setLogicalNetLayer(logicalNetLayer);
						}
						command.setParameter(MoveSelectionCommandBundle.END_POINT, point);
					}
				}//if (actionMode == MapState.MOVE_ACTION_MODE)
				else
				if (actionMode == MapState.NULL_ACTION_MODE)
				{
					//Это используется для рисования линии (NodeLink)
					mapState.setActionMode(MapState.DRAW_LINES_ACTION_MODE);
				}
			}//if(mouseMode == MapState.MOUSE_DRAGGED)
			else
			if(mouseMode == MapState.MOUSE_RELEASED)
			{
				if (operationMode == MapState.MOVE_FIXDIST)
				{
					logicalNetLayer.getCommandList().add(command);
					logicalNetLayer.getCommandList().execute();
					command = null;
				}//if (operationMode == MapState.MOVE_FIXDIST)
				else
				if (actionMode == MapState.MOVE_ACTION_MODE)
				{
					logicalNetLayer.getCommandList().add(command);
					logicalNetLayer.getCommandList().execute();
					command = null;
				}//if (actionMode == MapState.MOVE_ACTION_MODE)
				else
				if (actionMode == MapState.DRAW_LINES_ACTION_MODE)
				{
					mapState.setActionMode(MapState.NULL_ACTION_MODE);
					if(command == null)
					{
						command = new CreateNodeLinkCommandBundle(site);
						((CreateNodeLinkCommandBundle )command).setLogicalNetLayer(logicalNetLayer);
					}
					command.setParameter(CreateNodeLinkCommandBundle.END_POINT, point);
					logicalNetLayer.getCommandList().add(command);
					logicalNetLayer.getCommandList().execute();
					command = null;
				}//if (actionMode == MapState.DRAW_LINES_ACTION_MODE)
				mapState.setActionMode(MapState.NULL_ACTION_MODE);
			}//if(mouseMode == MapState.MOUSE_RELEASED)
		}//SwingUtilities.isLeftMouseButton(me)
	}
}

