// в этом классе содержатся все функции, необходимые для того , чтобы проинициализировать процесс
// оптимизации: передать в Dll топологию сети, установить параметры хода оптимизации и т д
// а также содержатся все данные о данном процессе оптимизации: его параметры, карта, которцую оптимизируем и т д

package com.syrus.AMFICOM.Client.Optimize;

import java.util.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;


public class OpticalOptimizerContext
{
    // индикаторы
    public int topology_set = 0;         // передана ли в dll топология сети ( только после выбора длины волны )
    public int prices_set = 0;           // передан ли в dll список цен на оборудование
    public int wavelength_set = 0;       // установлена ли длина волны тестирующего сигнала
    public int graph_set = 0;            // создан ли граф по данной сети (в случае если не указана длина волны конструктор может быть вызван, но правильного графа он не создаст)
    public int is_in_progress = 0;       // флаг состояния процесса оптимизации
    public boolean once_started = false; // флаг, был ли хоть раз запущен процесс оптимизации

    public int wavelength = -1;    // длина волны
    public int optimize_mode = 0;  // режим оптмизации, 0 - односторонее, 1 - встречное

    public OptimizeMDIMain mdiMain;// главное окно
    public Solution solution;      // решение
    public DllAdapter dllAdapter;  // класс-адаптер для удобного обращения с dll-кой
    public OptimizeParameters opt_prs = new OptimizeParameters();
    public double iterations;      // количество итераций, прошедших с запуска обптимизации
    public ArrayList original_paths;// вектор путей, которые изначально были на карте
    public ArrayList originally_lconnected_nodes; // соединённые изначально линками path-а узлы в виде троек {node1.id, node2.id, link.id}
    public double[] refl_prices;
    public double[] switch_prices;

