package com.syrus.AMFICOM.Client.Optimize;

import java.util.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;
//===========================================================================
// класс, содержащий информацию о ребре графа
// идентификатором объекта является его номер в массиве
public class Rib
{ //public int tested = 0;       // счётчик чиcла того, сколько раз ребро было протестировано
	public int id;                 // числовой идентификатор ребра (для DLL)
	public String str_id;          // строковый идентификатор ребра
  public String type;            // тип волокна ( кабель /cable/, одно волокно /fibre/ и т.д. )
	public int node1_id, node2_id; // номер вершин в числовом формате
  public double len;             // длина в дБ
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
	// вход : вершина ребра;  выход: другая вершна ребра
	public int get_another_node_id( Node node)
	{	int res = -1;
		if(node.id == node1_id) {res = node2_id;}
		if(node.id == node2_id) {res = node1_id;}

    return res;
	}
}
//===========================================================================
