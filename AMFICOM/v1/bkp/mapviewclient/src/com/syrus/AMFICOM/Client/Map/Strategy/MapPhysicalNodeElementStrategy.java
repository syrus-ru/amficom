/**
 * $Id: MapPhysicalNodeElementStrategy.java,v 1.9 2004/12/22 16:38:41 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.Action.BindPhysicalNodeToSiteCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreateNodeLinkCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveFixedDistanceCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Resource.Map.SiteNodeController;
import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;

import java.awt.Point;
import java.awt.event.MouseEvent;

import java.util.Iterator;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления топологическим узлом
 * 
 * 
 * 
 * @version $Revision: 1.9 $, $Date: 2004/12/22 16:38:41 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapPhysicalNodeElementStrategy implements  MapStrategy 
{
	LogicalNetLayer logicalNetLayer;
	ApplicationContext aContext;

	TopologicalNode node;
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
		this.node = (TopologicalNode)me;
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
		int operationMode = mapState.getOperationMode();

		Point point = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
			if(mouseMode == MapState.MOUSE_PRESSED)
			{
				if ((actionMode == MapState.SELECT_ACTION_MODE))
				{
					MapElement mel = logicalNetLayer.getCurrentMapElement();
					if(mel instanceof MapSelection)
					{
						MapSelection sel = (MapSelection )mel;
						sel.add(node);
					}
					else
					{
						MapSelection sel = new MapSelection(logicalNetLayer);
						sel.addAll(logicalNetLayer.getSelectedElements());
						logicalNetLayer.setCurrentMapElement(sel);
					}
				}
				if ((actionMode != MapState.SELECT_ACTION_MODE) &&
					(actionMode != MapState.MOVE_ACTION_MODE) )
				{
					logicalNetLayer.deselectAll();
				}
				node.setSelected(true);
			}
			else
			if(mouseMode == MapState.MOUSE_DRAGGED)
			{
				if(operationMode == MapState.MOVE_FIXDIST)
				{
					if(command == null)
					{
						command = new MoveFixedDistanceCommand(point, logicalNetLayer.getFixedNode(), node);
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
						command.setParameter(com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle.END_POINT, point);
					}

					node.setCanBind(false);
					

					for(Iterator it = logicalNetLayer.getMapView().getMap().getSiteNodes().iterator(); it.hasNext();)
					{
						SiteNode sit = (SiteNode)it.next();

						SiteNodeController snc = (SiteNodeController )logicalNetLayer.getMapViewController().getController(sit);

						if(!(sit instanceof MapUnboundNodeElement))
							if(snc.isMouseOnElement(sit, point))
							{
								node.setCanBind(true);
								break;
							}
					}

				}//if (actionMode == MapState.MOVE_ACTION_MODE)
				else
				if (actionMode == MapState.NULL_ACTION_MODE)
					if(!node.isActive())
				{
					//Это используется для рисования линии (NodeLink)
					mapState.setActionMode(MapState.DRAW_LINES_ACTION_MODE) ;
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

					PhysicalLink link = node.getPhysicalLink();
					if(link instanceof MapUnboundLinkElement)
					{
						if(node.isCanBind())
						{
							for(Iterator it = logicalNetLayer.getMapView().getMap().getSiteNodes().iterator(); it.hasNext();)
							{
								SiteNode site = (SiteNode)it.next();
								SiteNodeController snc = (SiteNodeController )logicalNetLayer.getMapViewController().getController(site);
								if(!(site instanceof MapUnboundNodeElement))
									if(snc.isMouseOnElement(site, point))
									{
										command = new BindPhysicalNodeToSiteCommandBundle(node, site);
										((BindPhysicalNodeToSiteCommandBundle )command).setLogicalNetLayer(logicalNetLayer);
										logicalNetLayer.getCommandList().add(command);
										logicalNetLayer.getCommandList().execute();
										command = null;
										break;
									}
							}
						}
					}
				}//if (actionMode == MapState.MOVE_ACTION_MODE)
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
