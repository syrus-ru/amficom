/*
 * ConstStorage.java
 * Created on 03.06.2004 18:08:38
 * 
 */
package com.syrus.AMFICOM.Client.Survey.General;

import java.text.SimpleDateFormat;

/**
 * @author Vladimir Dolzhenko
 * 
 * Storage for often used variables and constants
 */
public interface ConstStorage {

	final String			COLUMN_NAME_ACTION_ID		= "action_id";
	final String			COLUMN_NAME_ACTIVE			= "active";
	final String			COLUMN_NAME_CREATED			= "created";
	final String			COLUMN_NAME_DESCRIPTION		= "description";
	final String			COLUMN_NAME_DOMAIN_ID		= "domain_id";
	final String			COLUMN_NAME_END_TIME		= "end_time";
	final String			COLUMN_NAME_ID				= "id";
	final String			COLUMN_NAME_KIS_ID			= "kis_id";
	final String			COLUMN_NAME_LOCAL_ID		= "local_id";
	final String			COLUMN_NAME_MODIFIED		= "modified";
	final String			COLUMN_NAME_NAME			= "name";
	final String			COLUMN_NAME_PORT_ID			= "port_id";
	final String			COLUMN_NAME_REQUEST_ID		= "request_id";
	final String			COLUMN_NAME_RESULT_TYPE		= "result_type";
	final String			COLUMN_NAME_START_TIME		= "start_time";
	final String			COLUMN_NAME_STATUS			= "status";
	final String			COLUMN_NAME_TEMPORAL_TYPE	= "temporal_type";
	final String			COLUMN_NAME_TEST_TYPE_ID	= "test_type_id";
	final String			COLUMN_NAME_USER_ID			= "user_id";

	final String			DATE_FORMAT					= "dd.MM.yy hh:mm:ss";
	final SimpleDateFormat	SIMPLE_DATE_FORMAT			= new SimpleDateFormat(
																DATE_FORMAT);

	final String			SYS_DOMAIN					= "sysdomain";

}