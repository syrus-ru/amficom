package com.syrus.AMFICOM.Client.Survey.Alarm.Sorter;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import java.util.*;

public class AlarmSorter extends ObjectResourceSorter {

	String[][]	sortedColumns	= new String[][] { { COLUMN_ME_ID, COLUMN_TYPE_STRING},
			{ COLUMN_GENERATED, COLUMN_TYPE_LONG}, { COLUMN_STATUS, COLUMN_TYPE_STRING},
			{ COLUMN_ALARM_TYPE_NAME, COLUMN_TYPE_STRING}, { COLUMN_SOURCE_NAME, COLUMN_TYPE_STRING}};

	public AlarmSorter() {
		super();
	}

	public AlarmSorter(List list) {
		super(list);
	}

	public String[][] getSortedColumns() {
		return this.sortedColumns;
	}

	public String getString(ObjectResource or, String column) {
		Alarm alarm = (Alarm) or;
		if (column.equals(COLUMN_ME_ID)) {
			return alarm.getModel().getColumnValue(COLUMN_ME_ID);
		} else if (column.equals(COLUMN_ALARM_TYPE_NAME)) {
			return alarm.getModel().getColumnValue(COLUMN_ALARM_TYPE_NAME);
		} else if (column.equals(COLUMN_SOURCE_NAME)) {
			return alarm.getModel().getColumnValue(COLUMN_SOURCE_NAME);
		} else if (column.equals(COLUMN_STATUS)) { return alarm.getModel().getColumnValue(COLUMN_STATUS); }
		return null;
	}

	public long getLong(ObjectResource or, String column) {
		if (column.equals(COLUMN_GENERATED))
			return ((Alarm) or).generated;
		return 0;
	}

}

