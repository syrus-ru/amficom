package com.syrus.AMFICOM.Client.General.Event;

public class SchemeNavigateEvent extends OperationEvent
{
	public boolean SCHEME_PATH_SELECTED = false;
	public boolean SCHEME_ELEMENT_SELECTED = false;
	public boolean SCHEME_PROTO_ELEMENT_SELECTED = false;
	public boolean SCHEME_PORT_SELECTED = false;
	public boolean SCHEME_CABLE_PORT_SELECTED = false;
	public boolean SCHEME_LINK_SELECTED = false;
	public boolean SCHEME_CABLE_LINK_SELECTED = false;

	public boolean SCHEME_PATH_DESELECTED = false;
	public boolean SCHEME_ELEMENT_DESELECTED = false;
	public boolean SCHEME_PROTO_ELEMENT_DESELECTED = false;
	public boolean SCHEME_PORT_DESELECTED = false;
	public boolean SCHEME_CABLE_PORT_DESELECTED = false;
	public boolean SCHEME_LINK_DESELECTED = false;
	public boolean SCHEME_CABLE_LINK_DESELECTED = false;
	public boolean SCHEME_ALL_DESELECTED = false;
	public boolean SCHEME_SELECTED = false;

	public static final long SCHEME_PATH_SELECTED_EVENT = 0x00000001;
	public static final long SCHEME_ELEMENT_SELECTED_EVENT = 0x00000002;
	public static final long SCHEME_PROTO_ELEMENT_SELECTED_EVENT = 0x00000004;
	public static final long SCHEME_PORT_SELECTED_EVENT = 0x00000008;
	public static final long SCHEME_CABLE_PORT_SELECTED_EVENT = 0x00000010;
	public static final long SCHEME_LINK_SELECTED_EVENT = 0x00000020;
	public static final long SCHEME_CABLE_LINK_SELECTED_EVENT = 0x00000040;

	public static final long SCHEME_PATH_DESELECTED_EVENT = 0x00000100;
	public static final long SCHEME_ELEMENT_DESELECTED_EVENT = 0x00000200;
	public static final long SCHEME_PROTO_ELEMENT_DESELECTED_EVENT = 0x00000200;
	public static final long SCHEME_PORT_DESELECTED_EVENT = 0x00000800;
	public static final long SCHEME_CABLE_PORT_DESELECTED_EVENT = 0x00001000;
	public static final long SCHEME_LINK_DESELECTED_EVENT = 0x00002000;
	public static final long SCHEME_CABLE_LINK_DESELECTED_EVENT = 0x00004000;
	public static final long SCHEME_ALL_DESELECTED_EVENT = 0x00010000;
	public static final long SCHEME_SELECTED_EVENT = 0x00020000;

	public boolean isEditable = true;

	static final public String type = "schemenavigate";

	public SchemeNavigateEvent(Object source,	long typ, boolean isEditable)
	{
		super(source, 0, type);
		setTyp(typ);
		this.isEditable = isEditable;
	}

	public SchemeNavigateEvent(Object source, long typ)
	{
		this(source, typ, true);
	}

	public void setTyp(long typ)
	{
		if((typ == SCHEME_PATH_SELECTED_EVENT))
			SCHEME_PATH_SELECTED = true;
		if((typ == SCHEME_ELEMENT_SELECTED_EVENT))
			SCHEME_ELEMENT_SELECTED = true;
		if((typ == SCHEME_PROTO_ELEMENT_SELECTED_EVENT))
			SCHEME_PROTO_ELEMENT_SELECTED = true;
		if((typ == SCHEME_PORT_SELECTED_EVENT))
			SCHEME_PORT_SELECTED = true;
		if((typ == SCHEME_CABLE_PORT_SELECTED_EVENT))
			SCHEME_CABLE_PORT_SELECTED = true;
		if((typ == SCHEME_LINK_SELECTED_EVENT))
			SCHEME_LINK_SELECTED = true;
		if((typ == SCHEME_CABLE_LINK_SELECTED_EVENT))
			SCHEME_CABLE_LINK_SELECTED = true;

		if((typ == SCHEME_PATH_DESELECTED_EVENT))
			SCHEME_PATH_DESELECTED = true;
		if((typ == SCHEME_ELEMENT_DESELECTED_EVENT))
			SCHEME_ELEMENT_DESELECTED = true;
		if((typ == SCHEME_PORT_DESELECTED_EVENT))
			SCHEME_PORT_DESELECTED = true;
		if((typ == SCHEME_CABLE_PORT_DESELECTED_EVENT))
			SCHEME_CABLE_PORT_DESELECTED = true;
		if((typ == SCHEME_LINK_DESELECTED_EVENT))
			SCHEME_LINK_DESELECTED = true;
		if((typ == SCHEME_CABLE_LINK_DESELECTED_EVENT))
			SCHEME_CABLE_LINK_DESELECTED = true;
		if((typ == SCHEME_ALL_DESELECTED_EVENT))
			SCHEME_ALL_DESELECTED = true;
		if((typ == SCHEME_SELECTED_EVENT))
			SCHEME_SELECTED = true;
	}

}

