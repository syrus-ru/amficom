/*
 * $Id: StringFieldCondition.java,v 1.11 2004/12/07 10:59:48 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.corba.StringFieldCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StringFieldSort;
import com.syrus.AMFICOM.general.ApplicationException;


/**
 * @version $Revision: 1.11 $, $Date: 2004/12/07 10:59:48 $
 * @author $Author: bass $
 * @module measurement_v1
 */
public class StringFieldCondition extends com.syrus.AMFICOM.configuration.StringFieldCondition {

	private static StringFieldCondition	instance = null;
	private static boolean			initialized	= false;
	private static Object			lock		= new Object();
	
	private StringFieldCondition() {
		super(null, null, StringFieldSort.STRINGSORT_BASE);
	}
	
	public StringFieldCondition(StringFieldCondition_Transferable transferable){
		super(transferable);
	}

	public StringFieldCondition(String string, Short entityCode){
		super(string, entityCode);		
	}

	public StringFieldCondition(String string, Short entityCode, StringFieldSort sort){
		super(string, entityCode, sort);		
	}

	public StringFieldCondition(String string, short entityCode, StringFieldSort sort){
		super(string, entityCode, sort);		
	}
	
	public StringFieldCondition(String string, short entityCode){
		super(string, entityCode);		
	}

	public static StringFieldCondition getInstance() {
		if (!initialized) {
			synchronized (lock) {
				if (!initialized && instance == null)
					instance = new StringFieldCondition();
			}
			synchronized (lock) {
				initialized = true;
			}
		}
		return instance;
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;		
		if (object instanceof ParameterType){
			ParameterType parameterType = (ParameterType)object;
			switch(this.sort){
				case StringFieldSort._STRINGSORT_BASE:
					if (parameterType.getCodename().equals(this.string)){
						condition = true;
					}
					break;
			}
		} else if (object instanceof MeasurementType){
			MeasurementType measurementType = (MeasurementType)object;
			switch(this.sort){
				case StringFieldSort._STRINGSORT_BASE:
					if (measurementType.getCodename().equals(this.string)){
						condition = true;
					}
				break;
			}
		} else{
			condition = super.isConditionTrue(object);
		}
		return condition;
	}

}
