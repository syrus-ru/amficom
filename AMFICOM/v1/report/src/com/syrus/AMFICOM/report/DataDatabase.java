/*-
 * $Id: DataDatabase.java,v 1.3 2005/12/02 11:24:18 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import static com.syrus.AMFICOM.general.ObjectEntities.REPORTDATA_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.report.DataWrapper.COLUMN_MODEL_CLASS_NAME;
import static com.syrus.AMFICOM.report.DataWrapper.COLUMN_REPORT_NAME;
import static com.syrus.AMFICOM.report.DataWrapper.SIZE_MODEL_CLASS_NAME_COLUMN;
import static com.syrus.AMFICOM.report.DataWrapper.SIZE_REPORT_NAME_COLUMN;
import static com.syrus.AMFICOM.report.StorableElementWrapper.COLUMN_LOCATION_X;
import static com.syrus.AMFICOM.report.StorableElementWrapper.COLUMN_LOCATION_Y;
import static com.syrus.AMFICOM.report.StorableElementWrapper.COLUMN_REPORT_TEMPLATE_ID;
import static com.syrus.AMFICOM.report.StorableElementWrapper.COLUMN_SIZE_HEIGHT;
import static com.syrus.AMFICOM.report.StorableElementWrapper.COLUMN_SIZE_WIDTH;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Maxim Selivanov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/12/02 11:24:18 $
 * @module report
 */

public class DataDatabase extends StorableObjectDatabase<DataStorableElement> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected short getEntityCode() {
		return REPORTDATA_CODE;
	}
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_REPORT_NAME + COMMA
					+ COLUMN_MODEL_CLASS_NAME + COMMA 
					+ COLUMN_LOCATION_X + COMMA
					+ COLUMN_LOCATION_Y + COMMA
					+ COLUMN_SIZE_WIDTH + COMMA
					+ COLUMN_SIZE_HEIGHT + COMMA
					+ COLUMN_REPORT_TEMPLATE_ID;
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
					+ QUESTION + COMMA
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	/**
	 * @param storableObject
	 * @return sqlQuery
	 * @throws IllegalDataException
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(DataStorableElement storableObject) 
	throws IllegalDataException {
		return APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getReportName(), 
						SIZE_REPORT_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getModelClassName(), 
						SIZE_MODEL_CLASS_NAME_COLUMN) + APOSTROPHE + COMMA
				+ storableObject.getLocation().x + COMMA
				+ storableObject.getLocation().y + COMMA
				+ storableObject.getSize().getHeight() + COMMA
				+ storableObject.getSize().getHeight() + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getReportTemplateId());
	}
	
	/**
	 * @param storableObject
	 * @param preparedStatement
	 * @param startParameterNumber
	 * @return startParameterNumber
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			DataStorableElement storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		IntDimension size = storableObject.getSize();
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getReportName(), SIZE_REPORT_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getModelClassName(), SIZE_MODEL_CLASS_NAME_COLUMN);
		preparedStatement.setInt(++startParameterNumber, storableObject.getLocation().x);
		preparedStatement.setInt(++startParameterNumber, storableObject.getLocation().y);
		preparedStatement.setInt(++startParameterNumber, size.getWidth());
		preparedStatement.setInt(++startParameterNumber, size.getHeight());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getReportTemplateId());
		return startParameterNumber;
	}
	
	/**
	 * @param storableObject
	 * @param resultSet
	 * @return dataStorableElement
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected DataStorableElement updateEntityFromResultSet(
			DataStorableElement storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final Date created = new Date();
		final DataStorableElement reportData = (storableObject == null)
				? new DataStorableElement(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						created,
						created,
						null,
						null,
						ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null)
				: storableObject;
		
		reportData.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				resultSet.getInt(COLUMN_LOCATION_X),
				resultSet.getInt(COLUMN_LOCATION_Y),
				resultSet.getInt(COLUMN_SIZE_WIDTH),
				resultSet.getInt(COLUMN_SIZE_HEIGHT),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_REPORT_TEMPLATE_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_REPORT_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_MODEL_CLASS_NAME)));
		return reportData;
	}
	
}
