/*
 * $Id: AlertingMessageHolder.java,v 1.1 2004/06/22 09:57:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.process;

import com.syrus.AMFICOM.corba.portable.alarm.Message;
import com.syrus.AMFICOM.corba.portable.common.Identifier;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 09:57:10 $
 * @author $Author: bass $
 * @module serverprocess
 */
final class AlertingMessageHolder {
	private Message message;

	private Identifier alertingTypeId;

	private Identifier userId;

	AlertingMessageHolder(Message message, Identifier alertingTypeId, Identifier userId) {
		this.message = message;
		this.alertingTypeId = alertingTypeId;
		this.userId = userId;
	}

	/**
	 * Getter for property message.
	 * @return Value of property message.
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * Getter for property alertingTypeId.
	 * @return Value of property alertingTypeId.
	 */
	public Identifier getAlertingTypeId() {
		return alertingTypeId;
	}

	/**
	 * Getter for property userId.
	 * @return Value of property userId.
	 */
	public Identifier getUserId() {
		return userId;
	}
}
