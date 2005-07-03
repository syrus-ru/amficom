package com.syrus.AMFICOM.Client.General.Event;


public class ContextChangeEvent extends OperationEvent
{
	public boolean SESSION_OPENED = false;
	public boolean SESSION_CLOSED = false;
	public boolean SESSION_CHANGING = false;
	public boolean SESSION_CHANGED = false;
	public boolean CONNECTION_OPENED = false;
	public boolean CONNECTION_CLOSED = false;
	public boolean CONNECTION_CHANGING = false;
	public boolean CONNECTION_CHANGED = false;
	public boolean VIEW_CHANGED = false;
	public boolean PASSWORD_CHANGING = false;
	public boolean PASSWORD_CHANGED = false;
	public boolean CONNECTION_FAILED = false;
	public boolean DOMAIN_SELECTED = false;

	public static final long SESSION_OPENED_EVENT = 0x00000001;
	public static final long SESSION_CLOSED_EVENT = 0x00000002;
	public static final long SESSION_CHANGED_EVENT = 0x00000004;
	public static final long CONNECTION_OPENED_EVENT = 0x00000008;
	public static final long CONNECTION_CLOSED_EVENT = 0x00000010;
	public static final long CONNECTION_CHANGED_EVENT = 0x00000020;
	public static final long VIEW_CHANGED_EVENT = 0x00000040;
	public static final long PASSWORD_CHANGING_EVENT = 0x00000080;
	public static final long PASSWORD_CHANGED_EVENT = 0x00000100;
	public static final long SESSION_CHANGING_EVENT = 0x00000200;
	public static final long CONNECTION_CHANGING_EVENT = 0x00000400;
	public static final long CONNECTION_FAILED_EVENT = 0x00000800;
	public static final long DOMAIN_SELECTED_EVENT = 0x00001000;

	public long change_type;

	public static final String type = "contextchange";

	public ContextChangeEvent(Object source, long typ)
	{
		super(source, 0, type);
		change_type = typ;

		if((typ & SESSION_OPENED_EVENT) != 0)
			SESSION_OPENED = true;
		if((typ & SESSION_CLOSED_EVENT) != 0)
			SESSION_CLOSED = true;
		if((typ & SESSION_CHANGED_EVENT) != 0)
			SESSION_CHANGED = true;
		if((typ & CONNECTION_OPENED_EVENT) != 0)
			CONNECTION_OPENED = true;
		if((typ & CONNECTION_CLOSED_EVENT) != 0)
			CONNECTION_CLOSED = true;
		if((typ & CONNECTION_CHANGED_EVENT) != 0)
			CONNECTION_CHANGED = true;
		if((typ & VIEW_CHANGED_EVENT) != 0)
			VIEW_CHANGED = true;
		if((typ & PASSWORD_CHANGED_EVENT) != 0)
			PASSWORD_CHANGED = true;
		if((typ & PASSWORD_CHANGING_EVENT) != 0)
			PASSWORD_CHANGING = true;
		if((typ & SESSION_CHANGING_EVENT) != 0)
			SESSION_CHANGING = true;
		if((typ & CONNECTION_CHANGING_EVENT) != 0)
			CONNECTION_CHANGING = true;
		if((typ & CONNECTION_FAILED_EVENT) != 0)
			CONNECTION_FAILED = true;
		if((typ & DOMAIN_SELECTED_EVENT) != 0)
			DOMAIN_SELECTED = true;
	}

}
