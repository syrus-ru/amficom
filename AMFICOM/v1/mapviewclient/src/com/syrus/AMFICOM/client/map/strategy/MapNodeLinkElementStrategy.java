/**
 * $Id: MapNodeLinkElementStrategy.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления фрагментом линии
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapNodeLinkElementStrategy implements  MapStrategy 
{
	LogicalNetLayer logicalNetLayer;
	ApplicationContext aContext;
	
	MapNodeLinkElement nodeLink;

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
		this.nodeLink = (MapNodeLinkElement )me;
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
		Map map = nodeLink.getMap();

		int mouseMode = mapState.getMouseMode();
		int actionMode = mapState.getActionMode();

		if(SwingUtilities.isLeftMouseButton(me))
		{
			if ((actionMode != MapState.SELECT_ACTION_MODE) &&
				(actionMode != MapState.MOVE_ACTION_MODE) )
			{
				logicalNetLayer.getMapView().deselectAll();
//				map.deselectAll();
			}
			nodeLink.setSelected(true);

			if (actionMode == MapState.ALT_LINK_ACTION_MODE &&
				mouseMode == MapState.MOUSE_PRESSED)
			{
				command = new CreatePhysicalNodeCommandBundle(nodeLink, me.getPoint());
				((CreatePhysicalNodeCommandBundle )command).setLogicalNetLayer(logicalNetLayer);
				logicalNetLayer.getCommandList().add(command);
				logicalNetLayer.getCommandList().execute();
			}
		}

	}
}

