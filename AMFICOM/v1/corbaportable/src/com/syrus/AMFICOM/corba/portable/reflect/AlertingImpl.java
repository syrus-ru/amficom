/*
 * $Id: AlertingImpl.java,v 1.2 2004/09/25 18:06:32 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.reflect;

import com.syrus.AMFICOM.corba.portable.common.DatabaseAccessException;
import com.syrus.AMFICOM.corba.portable.reflect.common.ObjectResourceImpl;
import java.sql.Timestamp;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/09/25 18:06:32 $
 * @module corbaportable_v1
 */
final class AlertingImpl {
	/**
	 * <code>VARCHAR2(64)</code>, primary key.
	 *
	 * Is generated from a sequence and has the form
	 * &quot;<code>alrt&lt;sequence&nbsp;value&gt;</code>&quot;.
	 */
	private String id;

	/**
	 * <code>DATE(7)</code>, can be <code>null</code>.
	 */
	private Timestamp alerted;

	/*
	 * External references.
	 **************************************************************************/

	/**
	 * <code>VARCHAR2(64)</code>, foreign key.
	 *
	 * @see AlertingMessageUserLinkImpl#id
	 */
	private AlertingMessageUserLinkImpl alertingMessageUserId;

	/**
	 * <code>VARCHAR2(64)</code>, foreign key.
	 *
	 * @see EventImpl#id
	 */
	private EventImpl eventId;

	/*
	 * Internal fields.
	 */

	private static AlertingUtilities alertingUtilities;

	static {
		try {
			alertingUtilities
				= AlertingUtilitiesHelper
				.narrow(ObjectResourceImpl.getObject("AlertingUtilities"));
		} catch (Exception e) {
			/**
			 * @todo In the future, catch UserException and/or
			 *       InvocationTargetException separately.
			 *       In particular, when using JdbcConnection, a
			 *       UserException will be surely thrown.
			 */
			e.printStackTrace();
		}
	}

	AlertingImpl() {
	}

	public static synchronized void delete(String id) throws DatabaseAccessException {
		if ((id == null) || (id.length() == 0))
			return;
		alertingUtilities.delete(id);
	}

	public static String[] getMatchingIds(AlertingMessageUserLinkImpl alertingMessageUserId) throws DatabaseAccessException {
		if ((alertingMessageUserId == null) || (alertingMessageUserId.getId().length() == 0))
			return new String[0];
		return alertingUtilities.getMatchingIds(alertingMessageUserId.getId());
	}
}
