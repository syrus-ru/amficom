/**
 * $Id: MapNodeLinkElementStrategy.java,v 1.5 2004/12/22 16:38:41 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.Action.CreatePhysicalNodeCommandBundle;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления фрагментом линии
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/22 16:38:41 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapNodeLinkElementStrategy implements  MapStrategy 
{
	LogicalNetLayer logicalNetLayer;
	ApplicationContext aContext;
	
	NodeLink nodeLink;

	Command command;

	private static MapNodeLinkElementStrategy instance = new MapNodeLinkElementStrategy();

	private MapNodeLinkElementStrategy()
	{
	}

	public static MapNodeLinkElementStrategy getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
		this.nodeLink = (NodeLink)me;
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

		if(SwingUtilities.isLeftMouseButton(me))
		{
			if(mouseMode == MapState.MOUSE_PRESSED)
			{
				nodeLink.setSelected(true);
				if ((actionMode == MapState.SELECT_ACTION_MODE))
				{
					MapElement mel = logicalNetLayer.getCurrentMapElement();
					if(mel instanceof MapSelection)
					{
						MapSelection sel = (MapSelection )mel;
						sel.add(nodeLink);
					}
					else
					{
						MapSelection sel = new MapSelection(logicalNetLayer);
						sel.addAll(logicalNetLayer.getSelectedElements());
						logicalNetLayer.setCurrentMapElement(sel);
					}
				}
				else
				if (actionMode == MapState.ALT_LINK_ACTION_MODE)
				{
					command = new CreatePhysicalNodeCommandBundle(nodeLink, me.getPoint());
					((CreatePhysicalNodeCommandBundle )command).setLogicalNetLayer(logicalNetLayer);
					logicalNetLayer.getCommandList().add(command);
					logicalNetLayer.getCommandList().execute();
				}
				else
				if (actionMode != MapState.MOVE_ACTION_MODE)
				{
					logicalNetLayer.deselectAll();
					nodeLink.setSelected(true);
				}
			}
		}

	}
}

