package com.syrus.AMFICOM.Client.General.Command.Optimize;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Config.*;
//import com.syrus.AMFICOM.Client.General.Command.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Optimize.*;

public class ViewMapCommand extends VoidCommand
{
  Dispatcher dispatcher;
  ApplicationContext aContext;
  JDesktopPane desktop;
  ApplicationModelFactory factory;
  OptimizeMDIMain parent;
  //-------------------------------------------------------------------------------------------------------------
  public ViewMapCommand(){}
  //-------------------------------------------------------------------------------------------------------------
  public ViewMapCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext,
                        ApplicationModelFactory factory, OptimizeMDIMain parent )
  { this.dispatcher = dispatcher;
    this.desktop = desktop;
    this.aContext = aContext;
    this.factory = factory;
    this.parent = parent;
  }
  //-------------------------------------------------------------------------------------------------------------
  public void setParameter(String field, Object value)
  { if(field.equals("dispatcher"))
     setDispatcher((Dispatcher)value);
    else if(field.equals("aContext"))
     setApplicationContext((ApplicationContext )value);
  }
  //-------------------------------------------------------------------------------------------------------------
  public void setDispatcher(Dispatcher dispatcher)
  { this.dispatcher = dispatcher;
  }
  //-------------------------------------------------------------------------------------------------------------
  public void setApplicationContext(ApplicationContext aContext)
  { this.aContext = aContext;
  }
  //-------------------------------------------------------------------------------------------------------------
  public Object clone()
  { return new ViewMapCommand(dispatcher, desktop, aContext, factory, parent);
  }
  //-------------------------------------------------------------------------------------------------------------
  public void execute()
  { System.out.println("ViewMapCommand().execute(): starting ..." );
    if(parent.mapContext == null)
    {  System.out.println("ViewMapCommand().execute(): mdiMain.MapContext = null. aborting ..." );
   return;
    }
    { ApplicationContext aC = new ApplicationContext();
      aC.setApplicationModel(factory.create());
      aC.setConnectionInterface(aContext.getConnectionInterface());
      aC.setSessionInterface(aContext.getSessionInterface());
      aC.setDataSourceInterface( aC.getApplicationModel().getDataSource(aContext.getSessionInterface()) );
      aC.setDispatcher(dispatcher);
      System.out.println("Dispatcher " + dispatcher);

      NewMapViewCommand com2 = new NewMapViewCommand(dispatcher, desktop, aContext, factory);
      com2.execute();
      if(com2.frame == null) // такое может быть в случае если одна карта уже открыта (два окна карты из-под одной сессии открывать запрещено, так как отжирается слишком много памяти)
  {return;}

      while(!com2.frame.isVisible()) // ждём, пока карта не нарисуется
      { try
        { Thread.currentThread().sleep(100);
        }
        catch(InterruptedException ie)
        { ie.printStackTrace();
        }
      }
      com2.frame.setMapContext(parent.mapContext);
      // устанавливаем размер окна карты
      Dimension dim = parent.scrollPane.getSize();
      int width = (int)( (1.0-0.22)*dim.width ), height = (int)( (1.0-0.2)*dim.height );
      com2.frame.setBounds( 0, 0, width, height );
      parent.mapFrame = com2.frame;
      parent.mapFrame.setVisible(true); parent.mapFrame.toFront();
      //окно свойств элементов карты
      new ViewOptMapPropertiesCommand (parent.desktopPane, aContext).execute();
      // new ViewOptMapElementsCommand(parent.desktopPane, aContext).execute();
    }
    System.out.println("ViewMapCommand().execute(): done" );
  }
  //-------------------------------------------------------------------------------------------------------------
}