/*
 * $Id: StringFieldConditionImpl.java,v 1.1 2005/01/24 10:53:49 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.StringFieldSort;


/**
 * @version $Revision: 1.1 $, $Date: 2005/01/24 10:53:49 $
 * @author $Author: bob $
 * @module map_v1
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
		if (object instanceof PhysicalLinkType){			
			PhysicalLinkType linkType = (PhysicalLinkType )object;
			switch(this.sort){
				case StringFieldSort._STRINGSORT_BASE:
					if (linkType.getCodename().lastIndexOf(this.string) != -1){
						condition = true;						
					}
					break;
			}
		} 
		else if (object instanceof SiteNodeType){
			SiteNodeType siteType = (SiteNodeType )object;
			switch(this.sort){
				case StringFieldSort._STRINGSORT_BASE:
					if (siteType.getCodename().indexOf(this.string) != -1){
						condition = true;						
					}
					break;
			}
		}
		return condition;
	}
}
