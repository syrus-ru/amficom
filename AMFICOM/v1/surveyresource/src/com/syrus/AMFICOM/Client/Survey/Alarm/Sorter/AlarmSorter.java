package com.syrus.AMFICOM.Client.Survey.Alarm.Sorter;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import java.util.*;

public class AlarmSorter extends ObjectResourceSorter {

	String[][]	sortedColumns	= new String[][] {
			{ ObjectResourceModel.COLUMN_ME_ID, ObjectResourceModel.COLUMN_TYPE_STRING},
			{ ObjectResourceModel.COLUMN_GENERATED, ObjectResourceModel.COLUMN_TYPE_LONG},
			{ ObjectResourceModel.COLUMN_STATUS, ObjectResourceModel.COLUMN_TYPE_STRING},
			{ ObjectResourceModel.COLUMN_ALARM_TYPE_NAME, ObjectResourceModel.COLUMN_TYPE_STRING},
			{ ObjectResourceModel.COLUMN_SOURCE_NAME, ObjectResourceModel.COLUMN_TYPE_STRING}};

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
		if (column.equals(ObjectResourceModel.COLUMN_ME_ID)) {
			return alarm.getModel().getColumnValue(ObjectResourceModel.COLUMN_ME_ID);
		} else if (column.equals(ObjectResourceModel.COLUMN_ALARM_TYPE_NAME)) {
			return alarm.getModel().getColumnValue(ObjectResourceModel.COLUMN_ALARM_TYPE_NAME);
		} else if (column.equals(ObjectResourceModel.COLUMN_SOURCE_NAME)) {
			return alarm.getModel().getColumnValue(ObjectResourceModel.COLUMN_SOURCE_NAME);
		} else if (column.equals(ObjectResourceModel.COLUMN_STATUS)) { return alarm.getModel().getColumnValue(ObjectResourceModel.COLUMN_STATUS); }
		return null;
	}

	public long getLong(ObjectResource or, String column) {
		if (column.equals(ObjectResourceModel.COLUMN_GENERATED))
			return ((Alarm) or).generated;
		return 0;
	}

}

