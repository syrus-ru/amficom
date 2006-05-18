/*-
* $Id: XMLLinkedIdsConditionImpl.java,v 1.4 2006/02/28 15:19:58 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;
import static com.syrus.AMFICOM.administration.MCMWrapper.COLUMN_SERVER_ID;
import static com.syrus.AMFICOM.administration.PermissionAttributesWrapper.COLUMN_PARENT_ID;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PERMATTR_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ROLE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;

import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.XMLLinkedIdsCondition;

/**
 * @version $Revision: 1.4 $, $Date: 2006/02/28 15:19:58 $
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
			case MCM_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case SERVER_CODE:
						return super.getIdsByCondition(COLUMN_SERVER_ID);
					case DOMAIN_CODE:
						return super.getIdsByCondition(COLUMN_DOMAIN_ID);
					default:
						throw newExceptionLinkedEntityIllegal();
				}
			case DOMAIN_CODE:
				return super.getIdsByCondition(COLUMN_DOMAIN_ID);
			case SERVER_CODE:
				return super.getIdsByCondition(COLUMN_DOMAIN_ID);
			case PERMATTR_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case DOMAIN_CODE:
						return super.getIdsByCondition(COLUMN_DOMAIN_ID);
					case ROLE_CODE:
					case SYSTEMUSER_CODE:
						return super.getIdsByCondition(COLUMN_PARENT_ID);
					default:
						throw newExceptionLinkedEntityIllegal();
				}			
			default:
				throw newExceptionEntityIllegal();
		}
	}
}

