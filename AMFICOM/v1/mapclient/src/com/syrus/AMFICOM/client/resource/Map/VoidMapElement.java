/**
 * $Id: VoidMapElement.java,v 1.8 2004/09/23 10:05:30 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * ������ ������� 
 * 
 * 
 * 
 * @version $Revision: 1.8 $, $Date: 2004/09/23 10:05:30 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class VoidMapElement extends StubResource implements MapElement 
{
	protected Map map;
	
	protected static VoidMapElement singleton = new VoidMapElement();

	protected VoidMapElement()
	{
	}

	public String[][] getExportColumns()
	{
		return null;
	}

	public void setColumn(String field, String value)
	{
	}

	public static VoidMapElement getInstance(Map map)
	{
		singleton.map = map;
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

	public void paint(Graphics g)
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
		return map;
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
		Map mc = getMap();
		String s1 = mc.getName();

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
