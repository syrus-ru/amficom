package com.syrus.AMFICOM.corba.portable.reflect.common;

import com.syrus.AMFICOM.corba.portable.common.DatabaseAccessException;
import com.syrus.AMFICOM.server.prefs.JDBCConnectionManager;
import java.sql.SQLException;
import sqlj.runtime.ExecutionContext;
import sqlj.runtime.ref.DefaultContext;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 12:27:26 $
 * @author $Author: bass $
 */
public abstract class ObjectResourceUtilitiesImpl {
	protected ObjectResourceUtilitiesImpl() {
	}

	static {
		try {
			Class.forName(JDBCConnectionManager.class.getName());
		} catch (ClassNotFoundException cnfe) {
			;
		}
	}

	protected abstract String[] getIds(DefaultContext connCtx, ExecutionContext execCtx) throws SQLException;

	public String[] getIds() throws DatabaseAccessException {
		try {
			DefaultContext connCtx = DefaultContext.getDefaultContext();
			ExecutionContext execCtx = connCtx.getExecutionContext();
			return getIds(connCtx, execCtx);
		} catch (SQLException sqle) {
			throw new DatabaseAccessException();
		}
	}
}
