/*
 * $Id: StringFieldCondition.java,v 1.10 2005/01/14 18:07:08 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.corba.StringFieldCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StringFieldSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;


/**
 * @version $Revision: 1.10 $, $Date: 2005/01/14 18:07:08 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class StringFieldCondition implements StorableObjectCondition {

	protected Short entityCode;
	protected String string;
	protected int sort;

	public StringFieldCondition(StringFieldCondition_Transferable transferable){
		this.string = transferable.field_string;
		this.entityCode = new Short(transferable.entity_code);
		this.sort = transferable.sort.value();
	}	
	
	public StringFieldCondition(String string, Short entityCode){
		this.string = string;
		this.entityCode = entityCode;		
		this.sort = StringFieldSort._STRINGSORT_BASE;		
	}
	
	
	
	public StringFieldCondition(String string, Short entityCode, StringFieldSort sort){
		this.string = string;
		this.entityCode = entityCode;		
		this.sort = sort.value();
	}

	public StringFieldCondition(String string, short entityCode, StringFieldSort sort){
		this(string, new Short(entityCode), sort);		
	}
	
	public StringFieldCondition(String string, short entityCode){
		this(string, entityCode, StringFieldSort.STRINGSORT_BASE);		
	}


	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
	/**
	 * @todo write something expressive
	 */
		return condition;
	}

	public boolean isNeedMore(List list) throws ApplicationException {
		boolean needMore = true;
		if(list != null){
			for (Iterator it = list.iterator(); it.hasNext();) {
				Object object = it.next();
				if (isConditionTrue(object)){
					needMore = false;
					break;
				}
				
			}
		}
		return needMore;
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
