package com.syrus.AMFICOM.Client.General.Event;

//import com.syrus.AMFICOM.Client.Resource.Scheme.*;
//A0A
public class MapNavigateEvent extends OperationEvent
{
	public boolean MAP_MARKER_CREATED = false;
	public boolean MAP_MARKER_DELETED = false;
	public boolean MAP_MARKER_MOVED = false;
	public boolean MAP_MARKER_SELECTED = false;
	public boolean MAP_MARKER_DESELECTED = false;

	public boolean DATA_MARKER_CREATED = false;
	public boolean DATA_MARKER_DELETED = false;
	public boolean DATA_MARKER_MOVED = false;
	public boolean DATA_MARKER_SELECTED = false;
	public boolean DATA_MARKER_DESELECTED = false;

	public boolean DATA_EVENTMARKER_CREATED = false;
	public boolean DATA_EVENTMARKER_DELETED = false;

	public boolean DATA_ALARMMARKER_CREATED = false;
	public boolean DATA_ALARMMARKER_DELETED = false;

	public boolean MAP_PATH_SELECTED = false;
	public boolean MAP_ELEMENT_SELECTED = false;

	public boolean MAP_PATH_DESELECTED = false;
	public boolean MAP_ELEMENT_DESELECTED = false;

	public boolean MAP_PROPERTY_SELECTED = false;

	public static final long MAP_MARKER_CREATED_EVENT = 0x00000001;
	public static final long MAP_MARKER_DELETED_EVENT = 0x00000004;
	public static final long MAP_MARKER_MOVED_EVENT = 0x00000002;
	public static final long MAP_MARKER_SELECTED_EVENT = 0x00000008;
    public static final long MAP_MARKER_DESELECTED_EVENT = 0x00000010;

	public static final long DATA_MARKER_CREATED_EVENT = 0x00000040;
	public static final long DATA_MARKER_DELETED_EVENT = 0x00000080;
	public static final long DATA_MARKER_MOVED_EVENT = 0x00000020;
	public static final long DATA_MARKER_SELECTED_EVENT = 0x00000100;
	public static final long DATA_MARKER_DESELECTED_EVENT = 0x00000200;

	public static final long DATA_EVENTMARKER_CREATED_EVENT = 0x00000041;
	public static final long DATA_EVENTMARKER_DELETED_EVENT = 0x00000042;

	public static final long DATA_ALARMMARKER_CREATED_EVENT = 0x00000043;
	public static final long DATA_ALARMMARKER_DELETED_EVENT = 0x00000044;

	public static final long MAP_PATH_SELECTED_EVENT = 0x00008000;
	public static final long MAP_ELEMENT_SELECTED_EVENT = 0x00000014;

	public static final long MAP_PATH_DESELECTED_EVENT = 0x00200000;
	public static final long MAP_ELEMENT_DESELECTED_EVENT = 0x00000015;

	public static final long MAP_PROPERTY_SELECTED_EVENT = 0x00000013;

	public String marker_id;
	public double distance;

    public String mappathID;
    public String meID;
    public String linkID;

    public Object descriptor;

	public Object spd = null;

	static final public String type = "mapnavigate";

	public MapNavigateEvent(
			Object source,
			long typ)
	{
		super(source, 0, type);
		setTyp(typ);
	}
	
	public MapNavigateEvent(
			Object source,
			long typ,
			String marker_id,
			double myDistance,
			String map_path_id,
			String me_id)
	{
		super(source, 0, type);
        this.marker_id = marker_id;
		this.distance = myDistance;
        this.mappathID = map_path_id;
        this.meID = me_id;

		setTyp(typ);
	}

	public MapNavigateEvent(
			Object source,
			long typ,
			String marker_id,
			double myDistance,
			String scheme_path_id,
			String me_id,
			String link_id)
	{
		this(source, typ, marker_id, myDistance, scheme_path_id, me_id);
        this.linkID = link_id;
	}

	public void setTyp(long typ)
	{
		if((typ == MAP_MARKER_CREATED_EVENT))
			MAP_MARKER_CREATED = true;
		if((typ == MAP_MARKER_DELETED_EVENT))
			MAP_MARKER_DELETED = true;
		if((typ == MAP_MARKER_MOVED_EVENT))
			MAP_MARKER_MOVED = true;
		if((typ == MAP_MARKER_SELECTED_EVENT))
			MAP_MARKER_SELECTED = true;
		if((typ == MAP_MARKER_DESELECTED_EVENT))
			MAP_MARKER_DESELECTED = true;

		if((typ == DATA_MARKER_CREATED_EVENT))
			DATA_MARKER_CREATED = true;
		if((typ == DATA_MARKER_DELETED_EVENT))
			DATA_MARKER_DELETED = true;
		if((typ == DATA_MARKER_MOVED_EVENT))
			DATA_MARKER_MOVED = true;
		if((typ == DATA_MARKER_SELECTED_EVENT))
			DATA_MARKER_SELECTED = true;
		if((typ == DATA_MARKER_DESELECTED_EVENT))
			DATA_MARKER_DESELECTED = true;

		if((typ == DATA_EVENTMARKER_CREATED_EVENT))
			DATA_EVENTMARKER_CREATED = true;
		if((typ == DATA_EVENTMARKER_DELETED_EVENT))
			DATA_EVENTMARKER_DELETED = true;

		if((typ == DATA_ALARMMARKER_CREATED_EVENT))
			DATA_ALARMMARKER_CREATED = true;
		if((typ == DATA_ALARMMARKER_DELETED_EVENT))
			DATA_ALARMMARKER_DELETED = true;

		if((typ == MAP_PATH_SELECTED_EVENT))
			MAP_PATH_SELECTED = true;
		if((typ == MAP_ELEMENT_SELECTED_EVENT))
			MAP_ELEMENT_SELECTED = true;
			
		if((typ == MAP_PATH_DESELECTED_EVENT))
			MAP_PATH_DESELECTED = true;
		if((typ == MAP_ELEMENT_DESELECTED_EVENT))
			MAP_ELEMENT_DESELECTED = true;

		if((typ == MAP_PROPERTY_SELECTED_EVENT))
			MAP_PROPERTY_SELECTED = true;
	}

}
