/**
 * $Id: MapUnboundNodeElementStrategy.java,v 1.10 2005/01/31 12:19:19 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Command.Action.BindUnboundNodeToSiteCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.Controllers.SiteNodeController;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.UnboundNode;

import java.awt.Point;
import java.awt.event.MouseEvent;

import java.util.Iterator;

import javax.swing.SwingUtilities;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;

/**
 * ��������� ���������� �����
 * 
 * 
 * 
 * @version $Revision: 1.10 $, $Date: 2005/01/31 12:19:19 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapUnboundNodeElementStrategy implements  MapStrategy 
{
	LogicalNetLayer logicalNetLayer;
	ApplicationContext aContext;

	UnboundNode unbound;
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
		this.unbound = (UnboundNode)me;
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
					if(mel instanceof Selection)
					{
						Selection sel = (Selection)mel;
						sel.add(unbound);
					}
					else
					{
						Selection sel = new Selection(logicalNetLayer.getMapView().getMap());
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
					//���� ��������� �� ���������� ������
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
				
				for(Iterator it = logicalNetLayer.getMapView().getMap().getSiteNodes().iterator(); it.hasNext();)
				{
					SiteNode sit = (SiteNode)it.next();
					SiteNodeController snc = (SiteNodeController)logicalNetLayer.getMapViewController().getController(sit);
					if(!(sit instanceof UnboundNode))
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
						for(Iterator it = logicalNetLayer.getMapView().getMap().getSiteNodes().iterator(); it.hasNext();)
						{
							SiteNode site = (SiteNode)it.next();
							SiteNodeController snc = (SiteNodeController)logicalNetLayer.getMapViewController().getController(site);
							if(!(site instanceof UnboundNode))
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

