package com.syrus.AMFICOM.Client.Survey.Result.UI;

import java.awt.*;
import java.text.*;
import java.util.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;

public class AnalysisDisplayModel extends StubDisplayModel
{
	public AnalysisDisplayModel()
	{
	}

	public PropertyEditor getColumnEditor(ObjectResource o, String col_id)
	{
		if (!(o instanceof Analysis))
			return null;
		Analysis a = (Analysis)o;

		if(col_id.equals("name"))
			return new TextFieldEditor(a.getName());
		return null;
	}

	public String getColumnName (String col_id)
	{
		String s = "";
		if(col_id.equals("id"))
			s = "�������������";
		if(col_id.equals("name"))
			s = "��������";
		if(col_id.equals("created"))
			s = "����� ��������";
		if(col_id.equals("user_id"))
			s = "������������";
		if(col_id.equals("description"))
			s = "�����������";
		return s;
	}

	public PropertyRenderer getColumnRenderer(ObjectResource o, String col_id)
	{
		if (!(o instanceof Analysis))
			return null;
		Analysis a = (Analysis)o;
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy hh:mm:ss");

		if(col_id.equals("id"))
			return new TextFieldEditor(a.getId());
		if(col_id.equals("name"))
			return new TextFieldEditor(a.getName());
		if(col_id.equals("modified"))
			return new TextFieldEditor(sdf.format(new Date(a.modified)));
		if(col_id.equals("user_id"))
			return new TextFieldEditor(a.user_id);
		if(col_id.equals("description"))
			return new TextFieldEditor(a.description);
		return null;
	}

	public boolean isColumnEditable(String col_id)
	{
		if(col_id.equals("id"))
			return false;
		if(col_id.equals("name"))
			return true;
		if(col_id.equals("created"))
			return false;
		if(col_id.equals("user_id"))
			return false;
		if(col_id.equals("description"))
			return false;
		return false;
	}

	public Vector getColumns()
	{
		Vector cols = new Vector();
		cols.add("id");
		cols.add("name");
		cols.add("created");
		cols.add("user_id");
		cols.add("description");
		return cols;
	}

	public int getColumnSize(String col_id)
	{
		if(col_id.equals("id"))
			return 100;
		if(col_id.equals("name"))
			return 100;
		if(col_id.equals("created"))
			return 100;
		if(col_id.equals("user_id"))
			return 100;
		if(col_id.equals("description"))
			return 100;
		return 100;
	}

	public Color getColumnColor (ObjectResource o, String col_id)
	{
		return Color.white;
	}

	public boolean isColumnColored (String col_id)
	{
		return false;
	}

}