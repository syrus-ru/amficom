package com.syrus.AMFICOM.Client.General.Event;

public class SchemeElementsEvent extends OperationEvent
{
	public boolean UGO_TEXT_UPDATE = false;
	public boolean UGO_ICON_UPDATE = false;
	public boolean UGO_CREATE = false;
	public boolean OPEN_ELEMENT = false;
	public boolean OPEN_SCHEME = false;
	public boolean OPEN_PRIMARY_SCHEME = false;
	public boolean CLOSE_SCHEME = false;
	public boolean SCHEME_UGO_CREATE = false;
	public boolean CREATE_ALARMED_LINK = false;
	public boolean SCHEME_CHANGED = false;

	public boolean CABLE_PORT_NAME_UPDATE = false;
	public boolean CABLE_PORT_TYPE_UPDATE = false;
	public boolean PORT_NAME_UPDATE = false;
	public boolean PORT_TYPE_UPDATE = false;
	public boolean CABLE_LINK_NAME_UPDATE = false;
//  public boolean CABLE_LINK_TYPE_UPDATE = false;
	public boolean LINK_NAME_UPDATE = false;
//  public boolean LINK_TYPE_UPDATE = false;
	public boolean OBJECT_TYPE_UPDATE = false;

	public static final long UGO_TEXT_UPDATE_EVENT = 0x00000001;
	public static final long UGO_ICON_UPDATE_EVENT = 0x00000002;
	public static final long UGO_CREATE_EVENT = 0x00000004;
	public static final long SCHEME_UGO_CREATE_EVENT = 0x00000008;
	public static final long OPEN_ELEMENT_EVENT = 0x00000010;
	public static final long OPEN_SCHEME_EVENT = 0x00000020;
	public static final long CLOSE_SCHEME_EVENT = 0x00000040;
	public static final long OPEN_PRIMARY_SCHEME_EVENT = 0x00000080;
	public static final long SCHEME_CHANGED_EVENT = 0x00000100;

	public static final long CABLE_PORT_NAME_UPDATE_EVENT = 0x00001000;
	public static final long CABLE_PORT_TYPE_UPDATE_EVENT = 0x00002000;
	public static final long PORT_NAME_UPDATE_EVENT = 0x00004000;
	public static final long PORT_TYPE_UPDATE_EVENT = 0x00008000;
	public static final long CABLE_LINK_NAME_UPDATE_EVENT = 0x00010000;
	//public static final long CABLE_LINK_TYPE_UPDATE_EVENT = 0x00020000;
	public static final long LINK_NAME_UPDATE_EVENT = 0x00040000;
	//public static final long LINK_TYPE_UPDATE_EVENT = 0x00080000;
	public static final long CREATE_ALARMED_LINK_EVENT = 0x00100000;

	public static final long OBJECT_TYPE_UPDATE_EVENT = 0x10000000;

	public Object obj;
	static final public String type = "schemeelements";

	public SchemeElementsEvent(Object source, Object obj, long typ)
	{
		super(source, 0, type);
		this.obj = obj;

		if((typ & UGO_TEXT_UPDATE_EVENT ) != 0)
			UGO_TEXT_UPDATE = true;
		if((typ & UGO_ICON_UPDATE_EVENT) != 0)
			UGO_ICON_UPDATE = true;
		if((typ & UGO_CREATE_EVENT) != 0)
			UGO_CREATE = true;
		if((typ & OPEN_ELEMENT_EVENT) != 0)
			OPEN_ELEMENT = true;
		if((typ & OPEN_SCHEME_EVENT) != 0)
			OPEN_SCHEME = true;
		if((typ & OPEN_PRIMARY_SCHEME_EVENT) != 0)
			OPEN_PRIMARY_SCHEME = true;
		if((typ & CLOSE_SCHEME_EVENT) != 0)
			CLOSE_SCHEME = true;
		if((typ & SCHEME_UGO_CREATE_EVENT) != 0)
			SCHEME_UGO_CREATE = true;
	if((typ & SCHEME_CHANGED_EVENT) != 0)
			SCHEME_CHANGED = true;

	if((typ & CABLE_PORT_NAME_UPDATE_EVENT) != 0)
			CABLE_PORT_NAME_UPDATE = true;
		if((typ & CABLE_PORT_TYPE_UPDATE_EVENT) != 0)
			CABLE_PORT_TYPE_UPDATE = true;
		if((typ & PORT_NAME_UPDATE_EVENT) != 0)
			PORT_NAME_UPDATE = true;
		if((typ & PORT_TYPE_UPDATE_EVENT) != 0)
			PORT_TYPE_UPDATE = true;
		if((typ & CABLE_LINK_NAME_UPDATE_EVENT) != 0)
			CABLE_LINK_NAME_UPDATE = true;
//    if((typ & CABLE_LINK_TYPE_UPDATE_EVENT) != 0)
 //     CABLE_LINK_TYPE_UPDATE = true;
		if((typ & LINK_NAME_UPDATE_EVENT) != 0)
			LINK_NAME_UPDATE = true;
//    if((typ & LINK_TYPE_UPDATE_EVENT) != 0)
 //     LINK_TYPE_UPDATE = true;
		if((typ & OBJECT_TYPE_UPDATE_EVENT) != 0)
			OBJECT_TYPE_UPDATE = true;

		if((typ & CREATE_ALARMED_LINK_EVENT) != 0)
			CREATE_ALARMED_LINK = true;
		}
}