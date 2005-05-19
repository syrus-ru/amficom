
package com.syrus.AMFICOM.client.event;

import java.beans.PropertyChangeEvent;

public class StatusMessageEvent extends PropertyChangeEvent {

	public static final String	STATUS_MESSAGE		= "statusmessage";
	public static final String	STATUS_DOMAIN		= "statusdomain";
	public static final String	STATUS_USER			= "statususer";
	public static final String	STATUS_SESSION		= "statussession";
	public static final String	STATUS_SERVER		= "statusserver";
	public static final String	STATUS_PROGRESS_BAR	= "statusprogress";

	public StatusMessageEvent(Object source, String type, String text) {
		super(source, type, null, text);
	}

	public StatusMessageEvent(Object source, String type, boolean showProgressBar) {
		super(source, type, null, Boolean.valueOf(showProgressBar));
	}

	public String getText() {
		Object newValue = this.getNewValue();
		if (newValue instanceof String) { return (String) newValue; }
		return null;
	}

	public boolean isShowProgressBar() {
		Object newValue = this.getNewValue();
		if (newValue instanceof Boolean) { return ((Boolean) newValue).booleanValue(); }
		return false;
	}
}
