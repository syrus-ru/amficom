package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.geom.Point2D;

public final class MapNodeLinkPropertiesTableModel
		extends MapElementPropertiesTableModel
{

	MapNodeLinkElement nodelink;
	
	private static String[] rowTitles;

	private static Object[] defaultValues;

	static
	{
		rowTitles = new String[] {
			LangModel.getString(PROPERTY_NAME),
			LangModelMap.getString(PROPERTY_TOPOLOGICAL_LENGTH),
			LangModelMap.getString(PROPERTY_START_NODE_ID),
			LangModelMap.getString(PROPERTY_END_NODE_ID) };

		defaultValues = new Object[4];

		defaultValues[0] = "";
		defaultValues[1] = new Double(0.0D);
		defaultValues[2] = "";
		defaultValues[3] = "";
	}

	public MapNodeLinkPropertiesTableModel(MapNodeLinkElement nodelink)
	{
		super(defaultValues, rowTitles);
		setObjectResource(nodelink);
	}

	public void setValueAt(Object value, int row, int col)
	{
		if(col == 1)
		{
			switch(row)
			{
				case 0:
					nodelink.setName((String )value);;
					break;
				case 1:
				case 2:
				case 3:
					break;
				default:
					break;
			}
		}
		super.setValueAt(value, row, col);
	}

	public void setObjectResource(ObjectResource or)
	{
		this.nodelink = (MapNodeLinkElement )or;
		super.updateRow(nodelink.getName(), 0);
		super.updateRow(new Double(nodelink.getLengthLt()), 1);
		super.updateRow(nodelink.getStartNode(), 2);
		super.updateRow(nodelink.getEndNode(), 3);
	}

	public ObjectResource getObjectResource()
	{
		return nodelink;
	}

	public boolean isCellEditable(int row, int col)
	{
		if(col == 0)
			return false;
		return true;
	}
}
