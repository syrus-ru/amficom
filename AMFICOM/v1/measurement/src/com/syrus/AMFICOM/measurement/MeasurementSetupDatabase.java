package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.general.TableNames.MS_ME_LINK;
import static com.syrus.AMFICOM.measurement.MeasurementSetupWrapper.COLUMN_ANALYSIS_TEMPLATE_ID;
import static com.syrus.AMFICOM.measurement.MeasurementSetupWrapper.COLUMN_ETALON_TEMPLATE_ID;
import static com.syrus.AMFICOM.measurement.MeasurementSetupWrapper.COLUMN_MEASUREMENT_TEMPLATE_ID;
import static com.syrus.AMFICOM.measurement.MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID;
import static com.syrus.AMFICOM.measurement.MeasurementSetupWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

public final class MeasurementSetupDatabase extends StorableObjectDatabase<MeasurementSetup> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {
		return ObjectEntities.MEASUREMENTSETUP_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_MEASUREMENT_TEMPLATE_ID + COMMA
					+ COLUMN_ANALYSIS_TEMPLATE_ID + COMMA
					+ COLUMN_ETALON_TEMPLATE_ID + COMMA
					+ COLUMN_DESCRIPTION;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final MeasurementSetup storableObject) throws IllegalDataException {
		final String sql = DatabaseIdentifier.toSQLString(storableObject.getMeasurementTemplateId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getAnalysisTemplateId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getEtalonTemplateId()) + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final MeasurementSetup storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMeasurementTemplateId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getAnalysisTemplateId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getEtalonTemplateId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected MeasurementSetup updateEntityFromResultSet(final MeasurementSetup storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		final MeasurementSetup measurementSetup = (storableObject == null)
				? new MeasurementSetup(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						null,
						ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null)
					: storableObject;
				measurementSetup.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
						DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
						DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
						DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
						StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
						DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MEASUREMENT_TEMPLATE_ID),
						DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ANALYSIS_TEMPLATE_ID),
						DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ETALON_TEMPLATE_ID),
						DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)));
		return measurementSetup;
	}

	@Override
	protected Set<MeasurementSetup> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<MeasurementSetup> measurementSetups = super.retrieveByCondition(conditionQuery);

		this.retrieveLinkedIds(measurementSetups);

		return measurementSetups;
	}

	private void retrieveLinkedIds(final Set<MeasurementSetup> measurementSetups) throws RetrieveObjectException {
		if (measurementSetups == null || measurementSetups.isEmpty()) {
			return;
		}

		final Map<Identifier, Set<Identifier>> monitoredElementIdsMap = super.retrieveLinkedEntityIds(measurementSetups,
				MS_ME_LINK,
				LINK_COLUMN_MEASUREMENT_SETUP_ID,
				LINK_COLUMN_MONITORED_ELEMENT_ID);

		for (final MeasurementSetup measurementSetup : measurementSetups) {
			final Identifier measurementSetupId = measurementSetup.getId();

			final Set<Identifier> monitoredElementIds = monitoredElementIdsMap.get(measurementSetupId);
			measurementSetup.setMonitoredElementIds0(monitoredElementIds);
		}
	}

	@Override
	protected void insert(final Set<MeasurementSetup> actionTemplates) throws IllegalDataException, CreateObjectException {
		super.insert(actionTemplates);

		final Map<Identifier, Set<Identifier>> monitoredElementIdsMap = this.createMonitoredElementIdsMap(actionTemplates);
		super.insertLinkedEntityIds(monitoredElementIdsMap,
				MS_ME_LINK,
				LINK_COLUMN_MEASUREMENT_SETUP_ID,
				LINK_COLUMN_MONITORED_ELEMENT_ID);
	}

	@Override
	protected void update(final Set<MeasurementSetup> measurementSetups) throws UpdateObjectException {
		super.update(measurementSetups);

		final Map<Identifier, Set<Identifier>> monitoredElementIdsMap = this.createMonitoredElementIdsMap(measurementSetups);
		super.updateLinkedEntityIds(monitoredElementIdsMap,
				MS_ME_LINK,
				LINK_COLUMN_MEASUREMENT_SETUP_ID,
				LINK_COLUMN_MONITORED_ELEMENT_ID);
	}

	private Map<Identifier, Set<Identifier>> createMonitoredElementIdsMap(final Set<MeasurementSetup> measurementSetups) {
		final Map<Identifier, Set<Identifier>> monitoredElementIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final MeasurementSetup measurementSetup : measurementSetups) {
			monitoredElementIdsMap.put(measurementSetup.getId(), measurementSetup.getMonitoredElementIds());
		}
		return monitoredElementIdsMap;
	}
}
