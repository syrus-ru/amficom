//  ����� �������� �-��� �������������� ������� ��������� ���� � ���, ������� ��� �������� � Dll.
//  �� ������� � DLL ��� ����� ����
package com.syrus.AMFICOM.Client.Optimize;

import java.util.*;

import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.*;

//
//====================================================================================================================
public class Map2GraphAdapter
{   private ArrayList links = new ArrayList();//������������ ��� ��������� ����������� ���������� ������
    private OptimizeMDIMain mdiMain;
    private Scheme scheme = null;

    public Graph g = new Graph();
    //-------------------------------------------------------------------------
    // ��� �������� ������� ����� ���������� ������� � ����
    public Map2GraphAdapter(OptimizeMDIMain mdiMain)
    { CreateGraphByMap(mdiMain);
    }
    //-------------------------------------------------------------------------
    // ������� ��� ���� (� ������ � ��� �������)
    public void CreateGraphByMap(OptimizeMDIMain mdiMain)
    {try
     { this.mdiMain = mdiMain;
       ((OpticalOptimizerContext)mdiMain.optimizerContext).graph_set = 0;// ���� �� ������ ���������, �� �������� �������� "1"
       // ������ ������
       scheme = null;
       if(mdiMain != null) scheme = mdiMain.scheme;// ����� ������������� ������������ � ��� �������� �����, ����� ������� ��� ������ ���� � ����� ������� ��� �������������������
       else
       {System.err.println("CreateGraphByMap(Scheme): mdiMain = null. Impossible to start optimization.");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(),
             "������ �������������.\n ������ ����������� ����������.", "������ ������������� ����", javax.swing.JOptionPane.OK_OPTION);
    return;
       }
       if (scheme == null) // �������� , ��������� �� �����, ���� ���, �� �����
       { System.err.println("CreateGraphByMap(Scheme): Network scheme is not loaded.Impossible to start optimization.");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(),
             "����� ���� �� ���������.\n ������ ����������� ����������.", "������", javax.swing.JOptionPane.OK_OPTION);
         // �� ��������, ��� ���� ������, ����� ������� �� ����������� ����� ������ �� ���� �������
    return;
       }
       int n_cablelinks = processAllCables();// ����������� ������
       if ( n_cablelinks == -1 ) // ��� ���������� ������ �� �-���
    return;
       int n_links = processAllLinks();// ��������� ��� �����
       if(  n_links == -1 )// ��� ���������� ������ �� �-���
    return;
       if(n_cablelinks==0 && n_links==0) // ���� �� ������ ����� ��� ��������� � �� ���� , �� ����� - �������������� ������
       { System.err.println("CreateGraphByMap(Scheme): Nothing to optimize.");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(),
             "����� ��� �������� ������� �� ������������ ���� ����� �����.", "", javax.swing.JOptionPane.OK_OPTION);
    return;
       }
       // � � � �   � � � � � �
       setNodesAttributes();// ������������� �������� ������� ( ����� ��� ��� � �������� ��������� ��� )
       // ���� ����� �� ����� �����, �� ������ ���� ������
       ( (OpticalOptimizerContext)mdiMain.optimizerContext).graph_set = 1;
       System.out.println("Graph created: nnodes = " + g.nodes.size() + " nribs = " + g.ribs.size());
    }//try
    catch(NumberFormatException nfex)
    {  System.err.println("Type cast error in the function of network uploading.");
       javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(),"����������� ������ ��������� ����", "������ ������� ������", javax.swing.JOptionPane.OK_OPTION);
    }
    catch(Exception ex){ex.printStackTrace();}
}
//-------------------------------------------------------------------------
// ������������� �������� ������� ( ����� ��� ��� � �������� ��������� ��� )
  private void setNodesAttributes()
  { System.out.println("��������� ��������� ����������� � ����� ...");
    // �������� �� ���� ����� �����
    for( Iterator nodes = g.nodes.iterator(); nodes.hasNext(); )
    { Node node = (Node)nodes.next();
      String schel_id = node.str_id; // ������������� �������� ����� ( ����� �� , ��� � ������������� ���� � ����� )
      SchemeElement schel = (SchemeElement)Pool.get(SchemeElement.typ,schel_id);
      ElementAttribute att = (ElementAttribute)schel.attributes.get("optimizerNodeAttribute");
      if(att != null)
      { if(att.value.equals("restricted"))
        { node.active = false;
          node.fixed = true; // �������� "active" ������ ������
        }
      }
      // ���� ������� �� ����������, �� ������ ��� � ����������� optional
      else if( att == null )
      { //System.out.println("CreateGraphByMap(Scheme): ������� ����������� optimizerNodeAttribute ���� " + schel_id + " �� �����. ������ �������������.");
        att = new ElementAttribute();
        att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
        att.name = "������� ���";
        att.type_id = "optimizerNodeAttribute";
        att.value = "optional";// �� ��������� ����� optional
        schel.attributes.put(att.type_id, att);
      }
      // ���� ������� ����� �������� ������ ������� ��� �����������, �� � ���������� ������� ���� ���������
      // (�������� ��������� ������ �������� ������ �����)
      SchemeElement upper_schel = scheme.getTopLevelNonSchemeElement(schel);
      ElementAttribute upper_att = (ElementAttribute)upper_schel.attributes.get("optimizerNodeAttribute");
      if(upper_att != null)
      { if(upper_att.value.equals("restricted"))
        { node.active = false;
          node.fixed = true; // �������� "active" ������ ������
          //System.out.println("Element "+schel.id+" is not restricted but is a part of restricted element "+upper_schel.id);
        }
      }
      //************************* !!!
      // ���� ��� ����� �� ��������� ������� ��� � � ��������� ������
      if(!upper_schel.equipment_id.equals("") )// �� �������� ���� ������������ �� ���� ���� �� ���� � ��������
      {  Equipment eq = (Equipment)Pool.get(Equipment.typ, upper_schel.equipment_id);
         if( eq != null && eq.eq_class.equals("mufta") ) // ���� ��� �����, �� ������ ���������� �� ��� � ���� ���� (null ���� eq ��� kis)
         {  upper_att = (ElementAttribute )upper_schel.attributes.get("optimizerNodeAttribute");
            if( upper_att == null )// ���� ������� �� ����������, �� ������ ��� � �����������  � ��� ��������
            {	 upper_att = new ElementAttribute();
               upper_att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ); //���������� ������������� ������� (��������� �� �������)
               upper_schel.attributes.put(upper_att.type_id, upper_att);
            }
            upper_att.name = "������� ���";
            upper_att.type_id = "optimizerNodeAttribute";
            upper_att.value = "restricted";

            node.active = false;
            node.fixed = true; // �������� "active" ������ ������
            //System.out.println("Element "+schel.id+" is not restricted but is a part of restricted element "+upper_schel.id);
         }
      }
      else // ���� ��� � ��������, �� ��������, ����� ��� ��� ���, ���� ����� - ����� EquipmentType
      {	 if(!upper_schel.proto_element_id.equals(""))// ������ ���� ��� �� �����, � ����� ���� ������� ������� (���� ����� ���� ������ � ���� ������������� �����)
         { ProtoElement proto = (ProtoElement)Pool.get(ProtoElement.typ, upper_schel.proto_element_id );
           EquipmentType eqtype = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipment_type_id );
           if( eqtype!=null && eqtype.eq_class.equals("mufta"))
           { upper_att = (ElementAttribute)upper_schel.attributes.get("optimizerNodeAttribute");
             if(upper_att==null) // ���� ������� �� ����������, �� ������ ��� � �����������  � ��� ��������
             { upper_att = new ElementAttribute();
               upper_att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ); //���������� ������������� ������� (��������� �� �������)
               upper_schel.attributes.put(upper_att.type_id, upper_att);
             }
             upper_att.name = "������� ���";
             upper_att.type_id = "optimizerNodeAttribute";
             upper_att.value = "restricted";

             node.active = false;
             node.fixed = true; // �������� "active" ������ ������
             //System.out.println("Element "+schel.id+" is not restricted but is a part of restricted element "+upper_schel.id);
           }
         }
      }
      //*************************
    }
    System.out.println("��������� ��������� ����������� � ����� -  ��������� ������� \n");
  }
