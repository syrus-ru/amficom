/**
 * $Id: MapUnboundNodeElementStrategy.java,v 1.14 2005/02/02 08:57:28 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Command.Action.BindUnboundNodeToSiteCommandBundle;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.Client.Map.Controllers.SiteNodeController;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.UnboundNode;

import java.awt.Point;

import java.util.Iterator;

/**
 * Стратегия управления непривязанным узлом.
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/02/02 08:57:28 $
 * @module mapviewclient_v1
 */
public final class MapUnboundNodeElementStrategy extends MapStrategy 
{
	/**
	 * Непривязанный узел.
	 */
	UnboundNode unbound;
	/**
	 * Команда, выполняемая в соответствии со стратегией действий на узлом.
	 */
	Command command;

	/**
	 * Instance.
	 */
	private static MapUnboundNodeElementStrategy instance = new MapUnboundNodeElementStrategy();

	/**
	 * Private constructor.
	 */
	private MapUnboundNodeElementStrategy()
	{
	}

	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapUnboundNodeElementStrategy getInstance()
	{
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMapElement(MapElement me)
	{
		this.unbound = (UnboundNode)me;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMousePressed(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();

		if ((actionMode == MapState.SELECT_ACTION_MODE))
		{
			MapElement mel = logicalNetLayer.getCurrentMapElement();
			if (mel instanceof Selection)
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
		}//MapState.SELECT_ACTION_MODE
		if ((actionMode != MapState.SELECT_ACTION_MODE) && (actionMode != MapState.MOVE_ACTION_MODE))
		{
			logicalNetLayer.deselectAll();
		}// ! MapState.SELECT_ACTION_MODE && ! MapState.MOVE_ACTION_MODE
		unbound.setSelected(true);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMouseDragged(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();

		if (actionMode == MapState.MOVE_ACTION_MODE)
		{
			if (aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_BINDING))
			{
				if (command == null)
				{
					command = new MoveSelectionCommandBundle(point);
					((MoveSelectionCommandBundle)command).setLogicalNetLayer(logicalNetLayer);
				}
				command.setParameter(MoveSelectionCommandBundle.END_POINT, point);
			}
		}//MapState.MOVE_ACTION_MODE

		unbound.setCanBind(false);
		for (Iterator it = logicalNetLayer.getMapView().getMap().getSiteNodes().iterator();it.hasNext();)
		{
			SiteNode sit = (SiteNode)it.next();
			SiteNodeController snc = (SiteNodeController)logicalNetLayer.getMapViewController().getController(sit);
			if (!(sit instanceof UnboundNode)
				&& snc.isMouseOnElement(sit, point))
			{
				unbound.setCanBind(true);
				break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMouseReleased(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();

		if (actionMode == MapState.MOVE_ACTION_MODE)
		{
			if (command != null)
			{
				logicalNetLayer.getCommandList().add(command);
				logicalNetLayer.getCommandList().execute();
				command = null;
			}
			if (unbound.getCanBind())
			{
				for (Iterator it = logicalNetLayer.getMapView().getMap().getSiteNodes().iterator();it.hasNext();)
				{
					SiteNode site = (SiteNode)it.next();
					SiteNodeController snc = (SiteNodeController)logicalNetLayer.getMapViewController().getController(site);
					if (!(site instanceof UnboundNode)
						&& snc.isMouseOnElement(site, point))
					{
						command = new BindUnboundNodeToSiteCommandBundle(unbound, site);
						((BindUnboundNodeToSiteCommandBundle)command).setLogicalNetLayer(logicalNetLayer);
						logicalNetLayer.getCommandList().add(command);
						logicalNetLayer.getCommandList().execute();
						command = null;
						break;
					}
				}
			}
			command = null;
		}//MapState.MOVE_ACTION_MODE
		mapState.setActionMode(MapState.NULL_ACTION_MODE);
	}
}

