package com.syrus.AMFICOM.Client.Resource.Optimize;

import java.util.*;
import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;


import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import com.syrus.AMFICOM.Client.General.UI.PropertyEditor;
import com.syrus.AMFICOM.Client.General.UI.PropertyRenderer;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.TextFieldEditor;
import com.syrus.AMFICOM.Client.Resource.StubResource;;


// ВНИМАНИЕ! При изменениях этого класса его надо отдавать Андрею как исходникик, так как он у него лежит в map !

// класс хранит только информацию о топологии решения (берёт его из класса Scheme)
// дополнительная информация ( цены оборудования, список оборудования и т.п.) -  в классе InfoToStore
//==========================================================================================================
public class SolutionCompact extends StubResource
{ public String id = "";
  public String name = "";
  public String created = "";// время создания
  public String created_by = "";
  public String description = "";
  public String scheme_id = ""; // идентификатор схемы , к которой это решение относится
  public String domain_id = "";
  public static final String typ = "sm_solution";
  public double price = -1;
  public Vector paths = new Vector(1); // список путей SchemePaths (инициализируем просто чтобы не было null pointer exception если что не пропишется)
  public  SchemeMonitoringSolution_Transferable transferable = new SchemeMonitoringSolution_Transferable();
  //-----------------------------------------------------------------------------------------------------
  // по данным из решения создаём наш формат записи для БД
  public SolutionCompact( double price, String scheme_id, Vector paths, String id, String name )
  { this.id = id;
    this.name = name;
    this.price = price;
    this.scheme_id = scheme_id;
    this.paths = paths;
  }
  //-----------------------------------------------------------------------------------------------------
  public SolutionCompact(SchemeMonitoringSolution_Transferable transferable)
  { this.transferable = transferable;
    setLocalFromTransferable();
  }
  //-----------------------------------------------------------------------------------------------------
  public void setLocalFromTransferable()
  { id = transferable.id;
    name = transferable.name;
    scheme_id = transferable.scheme_id;
    price = transferable.price;
    // прописываем пути SchemePath из массива в вектор
    paths = new Vector(transferable.paths.length);
    paths.clear();
    for(int i=0; i<transferable.paths.length; i++)
    {  SchemePath sp = new SchemePath( transferable.paths[i] );
       paths.add(sp);
    }
// 88888888888888888888888888888
//    for (int i = 0; i < transferable.elements.length; i++)
//    { SchemeElement element = new SchemeElement(transferable.elements[i]);
//      if (transferable_element_ids.contains(element.getId()))
//        elements.add(element);
//      else
//        elements_to_register.add(element);
//    }
//    for (int i = 0; i < transferable.cable_links.length; i++)
//      cablelinks.add(new SchemeCableLink(transferable.cable_links[i]));
//    for (int i = 0; i < transferable.links.length; i++)
//      links.add(new SchemeLink(transferable.links[i]));
//    for (int i = 0; i < transferable.paths.length; i++)
//      paths.add(new SchemePath(transferable.paths[i]));
// 88888888888888888888888888888
  }
  //-----------------------------------------------------------------------------------------------------
  public void  updateLocalFromTransferable()
  { id = transferable.id;
    name = transferable.name;
    scheme_id = transferable.scheme_id;
    price = transferable.price;
    // прописываем пути SchemePath из массива в вектор
    paths = new Vector(transferable.paths.length);
    paths.clear();
    for(int i=0; i<transferable.paths.length; i++){ paths.add(transferable.paths[i]);}
  }
  //-----------------------------------------------------------------------------------------------------
  public void setTransferableFromLocal()
  { transferable.id = this.id;
    transferable.name = this.name;
    transferable.scheme_id = this.scheme_id;
    transferable.price = this.price;
    transferable.paths = new SchemePath_Transferable[this.paths.size()];
    for(int i=0; i<transferable.paths.length; i++)
    {  SchemePath path = (SchemePath)paths.get(i);
       path.setTransferableFromLocal();
       transferable.paths[i] = (SchemePath_Transferable)path.getTransferable();
    }
    System.out.println("SolutionCompact.setTransferableFromLocal() : transferable.scheme_id = " + transferable.scheme_id);
  }
  //-----------------------------------------------------------------------------------------------------
  public String getDomainId(){return domain_id;}
  //-----------------------------------------------------------------------------------------------------
  public String getId(){return id;}
  //-----------------------------------------------------------------------------------------------------
  public String getName(){return name;}
  //-----------------------------------------------------------------------------------------------------
  public Object getTransferable()
  {return transferable;
  }
  //-----------------------------------------------------------------------------------------------------
  public String getTyp(){return typ;}
  //-----------------------------------------------------------------------------------------------------
}
//==========================================================================================================
