package com.syrus.AMFICOM.Client.General.Command.Optimize;

import com.syrus.AMFICOM.Client.Map.UI.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.Schematics.Elements.*;
import com.syrus.AMFICOM.Client.Schematics.Elements.ElementsListFrame;//окно свойств элемента


// команда "открыть физическую схему сети" (загрузить с сервера)
//=================================================================================================
public class SchemeOpenCommand extends VoidCommand
{
	OptimizeMDIMain    mdiMain;
	ApplicationContext aContext;
	Scheme scheme;
	String scheme_id;
	Dispatcher dispatcher;
	//--------------------------------------------------------------------------
	public SchemeOpenCommand(Dispatcher dispatcher, ApplicationContext aContext, OptimizeMDIMain optimizeMDIMain)
	{
		this.aContext = aContext;
		this.dispatcher = dispatcher;
		this.mdiMain = optimizeMDIMain;
	}
	//--------------------------------------------------------------------------
	public Object clone()
	{	return new SchemeOpenCommand(dispatcher, aContext, mdiMain);
	}
	//--------------------------------------------------------------------------
	public void execute()
	{  System.out.println("SchemeOpenCommand.execute() : starting ...");
		// проверка прав доступа
		Checker checker = new Checker( aContext.getDataSourceInterface() );
		if( !checker.checkCommand(checker.openSchemeToBeOptimized) )
    { System.err.println("SchemeOpenCommand.execute : Checker.checkCommand = false; aborting...");
  return;
    }
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if (dataSource == null)
    {  System.err.println("SchemeOpenCommand.execute() : DataSourceInterface = null;  aborting...");
  return;
    }
    // меню выбора схемы
		MapChooserDialog mcd = new MapChooserDialog(aContext.getDataSourceInterface());
		DataSet dataSet = new DataSet(Pool.getHash(Scheme.typ));
		ObjectResourceDisplayModel odm = new SchemeDisplayModel();
		mcd.setContents(odm, dataSet);

		// отфильтровываем по домену
		ObjectResourceTableModel ortm = (ObjectResourceTableModel )mcd.listPane.getTable().getModel();
		ortm.setDomainId(aContext.getSessionInterface().getDomainId());
		ortm.restrictToDomain(true);//ф-я фильтрации схем по домену

    mcd.setTitle("Физическая схема");
		mcd.setModal(true);
		mcd.setVisible(true);

		if(mcd.retCode == mcd.RET_CANCEL)
    { System.out.println("SchemeOpenCommand.execute() : MapChooserDialog.retCode = MapChooserDialog.RET_CANCEL; aborting...");
  return;
    }
		if(mcd.retCode == mcd.RET_OK)
    {	System.out.println("SchemeOpenCommand.execute() : MapChooserDialog.retCode = MapChooserDialog.RET_OK; continuing...");
      //закрыть всё, что было открыто, если подтверждено открытие схемы
      new CloseAllCommand(dispatcher, mdiMain.desktopPane, aContext, mdiMain).execute();
      scheme = (Scheme)mcd.retObject;
      execute(scheme);
		}
    System.out.println("SchemeOpenCommand.execute() : done");
	}
  //---------------------------
  // часть действий при открытии схемы выделена в отдельную функцию, которая получает уже готовую сылку на схему.
  // Эта ф-я вызывается при открытии карты, когда ссылка на схему уже ивестна из карты.
  public void execute (Scheme scheme)
  { 	mdiMain.scheme = scheme;
      scheme.unpack();

      dispatcher.notify(new OperationEvent(this, 0, "scheme_is_opened"));
      mdiMain.aContext.getDispatcher().notify(new StatusMessageEvent(""));

      mdiMain.optimizerContext.setCableLinksOptimizeAttributes();//обновить информацию об атрибутах кабелей
      mdiMain.optimizerContext.setLinksOptimizeAttributes(); //обновить информацию об атрибутах линков

      // при открытии схемы открываем всё остальное ( кроме карты )
      new ViewShowallCommand(dispatcher, mdiMain.desktopPane, aContext, mdiMain).execute();

  }
}
//=================================================================================================