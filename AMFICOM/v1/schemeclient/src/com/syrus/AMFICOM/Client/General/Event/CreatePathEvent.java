package com.syrus.AMFICOM.Client.General.Event;

public class CreatePathEvent extends OperationEvent
{
	public boolean CREATE_PATH = false;
	public boolean EDIT_PATH = false;
	public boolean ADD_LINK = false;
	public boolean REMOVE_LINK = false;
	public boolean UPDATE_LINK = false;
	public boolean SET_START = false;
	public boolean SET_END = false;
	public boolean CANCEL_PATH_CREATION = false;
	public boolean SAVE_PATH = false;
	public boolean DELETE_PATH = false;
	public boolean PE_SELECTED = false;

	public static final long CREATE_PATH_EVENT = 0x00000001;
	public static final long ADD_LINK_EVENT = 0x00000002;
	public static final long SET_START_EVENT = 0x00000004;
	public static final long SET_END_EVENT = 0x00000008;
	public static final long CANCEL_PATH_CREATION_EVENT = 0x00000010;
	public static final long SAVE_PATH_EVENT = 0x00000020;
	public static final long EDIT_PATH_EVENT = 0x00000040;
	public static final long DELETE_PATH_EVENT = 0x00000080;
	public static final long REMOVE_LINK_EVENT = 0x00000100;
	public static final long PE_SELECTED_EVENT = 0x00000200;
	public static final long UPDATE_LINK_EVENT = 0x00000400;

	public long change_type;
	public Object[] cells;

	public static final String typ = "createpath";

	public CreatePathEvent(Object source, Object[] cells, long type)
	{
		super(source, 0, CreatePathEvent.typ);
		change_type = type;
		this.cells = cells;

		if((type & CREATE_PATH_EVENT) != 0)
			CREATE_PATH = true;
		if((type & EDIT_PATH_EVENT) != 0)
			EDIT_PATH = true;
		if((type & ADD_LINK_EVENT) != 0)
			ADD_LINK = true;
		if((type & REMOVE_LINK_EVENT) != 0)
			REMOVE_LINK = true;
	if((type & UPDATE_LINK_EVENT) != 0)
		UPDATE_LINK = true;
		if((type & SET_START_EVENT) != 0)
			SET_START = true;
		if((type & SET_END_EVENT) != 0)
			SET_END = true;
		if((type & CANCEL_PATH_CREATION_EVENT) != 0)
			CANCEL_PATH_CREATION = true;
		if((type & SAVE_PATH_EVENT) != 0)
			SAVE_PATH = true;
		if((type & DELETE_PATH_EVENT) != 0)
			DELETE_PATH = true;
	if((type & PE_SELECTED_EVENT) != 0)
			PE_SELECTED = true;
	}
}

