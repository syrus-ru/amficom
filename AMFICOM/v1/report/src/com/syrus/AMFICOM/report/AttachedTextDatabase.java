/*-
 * $Id: AttachedTextDatabase.java,v 1.4 2005/12/02 11:24:18 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import static com.syrus.AMFICOM.general.ObjectEntities.ATTACHEDTEXT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.report.AttachedTextWrapper.COLUMN_DISTANCE_X;
import static com.syrus.AMFICOM.report.AttachedTextWrapper.COLUMN_DISTANCE_Y;
import static com.syrus.AMFICOM.report.AttachedTextWrapper.COLUMN_FONT_NAME;
import static com.syrus.AMFICOM.report.AttachedTextWrapper.COLUMN_FONT_SIZE;
import static com.syrus.AMFICOM.report.AttachedTextWrapper.COLUMN_FONT_SYLE;
import static com.syrus.AMFICOM.report.AttachedTextWrapper.COLUMN_HORIZONTAL_ATTACHER_ID;
import static com.syrus.AMFICOM.report.AttachedTextWrapper.COLUMN_HORIZONTAL_ATTACH_TYPE;
import static com.syrus.AMFICOM.report.AttachedTextWrapper.COLUMN_TEXT;
import static com.syrus.AMFICOM.report.AttachedTextWrapper.COLUMN_VERTICAL_ATTACHER_ID;
import static com.syrus.AMFICOM.report.AttachedTextWrapper.COLUMN_VERTICAL_ATTACH_TYPE;
import static com.syrus.AMFICOM.report.AttachedTextWrapper.SIZE_FONT_NAME_COLUMN;
import static com.syrus.AMFICOM.report.StorableElementWrapper.COLUMN_LOCATION_X;
import static com.syrus.AMFICOM.report.StorableElementWrapper.COLUMN_LOCATION_Y;
import static com.syrus.AMFICOM.report.StorableElementWrapper.COLUMN_REPORT_TEMPLATE_ID;
import static com.syrus.AMFICOM.report.StorableElementWrapper.COLUMN_SIZE_HEIGHT;
import static com.syrus.AMFICOM.report.StorableElementWrapper.COLUMN_SIZE_WIDTH;

import java.awt.Font;
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
 * @version $Revision: 1.4 $, $Date: 2005/12/02 11:24:18 $
 * @module report
 */

public class AttachedTextDatabase extends StorableObjectDatabase<AttachedTextStorableElement> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected short getEntityCode() {
		return ATTACHEDTEXT_CODE;
	}
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_TEXT + COMMA
					+ COLUMN_FONT_NAME + COMMA
					+ COLUMN_FONT_SYLE + COMMA
					+ COLUMN_FONT_SIZE + COMMA
					+ COLUMN_HORIZONTAL_ATTACH_TYPE + COMMA
					+ COLUMN_VERTICAL_ATTACH_TYPE + COMMA
					+ COLUMN_DISTANCE_X + COMMA
					+ COLUMN_DISTANCE_Y + COMMA
					+ COLUMN_HORIZONTAL_ATTACHER_ID + COMMA
					+ COLUMN_VERTICAL_ATTACHER_ID + COMMA
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
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
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
	 * @throws IllegalDataException
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			AttachedTextStorableElement storableObject)
			throws IllegalDataException {
		Font font =  storableObject.getFont();
		return APOSTROPHE + storableObject.getText() + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(font.getName(), 
						SIZE_FONT_NAME_COLUMN) + APOSTROPHE + COMMA
				+ font.getStyle() + COMMA
				+ font.getSize() + COMMA
				+ storableObject.getHorizontalAttachType().ordinal() + COMMA
				+ storableObject.getVerticalAttachType().ordinal() + COMMA
				+ storableObject.getDistanceX() + COMMA
				+ storableObject.getDistanceY() + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getHorizontalAttacherId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getVerticalAttacherId()) + COMMA
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
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			AttachedTextStorableElement storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		Font font = storableObject.getFont();
		IntDimension size = storableObject.getSize();
		preparedStatement.setString(++startParameterNumber, storableObject.getText());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, font.getName(), SIZE_FONT_NAME_COLUMN);
		preparedStatement.setInt(++startParameterNumber, font.getStyle());
		preparedStatement.setInt(++startParameterNumber, font.getSize());
		preparedStatement.setInt(++startParameterNumber, storableObject.getHorizontalAttachType().ordinal());
		preparedStatement.setInt(++startParameterNumber, storableObject.getVerticalAttachType().ordinal());
		preparedStatement.setInt(++startParameterNumber, storableObject.getDistanceX());
		preparedStatement.setInt(++startParameterNumber, storableObject.getDistanceY());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getVerticalAttacherId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getHorizontalAttacherId());
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
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected AttachedTextStorableElement updateEntityFromResultSet(
			AttachedTextStorableElement storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final Date created = new Date();
		final AttachedTextStorableElement attachedText = (storableObject == null)
				? new AttachedTextStorableElement(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						created,
						created,
						null,
						null,
						ILLEGAL_VERSION,
						null,
						null,
						null,
						"",
						null,
						null,
						null,
						null,
						null,
						0,
						0)
				: storableObject;
		
		String fontName = resultSet.getString(COLUMN_FONT_NAME);
		int fontStyle = resultSet.getInt(COLUMN_FONT_SYLE);
		int fontSize = resultSet.getInt(COLUMN_FONT_SIZE);
		attachedText.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				resultSet.getInt(COLUMN_LOCATION_X),
				resultSet.getInt(COLUMN_LOCATION_Y),
				resultSet.getInt(COLUMN_SIZE_WIDTH),
				resultSet.getInt(COLUMN_SIZE_HEIGHT),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_REPORT_TEMPLATE_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_TEXT)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_VERTICAL_ATTACHER_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_HORIZONTAL_ATTACHER_ID),
				resultSet.getInt(COLUMN_DISTANCE_X),
				resultSet.getInt(COLUMN_DISTANCE_Y),
				new Font(fontName, fontStyle, fontSize),
				TextAttachingType.fromInt(resultSet.getInt(COLUMN_VERTICAL_ATTACH_TYPE)),
				TextAttachingType.fromInt(resultSet.getInt(COLUMN_HORIZONTAL_ATTACH_TYPE)));
		return attachedText;
	}
}
