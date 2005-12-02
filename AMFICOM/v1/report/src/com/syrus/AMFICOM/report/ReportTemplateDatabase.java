/*-
 * $Id: ReportTemplateDatabase.java,v 1.3 2005/12/02 11:24:18 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import static com.syrus.AMFICOM.general.ObjectEntities.REPORTTEMPLATE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.report.ReportTemplateWrapper.COLUMN_DESTINATION_MODULE;
import static com.syrus.AMFICOM.report.ReportTemplateWrapper.COLUMN_MARGIN_SIZE;
import static com.syrus.AMFICOM.report.ReportTemplateWrapper.COLUMN_ORIENTATION;
import static com.syrus.AMFICOM.report.ReportTemplateWrapper.COLUMN_SHEET_SIZE;
import static com.syrus.AMFICOM.report.ReportTemplateWrapper.SIZE_DESTINATION_MODULE_COLUMN;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.report.ReportTemplate.Orientation;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Maxim Selivanov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/12/02 11:24:18 $
 * @module report
 */
public class ReportTemplateDatabase extends StorableObjectDatabase<ReportTemplate> {

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {
		return REPORTTEMPLATE_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_NAME + COMMA
					+ COLUMN_DESCRIPTION + COMMA
					+ COLUMN_SHEET_SIZE + COMMA
					+ COLUMN_ORIENTATION + COMMA
					+ COLUMN_MARGIN_SIZE + COMMA
					+ COLUMN_DESTINATION_MODULE;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(ReportTemplate storableObject) 
	throws IllegalDataException {
		return APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ storableObject.getSheetSize().ordinal() + COMMA
				+ storableObject.getOrientation().ordinal() + COMMA
				+ storableObject.getMarginSize() + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDestinationModule(), SIZE_DESTINATION_MODULE_COLUMN) + APOSTROPHE;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(ReportTemplate storableObject, 
			PreparedStatement preparedStatement, 
			int startParameterNumber) throws IllegalDataException, 
			SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setInt(++startParameterNumber, storableObject.getSheetSize().ordinal());
		preparedStatement.setInt(++startParameterNumber, storableObject.getOrientation().ordinal());
		preparedStatement.setInt(++startParameterNumber, storableObject.getMarginSize());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDestinationModule(), SIZE_DESTINATION_MODULE_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected ReportTemplate updateEntityFromResultSet(ReportTemplate storableObject, 
			ResultSet resultSet) throws IllegalDataException, 
			RetrieveObjectException, SQLException {
		final Date created = new Date();
		final ReportTemplate reportTemplate = (storableObject == null)
				? new ReportTemplate(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						created,
						created,
						null,
						null,
						ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						0,
						null)
				: storableObject;

		reportTemplate.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				SheetSize.values()[resultSet.getInt(COLUMN_SHEET_SIZE)],
				Orientation.values()[resultSet.getInt(COLUMN_ORIENTATION)],
				resultSet.getInt(COLUMN_MARGIN_SIZE),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESTINATION_MODULE)));
		return reportTemplate;
	}

}
