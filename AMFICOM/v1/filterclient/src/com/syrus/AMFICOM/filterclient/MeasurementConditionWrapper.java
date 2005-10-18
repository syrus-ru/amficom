/*
 * $Id: MeasurementConditionWrapper.java,v 1.9 2005/10/18 07:36:47 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.filterclient;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.MeasurementWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;
import com.syrus.AMFICOM.newFilter.LangModelFilter;

/**
 * @version $Revision: 1.9 $, $Date: 2005/10/18 07:36:47 $
 * @author $Author: max $
 * @module filterclient
 */
public class MeasurementConditionWrapper implements ConditionWrapper {

	private static short entityCode = ObjectEntities.MEASUREMENT_CODE;

	private final static String NAME = LangModelFilter.getString("filter.criteria.entityname");
	private final static String START_TIME = LangModelFilter.getString("filter.criteria.starttime");

	private static List<ConditionKey> keys = new ArrayList<ConditionKey>();

	static {
		keys.add(new ConditionKey(StorableObjectWrapper.COLUMN_NAME, NAME, ConditionWrapper.STRING));
		keys.add(new ConditionKey(MeasurementWrapper.COLUMN_START_TIME, START_TIME, ConditionWrapper.DATE));
	}

	public List<ConditionKey> getKeys() {
		return keys;
	}

	public short getEntityCode() {
		return entityCode;
	}
}
