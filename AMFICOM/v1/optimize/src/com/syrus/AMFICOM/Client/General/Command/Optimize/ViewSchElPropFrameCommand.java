package com.syrus.AMFICOM.Client.General.Command.Optimize;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

import com.syrus.AMFICOM.Client.Schematics.Elements.*;//окно свойств элемента

// команда "открыть физическую схему сети" (загрузить с сервера)
//=================================================================================================
public class ViewSchElPropFrameCommand extends VoidCommand
{
  OptimizeMDIMain    mdiMain;
  ApplicationContext aContext;
  Scheme scheme;
  String scheme_id;
  Dispatcher dispatcher;
  //--------------------------------------------------------------------------
  public ViewSchElPropFrameCommand(Dispatcher dispatcher, ApplicationContext aContext, OptimizeMDIMain optimizeMDIMain)
  {
    this.aContext = aContext;
    this.dispatcher = dispatcher;
    this.mdiMain = optimizeMDIMain;
  }
  //--------------------------------------------------------------------------
  public Object clone()
  {	return new ViewSchElPropFrameCommand(dispatcher, aContext, mdiMain);
  }
  //--------------------------------------------------------------------------
  public void execute()
  {  System.out.println("ViewSchElPropFrameCommand.execute() : starting ...");
    // проверка прав доступа
    Checker checker = new Checker( aContext.getDataSourceInterface() );
    if( !checker.checkCommand(checker.openSchemeToBeOptimized) )
    { System.err.println("ViewSchElPropFrameCommand.execute : Checker.checkCommand = false; aborting...");
  return;
    }
  	System.out.println("ViewSchElPropFrameCommand.execute() : MapChooserDialog.retCode = MapChooserDialog.RET_OK; continuing...");

    ElementsListFrame frame = null;
    for(int i=0; i<mdiMain.desktopPane.getComponents().length; i++)
    { Component comp = mdiMain.desktopPane.getComponent(i);
      if(comp instanceof ElementsListFrame)
      { frame = (ElementsListFrame)comp;
      }
    }
    if(frame == null)
    { frame = new ElementsListFrame(aContext, false);
      mdiMain.desktopPane.add(frame);
      frame.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
    }
    //класс не мой, поэтому устанавливаем размер не методом place()
    Dimension dim = new Dimension(mdiMain.desktopPane.getWidth(), mdiMain.desktopPane.getHeight());
    int height = (int)(0.15*dim.height + 2 + 28), width = (int)(dim.width*0.22 - 2); // "+2" потому что просто подгонял при разрешении 1280*1024
    frame.setSize(width, height);
    frame.setLocation( (int)(dim.width-width + 1), (int)(dim.height*(1-0.2)-height + 2.5) );
    frame.setIconifiable(true);
    frame.setClosable(true);
    frame.setFrameIcon( new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/general.gif")) );
    frame.setVisible(true);
    frame.toFront();

    System.out.println("ViewSchElPropFrameCommand.execute() : done");
  }
  //--------------------------------------------------------------------------
}
//=================================================================================================
