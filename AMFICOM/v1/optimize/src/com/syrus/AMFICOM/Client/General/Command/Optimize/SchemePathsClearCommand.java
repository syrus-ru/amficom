package com.syrus.AMFICOM.Client.General.Command.Optimize;

import com.syrus.AMFICOM.Client.Map.UI.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.Optimize.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Optimize.*;

import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;
import com.syrus.AMFICOM.Client.General.Model.*;


// ������� "�������� �� ���� �����, ������� ���� �����-���o ������� � �����"
public class SchemePathsClearCommand extends VoidCommand
{ OptimizeMDIMain    mdiMain;
  ApplicationContext aContext;
  Dispatcher dispatcher;
  //--------------------------------------------------------------------------
  public SchemePathsClearCommand(Dispatcher dispatcher, ApplicationContext aContext, OptimizeMDIMain mdiMain)
  { this.mdiMain = mdiMain;
    this.aContext = aContext;
    this.dispatcher = dispatcher;
  }
  //--------------------------------------------------------------------------
  public Object clone()
  { return new SchemePathsClearCommand(dispatcher, aContext, mdiMain);
  }
  //--------------------------------------------------------------------------
  public void execute()
  {
//    //�������� ���� �������
//    Checker checker = new Checker( aContext.getDataSourceInterface() );
//    if( !checker.checkCommand(checker.openSchemeToBeOptimized) ) // !!! �� openSchemeToBeOptimized
//    {
//  return;
//    }
    System.out.println("SchemePathsClearCommand.execute() - starting ...");
    mdiMain.optimizerContext.original_paths.clear();//������� ��� ����������� ������ �� ������ ����
    mdiMain.optimizerContext.originally_lconnected_nodes.clear();//������� ��� ������ � ������, ���������� ��� ����������� (tested)
    if(mdiMain.scheme == null)
    { System.err.println("SchemePathsClearCommand.execute(): mdiMain.scheme = null - aborting ...");
  return;
    }
    // ��� ������� ���������� �������� ������� �� ����� ����������� "tested" � ��������� "active"
    for( Enumeration cls = mdiMain.scheme.getAllCableLinks(); cls.hasMoreElements();) // �� ���� �������
    { SchemeCableLink scl = (SchemeCableLink) cls.nextElement();
      ElementAttribute att = (ElementAttribute)scl.attributes.get("optimizerRibAttribute");
      if(att != null)
      { if( att.value.equals("tested") )
        { att.value = "active";}
      }
    }
    for( Enumeration cls = mdiMain.scheme.getAllLinks(); cls.hasMoreElements();) // �� ���� ������
    { SchemeLink sl = (SchemeLink) cls.nextElement();
      ElementAttribute att = (ElementAttribute)sl.attributes.get("optimizerRibAttribute");
      if(att != null)
      { if( att.value.equals("tested") )
        { att.value = "active";}
      }
    }
    mdiMain.ribsModeFrame.repaint();//������������ ���������� ��������

    mdiMain.optimizerContext.solution = new Solution();//������� ������ �������
    dispatcher.notify(new OperationEvent(this, 0, "solution_overwrite_event"));//��������� ������ ������� ������
    mdiMain.aContext.getDispatcher().notify(new StatusMessageEvent("����� �������"));
    System.out.println("SchemePathsClearCommand.execute() - done");
  }
  //--------------------------------------------------------------------------
}
