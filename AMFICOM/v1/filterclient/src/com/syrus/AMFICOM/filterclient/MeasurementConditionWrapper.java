/*
 * $Id: MeasurementConditionWrapper.java,v 1.12 2006/05/30 12:10:51 stas Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.filterclient;

import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.MeasurementWrapper;
import com.syrus.AMFICOM.newFilter.ConditionKey;
import com.syrus.AMFICOM.newFilter.LangModelFilter;

/**
 * @version $Revision: 1.12 $, $Date: 2006/05/30 12:10:51 $
 * @author $Author: stas $
 * @module filterclient
 */
public class MeasurementConditionWrapper implements ConditionWrapper {

	private static short entityCode = MEASUREMENT_CODE;

	private final static String NAME = LangModelFilter.getString("filter.criteria.entityname");
	private final static String START_TIME = LangModelFilter.getString("filter.criteria.starttime");

	public final static ConditionKey START_TIME_CONDITION_KEY = new ConditionKey(MeasurementWrapper.COLUMN_START_TIME, START_TIME, ConditionWrapper.DATE);
	public final static ConditionKey NAME_CONDITION_KEY = new ConditionKey(StorableObjectWrapper.COLUMN_NAME, NAME, ConditionWrapper.STRING);
	
	private final static List<ConditionKey> keys = new ArrayList<ConditionKey>();

	static {
		keys.add(NAME_CONDITION_KEY);
		keys.add(START_TIME_CONDITION_KEY);
	}

	public List<ConditionKey> getKeys() {
		return Collections.unmodifiableList(keys);
	}

	public short getEntityCode() {
		return entityCode;
	}
}
