/*
 * $Id: StringFieldConditionImpl.java,v 1.1 2005/01/24 10:47:40 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.corba.StringFieldSort;


/**
 * @version $Revision: 1.1 $, $Date: 2005/01/24 10:47:40 $
 * @author $Author: bob $
 * @module measurement_v1
 */
final class StringFieldConditionImpl extends com.syrus.AMFICOM.general.StringFieldCondition {

	private StringFieldConditionImpl(final String string, final Short entityCode, final StringFieldSort sort) {
		super();
		this.string = string;
		this.entityCode = entityCode;
		this.sort = sort.value();
	}
	
	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;		
		if (object instanceof ParameterType) {
			ParameterType parameterType = (ParameterType)object;
			switch(this.sort){
				case StringFieldSort._STRINGSORT_BASE:
					if (parameterType.getCodename().equals(this.string)) {
						condition = true;
					}
					break;
			}
		} else if (object instanceof MeasurementType) {
			MeasurementType measurementType = (MeasurementType)object;
			switch(this.sort){
				case StringFieldSort._STRINGSORT_BASE:
					if (measurementType.getCodename().equals(this.string)) {
						condition = true;
					}
				break;
			}
		} 
		return condition;
	}

}
