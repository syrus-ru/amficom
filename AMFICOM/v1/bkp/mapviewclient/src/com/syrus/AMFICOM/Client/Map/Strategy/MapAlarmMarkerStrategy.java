/**
 * $Id: MapAlarmMarkerStrategy.java,v 1.10 2005/02/02 08:57:27 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.AlarmMarker;
import com.syrus.AMFICOM.mapview.Selection;

import java.awt.Point;

/**
 * ��������� ���������� �������� ������� �������.
 * @author $Author: krupenn $
 * @version $Revision: 1.10 $, $Date: 2005/02/02 08:57:27 $
 * @module mapviewclient_v1
 */
public final class MapAlarmMarkerStrategy extends MapStrategy 
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
	{
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
	{
		int actionMode = mapState.getActionMode();

		if ((actionMode == MapState.SELECT_ACTION_MODE))
		{
			MapElement mel = logicalNetLayer.getCurrentMapElement();
			if (mel instanceof Selection)
			{
				Selection sel = (Selection)mel;
				sel.add(marker);
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
		marker.setSelected(true);
	}
}

