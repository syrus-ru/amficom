/*
 * $Id: StringFieldCondition.java,v 1.3 2004/10/20 06:29:19 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.configuration.corba.StringFieldCondition_Transferable;
import com.syrus.AMFICOM.configuration.corba.StringFieldSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;


/**
 * @version $Revision: 1.3 $, $Date: 2004/10/20 06:29:19 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class StringFieldCondition implements StorableObjectCondition {

	private Short entityCode;
	private String string;
	private int sort;

	public StringFieldCondition(StringFieldCondition_Transferable transferable){
		this.string = transferable.field_string;
		this.entityCode = new Short(transferable.entity_code);
		this.sort = transferable.sort.value();
	}
	
	public StringFieldCondition(String string, Short entityCode, StringFieldSort sort){
		this.string = string;
		this.entityCode = entityCode;		
		this.sort = sort.value();
	}

	public StringFieldCondition(String string, short entityCode, StringFieldSort sort){
		this(string, new Short(entityCode), sort);		
	}

	
	/**
	 * @return <code>true</code>
	 *         <ul>
	 * 
	 * <li>if {@link entityCode} is {@link User} and sort is STRINGSORT_USERLOGIN for all users for
	 * login equals string;</li>
	 * <li>if {@link entityCode} is {@link User} and sort is STRINGSORT_USERNAME for all users for
	 * name equals string;</li>
	 * 
	 * </ul>
	 */
	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		if (object instanceof User){			
			User user = (User)object;
			switch(this.sort){
				case StringFieldSort._STRINGSORT_BASE:
				case StringFieldSort._STRINGSORT_USERLOGIN:
					if (user.getLogin().equals(this.string)){
						condition = true;						
					}
					break;
				case StringFieldSort._STRINGSORT_USERNAME:
					if (user.getName().equals(this.string)){
						condition = true;
					}
				break;
			}
		} 
		return condition;
	}

	public Short getEntityCode() {
		return this.entityCode;
	}

	public void setEntityCode(short entityCode){
		setEntityCode(new Short(entityCode));
	}
	
	public void setEntityCode(Short entityCode) {
		this.entityCode = entityCode;

	}

	public Object getTransferable() {
		return new StringFieldCondition_Transferable(this.entityCode.shortValue(), this.string, StringFieldSort.from_int(this.sort));
	}

	public String getString() {
		return this.string;
	}
	
	public void setString(String string) {
		this.string = string;
	}
	
	public void setSort(StringFieldSort sort){
		this.sort = sort.value();
	}
	
	public StringFieldSort getSort(){
		return StringFieldSort.from_int(this.sort);
	}
}
