/*
 * $Id: StringFieldConditionImpl.java,v 1.1 2005/01/24 10:43:40 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.StringFieldSort;


/**
 * @version $Revision: 1.1 $, $Date: 2005/01/24 10:43:40 $
 * @author $Author: bob $
 * @module administration_v1
 */
final class StringFieldConditionImpl extends com.syrus.AMFICOM.general.StringFieldCondition {

	private StringFieldConditionImpl(String string, Short entityCode, StringFieldSort sort) {
		this.string = string;
		this.entityCode = entityCode;		
		this.sort = sort.value();
	}

	/**
	 * @return <code>true</code>
	 *         <ul>
	 * 
	 * <li>if {@link #entityCode} is {@link User} and sort is STRINGSORT_USERLOGIN for all users for
	 * login equals string;</li>
	 * <li>if {@link #entityCode} is {@link User} and sort is STRINGSORT_USERNAME for all users for
	 * name equals string;</li>
	 * 
	 * </ul>
	 */
	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		if (object instanceof User) {			
			User user = (User)object;
			switch(this.sort) {
				case StringFieldSort._STRINGSORT_BASE:
				case StringFieldSort._STRINGSORT_USERLOGIN:
					if (user.getLogin().equals(this.string)) {
						condition = true;						
					}
					break;
				case StringFieldSort._STRINGSORT_USERNAME:
					if (user.getName().equals(this.string)) {
						condition = true;
					}
				break;
			}
		} else if (object instanceof Domain) {
			Domain domain = (Domain)object;
			switch(this.sort) {
				case StringFieldSort._STRINGSORT_BASE:
					if (domain.getName().equals(this.string)) {
						condition = true;						
					}
					break;
			}
		}
		return condition;
	}	
}
