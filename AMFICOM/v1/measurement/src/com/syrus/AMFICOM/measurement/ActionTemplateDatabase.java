/*-
 * $Id: ActionTemplateDatabase.java,v 1.1.2.7 2006/04/12 13:02:20 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ObjectEntities.ACTIONTEMPLATE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.general.TableNames.ACTMPL_ME_LINK;
import static com.syrus.AMFICOM.general.TableNames.ACTMPL_PAR_LINK;
import static com.syrus.AMFICOM.measurement.ActionTemplateWrapper.COLUMN_APPROXIMATE_ACTION_DURATION;
import static com.syrus.AMFICOM.measurement.ActionTemplateWrapper.COLUMN_MEASUREMENT_PORT_TYPE_ID;
import static com.syrus.AMFICOM.measurement.ActionTemplateWrapper.LINK_COLUMN_ACTION_PARAMETER_ID;
import static com.syrus.AMFICOM.measurement.ActionTemplateWrapper.LINK_COLUMN_ACTION_TEMPLATE_ID;
import static com.syrus.AMFICOM.measurement.ActionTemplateWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.measurement.ActionTypeDatabase.ActionTypeKindDatabase;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.1.2.7 $, $Date: 2006/04/12 13:02:20 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionTemplateDatabase extends StorableObjectDatabase<ActionTemplate<Action>> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {
		return ACTIONTEMPLATE_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = ActionTypeKindDatabase.getInstance().getColumns() + COMMA
					+ COLUMN_MEASUREMENT_PORT_TYPE_ID + COMMA
					+ COLUMN_DESCRIPTION + COMMA
					+ COLUMN_APPROXIMATE_ACTION_DURATION;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = ActionTypeKindDatabase.getInstance().getUpdateMultipleSQLValues() + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final ActionTemplate<Action> storableObject) throws IllegalDataException {
		final String sql = ActionTypeKindDatabase.getInstance().getUpdateSingleSQLValues(storableObject.getActionTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getMeasurementPortTypeId()) + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ Long.toString(storableObject.getApproximateActionDuration());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final ActionTemplate<Action> storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		startParameterNumber = ActionTypeKindDatabase.getInstance().setEntityForPreparedStatement(storableObject.getActionTypeId(),
				preparedStatement,
				startParameterNumber);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMeasurementPortTypeId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setLong(++startParameterNumber, storableObject.getApproximateActionDuration());
		return startParameterNumber;
	}

	@Override
	protected ActionTemplate<Action> updateEntityFromResultSet(final ActionTemplate<Action> storableObject,
			final ResultSet resultSet) throws IllegalDataException, RetrieveObjectException, SQLException {
		final ActionTemplate<Action> actionTemplate = (storableObject == null)
				? new ActionTemplate<Action>(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						null,
						ILLEGAL_VERSION,
						null,
						null,
						null,
						0,
						null,
						null)
					: storableObject;

		final Identifier actionTypeId = ActionTypeKindDatabase.getInstance().getActionTypeId(resultSet);

		actionTemplate.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				actionTypeId,
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MEASUREMENT_PORT_TYPE_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				resultSet.getLong(COLUMN_APPROXIMATE_ACTION_DURATION));
		return actionTemplate;
	}

	@Override
	protected Set<ActionTemplate<Action>> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<ActionTemplate<Action>> actionTemplates = super.retrieveByCondition(conditionQuery);

		this.retrieveLinkedIds(actionTemplates);

		return actionTemplates;
	}

	private void retrieveLinkedIds(final Set<ActionTemplate<Action>> actionTemplates) throws RetrieveObjectException {
		if (actionTemplates == null || actionTemplates.isEmpty()) {
			return;
		}

		final Map<Identifier, Set<Identifier>> apIdsMap = super.retrieveLinkedEntityIds(actionTemplates,
				ACTMPL_PAR_LINK,
				LINK_COLUMN_ACTION_TEMPLATE_ID,
				LINK_COLUMN_ACTION_PARAMETER_ID);
		final Map<Identifier, Set<Identifier>> meIdsMap = super.retrieveLinkedEntityIds(actionTemplates,
				ACTMPL_ME_LINK,
				LINK_COLUMN_ACTION_TEMPLATE_ID,
				LINK_COLUMN_MONITORED_ELEMENT_ID);
		for (final ActionTemplate<Action> actionTemplate : actionTemplates) {
			final Identifier actionTemplateId = actionTemplate.getId();

			final Set<Identifier> actionParameterIds = apIdsMap.get(actionTemplateId);
			actionTemplate.setActionParameterIds0(actionParameterIds);

			final Set<Identifier> monitoredElementIds = meIdsMap.get(actionTemplateId);
			actionTemplate.setMonitoredElementIds0(monitoredElementIds);
		}
	}

	@Override
	protected void insert(final Set<ActionTemplate<Action>> actionTemplates) throws IllegalDataException, CreateObjectException {
		super.insert(actionTemplates);

		final Map<Identifier, Set<Identifier>> actionParameterIdsMap = StorableObject.createValuesMap(actionTemplates, LINK_COLUMN_ACTION_PARAMETER_ID);
		super.insertLinkedEntityIds(actionParameterIdsMap,
				ACTMPL_PAR_LINK,
				LINK_COLUMN_ACTION_TEMPLATE_ID,
				LINK_COLUMN_ACTION_PARAMETER_ID);

		final Map<Identifier, Set<Identifier>> monitoredElementIdsMap = StorableObject.createValuesMap(actionTemplates, LINK_COLUMN_MONITORED_ELEMENT_ID);
		super.insertLinkedEntityIds(monitoredElementIdsMap,
				ACTMPL_ME_LINK,
				LINK_COLUMN_ACTION_TEMPLATE_ID,
				LINK_COLUMN_MONITORED_ELEMENT_ID);
	}

	@Override
	protected void update(final Set<ActionTemplate<Action>> actionTemplates) throws UpdateObjectException {
		super.update(actionTemplates);

		final Map<Identifier, Set<Identifier>> actionParameterIdsMap = StorableObject.createValuesMap(actionTemplates, LINK_COLUMN_ACTION_PARAMETER_ID);
		super.updateLinkedEntityIds(actionParameterIdsMap,
				ACTMPL_PAR_LINK,
				LINK_COLUMN_ACTION_TEMPLATE_ID,
				LINK_COLUMN_ACTION_PARAMETER_ID);

		final Map<Identifier, Set<Identifier>> monitoredElementIdsMap = StorableObject.createValuesMap(actionTemplates, LINK_COLUMN_MONITORED_ELEMENT_ID);
		super.updateLinkedEntityIds(monitoredElementIdsMap,
				ACTMPL_ME_LINK,
				LINK_COLUMN_ACTION_TEMPLATE_ID,
				LINK_COLUMN_MONITORED_ELEMENT_ID);
	}
}
