package com.syrus.AMFICOM.server.measurement;

import java.sql.SQLException;
import java.sql.Timestamp;

public abstract class Action {
	public static final int ACTION_TYPE_TEST = 0;
	public static final int ACTION_TYPE_ANALYSIS = 1;
	public static final int ACTION_TYPE_EVALUATION = 2;
	public static final int ACTION_TYPE_MODELING = 3;

	String id;
	String type_id;
	String monitored_element_id;
	Timestamp deleted;
	Timestamp modified;
	String name;
	String description;
	String domain_id;
	String user_id;

	public Action() {
	}

	public String getId() {
    return this.id;
  }

  public String getTypeId() {
    return this.type_id;
  }

	public String getDomainId() {
		return this.domain_id;
	}

	public String getUserId() {
		return this.user_id;
	}

	public abstract boolean isModified() throws SQLException;

	protected abstract void setModified() throws SQLException;
}