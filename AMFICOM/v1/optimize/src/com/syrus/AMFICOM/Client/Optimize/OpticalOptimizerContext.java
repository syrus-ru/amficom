// � ���� ������ ���������� ��� �������, ����������� ��� ���� , ����� ������������������� �������
// �����������: �������� � Dll ��������� ����, ���������� ��������� ���� ����������� � � �
// � ����� ���������� ��� ������ � ������ �������� �����������: ��� ���������, �����, �������� ������������ � � �

package com.syrus.AMFICOM.Client.Optimize;

import java.util.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;


public class OpticalOptimizerContext
{
    // ����������
    public int topology_set = 0;         // �������� �� � dll ��������� ���� ( ������ ����� ������ ����� ����� )
    public int prices_set = 0;           // ������� �� � dll ������ ��� �� ������������
    public int wavelength_set = 0;       // ����������� �� ����� ����� ������������ �������
    public int graph_set = 0;            // ������ �� ���� �� ������ ���� (� ������ ���� �� ������� ����� ����� ����������� ����� ���� ������, �� ����������� ����� �� �� �������)
    public int is_in_progress = 0;       // ���� ��������� �������� �����������
    public boolean once_started = false; // ����, ��� �� ���� ��� ������� ������� �����������

    public int wavelength = -1;    // ����� �����
    public int optimize_mode = 0;  // ����� ����������, 0 - ������������, 1 - ���������

    public OptimizeMDIMain mdiMain;// ������� ����
    public Solution solution;      // �������
    public DllAdapter dllAdapter;  // �����-������� ��� �������� ��������� � dll-���
    public OptimizeParameters opt_prs = new OptimizeParameters();
    public double iterations;      // ���������� ��������, ��������� � ������� ������������
    public ArrayList original_paths;// ������ �����, ������� ���������� ���� �� �����
    public ArrayList originally_lconnected_nodes; // ���������� ���������� ������� path-� ���� � ���� ����� {node1.id, node2.id, link.id}
    public double[] refl_prices;
    public double[] switch_prices;

