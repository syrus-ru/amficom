package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.ObjectEntities;

public class TemporalPattern_Database extends StorableObject_Database {
	public static final String COLUMN_ID;
	public static final String COLUMN_CREATED;
	public static final String COLUMN_MODIFIED;
	public static final String COLUMN_CREATOR_ID;
	public static final String COLUMN_MODIFIER_ID;
	public static final String COLUMN_DESCRIPTION;
	public static final String COLUMN_VALUE;

	private TemporalPattern fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof TemporalPattern)
			return (TemporalPattern)storableObject;
		else
			throw new Exception("TemporalPattern_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		this.retrieveTemporalPattern(temporalPattern);
		this.retrieveCronStrings(temporalPattern);
	}

	private void retrieveTemporalPattern(TemporalPattern temporalPattern) throws Exception {
		String tp_id_str = temporalPattern.getId().toString();
		StringBuffer sql = new StringBuffer("SELECT ");
		sql.append(DatabaseDate.toQuerySubString(COLUMN_CREATED) + ", ");
		sql.append(DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + ", ");
		sql.append(COLUMN_CREATOR_ID + ", ");
		sql.append(COLUMN_MODIFIER_ID + ", ");
		sql.append(COLUMN_DESCRIPTION + ", ");
		sql.append(COLUMN_VALUE);
		sql.append(" FROM " + ObjectEntities.TEMPORALPATTERN_ENTITY);
		sql.append(" WHERE " + COLUMN_ID + " = " + tp_id_str);
/*
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + ", " 
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + ", "
			+ COLUMN_CREATOR_ID + ", "
			+ COLUMN_MODIFIER_ID + ", "
			+ COLUMN_DESCRIPTION + ", "
			+ COLUMN_VALUE
			+ " FROM " + ObjectEntities.TEMPORALPATTERN_ENTITY
			+ " WHERE " + COLUMN_ID + " = " + tp_id_str;*/
	}

	
}
