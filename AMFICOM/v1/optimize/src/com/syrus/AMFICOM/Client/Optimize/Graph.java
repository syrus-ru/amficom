package com.syrus.AMFICOM.Client.Optimize;

import java.util.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

// �����, ���������� �������������� �������� ��������� �����, ������� � �����
//===========================================================================
public class Graph
{ // ��������� ����� ��� ������� � ������� ����� � ��������� id ( ��������� c ���� )
	public ArrayList ribs = new ArrayList(); // ������������ ������ ����� ����� (��� ����� ����� ����� ���� � �����).
	public ArrayList nodes = new ArrayList();// ������������ ������ ������ ����� (��� ����� ����� ����� ������ � �����).
	//-------------------------------------------------------------------------
	public Graph(){}
	//-------------------------------------------------------------------------
	// ���� : ������������ �������
	// �����: ������� � ������ ���������������
	public Node node_str_id_2_node(String str_id)
	{ Node n = null;
		boolean exist = false;

    for(Iterator nds = nodes.iterator(); nds.hasNext();)
    { Node node = (Node)nds.next();
      if( node.str_id.equals(str_id) )
      {  n = node;
         exist = true;
      }
    }
		if(!exist)
		{ System.err.println("Node "+ str_id +" does not exist in nodes[] ");
		}
		return n;
	}
	//-------------------------------------------------------------------------
	// ���� ������� � ����� ��������� ��������������� ��� ���, �� ��� ��������, ���� ��� ����, �� �����
	public int add_node(String node_str_id, double attenuation)
	{ boolean exist = false;
		int id  = -1;
    Node n = null;
    for( Iterator nds = nodes.iterator(); nds.hasNext();)
    { n = (Node)nds.next();
      if( n.str_id.equals(node_str_id) )
       {  exist = true;
          id = n.id;
          break;
       }
    }
		if( !exist )//���� ����� ������� ���, �� ��������� � (������ � ���� ���� ����)
		{ //������������� ��������������� (�������� � ���� - ��� �����, ��� ��� � Dll ���� ���������� � ����)
			id = nodes.size();//������������� ����� ����� ������� ��� ��������� � �������!
			Node node = new Node(node_str_id, id, attenuation);
			nodes.add(node);
		}
		return id;
	}
	//-------------------------------------------------------------------------
	// ���� : ������������� �����
	// �����: ����� � ������ ���������������
	public Rib rib_str_id_2_rib(String str_id)
	{	Rib r = null;
		boolean exist = false;

    for(Iterator rbs = ribs.iterator(); rbs.hasNext();)
    { r = (Rib)rbs.next();
      if( r.str_id.equals(str_id))
      {  exist = true;
         break;
      }
    }
  	if(!exist){System.out.println("Warning: Rib "+ str_id +" does not exist in ribs[] ");}

		return r;
	}
//-------------------------------------------------------------------------
	//  �������� ����� � ��������� � ����� � ����
	//  ���������: ��� ������� - ����� �����, ��������� � ������ �������, ����� ����� � ��� ��������� �������������
	//  ����������: �������� ������������� �����
	public int add_rib (String n1_str_id, String type, double n1_len, String n2_str_id, double n2_len, double length, String rib_str_id)
	{ // ���� ����� � ����� ��������� ��������������� ��� ����, �� �����
		boolean exist = false;
		int rib_id = -1;
    Rib r = null;
    for( Iterator rbs = ribs.iterator(); rbs.hasNext();)
    { r = (Rib)rbs.next();
      if( r.str_id.equals(rib_str_id) )
       {  exist = true;
          rib_id = r.id;
          break;
       }
    }
		if( !exist ) // ���� ����� ������� ���, �� ��������� � (������ � ���� ���� ����)
		{	int n1_id = add_node(n1_str_id, n1_len); // ���� ����� ������ ��� ���,
			int n2_id = add_node(n2_str_id, n2_len); // �� ��� ���������
			Rib rib = new Rib(ribs.size() + 1, rib_str_id, type, length, n1_id, n2_id);// ��������� ���� � Dll � �������
			ribs.add(rib);// ��������� ����� � ������ ����
			// ����������� ���������� � ����� � ��� ��������, ������� ��� ���������
			((Node)nodes.get(n1_id)).add_rib(rib);
			((Node)nodes.get(n2_id)).add_rib(rib);
		}

		return rib_id;
	}
}
//===========================================================================

