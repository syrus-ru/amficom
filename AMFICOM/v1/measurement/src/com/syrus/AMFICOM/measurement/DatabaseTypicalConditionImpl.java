/*
* $Id: DatabaseTypicalConditionImpl.java,v 1.17 2005/08/29 14:56:46 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.TableNames;
import com.syrus.AMFICOM.general.TypicalCondition;


/**
 * @version $Revision: 1.17 $, $Date: 2005/08/29 14:56:46 $
 * @author $Author: arseniy $
 * @module measurement
 */
class DatabaseTypicalConditionImpl extends AbstractDatabaseTypicalCondition {

	@SuppressWarnings("unused")
	private DatabaseTypicalConditionImpl(final TypicalCondition typicalCondition) {
		super(typicalCondition);
	}

	@Override
	protected String getColumnName() throws IllegalObjectEntityException {
		/* check key support */
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.TEST_CODE:
				if (this.condition.getKey().equals(TestWrapper.COLUMN_START_TIME)) {
					return TestWrapper.COLUMN_START_TIME;
				}
				if (this.condition.getKey().equals(TestWrapper.COLUMN_END_TIME)) {
					return TestWrapper.COLUMN_END_TIME;
				}
				if (this.condition.getKey().equals(TestWrapper.COLUMN_STATUS)) {
					return TestWrapper.COLUMN_STATUS;
				}
				break;
			case ObjectEntities.PERIODICALTEMPORALPATTERN_CODE:
				if (this.condition.getKey().equals(PeriodicalTemporalPatternWrapper.COLUMN_PERIOD)) {
					return PeriodicalTemporalPatternWrapper.COLUMN_PERIOD;
				}
				break;
			case ObjectEntities.MEASUREMENTPORT_TYPE_CODE:
				if (this.condition.getKey().equals(MeasurementPortTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_CODE)) {
					return MeasurementPortTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID;
				}
				break;
			case ObjectEntities.MEASUREMENTSETUP_CODE:
				if (this.condition.getKey().equals(MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_TYPE_CODE)) {
					return MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID;
				}
				break;
			default:
				throw new IllegalObjectEntityException("Entity '" + ObjectEntities.codeToString(this.condition.getEntityCode())
						+ "' and key '" + this.condition.getKey()
						+ "' are not supported.", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
		return null;
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

}
