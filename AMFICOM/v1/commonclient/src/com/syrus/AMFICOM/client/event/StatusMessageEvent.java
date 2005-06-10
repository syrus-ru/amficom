
package com.syrus.AMFICOM.client.event;

import java.beans.PropertyChangeEvent;

import com.syrus.util.Log;

public class StatusMessageEvent extends PropertyChangeEvent {

	private static final long serialVersionUID = 3257847671132730418L;

	public static final String	STATUS_MESSAGE		= "statusmessage";
	public static final String	STATUS_DOMAIN		= "statusdomain";
	public static final String	STATUS_USER			= "statususer";
	public static final String	STATUS_SESSION		= "statussession";
	public static final String	STATUS_SERVER		= "statusserver";
	public static final String	STATUS_PROGRESS_BAR	= "statusprogress";

	public StatusMessageEvent(Object source, String type, String text) {
		super(source, type, null, text);
		Log.debugMessage("StatusMessageEvent.StatusMessageEvent | text ", Log.FINEST);
	}

	public StatusMessageEvent(Object source, String type, boolean showProgressBar) {
		super(source, type, null, Boolean.valueOf(showProgressBar));
		Log.debugMessage("StatusMessageEvent.StatusMessageEvent | boolean ", Log.FINEST);
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
