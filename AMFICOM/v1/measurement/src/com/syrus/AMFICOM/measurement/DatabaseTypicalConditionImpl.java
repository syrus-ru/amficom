/*-
 * $Id: DatabaseTypicalConditionImpl.java,v 1.28.2.4 2006/03/22 11:42:50 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONTEMPLATE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PERIODICALTEMPORALPATTERN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.measurement.KISWrapper.COLUMN_HOSTNAME;
import static com.syrus.AMFICOM.measurement.PeriodicalTemporalPatternWrapper.COLUMN_PERIOD;
import static com.syrus.AMFICOM.measurement.TestWrapper.COLUMN_END_TIME;
import static com.syrus.AMFICOM.measurement.TestWrapper.COLUMN_START_TIME;
import static com.syrus.AMFICOM.measurement.TestWrapper.COLUMN_STATUS;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.28.2.4 $, $Date: 2006/03/22 11:42:50 $
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
			case MEASUREMENT_TYPE_CODE:
			case ANALYSIS_TYPE_CODE:
			case MODELING_TYPE_CODE:
				return key == COLUMN_CODENAME;
			case MEASUREMENT_CODE:
				return key == ActionWrapper.COLUMN_STATUS
						|| key == ActionWrapper.COLUMN_START_TIME
						|| key == COLUMN_NAME;
			case TEST_CODE:
				return key == COLUMN_START_TIME
						|| key == COLUMN_END_TIME
						|| key == COLUMN_STATUS
						|| key == COLUMN_DESCRIPTION;
			case PERIODICALTEMPORALPATTERN_CODE:
				return key == COLUMN_PERIOD;
			case MEASUREMENTPORT_TYPE_CODE:
				return key == COLUMN_CODENAME;
			case ACTIONTEMPLATE_CODE:
				return key == COLUMN_DESCRIPTION;
			case KIS_CODE:
				return key == COLUMN_NAME
						|| key == COLUMN_HOSTNAME;
			case MONITOREDELEMENT_CODE:
				return key == COLUMN_NAME;
			default:
				return false;
		}
	}
}
