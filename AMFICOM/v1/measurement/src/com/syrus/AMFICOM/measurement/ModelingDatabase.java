/*
 * $Id: ModelingDatabase.java,v 1.55.2.4 2006/03/01 15:43:14 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;


import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_TYPE_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_ACTION_TEMPLATE_ID;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_DURATION;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_MONITORED_ELEMENT_ID;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_START_TIME;
import static com.syrus.AMFICOM.measurement.ActionWrapper.COLUMN_STATUS;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.Action.ActionStatus;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.55.2.4 $, $Date: 2006/03/01 15:43:14 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class ModelingDatabase extends ActionDatabase<ModelingResultParameter, Modeling> {

	@Override
	protected short getEntityCode() {
		return MODELING_CODE;
	}

	@Override
	protected Modeling updateEntityFromResultSet(final Modeling storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		final Modeling modeling = (storableObject == null)
				? new Modeling(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null,
						0,
						null)
					: storableObject;

		modeling.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MONITORED_ELEMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ACTION_TEMPLATE_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_START_TIME),
				resultSet.getLong(COLUMN_DURATION),
				ActionStatus.valueOf(resultSet.getInt(COLUMN_STATUS)));
		return modeling;
	}

}
