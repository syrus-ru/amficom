/**
 * $Id: MapNodeLinkElementStrategy.java,v 1.14 2005/03/02 12:35:40 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.Map.Command.Action.CreatePhysicalNodeCommandBundle;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.Selection;

import java.awt.Point;

/**
 * ��������� ���������� ���������� �����.
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/03/02 12:35:40 $
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

		this.nodeLink.setSelected(true);
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
				Selection sel = new Selection(super.logicalNetLayer.getMapView().getMap());
				sel.addAll(super.logicalNetLayer.getSelectedElements());
				super.logicalNetLayer.setCurrentMapElement(sel);
			}
		}//MapState.SELECT_ACTION_MODE
		else if (actionMode == MapState.ALT_LINK_ACTION_MODE)
		{
			this.command = new CreatePhysicalNodeCommandBundle(this.nodeLink, point);
			this.command.setLogicalNetLayer(super.logicalNetLayer);
			super.logicalNetLayer.getCommandList().add(this.command);
			super.logicalNetLayer.getCommandList().execute();
		}//MapState.ALT_LINK_ACTION_MODE
		else if (actionMode != MapState.MOVE_ACTION_MODE)
		{
			super.logicalNetLayer.deselectAll();
			this.nodeLink.setSelected(true);
		}//MapState.MOVE_ACTION_MODE
	}
}

