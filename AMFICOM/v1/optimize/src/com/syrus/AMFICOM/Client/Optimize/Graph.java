package com.syrus.AMFICOM.Client.Optimize;

import java.util.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

// класс, содержащий математическое описание топологии графа, взятого с карты
//===========================================================================
public class Graph
{ // положение ребра или вершины в массиве расно её числовому id ( нумерация c нуля )
	public ArrayList ribs = new ArrayList(); // динамический массив ребер графа (его длина равна числу рёбер в графе).
	public ArrayList nodes = new ArrayList();// динамический массив вершин графа (его длина равна числу вершин в графе).
	//-------------------------------------------------------------------------
	public Graph(){}
	//-------------------------------------------------------------------------
	// вход : идентификтор вершины
	// выход: вершина с данным идентификатором
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
	// если вершины с таким строковым идентификатором ещё нет, то она создаётся, если уже есть, то выход
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
		if( !exist )//если такой вершины нет, то добавляем её (список её рёбер пока пуст)
		{ //автоматически пронумеровываем (НАЧИНАЕМ С НУЛЯ - это важно, так как в Dll тоже начинается с нуля)
			id = nodes.size();//идентификатор ребра равен индексу его положения в массиве!
			Node node = new Node(node_str_id, id, attenuation);
			nodes.add(node);
		}
		return id;
	}
	//-------------------------------------------------------------------------
	// вход : идентификатор ребра
	// выход: ребро с данным идентификатором
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
	//  добавить ребро и затухание в узлах В ГРАФ
	//  принимает: две вершины - конца ребра, затухание в каждой вершине, длину ребра и его строковый идентификатор
	//  возвращает: числовой идентификатор ребра
	public int add_rib (String n1_str_id, String type, double n1_len, String n2_str_id, double n2_len, double length, String rib_str_id)
	{ // если ребро с таким строковым идентификатором уже есть, то выход
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
		if( !exist ) // если такой вершины нет, то добавляем её (список её рёбер пока пуст)
		{	int n1_id = add_node(n1_str_id, n1_len); // если таких вершин ещё нет,
			int n2_id = add_node(n2_str_id, n2_len); // то они создаются
			Rib rib = new Rib(ribs.size() + 1, rib_str_id, type, length, n1_id, n2_id);// нумерация рёбер в Dll с единицы
			ribs.add(rib);// добавляем ребро в массив рёбер
			// прописываем информацию о ребре в тех вершинах, которое оно соединяет
			((Node)nodes.get(n1_id)).add_rib(rib);
			((Node)nodes.get(n2_id)).add_rib(rib);
		}

		return rib_id;
	}
}
//===========================================================================

