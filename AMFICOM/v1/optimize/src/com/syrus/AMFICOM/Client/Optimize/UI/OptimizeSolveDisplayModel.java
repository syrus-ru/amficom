package com.syrus.AMFICOM.Client.Optimize.UI;

//import java.awt.*;
import java.util.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

public class OptimizeSolveDisplayModel extends StubDisplayModel
{
	private List columnList;
	//---------------------------------------------------------------------------------------------------------------
	public OptimizeSolveDisplayModel()
	{ super();
	}
	//---------------------------------------------------------------------------------------------------------------
	public List getColumns()
	{  if (this.columnList==null)
	   {	this.columnList = new ArrayList();
			this.columnList.add("full_path");
		}
		return this.columnList;
	}
	//---------------------------------------------------------------------------------------------------------------
	public String getColumnName(String col_id)
	{	String s = "";
		if(col_id.equals("full_path"))
		{	s = "Маршрут";
		}
		return s;
	}
	//---------------------------------------------------------------------------------------------------------------
	public boolean isColumnEditable(String col_id)
	{	boolean result = false;
		if(col_id.equals("full_path"))
		{	result = false;
		}
		return result;
	}
	//----------------------------------------------------------------------------------------------------------------
}