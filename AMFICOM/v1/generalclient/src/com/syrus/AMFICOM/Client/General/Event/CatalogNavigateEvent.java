package com.syrus.AMFICOM.Client.General.Event;

public class CatalogNavigateEvent extends OperationEvent
{
	public boolean CATALOG_PATH_SELECTED = false;
	public boolean CATALOG_EQUIPMENT_SELECTED = false;
	public boolean CATALOG_PORT_SELECTED = false;
	public boolean CATALOG_CABLE_PORT_SELECTED = false;
	public boolean CATALOG_ACCESS_PORT_SELECTED = false;
	public boolean CATALOG_LINK_SELECTED = false;
	public boolean CATALOG_CABLE_LINK_SELECTED = false;

	public boolean CATALOG_PATH_DESELECTED = false;
	public boolean CATALOG_EQUIPMENT_DESELECTED = false;
	public boolean CATALOG_PORT_DESELECTED = false;
	public boolean CATALOG_CABLE_PORT_DESELECTED = false;
	public boolean CATALOG_ACCESS_PORT_DESELECTED = false;
	public boolean CATALOG_LINK_DESELECTED = false;
	public boolean CATALOG_CABLE_LINK_DESELECTED = false;

	public static final long CATALOG_PATH_SELECTED_EVENT = 0x00000001;
	public static final long CATALOG_EQUIPMENT_SELECTED_EVENT = 0x00000002;
	public static final long CATALOG_PORT_SELECTED_EVENT = 0x00000004;
	public static final long CATALOG_CABLE_PORT_SELECTED_EVENT = 0x00000008;
	public static final long CATALOG_ACCESS_PORT_SELECTED_EVENT = 0x00000010;
	public static final long CATALOG_LINK_SELECTED_EVENT = 0x00000020;
	public static final long CATALOG_CABLE_LINK_SELECTED_EVENT = 0x00000040;

	public static final long CATALOG_PATH_DESELECTED_EVENT = 0x00000100;
	public static final long CATALOG_EQUIPMENT_DESELECTED_EVENT = 0x00000200;
	public static final long CATALOG_PORT_DESELECTED_EVENT = 0x00000400;
	public static final long CATALOG_CABLE_PORT_DESELECTED_EVENT = 0x00000800;
	public static final long CATALOG_ACCESS_PORT_DESELECTED_EVENT = 0x00001000;
	public static final long CATALOG_LINK_DESELECTED_EVENT = 0x00002000;
	public static final long CATALOG_CABLE_LINK_DESELECTED_EVENT = 0x00004000;

	public boolean isEditable = true;

	static final public String type = "catalognavigate";

	public CatalogNavigateEvent(Object source, long typ, boolean isEditable)
	{
		super(source, 0, type);
		setTyp(typ);
		this.isEditable = isEditable;
	}

	public CatalogNavigateEvent(Object source, long typ)
	{
		this(source, typ, true);
	}

	public void setTyp(long typ)
	{
		if((typ == CATALOG_PATH_SELECTED_EVENT))
			CATALOG_PATH_SELECTED = true;
		if((typ == CATALOG_EQUIPMENT_SELECTED_EVENT))
			CATALOG_EQUIPMENT_SELECTED = true;
		if((typ == CATALOG_PORT_SELECTED_EVENT))
			CATALOG_PORT_SELECTED = true;
		if((typ == CATALOG_CABLE_PORT_SELECTED_EVENT))
			CATALOG_CABLE_PORT_SELECTED = true;
		if((typ == CATALOG_ACCESS_PORT_SELECTED_EVENT))
			CATALOG_ACCESS_PORT_SELECTED = true;
		if((typ == CATALOG_LINK_SELECTED_EVENT))
			CATALOG_LINK_SELECTED = true;
		if((typ == CATALOG_CABLE_LINK_SELECTED_EVENT))
			CATALOG_CABLE_LINK_SELECTED = true;


		if((typ == CATALOG_PATH_DESELECTED_EVENT))
			CATALOG_PATH_DESELECTED = true;
		if((typ == CATALOG_EQUIPMENT_DESELECTED_EVENT))
			CATALOG_EQUIPMENT_DESELECTED = true;
		if((typ == CATALOG_PORT_DESELECTED_EVENT))
			CATALOG_PORT_DESELECTED = true;
		if((typ == CATALOG_CABLE_PORT_DESELECTED_EVENT))
			CATALOG_CABLE_PORT_DESELECTED = true;
		if((typ == CATALOG_ACCESS_PORT_DESELECTED_EVENT))
			CATALOG_ACCESS_PORT_DESELECTED = true;
		if((typ == CATALOG_LINK_DESELECTED_EVENT))
			CATALOG_LINK_DESELECTED = true;
		if((typ == CATALOG_CABLE_LINK_DESELECTED_EVENT))
			CATALOG_CABLE_LINK_DESELECTED = true;
	}

}
