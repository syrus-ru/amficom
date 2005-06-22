/**
 * $Id: MapSiteNodeElementStrategy.java,v 1.20 2005/06/22 08:43:49 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.strategy;

import java.awt.Point;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.command.action.CreateNodeLinkCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.MoveFixedDistanceCommand;
import com.syrus.AMFICOM.client.map.command.action.MoveSelectionCommandBundle;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.Selection;

/**
 * Стратегия управления узлом.
 * @author $Author: krupenn $
 * @version $Revision: 1.20 $, $Date: 2005/06/22 08:43:49 $
 * @module mapviewclient_v1
 */
public final class MapSiteNodeElementStrategy extends AbstractMapStrategy 
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
	{// empty
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
	protected void leftMousePressed(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();

		if ((actionMode == MapState.SELECT_ACTION_MODE))
		{
			MapElement mel = super.logicalNetLayer.getCurrentMapElement();
			if (mel instanceof Selection)
			{
				Selection sel = (Selection)mel;
				sel.add(this.site);
			}
			else
			{
				Selection sel = new Selection();
				sel.addAll(super.logicalNetLayer.getSelectedElements());
				super.logicalNetLayer.setCurrentMapElement(sel);
			}
		}//MapState.SELECT_ACTION_MODE
		if ((actionMode != MapState.SELECT_ACTION_MODE) && (actionMode != MapState.MOVE_ACTION_MODE))
		{
			super.logicalNetLayer.deselectAll();
		}// ! MapState.SELECT_ACTION_MODE && ! MapState.MOVE_ACTION_MODE
		super.logicalNetLayer.getMapView().getMap().setSelected(this.site, true);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMouseDragged(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();
		int operationMode = mapState.getOperationMode();

		if (operationMode == MapState.MOVE_FIXDIST)
		{
			if (this.command == null)
			{
				this.command = new MoveFixedDistanceCommand(point, super.logicalNetLayer.getFixedNode(), this.site);
				((MoveSelectionCommandBundle)this.command).setLogicalNetLayer(super.logicalNetLayer);
			}
			this.command.setParameter(MoveSelectionCommandBundle.END_POINT, point);
		}//MapState.MOVE_FIXDIST
		else if (actionMode == MapState.MOVE_ACTION_MODE)
		{
			if (super.aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			{
				if (this.command == null)
				{
					this.command = new MoveSelectionCommandBundle(super.logicalNetLayer.getStartPoint());
					((MoveSelectionCommandBundle)this.command).setLogicalNetLayer(super.logicalNetLayer);
				}
				this.command.setParameter(MoveSelectionCommandBundle.END_POINT, point);
			}
		}//MapState.MOVE_ACTION_MODE
		else if (actionMode == MapState.NULL_ACTION_MODE)
		{
			mapState.setActionMode(MapState.DRAW_LINES_ACTION_MODE);
		}//MapState.NULL_ACTION_MODE
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMouseReleased(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();
		int operationMode = mapState.getOperationMode();

		if (operationMode == MapState.MOVE_FIXDIST)
		{
			super.logicalNetLayer.getCommandList().add(this.command);
			super.logicalNetLayer.getCommandList().execute();
			this.command = null;
		}//MapState.MOVE_FIXDIST
		else if (actionMode == MapState.MOVE_ACTION_MODE)
		{
			super.logicalNetLayer.getCommandList().add(this.command);
			super.logicalNetLayer.getCommandList().execute();
			this.command = null;
		}//MapState.MOVE_ACTION_MODE
		else if (actionMode == MapState.DRAW_LINES_ACTION_MODE)
		{
			mapState.setActionMode(MapState.NULL_ACTION_MODE);
			if (this.command == null)
			{
				this.command = new CreateNodeLinkCommandBundle(this.site);
				((CreateNodeLinkCommandBundle)this.command).setLogicalNetLayer(super.logicalNetLayer);
			}
			this.command.setParameter(CreateNodeLinkCommandBundle.END_POINT, point);
			super.logicalNetLayer.getCommandList().add(this.command);
			super.logicalNetLayer.getCommandList().execute();
			this.command = null;
		}//MapState.DRAW_LINES_ACTION_MODE
		mapState.setActionMode(MapState.NULL_ACTION_MODE);
	}

}

