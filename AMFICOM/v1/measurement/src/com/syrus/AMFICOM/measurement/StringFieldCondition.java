/*
 * $Id: StringFieldCondition.java,v 1.7 2004/10/20 06:29:47 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.configuration.corba.StringFieldCondition_Transferable;
import com.syrus.AMFICOM.configuration.corba.StringFieldSort;
import com.syrus.AMFICOM.general.ApplicationException;


/**
 * @version $Revision: 1.7 $, $Date: 2004/10/20 06:29:47 $
 * @author $Author: bob $
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
	
	public StringFieldCondition(String string, Short entityCode, StringFieldSort sort){
		super(string, entityCode, sort);		
	}

	public StringFieldCondition(String string, short entityCode, StringFieldSort sort){
		super(string, entityCode, sort);		
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
			switch(getSort().value()){
				case StringFieldSort._STRINGSORT_BASE:
					if (parameterType.getCodename().equals(getString())){
						condition = true;
					}
					break;
			}
		} else if (object instanceof MeasurementType){
			MeasurementType measurementType = (MeasurementType)object;
			switch(getSort().value()){
				case StringFieldSort._STRINGSORT_BASE:
					if (measurementType.getCodename().equals(getString())){
						condition = true;
					}
				break;
			}
		} else{
			super.isConditionTrue(object);
		}
		return condition;
	}

}
