/**
 * $Id: MapAlarmMarkerStrategy.java,v 1.17 2005/06/22 08:43:48 krupenn Exp $
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
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.AlarmMarker;
import com.syrus.AMFICOM.mapview.Selection;

/**
 * ��������� ���������� �������� ������� �������.
 * @author $Author: krupenn $
 * @version $Revision: 1.17 $, $Date: 2005/06/22 08:43:48 $
 * @module mapviewclient_v1
 */
public final class MapAlarmMarkerStrategy extends AbstractMapStrategy 
{
	/**
	 * ������ ������� �������.
	 */
	AlarmMarker marker;
	
	/**
	 * instance
	 */
	private static MapAlarmMarkerStrategy instance = new MapAlarmMarkerStrategy();

	/**
	 * Private constructor.
	 */
	private MapAlarmMarkerStrategy()
	{//empty
	}

	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapAlarmMarkerStrategy getInstance()
	{
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMapElement(MapElement me)
	{
		this.marker = (AlarmMarker)me;
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
				sel.add(this.marker);
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
		super.logicalNetLayer.getMapView().getMap().setSelected(this.marker, true);
	}
}

