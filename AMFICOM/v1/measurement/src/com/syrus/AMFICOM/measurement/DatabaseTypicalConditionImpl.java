/*-
 * $Id: DatabaseTypicalConditionImpl.java,v 1.28.2.2 2006/02/14 01:09:56 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONTEMPLATE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PERIODICALTEMPORALPATTERN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.28.2.2 $, $Date: 2006/02/14 01:09:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
final class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	@SuppressWarnings("unused")
	private DatabaseTypicalConditionImpl(final TypicalCondition typicalCondition) {
		super(typicalCondition);
	}

	@Override
	protected String getLinkedThisColumnName() throws IllegalObjectEntityException {
		throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
				+ "' is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}
	
	@Override
	protected String getLinkedColumnName() throws IllegalObjectEntityException {
		throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
				+ "' is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	@Override
	protected String getLinkedTableName() throws IllegalObjectEntityException {
		throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
				+ "' is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	@Override
	protected boolean isKeySupported(final String key) {
		switch (this.condition.getEntityCode().shortValue()) {
		case MEASUREMENT_CODE:
			return key == ActionWrapper.COLUMN_STATUS
					|| key == ActionWrapper.COLUMN_START_TIME
					|| key == StorableObjectWrapper.COLUMN_NAME;
		case TEST_CODE:
			return key == TestWrapper.COLUMN_START_TIME
					|| key == TestWrapper.COLUMN_END_TIME
					|| key == TestWrapper.COLUMN_STATUS
					|| key == StorableObjectWrapper.COLUMN_DESCRIPTION;
		case PERIODICALTEMPORALPATTERN_CODE:
			return key == PeriodicalTemporalPatternWrapper.COLUMN_PERIOD;
		case MEASUREMENTPORT_TYPE_CODE:
			return key == StorableObjectWrapper.COLUMN_CODENAME;
		case ACTIONTEMPLATE_CODE:
			return key == StorableObjectWrapper.COLUMN_DESCRIPTION;
		case MONITOREDELEMENT_CODE:
			return key == StorableObjectWrapper.COLUMN_NAME;
		default:
			return false;
		}
	}
}
