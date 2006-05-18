/*-
* $Id: XMLLinkedIdsConditionImpl.java,v 1.1 2005/11/03 17:12:25 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import java.util.Set;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.XMLLinkedIdsCondition;



/**
 * @version $Revision: 1.1 $, $Date: 2005/11/03 17:12:25 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module measurement
 */
public class XMLLinkedIdsConditionImpl extends XMLLinkedIdsCondition {

	@SuppressWarnings("unused")
	private XMLLinkedIdsConditionImpl(final LinkedIdsCondition condition,
	                            final StorableObjectXMLDriver driver) {
		super();
		super.condition = condition;
		super.driver = driver;
	}
	
	@Override
	public Set<Identifier> getIdsByCondition() throws IllegalDataException {		
		switch (super.condition.getEntityCode().shortValue()) {			
			case ObjectEntities.KIS_CODE:
				return super.getIdsByCondition(DomainMember.COLUMN_DOMAIN_ID);				
			default:
				throw newExceptionEntityIllegal();
		}
	}
}

