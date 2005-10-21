
package com.syrus.AMFICOM.client.event;

import java.beans.PropertyChangeEvent;

public final class ContextChangeEvent extends PropertyChangeEvent {

	private static final long serialVersionUID = 3762253058452566068L;


	@Deprecated
	public static final long	SESSION_OPENED_EVENT		= 0x00000001;
	@Deprecated
	public static final long	SESSION_CLOSED_EVENT		= 0x00000002;
	@Deprecated
	public static final long	SESSION_CHANGED_EVENT		= 0x00000004;
	@Deprecated
	public static final long	CONNECTION_OPENED_EVENT		= 0x00000008;
	@Deprecated
	public static final long	CONNECTION_CLOSED_EVENT		= 0x00000010;
	@Deprecated
	public static final long	CONNECTION_CHANGED_EVENT	= 0x00000020;
	@Deprecated
	public static final long	VIEW_CHANGED_EVENT			= 0x00000040;
	@Deprecated
	public static final long	PASSWORD_CHANGING_EVENT		= 0x00000080;
	@Deprecated
	public static final long	PASSWORD_CHANGED_EVENT		= 0x00000100;
	@Deprecated
	public static final long	SESSION_CHANGING_EVENT		= 0x00000200;
	@Deprecated
	public static final long	CONNECTION_CHANGING_EVENT	= 0x00000400;
	@Deprecated
	public static final long	CONNECTION_FAILED_EVENT		= 0x00000800;
	@Deprecated
	public static final long	DOMAIN_SELECTED_EVENT		= 0x00001000;

	public static final long	LOGGED_IN_EVENT				= SESSION_OPENED_EVENT;
	public static final long	LOGGED_OUT_EVENT			= SESSION_CLOSED_EVENT;
	
	public static final String	TYPE						= ContextChangeEvent.class.getName();
	
	private long type;

	public ContextChangeEvent(final Object source, final long type) {
		super(source, TYPE, null, null);
		this.type = type;
	}

	@Deprecated
	public boolean isConnectionChanged() {
		return (this.type & CONNECTION_CHANGED_EVENT) != 0;
	}

	@Deprecated
	public boolean isConnectionChanging() {
		return (this.type & CONNECTION_CHANGING_EVENT) != 0;
	}

	@Deprecated
	public boolean isConnectionClosed() {
		return (this.type & CONNECTION_CLOSED_EVENT) != 0;
	}

	@Deprecated
	public boolean isConnectionFailed() {
		return (this.type & CONNECTION_FAILED_EVENT) != 0;
	}

	@Deprecated
	public boolean isConnectionOpened() {
		return (this.type & CONNECTION_OPENED_EVENT) != 0;
	}

	@Deprecated
	public boolean isDomainSelected() {
		return (this.type & DOMAIN_SELECTED_EVENT) != 0;
	}

	@Deprecated
	public boolean isPasswordChanged() {
		return (this.type & PASSWORD_CHANGED_EVENT) != 0;
	}

	@Deprecated
	public boolean isPasswordChanging() {
		return (this.type & PASSWORD_CHANGING_EVENT) != 0;
	}

	@Deprecated
	public boolean isSessionChanged() {
		return (this.type & SESSION_CHANGED_EVENT) != 0;
	}

	@Deprecated
	public boolean isSessionChanging() {
		return (this.type & SESSION_CHANGING_EVENT) != 0;
	}

	@Deprecated
	public boolean isSessionClosed() {
		return (this.type & SESSION_CLOSED_EVENT) != 0;
	}

	@Deprecated
	public boolean isSessionOpened() {
		return (this.type & SESSION_OPENED_EVENT) != 0;
	}

	@Deprecated
	public boolean isViewChanged() {
		return (this.type & VIEW_CHANGED_EVENT) != 0;
	}

	public boolean isLoggedIn() {
		return (this.type & LOGGED_IN_EVENT) != 0;
	}
	
	public boolean isLoggedOut() {
		return (this.type & LOGGED_OUT_EVENT) != 0;
	}

}
