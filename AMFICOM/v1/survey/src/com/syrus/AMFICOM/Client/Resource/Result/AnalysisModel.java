package com.syrus.AMFICOM.Client.Resource.Result;

import java.text.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;

public class AnalysisModel extends ObjectResourceModel
{
  Analysis a;

  public AnalysisModel(Analysis analysis)
  {
    a = analysis;
  }

  public String getColumnValue(String col_id)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy hh:mm:ss");
    String s = "";
    try
    {
      if(col_id.equals("id"))
        s = a.getId();
      if(col_id.equals("name"))
        s = a.getName();
      if(col_id.equals("modified"))
        s = sdf.format(new Date(a.modified));
      if(col_id.equals("user_id"))
        s = a.user_id;
      if(col_id.equals("description"))
        s = a.description;
    }
    catch(Exception e)
    {
//      System.out.println("error gettin field value - Analysis");
      s = "";
    }
    return s;
	}
}