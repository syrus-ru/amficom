package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.geom.Point2D;

public final class MapPhysicalNodePropertiesTableModel
		extends MapElementPropertiesTableModel
{

	MapPhysicalNodeElement node;
	
	private static String[] rowTitles;

	private static Object[] defaultValues;

	static
	{
		rowTitles = new String[] {
			LangModel.getString(PROPERTY_NAME),
			LangModelMap.getString(PROPERTY_LATITUDE),
			LangModelMap.getString(PROPERTY_LONGITUDE) };

		defaultValues = new Object[3];

		defaultValues[0] = "";
		defaultValues[1] = new Double(0.0D);
		defaultValues[2] = new Double(0.0D);
	}

	public MapPhysicalNodePropertiesTableModel(MapPhysicalNodeElement node)
	{
		super(defaultValues, rowTitles);
		setObjectResource(node);
	}

	public void setValueAt(Object value, int row, int col)
	{
		if(col == 1)
		{
			switch(row)
			{
				case 0:
					node.setName((String )value);;
					break;
				case 1:
				{
					try
					{
						Point2D.Double pt = node.getAnchor();
						pt.x = Double.parseDouble((String )value);
						node.setAnchor(pt);
					}
					catch(NumberFormatException e)
					{
						return;
					}
					break;
				}
				case 2:
				{
					try
					{
						Point2D.Double pt = node.getAnchor();
						pt.y = Double.parseDouble((String )value);
						node.setAnchor(pt);
					}
					catch(NumberFormatException e)
					{
						return;
					}
					break;
				}
				default:
					break;
			}
		}
		super.setValueAt(value, row, col);
	}

	public void setObjectResource(ObjectResource or)
	{
		this.node = (MapPhysicalNodeElement )or;
		super.updateRow(node.getName(), 0);
		super.updateRow(new Double(node.getAnchor().x), 1);
		super.updateRow(new Double(node.getAnchor().y), 2);
	}

	public ObjectResource getObjectResource()
	{
		return node;
	}

	public boolean isCellEditable(int row, int col)
	{
		if(col == 0)
			return false;
		return true;
	}
}
