
package com.syrus.AMFICOM.client.event;

import java.beans.PropertyChangeEvent;

public class ContextChangeEvent extends PropertyChangeEvent {

	public static final long	SESSION_OPENED_EVENT		= 0x00000001;
	public static final long	SESSION_CLOSED_EVENT		= 0x00000002;
	public static final long	SESSION_CHANGED_EVENT		= 0x00000004;
	public static final long	CONNECTION_OPENED_EVENT		= 0x00000008;
	public static final long	CONNECTION_CLOSED_EVENT		= 0x00000010;
	public static final long	CONNECTION_CHANGED_EVENT	= 0x00000020;
	public static final long	VIEW_CHANGED_EVENT			= 0x00000040;
	public static final long	PASSWORD_CHANGING_EVENT		= 0x00000080;
	public static final long	PASSWORD_CHANGED_EVENT		= 0x00000100;
	public static final long	SESSION_CHANGING_EVENT		= 0x00000200;
	public static final long	CONNECTION_CHANGING_EVENT	= 0x00000400;
	public static final long	CONNECTION_FAILED_EVENT		= 0x00000800;
	public static final long	DOMAIN_SELECTED_EVENT		= 0x00001000;

	public static final String	TYPE						= ContextChangeEvent.class.getName();
	
	private long type;

	public ContextChangeEvent(Object source, long type) {
		super(source, TYPE, null, null);
		this.type = type;
	}

	public boolean isConnectionChanged() {
		return (this.type & CONNECTION_CHANGED_EVENT) != 0;
	}

	public boolean isConnectionChanging() {
		return (this.type & CONNECTION_CHANGING_EVENT) != 0;
	}

	public boolean isConnectionClosed() {
		return (this.type & CONNECTION_CLOSED_EVENT) != 0;
	}

	public boolean isConnectionFailed() {
		return (this.type & CONNECTION_FAILED_EVENT) != 0;
	}

	public boolean isConnectionOpened() {
		return (this.type & CONNECTION_OPENED_EVENT) != 0;
	}

	public boolean isDomainSelected() {
		return (this.type & DOMAIN_SELECTED_EVENT) != 0;
	}

	public boolean isPasswordChanged() {
		return (this.type & PASSWORD_CHANGED_EVENT) != 0;
	}

	public boolean isPasswordChanging() {
		return (this.type & PASSWORD_CHANGING_EVENT) != 0;
	}

	public boolean isSessionChanged() {
		return (this.type & SESSION_CHANGED_EVENT) != 0;
	}

	public boolean isSessionChanging() {
		return (this.type & SESSION_CHANGING_EVENT) != 0;
	}

	public boolean isSessionClosed() {
		return (this.type & SESSION_CLOSED_EVENT) != 0;
	}

	public boolean isSessionOpened() {
		return (this.type & SESSION_OPENED_EVENT) != 0;
	}

	public boolean isViewChanged() {
		return (this.type & VIEW_CHANGED_EVENT) != 0;
	}

}