    // параметры оптимизации
    String[] param_descriptions = new String[] {
              "коэффициент запаса",
              "процент рандомизации", // коэффициент мутации (мутируют примерно mutation_rate часть от общего числа особей)
              "степень рандомизации", // количество промутировавших генов в среднем в той особи , которая подверглась мутации
              "P удаления КИС",
              "P создания КИС",
              "P сращения волокон",
              "P разрыва волокон",
              "консервативность"
             };
    Double[][] param_values = new Double[][]
       { {new Double(opt_prs.len_margin)},
         {new Double(opt_prs.mutation_rate)},
         {new Double(opt_prs.mutation_degree)},
         {new Double(opt_prs.rtu_delete_prob)},
         {new Double(opt_prs.rtu_create_prob)},
         {new Double(opt_prs.nodes_splice_prob)},
         {new Double(opt_prs.nodes_cut_prob)},
         {new Double(opt_prs.survivor_rate)}
      };
    // названия линков, имена которых надо брать в скобки
    Vector unilinks = new Vector();
    //--------------------------------------------------------------------------------------------------------------
    public OpticalOptimizerContext(OptimizeMDIMain mdiMain)
    { this.mdiMain = mdiMain;
    }
    //--------------------------------------------------------------------------------------------------------------
    //передать в Dll топологию сети. Вход: карта.
    public void setTopology(MapContext map_c){}
    //--------------------------------------------------------------------------------------------------------------
    // передать в Dll параметры оптимизации   !!! ДОДЕЛАТЬ В ПЕРВУЮ ОЧЕРЕДЬ
    public void setParameters(OptimizeParameters param)
    {
    }
    //--------------------------------------------------------------------------------------------------------------
    // передать в Dll таблицу зависимости цены от динамич диапазона РТУ
    public void setOptimizationBase(RTUInfo base[]){}//price, range, distance
    //--------------------------------------------------------------------------------------------------------------
    public void setPricelist()
    { if(mdiMain == null)
      {  System.err.println( "setPricelist(): mdiMain == null. Setting aborting..." );
      }
      else
      { setPricelist(mdiMain.kisSelectFrame);
      }
    }
    //--------------------------------------------------------------------------------------------------------------
    public void setOptimizationParam(int row , double value)
    {  opt_prs.setOptimizationParam(row, value); //меняем в объекте
       dllAdapter.setOptimizationParam (row, value); // меняем в dll
    }
    //--------------------------------------------------------------------------------------------------------------
    // передать в Dll таблицу зависимости цены от динамич диапазона РТУ и количества портов на свиче
    // также инициализирует локальные массивы refl_prices и switch_prices этимми же данными
    public void setPricelist(KISselectionFrame kisSelect)
    {	// цена передаётся в Dll только ОДИН раз - по команде "начать оптимизацию"
      if(prices_set != 0)
    return;
      Vector v = new Vector();
      // для рефлектометров
      v.clear();
      double wls = (double)wavelength; // wls - wavelength selected
      // из всех рефлектометров выбираем только те, которые работают на выбранной длине волны wls
      for( Enumeration rsds=kisSelect.reflectometers.elements() ; rsds.hasMoreElements();)//reflectometers - вектор из  ReflectometerShortData(rID, rn, rwl, rdd, rp)
      { ReflectometerShortData refl = (ReflectometerShortData)rsds.nextElement();//рефлектометр ( краткая форма )
        for(int j=0; j<refl.wavelength.size(); j++ )
        {  if( Double.parseDouble( (String)refl.wavelength.elementAt(j) ) == wls )// если данный рефлектометрт может работать на выбранной длине волны
           { String dw = refl.dynamicWindow.elementAt(j).toString();
             String pr = refl.price;
             v.add( new String(dw) ); v.add( new String(pr) );
           }
        }
      }
      int j=0, size = v.size();
      while(  j<size && v.size()>0)// удаляем пустые и незаполненные поля ( незаполненным считается всякое поле, содержащее "?" )
      { if ( v.elementAt(j).equals("") || v.elementAt(j+1).equals("")
             || ((String)v.elementAt(j)).indexOf("?")!= -1  || ((String)v.elementAt(j+1)).indexOf("?")!= -1
           )
       {	 v.remove(j); v.remove(j);// если удалаили из вектора элементы, то слеующий сдвинется и встанет на место удалённого
           size = v.size();
       }
       else
       {  j+=2; //данные идут парами
       }
      }
      if(v.size() == 0)// если цен нет
      {	 //javax.swing.JOptionPane.showMessageDialog (Environment.getActiveWindow(),"цены рефлектометров не заданы ", "ошибка задания цен", javax.swing.JOptionPane.OK_OPTION);
  return;
      }
      // преобразуем из String в double
      size = v.size();
      for(int i=0; i<size; i++)
      {  double d = Double.parseDouble( (String) v.elementAt(i) );
         v.set(i, new Double(d) );// заменяем
      }
      // приводим вектор double к массиву double
      if (v.size() != 0)
      { Double[] Arr = ( (Double[] ) v.toArray( new Double[0]) );
        double[] arr = new double[v.size()];
        for(j=0; j<v.size(); j++){ arr[j] = Arr[j].doubleValue(); }
        dllAdapter.SetReflPricelist(arr);
        //прописываем в локальный массив
        refl_prices = new double[v.size()];
        for(j=0; j<v.size(); j++){ refl_prices[j] = arr[j]; }
      }
      // для коммутаторов
      v.clear();
      v.add (new String("1") ); v.add( new String("0"));// прямое подключение нисколько не стоит (разветвителя нет)
      for(int i=0; i<kisSelect.switchers.size(); i++)
      {   String pn = ( (SwitcherShortData)(kisSelect.switchers.elementAt(i)) ).portNumber;
          String pr = ( (SwitcherShortData)(kisSelect.switchers.elementAt(i)) ).price;
          v.add( new String(pn) ); v.add( new String(pr) );
      }
      j=0; size=v.size();
      while(  j<size && v.size()>0 )
      { if ( v.elementAt(j).equals("") || v.elementAt(j+1).equals("") )
        {	 v.remove(j); v.remove(j);// если удалаили из вектора элементы, то слеующий сдвинется и встанет на место удалённого
           size = v.size();
        }
        else
        {  j+=2;// если удалаили из вектора элементы, то слеующий сдвинется и встанет на место удалённого
        }
      }
      if(v.size() <= 1 )// если цен нет ("=1" если задана цена 0 для одного порта)
      {	 //javax.swing.JOptionPane.showMessageDialog (Environment.getActiveWindow(),"цены коммутаторов не заданы ", "ошибка задания цен", javax.swing.JOptionPane.OK_OPTION);
  return;
      }
      // преобразуем из String в double
      size = v.size();
      for(int i=0; i < size; i++)
      {  double d = Double.parseDouble( (String) v.elementAt(i) );
         v.set( i, new Double(d) );
      }
      // приводим вектор double к массиву double
      if (v.size() != 0)
      { Double[] Arr = ( (Double[] ) v.toArray( new Double[0]) );
        double[] arr = new double[v.size()];
        for(j=0; j<v.size(); j++){ arr[j] = Arr[j].doubleValue(); }
        dllAdapter.SetCrossPricelist(arr);
        //прописываем в локальный массив
        switch_prices = new double[v.size()];
        for(j=0; j<v.size(); j++){ switch_prices[j] = arr[j]; }
      }
      prices_set = 1;
    }
    //----------------------------------------------------------------------------------------------------------------
    // возвращает вектор строк, каждая из которых описывает один путь тестирования, а вместе они описывают всю топологию тестирования
    public Vector getSolutionInString()
    {	Vector pathElementStrId = new Vector();// вектор строковых идентификаторов одного шага пути тестирования (вершины или ребра)
      Vector transmissionPath = new Vector();// путь тестирования, состоящий из transmittionElementStrId  строк, описывающих по одному пути тестирования

      String s = "";// s = getName() + " : ";
      Vector v = null; // v = physicalLink_ids;
      for(int i = 0; i < v.size(); i++)// по всему массиву линков
      {	String link_id = (String )v.elementAt(i);
        s += Pool.getName(SchemeCableLink.typ, link_id);
        if(i != v.size() - 1 )
          s += " -> ";
      }

      return transmissionPath;
    }
    //--------------------------------------------------------------------------------------------------------------
    // начать\продолжить процесс оптимизации
    public void Start()
    {  dllAdapter.Run();
       is_in_progress = 1;
    };
    //--------------------------------------------------------------------------------------------------------------
    // приостановить процесс оптимизации
    public void Stop()
    {  dllAdapter.Suspend();
       is_in_progress = 0;
       // записываем лучшее решение
       solution = getCurrentBestSolution();// объект Solution создаётся только здесь и при загрузке ( больше нигде )
       iterations = (double)dllAdapter.GetIterationCounter();
       // все, кому надо, отловят это событие и перерисуют решение, взяв его из mdiMain
       mdiMain.getInternalDispatcher().notify(new OperationEvent(this, 0, "solution_updated_event"));
    };
    //--------------------------------------------------------------------------------------------------------------
    // вернуть фитнес лучшего
    public double GetBestPrice()
    {  return dllAdapter.GetBestPrice();
    }
    //--------------------------------------------------------------------------------------------------------------
    //вернуть 0-е решение при помощи dllAdapter-a
    public Solution getCurrentBestSolution()
    {  return new Solution(dllAdapter, (int)0);//вернуть 0-е решение при помощи dllAdapter-a
    }
    //--------------------------------------------------------------------------------------------------------------
    // вернуть index-е решение при помощи dllAdapter-a
    public Solution getSolutionByIndex(int index)
    { return new Solution(dllAdapter, (int)index);//вернуть index-е решение при помощи dllAdapter-a
    }
    //--------------------------------------------------------------------------------------------------------------
    // рисовать решение
    public void drawSolution (Solution  solution)
    { dllAdapter.g2m.CreateMapByGraph(solution);
    }
    //--------------------------------------------------------------------------------------------------------------
    // вернуть состояние процесса оптимизации
    public int is_in_progress(){return is_in_progress;}
    //--------------------------------------------------------------------------------------------------------------
    // вычислить затухание в кабеле на указанной длине волны
    public double calcCableLinkAttenuation( SchemeCableLink scl, int wavelength)
    {  double len_db = -1;
       double length = -1, attenuation = 0;
       String atten_str = "Attenuation_" + Integer.toString(wavelength);

       if(scl!=null){ length = scl.getOpticalLength()/1000;}
       else
       { System.err.println( "Error in CreateGraphByMap(...): SchemeCableLink = null");
         javax.swing.JOptionPane.showMessageDialog(Environment.getActiveWindow(),
             "В схеме нет ни одной линии связи", "ошибка данных", javax.swing.JOptionPane.OK_OPTION);
    return -1;
       }
       // затухание кабеля берём по затуханию одного из его волокон - нулевого( считаем , что характеристики всех волокон в одном кабеле одинаковые
       if(scl.cable_threads == null)
       { System.err.println("Error in CreateGraphByMap(...): SchemeCableLink - scl.cable_threads = null");
       }
       if(scl.cable_threads.size() == 0)
       { System.err.println("Error in CreateGraphByMap(...): SchemeCableLink - scl.cable_threads.size() = 0");
         javax.swing.JOptionPane.showMessageDialog(Environment.getActiveWindow(),
             "В кабеле "+scl.getName()+" не заданы волокна", "ошибка считывания параметров сети", javax.swing.JOptionPane.OK_OPTION);
    return -1;
       }
       SchemeCableThread thr = (SchemeCableThread)scl.cable_threads.iterator().next();
       LinkType cl_type = (LinkType)Pool.get(LinkType.typ, thr.link_type_id);
       if(cl_type!=null)
       { attenuation = Double.parseDouble( ( (Characteristic)cl_type.characteristics.get(atten_str)).value);
       }
       else
       { System.err.println("CreateGraphByMap(Scheme): Attenuation " + atten_str + " in cable thread "+scl.getName() + " is not set. Impossible to start optimization.");
         javax.swing.JOptionPane.showMessageDialog(Environment.getActiveWindow(),
             "Потери " + atten_str + " в волокне кабеля " + scl.getName() + " не заданы.\n Начать оптимизацию невозможно.",
             "ошибка считывания параметров сети", javax.swing.JOptionPane.OK_OPTION);
    return -1;
       }
       if( attenuation<0 )
       { System.err.println("CreateGraphByMap(Scheme): Attenuation " + atten_str
                            +" in cable thread " + scl.getName()+" ="+attenuation + ". Impossible to start optimization.");
         javax.swing.JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Потери "+atten_str+" в волокне кабеля "
             +scl.getName()+" не заданы \n Начать оптимизацию невозможно.", "ошибка данных", javax.swing.JOptionPane.OK_OPTION);
    return -1;
       }
       len_db = length*attenuation;
       return len_db;
    }
    //--------------------------------------------------------------------------------------------------------------
    // вычислить затухание динка
    public double calcLinkAttenuation( SchemeLink sl, int wavelength)
    {  double length = -1, attenuation = 0;
       double len_db = -1;// len_db = length*attenuation - длина в децибелах
       String atten_str = "Attenuation_" + Integer.toString(wavelength);
       if(sl!=null)
       { length = sl.getOpticalLength()/1000;
       }
       else
       { System.err.println( "CreateGraphByMap(...): SchemeLink = null");
         javax.swing.JOptionPane.showMessageDialog(Environment.getActiveWindow(),
             "Ошибка считывания линка схемы.\n Начать оптимизацию невозможно.", "ошибка считывания сети", javax.swing.JOptionPane.OK_OPTION);
    return -1;
       }

       if(!sl.link_id.equals("")) // если это не каталожный элемент
       { Link l =(Link)Pool.get(Link.typ, sl.link_id);
         if(l!= null && l.characteristics.get(atten_str) != null)
         {  attenuation = Double.parseDouble( ((Characteristic)l.characteristics.get(atten_str)).value );
         }
       }
       else // если каталожный, то обращаемся по типу
       { LinkType l_type = (LinkType)Pool.get(LinkType.typ, sl.link_type_id);
         if(l_type!=null)
         { attenuation = Double.parseDouble( ((Characteristic)l_type.characteristics.get(atten_str)).value );
         }
         else
         { System.err.println("CreateGraphByMap(Scheme): Attenuation "+atten_str + " in thread "+sl.getName() + " is not set. Impossible to start optimization.");
           javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(), "Потери "+atten_str+" в волокне "+sl.getName()
               + " не заданы.\n Начать оптимизацию невозможно.", "ошибка считывания параметров сети", javax.swing.JOptionPane.OK_OPTION);
    return -1;
         }
       }
      if(attenuation == -1)
      { System.err.println("CreateGraphByMap(Scheme): Attenuation " + atten_str
                           +" in thread " + sl.getName()+" is not set. Impossible to start optimization.");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(), "Потери "+atten_str+" в волокне "
            +sl.getName()+" не заданы.\n Начать оптимизацию невозможно.", "ошибка данных", javax.swing.JOptionPane.OK_OPTION);
    return -1;
      }
      else if(attenuation < 0)
      { System.err.println("CreateGraphByMap(Scheme): Attenuation " + atten_str
                          +" in thread " + sl.getName()+" =" + attenuation + ". Impossible to start optimization.");
         javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(), "Потери "+atten_str+" в волокне "
            +sl.getName()+" равны " + attenuation + ".\n Начать оптимизацию невозможно.", "недопустимое значение параметра", javax.swing.JOptionPane.OK_OPTION);
    return -1;
      }

      len_db = length*attenuation;
      return len_db;
    }
  //------------------------------------------------------------------------------------------------------
  public void setCableLinksOptimizeAttributes()
  { for( Iterator cls = mdiMain.scheme.getAllCableLinks().iterator(); cls.hasNext();) //по всем кабелям ( то есть по всем  рёбрам )
    { SchemeCableLink scl = (SchemeCableLink)cls.next();
      String scl_id = scl.id;
      ElementAttribute att = (ElementAttribute)scl.attributes.get("optimizerRibAttribute");
      //если этот линк уже был прописан при загрузке в путь, то не передааём его в dll, то есть его  для нас не существует
      if(isLinkTestedBefore(scl_id))
      { if(att==null)
        { att = new ElementAttribute();
          att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
          att.name = "Активность";
          att.type_id = "optimizerRibAttribute";
          att.value = "tested";
          scl.attributes.put(att.type_id, att);
        }
        else
        { att.value = "tested";
        }
    continue;
      }
      else
      { if(att==null) // если атрибут не существует, то создаём его и прописываем active
        { //System.out.println("CreateGraphByMap(Scheme): атрибут оптимизации optimizerRibAttribute волокна " + scl_id + " не задан. Создаём автоматически.");
          att = new ElementAttribute();
          att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
          att.name = "Активность";
          att.type_id = "optimizerRibAttribute";
          att.value = "active"; // по умолчанию стоит active
          scl.attributes.put(att.type_id, att);
        }
        else
        { att.value = "active";
        }
      }
    }
  }
  //------------------------------------------------------------------------------------------------------
  public void setLinksOptimizeAttributes()
  { for( Iterator ls = this.mdiMain.scheme.getAllLinks().iterator(); ls.hasNext();) // по всем линкам
    { SchemeLink sl = (SchemeLink)ls.next();
      ElementAttribute att = (ElementAttribute )sl.attributes.get("optimizerRibAttribute");
      // КИСовые линки пропускаем, помечая их как "passive"
      if( isLinkRtuConnected(sl) )
      { if(att==null)
        { att = new ElementAttribute();
          att.id = this.mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
          att.name = "Активность";
          att.type_id = "optimizerRibAttribute";
          att.value = "passive";
          sl.attributes.put(att.type_id, att);
        }
        else{att.value = "passive";}
    continue;
      }
      // уже протестированные линки пропускаем, помечая их как "tested"
      if( isLinkTestedBefore(sl.getId()) )
      { if(att==null)
        { att = new ElementAttribute();
          att.id = this.mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
          att.name = "Активность";
          att.type_id = "optimizerRibAttribute";
          att.value = "tested";
          sl.attributes.put(att.type_id, att);
        }
        else{ att.value = "tested";}
    continue;
      }
      // дублирующие линки пропускаем, помечая их как "passive"
      if( isLinkNodesConnectedBefore(sl) )
      { if(att==null)
        { att = new ElementAttribute();
          att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
          att.name = "Активность";
          att.type_id = "optimizerRibAttribute";
          att.value = "tested";
          sl.attributes.put(att.type_id, att);
        }
        else{ att.value = "tested";}
    continue;
      }
      // если линк можно тестировать, но атрибут не существует, то создаём его и прописываем active
      if( att == null )
      { //System.out.println("CreateGraphByMap(Scheme): атрибут оптимизации optimizerRibAttribute волокна " + sl_id + " не задан. Инициализирован значением по умолчанию.");
        att = new ElementAttribute();
        att.id = mdiMain.aContext.getDataSourceInterface().GetUId(ElementAttribute.typ);
        att.name = "Активность";
        att.type_id = "optimizerRibAttribute";
        att.value = "active";// по умолчанию стоит active
        sl.attributes.put(att.type_id, att);
      }
    }
  }
  //---------------------------------------------------------------------------
  // проверить, подсоединяет ли данный линк к схеме КИС
  private boolean isLinkRtuConnected (SchemeLink sl)
  { boolean res = false;
    SchemePort sp = (SchemePort)Pool.get(SchemePort.typ, sl.source_port_id);
    if(!sp.access_port_type_id.equals(""))
    { res = true;
    }
    if(!res) // если один порт не входит в КИС, то надо проверить ещё и второй порт
    { sp = (SchemePort)Pool.get(SchemePort.typ, sl.target_port_id);
      if(!sp.access_port_type_id.equals(""))
      { res = true;
      }
    }
    return res;
  }
  //-------------------------------------------------------------------------
  //проверить был ли этот линк или кабельный линк с таким id уже прописан при загрузке в какой-либо путь
  // Учитывает эквивалентность дублирующих линков в муфтах и прочих устройствах (пАрах вершин) !!! пока ещё не учитывает
  public boolean isLinkTestedBefore (String link_id)
  { boolean res = false;
    if(original_paths == null)
  return res;
    for( Iterator paths = original_paths.iterator(); paths.hasNext();) // по всем путям
    { SchemePath sp = (SchemePath)paths.next();
      for(Iterator links = sp.links.iterator(); links.hasNext();)
      { PathElement link = (PathElement)links.next();
        if(link.link_id.equals(link_id))
        {  res = true;
      break;
        }
      }
      if(res)
    break; // for(Enumeration links = sp.links.elements(); links.hasMoreElements();)
    }
    return res;
  }
  //------------------------------------------------------------------------------------------------------
  // были ли уже связаны узлы, которые соединяются линком, другим линком из пути, который был изначально загружен вместе со схемой
  public boolean isLinkNodesConnectedBefore(SchemeLink sl)
  { boolean res = false;

    String se1_id = ((SchemeElement)this.mdiMain.scheme.getSchemeElementByPort(sl.source_port_id)).id;
    String se2_id = ((SchemeElement)this.mdiMain.scheme.getSchemeElementByPort(sl.target_port_id)).id;
    String ose1_id, ose2_id, osl_id;
    for(Iterator s = originally_lconnected_nodes.iterator(); s.hasNext(); )
    { ose1_id = (String) s.next();  ose2_id = (String) s.next();  osl_id = (String) s.next();
      if( (ose1_id.equals(se1_id)&&ose2_id.equals(se2_id)) ||  (ose1_id.equals(se2_id)&&ose2_id.equals(se1_id)) )
      {  res = true;
    break;
      }
    }
    return res;
  }
  //------------------------------------------------------------------------------------------------------
}
