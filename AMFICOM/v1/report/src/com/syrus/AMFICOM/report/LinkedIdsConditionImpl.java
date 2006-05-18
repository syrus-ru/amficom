/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.4 2006/03/27 11:21:39 bass Exp $
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

import java.util.Set;

import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @version $Revision: 1.4 $
 * @author $Author: bass $
 * @module report
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {
	@SuppressWarnings("unused")
	private LinkedIdsConditionImpl(final Set<? extends Identifiable> linkedIdentifiables, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIdentifiables = linkedIdentifiables;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	@Override
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		switch (this.entityCode.shortValue()) {
			case ATTACHEDTEXT_CODE:
				final AttachedTextStorableElement attText = (AttachedTextStorableElement) storableObject;
				switch (this.linkedEntityCode) {
				case REPORTTEMPLATE_CODE:
					return super.conditionTest(attText.getReportTemplateId());
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case REPORTIMAGE_CODE:
				final ImageStorableElement reportImage = (ImageStorableElement) storableObject;
				switch (this.linkedEntityCode) {
				case REPORTTEMPLATE_CODE:
					return super.conditionTest(reportImage.getReportTemplateId());
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case REPORTDATA_CODE:
				final DataStorableElement reportData = (DataStorableElement) storableObject;
				switch (this.linkedEntityCode) {
				case REPORTTEMPLATE_CODE:
					return super.conditionTest(reportData.getReportTemplateId());
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case REPORTTABLEDATA_CODE:
				final TableDataStorableElement reportTableData = (TableDataStorableElement) storableObject;
				switch (this.linkedEntityCode) {
				case REPORTTEMPLATE_CODE:
					return super.conditionTest(reportTableData.getReportTemplateId());
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			default:
				throw newExceptionEntityIllegal();
		}		
	}

	@Override
	public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
		return true;
	}
}
