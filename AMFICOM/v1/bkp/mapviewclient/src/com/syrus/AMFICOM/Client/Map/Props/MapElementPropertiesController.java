
package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesController;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import java.util.LinkedList;
import java.util.List;

public class MapElementPropertiesController
	implements ObjectResourcePropertiesController
{
	public static final String PROPERTY_ID = "Id";
	public static final String PROPERTY_NAME = "Name";
	public static final String PROPERTY_DESCRIPTION = "Description";
	public static final String PROPERTY_USER_ID = "User_id";
	public static final String PROPERTY_CREATED = "Created";
	public static final String PROPERTY_CREATED_BY = "Created_by";
	public static final String PROPERTY_MODIFIED = "Modified";
	public static final String PROPERTY_MODIFIED_BY = "Modified_by";
	public static final String PROPERTY_SITE_ID = "Site_id";	
	public static final String PROPERTY_SCHEME_CABLE_ID = "Scheme_cable_id";	
	public static final String PROPERTY_SCHEME_PATH_ID = "Scheme_path_id";	
	public static final String PROPERTY_COLLECTOR_ID = "Collector_id";	
	public static final String PROPERTY_PATH_ID = "Path_id";	
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
	public static final String PROPERTY_TYPE = "Type";
	public static final String PROPERTY_SELECTION_COUNT = "SelectionCount";

	private List keys = new LinkedList();

	public String getKey(final int index) 
	{
		return (String )this.keys.get(index);
	}

	public List getKeys() 
	{
		return this.keys;
	}

	public String getName(final String key)
	{
		return "";
	}

	public Object getValue(final Object object, final String key)
	{
		return null;
	}

	public boolean isEditable(final String key)
	{
		return false;
	}

	public void setValue(Object object, final String key, final Object value)
	{
	}

	public Object getPropertyValue(final String key) 
	{
		return "";
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) 
	{
	}

	public Class getPropertyClass(String key) 
	{
		return String.class;
	}
}
