/*
* $Id: StringFieldConditionImpl.java,v 1.1 2005/01/24 10:58:28 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.StringFieldSort;


/**
 * @version $Revision: 1.1 $, $Date: 2005/01/24 10:58:28 $
 * @author $Author: bob $
 * @module general_v1
 */
final class StringFieldConditionImpl extends StringFieldCondition {

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
		} 
		return condition;
	}

}
