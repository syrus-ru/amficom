
package com.syrus.AMFICOM.client_.resource;

import java.text.SimpleDateFormat;
import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * Controller provides data from Model (ObjectResource) using key set.
 * 
 * Model has various fields, accessors for them and many other things, which are
 * represented through controller to viewers using the same interface of
 * interaction.
 * 
 * All entities of the same kind use a single Contoller, that's why controller's
 * constructor must be private and its instance must be obtained using a static
 * method <code>getInstance()</code>.
 * 
 * @author $Author: bob $
 * @version $Revision: 1.5 $, $Date: 2005/02/03 09:12:04 $
 * @see <a href =
 *      "http://bass.science.syrus.ru/java/Bitter%20Java.pdf">&laquo;Bitter
 *      Java&raquo; by Bruce A. Tate </a>
 * @module generalclient_v1
 */

public interface ObjectResourceController extends StorableObjectWrapper {

	final String			COLUMN_ALARM_TYPE_NAME	= "alarm_type_name";
	final String			COLUMN_GENERATED		= "generated";
	final String			COLUMN_KIS_ID			= "kis_id";
	final String			COLUMN_LOCAL_ID			= "local_id";
	final String			COLUMN_ME_ID			= "monitored_element_id";
	final String			COLUMN_PORT_ID			= "port_id";
	final String			COLUMN_SOURCE_NAME		= "source_name";
	final String			COLUMN_START_TIME		= "start_time";
	final String			COLUMN_STATUS			= "status";
	final String			COLUMN_TEMPORAL_TYPE	= "temporal_type";
	final String			COLUMN_TEST_TYPE_ID		= "test_type_id";

	final String			COLUMN_TYPE_LIST		= "list";
	final String			COLUMN_TYPE_LONG		= "long";
	final String			COLUMN_TYPE_NUMERIC		= "numeric";
	final String			COLUMN_TYPE_RANGE		= "range";
	final String			COLUMN_TYPE_STRING		= "string";
	final String			COLUMN_TYPE_TIME		= "time";

	final String			DATE_FORMAT				= "dd.MM.yy HH:mm:ss";
	final SimpleDateFormat	SIMPLE_DATE_FORMAT		= new SimpleDateFormat(DATE_FORMAT);

}
