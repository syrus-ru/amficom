/*-
* $Id: XMLLinkedIdsConditionImpl.java,v 1.2 2005/09/14 19:01:24 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.administration;

import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.XMLLinkedIdsCondition;



/**
 * @version $Revision: 1.2 $, $Date: 2005/09/14 19:01:24 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module administration
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
			case ObjectEntities.MCM_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.SERVER_CODE:
						return super.getIdsByCondition(MCMWrapper.COLUMN_SERVER_ID);
					case ObjectEntities.DOMAIN_CODE:
						return super.getIdsByCondition(DomainMember.COLUMN_DOMAIN_ID);
					default:
						throw newExceptionLinkedEntityIllegal();
				}
			case ObjectEntities.DOMAIN_CODE:
				return super.getIdsByCondition(DomainMember.COLUMN_DOMAIN_ID);
			case ObjectEntities.SERVER_CODE:
				return super.getIdsByCondition(DomainMember.COLUMN_DOMAIN_ID);
			case ObjectEntities.PERMATTR_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.DOMAIN_CODE:
						return super.getIdsByCondition(DomainMember.COLUMN_DOMAIN_ID);
					case ObjectEntities.SYSTEMUSER_CODE:
						return super.getIdsByCondition(PermissionAttributesWrapper.COLUMN_USER_ID);
					default:
						throw newExceptionLinkedEntityIllegal();
				}			
			default:
				throw newExceptionEntityIllegal();
		}
	}
}

