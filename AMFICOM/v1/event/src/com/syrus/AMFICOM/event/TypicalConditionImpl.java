/*
 * $Id: TypicalConditionImpl.java,v 1.5 2005/03/24 12:16:38 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.Collection;
import java.util.Date;

import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.TypicalSort;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.5 $, $Date: 2005/03/24 12:16:38 $
 * @author $Author: arseniy $
 * @module event_v1
 */
class TypicalConditionImpl extends TypicalCondition {
	private TypicalConditionImpl(final int firstInt,
			final int secondInt,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		super(); // First line must invoke superconstructor w/o parameters.
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
		super(); // First line must invoke superconstructor w/o parameters.
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
		super(); // First line must invoke superconstructor w/o parameters.
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
		super(); // First line must invoke superconstructor w/o parameters.
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
		super(); // First line must invoke superconstructor w/o parameters.
		this.value = firstDate;
		this.otherValue = secondDate;
		this.type = TypicalSort._TYPE_DATE;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
		
	}

	public boolean isNeedMore(Collection collection) {
		boolean more = true;

		if (this.type == TypicalSort._TYPE_STRING && this.operation == OperationSort._OPERATION_EQUALS)
			if (collection != null && !collection.isEmpty())
				more = false;

		return more;
	}

	public boolean isConditionTrue(Object object) throws IllegalObjectEntityException {
		Wrapper wrapper;
		if (object instanceof EventType)
			wrapper = EventTypeWrapper.getInstance();
		else
			if (object instanceof Event)
				wrapper = EventWrapper.getInstance();
			else
				throw new IllegalObjectEntityException(ENTITY_NOT_REGISTERED + object.getClass().getName(),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);

		return super.parseCondition(wrapper.getValue(object, this.key));
	}

}
