//  Класс содержит ф-ции преобразования формата топологии сети в вид, удобный для передачи в Dll.
//  Всё общение с DLL идёт через него
package com.syrus.AMFICOM.Client.Optimize;

import java.util.*;

import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.*;

//
//====================================================================================================================
public class Map2GraphAdapter
{   private ArrayList links = new ArrayList();//используется для выявления дублирующих внутренних линков
    private OptimizeMDIMain mdiMain;
    private Scheme scheme = null;

    public Graph g = new Graph();
    //-------------------------------------------------------------------------
    // при создании объекта сразу производим перевод в граф
    public Map2GraphAdapter(OptimizeMDIMain mdiMain)
    { CreateGraphByMap(mdiMain);
    }
    //-------------------------------------------------------------------------
    // заносим все рёбра (а значит и все вершины)
    public void CreateGraphByMap(OptimizeMDIMain mdiMain)
    {try
     { this.mdiMain = mdiMain;
       ((OpticalOptimizerContext)mdiMain.optimizerContext).graph_set = 0;// если всё пройдёт нормально, то поставим значение "1"
       // вектор линков
       scheme = null;
       if(mdiMain != null) scheme = mdiMain.scheme;// схема автоматически подгружается и при открытии карты, таким образом она должна быть к этому моменту уже проинициализирована
       else
       {System.err.println("CreateGraphByMap(Scheme): mdiMain = null. Impossible to start optimization.");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(),
             "Ошибка инициализации.\n Начать оптимизацию невозможно.", "ошибка предобработки сети", javax.swing.JOptionPane.OK_OPTION);
    return;
       }
       if (scheme == null) // проверка , загружена ли схема, если нет, то выход
       { System.err.println("CreateGraphByMap(Scheme): Network scheme is not loaded.Impossible to start optimization.");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(),
             "Схема сети не загружена.\n Начать оптимизацию невозможно.", "ошибка", javax.swing.JOptionPane.OK_OPTION);
         // не отмечаем, что граф создан, чтобы процесс не продолжался после выхода из этой функции
    return;
       }
       int n_cablelinks = processAllCables();// рописатьвсе кабели
       if ( n_cablelinks == -1 ) // код ошибочного выхода из ф-ции
    return;
       int n_links = processAllLinks();// прописать все линки
       if(  n_links == -1 )// код ошибочного выхода из ф-ции
    return;
       if(n_cablelinks==0 && n_links==0) // если ни одного ребра так добавлено и не было , то выход - оптимизировать нечего
       { System.err.println("CreateGraphByMap(Scheme): Nothing to optimize.");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(),
             "Схема уже содержит решение по тестированию всех линий связи.", "", javax.swing.JOptionPane.OK_OPTION);
    return;
       }
       // Г Р А Ф   С О З Д А Н
       setNodesAttributes();// устанавливаем атрибуты вершины ( можно или нет в вершинах размещать КИС )
       // если дошли до этого места, то значит граф создан
       ( (OpticalOptimizerContext)mdiMain.optimizerContext).graph_set = 1;
       System.out.println("Graph created: nnodes = " + g.nodes.size() + " nribs = " + g.ribs.size());
    }//try
    catch(NumberFormatException nfex)
    {  System.err.println("Type cast error in the function of network uploading.");
       javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(),"Неправильно заданы параметры сети", "ошибка формата данных", javax.swing.JOptionPane.OK_OPTION);
    }
    catch(Exception ex){ex.printStackTrace();}
}
//-------------------------------------------------------------------------
// устанавливаем атрибуты вершины ( можно или нет в вершинах размещать КИС )
  private void setNodesAttributes()
  { System.out.println("Установка атрибутов оптимизации в узлах ...");
    // проходим по всем узлам графа
    for( Iterator nodes = g.nodes.iterator(); nodes.hasNext(); )
    { Node node = (Node)nodes.next();
      String schel_id = node.str_id; // идентификатор элемента схемы ( такой же , как и идентификатор узла а графе )
      SchemeElement schel = (SchemeElement)Pool.get(SchemeElement.typ,schel_id);
      ElementAttribute att = (ElementAttribute)schel.attributes.get("optimizerNodeAttribute");
      if(att != null)
      { if(att.value.equals("restricted"))
        { node.active = false;
          node.fixed = true; // значение "active" менять нельзя
        }
      }
      // если атрибут не существует, то создаём его и прописываем optional
      else if( att == null )
      { //System.out.println("CreateGraphByMap(Scheme): атрибут оптимизации optimizerNodeAttribute узла " + schel_id + " не задан. Создаём автоматически.");
        att = new ElementAttribute();
        att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
        att.name = "Наличие КИС";
        att.type_id = "optimizerNodeAttribute";
        att.value = "optional";// по умолчанию стоит optional
        schel.attributes.put(att.type_id, att);
      }
      // если элемент более высокого уровня помечен как запрещённый, то и внутренний элемент надо запретить
      // (например кабельная панель является частью муфты)
      SchemeElement upper_schel = scheme.getTopLevelNonSchemeElement(schel);
      ElementAttribute upper_att = (ElementAttribute)upper_schel.attributes.get("optimizerNodeAttribute");
      if(upper_att != null)
      { if(upper_att.value.equals("restricted"))
        { node.active = false;
          node.fixed = true; // значение "active" менять нельзя
          //System.out.println("Element "+schel.id+" is not restricted but is a part of restricted element "+upper_schel.id);
        }
      }
      //************************* !!!
      // если это муфта то запрещаем ставить КИС в её составных частях
      if(!upper_schel.equipment_id.equals("") )// по элементу берём оборудование из пула если он есть в каталоге
      {  Equipment eq = (Equipment)Pool.get(Equipment.typ, upper_schel.equipment_id);
         if( eq != null && eq.eq_class.equals("mufta") ) // если это муфта, то ставим запрещение на КИС в этом узле (null если eq это kis)
         {  upper_att = (ElementAttribute )upper_schel.attributes.get("optimizerNodeAttribute");
            if( upper_att == null )// если атрибут не существует, то создаём его и прописываем  в хэш елемента
            {	 upper_att = new ElementAttribute();
               upper_att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ); //уникальный идентификатор объекта (генерится на сервере)
               upper_schel.attributes.put(upper_att.type_id, upper_att);
            }
            upper_att.name = "Наличие КИС";
            upper_att.type_id = "optimizerNodeAttribute";
            upper_att.value = "restricted";

            node.active = false;
            node.fixed = true; // значение "active" менять нельзя
            //System.out.println("Element "+schel.id+" is not restricted but is a part of restricted element "+upper_schel.id);
         }
      }
      else // если нет в каталоге, то узнавать, муфта это или нет, надо иначе - через EquipmentType
      {	 if(!upper_schel.proto_element_id.equals(""))// пустое поле это НЕ муфта, а схема боле низкого порядка (узел может быть схемой в силу рекурсивности схемы)
         { ProtoElement proto = (ProtoElement)Pool.get(ProtoElement.typ, upper_schel.proto_element_id );
           EquipmentType eqtype = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipment_type_id );
           if( eqtype!=null && eqtype.eq_class.equals("mufta"))
           { upper_att = (ElementAttribute)upper_schel.attributes.get("optimizerNodeAttribute");
             if(upper_att==null) // если атрибут не существует, то создаём его и прописываем  в хэш елемента
             { upper_att = new ElementAttribute();
               upper_att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ); //уникальный идентификатор объекта (генерится на сервере)
               upper_schel.attributes.put(upper_att.type_id, upper_att);
             }
             upper_att.name = "Наличие КИС";
             upper_att.type_id = "optimizerNodeAttribute";
             upper_att.value = "restricted";

             node.active = false;
             node.fixed = true; // значение "active" менять нельзя
             //System.out.println("Element "+schel.id+" is not restricted but is a part of restricted element "+upper_schel.id);
           }
         }
      }
      //*************************
    }
    System.out.println("Установка атрибутов оптимизации в узлах -  завершено успешно \n");
  }
