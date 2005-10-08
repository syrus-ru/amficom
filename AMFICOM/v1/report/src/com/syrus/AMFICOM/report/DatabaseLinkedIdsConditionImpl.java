/*-
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.2 2005/10/08 13:16:31 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import static com.syrus.AMFICOM.general.ObjectEntities.ATTACHEDTEXT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTDATA_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTIMAGE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTTABLEDATA_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.REPORTTEMPLATE_CODE;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;

/**
 * @author Maxim Selivanov
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/10/08 13:16:31 $
 * @module report
 */
public class DatabaseLinkedIdsConditionImpl extends
		AbstractDatabaseLinkedIdsCondition {

	@SuppressWarnings("unused")
	private DatabaseLinkedIdsConditionImpl(
			final LinkedIdsCondition linkedIdsCondition) {
		super(linkedIdsCondition);
	}

	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
		case ATTACHEDTEXT_CODE:
			switch (super.condition.getLinkedEntityCode()) {
			case REPORTTEMPLATE_CODE:
				return super.getQuery(StorableElementWrapper.COLUMN_REPORT_TEMPLATE_ID);
			default:
				throw newExceptionLinkedEntityIllegal();
			}
		case REPORTIMAGE_CODE:
			switch (super.condition.getLinkedEntityCode()) {
			case REPORTTEMPLATE_CODE:
				return super.getQuery(StorableElementWrapper.COLUMN_REPORT_TEMPLATE_ID);
			default:
				throw newExceptionLinkedEntityIllegal();
			}
		case REPORTDATA_CODE:
			switch (super.condition.getLinkedEntityCode()) {
			case REPORTTEMPLATE_CODE:
				return super.getQuery(StorableElementWrapper.COLUMN_REPORT_TEMPLATE_ID);
			default:
				throw newExceptionLinkedEntityIllegal();
			}
		case REPORTTABLEDATA_CODE:
			switch (super.condition.getLinkedEntityCode()) {
			case REPORTTEMPLATE_CODE:
				return super.getQuery(StorableElementWrapper.COLUMN_REPORT_TEMPLATE_ID);
			default:
				throw newExceptionLinkedEntityIllegal();
			}
		default:
			throw newExceptionEntityIllegal();
		}
	}
}