//---------------------------------------------------------------------------------
// C � � � � � � � �     � � �     � � � � � �
private int processAllCables()
{   int wavelength = mdiMain.optimizerContext.wavelength;
    int nscl=0;// ������� ����������� ������
    String atten_str = "Attenuation_" + Integer.toString(wavelength);
    for( Enumeration cls = scheme.getAllCableLinks(); cls.hasMoreElements();) // �� ���� ������� ( �� ���� �� ����  ����� )
    {  SchemeCableLink scl = (SchemeCableLink) cls.nextElement();
       String scl_id = scl.id ;
       ElementAttribute att = (ElementAttribute)scl.attributes.get("optimizerRibAttribute");
       //���� ���� ���� ��� ��� �������� ��� �������� � ����, �� �� �������� ��� � dll, �� ���� ���  ��� ��� �� ����������
       if(att == null)
       { System.err.println("Link in cable " + scl_id + " has no optimization attribute. Impossible to start optimization.");
 return -1;
       }
       if( att.value.equals("tested") || att.value.equals("passive") )// ���������� � ����� ���������������� ������ ����������
    continue;

      double len_db = mdiMain.optimizerContext.calcCableLinkAttenuation(scl, wavelength);// len_db = length*attenuation - ����� � ���������
      if (len_db == -1)
      {  System.err.println("Failed getting signal loss for cable " + scl_id + " for wavelength" + String.valueOf(wavelength) + ". Impossible to start optimization.");
 return-1;
      }
      // ����� ����, ������� ��������� ������ ������:
      // � ����� ���� ��������, � ������ �������� ���� ������, � ������ ������� ���� �����
      // ���� ������� ����� �������� ������ � ��������� ������, �� ���� ������������� ������������� ����� ��������
      String n1_str_id = get_shemeElementId_by_cablePortId(mdiMain.scheme, scl.source_port_id);
      String n2_str_id = get_shemeElementId_by_cablePortId(mdiMain.scheme, scl.target_port_id);
      if(n1_str_id.equals("error"))
      { System.err.println("CreateGraphByMap(Scheme):incorrect node string-identificator. Impossible to start optimization.");
        javax.swing.JOptionPane.showMessageDialog(Environment.getActiveWindow(), "�������� ��������(����), ����������� ���� "+scl.source_port_id
            +", ����� ������������ ��������. ������ ����������� ����������.", "������ ���������� ���������� ����", javax.swing.JOptionPane.OK_OPTION);
 return -1;
      }
      if(n2_str_id.equals("error"))
      { System.err.println("CreateGraphByMap(Scheme):incorrect node string-identificator. Impossible to start optimization.");
        javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(), "�������� ����, ����������� ���� "+scl.source_port_id
            + ", ����� ������������ ��������. ������ ����������� ����������.", "������ ���������� ���������� ����", javax.swing.JOptionPane.OK_OPTION);
 return -1;
      }
      // ������������� �������� ������������ � ����������� �����, ���� ����������, �������� ����������� � �����
      double n1_len = Node.default_att, n2_len = Node.default_att; //��������� �� ��������� (����������� ����)
      String rib_str_id = scl.getId();
      //g.add_rib(n1_str_id - ������������� ������� ����, n1_len, n2_str_id, n2_len, len_db, rib_str_id - ������������� �����);
      if( len_db == 0 )
      {  System.err.println("processAllCables(): Length of cable with id = "+ rib_str_id + " equals 0");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(), "����� ������� � ������ " + scl.getName()
             +" ����� ����. ������ ����������� ����������.", "������������ �������� ����� ������", javax.swing.JOptionPane.OK_OPTION);
 return -1;
      }
      else
      { g.add_rib( n1_str_id, "cable", n1_len, n2_str_id, n2_len, len_db, rib_str_id );
        nscl++;
      }
    } //for( Enumeration cls = scheme.getAllCableLinks(); cls.hasMoreElements();)
   return nscl;// ���������� ����������� ��������� ������
}
//---------------------------------------------------------------------------------------
// � � � � � � � � �   � � �   � � � � � (�� ��������� ������ ���� ������������ �������� ������)
private int processAllLinks()
{   int nsl = 0;// ������� ����������� ������
    int wavelength = mdiMain.optimizerContext.wavelength;
    mdiMain.optimizerContext.unilinks.clear();// ������� ������ ���, ������� ���� ����� � ������
    for( Enumeration ls = scheme.getAllLinks(); ls.hasMoreElements();) // �� ���� ������
    { SchemeLink sl = (SchemeLink)ls.nextElement();
      String sl_id = sl.id ;
      ElementAttribute att = (ElementAttribute )sl.attributes.get("optimizerRibAttribute");
      if(att == null)
      { System.err.println("Link " + sl_id + " has no optimization attribute. Impossible to start optimization.");
 return -1;
      }
      if( att.value.equals("tested") || att.value.equals("passive") ) // ���������� � ����� ���������������� ������ ����������
    continue;
      double len_db = mdiMain.optimizerContext.calcLinkAttenuation( sl, wavelength);// len_db = length*attenuation - ����� � ���������
      if (len_db == -1)
 return -1;
     String rib_str_id = sl.getId();
     // ����� ����, ������� ��������� ������ ����:
     String n1_str_id = get_shemeElementId_by_portId(mdiMain.scheme, sl.source_port_id);
     String n2_str_id = get_shemeElementId_by_portId(mdiMain.scheme, sl.target_port_id);
     String unirib_str_id = isNodesLinked(n1_str_id, n2_str_id, rib_str_id);// unirib - ����, ������� ���������� ���� �� ������ ������ ����� ����� ���������
     if(!unirib_str_id.equals("")) // ���� ��� ��� �������� ��� ��������� ������ (������ ������), �� �� ��������� ������ �����
     {  //System.out.println("CreateGraphByMap(Scheme):����� ������������ "+n1_str_id+" � "+n1_str_id+" ��� ���������� ���� "+unirib_str_id+". ����������� ���� "+rib_str_id+" ��������������");
        // ���������  n1 � n2 ��� ��������� unirib_str_id, � ���� ������ ���� rib_str_id, �� ��� ������� ����� ���� ����� � ������
        if(!mdiMain.optimizerContext.unilinks.contains(unirib_str_id))
        { mdiMain.optimizerContext.unilinks.add(unirib_str_id);
        }
     }
     else // ��������� ����� �����
     { // ���� ����� ���� ��� ���, �� ���������� ��� � ���� �������
       links.add(n1_str_id);links.add(n2_str_id); links.add(rib_str_id);// ����� �������� "�������-�������-����"
       if(n1_str_id.equals("error"))
       { System.err.println("CreateGraphByMap(Scheme): Incorrect string of node identificator. Impossible to start optimization");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(), "�������� �������� �����, ����������� ���� "+sl.source_port_id
             +", ����� ������������ ��������. ������ ����������� ����������.", "������������ �������� �����", javax.swing.JOptionPane.OK_OPTION);
 return -1;
       }
       if(n2_str_id.equals("error"))
       { System.err.println("CreateGraphByMap(Scheme): Incorrect string of node identificator. Impossible to start optimization");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(), "�������� ����, ����������� ���� "+sl.source_port_id
             +", ����� ������������ ��������. ������ ����������� ����������.", "������������ �������� �����", javax.swing.JOptionPane.OK_OPTION);
 return -1;
       }
       // ������������� �������� ������������ � ����������� �����, ���� ����������, �������� ����������� � �����
       double n1_len = Node.default_att, n2_len = Node.default_att; //��������� �� ��������� (����������� ����)
       // ������ � ������ ����������: ���� ����� ����� = 0, �� ������ ��� ��������� ���� � ����� (�� ���� ������)
       if( len_db == 0 )
       { g.add_rib(n1_str_id, "fiber", n1_len/2, n2_str_id, n2_len/2, len_db, rib_str_id);
         nsl++;
       }
       else
       { g.add_rib(n1_str_id, "fiber", n1_len, n2_str_id, n2_len, len_db, rib_str_id);
         nsl++;
       }
     }
    } //for( Enumeration ls = scheme.getAllCableLinks(); ls.hasMoreElements();)
    return nsl;
}
  //-------------------------------------------------------------------------
  // ���� : �����, ��������� ������������� �����
  // �����: ��������� ������������� ��������, ���������� � ���� ���������� � ������, ���������
  // ������������� �������� ������� �� �����
  public String get_shemeElementId_by_cablePortId(Scheme scheme, String port_id)
  { String id = "error";
    SchemeElement schel = scheme.getSchemeElementByCablePort(port_id);
    if( schel != null)
    { //SchemeElement topLevelElement = scheme.getTopLevelNonSchemeElement(schel);
      //if(topLevelElement != null){ id = topLevelElement.getId();}
      id = schel.getId();
    }
    return id;
  }
 //-------------------------------------------------------------------------
 public String get_shemeElementId_by_portId(Scheme scheme, String port_id)
 { String id = "error";
   SchemeElement schel = scheme.getSchemeElementByPort(port_id);
   if( schel != null)
   { //SchemeElement topLevelElement = scheme.getTopLevelNonSchemeElement(schel);
     //if(topLevelElement != null){ id = topLevelElement.getId();}
     id = schel.id;//���� id �� ������ �������� ��������, � ������ ����, � ������� ��������������� �������� ������ ��� ����
   }
   return id;
 }
 //-------------------------------------------------------------------------
 // ���� ��������� ���� ������ ��� ��������� (����������� �������� ���� �-���), �� ���������� ��� �����, ������� ���������.
 // ���� ���, �� ��������� ��������� ���� ������ � ������ � false
 // ���������� � g ���� �� ��������, ��� ��� ��� ���������� ������ ������������� _�����_. ������ ����� ������������� ��� �����������.
 private String isNodesLinked(String n1, String n2, String r)
 { String result = "", link_name, node1, node2;
   for(Iterator ils = links.iterator(); ils.hasNext(); ) // ils - Iterator of LinkS
   { node1 = (String) ils.next(); node2 = (String)ils.next(); link_name = (String)ils.next();
     if( (node1.equals(n1)&&node2.equals(n2)) || (node1.equals(n2)&&node2.equals(n1)) ) //���� �������������� => ������� �� �����
     {  result = link_name;// ��� �����
   break;
     }
   }
   return result;
 }
 //-------------------------------------------------------------------------
}
//======================================================================================================================
