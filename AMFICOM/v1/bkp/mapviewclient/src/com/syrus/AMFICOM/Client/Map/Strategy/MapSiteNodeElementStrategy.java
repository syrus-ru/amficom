/**
 * $Id: MapSiteNodeElementStrategy.java,v 1.10 2005/02/01 17:18:16 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateNodeLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveFixedDistanceCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.Selection;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления узлом.
 * @author $Author: krupenn $
 * @version $Revision: 1.10 $, $Date: 2005/02/01 17:18:16 $
 * @module mapviewclient_v1
 */
public final class MapSiteNodeElementStrategy extends MapStrategy 
{
	/**
	 * Сетевой узел.
	 */
	SiteNode site;
	/**
	 * Команда, выполняемая в соответствии со стратегией действий на узлом.
	 */
	Command command;

	/**
	 * Instance.
	 */
	private static MapSiteNodeElementStrategy instance = new MapSiteNodeElementStrategy();

	/**
	 * Private constructor.
	 */
	private MapSiteNodeElementStrategy()
	{
	}

	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapSiteNodeElementStrategy getInstance()
	{
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMapElement(MapElement me)
	{
		this.site = (SiteNode)me;
	}

	/**
	 * {@inheritDoc}
	 */
	public void doContextChanges(MouseEvent me)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "doContextChanges()");
		
		MapState mapState = logicalNetLayer.getMapState();

		int mouseMode = mapState.getMouseMode();

		Point point = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
			if(mouseMode == MapState.MOUSE_PRESSED)
			{
				leftMousePressed(mapState, point);
			}//MapState.MOUSE_PRESSED
			else
			if(mouseMode == MapState.MOUSE_DRAGGED)
			{
				leftMouseDragged(mapState, point);
			}//MapState.MOUSE_DRAGGED
			else
			if(mouseMode == MapState.MOUSE_RELEASED)
			{
				leftMouseReleased(mapState, point);
			}//MapState.MOUSE_RELEASED
		}//SwingUtilities.isLeftMouseButton(me)
	}

	/**
	 * Process left mouse released.
	 * @param mapState map state
	 * @param point new point
	 */
	void leftMouseReleased(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();
		int operationMode = mapState.getOperationMode();

		if (operationMode == MapState.MOVE_FIXDIST)
		{
			logicalNetLayer.getCommandList().add(command);
			logicalNetLayer.getCommandList().execute();
			command = null;
		}//MapState.MOVE_FIXDIST
		else if (actionMode == MapState.MOVE_ACTION_MODE)
		{
			logicalNetLayer.getCommandList().add(command);
			logicalNetLayer.getCommandList().execute();
			command = null;
		}//MapState.MOVE_ACTION_MODE
		else if (actionMode == MapState.DRAW_LINES_ACTION_MODE)
		{
			mapState.setActionMode(MapState.NULL_ACTION_MODE);
			if (command == null)
			{
				command = new CreateNodeLinkCommandBundle(site);
				((CreateNodeLinkCommandBundle)command).setLogicalNetLayer(logicalNetLayer);
			}
			command.setParameter(CreateNodeLinkCommandBundle.END_POINT, point);
			logicalNetLayer.getCommandList().add(command);
			logicalNetLayer.getCommandList().execute();
			command = null;
		}//MapState.DRAW_LINES_ACTION_MODE
		mapState.setActionMode(MapState.NULL_ACTION_MODE);
	}

	/**
	 * Process left mouse dragged.
	 * @param mapState map state
	 * @param point new point
	 */
	void leftMouseDragged(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();
		int operationMode = mapState.getOperationMode();

		if (operationMode == MapState.MOVE_FIXDIST)
		{
			if (command == null)
			{
				command = new MoveFixedDistanceCommand(point, logicalNetLayer.getFixedNode(), site);
				((MoveSelectionCommandBundle)command).setLogicalNetLayer(logicalNetLayer);
			}
			command.setParameter(MoveSelectionCommandBundle.END_POINT, point);
		}//MapState.MOVE_FIXDIST
		else if (actionMode == MapState.MOVE_ACTION_MODE)
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
		else if (actionMode == MapState.NULL_ACTION_MODE)
		{
			mapState.setActionMode(MapState.DRAW_LINES_ACTION_MODE);
		}//MapState.NULL_ACTION_MODE
	}

	/**
	 * Process left mouse pressed.
	 * @param mapState map state
	 * @param point new point
	 */
	void leftMousePressed(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();

		if ((actionMode == MapState.SELECT_ACTION_MODE))
		{
			MapElement mel = logicalNetLayer.getCurrentMapElement();
			if (mel instanceof Selection)
			{
				Selection sel = (Selection)mel;
				sel.add(site);
			}
			else
			{
				Selection sel = new Selection(logicalNetLayer.getMapView().getMap());
				sel.addAll(logicalNetLayer.getSelectedElements());
				logicalNetLayer.setCurrentMapElement(sel);
			}
		}//MapState.SELECT_ACTION_MODE
		if ((actionMode != MapState.SELECT_ACTION_MODE) && (actionMode != MapState.MOVE_ACTION_MODE))
		{
			logicalNetLayer.deselectAll();
		}// ! MapState.SELECT_ACTION_MODE && ! MapState.MOVE_ACTION_MODE
		site.setSelected(true);
	}
}

