package com.syrus.AMFICOM.Client.Resource.Result;

import java.text.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;

public class ModelingModel extends ObjectResourceModel
{
	Modeling m;

	public ModelingModel(Modeling modeling)
	{
		m = modeling;
	}

	public String getColumnValue(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy hh:mm:ss");
		String s = "";
		try
		{
			if(col_id.equals("id"))
				s = m.getId();
			if(col_id.equals("name"))
				s = m.getName();
			if(col_id.equals("modified"))
				s = sdf.format(new Date(m.modified));
			if(col_id.equals("user_id"))
				s = m.user_id;
		}
		catch(Exception e)
		{
			System.out.println("error gettin field value - Modeling");
			s = "";
		}
		return s;
	}
}
