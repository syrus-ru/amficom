package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;


public class SchemeDisplayModel extends StubDisplayModel
{
	public SchemeDisplayModel()
	{
	}

	public PropertyEditor getColumnEditor(ObjectResource o, String col_id)
	{
		if (!(o instanceof Scheme))
			return null;
		Scheme scheme = (Scheme)o;

		if(col_id.equals("name"))
			return new TextFieldEditor(scheme.getName());
		return null;
	}

	public String getColumnName (String col_id)
	{
		String s = "";
		if(col_id.equals("id"))
			s = "Идентификатор";
		if(col_id.equals("name"))
			s = "Название";
		if(col_id.equals("created"))
			s = "Время создания";
		if(col_id.equals("created_by"))
			s = "Пользователь";
		if(col_id.equals("description"))
			s = "Комментарий";
		return s;
	}

	public PropertyRenderer getColumnRenderer(ObjectResource o, String col_id)
	{
		if (!(o instanceof Scheme))
			return null;
		Scheme scheme = (Scheme)o;
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy hh:mm:ss");

		if(col_id.equals("id"))
			return new TextFieldEditor(scheme.getId());
		if(col_id.equals("name"))
			return new TextFieldEditor(scheme.getName());
		if(col_id.equals("created"))
			return new TextFieldEditor(sdf.format(new Date(scheme.created)));
		if(col_id.equals("created_by"))
			return new TextFieldEditor(scheme.created_by);
		if(col_id.equals("description"))
			return new TextFieldEditor(scheme.description);
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
		if(col_id.equals("created_by"))
			return false;
		if(col_id.equals("description"))
			return false;
		return false;
	}

	public java.util.List getColumns()
	{
		Vector cols = new Vector();
		//cols.add("id");
		cols.add("name");
		cols.add("created");
		cols.add("created_by");
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
		if(col_id.equals("created_by"))
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
