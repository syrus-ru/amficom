package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.StorableObject_Database;

public class MeasurementDatabaseContext {
	public static StorableObject_Database measurementDatabase;
	public static StorableObject_Database temporalPatternDatabase;

	public static void init(StorableObject_Database measurementDatabase1,
													StorableObject_Database temporalPatternDatabase1) {
		measurementDatabase = measurementDatabase1;
		temporalPatternDatabase = temporalPatternDatabase1;
	}
}