    // ��������� �����������
    String[] param_descriptions = new String[] {
              "����������� ������",
              "������� ������������", // ����������� ������� (�������� �������� mutation_rate ����� �� ������ ����� ������)
              "������� ������������", // ���������� ��������������� ����� � ������� � ��� ����� , ������� ����������� �������
              "P �������� ���",
              "P �������� ���",
              "P �������� �������",
              "P ������� �������",
              "����������������"
             };
    Double[][] param_values = new Double[][]
       { {new Double(opt_prs.len_margin)},
         {new Double(opt_prs.mutation_rate)},
         {new Double(opt_prs.mutation_degree)},
         {new Double(opt_prs.rtu_delete_prob)},
         {new Double(opt_prs.rtu_create_prob)},
         {new Double(opt_prs.nodes_splice_prob)},
         {new Double(opt_prs.nodes_cut_prob)},
         {new Double(opt_prs.survivor_rate)}
      };
    // �������� ������, ����� ������� ���� ����� � ������
    Vector unilinks = new Vector();
    //--------------------------------------------------------------------------------------------------------------
    public OpticalOptimizerContext(OptimizeMDIMain mdiMain)
    { this.mdiMain = mdiMain;
    }
    //--------------------------------------------------------------------------------------------------------------
    //�������� � Dll ��������� ����. ����: �����.
    public void setTopology(MapContext map_c){}
    //--------------------------------------------------------------------------------------------------------------
    // �������� � Dll ��������� �����������   !!! �������� � ������ �������
    public void setParameters(OptimizeParameters param)
    {
    }
    //--------------------------------------------------------------------------------------------------------------
    // �������� � Dll ������� ����������� ���� �� ������� ��������� ���
    public void setOptimizationBase(RTUInfo base[]){}//price, range, distance
    //--------------------------------------------------------------------------------------------------------------
    public void setPricelist()
    { if(mdiMain == null)
      {  System.err.println( "setPricelist(): mdiMain == null. Setting aborting..." );
      }
      else
      { setPricelist(mdiMain.kisSelectFrame);
      }
    }
    //--------------------------------------------------------------------------------------------------------------
    public void setOptimizationParam(int row , double value)
    {  opt_prs.setOptimizationParam(row, value); //������ � �������
       dllAdapter.setOptimizationParam (row, value); // ������ � dll
    }
    //--------------------------------------------------------------------------------------------------------------
    // �������� � Dll ������� ����������� ���� �� ������� ��������� ��� � ���������� ������ �� �����
    // ����� �������������� ��������� ������� refl_prices � switch_prices ������ �� �������
    public void setPricelist(KISselectionFrame kisSelect)
    {	// ���� ��������� � Dll ������ ���� ��� - �� ������� "������ �����������"
      if(prices_set != 0)
    return;
      Vector v = new Vector();
      // ��� ��������������
      v.clear();
      double wls = (double)wavelength; // wls - wavelength selected
      // �� ���� �������������� �������� ������ ��, ������� �������� �� ��������� ����� ����� wls
      for( Enumeration rsds=kisSelect.reflectometers.elements() ; rsds.hasMoreElements();)//reflectometers - ������ ��  ReflectometerShortData(rID, rn, rwl, rdd, rp)
      { ReflectometerShortData refl = (ReflectometerShortData)rsds.nextElement();//������������ ( ������� ����� )
        for(int j=0; j<refl.wavelength.size(); j++ )
        {  if( Double.parseDouble( (String)refl.wavelength.elementAt(j) ) == wls )// ���� ������ ������������� ����� �������� �� ��������� ����� �����
           { String dw = refl.dynamicWindow.elementAt(j).toString();
             String pr = refl.price;
             v.add( new String(dw) ); v.add( new String(pr) );
           }
        }
      }
      int j=0, size = v.size();
      while(  j<size && v.size()>0)// ������� ������ � ������������� ���� ( ������������� ��������� ������ ����, ���������� "?" )
      { if ( v.elementAt(j).equals("") || v.elementAt(j+1).equals("")
             || ((String)v.elementAt(j)).indexOf("?")!= -1  || ((String)v.elementAt(j+1)).indexOf("?")!= -1
           )
       {	 v.remove(j); v.remove(j);// ���� �������� �� ������� ��������, �� �������� ��������� � ������� �� ����� ���������
           size = v.size();
       }
       else
       {  j+=2; //������ ���� ������
       }
      }
      if(v.size() == 0)// ���� ��� ���
      {	 //javax.swing.JOptionPane.showMessageDialog (Environment.getActiveWindow(),"���� �������������� �� ������ ", "������ ������� ���", javax.swing.JOptionPane.OK_OPTION);
  return;
      }
      // ����������� �� String � double
      size = v.size();
      for(int i=0; i<size; i++)
      {  double d = Double.parseDouble( (String) v.elementAt(i) );
         v.set(i, new Double(d) );// ��������
      }
      // �������� ������ double � ������� double
      if (v.size() != 0)
      { Double[] Arr = ( (Double[] ) v.toArray( new Double[0]) );
        double[] arr = new double[v.size()];
        for(j=0; j<v.size(); j++){ arr[j] = Arr[j].doubleValue(); }
        dllAdapter.SetReflPricelist(arr);
        //����������� � ��������� ������
        refl_prices = new double[v.size()];
        for(j=0; j<v.size(); j++){ refl_prices[j] = arr[j]; }
      }
      // ��� ������������
      v.clear();
      v.add (new String("1") ); v.add( new String("0"));// ������ ����������� ��������� �� ����� (������������ ���)
      for(int i=0; i<kisSelect.switchers.size(); i++)
      {   String pn = ( (SwitcherShortData)(kisSelect.switchers.elementAt(i)) ).portNumber;
          String pr = ( (SwitcherShortData)(kisSelect.switchers.elementAt(i)) ).price;
          v.add( new String(pn) ); v.add( new String(pr) );
      }
      j=0; size=v.size();
      while(  j<size && v.size()>0 )
      { if ( v.elementAt(j).equals("") || v.elementAt(j+1).equals("") )
        {	 v.remove(j); v.remove(j);// ���� �������� �� ������� ��������, �� �������� ��������� � ������� �� ����� ���������
           size = v.size();
        }
        else
        {  j+=2;// ���� �������� �� ������� ��������, �� �������� ��������� � ������� �� ����� ���������
        }
      }
      if(v.size() <= 1 )// ���� ��� ��� ("=1" ���� ������ ���� 0 ��� ������ �����)
      {	 //javax.swing.JOptionPane.showMessageDialog (Environment.getActiveWindow(),"���� ������������ �� ������ ", "������ ������� ���", javax.swing.JOptionPane.OK_OPTION);
  return;
      }
      // ����������� �� String � double
      size = v.size();
      for(int i=0; i < size; i++)
      {  double d = Double.parseDouble( (String) v.elementAt(i) );
         v.set( i, new Double(d) );
      }
      // �������� ������ double � ������� double
      if (v.size() != 0)
      { Double[] Arr = ( (Double[] ) v.toArray( new Double[0]) );
        double[] arr = new double[v.size()];
        for(j=0; j<v.size(); j++){ arr[j] = Arr[j].doubleValue(); }
        dllAdapter.SetCrossPricelist(arr);
        //����������� � ��������� ������
        switch_prices = new double[v.size()];
        for(j=0; j<v.size(); j++){ switch_prices[j] = arr[j]; }
      }
      prices_set = 1;
    }
    //----------------------------------------------------------------------------------------------------------------
    // ���������� ������ �����, ������ �� ������� ��������� ���� ���� ������������, � ������ ��� ��������� ��� ��������� ������������
    public Vector getSolutionInString()
    {	Vector pathElementStrId = new Vector();// ������ ��������� ��������������� ������ ���� ���� ������������ (������� ��� �����)
      Vector transmissionPath = new Vector();// ���� ������������, ��������� �� transmittionElementStrId  �����, ����������� �� ������ ���� ������������

      String s = "";// s = getName() + " : ";
      Vector v = null; // v = physicalLink_ids;
      for(int i = 0; i < v.size(); i++)// �� ����� ������� ������
      {	String link_id = (String )v.elementAt(i);
        s += Pool.getName(SchemeCableLink.typ, link_id);
        if(i != v.size() - 1 )
          s += " -> ";
      }

      return transmissionPath;
    }
    //--------------------------------------------------------------------------------------------------------------
    // ������\���������� ������� �����������
    public void Start()
    {  dllAdapter.Run();
       is_in_progress = 1;
    };
    //--------------------------------------------------------------------------------------------------------------
    // ������������� ������� �����������
    public void Stop()
    {  dllAdapter.Suspend();
       is_in_progress = 0;
       // ���������� ������ �������
       solution = getCurrentBestSolution();// ������ Solution �������� ������ ����� � ��� �������� ( ������ ����� )
       iterations = (double)dllAdapter.GetIterationCounter();
       // ���, ���� ����, ������� ��� ������� � ���������� �������, ���� ��� �� mdiMain
       mdiMain.getInternalDispatcher().notify(new OperationEvent(this, 0, "solution_updated_event"));
    };
    //--------------------------------------------------------------------------------------------------------------
    // ������� ������ �������
    public double GetBestPrice()
    {  return dllAdapter.GetBestPrice();
    }
    //--------------------------------------------------------------------------------------------------------------
    //������� 0-� ������� ��� ������ dllAdapter-a
    public Solution getCurrentBestSolution()
    {  return new Solution(dllAdapter, (int)0);//������� 0-� ������� ��� ������ dllAdapter-a
    }
    //--------------------------------------------------------------------------------------------------------------
    // ������� index-� ������� ��� ������ dllAdapter-a
    public Solution getSolutionByIndex(int index)
    { return new Solution(dllAdapter, (int)index);//������� index-� ������� ��� ������ dllAdapter-a
    }
    //--------------------------------------------------------------------------------------------------------------
    // �������� �������
    public void drawSolution (Solution  solution)
    { dllAdapter.g2m.CreateMapByGraph(solution);
    }
    //--------------------------------------------------------------------------------------------------------------
    // ������� ��������� �������� �����������
    public int is_in_progress(){return is_in_progress;}
    //--------------------------------------------------------------------------------------------------------------
    // ��������� ��������� � ������ �� ��������� ����� �����
    public double calcCableLinkAttenuation( SchemeCableLink scl, int wavelength)
    {  double len_db = -1;
       double length = -1, attenuation = 0;
       String atten_str = "Attenuation_" + Integer.toString(wavelength);

       if(scl!=null){ length = scl.getOpticalLength()/1000;}
       else
       { System.err.println( "Error in CreateGraphByMap(...): SchemeCableLink = null");
         javax.swing.JOptionPane.showMessageDialog(Environment.getActiveWindow(),
             "� ����� ��� �� ����� ����� �����", "������ ������", javax.swing.JOptionPane.OK_OPTION);
    return -1;
       }
       // ��������� ������ ���� �� ��������� ������ �� ��� ������� - ��������( ������� , ��� �������������� ���� ������� � ����� ������ ����������
       if(scl.cable_threads == null)
       { System.err.println("Error in CreateGraphByMap(...): SchemeCableLink - scl.cable_threads = null");
       }
       if(scl.cable_threads.size() == 0)
       { System.err.println("Error in CreateGraphByMap(...): SchemeCableLink - scl.cable_threads.size() = 0");
         javax.swing.JOptionPane.showMessageDialog(Environment.getActiveWindow(),
             "� ������ "+scl.getName()+" �� ������ �������", "������ ���������� ���������� ����", javax.swing.JOptionPane.OK_OPTION);
    return -1;
       }
       SchemeCableThread thr = (SchemeCableThread)scl.cable_threads.iterator().next();
       LinkType cl_type = (LinkType)Pool.get(LinkType.typ, thr.link_type_id);
       if(cl_type!=null)
       { attenuation = Double.parseDouble( ( (Characteristic)cl_type.characteristics.get(atten_str)).value);
       }
       else
       { System.err.println("CreateGraphByMap(Scheme): Attenuation " + atten_str + " in cable thread "+scl.getName() + " is not set. Impossible to start optimization.");
         javax.swing.JOptionPane.showMessageDialog(Environment.getActiveWindow(),
             "������ " + atten_str + " � ������� ������ " + scl.getName() + " �� ������.\n ������ ����������� ����������.",
             "������ ���������� ���������� ����", javax.swing.JOptionPane.OK_OPTION);
    return -1;
       }
       if( attenuation<0 )
       { System.err.println("CreateGraphByMap(Scheme): Attenuation " + atten_str
                            +" in cable thread " + scl.getName()+" ="+attenuation + ". Impossible to start optimization.");
         javax.swing.JOptionPane.showMessageDialog(Environment.getActiveWindow(), "������ "+atten_str+" � ������� ������ "
             +scl.getName()+" �� ������ \n ������ ����������� ����������.", "������ ������", javax.swing.JOptionPane.OK_OPTION);
    return -1;
       }
       len_db = length*attenuation;
       return len_db;
    }
    //--------------------------------------------------------------------------------------------------------------
    // ��������� ��������� �����
    public double calcLinkAttenuation( SchemeLink sl, int wavelength)
    {  double length = -1, attenuation = 0;
       double len_db = -1;// len_db = length*attenuation - ����� � ���������
       String atten_str = "Attenuation_" + Integer.toString(wavelength);
       if(sl!=null)
       { length = sl.getOpticalLength()/1000;
       }
       else
       { System.err.println( "CreateGraphByMap(...): SchemeLink = null");
         javax.swing.JOptionPane.showMessageDialog(Environment.getActiveWindow(),
             "������ ���������� ����� �����.\n ������ ����������� ����������.", "������ ���������� ����", javax.swing.JOptionPane.OK_OPTION);
    return -1;
       }

       if(!sl.link_id.equals("")) // ���� ��� �� ���������� �������
       { Link l =(Link)Pool.get(Link.typ, sl.link_id);
         if(l!= null && l.characteristics.get(atten_str) != null)
         {  attenuation = Double.parseDouble( ((Characteristic)l.characteristics.get(atten_str)).value );
         }
       }
       else // ���� ����������, �� ���������� �� ����
       { LinkType l_type = (LinkType)Pool.get(LinkType.typ, sl.link_type_id);
         if(l_type!=null)
         { attenuation = Double.parseDouble( ((Characteristic)l_type.characteristics.get(atten_str)).value );
         }
         else
         { System.err.println("CreateGraphByMap(Scheme): Attenuation "+atten_str + " in thread "+sl.getName() + " is not set. Impossible to start optimization.");
           javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(), "������ "+atten_str+" � ������� "+sl.getName()
               + " �� ������.\n ������ ����������� ����������.", "������ ���������� ���������� ����", javax.swing.JOptionPane.OK_OPTION);
    return -1;
         }
       }
      if(attenuation == -1)
      { System.err.println("CreateGraphByMap(Scheme): Attenuation " + atten_str
                           +" in thread " + sl.getName()+" is not set. Impossible to start optimization.");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(), "������ "+atten_str+" � ������� "
            +sl.getName()+" �� ������.\n ������ ����������� ����������.", "������ ������", javax.swing.JOptionPane.OK_OPTION);
    return -1;
      }
      else if(attenuation < 0)
      { System.err.println("CreateGraphByMap(Scheme): Attenuation " + atten_str
                          +" in thread " + sl.getName()+" =" + attenuation + ". Impossible to start optimization.");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(), "������ "+atten_str+" � ������� "
            +sl.getName()+" ����� " + attenuation + ".\n ������ ����������� ����������.", "������������ �������� ���������", javax.swing.JOptionPane.OK_OPTION);
    return -1;
      }

      len_db = length*attenuation;
      return len_db;
    }
  //------------------------------------------------------------------------------------------------------
  public void setCableLinksOptimizeAttributes()
  { for( Iterator cls = mdiMain.scheme.getAllCableLinks().iterator(); cls.hasNext();) //�� ���� ������� ( �� ���� �� ����  ����� )
    { SchemeCableLink scl = (SchemeCableLink)cls.next();
      String scl_id = scl.id;
      ElementAttribute att = (ElementAttribute)scl.attributes.get("optimizerRibAttribute");
      //���� ���� ���� ��� ��� �������� ��� �������� � ����, �� �� �������� ��� � dll, �� ���� ���  ��� ��� �� ����������
      if(isLinkTestedBefore(scl_id))
      { if(att==null)
        { att = new ElementAttribute();
          att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
          att.name = "����������";
          att.type_id = "optimizerRibAttribute";
          att.value = "tested";
          scl.attributes.put(att.type_id, att);
        }
        else
        { att.value = "tested";
        }
    continue;
      }
      else
      { if(att==null) // ���� ������� �� ����������, �� ������ ��� � ����������� active
        { //System.out.println("CreateGraphByMap(Scheme): ������� ����������� optimizerRibAttribute ������� " + scl_id + " �� �����. ������ �������������.");
          att = new ElementAttribute();
          att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
          att.name = "����������";
          att.type_id = "optimizerRibAttribute";
          att.value = "active"; // �� ��������� ����� active
          scl.attributes.put(att.type_id, att);
        }
        else
        { att.value = "active";
        }
      }
    }
  }
  //------------------------------------------------------------------------------------------------------
  public void setLinksOptimizeAttributes()
  { for( Iterator ls = this.mdiMain.scheme.getAllLinks().iterator(); ls.hasNext();) // �� ���� ������
    { SchemeLink sl = (SchemeLink)ls.next();
      ElementAttribute att = (ElementAttribute )sl.attributes.get("optimizerRibAttribute");
      // ������� ����� ����������, ������� �� ��� "passive"
      if( isLinkRtuConnected(sl) )
      { if(att==null)
        { att = new ElementAttribute();
          att.id = this.mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
          att.name = "����������";
          att.type_id = "optimizerRibAttribute";
          att.value = "passive";
          sl.attributes.put(att.type_id, att);
        }
        else{att.value = "passive";}
    continue;
      }
      // ��� ���������������� ����� ����������, ������� �� ��� "tested"
      if( isLinkTestedBefore(sl.getId()) )
      { if(att==null)
        { att = new ElementAttribute();
          att.id = this.mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
          att.name = "����������";
          att.type_id = "optimizerRibAttribute";
          att.value = "tested";
          sl.attributes.put(att.type_id, att);
        }
        else{ att.value = "tested";}
    continue;
      }
      // ����������� ����� ����������, ������� �� ��� "passive"
      if( isLinkNodesConnectedBefore(sl) )
      { if(att==null)
        { att = new ElementAttribute();
          att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
          att.name = "����������";
          att.type_id = "optimizerRibAttribute";
          att.value = "tested";
          sl.attributes.put(att.type_id, att);
        }
        else{ att.value = "tested";}
    continue;
      }
      // ���� ���� ����� �����������, �� ������� �� ����������, �� ������ ��� � ����������� active
      if( att == null )
      { //System.out.println("CreateGraphByMap(Scheme): ������� ����������� optimizerRibAttribute ������� " + sl_id + " �� �����. ��������������� ��������� �� ���������.");
        att = new ElementAttribute();
        att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
        att.name = "����������";
        att.type_id = "optimizerRibAttribute";
        att.value = "active";// �� ��������� ����� active
        sl.attributes.put(att.type_id, att);
      }
    }
  }
  //---------------------------------------------------------------------------
  // ���������, ������������ �� ������ ���� � ����� ���
  private boolean isLinkRtuConnected (SchemeLink sl)
  { boolean res = false;
    SchemePort sp = (SchemePort)Pool.get(SchemePort.typ, sl.source_port_id);
    if(!sp.access_port_type_id.equals(""))
    { res = true;
    }
    if(!res) // ���� ���� ���� �� ������ � ���, �� ���� ��������� ��� � ������ ����
    { sp = (SchemePort)Pool.get(SchemePort.typ, sl.target_port_id);
      if(!sp.access_port_type_id.equals(""))
      { res = true;
      }
    }
    return res;
  }
  //-------------------------------------------------------------------------
  //��������� ��� �� ���� ���� ��� ��������� ���� � ����� id ��� �������� ��� �������� � �����-���� ����
  // ��������� ��������������� ����������� ������ � ������ � ������ ����������� (����� ������) !!! ���� ��� �� ���������
  public boolean isLinkTestedBefore (String link_id)
  { boolean res = false;
    if(original_paths == null)
  return res;
    for( Iterator paths = original_paths.iterator(); paths.hasNext();) // �� ���� �����
    { SchemePath sp = (SchemePath)paths.next();
      for(Iterator links = sp.links.iterator(); links.hasNext();)
      { PathElement link = (PathElement)links.next();
        if(link.link_id.equals(link_id))
        {  res = true;
      break;
        }
      }
      if(res)
    break; // for(Enumeration links = sp.links.elements(); links.hasMoreElements();)
    }
    return res;
  }
  //------------------------------------------------------------------------------------------------------
  // ���� �� ��� ������� ����, ������� ����������� ������, ������ ������ �� ����, ������� ��� ���������� �������� ������ �� ������
  public boolean isLinkNodesConnectedBefore(SchemeLink sl)
  { boolean res = false;

    String se1_id = ((SchemeElement)this.mdiMain.scheme.getSchemeElementByPort(sl.source_port_id)).id;
    String se2_id = ((SchemeElement)this.mdiMain.scheme.getSchemeElementByPort(sl.target_port_id)).id;
    String ose1_id, ose2_id, osl_id;
    for(Iterator s = originally_lconnected_nodes.iterator(); s.hasNext(); )
    { ose1_id = (String) s.next();  ose2_id = (String) s.next();  osl_id = (String) s.next();
      if( (ose1_id.equals(se1_id)&&ose2_id.equals(se2_id)) ||  (ose1_id.equals(se2_id)&&ose2_id.equals(se1_id)) )
      {  res = true;
    break;
      }
    }
    return res;
  }
  //------------------------------------------------------------------------------------------------------
}
