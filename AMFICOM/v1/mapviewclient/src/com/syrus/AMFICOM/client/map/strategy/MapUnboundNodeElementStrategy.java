/**
 * $Id: MapUnboundNodeElementStrategy.java,v 1.7 2004/12/07 17:05:54 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.Action.BindUnboundNodeToSiteCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.SiteNodeController;
import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;

import java.awt.Point;
import java.awt.event.MouseEvent;

import java.util.Iterator;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления узлом
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2004/12/07 17:05:54 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapUnboundNodeElementStrategy implements  MapStrategy 
{
	LogicalNetLayer logicalNetLayer;
	ApplicationContext aContext;

	MapUnboundNodeElement unbound;
	Command command;

	private static MapUnboundNodeElementStrategy instance = new MapUnboundNodeElementStrategy();

	private MapUnboundNodeElementStrategy()
	{
	}

	public static MapUnboundNodeElementStrategy getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
		this.unbound = (MapUnboundNodeElement )me;
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
			if(mouseMode == MapState.MOUSE_PRESSED)
			{
				if ((actionMode == MapState.SELECT_ACTION_MODE))
				{
					MapElement mel = logicalNetLayer.getCurrentMapElement();
					if(mel instanceof MapSelection)
					{
						MapSelection sel = (MapSelection )mel;
						sel.add(unbound);
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
				unbound.setSelected(true);
			}
			else
			if(mouseMode == MapState.MOUSE_DRAGGED)
			{
				if (actionMode == MapState.MOVE_ACTION_MODE)
				{
					//Если разрешено то перемещаем объект
					if ( aContext.getApplicationModel().isEnabled(
						MapApplicationModel.ACTION_EDIT_BINDING))
					{
						if(command == null)
						{
//							List selection = logicalNetLayer.getSelectedElements();
//							if(selection.size() == 1)
//								command = new 
							command = new MoveSelectionCommandBundle(point);
							((MoveSelectionCommandBundle )command).setLogicalNetLayer(logicalNetLayer);
						}
						command.setParameter(MoveSelectionCommandBundle.END_POINT, point);
					}
				}//if (actionMode == MapState.MOVE_ACTION_MODE)

				unbound.setCanBind(false);
				
				for(Iterator it = logicalNetLayer.getMapView().getMap().getMapSiteNodeElements().iterator(); it.hasNext();)
				{
					MapSiteNodeElement sit = (MapSiteNodeElement )it.next();
					SiteNodeController snc = (SiteNodeController )logicalNetLayer.getMapViewController().getController(sit);
					if(!(sit instanceof MapUnboundNodeElement))
						if(snc.isMouseOnElement(sit, point))
						{
							unbound.setCanBind(true);
							break;
						}
				}
				
//				MapElement mapElement = logicalNetLayer.getMapElementAtPoint(point);
//				unbound.setCanBind(mapElement instanceof MapSiteNodeElement);
				
			}//if(mouseMode == MapState.MOUSE_DRAGGED)
			else
			if(mouseMode == MapState.MOUSE_RELEASED)
			{
				if (actionMode == MapState.MOVE_ACTION_MODE)
				{
					if(command != null)
					{
						// complete move
						logicalNetLayer.getCommandList().add(command);
						logicalNetLayer.getCommandList().execute();
						command = null;
					}
					
					if(unbound.getCanBind())
					{
						for(Iterator it = logicalNetLayer.getMapView().getMap().getMapSiteNodeElements().iterator(); it.hasNext();)
						{
							MapSiteNodeElement site = (MapSiteNodeElement )it.next();
							SiteNodeController snc = (SiteNodeController )logicalNetLayer.getMapViewController().getController(site);
							if(!(site instanceof MapUnboundNodeElement))
								if(snc.isMouseOnElement(site, point))
								{
									command = new BindUnboundNodeToSiteCommandBundle(unbound, site);
									((BindUnboundNodeToSiteCommandBundle)command).setLogicalNetLayer(logicalNetLayer);
									logicalNetLayer.getCommandList().add(command);
									logicalNetLayer.getCommandList().execute();
									command = null;
									break;
								}
						}
//						MapSiteNodeElement site = (MapSiteNodeElement )logicalNetLayer.getMapElementAtPoint(point);
//						command = new BindToSiteCommandBundle(unbound, site);
//						((BindToSiteCommandBundle )command).setLogicalNetLayer(logicalNetLayer);
					}

					command = null;
				}//if (actionMode == MapState.MOVE_ACTION_MODE)
				mapState.setActionMode(MapState.NULL_ACTION_MODE);
			}//if(mouseMode == MapState.MOUSE_RELEASED)
		}//SwingUtilities.isLeftMouseButton(me)
	}
}