//---------------------------------------------------------------------------------
// C Ч И Т Ы В А Е М     В С Е     К А Б Е Л И
private int processAllCables()
{   int wavelength = mdiMain.optimizerContext.wavelength;
    int nscl=0;// счётчик добавленных линков
    String atten_str = "Attenuation_" + Integer.toString(wavelength);
    for( Enumeration cls = scheme.getAllCableLinks(); cls.hasMoreElements();) // по всем кабелям ( то есть по всем  рёбрам )
    {  SchemeCableLink scl = (SchemeCableLink) cls.nextElement();
       String scl_id = scl.id ;
       ElementAttribute att = (ElementAttribute)scl.attributes.get("optimizerRibAttribute");
       //если этот линк уже был прописан при загрузке в путь, то не передааём его в dll, то есть его  для нас не существует
       if(att == null)
       { System.err.println("Link in cable " + scl_id + " has no optimization attribute. Impossible to start optimization.");
 return -1;
       }
       if( att.value.equals("tested") || att.value.equals("passive") )// пассиавные и ранее протестированные кабели пропускаем
    continue;

      double len_db = mdiMain.optimizerContext.calcCableLinkAttenuation(scl, wavelength);// len_db = length*attenuation - длина в децибелах
      if (len_db == -1)
      {  System.err.println("Failed getting signal loss for cable " + scl_id + " for wavelength" + String.valueOf(wavelength) + ". Impossible to start optimization.");
 return-1;
      }
      // узнаём узлы, которые соединяет данный кабель:
      // в схеме есть элементы, в каждом элементе есть девайс, в каждом девайсе есть порты
      // если элемент схемы содержит девайс с указанным протом, то узлу прописывается идентификатор этого элемента
      String n1_str_id = get_shemeElementId_by_cablePortId(mdiMain.scheme, scl.source_port_id);
      String n2_str_id = get_shemeElementId_by_cablePortId(mdiMain.scheme, scl.target_port_id);
      if(n1_str_id.equals("error"))
      { System.err.println("CreateGraphByMap(Scheme):incorrect node string-identificator. Impossible to start optimization.");
        javax.swing.JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Название элемента(узла), содержащего порт "+scl.source_port_id
            +", имеет недопустимое значение. Начать оптимизацию невозможно.", "ошибка считывания параметров сети", javax.swing.JOptionPane.OK_OPTION);
 return -1;
      }
      if(n2_str_id.equals("error"))
      { System.err.println("CreateGraphByMap(Scheme):incorrect node string-identificator. Impossible to start optimization.");
        javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(), "Название узла, содержащего порт "+scl.source_port_id
            + ", имеет недопустимое значение. Начать оптимизацию невозможно.", "ошибка считывания параметров сети", javax.swing.JOptionPane.OK_OPTION);
 return -1;
      }
      // устанавливаем атрибуты обязательных и запрещённых узлов, если необходимо, атрибуты считываются с карты
      double n1_len = Node.default_att, n2_len = Node.default_att; //затухание по умолчанию (статическое поле)
      String rib_str_id = scl.getId();
      //g.add_rib(n1_str_id - идентификатор первого узла, n1_len, n2_str_id, n2_len, len_db, rib_str_id - идентификатор линка);
      if( len_db == 0 )
      {  System.err.println("processAllCables(): Length of cable with id = "+ rib_str_id + " equals 0");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(), "Длина волокна в кабеле " + scl.getName()
             +" равна нулю. Начать оптимизацию невозможно.", "недопустимое значение длины кабеля", javax.swing.JOptionPane.OK_OPTION);
 return -1;
      }
      else
      { g.add_rib( n1_str_id, "cable", n1_len, n2_str_id, n2_len, len_db, rib_str_id );
        nscl++;
      }
    } //for( Enumeration cls = scheme.getAllCableLinks(); cls.hasMoreElements();)
   return nscl;// количество добавленных кабельных линков
}
//---------------------------------------------------------------------------------------
// С Ч И Т Ы В А Е М   В С Е   Л И Н К И (по отдельным линкам тоже пропускается тестовый сигнал)
private int processAllLinks()
{   int nsl = 0;// счётчик добавленных линков
    int wavelength = mdiMain.optimizerContext.wavelength;
    mdiMain.optimizerContext.unilinks.clear();// очищаем список имён, которые надо брать в скобки
    for( Enumeration ls = scheme.getAllLinks(); ls.hasMoreElements();) // по всем линкам
    { SchemeLink sl = (SchemeLink)ls.nextElement();
      String sl_id = sl.id ;
      ElementAttribute att = (ElementAttribute )sl.attributes.get("optimizerRibAttribute");
      if(att == null)
      { System.err.println("Link " + sl_id + " has no optimization attribute. Impossible to start optimization.");
 return -1;
      }
      if( att.value.equals("tested") || att.value.equals("passive") ) // пассиавные и ранее протестированные кабели пропускаем
    continue;
      double len_db = mdiMain.optimizerContext.calcLinkAttenuation( sl, wavelength);// len_db = length*attenuation - длина в децибелах
      if (len_db == -1)
 return -1;
     String rib_str_id = sl.getId();
     // узнаём узлы, которые соединяет данный линк:
     String n1_str_id = get_shemeElementId_by_portId(mdiMain.scheme, sl.source_port_id);
     String n2_str_id = get_shemeElementId_by_portId(mdiMain.scheme, sl.target_port_id);
     String unirib_str_id = isNodesLinked(n1_str_id, n2_str_id, rib_str_id);// unirib - линк, который выбирается один из набора линков между парой устройств
     if(!unirib_str_id.equals("")) // если эти два элемента уже соединены линком (именно линком), то не добавляем нового ребра
     {  //System.out.println("CreateGraphByMap(Scheme):Между устройствами "+n1_str_id+" и "+n1_str_id+" уже существует линк "+unirib_str_id+". Дублирующий линк "+rib_str_id+" проигнорирован");
        // поскольку  n1 и n2 уже соединены unirib_str_id, и есть второй линк rib_str_id, то имя первого линка надо взять в скобки
        if(!mdiMain.optimizerContext.unilinks.contains(unirib_str_id))
        { mdiMain.optimizerContext.unilinks.add(unirib_str_id);
        }
     }
     else // добавляем новое ребро
     { // если такой пары ещё нет, то дописываем это в нашу таблицу
       links.add(n1_str_id);links.add(n2_str_id); links.add(rib_str_id);// пишем тройками "вершина-вершина-линк"
       if(n1_str_id.equals("error"))
       { System.err.println("CreateGraphByMap(Scheme): Incorrect string of node identificator. Impossible to start optimization");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(), "Название элемента схемы, содержащего порт "+sl.source_port_id
             +", имеет недопустимое значение. Начать оптимизацию невозможно.", "недопустимое значение имени", javax.swing.JOptionPane.OK_OPTION);
 return -1;
       }
       if(n2_str_id.equals("error"))
       { System.err.println("CreateGraphByMap(Scheme): Incorrect string of node identificator. Impossible to start optimization");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(), "Название узла, содержащего порт "+sl.source_port_id
             +", имеет недопустимое значение. Начать оптимизацию невозможно.", "недопустимое значение имени", javax.swing.JOptionPane.OK_OPTION);
 return -1;
       }
       // устанавливаем атрибуты обязательных и запрещённых узлов, если необходимо, атрибуты считываются с карты
       double n1_len = Node.default_att, n2_len = Node.default_att; //затухание по умолчанию (статическое поле)
       // сварки в муфтах игнорируем: если длина линка = 0, то значит это фиктивный линк в муфте (то есть сварка)
       if( len_db == 0 )
       { g.add_rib(n1_str_id, "fiber", n1_len/2, n2_str_id, n2_len/2, len_db, rib_str_id);
         nsl++;
       }
       else
       { g.add_rib(n1_str_id, "fiber", n1_len, n2_str_id, n2_len, len_db, rib_str_id);
         nsl++;
       }
     }
    } //for( Enumeration ls = scheme.getAllCableLinks(); ls.hasMoreElements();)
    return nsl;
}
  //-------------------------------------------------------------------------
  // вход : схема, строковый идентификатор порта
  // выход: строковый идентификатор элемента, содержащий в себе устройство с портом, строковый
  // идентификатор которого передан на схему
  public String get_shemeElementId_by_cablePortId(Scheme scheme, String port_id)
  { String id = "error";
    SchemeElement schel = scheme.getSchemeElementByCablePort(port_id);
    if( schel != null)
    { //SchemeElement topLevelElement = scheme.getTopLevelNonSchemeElement(schel);
      //if(topLevelElement != null){ id = topLevelElement.getId();}
      id = schel.getId();
    }
    return id;
  }
 //-------------------------------------------------------------------------
 public String get_shemeElementId_by_portId(Scheme scheme, String port_id)
 { String id = "error";
   SchemeElement schel = scheme.getSchemeElementByPort(port_id);
   if( schel != null)
   { //SchemeElement topLevelElement = scheme.getTopLevelNonSchemeElement(schel);
     //if(topLevelElement != null){ id = topLevelElement.getId();}
     id = schel.id;//берём id не самого верхнего элемента, а именно того, в который непосредственно приходит кабель или линк
   }
   return id;
 }
 //-------------------------------------------------------------------------
 // Если указанная пара вершин уже соединена (предыдущими вызовами этой ф-ции), то возвращает имя линка, которым соединена.
 // Если нет, то добавляет указанную пару вершин в список и false
 // Обращаться к g было бы ошибочно, так как нас интересуют только дублирующиеся _линки_. Кабели могут дублироваться без ограничений.
 private String isNodesLinked(String n1, String n2, String r)
 { String result = "", link_name, node1, node2;
   for(Iterator ils = links.iterator(); ils.hasNext(); ) // ils - Iterator of LinkS
   { node1 = (String) ils.next(); node2 = (String)ils.next(); link_name = (String)ils.next();
     if( (node1.equals(n1)&&node2.equals(n2)) || (node1.equals(n2)&&node2.equals(n1)) ) //граф ненаправленный => порядок не важен
     {  result = link_name;// имя линка
   break;
     }
   }
   return result;
 }
 //-------------------------------------------------------------------------
}
//======================================================================================================================
