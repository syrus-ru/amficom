package com.syrus.AMFICOM.client.event;

import java.beans.PropertyChangeEvent;

public class ObserverEvent extends PropertyChangeEvent 
{
	public static final String ALARM_FRAME_DISPLAYED  = "alarmFrameDisplayed";
	public static final String ALARM_POPUP_FRAME_DISPLAYED =
		"alarmPopupFrameDisplayed";
	public static final String RESULT_FRAME_DISPLAYED = "resultFrameDisplayed";

	public ObserverEvent(Object source, String command) {
		super(source, command, null, null);
	}
}