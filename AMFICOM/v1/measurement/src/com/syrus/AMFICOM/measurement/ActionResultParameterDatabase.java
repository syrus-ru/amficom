/*-
 * $Id: ActionResultParameterDatabase.java,v 1.1.2.4 2006/03/15 15:50:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_TYPE_ID;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;

/**
 * @version $Revision: 1.1.2.4 $, $Date: 2006/03/15 15:50:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public abstract class ActionResultParameterDatabase<R extends ActionResultParameter<A>, A extends Action<R>> extends ParameterDatabase<R> {

	@Override
	protected String getColumnsTmplTmpl() {
		return COLUMN_TYPE_ID + COMMA
			+ this.getActionColumnName();
	}

	abstract String getActionColumnName();

	@Override
	protected String getUpdateMultipleSQLValuesTmplTmpl() {
		return QUESTION + COMMA
			+ QUESTION;

	}

	@Override
	protected String getUpdateSingleSQLValuesTmplTmpl(final R storableObject) throws IllegalDataException {
		final String sql = DatabaseIdentifier.toSQLString(storableObject.getTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getActionId());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmplTmpl(final R storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getActionId());
		return startParameterNumber;
	}
}
