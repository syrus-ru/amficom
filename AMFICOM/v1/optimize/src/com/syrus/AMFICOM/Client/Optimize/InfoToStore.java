// объект хранящий инфо для формирования отчёта ( сохраняется отдельно в БД )
package com.syrus.AMFICOM.Client.Optimize;

import java.awt.*;
import java.util.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;
import com.syrus.AMFICOM.CORBA.Alarm.*;
import com.syrus.AMFICOM.CORBA.Resource.*;
import com.syrus.AMFICOM.CORBA.Scheme.*;
import java.text.SimpleDateFormat;

public class InfoToStore
{
  public SchemeOptimizeInfo_Transferable transferable = new SchemeOptimizeInfo_Transferable();

  public static String typ = "optimized_scheme_info";// идентификатор класса
  public String id = "";  // уникальный идентификатор объекта, выдаваемый объекту автоматически по идентификатору класса
  public String scheme_id = "";  // идентификатор схемы, которую оптимизируем с этими параметрами
  public String solution_compact_id = ""; // идентификатор компактного формата решения, которое хранится в БД отдельно от схемы
  public String date = ""; // дата создания решения
  public OptimizeMDIMain mdiMain;
  public OptimizeParameters opt_prm;

  public int optimize_mode; // режим оптимизации
  public double price; // цена решения
  public double wavelen;  // длина волны, на которой проводилось тестирование
  public double iterations;
  public double[] refl_prices;
  public double[] refl_ranges;
  public String[] refl_names;  // ID рефлектометров
  public double[] switch_prices;
  public String[] switch_names; // ID коммутаторов
  public double[] switch_nports;
  //---------------------------------------------------------------------------------------
  public InfoToStore(OptimizeMDIMain  mdiMain)
  {  this.mdiMain = mdiMain;
     id = mdiMain.aContext.getDataSourceInterface().GetUId(typ);
     update();
  }
  //---------------------------------------------------------------------------------------
  public void update()
  {  this.opt_prm = mdiMain.optimizerContext.opt_prs;
     scheme_id = (String)mdiMain.scheme.id; // mdiMain.scheme перед вызовом переписан своим клоном
     date = (new SimpleDateFormat()).format(new Date());
     optimize_mode = mdiMain.optimizerContext.optimize_mode;
     price = mdiMain.optimizerContext.solution.price;
     iterations = mdiMain.optimizerContext.iterations;
     wavelen =  mdiMain.optimizerContext.wavelength;

     Vector tmp_names = new Vector();
     Vector tmp_prices = new Vector();
     Vector tmp_ranges = new Vector();
     Vector tmp_nports = new Vector();
     Vector reflectometers = mdiMain.kisSelectFrame.reflectometers;//рефлектометры из списка в окне kisSelect
     Vector switches = mdiMain.kisSelectFrame.switchers;
     // прописываем рефлектометры
     for(Enumeration rs = reflectometers.elements(); rs.hasMoreElements();)
     { ReflectometerShortData rsd = (ReflectometerShortData) rs.nextElement();
       if( rsd.wavelength.contains( Integer.toString((int)wavelen)) )// длина волны предполагается целой
       { if( !rsd.price.equals("") ) // если цена задана
         { tmp_names.add( rsd.ID); //reflectometerName );
           tmp_prices.add( rsd.price );
           tmp_ranges.add( rsd.dynamicWindow.elementAt(0) );
         }
       }
     }
     //переводим векторы в массив   refl_prices = ( (Double[])tmp_prices.toArray( new Double[0]) );
     refl_prices = new double[tmp_prices.size()];
     refl_names = new String[tmp_names.size()];
     refl_ranges = new double[tmp_ranges.size()];
     for(int i=0; i<tmp_prices.size(); i++)
     { refl_prices[i] = Double.parseDouble( tmp_prices.elementAt(i).toString() );
       refl_names[i] = (String)tmp_names.elementAt(i);
       refl_ranges[i] = Double.parseDouble( tmp_ranges.elementAt(i).toString() );
     }
     // прописываем свичи
     tmp_names.clear(); tmp_prices.clear(); tmp_nports.clear();
     for(Enumeration sws = switches.elements(); sws.hasMoreElements();)
     { SwitcherShortData swsd = (SwitcherShortData)sws.nextElement();
       if( !swsd.price.equals("")) // если цена задана
       { tmp_names.add( swsd.ID);
         tmp_prices.add( swsd.price);
         tmp_nports.add( swsd.portNumber );
       }
     }
     switch_prices = new double[tmp_prices.size()]; // переводим векторы в массив
     switch_names = new String[tmp_names.size()];
     switch_nports = new double[tmp_nports.size()];
     for(int i=0; i<tmp_prices.size(); i++)
     { switch_prices[i] = Double.parseDouble( tmp_prices.elementAt(i).toString() );
       switch_names[i] = (String)tmp_names.elementAt(i);
       switch_nports[i] = Double.parseDouble( tmp_nports.elementAt(i).toString() );
     }
  }
  //---------------------------------------------------------------------------------------
  public void setTransferableFromLocal()
  {  transferable.id = this.id;
     transferable.scheme_id = this.scheme_id;
     transferable.solution_compact_id = this.solution_compact_id;
     transferable.len_margin = this.opt_prm.len_margin;
     transferable.date = this.date;
     transferable.optimize_mode = this.optimize_mode;
     transferable.price = this.price;
     transferable.iterations = this.iterations;
     //transferable.wavelength = this.wavelen;
     transferable.mutation_rate = this.opt_prm.mutation_rate;
     transferable.mutation_degree = this.opt_prm.mutation_degree;
     transferable.rtu_delete_prob = this.opt_prm.rtu_delete_prob;
     transferable.rtu_create_prob = this.opt_prm.rtu_create_prob;
     transferable.nodes_splice_prob = this.opt_prm.nodes_splice_prob;
     transferable.nodes_cut_prob  = this.opt_prm.nodes_cut_prob;
     transferable.survivor_rate = this.opt_prm.survivor_rate;

     transferable.refl_prices = new double[this.refl_prices.length];
     for(int i=0; i<refl_prices.length; i++){ transferable.refl_prices[i] = this.refl_prices[i];}
     transferable.refl_names = new String[this.refl_names.length];
     for(int i=0; i<refl_prices.length; i++){ transferable.refl_names[i] = this.refl_names[i];}
     transferable.refl_ranges = new double[this.refl_ranges.length];
     for(int i=0; i<refl_ranges.length; i++){ transferable.refl_ranges[i] = this.refl_ranges[i];}

     transferable.switch_prices = new double[this.switch_prices.length];
     for(int i=0; i<switch_prices.length; i++){ transferable.switch_prices[i] = this.switch_prices[i];}
     transferable.switch_names = new String[this.switch_names.length];
     for(int i=0; i<switch_names.length; i++){ transferable.switch_names[i] = this.switch_names[i];}
     transferable.switch_nports = new double[this.switch_nports.length];
     for(int i=0; i<switch_nports.length; i++){ transferable.switch_nports[i] = this.switch_nports[i];}

  }
  //---------------------------------------------------------------------------------------
  public void setLocalFromTransferable()
  {  this.id = transferable.id;
     this.scheme_id = transferable.scheme_id;
     this.date = transferable.date;

     this.opt_prm.len_margin = transferable.len_margin;
     this.opt_prm.mutation_rate = transferable.mutation_rate;
     this.opt_prm.mutation_degree = transferable.mutation_degree;
     this.opt_prm.rtu_delete_prob = transferable.rtu_delete_prob;
     this.opt_prm.rtu_create_prob = transferable.rtu_create_prob;
     this.opt_prm.nodes_splice_prob = transferable.nodes_splice_prob;
     this.opt_prm.nodes_cut_prob = transferable.nodes_cut_prob;
     this.opt_prm.survivor_rate = transferable.survivor_rate;

     this.optimize_mode = transferable.optimize_mode;
     this.price = transferable.price;
     this.iterations = transferable.iterations;
     // в обратную сторону цены не счтитываем
  }

  //---------------------------------------------------------------------------------------
}