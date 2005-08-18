// ������ �������� ���� ��� ������������ ������ ( ����������� �������� � �� )
package com.syrus.AMFICOM.Client.Optimize;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;

public class InfoToStore { 
  public Identifier id;  // ���������� ������������� �������, ���������� ������� ������������� �� �������������� ������
  public Identifier scheme_id;  // ������������� �����, ������� ������������ � ����� �����������
  public Identifier solution_compact_id; // ������������� ����������� ������� �������, ������� �������� � �� �������� �� �����
  public String date = ""; // ���� �������� �������
  public OptimizeMDIMain mdiMain;
  public OptimizeParameters opt_prm;

  public int optimize_mode; // ����� �����������
  public double price; // ���� �������
  public double wavelen;  // ����� �����, �� ������� ����������� ������������
  public double iterations;
  public double[] refl_prices;
  public double[] refl_ranges;
  public String[] refl_names;  // ID ��������������
  public double[] switch_prices;
  public String[] switch_names; // ID ������������
  public double[] switch_nports;
  //---------------------------------------------------------------------------------------
  public InfoToStore(OptimizeMDIMain  mdiMain)
  {  this.mdiMain = mdiMain;
     id = mdiMain.aContext.getDataSourceInterface().GetUId(ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE);
     update();
  }
  //---------------------------------------------------------------------------------------
  public void update()
  {  this.opt_prm = mdiMain.optimizerContext.opt_prs;
     scheme_id = mdiMain.scheme.getId(); // mdiMain.scheme ����� ������� ��������� ����� ������
     date = (new SimpleDateFormat()).format(new Date());
     optimize_mode = mdiMain.optimizerContext.optimize_mode;
     price = mdiMain.optimizerContext.solution.price;
     iterations = mdiMain.optimizerContext.iterations;
     wavelen =  mdiMain.optimizerContext.wavelength;

     Vector tmp_names = new Vector();
     Vector tmp_prices = new Vector();
     Vector tmp_ranges = new Vector();
     Vector tmp_nports = new Vector();
     Vector reflectometers = mdiMain.kisSelectFrame.reflectometers;//������������� �� ������ � ���� kisSelect
     Vector switches = mdiMain.kisSelectFrame.switchers;
     // ����������� �������������
     for(Enumeration rs = reflectometers.elements(); rs.hasMoreElements();)
     { ReflectometerShortData rsd = (ReflectometerShortData) rs.nextElement();
       if( rsd.wavelength.contains( Integer.toString((int)wavelen)) )// ����� ����� �������������� �����
       { if( !rsd.price.equals("") ) // ���� ���� ������
         { tmp_names.add( rsd.ID); //reflectometerName );
           tmp_prices.add( rsd.price );
           tmp_ranges.add( rsd.dynamicWindow.elementAt(0) );
         }
       }
     }
     //��������� ������� � ������   refl_prices = ( (Double[])tmp_prices.toArray( new Double[0]) );
     refl_prices = new double[tmp_prices.size()];
     refl_names = new String[tmp_names.size()];
     refl_ranges = new double[tmp_ranges.size()];
     for(int i=0; i<tmp_prices.size(); i++)
     { refl_prices[i] = Double.parseDouble( tmp_prices.elementAt(i).toString() );
       refl_names[i] = (String)tmp_names.elementAt(i);
       refl_ranges[i] = Double.parseDouble( tmp_ranges.elementAt(i).toString() );
     }
     // ����������� �����
     tmp_names.clear(); tmp_prices.clear(); tmp_nports.clear();
     for(Enumeration sws = switches.elements(); sws.hasMoreElements();)
     { SwitcherShortData swsd = (SwitcherShortData)sws.nextElement();
       if( !swsd.price.equals("")) // ���� ���� ������
       { tmp_names.add( swsd.ID);
         tmp_prices.add( swsd.price);
         tmp_nports.add( swsd.portNumber );
       }
     }
     switch_prices = new double[tmp_prices.size()]; // ��������� ������� � ������
     switch_names = new String[tmp_names.size()];
     switch_nports = new double[tmp_nports.size()];
     for(int i=0; i<tmp_prices.size(); i++)
     { switch_prices[i] = Double.parseDouble( tmp_prices.elementAt(i).toString() );
       switch_names[i] = (String)tmp_names.elementAt(i);
       switch_nports[i] = Double.parseDouble( tmp_nports.elementAt(i).toString() );
     }
  }
  //---------------------------------------------------------------------------------------
}