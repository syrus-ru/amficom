package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.CORBA.General.TestTemporalType;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import java.awt.*;
import java.text.*;
import java.util.*;

public class ResultSetModel extends ObjectResourceModel
{
	public ResultSet rs;

	public ResultSetModel(ResultSet rs)
	{
		this.rs = rs;
	}

	public String getColumnValue(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
		String s = "";
		try
		{
			if(col_id.equals("domain_id"))
				s = Pool.getName(Domain.typ, rs.domain_id);
			if(col_id.equals("start_time"))
				s = sdf.format(new Date(rs.start_time));
			if(col_id.equals("end_time"))
				s = sdf.format(new Date(rs.end_time));
			if(col_id.equals("active"))
			{
				if(rs.active)
					s = "Да";
				else
					s = "";
			}
		}
		catch(Exception e)
		{
//			System.out.println("error gettin field value - Result");
			s = "";
		}
		return s;
	}

	public Component getColumnRenderer(String col_id)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

		if(col_id.equals("domain_id"))
			return new ObjectResourceComboBox(Pool.getHash(Domain.typ),rs.domain_id);
		if(col_id.equals("start_time"))
			return new TextFieldEditor(sdf.format(new Date(rs.start_time)));
		if(col_id.equals("end_time"))
			return new TextFieldEditor(sdf.format(new Date(rs.end_time)));
		if(col_id.equals("active"))
			return new TextFieldEditor(getColumnValue(col_id));
		return null;
	}

	public Component getColumnEditor(String col_id)
	{
		return getColumnRenderer(col_id);
	}
}