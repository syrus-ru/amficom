/*
 * $Id: SqlConstants.java,v 1.1 2004/10/18 15:16:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/10/18 15:16:58 $
 * @module servermisc_v1
 */
public interface SqlConstants {
	boolean DEBUG = true;

	char APOSTROPHE = '\'';

	String COMMA = ", ";
	String EQUALS = " = ";

	String COLUMN_CODENAME = "codename";
	String COLUMN_CONTACT = "contact";
	String COLUMN_CREATED_BY = "created_by";
	String COLUMN_CREATED = "created";
	String COLUMN_DESCRIPTION = "description";
	String COLUMN_DOMAIN_ID = "domain_id";
	String COLUMN_HOSTNAME = "hostname";
	String COLUMN_ID = "id";
	String COLUMN_IMAGE_ID = "image_id";
	String COLUMN_LICENSE_ID = "license_id";
	String COLUMN_LOCATION = "location";
	String COLUMN_LOGIN = "login";
	String COLUMN_MODIFIED_BY = "modified_by";
	String COLUMN_MODIFIED = "modified";
	String COLUMN_NAME = "name";
	String COLUMN_OBJECT_PERMISSION_ID = "object_permission_id";
	String COLUMN_OPERATIONAL_ID = "operational_id";
	String COLUMN_OPERATOR_ID = "operator_id";
	String COLUMN_ORGANIZATION_ID = "organization_id";
	String COLUMN_OWNER_ID = "owner_id";
	String COLUMN_SUBSCRIBER_ID = "subscriber_id";
	String COLUMN_TYPE = "type";
	String COLUMN_VERSION = "version";

	String KEYWORD_FROM = " FROM ";
	String KEYWORD_SELECT = "SELECT ";
	String KEYWORD_WHERE = " WHERE ";

	String TABLE_AGENTS = "amficom.agents";
	String TABLE_CLIENTS = "amficom.clients";
	String TABLE_DOMAINS = "amficom.domains";
	String TABLE_IMAGERESOURCES = "amficom.imageresources";
	String TABLE_SERVERS = "amficom.servers";
	String TABLE_USERS = "amficom.users";
}
