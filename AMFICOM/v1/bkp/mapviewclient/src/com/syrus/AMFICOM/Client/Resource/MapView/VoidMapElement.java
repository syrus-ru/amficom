/**
 * $Id: VoidMapElement.java,v 1.8 2004/12/08 16:20:22 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Пустой элемент 
 * 
 * 
 * 
 * @version $Revision: 1.8 $, $Date: 2004/12/08 16:20:22 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class VoidMapElement extends StubResource implements MapElement 
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

	public String getId()
	{
		throw new UnsupportedOperationException();
	}

	public void setId(String id)
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
