/*
 * $Id: StringFieldCondition.java,v 1.3 2004/10/13 11:07:37 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.measurement.corba.StringFieldCondition_Transferable;


/**
 * @version $Revision: 1.3 $, $Date: 2004/10/13 11:07:37 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class StringFieldCondition implements StorableObjectCondition {

	private Short entityCode;
	private String string;

	private static StringFieldCondition	instance = null;
	private static boolean			initialized	= false;
	private static Object			lock		= new Object();
	
	private StringFieldCondition() {
		// empty
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
	
	public StringFieldCondition(StringFieldCondition_Transferable transferable){
		this.string = transferable.field_string;
		this.entityCode = new Short(transferable.entity_code);
	}
	
	public StringFieldCondition(String string, Short entityCode){
		this.string = string;
		this.entityCode = entityCode;		
	}

	public StringFieldCondition(String string, short entityCode){
		this(string, new Short(entityCode));		
	}

	
	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		if (object instanceof ParameterType){
			ParameterType parameterType = (ParameterType)object;
			if (parameterType.getCodename().equals(this.string)){
				condition = true;
			}
		} else if (object instanceof MeasurementType){
			MeasurementType measurementType = (MeasurementType)object;
			if (measurementType.getCodename().equals(this.string)){
				condition = true;
			}
		}
		return condition;
	}

	public Short getEntityCode() {
		return this.entityCode;
	}

	public void setEntityCode(Short entityCode) {
		this.entityCode = entityCode;

	}

	public Object getTransferable() {
		return new StringFieldCondition_Transferable(this.entityCode.shortValue(), this.string);
	}

	public String getString() {
		return this.string;
	}
	
	public void setString(String string) {
		this.string = string;
	}
}
