package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import java.awt.Color;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;


public class SchemeDisplayModel extends StubDisplayModel
{
	static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy hh:mm:ss");
	static TextFieldEditor r = new TextFieldEditor();
	List cols;

	public SchemeDisplayModel()
	{
		cols = new ArrayList(4);
		cols.add("name");
		cols.add("created");
		cols.add("created_by");
		cols.add("description");
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

		if(col_id.equals("id"))
			r.setText(scheme.getId());
		else if(col_id.equals("name"))
			r.setText(scheme.getName());
		else if(col_id.equals("created"))
			r.setText(sdf.format(new Date(scheme.created)));
		else if(col_id.equals("created_by"))
			r.setText(scheme.created_by);
		else if(col_id.equals("description"))
			r.setText(scheme.description);
		else
			return null;
		return r;
	}

	public boolean isColumnEditable(String col_id)
	{
		if(col_id.equals("name"))
			return true;
		return false;
	}

	public List getColumns()
	{
		return cols;
	}

	public int getColumnSize(String col_id)
	{
//		if(col_id.equals("id"))
//			return 100;
//		if(col_id.equals("name"))
//			return 100;
//		if(col_id.equals("created"))
//			return 100;
//		if(col_id.equals("created_by"))
//			return 100;
//		if(col_id.equals("description"))
//			return 100;
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
