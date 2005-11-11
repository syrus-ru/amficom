/*-
 * $Id: DatabaseTypicalConditionImpl.java,v 1.25 2005/11/11 09:15:17 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTSETUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PERIODICALTEMPORALPATTERN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TableNames;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.25 $, $Date: 2005/11/11 09:15:17 $
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
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.MEASUREMENTPORT_TYPE_CODE:
				return MeasurementPortTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID;
			case ObjectEntities.MEASUREMENTSETUP_CODE:
				return MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID;
			default:
				throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
						+ "' is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}
	
	@Override
	protected String getLinkedColumnName() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.MEASUREMENTPORT_TYPE_CODE:
				return MeasurementPortTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_CODE;
			case ObjectEntities.MEASUREMENTSETUP_CODE:
				return MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_TYPE_CODE;
			default:
				throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
						+ "' is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

	@Override
	protected String getLinkedTableName() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.MEASUREMENTPORT_TYPE_CODE:
				return TableNames.MNTPORTTYPMNTTYPLINK;
			case ObjectEntities.MEASUREMENTSETUP_CODE:
				return TableNames.MEASUREMENTSETUP_MT_LINK;
			default:
				throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
						+ "' is not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

	@Override
	protected boolean isKeySupported(final String key) {
		switch (this.condition.getEntityCode().shortValue()) {
		case MEASUREMENT_CODE:
			return key == MeasurementWrapper.COLUMN_STATUS
					|| key == MeasurementWrapper.COLUMN_START_TIME;
		case TEST_CODE:
			return key == TestWrapper.COLUMN_START_TIME
					|| key == TestWrapper.COLUMN_END_TIME
					|| key == TestWrapper.COLUMN_STATUS;
		case PERIODICALTEMPORALPATTERN_CODE:
			return key == PeriodicalTemporalPatternWrapper.COLUMN_PERIOD;
		case MEASUREMENTPORT_TYPE_CODE:
			return key == StorableObjectWrapper.COLUMN_CODENAME
						|| key == MeasurementPortTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_CODE;
		case MEASUREMENTSETUP_CODE:
			return key == MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_TYPE_CODE;
		default:
			return false;
		}
	}
}
