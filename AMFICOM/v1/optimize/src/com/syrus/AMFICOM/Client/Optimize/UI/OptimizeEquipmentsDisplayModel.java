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

//модель таблицы для окна, в котором отображаются оптимизационные атрибуты КИСов ( fixed, active и т д )
//================================================================================================================
public class OptimizeEquipmentsDisplayModel extends StubDisplayModel
{	private OptimizeMDIMain mdiMain;
  //------------------------------------------------------------------------------------
  public OptimizeEquipmentsDisplayModel(OptimizeMDIMain mdiMain)
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
  {	Vector cols = new Vector();
    cols.add("optimizerNodeAttribute");
    //cols.add("id");
    cols.add("name");
    return cols;
  }
  //------------------------------------------------------------------------------------
  public String getColumnName(String col_id)
  {	String result = "";
    if(col_id.equals("name"))                        { result = "название";}
    else if(col_id.equals("optimizerNodeAttribute")) { result = "наличие КИС";}
    else if(col_id.equals("id"))                     { result = "идентификатор";}

    return result;
  }
  //------------------------------------------------------------------------------------
  public int getColumnSize(String col_id)
  {	int result = 100;
    if(col_id.equals("name"))                           { result = 200;}
    else if(col_id.equals("optimizerNodeAttribute"))	{ result = 100;}
    else if(col_id.equals("id"))                        { result = 100;}

    return result;
  }
  //------------------------------------------------------------------------------------
  // редактировать можно только ячейку с описанием оптимизационных атрибутов вершины
  public boolean isColumnEditable(String string)
  { boolean result = false;
    if(string.equals("optimizerNodeAttribute"))
    {	result = true;
    }
    return result;
  }
  //------------------------------------------------------------------------------------
  public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
  {	return (PropertyEditor) getColumnRenderer(or, col_id);
  }
  //------------------------------------------------------------------------------------
  // рендерер стобца с комбобоксами атрибутов оптимизации
  // муфтам сразу автоматически выставляется "restricted"
  public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
  { PropertyRenderer result = null;
    if( col_id.equals("optimizerNodeAttribute") && (or instanceof SchemeElement) )
    {	SchemeElement schel = (SchemeElement) or;
      ElementAttribute attr = (ElementAttribute )schel.attributes.get("optimizerNodeAttribute");
      // если атрибут не существует, то создаём его и прописываем optional
      if( attr == null )
      {	attr = new ElementAttribute();
        attr.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
        attr.name = "Наличие КИС";
        attr.type_id = "optimizerNodeAttribute";
        attr.value = "optional";// по умолчанию стоит optional

        schel.attributes.put(attr.type_id, attr);
      }
      //************************* !!!
      // в муфтах запрещаем ставить КИС
      if(! schel.equipment_id.equals("") )// по элементу берём оборудование из пула если он есть в каталоге
      {  Equipment eq = (Equipment)Pool.get(Equipment.typ, schel.equipment_id);
         if( eq != null && eq.eq_class.equals("mufta") ) // если это муфта, то ставим запрещение на КИС в этом узле (null если eq это kis)
         {  ElementAttribute att = (ElementAttribute )schel.attributes.get("optimizerNodeAttribute");
            if( att == null )// если атрибут не существует, то создаём его и прописываем  в хэш елемента
            {	 att = new ElementAttribute();
               att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ); //уникальный идентификатор объекта (генерится на сервере)
               schel.attributes.put(att.type_id, att);
            }
            att.name = "Наличие КИС";
            att.type_id = "optimizerNodeAttribute";
            att.value = "restricted";
         }
      }
      else // если нет в каталоге, то узнавать, муфта это или нет, надо иначе - через EquipmentType
      {	 if(!schel.proto_element_id.equals(""))// пустое поле это НЕ муфта, а схема боле низкого порядка (узел может быть схемой в силу рекурсивности схемы)
         { ProtoElement proto = (ProtoElement)Pool.get(ProtoElement.typ, schel.proto_element_id );
           EquipmentType eqtype = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipment_type_id );
           if( eqtype!=null && eqtype.eq_class.equals("mufta"))
           { ElementAttribute att = (ElementAttribute)schel.attributes.get("optimizerNodeAttribute");
             if(att==null) // если атрибут не существует, то создаём его и прописываем  в хэш елемента
             { att = new ElementAttribute();
               att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ); //уникальный идентификатор объекта (генерится на сервере)
               schel.attributes.put(att.type_id, att);
             }
             att.name = "Наличие КИС";
             att.type_id = "optimizerNodeAttribute";
             att.value = "restricted";
           }
         }
      }
      //*************************
      result = new OptimizerNodeAttributeComboBox((String)attr.value);
    }
    return result;

  }
  //------------------------------------------------------------------------------------
  public boolean isColumnColored(String col_id)
  {	return true;
  }
  //------------------------------------------------------------------------------------
  public Color getColumnColor (ObjectResource or, String col_id)
  {	Color color = Color.cyan;
    if(or instanceof SchemeElement)
    {  SchemeElement se = (SchemeElement)or;
       if(se.attributes.get("optimizerNodeAttribute") != null) // по умолчания всегда null
       { if( ((ElementAttribute)se.attributes.get("optimizerNodeAttribute")).value.equals("obligatory") )
         { color = Color.green;
         }
         else if( ((ElementAttribute)se.attributes.get("optimizerNodeAttribute")).value.equals("restricted") )
         { color = Color.yellow;
         }
         else if( ((ElementAttribute)se.attributes.get("optimizerNodeAttribute")).value.equals("optional") )
         { color = Color.white;
         }
       }
    }
    return color;
  }
  //------------------------------------------------------------------------------------
}
