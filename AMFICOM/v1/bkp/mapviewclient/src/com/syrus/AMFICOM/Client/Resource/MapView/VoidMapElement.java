/**
 * $Id: VoidMapElement.java,v 1.9 2004/12/22 16:38:43 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;

import java.util.List;

/**
 * ������ ������� 
 * 
 * 
 * 
 * @version $Revision: 1.9 $, $Date: 2004/12/22 16:38:43 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class VoidMapElement implements MapElement 
{
	protected MapView mapView;
	
	protected static VoidMapElement singleton = new VoidMapElement();

	protected VoidMapElement()
	{
	}

	public String[][] getExportColumns()
	{
		throw new UnsupportedOperationException();
	}

	public void setColumn(String field, String value)
	{
		throw new UnsupportedOperationException();
	}

	public static VoidMapElement getInstance(MapView mapView)
	{
		singleton.mapView = mapView;
		return singleton;
	}

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	public MapElementState getState()
	{
		return new VoidMapState();
	}

	public boolean isSelected()
	{
		return true;
	}

	public void setSelected(boolean selected)
	{
		throw new UnsupportedOperationException();
	}

	public void setAlarmState(boolean alarmState)
	{
		throw new UnsupportedOperationException();
	}

	public boolean getAlarmState()
	{
		throw new UnsupportedOperationException();
	}

	public Identifier getId()
	{
		throw new UnsupportedOperationException();
	}

	public void setId(Identifier id)
	{
		throw new UnsupportedOperationException();
	}

	public String getName()
	{
		throw new UnsupportedOperationException();
	}

	public void setName(String name)
	{
		throw new UnsupportedOperationException();
	}

	public DoublePoint getLocation()
	{
		throw new UnsupportedOperationException();
	}

	public void setMap(Map mc)
	{
		throw new UnsupportedOperationException();
	}
	
	public Map getMap()
	{
		return mapView.getMap();
	}

	public MapView getMapView()
	{
		return mapView;
	}

	public List getCharacteristics() 
	{
		throw new UnsupportedOperationException();
	}

	public void addCharacteristic(Characteristic ch)
	{
		throw new UnsupportedOperationException();
	}

	public void removeCharacteristic(Characteristic ch)
	{
		throw new UnsupportedOperationException();
	}

	public String getToolTipText()
	{
		String s1 = getMapView().getName();

		return s1;
	}

	public void revert(MapElementState state)
	{
		throw new UnsupportedOperationException();
	}

	public boolean isRemoved()
	{
		throw new UnsupportedOperationException();
	}
	
	public void setRemoved(boolean removed)
	{
		throw new UnsupportedOperationException();
	}

}
