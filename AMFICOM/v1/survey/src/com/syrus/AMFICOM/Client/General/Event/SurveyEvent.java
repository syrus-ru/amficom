package com.syrus.AMFICOM.Client.General.Event;

public class SurveyEvent extends OperationEvent 
{
	public static final String ALARM_FRAME_DISPLAYED  = "alarmFrameDisplayed";
	public static final String ALARM_POPUP_FRAME_DISPLAYED =
		"alarmPopupFrameDisplayed";
	public static final String RESULT_FRAME_DISPLAYED = "resultFrameDisplayed";

	private SurveyEvent(Object source, String command)
	{
		super(source, 0, command);
	}
}