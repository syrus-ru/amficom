/*
 * $Id: ObjectResourceUtilitiesImpl.java,v 1.1.2.1 2004/10/12 13:12:59 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.reflect.common;

import com.syrus.AMFICOM.corba.portable.common.DatabaseAccessException;
import com.syrus.AMFICOM.server.prefs.JdbcConnectionManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import sqlj.runtime.ExecutionContext;
import sqlj.runtime.ref.DefaultContext;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1.2.1 $, $Date: 2004/10/12 13:12:59 $
 * @module corbaportable_v1
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
			sqle.printStackTrace();
			throw ObjectResourceImpl.box(sqle);
		}
	}
}
