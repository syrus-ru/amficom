package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_TYPE_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.measurement.ActionResultParameterWrapper.COLUMN_VALUE;
import static com.syrus.AMFICOM.measurement.AnalysisResultParameterWrapper.COLUMN_ANALYSIS_ID;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseDate;

public final class AnalysisResultParameterDatabase extends ActionResultParameterDatabase<AnalysisResultParameter> {

	@Override
	protected short getEntityCode() {
		return ObjectEntities.ANALYSISRESULTPARAMETER_CODE;
	}

	@Override
	String getActionColumnName() {
		return COLUMN_ANALYSIS_ID;
	}

	@Override
	protected AnalysisResultParameter updateEntityFromResultSet(final AnalysisResultParameter storableObject,
			final ResultSet resultSet) throws IllegalDataException, RetrieveObjectException, SQLException {
		final AnalysisResultParameter analysisResultParameter = (storableObject == null)
				? new AnalysisResultParameter(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null)
					: storableObject;
		analysisResultParameter.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ANALYSIS_ID),
				ByteArrayDatabase.toByteArray(resultSet.getBlob(COLUMN_VALUE)));
		return analysisResultParameter;
	}

}
