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

//������ ������� ��� ����, � ������� ������������ ��������������� �������� ����� ( fixed, active � � � )
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
  //����� ������� �c���������� ��� ����������� ������� ������ � �������
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
    if(col_id.equals("name"))                        { result = "��������";}
    else if(col_id.equals("optimizerNodeAttribute")) { result = "������� ���";}
    else if(col_id.equals("id"))                     { result = "�������������";}

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
  // ������������� ����� ������ ������ � ��������� ��������������� ��������� �������
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
  // �������� ������ � ������������ ��������� �����������
  // ������ ����� ������������� ������������ "restricted"
  public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
  { PropertyRenderer result = null;
    if( col_id.equals("optimizerNodeAttribute") && (or instanceof SchemeElement) )
    {	SchemeElement schel = (SchemeElement) or;
      ElementAttribute attr = (ElementAttribute )schel.attributes.get("optimizerNodeAttribute");
      // ���� ������� �� ����������, �� ������ ��� � ����������� optional
      if( attr == null )
      {	attr = new ElementAttribute();
        attr.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
        attr.name = "������� ���";
        attr.type_id = "optimizerNodeAttribute";
        attr.value = "optional";// �� ��������� ����� optional

        schel.attributes.put(attr.type_id, attr);
      }
      //************************* !!!
      // � ������ ��������� ������� ���
      if(! schel.equipment_id.equals("") )// �� �������� ���� ������������ �� ���� ���� �� ���� � ��������
      {  Equipment eq = (Equipment)Pool.get(Equipment.typ, schel.equipment_id);
         if( eq != null && eq.eq_class.equals("mufta") ) // ���� ��� �����, �� ������ ���������� �� ��� � ���� ���� (null ���� eq ��� kis)
         {  ElementAttribute att = (ElementAttribute )schel.attributes.get("optimizerNodeAttribute");
            if( att == null )// ���� ������� �� ����������, �� ������ ��� � �����������  � ��� ��������
            {	 att = new ElementAttribute();
               att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ); //���������� ������������� ������� (��������� �� �������)
               schel.attributes.put(att.type_id, att);
            }
            att.name = "������� ���";
            att.type_id = "optimizerNodeAttribute";
            att.value = "restricted";
         }
      }
      else // ���� ��� � ��������, �� ��������, ����� ��� ��� ���, ���� ����� - ����� EquipmentType
      {	 if(!schel.proto_element_id.equals(""))// ������ ���� ��� �� �����, � ����� ���� ������� ������� (���� ����� ���� ������ � ���� ������������� �����)
         { ProtoElement proto = (ProtoElement)Pool.get(ProtoElement.typ, schel.proto_element_id );
           EquipmentType eqtype = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipment_type_id );
           if( eqtype!=null && eqtype.eq_class.equals("mufta"))
           { ElementAttribute att = (ElementAttribute)schel.attributes.get("optimizerNodeAttribute");
             if(att==null) // ���� ������� �� ����������, �� ������ ��� � �����������  � ��� ��������
             { att = new ElementAttribute();
               att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ); //���������� ������������� ������� (��������� �� �������)
               schel.attributes.put(att.type_id, att);
             }
             att.name = "������� ���";
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
       if(se.attributes.get("optimizerNodeAttribute") != null) // �� ��������� ������ null
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
