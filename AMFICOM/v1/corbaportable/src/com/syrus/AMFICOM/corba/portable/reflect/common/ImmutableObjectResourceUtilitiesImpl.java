package com.syrus.AMFICOM.corba.portable.reflect.common;

import com.syrus.AMFICOM.corba.portable.common.DatabaseAccessException;
import java.sql.SQLException;
import sqlj.runtime.ExecutionContext;
import sqlj.runtime.ref.DefaultContext;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 12:27:26 $
 * @author $Author: bass $
 */
public abstract class ImmutableObjectResourceUtilitiesImpl extends ObjectResourceUtilitiesImpl {
	protected ImmutableObjectResourceUtilitiesImpl() {
	}

	protected abstract String getCodename(String id, DefaultContext connCtx, ExecutionContext execCtx) throws SQLException;

	public String getCodename(String id) throws DatabaseAccessException {
		try {
			DefaultContext connCtx = DefaultContext.getDefaultContext();
			ExecutionContext execCtx = connCtx.getExecutionContext();
			return getCodename(id, connCtx, execCtx);
		} catch (SQLException sqle) {
			throw new DatabaseAccessException();
		}
	}
	
	protected abstract String getName(String id, DefaultContext connCtx, ExecutionContext execCtx) throws SQLException;

	public String getName(String id) throws DatabaseAccessException {
		try {
			DefaultContext connCtx = DefaultContext.getDefaultContext();
			ExecutionContext execCtx = connCtx.getExecutionContext();
			return getName(id, connCtx, execCtx);
		} catch (SQLException sqle) {
			throw new DatabaseAccessException();
		}
	}
}
