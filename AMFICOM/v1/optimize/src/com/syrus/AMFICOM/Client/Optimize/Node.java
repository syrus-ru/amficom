package com.syrus.AMFICOM.Client.Optimize;

import java.util.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;
//==================================================================================================================
// �����, ���������� ���������� � ������� �����
// ��������������� ������� �������� ��� ����� � �������
public class Node
{
	static double default_att = 0.1;//��������� �� ���������
	public int id;         // �������� ������������� ������� (��� DLL)
	public String str_id;  // ��������� ������������� �������
	ArrayList ribs = new ArrayList();// ������ ����� (��� ������ ����� ����� ��������� �����, � � ������� �������)

	int device_type = 1 ;              // ��� ����������, ������������ � ���� ����. �����������: 1 - �����(�.�. ������), 2 - bypass(�������� �������, ���������� 2 �������)
	public double  attenuation;         // ��������� ������� � �������� ����, ����������� � ���� ���� ( 2 ������ ������ ��� bypass )
	public boolean active       = false;// false - � ������ ���� ��� RTU, true - ����
	// public boolean can_be_active = true; // true - � ���� ���� ����� ��������� RTU, false - ������ (� �����, ��������, ������, � � bypass - ����� )
	public boolean fixed        = false;// true - �������(���������) ��� �� ����� ���� ������, false - �����
	//double  init_attenuation = 0;      // ��������� � dB �� ��������� ����� � ������� "���+���������� �������".
	//-------------------------------------------------------------------------
	public Node(String node_str_id, int n_id, double node_attenuation)
	{ id = n_id;
		str_id = node_str_id;
		attenuation = node_attenuation;
	}
	//-------------------------------------------------------------------------
	// ��������� ����� � ������ ���� ���� �������
	public void add_rib(Rib rib)
	{  ribs.add(rib);
	}
}
//==================================================================================================================
