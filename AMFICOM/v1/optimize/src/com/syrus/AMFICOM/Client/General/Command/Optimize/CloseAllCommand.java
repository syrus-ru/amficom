package com.syrus.AMFICOM.Client.General.Command.Optimize;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Optimize.*;

// ������� "������� ��" - ��������� ��� ����, ����� ��������
//========================================================================================================
public class CloseAllCommand extends VoidCommand
{ private Dispatcher dispatcher;
  private ApplicationContext aContext;
  private JDesktopPane desktop;
  private OptimizeMDIMain mdiMain;
  //--------------------------------------------------------------------------------
  public CloseAllCommand(){}
  //--------------------------------------------------------------------------------
  public CloseAllCommand (  Dispatcher dispatcher, JDesktopPane desktop,
                            ApplicationContext aContext, OptimizeMDIMain optimizeMDIMain )
  {	 this.dispatcher = dispatcher;
     this.desktop = desktop;
     this.aContext = aContext;
     this.mdiMain = optimizeMDIMain;
  }
  //--------------------------------------------------------------------------------
  public void setParameter(String field, Object value)
  {	 if(field.equals("dispatcher"))
     { setDispatcher((Dispatcher)value);
     }
     else if(field.equals("aContext"))
     { setApplicationContext((ApplicationContext)value);
     }
  }
  //--------------------------------------------------------------------------------
  public void setDispatcher(Dispatcher dispatcher)
  { this.dispatcher = dispatcher;
  }
  //--------------------------------------------------------------------------------
  public void setApplicationContext(ApplicationContext aContext)
  { this.aContext = aContext;
  }
  //--------------------------------------------------------------------------------
  public Object clone()
  { return new CloseAllCommand(dispatcher, desktop, aContext, mdiMain);
  }
  //--------------------------------------------------------------------------------
  public void execute()
  {	System.out.println("CloseAllCommand.execute() starting ...");

    // ���� ����� ���� �������, �� ���� �������
    dispatcher.notify(new SchemeElementsEvent(this, mdiMain.scheme, SchemeElementsEvent.CLOSE_SCHEME_EVENT));
    new MapOptCloseCommand(dispatcher, aContext, mdiMain).execute();// ��������� �����

    closeAllInternalFrames();// ������� ��� ����, ������� ���� ������� ��� �������� �����
    disableSomeMenuItems( aContext.getApplicationModel() ); //���������� ���� � ��������� ��������� (�.�. ����� ��� ������ ���������)

    dispatcher.notify(new OperationEvent(this, 0, "close_all"));// �� ��� ���������, ��������, ������
    mdiMain.aContext.getDispatcher().notify(new StatusMessageEvent("����� �� ���������"));
    System.out.println("CloseAllCommand.execute() - done");
  }
  //--------------------------------------------------------------------------------
  // ������� ��� ����, ������� ���� ������� ��� ������� �����
  private void closeAllInternalFrames()
  { if(mdiMain.kisSelectFrame != null) // ����, ���������� ���������� � ��������������� � ����� ������������
    { mdiMain.kisSelectFrame.dispose();
      mdiMain.kisSelectFrame = null; // dispose() ������ ���������� �������, �� ���� ���������� ������������
    }
    if(mdiMain.iterHistFrame != null) // ���� ������� ���� �����������
    { mdiMain.iterHistFrame.dispose();
      mdiMain.iterHistFrame = null;
    }
    if(mdiMain.paramsFrame != null) // ���� ������� ���������� �����������
    { mdiMain.paramsFrame.dispose();
      mdiMain.paramsFrame = null;
    }
    if(mdiMain.solutionFrame != null) // ���� ��������� ����� �������� ������ �� �������
    { mdiMain.solutionFrame.dispose();
      mdiMain.solutionFrame = null;
    }
    if(mdiMain.nodesModeFrame != null) // ���� ������� ������� ����� ( fixed , active )
    { mdiMain.nodesModeFrame.dispose();
      mdiMain.nodesModeFrame = null;
    }
    if(mdiMain.ribsModeFrame != null) // ���� ������� ������� ���� ( active )
    { mdiMain.ribsModeFrame.dispose();
      mdiMain.ribsModeFrame = null;
    }
    if(mdiMain.schemeFrame != null) // ���� ����������� �����
    { mdiMain.schemeFrame.dispose();
      mdiMain.schemeFrame = null;
    }
    if(mdiMain.mapFrame != null) // ���� ����������� �����
    { //mdiMain.mapFrame.dispose();
      mdiMain.mapFrame = null;
    }
  }
  //--------------------------------------------------------------------------------
  // ������� ����������� ��������� ������ ���� (������ �������� ����������� �� ������� close_all)
  private void disableSomeMenuItems( ApplicationModel aModel )
  {
//      aModel.setAllItemsEnabled(false);
//      aModel.enable("menuSession");
//      aModel.enable("menuSessionNew");
//      aModel.enable("menuSessionConnection");
      aModel.enable("menuExit");
      aModel.enable("menuView");
      aModel.enable("menuHelp");
      aModel.enable("menuHelpAbout");

      aModel.enable("menuScheme"); // ���� "�����"
      aModel.disable("menuMapOpen");
      aModel.disable("menuSchemeOpen");
      aModel.disable("menuSchemeSave");
      aModel.disable("menuSchemeSaveAs");
      aModel.disable("menuLoadSm");
      aModel.disable("menuClearScheme");
      aModel.disable("menuSchemeClose");
      aModel.enable("menuView"); // ���� "���"
      aModel.disable("menuViewMap");
      aModel.disable("menuViewScheme");
      aModel.disable("menuViewMapElProperties");
      aModel.disable("menuViewSchElProperties");
      aModel.disable("menuViewSolution");
      aModel.disable("menuViewKIS");
       aModel.disable("menuViewMode");
      aModel.disable("menuViewParams");
      aModel.disable("menuViewGraph");
      aModel.disable("menuViewShowall");
      aModel.enable("menuOptimize"); // ���� "�����������"
      aModel.disable("menuOptimizeStart");
      aModel.disable("menuOptimizeCriteria");
      aModel.disable("menuOptimizeMode");
      aModel.disable("menuOptimizeStop");
      aModel.disable("menuReportCreate");

      aModel.fireModelChanged("");
  }
  //-----------------------------------------------------------------------------------------
}
//========================================================================================================