package com.syrus.AMFICOM.Client.Optimize;

import java.util.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;
//===========================================================================
// �����, ���������� ���������� � ����� �����
// ��������������� ������� �������� ��� ����� � �������
public class Rib
{ //public int tested = 0;       // ������� ��c�� ����, ������� ��� ����� ���� ��������������
	public int id;                 // �������� ������������� ����� (��� DLL)
	public String str_id;          // ��������� ������������� �����
  public String type;            // ��� ������� ( ������ /cable/, ���� ������� /fibre/ � �.�. )
	public int node1_id, node2_id; // ����� ������ � �������� �������
  public double len;             // ����� � ��
	//-------------------------------------------------------------------------
	public Rib( int rib_id, String rib_str_id, String type, double rib_len, int n1_id, int n2_id )
	{ str_id = rib_str_id;
    this.type = type;
		id = rib_id;
		node1_id = n1_id;
		node2_id = n2_id;
		len = rib_len;
	}
	//-------------------------------------------------------------------------
	// ���� : ������� �����;  �����: ������ ������ �����
	public int get_another_node_id( Node node)
	{	int res = -1;
		if(node.id == node1_id) {res = node2_id;}
		if(node.id == node2_id) {res = node1_id;}

    return res;
	}
}
//===========================================================================
