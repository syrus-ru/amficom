package com.syrus.AMFICOM.Client.Map.UI.Display;

import com.syrus.AMFICOM.Client.General.UI.PropertyEditor;
import com.syrus.AMFICOM.Client.General.UI.PropertyRenderer;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.TextFieldEditor;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarker;
import com.syrus.AMFICOM.Client.Resource.Map.MapTransmissionPathElement;
import com.syrus.AMFICOM.Client.Resource.MyUtil;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

//A0A
public class MapMarkerDisplayModel extends StubDisplayModel
{
	public MapMarkerDisplayModel()
	{
		super();
	}

	List cols = new LinkedList();
	{
//		cols.add("id");
//		cols.add("name");
		cols.add("path_id");
		cols.add("longitude");
		cols.add("latitude");
		cols.add("Length_1");
		cols.add("Length_2");
	}

	public List getColumns()
	{
		return cols;
	}

	public String getColumnName(String col_id)
	{
		if(col_id.equals("id"))
			return "Идентификатор";
		if(col_id.equals("name"))
			return "Название";
		if(col_id.equals("path_id"))
			return "Путь";
		if(col_id.equals("longitude"))
			return "Долгота";
		if(col_id.equals("latitude"))
			return "Широта";
		if(col_id.equals("Length_1"))
			return "Длина_1";
		if(col_id.equals("Length_2"))
			return "Длина_2";
		return "";
	}

	public int getColumnSize(String col_id)
	{
		if(col_id.equals("id"))
			return 100;
		if(col_id.equals("name"))
			return 100;
		if(col_id.equals("codename"))
			return 100;
		if(col_id.equals("path_id"))
			return 100;
		if(col_id.equals("longitude"))
			return 100;
		if(col_id.equals("latitude"))
			return 100;
		return 100;
	}
/*
	public boolean isColumnEditable(String col_id)
	{
		if(col_id.equals("id"))
			return false;
		if(col_id.equals("name"))
			return true;
		if(col_id.equals("codename"))
			return true;
		if(col_id.equals("owner_id"))
			return false;
		if(col_id.equals("Length_1"))
			return true;
		if(col_id.equals("Length_2"))
			return false;
		if(col_id.equals("typ"))
			return false;
		if(col_id.equals("longitude"))
			return true;
		if(col_id.equals("latitude"))
			return true;
		return false;
	}
*/
	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
	{
		MapMarker marker = (MapMarker )or;
		if(col_id.equals("id"))
			return new TextFieldEditor(marker.getId());
		if(col_id.equals("name"))
			return new TextFieldEditor(marker.name);
		if(col_id.equals("path_id"))
			return new TextFieldEditor( Pool.getName(MapTransmissionPathElement.typ, marker.path_id));
		if(col_id.equals("longitude"))
			return new TextFieldEditor( String.valueOf(MyUtil.fourdigits(marker.getAnchor().x)) );
		if(col_id.equals("latitude"))
			return new TextFieldEditor( String.valueOf(MyUtil.fourdigits(marker.getAnchor().y)) );
		if(col_id.equals("Length_1"))
			return new TextFieldEditor( String.valueOf(MyUtil.fourdigits(marker.getFromStartLengthLf())) );
		if(col_id.equals("Length_2"))
			return new TextFieldEditor( String.valueOf(MyUtil.fourdigits(marker.getFromEndLengthLf())) );
		return null;
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
	{
		return (PropertyEditor )getColumnRenderer(or, col_id);
	}
}