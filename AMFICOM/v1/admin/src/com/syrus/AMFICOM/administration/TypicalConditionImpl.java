/*
 * $Id: TypicalConditionImpl.java,v 1.2 2005/02/08 11:24:45 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.Date;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.TypicalSort;
import com.syrus.util.Log;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/08 11:24:45 $
 * @author $Author: arseniy $
 * @module admin_v1
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

	public boolean isNeedMore(List list) throws ApplicationException {
		return true;
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean result = false;
		Wrapper wrapper = null;
		if (object instanceof User) {
			wrapper = UserWrapper.getInstance();
		} else if (object instanceof Domain) {
			wrapper = DomainWrapper.getInstance();
		} else if (object instanceof Server) {
			wrapper = ServerWrapper.getInstance();
		} else if (object instanceof MCM) {
			wrapper = MCMWrapper.getInstance();
		}
		if (wrapper != null)
			result = super.parseCondition(wrapper.getValue(object, this.key));
		else
			Log.errorMessage("TypicalConditionImpl.isConditionTrue | Class " + object.getClass().getName()
					+ " is not supported");
		return result;
	}

}
