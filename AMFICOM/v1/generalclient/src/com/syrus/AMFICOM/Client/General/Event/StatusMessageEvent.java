package com.syrus.AMFICOM.Client.General.Event;

public class StatusMessageEvent extends OperationEvent
{
	/**
	 * @deprecated use one of
	 * 		STATUS_MESSAGE
	 * 		STATUS_DOMAIN
	 * 		STATUS_USER
	 * 		STATUS_SESSION
	 * 		STATUS_SERVER
	 */
	static final public String type = "statusmessage";

	static final public String STATUS_MESSAGE = "statusmessage";
	static final public String STATUS_DOMAIN = "statusdomain";
	static final public String STATUS_USER = "statususer";
	static final public String STATUS_SESSION = "statussession";
	static final public String STATUS_SERVER = "statusserver";
	static final public String STATUS_PROGRESS_BAR = "statusprogress";

	String text = "";
	boolean showProgressBar = false;
	
	public StatusMessageEvent(String type, String text)
	{
		super(text, 0, type);
		this.text = text;
	}

	public StatusMessageEvent(String type, boolean showProgressBar)
	{
		super(Boolean.valueOf(showProgressBar), 0, type);
		this.showProgressBar = showProgressBar;
	}

	/**
	 * @deprecated use {@link #StatusMessageEvent(String, String) StatusMessageEvent(STATUS_MESSAGE, text)}
	 */
	public StatusMessageEvent(String text)
	{
		this(STATUS_MESSAGE, text);
//		super(text, 0, type);
	}

	public String getText()
	{
		return text;
	}
	
	public boolean getShowProgressBar()
	{
		return showProgressBar;
	}
}
