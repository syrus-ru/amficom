/*-
 * $Id: TypicalConditionImpl.java,v 1.10 2005/04/04 13:09:40 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Date;
import java.util.Set;

import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.TypicalSort;

/**
 * @version $Revision: 1.10 $, $Date: 2005/04/04 13:09:40 $
 * @author $Author: bass $
 * @module config_v1
 */
final class TypicalConditionImpl extends TypicalCondition {
	private TypicalConditionImpl(final int firstInt,
			final int secondInt,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.firstInt = firstInt;
		this.secondInt = secondInt;
		this.type = TypicalSort._TYPE_NUMBER_INT;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	private TypicalConditionImpl(final long firstLong,
			final long secondLong,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.firstLong = firstLong;
		this.secondLong = secondLong;
		this.type = TypicalSort._TYPE_NUMBER_LONG;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	private TypicalConditionImpl(final double firstDouble,
			final double secondDouble,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.firstDouble = firstDouble;
		this.secondDouble = secondDouble;
		this.type = TypicalSort._TYPE_NUMBER_DOUBLE;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	private TypicalConditionImpl(final String value,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.value = value;
		this.type = TypicalSort._TYPE_STRING;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	private TypicalConditionImpl(final Date firstDate,
			final Date secondDate,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.value = firstDate;
		this.otherValue = secondDate;
		this.type = TypicalSort._TYPE_DATE;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	public boolean isNeedMore(final Set storableObjects) {
		return this.type != TypicalSort._TYPE_STRING
				|| this.operation != OperationSort._OPERATION_EQUALS
				|| storableObjects == null
				|| storableObjects.isEmpty();
	}

	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		throw new IllegalObjectEntityException(ENTITY_NOT_REGISTERED + storableObject.getClass().getName(),
				IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

}
