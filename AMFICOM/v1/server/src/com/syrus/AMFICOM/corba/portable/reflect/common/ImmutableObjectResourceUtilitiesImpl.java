/*
 * $Id: ImmutableObjectResourceUtilitiesImpl.java,v 1.1.2.1 2004/10/12 13:12:59 bass Exp $
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
 * @version $Revision: 1.1.2.1 $, $Date: 2004/10/12 13:12:59 $
 * @module corbaportable_v1
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
			sqle.printStackTrace();
			throw ObjectResourceImpl.box(sqle);
		}
	}
	
	protected abstract String getName(String id, DefaultContext connCtx, ExecutionContext execCtx) throws SQLException;

	public String getName(String id) throws DatabaseAccessException {
		try {
			DefaultContext connCtx = DefaultContext.getDefaultContext();
			ExecutionContext execCtx = connCtx.getExecutionContext();
			return getName(id, connCtx, execCtx);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw ObjectResourceImpl.box(sqle);
		}
	}
}
