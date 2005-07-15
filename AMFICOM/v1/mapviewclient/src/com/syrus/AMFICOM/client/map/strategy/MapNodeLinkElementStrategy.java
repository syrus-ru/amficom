/**
 * $Id: MapNodeLinkElementStrategy.java,v 1.20 2005/07/15 17:06:08 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.strategy;

import java.awt.Point;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalNodeCommandBundle;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.Selection;

/**
 * ��������� ���������� ���������� �����.
 * @author $Author: krupenn $
 * @version $Revision: 1.20 $, $Date: 2005/07/15 17:06:08 $
 * @module mapviewclient_v1
 */
public final class MapNodeLinkElementStrategy extends AbstractMapStrategy 
{
	/**
	 * �������� �����.
	 */
	NodeLink nodeLink;

	/**
	 * ������� ������� ������ ��������������� ����.
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
	{//empty
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
	protected void leftMousePressed(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();

		super.logicalNetLayer.getMapView().getMap().setSelected(this.nodeLink, true);
		if ((actionMode == MapState.SELECT_ACTION_MODE))
		{
			MapElement mel = super.logicalNetLayer.getCurrentMapElement();
			if (mel instanceof Selection)
			{
				Selection sel = (Selection)mel;
				sel.add(this.nodeLink);
			}
			else
			{
				Selection sel = new Selection();
				sel.addAll(super.logicalNetLayer.getSelectedElements());
				super.logicalNetLayer.setCurrentMapElement(sel);
			}
		}//MapState.SELECT_ACTION_MODE
		else if (actionMode == MapState.ALT_LINK_ACTION_MODE)
		{
			this.command = new CreatePhysicalNodeCommandBundle(this.nodeLink, point);
			this.command.setNetMapViewer(super.netMapViewer);
			super.logicalNetLayer.getCommandList().add(this.command);
			super.logicalNetLayer.getCommandList().execute();
		}//MapState.ALT_LINK_ACTION_MODE
		else if (actionMode != MapState.MOVE_ACTION_MODE)
		{
			super.logicalNetLayer.deselectAll();
			super.logicalNetLayer.getMapView().getMap().setSelected(this.nodeLink, true);
		}//MapState.MOVE_ACTION_MODE
		this.netMapViewer.getLogicalNetLayer().sendSelectionChangeEvent();
	}
}

