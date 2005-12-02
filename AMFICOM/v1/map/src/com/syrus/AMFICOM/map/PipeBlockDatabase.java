/*-
 * $Id: PipeBlockDatabase.java,v 1.2 2005/12/02 11:24:13 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.PIPEBLOCK_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.map.PipeBlockWrapper.COLUMN_DIMENSION_X;
import static com.syrus.AMFICOM.map.PipeBlockWrapper.COLUMN_DIMENSION_Y;
import static com.syrus.AMFICOM.map.PipeBlockWrapper.COLUMN_HORIZONTAL_VERTICAL;
import static com.syrus.AMFICOM.map.PipeBlockWrapper.COLUMN_LEFT_TO_RIGHT;
import static com.syrus.AMFICOM.map.PipeBlockWrapper.COLUMN_NUMBER;
import static com.syrus.AMFICOM.map.PipeBlockWrapper.COLUMN_TOP_TO_BOTTOM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.util.database.DatabaseDate;

/**
 * @author max
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/12/02 11:24:13 $
 * @module map
 */

public class PipeBlockDatabase extends StorableObjectDatabase<PipeBlock> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected short getEntityCode() {		
		return PIPEBLOCK_CODE;
	}
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_NUMBER + COMMA
				+ COLUMN_DIMENSION_X + COMMA
				+ COLUMN_DIMENSION_Y + COMMA
				+ COLUMN_LEFT_TO_RIGHT + COMMA
				+ COLUMN_TOP_TO_BOTTOM + COMMA
				+ COLUMN_HORIZONTAL_VERTICAL;
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
	protected int setEntityForPreparedStatementTmpl(final PipeBlock storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final IntDimension dimension = storableObject.getDimension();
		preparedStatement.setInt(++startParameterNumber, storableObject.getNumber());
		preparedStatement.setInt(++startParameterNumber, dimension.getWidth());
		preparedStatement.setInt(++startParameterNumber, dimension.getHeight());
		preparedStatement.setInt(++startParameterNumber, storableObject.isLeftToRight() ? 1 : 0);
		preparedStatement.setInt(++startParameterNumber, storableObject.isTopToBottom() ? 1 : 0);
		preparedStatement.setInt(++startParameterNumber, storableObject.isHorizontalVertical() ? 1 : 0);
		return startParameterNumber;
	}
	
	@Override
	protected String getUpdateSingleSQLValuesTmpl(final PipeBlock storableObject) 
	throws IllegalDataException {
		final IntDimension dimension = storableObject.getDimension();
		final String values = storableObject.getNumber() + COMMA
			+ dimension.getWidth() + COMMA
			+ dimension.getHeight() + COMMA
			+ (storableObject.isLeftToRight() ? "1" : "0") + COMMA
			+ (storableObject.isTopToBottom() ? "1" : "0") + COMMA
			+ (storableObject.isHorizontalVertical() ? "1" : "0");
		return values;
	}
	
	@Override
	protected PipeBlock updateEntityFromResultSet(final PipeBlock storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		final PipeBlock pipeBlock = (storableObject == null) ? new PipeBlock(DatabaseIdentifier.getIdentifier(resultSet,
				COLUMN_ID),
				null,
				StorableObjectVersion.ILLEGAL_VERSION,
				0,
				null,
				false,
				false,
				false) : storableObject;

		pipeBlock.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				resultSet.getInt(COLUMN_NUMBER),
				resultSet.getInt(COLUMN_DIMENSION_X),
				resultSet.getInt(COLUMN_DIMENSION_Y),
				resultSet.getInt(COLUMN_LEFT_TO_RIGHT) == 1 ? true : false,
				resultSet.getInt(COLUMN_TOP_TO_BOTTOM) == 1 ? true : false,
				resultSet.getInt(COLUMN_HORIZONTAL_VERTICAL) == 1 ? true : false);
		return pipeBlock;
	}
}
