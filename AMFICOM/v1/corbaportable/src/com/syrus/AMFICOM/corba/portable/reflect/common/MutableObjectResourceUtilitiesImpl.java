/*
 * $Id: MutableObjectResourceUtilitiesImpl.java,v 1.2 2004/09/25 18:06:32 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.reflect.common;

import com.syrus.AMFICOM.corba.portable.common.DatabaseAccessException;
import java.sql.SQLException;
import sqlj.runtime.ExecutionContext;
import sqlj.runtime.ref.DefaultContext;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/09/25 18:06:32 $
 * @module corbaportable_v1
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
			sqle.printStackTrace();
			throw ObjectResourceImpl.box(sqle);
		}
	}
}
