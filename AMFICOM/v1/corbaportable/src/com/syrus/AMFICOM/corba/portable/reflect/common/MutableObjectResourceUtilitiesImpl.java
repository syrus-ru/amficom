package com.syrus.AMFICOM.corba.portable.reflect.common;

import com.syrus.AMFICOM.corba.portable.common.DatabaseAccessException;
import java.sql.SQLException;
import sqlj.runtime.ExecutionContext;
import sqlj.runtime.ref.DefaultContext;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 12:27:26 $
 * @author $Author: bass $
 */
public abstract class MutableObjectResourceUtilitiesImpl extends ObjectResourceUtilitiesImpl {
	protected MutableObjectResourceUtilitiesImpl() {
	}

	/**
	 * Returns a new universally unique identifier using the specified
	 * connection and execution contexts. This method is thread-safe.
	 * 
	 * @return a new universally unique identifier which can be used to
	 *         construct an <code>MutableObjectResourceImpl</code> object.
	 */
	protected abstract String getUUID(DefaultContext connCtx, ExecutionContext execCtx) throws SQLException;

	/**
	 * Returns a new universally unique identifier using the default connection
	 * and execution contexts. This method is thread-safe.
	 * 
	 * @return a new universally unique identifier which can be used to
	 *         construct an <code>MutableObjectResourceImpl</code> object.
	 */
	public String getUUID() throws DatabaseAccessException {
		try {
			DefaultContext connCtx = DefaultContext.getDefaultContext();
			ExecutionContext execCtx = connCtx.getExecutionContext();
			return getUUID(connCtx, execCtx);
		} catch (SQLException sqle) {
			throw new DatabaseAccessException();
		}
	}
}
