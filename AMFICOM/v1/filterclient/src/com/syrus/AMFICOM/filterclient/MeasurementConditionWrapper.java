/*
 * $Id: MeasurementConditionWrapper.java,v 1.7 2005/08/09 20:34:31 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.filterclient;

import java.util.ArrayList;
import java.util.Collection;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.MeasurementWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/09 20:34:31 $
 * @author $Author: arseniy $
 * @module filterclient
 */
public class MeasurementConditionWrapper implements ConditionWrapper {

	private static short entityCode = ObjectEntities.MEASUREMENT_CODE;

	private final static String NAME = "Entity name";
	private final static String START_TIME = "Start time";

	private static Collection<ConditionKey> keys = new ArrayList<ConditionKey>();

	static {
		keys.add(new ConditionKey(StorableObjectWrapper.COLUMN_NAME, NAME, ConditionWrapper.STRING));
		keys.add(new ConditionKey(MeasurementWrapper.COLUMN_START_TIME, START_TIME, ConditionWrapper.DATE));
	}

	public Collection<ConditionKey> getKeys() {
		return keys;
	}

	public short getEntityCode() {
		return entityCode;
	}
}
