/*
 * $Id: StringFieldCondition.java,v 1.1 2004/10/19 14:22:32 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.configuration.corba.StringFieldCondition_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;


/**
 * @version $Revision: 1.1 $, $Date: 2004/10/19 14:22:32 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class StringFieldCondition implements StorableObjectCondition {

	private Short entityCode;
	private String string;

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

	
	/**
	 * @return <code>true</code>
	 *         <ul>
	 * 
	 * <li>if {@link entityCode}is {@link User}for all users for
	 * login equals string;</li>
	 * 
	 * </ul>
	 */
	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		if (object instanceof User){
			User parameterType = (User)object;
			if (parameterType.getLogin().equals(this.string)){
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
