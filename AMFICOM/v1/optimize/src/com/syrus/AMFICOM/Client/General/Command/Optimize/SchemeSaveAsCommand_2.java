package com.syrus.AMFICOM.Client.General.Command.Optimize;

import com.syrus.AMFICOM.Client.Map.UI.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Scheme.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

import com.syrus.AMFICOM.Client.General.Checker;

//SchemeSaveAsCommand есть вдругом модуле, поэтому  пишем "_2"
public class SchemeSaveAsCommand_2 extends VoidCommand
{ OptimizeMDIMain    mdiMain;
  ApplicationContext aContext;
  Dispatcher dispatcher;
  //--------------------------------------------------------------------------
  public SchemeSaveAsCommand_2(Dispatcher dispatcher, ApplicationContext aContext, OptimizeMDIMain mdiMain)
  { this.mdiMain = mdiMain;
    this.aContext = aContext;
    this.dispatcher = dispatcher;
  }
  //--------------------------------------------------------------------------
  public Object clone()
  { return new SchemeSaveAsCommand_2(dispatcher, aContext, mdiMain);
  }
  //--------------------------------------------------------------------------
  public void execute()
  { // проверка прав доступа
    Checker checker = new Checker( aContext.getDataSourceInterface() );
    if( !checker.checkCommand(checker.saveResultOfOptimization) )
    {
  return;
    }
    System.out.println("SchemeSaveAsCommand_2.execute() - starting ...");
    DataSourceInterface dataSource = aContext.getDataSourceInterface();
    new SchemeSaveAsCommand(aContext, mdiMain.schemeFrame.schemePanel,mdiMain.schemeFrame.schemePanel ).execute();
    // в процессе записи схема подменяется на клон, поэтому надо переприсвоить ссылку на схему в mdiMain
    mdiMain.scheme  = mdiMain.schemeFrame.schemePanel.scheme;// у mdiMain.schemeFrame.schemePanel.scheme уже прописан клон
    System.out.println("saving additional info about scheme-decision: starting...");
    InfoToStore additional_info = new InfoToStore( mdiMain );
    additional_info.setTransferableFromLocal();
    Pool.put(InfoToStore.typ, additional_info.id, additional_info.transferable);
    dataSource.SaveSchemeOptimizeInfo(additional_info.id);
    System.out.println("saving additional info about scheme-decision: done");

    dispatcher.notify(new OperationEvent(this, 0, "scheme_saved_event"));
    System.out.println("SchemeSaveAsCommand_2.execute() - done");
  }
  //--------------------------------------------------------------------------
}
