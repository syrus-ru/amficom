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

public class TemporalPattern_Database extends StorableObject_Database  {

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
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString("created") + ", " 
			+ DatabaseDate.toQuerySubString("modified") + ", "
			+ "creator_id, "
			+ "modifier_id, "
			+ "description, "
			+ "cron_strings"
			+ " FROM " + ObjectEntities.TEMPORALPATTERN_ENTITY
			+ " WHERE id = " + tp_id_str;
	}

	
}
