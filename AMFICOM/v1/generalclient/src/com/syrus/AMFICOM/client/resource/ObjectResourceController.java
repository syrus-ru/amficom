
package com.syrus.AMFICOM.client.resource;

import java.text.SimpleDateFormat;
import java.util.List;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/24 06:54:50 $
 * @author $Author: bob $
 * @module generalclient_v1
 */

public interface ObjectResourceController {

	final String		COLUMN_ALARM_TYPE_NAME	= "alarm_type_name";
	final String		COLUMN_GENERATED	= "generated";
	final String		COLUMN_KIS_ID		= "kis_id";
	final String		COLUMN_LOCAL_ID		= "local_id";
	final String		COLUMN_ME_ID		= "monitored_element_id";
	final String		COLUMN_PORT_ID		= "port_id";
	final String		COLUMN_SOURCE_NAME	= "source_name";
	final String		COLUMN_START_TIME	= "start_time";
	final String		COLUMN_STATUS		= "status";
	final String		COLUMN_TEMPORAL_TYPE	= "temporal_type";
	final String		COLUMN_TEST_TYPE_ID	= "test_type_id";

	final String		COLUMN_TYPE_LIST	= "list";
	final String		COLUMN_TYPE_LONG	= "long";
	final String		COLUMN_TYPE_NUMERIC	= "numeric";
	final String		COLUMN_TYPE_RANGE	= "range";
	final String		COLUMN_TYPE_STRING	= "string";
	final String		COLUMN_TYPE_TIME	= "time";

	final String		DATE_FORMAT		= "dd.MM.yy HH:mm:ss";
	final SimpleDateFormat	SIMPLE_DATE_FORMAT	= new SimpleDateFormat(DATE_FORMAT);

	String getKey(final int index);

	List getKeys();

	String getName(final String key);

	Class getPropertyClass(final String key);

	Object getPropertyValue(final String key);
	
	void setPropertyValue(final String key, final Object objectKey,final Object objectValue);

	Object getValue(final ObjectResource objectResource, final String key);

	boolean isEditable(final String key);

	void setValue(ObjectResource objectResource, final String key, final Object value);

}