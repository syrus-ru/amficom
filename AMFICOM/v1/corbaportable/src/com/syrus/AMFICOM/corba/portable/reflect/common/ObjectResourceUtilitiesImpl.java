package com.syrus.AMFICOM.corba.portable.reflect.common;

import com.syrus.AMFICOM.corba.portable.common.DatabaseAccessException;
import com.syrus.AMFICOM.server.prefs.JdbcConnectionManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import sqlj.runtime.ExecutionContext;
import sqlj.runtime.ref.DefaultContext;

/**
 * @version $Revision: 1.2 $, $Date: 2004/09/14 12:51:40 $
 * @author $Author: bass $
 */
public abstract class ObjectResourceUtilitiesImpl {
	private static final DataSource DATA_SOURCE = JdbcConnectionManager.getDataSource();

	protected ObjectResourceUtilitiesImpl() {
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
