/**
 * $Id: MapNodeLinkElementStrategy.java,v 1.8 2005/02/01 16:16:13 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Command.Action.CreatePhysicalNodeCommandBundle;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.Selection;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления фрагментом линии.
 * @author $Author: krupenn $
 * @version $Revision: 1.8 $, $Date: 2005/02/01 16:16:13 $
 * @module mapviewclient_v1
 */
public final class MapNodeLinkElementStrategy extends MapStrategy 
{
	/**
	 * Фрагмент лниии.
	 */
	NodeLink nodeLink;

	/**
	 * Команда вставки нового топологического узла.
	 */
	CreatePhysicalNodeCommandBundle command;

	/**
	 * Instance.
	 */
	private static MapNodeLinkElementStrategy instance = new MapNodeLinkElementStrategy();

	/**
	 * Private constructor.
	 */
	private MapNodeLinkElementStrategy()
	{
	}

	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapNodeLinkElementStrategy getInstance()
	{
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMapElement(MapElement me)
	{
		this.nodeLink = (NodeLink)me;
	}

	/**
	 * {@inheritDoc}
	 */
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
					if(mel instanceof Selection)
					{
						Selection sel = (Selection)mel;
						sel.add(nodeLink);
					}
					else
					{
						Selection sel = new Selection(logicalNetLayer.getMapView().getMap());
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

