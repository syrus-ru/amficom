package com.syrus.AMFICOM.Client.Optimize.UI;

import java.awt.*;
import java.util.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

//модель таблицы для окна, в котором отображаются оптимизационные атрибуты линоков (рёбер) : active
//================================================================================================================
public class OptimizeRibsDisplayModel extends StubDisplayModel
  {private OptimizeMDIMain mdiMain;
    //------------------------------------------------------------------------------------
    public OptimizeRibsDisplayModel(OptimizeMDIMain mdiMain)
    { super();
      this.mdiMain = mdiMain;
    }
    //------------------------------------------------------------------------------------
    public void setMDIMain (OptimizeMDIMain mdiMain)
    { this.mdiMain =  mdiMain;
    }
    //------------------------------------------------------------------------------------
    //Далее функции иcполузуются для отображения свойств класса в таблице
    public Vector getColumns()
    { Vector cols = new Vector();
      cols.add("optimizerRibAttribute");
      //cols.add("id");
      cols.add("name");
      return cols;
    }
    //------------------------------------------------------------------------------------
    public String getColumnName(String col_id)
    { String result = "";
      if(col_id.equals("name"))                       { result = "название";}
      else if(col_id.equals("optimizerRibAttribute")) { result = "активность";}
      else if(col_id.equals("id"))	                  { result = "идентификатор";}

      return result;
    }
    //------------------------------------------------------------------------------------
    public int getColumnSize(String col_id)
    { int result = 100;
      if(col_id.equals("name"))                       {	result = 200;}
      else if(col_id.equals("optimizerRibAttribute"))	{	result = 100;}
      else if(col_id.equals("id"))                    {	result = 100;}

      return result;
    }
    //------------------------------------------------------------------------------------
    // редактировать можно только ячейку с описанием оптимизационных атрибутов
    public boolean isColumnEditable(String string)
    { boolean result = false;
      if(string.equals("optimizerRibAttribute"))
      {	result = true;
      }
      return result;
    }
    //------------------------------------------------------------------------------------
    public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
    {	return (PropertyEditor)getColumnRenderer(or, col_id);
    }
    //------------------------------------------------------------------------------------
    // рендерер столбца с комбобоксами атрибутов оптимизации
    public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
    { PropertyRenderer result = null;
      if( col_id.equals("optimizerRibAttribute") && (or instanceof SchemeCableLink) )
      {	SchemeCableLink schсl = (SchemeCableLink) or;
        ElementAttribute attr = (ElementAttribute )schсl.attributes.get("optimizerRibAttribute");
        if( attr != null )
        {	result = new OptimizerRibAttributeComboBox((String)attr.value);}
      }
      else if( col_id.equals("optimizerRibAttribute") && (or instanceof SchemeLink) )
      {	SchemeLink sl = (SchemeLink) or;
        ElementAttribute attr = (ElementAttribute )sl.attributes.get("optimizerRibAttribute");
        if( attr != null )
        { result = new OptimizerRibAttributeComboBox((String)attr.value);}
      }
      return result;
    }
    //------------------------------------------------------------------------------------
    public boolean isColumnColored(String col_id)
    { return true;
    }
    //------------------------------------------------------------------------------------
    public Color getColumnColor (ObjectResource or, String col_id)
    {	Color color = Color.PINK;
      if(or instanceof SchemeCableLink)
      {  SchemeCableLink scl = (SchemeCableLink)or;
         ElementAttribute el_at = (ElementAttribute)scl.attributes.get("optimizerRibAttribute");
         if( el_at != null)
         { String scl_av = el_at.value;
           if( scl_av.equals("active") )
           { color = Color.WHITE;}
           else if( scl_av.equals("passive") )
           { color = Color.YELLOW;}
           else if( scl_av.equals("tested") )
           { color = Color.LIGHT_GRAY;}
         }
      }
      else if(or instanceof SchemeLink)
      {  SchemeLink sl = (SchemeLink)or;
         ElementAttribute el_at = (ElementAttribute)sl.attributes.get("optimizerRibAttribute");
         if( el_at != null)
         { String scl_av = el_at.value;
           if( scl_av.equals("active") )
           { color = Color.WHITE;}
           else if( scl_av.equals("passive") )
           { color = Color.YELLOW;}
           else if( scl_av.equals("tested") )
           { color = Color.LIGHT_GRAY;}
         }
      }
      if(color.equals(color.PINK))
      { int i=1;}//!!!
      return color;
    }
    //------------------------------------------------------------------------------------
}
