
package com.syrus.AMFICOM.client.event;

import java.beans.PropertyChangeEvent;

public class StatusMessageEvent extends PropertyChangeEvent {

	private static final long serialVersionUID = 3257847671132730418L;

	public static final String	STATUS_MESSAGE		= "statusmessage";
	public static final String	STATUS_DOMAIN		= "statusdomain";
	public static final String	STATUS_USER			= "statususer";
	public static final String	STATUS_SESSION		= "statussession";
	public static final String	STATUS_SERVER		= "statusserver";
	public static final String	STATUS_PROGRESS_BEGIN = "statusprogressstart";
	public static final String	STATUS_PROGRESS_END	= "statusprogressend";
	public static final String	STATUS_PROGRESS_TEXT = "statusprogresstext";
	public static final String	STATUS_PROGRESS_PERCENTS = "statusprogresspercents";

	public StatusMessageEvent(Object source, String type, String text) {
		super(source, type, null, text);
	}

	private StatusMessageEvent(Object source, String type, Object obj) {
		super(source, type, null, obj);
	}

	/**
	 * ������� �������, ���������� �������� ������ (����������� �����������)
	 * ProgressBar'�.
	 * 
	 * @param source not null
	 * @param text �����, nullable
	 * @return �������, ���������� �������� ������ (����������� �����������)
	 * ProgressBar'�
	 */
	public static StatusMessageEvent beginProgressBarEvent(Object source, String text) {
		return new StatusMessageEvent(source, STATUS_PROGRESS_BEGIN, text);
	}
	/**
	 * ������� �������, ���������� �������� ���������� ProgressBar'�.
	 * 
	 * @param source not null
	 * @return �������, ���������� �������� ����������  ProgressBar'�
	 */
	public static StatusMessageEvent endProgressBarEvent(Object source) {
		return new StatusMessageEvent(source, STATUS_PROGRESS_END, null);
	}

	/**
	 * ������� �������, ���������� ��������� �������� �������������
	 * ���������� ProgressBar'�.
	 * 
	 * @param source not null
	 * @param percents 0 .. 100
	 * @return �������, ���������� ��������� �������� �������������
	 * ���������� ProgressBar'�
	 */
	public static StatusMessageEvent progressBarPercents(Object source, int percents) {
		return new StatusMessageEvent(
				source, STATUS_PROGRESS_PERCENTS, Integer.valueOf(percents));
	}

	/**
	 * ������� �������, ���������� ��������� ������ �����
	 * ���������� ProgressBar'�.
	 * 
	 * @param source not null
	 * @param text �����, nullable
	 * @return �������, ���������� ��������� ������ �����
	 * ���������� ProgressBar'�
	 */
	public static StatusMessageEvent progressBarText(Object source, String text) {
		return new StatusMessageEvent(source, STATUS_PROGRESS_TEXT, text);
	}

	public int getInteger() {
		return ((Integer)this.getNewValue()).intValue();
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
