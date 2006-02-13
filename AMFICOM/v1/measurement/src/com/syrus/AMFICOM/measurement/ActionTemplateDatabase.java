/*-
 * $Id: ActionTemplateDatabase.java,v 1.1.2.2 2006/02/13 19:31:15 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.TableNames.ACT_PAR_TMPL_LINK;
import static com.syrus.AMFICOM.general.TableNames.ME_TMPL_LINK;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.measurement.ActionTemplateWrapper.LINK_COLUMN_ACTION_PARAMETER_ID;
import static com.syrus.AMFICOM.measurement.ActionTemplateWrapper.LINK_COLUMN_ACTION_TEMPLATE_ID;
import static com.syrus.AMFICOM.measurement.ActionTemplateWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID;

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

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/02/13 19:31:15 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ActionTemplateDatabase extends StorableObjectDatabase<ActionTemplate> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {
		return ObjectEntities.ACTIONTEMPLATE_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_DESCRIPTION;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final ActionTemplate storableObject) throws IllegalDataException {
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final ActionTemplate storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected ActionTemplate updateEntityFromResultSet(final ActionTemplate storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		final ActionTemplate actionTemplate = (storableObject == null)
				? new ActionTemplate(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						null,
						ILLEGAL_VERSION,
						null,
						null,
						null)
					: storableObject;
		actionTemplate.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)));
		return actionTemplate;
	}

	@Override
	protected Set<ActionTemplate> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<ActionTemplate> actionTemplates = super.retrieveByCondition(conditionQuery);

		this.retrieveLinkedIds(actionTemplates);

		return actionTemplates;
	}

	private void retrieveLinkedIds(final Set<ActionTemplate> actionTemplates) throws RetrieveObjectException {
		if (actionTemplates == null || actionTemplates.isEmpty()) {
			return;
		}

		final Map<Identifier, Set<Identifier>> apIdsMap = super.retrieveLinkedEntityIds(actionTemplates,
				ACT_PAR_TMPL_LINK,
				LINK_COLUMN_ACTION_TEMPLATE_ID,
				LINK_COLUMN_ACTION_PARAMETER_ID);
		final Map<Identifier, Set<Identifier>> meIdsMap = super.retrieveLinkedEntityIds(actionTemplates,
				ME_TMPL_LINK,
				LINK_COLUMN_ACTION_TEMPLATE_ID,
				LINK_COLUMN_MONITORED_ELEMENT_ID);
		for (final ActionTemplate actionTemplate : actionTemplates) {
			final Identifier actionTemplateId = actionTemplate.getId();

			final Set<Identifier> actionParameterIds = apIdsMap.get(actionTemplateId);
			actionTemplate.setActionParameterIds0(actionParameterIds);

			final Set<Identifier> monitoredElementIds = meIdsMap.get(actionTemplateId);
			actionTemplate.setMonitoredElementIds0(monitoredElementIds);
		}
	}

	@Override
	protected void insert(final Set<ActionTemplate> actionTemplates) throws IllegalDataException, CreateObjectException {
		super.insert(actionTemplates);

		final Map<Identifier, Set<Identifier>> actionParameterIdsMap = this.createActionParameterIdsMap(actionTemplates);
		super.insertLinkedEntityIds(actionParameterIdsMap,
				ACT_PAR_TMPL_LINK,
				LINK_COLUMN_ACTION_TEMPLATE_ID,
				LINK_COLUMN_ACTION_PARAMETER_ID);

		final Map<Identifier, Set<Identifier>> monitoredElementIdsMap = this.createMonitoredElementIdsMap(actionTemplates);
		super.insertLinkedEntityIds(monitoredElementIdsMap,
				ME_TMPL_LINK,
				LINK_COLUMN_ACTION_TEMPLATE_ID,
				LINK_COLUMN_MONITORED_ELEMENT_ID);
	}

	@Override
	protected void update(final Set<ActionTemplate> actionTemplates) throws UpdateObjectException {
		super.update(actionTemplates);

		final Map<Identifier, Set<Identifier>> actionParameterIdsMap = this.createActionParameterIdsMap(actionTemplates);
		super.updateLinkedEntityIds(actionParameterIdsMap,
				ACT_PAR_TMPL_LINK,
				LINK_COLUMN_ACTION_TEMPLATE_ID,
				LINK_COLUMN_ACTION_PARAMETER_ID);

		final Map<Identifier, Set<Identifier>> monitoredElementIdsMap = this.createMonitoredElementIdsMap(actionTemplates);
		super.updateLinkedEntityIds(monitoredElementIdsMap,
				ME_TMPL_LINK,
				LINK_COLUMN_ACTION_TEMPLATE_ID,
				LINK_COLUMN_MONITORED_ELEMENT_ID);
	}

	private Map<Identifier, Set<Identifier>> createActionParameterIdsMap(final Set<ActionTemplate> actionTemplates) {
		final Map<Identifier, Set<Identifier>> actionParameterIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final ActionTemplate actionTemplate : actionTemplates) {
			actionParameterIdsMap.put(actionTemplate.getId(), actionTemplate.getActionParameterIds());
		}
		return actionParameterIdsMap;
	}

	private Map<Identifier, Set<Identifier>> createMonitoredElementIdsMap(final Set<ActionTemplate> actionTemplates) {
		final Map<Identifier, Set<Identifier>> monitoredElementIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final ActionTemplate actionTemplate : actionTemplates) {
			monitoredElementIdsMap.put(actionTemplate.getId(), actionTemplate.getMonitoredElementIds());
		}
		return monitoredElementIdsMap;
	}
}
