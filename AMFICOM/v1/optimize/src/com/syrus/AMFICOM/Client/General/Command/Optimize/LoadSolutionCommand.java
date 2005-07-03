package com.syrus.AMFICOM.Client.General.Command.Optimize;

import com.syrus.AMFICOM.Client.Map.UI.*;
import com.syrus.AMFICOM.Client.Optimize.UI.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Optimize.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

// команда "загрузить решение для уже открытой схемы"
public class LoadSolutionCommand extends VoidCommand
{ OptimizeMDIMain    mdiMain;
  ApplicationContext aContext;
  Dispatcher dispatcher;
  //--------------------------------------------------------------------------
  public LoadSolutionCommand(Dispatcher dispatcher, ApplicationContext aContext, OptimizeMDIMain mdiMain)
  { this.mdiMain = mdiMain;
    this.aContext = aContext;
    this.dispatcher = dispatcher;
  }
  //--------------------------------------------------------------------------
  public Object clone()
  { return new LoadSolutionCommand(dispatcher, aContext, mdiMain);
  }
  //--------------------------------------------------------------------------
  public void execute()
  { // проверка прав доступа
    Checker checker = new Checker( aContext.getDataSourceInterface() );
    if( !checker.checkCommand(checker.openSchemeToBeOptimized) ) // !!!  не openSchemeToBeOptimized , но пока так
    {
  return;
    }
    System.out.println("LoadSolutionCommand.execute() - starting ...");
    if(mdiMain.scheme == null)
    { System.err.println("LoadSolutionCommand.execute(): mdiMain.scheme = null - aborting ...");
  return;
    }
    DataSourceInterface dataSource = aContext.getDataSourceInterface();
    // меню выбора схемы
    dataSource.LoadSchemeMonitoringSolutions();// подгружаем в пул список всех SolutionCompact
    MapChooserDialog mcd = new MapChooserDialog(dataSource);
    DataSet dataSet = new DataSet(Pool.getHash(SolutionCompact.typ));
    // отфильтровываем : оставляем только те решения, которые относятся к нашей схеме
    for(int i=0; i<dataSet.size(); i++)
    { if ( !((SolutionCompact)dataSet.get(i)).scheme_id.equals(mdiMain.scheme.id))
      {  dataSet.removeAt(i);
         i--; //так как вектор стал короче
      }
    }
    ObjectResourceDisplayModel odm = new SolutionCompactDisplayModel();
    mcd.setContents(odm, dataSet);

    ObjectResourceTableModel ortm = (ObjectResourceTableModel )mcd.listPane.getTable().getModel();

    mcd.setTitle("Решение");
    mcd.setModal(true);
    mcd.setVisible(true);

    if(mcd.retCode == mcd.RET_CANCEL)
    { System.out.println("LoadSolutionCommand.execute() : MapChooserDialog.retCode = MapChooserDialog.RET_CANCEL; aborting...");
  return;
    }
    if(mcd.retCode == mcd.RET_OK)
    {	System.out.println("LoadSolutionCommand.execute() : MapChooserDialog.retCode = MapChooserDialog.RET_OK; continuing...");
      SolutionCompact slc =  (SolutionCompact)mcd.retObject;
      slc.setLocalFromTransferable();//приветсти из вида path_transferable к path
      this.mdiMain.optimizerContext.solution = new Solution(slc);
      this.dispatcher.notify(new OperationEvent(this, 0, "solution_overwrite_event"));//очистить схему и прописать только то, что загрузили
      this.mdiMain.aContext.getDispatcher().notify(new StatusMessageEvent("СМ загружена"));
      // при обновлении схемы открываем всё остальное ( кроме карты )
      new ViewShowallCommand(this.dispatcher, mdiMain.desktopPane, aContext, this.mdiMain).execute();
    }

    System.out.println("LoadSolutionCommand.execute() - done");
  }
  //--------------------------------------------------------------------------
}
