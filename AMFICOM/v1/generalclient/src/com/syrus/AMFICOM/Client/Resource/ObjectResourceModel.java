/*
 * $Id: ObjectResourceModel.java,v 1.6 2004/09/27 16:09:13 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.Client.General.UI.*;
import java.awt.Component;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2004/09/27 16:09:13 $
 * @module generalclient_v1
 */
public class ObjectResourceModel
{	
	public static final String	COLUMN_ALARM_TYPE_NAME	= "alarm_type_name";
	public static final String	COLUMN_GENERATED		= "generated";
	public static final String	COLUMN_KIS_ID			= "kis_id";
	public static final String	COLUMN_LOCAL_ID			= "local_id";
	public static final String	COLUMN_ME_ID			= "monitored_element_id";
	public static final String	COLUMN_PORT_ID			= "port_id";
	public static final String	COLUMN_SOURCE_NAME		= "source_name";
	public static final String	COLUMN_START_TIME		= "start_time";
	public static final String	COLUMN_STATUS			= "status";
	public static final String	COLUMN_TEMPORAL_TYPE	= "temporal_type";
	public static final String	COLUMN_TEST_TYPE_ID		= "test_type_id";
	
	public static final String 	COLUMN_TYPE_LIST		= "list";
	public static final String	COLUMN_TYPE_LONG		= "long";
	public static final String 	COLUMN_TYPE_NUMERIC		= "numeric";
	public static final String 	COLUMN_TYPE_RANGE		= "range";
	public static final String	COLUMN_TYPE_STRING		= "string";
	public static final String	COLUMN_TYPE_TIME		= "time";	
	
	protected ObjectResource or;
	private LinkedList children = new LinkedList();
	private ObjectResourcePropertiesPane panel = new GeneralPanel();

	private LinkedList propertyColumns = new LinkedList();

	public ObjectResourceModel()
	{
	}

	public ObjectResourceModel(ObjectResource or)
	{
		this.or = or;
	}

	public Collection getChildren(String key)
	{
		return children;
	}
	
	public int getColumnCount(){
		throw new UnsupportedOperationException("this method must be override!");
	}

	public String getColumnValue(String col_id)
	{
		return "";
	}

	public List getPropertyColumns()
	{
		return propertyColumns;
	}

	public Component getPropertyEditor(String col_id)
    {
		return null;
	}

	public String getPropertyName(String col_id)
	{
		return "";
	}

	public ObjectResourcePropertiesPane getPropertyPane()
	{
		return panel;
	}
	
	public Component getPropertyRenderer(String col_id)
    {
		return null;
	}

	public String getPropertyValue(String col_id)
	{
		return "";
	}
	
	public boolean isPropertyEditable(String col_id)
	{
		return false;
	}

	public void setColumnValue(String col_id, Object val)
	{
	}

	public void setPropertyValue(String col_id, Object val)
	{
	}
}
