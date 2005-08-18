package com.syrus.AMFICOM.Client.Optimize;

import java.util.Vector;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Optimize.SolutionCompact;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
// ����� ������ ��� ���������� �� ����� �������
// ����������� ����� �������� ��������� ������� �� �����
//======================================================================================================================
public class Solution
{
  public String id = "";
  public String name = "";
  public String scheme_id; // ������������� ����� , � ������� ��� ������� ���������
  public String typ = "SM_solution";
  public int index;   // ����� ������� �� ������ ��������
	public double price;// ����
	private DllAdapter dllAdapter;
	private ViewSchemeFrame schemeFrame;// ���� � ������� ��������� ������ ������ �� ������ schemePanel
	// ���������� �� ���������������� ��������� ���� ����������� � ������� ��� ����
	public Vector paths;       // ������ ����� SchemePaths
	private int pathnumber = 0;// ����������� ��������� �����
	//--------------------------------------------------------------------------------------------------------------
  public Solution ()
  {  scheme_id = "not defined";
     index = -1;
     price = -1;
     paths = new Vector();
  }
  //--------------------------------------------------------------------------------------------------------------
	// ����������� ��������� ���� paths �������� �� schemePath-��
	public Solution (DllAdapter dllAdapter, int index)
	{	this.index = index;
    this.dllAdapter = dllAdapter;
    OptimizeMDIMain mdiMain = dllAdapter.mdiMain;
    id =  mdiMain.aContext.getDataSourceInterface().GetUId(this.typ);
    scheme_id = mdiMain.scheme.id;
		if(index == 0){	price = dllAdapter.GetBestPrice(); }
    else { price = -1; }

		double topology[] = DllAdapter.getNetMonitoringTopology(index);// ��������� ����������� � �������� �������, 0-������ �������
		String r_id = "", r_type = "";
		SchemePath schemePath = new SchemePath("");// ���� ���� ������������
		paths = new Vector(); // ������ ���� ����� (����� ������� ��� ���������� � �����)
		 // ������������� ���� ������ (������ ���� �������), �� ���� ������������� ����, � ������� ����� ���
		schemePath.start_device_id = ( (Node)dllAdapter.m2g.g.nodes.get((int)topology[1]) ).str_id ;
    Rib r;
		for(int i=1; i<topology.length; i+=2)
		{ if(topology[i+1] != -1)// ��������� � ���� ����� ��������
			{ r = (Rib)dllAdapter.m2g.g.ribs.get((int)topology[i+1]) ;
        r_id = r.str_id ; // ������������� ����� (�����)
        r_type =   r.type ; // ��� ����� (������ ��� ���� �������)
				if(r_type.equals("fiber"))// ���� ��� �������(�� ��������� ����)
        { SchemeLink sl = (SchemeLink)Pool.get(SchemeLink.typ, r_id);
          addLink(schemePath, sl.id);
        }
        else if(r_type.equals("cable"))// ���� ��� ��������� ����
        { SchemeCableLink scl = (SchemeCableLink)Pool.get(SchemeCableLink.typ, r_id);//!!!
          // �������� ! ���� �� ������ ����� 0� ������� ��� ��������� ������� (������-�� ��� ������ �������� ������������)
          addCableLink(schemePath, scl.id,( (SchemeCableThread)scl.cable_threads.get(0)).id);
        }
			}
			else//���� ������ ���� ��������, �� ��������� ��� � ������ �����
			{  // ������������� ���� ����� (���� ����� ������)
				 schemePath.end_device_id = ( (Node)dllAdapter.m2g.g.nodes.get((int)topology[i]) ).str_id ;
         // ��������� ������ �� ������ ����
         double loss = topology[i+2];// ... , ����� ���� ����� �������� (i), -1 (i+1), ������ (i+2), -2 (i+3), ...
				 i += 2;
				 ElementAttribute attr = new ElementAttribute();
				 {	attr = new ElementAttribute();
						attr.id = dllAdapter.mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);// �������� ���������� �������������
						attr.name = "������ ������ �� ��������";
						attr.type_id = "roughly_estimated_path_loss";
						attr.value = String.valueOf(loss);
						schemePath.attributes.put(attr.type_id, attr);
				 }
				 savePath(schemePath); // ��������� ���� � ������� ����� paths

				 schemePath = new SchemePath("");// ������ ����� ���� ����� ������ ������� (����� ���������� ��� ��� ���������� � savePath(SchemePath schemePath) )
				 if(i+2 < topology.length)
				 { // ������������� ���� ������ (������ ���� �������)
					 schemePath.start_device_id = ( (Node)dllAdapter.m2g.g.nodes.get((int)topology[i+2]) ).str_id ;
				 }
			}// else
		}// ������ � ��� ���� paths
	}
	//--------------------------------------------------------------------------------------------------------------
  // ������� Solution �� ������ �� ���� ���� , � ������� ��� �������� � �� (SolutionCompact)
  public Solution (SolutionCompact slc)
  { id = slc.id;
    name = slc.name;
    scheme_id = slc.scheme_id; // ������������� ����� , � ������� ��� ������� ���������
    typ = slc.typ;
    price  = slc.price;

    paths = slc.paths;
  }
  //--------------------------------------------------------------------------------------------------------------
	// �������� ������� CableLink(������) � ����
	private void addCableLink(SchemePath schemePath, String cable_id, String thread_id)
	{	PathElement pe = new PathElement();
		pe.is_cable = true;
		pe.n = schemePath.links.size(); // ���� ��������� ����� ���������
		pe.link_id = cable_id;          // ���� ��������� ����� ���������
		pe.thread_id = thread_id;       // ��������� �� ����, ��� ������� ����������� �-�
		schemePath.links.add(pe);
	}
  //--------------------------------------------------------------------------------------------------------------
  // �������� ������� Link(�������) � ����
  private void addLink(SchemePath schemePath, String link_id)
  {	PathElement pe = new PathElement();
    pe.is_cable = false;
    pe.n = schemePath.links.size();
    pe.link_id = link_id;
    pe.thread_id = "";//  � ������ ������� ( ���� ���  �� � ������ ) thread_id ������ ������ , �� �� null
    schemePath.links.add(pe);
  }
	//--------------------------------------------------------------------------------------------------------------
	private void savePath(SchemePath schemePath)
	{ schemePath.id = dllAdapter.mdiMain.aContext.getDataSourceInterface().GetUId(SchemePath.typ);
		schemePath.name = "���� " + Integer.toString(++ pathnumber);
		schemePath.type_id = "refpathtype";// refpathtype - ������������� ����������� ����
		paths.add(schemePath);
	}
	//--------------------------------------------------------------------------------------------------------------


}