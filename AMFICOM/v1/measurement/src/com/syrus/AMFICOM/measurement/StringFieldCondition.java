/*
 * $Id: StringFieldCondition.java,v 1.6 2004/10/19 14:30:31 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.configuration.corba.StringFieldCondition_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;


/**
 * @version $Revision: 1.6 $, $Date: 2004/10/19 14:30:31 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class StringFieldCondition extends com.syrus.AMFICOM.configuration.StringFieldCondition {

	private static StringFieldCondition	instance = null;
	private static boolean			initialized	= false;
	private static Object			lock		= new Object();
	
	private StringFieldCondition() {
		super(null, null);
	}
	
	public StringFieldCondition(StringFieldCondition_Transferable transferable){
		super(transferable);
	}
	
	public StringFieldCondition(String string, Short entityCode){
		super(string, entityCode);		
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
			if (parameterType.getCodename().equals(getString())){
				condition = true;
			}
		} else if (object instanceof MeasurementType){
			MeasurementType measurementType = (MeasurementType)object;
			if (measurementType.getCodename().equals(getString())){
				condition = true;
			}
		} else{
			super.isConditionTrue(object);
		}
		return condition;
	}

}
