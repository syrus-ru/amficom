package com.syrus.AMFICOM.Client.Map.UI.Display;

import com.syrus.AMFICOM.Client.General.UI.PropertyEditor;
import com.syrus.AMFICOM.Client.General.UI.PropertyRenderer;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.TextFieldEditor;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.MyUtil;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.util.Vector;

//A0A
public class MapMarkElementDisplayModel extends StubDisplayModel
{
	public MapMarkElementDisplayModel()
	{
		super();
	}

	public Vector getColumns()
	{
		Vector cols = new Vector();
		cols.add("link_id");
		cols.add("name");
//		cols.add("text");
		cols.add("longitude");
		cols.add("latitude");
		cols.add("Length_1");
		cols.add("Length_2");
		return cols;
	}

	public String getColumnName(String col_id)
	{
		if(col_id.equals("link_id"))
			return "����� �����";
		if(col_id.equals("name"))
			return "�����";
		if(col_id.equals("longitude"))
			return "�������";
		if(col_id.equals("latitude"))
			return "������";
		if(col_id.equals("Length_1"))
			return "�����_1";
		if(col_id.equals("Length_2"))
			return "�����_2";
		return "";
	}

	public int getColumnSize(String col_id)
	{
		if(col_id.equals("link_id"))
			return 100;
		if(col_id.equals("name"))
			return 100;
		if(col_id.equals("longitude"))
			return 100;
		if(col_id.equals("latitude"))
			return 100;
		return 100;
	}

	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
	{
		MapMarkElement mark = (MapMarkElement )or;
		if(col_id.equals("id"))
			return new TextFieldEditor(mark.getId());
		if(col_id.equals("name"))
			return new TextFieldEditor(mark.name);
		if(col_id.equals("longitude"))
			return new TextFieldEditor( String.valueOf(MyUtil.fourdigits(mark.getAnchor().x)) );
		if(col_id.equals("latitude"))
			return new TextFieldEditor( String.valueOf(MyUtil.fourdigits(mark.getAnchor().y)) );
		if(col_id.equals("Length_1"))
			return new TextFieldEditor( String.valueOf(MyUtil.fourdigits(mark.getFromStartLengthLf())) );
		if(col_id.equals("Lengtht_2"))
			return new TextFieldEditor( String.valueOf(MyUtil.fourdigits(mark.getFromEndLengthLf())) );
		return null;
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
	{
		return (PropertyEditor )getColumnRenderer(or, col_id);
	}
}