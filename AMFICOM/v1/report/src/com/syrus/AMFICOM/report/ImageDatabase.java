/*-
 * $Id: ImageDatabase.java,v 1.5 2005/12/02 11:24:18 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import static com.syrus.AMFICOM.general.ObjectEntities.REPORTIMAGE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.report.ImageWrapper.COLUMN_BITMAP_IMAGE_RESOURCE_ID;
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
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.util.database.DatabaseDate;

/**
 * @author Maxim Selivanov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/12/02 11:24:18 $
 * @module report
 */

public class ImageDatabase extends StorableObjectDatabase<ImageStorableElement> {
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected short getEntityCode() {
		return REPORTIMAGE_CODE;
	}
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_BITMAP_IMAGE_RESOURCE_ID + COMMA
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
	protected String getUpdateSingleSQLValuesTmpl(ImageStorableElement storableObject) 
	throws IllegalDataException {
		return DatabaseIdentifier.toSQLString(storableObject.getBitmapImageResourceId()) + COMMA
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
			ImageStorableElement storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		IntDimension size = storableObject.getSize();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getBitmapImageResourceId());
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
	 * @throws SQLException
	 */
	@Override
	protected ImageStorableElement updateEntityFromResultSet(
			ImageStorableElement storableObject, ResultSet resultSet)
			throws SQLException {
		final Date created = new Date();
		final ImageStorableElement reportImage = (storableObject == null)
				? new ImageStorableElement(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						created,
						created,
						null,
						null,
						ILLEGAL_VERSION,
						null,
						null,
						null,
						Identifier.VOID_IDENTIFIER)
				: storableObject;
		
		reportImage.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				resultSet.getInt(COLUMN_LOCATION_X),
				resultSet.getInt(COLUMN_LOCATION_Y),
				resultSet.getInt(COLUMN_SIZE_WIDTH),
				resultSet.getInt(COLUMN_SIZE_HEIGHT),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_REPORT_TEMPLATE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_BITMAP_IMAGE_RESOURCE_ID));
		return reportImage;
	}
}
