/**
 * $Id: VoidMapElement.java,v 1.1 2004/09/30 13:38:11 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;

/**
 * Пустой элемент 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/30 13:38:11 $
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

	public Object clone(DataSourceInterface dataSource)
	{
		throw new UnsupportedOperationException();
	}
	
	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	public MapElementState getState()
	{
		throw new UnsupportedOperationException();
	}

	public boolean isVisible(Rectangle2D.Double visibleBounds)
	{
		throw new UnsupportedOperationException();
	}

	public void paint(Graphics g, Rectangle2D.Double visibleBounds)
	{
		throw new UnsupportedOperationException();
	}

	public boolean isSelected()
	{
		throw new UnsupportedOperationException();
	}

	public void setSelected(boolean selected)
	{
	}

	public boolean isMouseOnThisObject(Point currentMousePoint)
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

	public Point2D.Double getAnchor()
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

	public ObjectResourceModel getModel()
	{
		return null;//new MapModel(getMap());
	}

	public boolean isMovable()
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
