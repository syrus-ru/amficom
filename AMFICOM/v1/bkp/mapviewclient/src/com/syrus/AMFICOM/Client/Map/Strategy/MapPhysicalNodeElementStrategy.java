/**
 * $Id: MapPhysicalNodeElementStrategy.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;

import com.syrus.AMFICOM.Client.Map.Command.Action.CreateNodeLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveFixedDistanceCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * ��������� ���������� �������������� �����
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapPhysicalNodeElementStrategy implements  MapStrategy 
{
	LogicalNetLayer logicalNetLayer;
	ApplicationContext aContext;

	MapPhysicalNodeElement node;
	Command command;

	private static MapPhysicalNodeElementStrategy instance = new MapPhysicalNodeElementStrategy();

	private MapPhysicalNodeElementStrategy()
	{
	}

	public static MapPhysicalNodeElementStrategy getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
		this.node = (MapPhysicalNodeElement )me;
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
		Map map = node.getMap();

		int mouseMode = mapState.getMouseMode();
		int actionMode = mapState.getActionMode();

		Point point = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
			if ((actionMode != MapState.SELECT_ACTION_MODE) &&
				(actionMode != MapState.MOVE_ACTION_MODE) )
			{
				logicalNetLayer.getMapView().deselectAll();
//				map.deselectAll();
			}
			node.setSelected(true);

			if(mouseMode == MapState.MOUSE_DRAGGED)
			{
				if (actionMode == MapState.MOVE_ACTION_MODE)
				{
					//���� ��������� �� ���������� ������
					if ( aContext.getApplicationModel().isEnabled("mapActionMoveEquipment"))
					{
						if(command == null)
						{
							command = new MoveSelectionCommandBundle(point);
							((MoveSelectionCommandBundle)command).setLogicalNetLayer(logicalNetLayer);
						}
						command.setParameter(com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle.END_POINT, point);
					}
				}//if (actionMode == MapState.MOVE_ACTION_MODE)
				else
				if(actionMode == MapState.FIXDIST_ACTION_MODE)
				{
					logicalNetLayer.getMapView().deselectAll();
//					map.deselectAll();
					node.setSelected(true);

					if(command == null)
					{
						command = new MoveFixedDistanceCommand(point);
						((MoveSelectionCommandBundle)command).setLogicalNetLayer(logicalNetLayer);
					}
					command.setParameter(com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle.END_POINT, point);
				}//if(actionMode == MapState.FIXDIST_ACTION_MODE)
				else
				if (actionMode == MapState.NULL_ACTION_MODE)
					if(!node.isActive())
				{
					//��� ������������ ��� ��������� ����� (NodeLink)
					mapState.setActionMode(MapState.DRAW_LINES_ACTION_MODE) ;
				}
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
				else
				if (actionMode == MapState.FIXDIST_ACTION_MODE)
				{
					logicalNetLayer.getCommandList().add(command);
					logicalNetLayer.getCommandList().execute();
					command = null;
				}//if (actionMode == MapState.FIXDIST_ACTION_MODE)
				else
				if (actionMode == MapState.DRAW_LINES_ACTION_MODE)
				{
					mapState.setActionMode(MapState.NULL_ACTION_MODE);
					if(command == null)
					{
						command = new CreateNodeLinkCommandBundle(node);
						((CreateNodeLinkCommandBundle )command).setLogicalNetLayer(logicalNetLayer);
					}
					command.setParameter(CreateNodeLinkCommandBundle.END_POINT, point);
					logicalNetLayer.getCommandList().add(command);
					logicalNetLayer.getCommandList().execute();
					command = null;
				}//if (actionMode == MapState.DRAW_LINES_ACTION_MODE)
				mapState.setActionMode(MapState.NULL_ACTION_MODE);
			}//if(mouseMode == MapState.MOUSE_RELEASED)
		}//if(SwingUtilities.isLeftMouseButton(me))
	}
}
