package com.syrus.AMFICOM.Client.Optimize.UI;

import java.text.*;
import java.util.*;

import java.awt.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Optimize.*;

public class SolutionCompactDisplayModel extends StubDisplayModel
{
  //-----------------------------------------------------------------------------
  public SolutionCompactDisplayModel(){}
  //-----------------------------------------------------------------------------
  public PropertyEditor getColumnEditor(ObjectResource o, String col_id)
  { if (!(o instanceof SolutionCompact))
  return null;
    SolutionCompact slc = (SolutionCompact)o;
    if(col_id.equals("name"))
  return new TextFieldEditor(slc.getName());
    return null;
  }
  //-----------------------------------------------------------------------------
  public String getColumnName (String col_id)
  { String s = "";
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
  //-----------------------------------------------------------------------------
  public PropertyRenderer getColumnRenderer(ObjectResource o, String col_id)
  { if (!(o instanceof SolutionCompact))
      return null;
    SolutionCompact slc = (SolutionCompact)o;
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy hh:mm:ss");
    if(col_id.equals("id"))
      return new TextFieldEditor(slc.getId());
    if(col_id.equals("name"))
      return new TextFieldEditor(slc.getName());
    if(col_id.equals("created"))
    { if(!slc.created.equals(""))
      return new TextFieldEditor(sdf.format(new Date(slc.created)));
    }
    if(col_id.equals("created_by"))
      return new TextFieldEditor(slc.created_by);
    if(col_id.equals("description"))
      return new TextFieldEditor(slc.description);
    return null;
  }
  //-----------------------------------------------------------------------------
  public boolean isColumnEditable(String col_id)
  { if(col_id.equals("id"))
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
  //-----------------------------------------------------------------------------
  public Vector getColumns()
  { Vector cols = new Vector();
    //cols.add("id");
    cols.add("name");
    cols.add("created");
    cols.add("created_by");
    cols.add("description");
    return cols;
  }
  //-----------------------------------------------------------------------------
  public int getColumnSize(String col_id)
  { if(col_id.equals("id"))
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
  //-----------------------------------------------------------------------------
  public Color getColumnColor (ObjectResource o, String col_id)
  { return Color.white;
  }
  //-----------------------------------------------------------------------------
  public boolean isColumnColored (String col_id)
  { return false;
  }
  //-----------------------------------------------------------------------------
}
