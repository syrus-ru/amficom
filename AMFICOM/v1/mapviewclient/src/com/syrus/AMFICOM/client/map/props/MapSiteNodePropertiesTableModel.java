package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.Resource.Pool;
import java.awt.geom.Point2D;

public final class MapSiteNodePropertiesTableModel
		extends MapElementPropertiesTableModel
{

	MapSiteNodeElement site;
	
	private static String[] rowTitles;

	private static Object[] defaultValues;

	static
	{
		rowTitles = new String[] {
			LangModel.getString(PROPERTY_NAME),
			LangModelMap.getString(PROPERTY_PROTO_ID),
			LangModelMap.getString(PROPERTY_LATITUDE),
			LangModelMap.getString(PROPERTY_LONGITUDE) };

		defaultValues = new Object[4];

		defaultValues[0] = "";
		defaultValues[1] = "";
		defaultValues[2] = new Double(0.0D);
		defaultValues[3] = new Double(0.0D);
	}

	public MapSiteNodePropertiesTableModel(MapSiteNodeElement site)
	{
		super(defaultValues, rowTitles);
		setObjectResource(site);
	}

	public void setObjectResource(ObjectResource or)
	{
		this.site = (MapSiteNodeElement )or;
		super.updateRow(site.getName(), 0);
		super.updateRow(Pool.get(MapNodeProtoElement.typ, site.getMapProtoId()), 1);
		super.updateRow(new Double(site.getAnchor().x), 2);
		super.updateRow(new Double(site.getAnchor().y), 3);
	}

	public void setValueAt(Object value, int row, int col)
	{
		if(col == 1)
		{
			switch(row)
			{
				case 0:
					site.setName((String )value);;
					break;
				case 1:
					site.setMapProtoId(((MapElement )value).getId());
					break;
				case 2:
				{
					try
					{
						Point2D.Double pt = site.getAnchor();
						pt.x = Double.parseDouble((String )value);
						site.setAnchor(pt);
					}
					catch(NumberFormatException e)
					{
						return;
					}
					break;
				}
				case 3:
				{
					try
					{
						Point2D.Double pt = site.getAnchor();
						pt.y = Double.parseDouble((String )value);
						site.setAnchor(pt);
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

	public ObjectResource getObjectResource()
	{
		return site;
	}

	public boolean isCellEditable(int row, int col)
	{
		if(col == 0)
			return false;
		return true;
	}
}
