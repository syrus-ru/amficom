package com.syrus.AMFICOM.Client.Optimize;

import java.util.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;
//==================================================================================================================
// класс, содержащий информацию о вершине графа
// идентификатором объекта является его номер в массиве
public class Node
{
	static double default_att = 0.1;//затухание по умолчанию
	public int id;         // числовой идентификатор вершины (для DLL)
	public String str_id;  // строковый идентификатор вершины
	ArrayList ribs = new ArrayList();// список ребер (его размер равен числу выходящих ребер, т е степени вершины)

	int device_type = 1 ;              // тип устройства, находящегося в этом узле. Допускаются: 1 - муфта(т.е. сварка), 2 - bypass(обводной элемент, содержащий 2 разъёма)
	public double  attenuation;         // затухание сигнала в обходном пути, находящемся в этом узле ( 2 сварки сварка или bypass )
	public boolean active       = false;// false - в данном узле нет RTU, true - есть
	// public boolean can_be_active = true; // true - в этот узел можно поместить RTU, false - нельзя (в муфту, например, нельзя, а в bypass - можно )
	public boolean fixed        = false;// true - убирать(поместить) КИС из этого узла нельзя, false - можно
	//double  init_attenuation = 0;      // затухание в dB на начальном этапе в системе "КИС+коммутатор сигнала".
	//-------------------------------------------------------------------------
	public Node(String node_str_id, int n_id, double node_attenuation)
	{ id = n_id;
		str_id = node_str_id;
		attenuation = node_attenuation;
	}
	//-------------------------------------------------------------------------
	// добавляет ребро в список рёбер этой вершины
	public void add_rib(Rib rib)
	{  ribs.add(rib);
	}
}
//==================================================================================================================
