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


// команда "очистить от всех путей, которые были когда-либo вписаны в схему"
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
//    //проверка прав доступа
//    Checker checker = new Checker( aContext.getDataSourceInterface() );
//    if( !checker.checkCommand(checker.openSchemeToBeOptimized) ) // !!! не openSchemeToBeOptimized
//    {
//  return;
//    }
    System.out.println("SchemePathsClearCommand.execute() - starting ...");
    mdiMain.optimizerContext.original_paths.clear();//очищаем все загруженные вместе со схемой пути
    mdiMain.optimizerContext.originally_lconnected_nodes.clear();//очищаем все записи о линках, помеченных как дублирующие (tested)
    if(mdiMain.scheme == null)
    { System.err.println("SchemePathsClearCommand.execute(): mdiMain.scheme = null - aborting ...");
  return;
    }
    // при очистке возвращаем атрибуты волокон из ранее прописанных "tested" в состояние "active"
    for( Enumeration cls = mdiMain.scheme.getAllCableLinks(); cls.hasMoreElements();) // по всем кабелям
    { SchemeCableLink scl = (SchemeCableLink) cls.nextElement();
      ElementAttribute att = (ElementAttribute)scl.attributes.get("optimizerRibAttribute");
      if(att != null)
      { if( att.value.equals("tested") )
        { att.value = "active";}
      }
    }
    for( Enumeration cls = mdiMain.scheme.getAllLinks(); cls.hasMoreElements();) // по всем линкам
    { SchemeLink sl = (SchemeLink) cls.nextElement();
      ElementAttribute att = (ElementAttribute)sl.attributes.get("optimizerRibAttribute");
      if(att != null)
      { if( att.value.equals("tested") )
        { att.value = "active";}
      }
    }
    mdiMain.ribsModeFrame.repaint();//перерисовать обновлённые значения

    mdiMain.optimizerContext.solution = new Solution();//создать пустое решение
    dispatcher.notify(new OperationEvent(this, 0, "solution_overwrite_event"));//прописать пустое решение поверх
    mdiMain.aContext.getDispatcher().notify(new StatusMessageEvent("схема очищена"));
    System.out.println("SchemePathsClearCommand.execute() - done");
  }
  //--------------------------------------------------------------------------
}
