
package com.syrus.AMFICOM.client.event;

import java.beans.PropertyChangeEvent;

public final class ContextChangeEvent extends PropertyChangeEvent {

	public static final long	LOGGED_IN_EVENT				= 0x00000001;
	public static final long	LOGGED_OUT_EVENT			= 0x00000002;
	
	public static final String	TYPE						= ContextChangeEvent.class.getName();
	
	private long type;

	public ContextChangeEvent(final Object source, final long type) {
		super(source, TYPE, null, null);
		this.type = type;
	}

	public boolean isLoggedIn() {
		return (this.type & LOGGED_IN_EVENT) != 0;
	}
	
	public boolean isLoggedOut() {
		return (this.type & LOGGED_OUT_EVENT) != 0;
	}

}
