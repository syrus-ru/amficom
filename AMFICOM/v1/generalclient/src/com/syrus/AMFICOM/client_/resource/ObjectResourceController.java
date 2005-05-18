
package com.syrus.AMFICOM.client_.resource;

import java.text.SimpleDateFormat;
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
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2005/05/18 14:01:20 $
 * @see <a href =
 *      "http://bass.science.syrus.ru/java/Bitter%20Java.pdf">&laquo;Bitter
 *      Java&raquo; by Bruce A. Tate </a>
 * @module generalclient_v1
 */

public abstract class ObjectResourceController extends StorableObjectWrapper {

	public static final String			COLUMN_ALARM_TYPE_NAME	= "alarm_type_name";
	public static final String			COLUMN_GENERATED		= "generated";
	public static final String			COLUMN_KIS_ID			= "kis_id";
	public static final String			COLUMN_LOCAL_ID			= "local_id";
	public static final String			COLUMN_ME_ID			= "monitored_element_id";
	public static final String			COLUMN_PORT_ID			= "port_id";
	public static final String			COLUMN_SOURCE_NAME		= "source_name";
	public static final String			COLUMN_START_TIME		= "start_time";
	public static final String			COLUMN_STATUS			= "status";
	public static final String			COLUMN_TEMPORAL_TYPE	= "temporal_type";
	public static final String			COLUMN_TEST_TYPE_ID		= "test_type_id";

	public static final String			COLUMN_TYPE_LIST		= "list";
	public static final String			COLUMN_TYPE_LONG		= "long";
	public static final String			COLUMN_TYPE_NUMERIC		= "numeric";
	public static final String			COLUMN_TYPE_RANGE		= "range";
	public static final String			COLUMN_TYPE_STRING		= "string";
	public static final String			COLUMN_TYPE_TIME		= "time";

	public static final String			DATE_FORMAT				= "dd.MM.yy HH:mm:ss";
	public static final SimpleDateFormat	SIMPLE_DATE_FORMAT		= new SimpleDateFormat(DATE_FORMAT);

}
