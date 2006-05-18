/*-
* $Id: LayoutItemDatabase.java,v 1.4 2006/02/28 15:19:58 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resource;

import static com.syrus.AMFICOM.general.ObjectEntities.LAYOUT_ITEM_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.resource.LayoutItemWrapper.COLUMN_LAYOUT_NAME;
import static com.syrus.AMFICOM.resource.LayoutItemWrapper.COLUMN_PARENT_ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.4 $, $Date: 2006/02/28 15:19:58 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module resource
 */
public class LayoutItemDatabase extends StorableObjectDatabase<LayoutItem> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return LAYOUT_ITEM_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_PARENT_ID + COMMA
				+ COLUMN_LAYOUT_NAME + COMMA
				+ COLUMN_NAME;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final LayoutItem storableObject) throws IllegalDataException {
		final String sql = DatabaseIdentifier.toSQLString(storableObject.getParentId()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getLayoutName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE;
		return sql;
	}

	@Override
	protected LayoutItem updateEntityFromResultSet(final LayoutItem storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final LayoutItem layoutItem = (storableObject == null) ? new LayoutItem(DatabaseIdentifier.getIdentifier(resultSet,
				COLUMN_ID), null, StorableObjectVersion.ILLEGAL_VERSION, null, null, null) : storableObject;
		layoutItem.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_LAYOUT_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)));
		return layoutItem;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final LayoutItem layoutItem,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, 
			++startParameterNumber, 
			layoutItem.getParentId());
		DatabaseString.setString(preparedStatement, 
			++startParameterNumber, 
			layoutItem.getLayoutName(), 
			SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, 
			++startParameterNumber,
			layoutItem.getName(), 
			SIZE_NAME_COLUMN);
		return startParameterNumber;
	}
}

