/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.UI.FixedSizeEditableTableModel;

public class MapElementPropertiesTableModel extends FixedSizeEditableTableModel
{
	public static final String KEY_PROPERTY = "Property";
	public static final String KEY_VALUE = "Value";

	public static final String PROPERTY_ID = "Id";
	public static final String PROPERTY_NAME = "Name";
	public static final String PROPERTY_DESCRIPTION = "Description";
	public static final String PROPERTY_USER_ID = "User_id";
	public static final String PROPERTY_CREATED = "Created";
	public static final String PROPERTY_CREATED_BY = "Created_by";
	public static final String PROPERTY_MODIFIED = "Modified";
	public static final String PROPERTY_MODIFIED_BY = "Modified_by";
	public static final String PROPERTY_SITE_ID = "Site_id";	
	public static final String PROPERTY_COLLECTOR_ID = "Collector_id";	
	public static final String PROPERTY_PHYSICAL_LINK_ID = "Physical_link_id";	
	public static final String PROPERTY_START_NODE_ID = "Start_node_id";	
	public static final String PROPERTY_END_NODE_ID = "End_node_id";	
	public static final String PROPERTY_PROTO_ID = "Proto_id";	
	public static final String PROPERTY_LONGITUDE = "Longitude";
	public static final String PROPERTY_LATITUDE = "Latitude";
	public static final String PROPERTY_CITY = "City";	
	public static final String PROPERTY_STREET = "Street";	
	public static final String PROPERTY_BUILDING = "Building";	
	public static final String PROPERTY_ACTIVE = "Active";
	public static final String PROPERTY_COLOR = "Color";
	public static final String PROPERTY_STYLE = "Style";
	public static final String PROPERTY_THICKNESS = "Thickness";
	public static final String PROPERTY_TOPOLOGICAL_LENGTH = "TopologicalLength";
	public static final String PROPERTY_OPTICAL_LENGTH = "OpticalLength";
	public static final String PROPERTY_PHYSICAL_LENGTH = "PhysicalLength";
	public static final String PROPERTY_DISTANCE = "Distance";

	private static String[] keysArray;
	
	private static int[] editableColumns;
	
	static
	{
		// empty private constructor
		keysArray = new String[] { 
				LangModelMap.getString(KEY_PROPERTY), 
				LangModelMap.getString(KEY_VALUE)};
	
		editableColumns = new int[] { 1 };
	}

	public MapElementPropertiesTableModel(
			Object[] defaultValues,
			String[] rowTitles)
	{
		super(keysArray, defaultValues, rowTitles, editableColumns);
	}

	private static final MapElementPropertiesTableModel EMPTY = 
		new MapElementPropertiesTableModel(new Object[0], new String[0]);

	public static MapElementPropertiesTableModel getEmpltyTableModel()
	{
		return EMPTY;
	}

}
