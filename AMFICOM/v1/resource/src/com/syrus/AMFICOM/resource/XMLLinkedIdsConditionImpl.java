/*-
* $Id: XMLLinkedIdsConditionImpl.java,v 1.1 2005/08/22 13:52:15 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resource;

import static com.syrus.AMFICOM.general.ObjectEntities.LAYOUT_ITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;

import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.XMLLinkedIdsCondition;


/**
 * @version $Revision: 1.1 $, $Date: 2005/08/22 13:52:15 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module resource
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
			case LAYOUT_ITEM_CODE:
				switch (super.condition.getLinkedEntityCode()) {
				/* General */
				case SYSTEMUSER_CODE:
					return super.getIdsByCondition(StorableObjectWrapper.COLUMN_CREATOR_ID);
					/* Resource */
				case LAYOUT_ITEM_CODE:
					return super.getIdsByCondition(LayoutItemWrapper.COLUMN_PARENT_ID);
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			default:
				throw newExceptionEntityIllegal();
		}
	}
}

