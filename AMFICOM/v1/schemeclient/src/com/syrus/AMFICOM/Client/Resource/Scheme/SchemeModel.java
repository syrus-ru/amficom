package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;

public class SchemeModel extends ObjectResourceModel
{
	Scheme scheme;

	public SchemeModel(Scheme scheme)
	{
		this.scheme = scheme;
	}

	public String getColumnValue(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy hh:mm:ss");
		String s = "";
		try
		{
			if(col_id.equals("id"))
				s = scheme.getId();
			if(col_id.equals("name"))
				s = scheme.getName();
			if(col_id.equals("created"))
				s = sdf.format(new Date(scheme.created));
			if(col_id.equals("created_by"))
				s = scheme.created_by;
			if(col_id.equals("description"))
				s = scheme.description;
		}
		catch(Exception e)
		{
			System.out.println("error gettin field value - Scheme");
			s = "";
		}
		return s;
	}
}
