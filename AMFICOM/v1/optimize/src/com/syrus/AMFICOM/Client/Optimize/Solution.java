package com.syrus.AMFICOM.Client.Optimize;

import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.Optimize.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.*;

import com.syrus.AMFICOM.Client.General.Command.Optimize.*;
// класс хранит всю информацию об одном решении
// конструктор сразу отсылает созданное решение на схему
//======================================================================================================================
public class Solution
{
  public String id = "";
  public String name = "";
  public String scheme_id; // идентификатор схемы , к которой это решение относится
  public String typ = "SM_solution";
  public int index;   // номер решения на данной итерации
	public double price;// цена
	private DllAdapter dllAdapter;
	private ViewSchemeFrame schemeFrame;// окно в котором находится панель работы со схемой schemePanel
	// информация об оптимизированной топологии сети мониторинга в формате для схеы
	public Vector paths;       // список путей SchemePaths
	private int pathnumber = 0;// итеративный нумератор путей
	//--------------------------------------------------------------------------------------------------------------
  public Solution ()
  {  scheme_id = "not defined";
     index = -1;
     price = -1;
     paths = new Vector();
  }
  //--------------------------------------------------------------------------------------------------------------
	// конструктор заполняет поле paths решением из schemePath-ов
	public Solution (DllAdapter dllAdapter, int index)
	{	this.index = index;
    this.dllAdapter = dllAdapter;
    OptimizeMDIMain mdiMain = dllAdapter.mdiMain;
    id =  mdiMain.aContext.getDataSourceInterface().GetUId(this.typ);
    scheme_id = mdiMain.scheme.id;
		if(index == 0){	price = dllAdapter.GetBestPrice(); }
    else { price = -1; }

		double topology[] = DllAdapter.getNetMonitoringTopology(index);// топология мониторинга в числовом формате, 0-лучшее решение
		String r_id = "", r_type = "";
		SchemePath schemePath = new SchemePath("");// ОДИН путь тестироваиия
		paths = new Vector(); // вектор всех путей (потом передаём для добавления в схему)
		 // идентификатор узла начала (откуда линк исходит), то есть идентификатор узла, в котором стоит КИС
		schemePath.start_device_id = ( (Node)dllAdapter.m2g.g.nodes.get((int)topology[1]) ).str_id ;
    Rib r;
		for(int i=1; i<topology.length; i+=2)
		{ if(topology[i+1] != -1)// добавляем в линк точки маршрута
			{ r = (Rib)dllAdapter.m2g.g.ribs.get((int)topology[i+1]) ;
        r_id = r.str_id ; // идентификатор линка (ребра)
        r_type =   r.type ; // тип линка (кабель или одно волокно)
				if(r_type.equals("fiber"))// если это волокно(не кабельный линк)
        { SchemeLink sl = (SchemeLink)Pool.get(SchemeLink.typ, r_id);
          addLink(schemePath, sl.id);
        }
        else if(r_type.equals("cable"))// если это кабельный линк
        { SchemeCableLink scl = (SchemeCableLink)Pool.get(SchemeCableLink.typ, r_id);//!!!
          // ВНИМАНИЕ ! берём из кабеля вседа 0е волокно для тестового сигнала (вообще-то это должен задавать пользователь)
          addCableLink(schemePath, scl.id,( (SchemeCableThread)scl.cable_threads.get(0)).id);
        }
			}
			else//если данный путь закончен, то добавляем его в массив путей
			{  // идентификатор узла конца (куда линнк входит)
				 schemePath.end_device_id = ( (Node)dllAdapter.m2g.g.nodes.get((int)topology[i]) ).str_id ;
         // оценочные потери на данном пути
         double loss = topology[i+2];// ... , номер узла конца маршрута (i), -1 (i+1), потери (i+2), -2 (i+3), ...
				 i += 2;
				 ElementAttribute attr = new ElementAttribute();
				 {	attr = new ElementAttribute();
						attr.id = dllAdapter.mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);// получить уникальный идентификатор
						attr.name = "оценка потерь на маршруте";
						attr.type_id = "roughly_estimated_path_loss";
						attr.value = String.valueOf(loss);
						schemePath.attributes.put(attr.type_id, attr);
				 }
				 savePath(schemePath); // добавляем путь к вектору путей paths

				 schemePath = new SchemePath("");// создаём новый путь после записи старого (дадим корректное имя при схоранении в savePath(SchemePath schemePath) )
				 if(i+2 < topology.length)
				 { // идентификатор узла начала (откуда линк исходит)
					 schemePath.start_device_id = ( (Node)dllAdapter.m2g.g.nodes.get((int)topology[i+2]) ).str_id ;
				 }
			}// else
		}// теперь у нас есть paths
	}
	//--------------------------------------------------------------------------------------------------------------
  // создать Solution по данным из того вида , в котором оно хранится в БД (SolutionCompact)
  public Solution (SolutionCompact slc)
  { id = slc.id;
    name = slc.name;
    scheme_id = slc.scheme_id; // идентификатор схемы , к которой это решение относится
    typ = slc.typ;
    price  = slc.price;

    paths = slc.paths;
  }
  //--------------------------------------------------------------------------------------------------------------
	// добавить элемент CableLink(кабель) в путь
	private void addCableLink(SchemePath schemePath, String cable_id, String thread_id)
	{	PathElement pe = new PathElement();
		pe.is_cable = true;
		pe.n = schemePath.links.size(); // надо заполнить перед передачей
		pe.link_id = cable_id;          // надо заполнить перед передачей
		pe.thread_id = thread_id;       // заполнять не надо, это сделает принимающая ф-я
		schemePath.links.add(pe);
	}
  //--------------------------------------------------------------------------------------------------------------
  // добавить элемент Link(волокно) в путь
  private void addLink(SchemePath schemePath, String link_id)
  {	PathElement pe = new PathElement();
    pe.is_cable = false;
    pe.n = schemePath.links.size();
    pe.link_id = link_id;
    pe.thread_id = "";//  у одного волокна ( если оно  не в кабеде ) thread_id всегда пустой , но не null
    schemePath.links.add(pe);
  }
	//--------------------------------------------------------------------------------------------------------------
	private void savePath(SchemePath schemePath)
	{ schemePath.id = dllAdapter.mdiMain.aContext.getDataSourceInterface().GetUId(SchemePath.typ);
		schemePath.name = "Путь " + Integer.toString(++ pathnumber);
		schemePath.type_id = "refpathtype";// refpathtype - идентификатор оптического пути
		paths.add(schemePath);
	}
	//--------------------------------------------------------------------------------------------------------------


}