package com.syrus.AMFICOM.Client.General.Command.Optimize;

import com.syrus.AMFICOM.Client.Map.UI.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import javax.swing.*;
import com.syrus.AMFICOM.Client.General.Checker;


public class SchemeSaveCommand extends VoidCommand
{
	OptimizeMDIMain    mdiMain;
	ApplicationContext aContext;
	Dispatcher dispatcher;
	//--------------------------------------------------------------------------
	public SchemeSaveCommand(Dispatcher dispatcher, ApplicationContext aContext, OptimizeMDIMain mdiMain)
	{	this.mdiMain = mdiMain;
		this.aContext = aContext;
		this.dispatcher = dispatcher;
	}
	//--------------------------------------------------------------------------
	public Object clone()
	{	return new SchemeSaveCommand(dispatcher, aContext, mdiMain);
	}
	//--------------------------------------------------------------------------
	public void execute()
	{	// �������� ���� �������
		Checker checker = new Checker( aContext.getDataSourceInterface() );
		if( !checker.checkCommand(checker.saveResultOfOptimization) )
  return;

    if(mdiMain.optimizerContext.original_paths.size() != 0)// ���� ���� ����
    {  int ret = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
          "������������ ���� ����������� ����� �������� ������� ��������. \n �� ������� ?",
          "������ ��", JOptionPane.YES_NO_OPTION);
       if(ret == JOptionPane.NO_OPTION)
 return;
    }
	  System.out.println("SchemeSaveCommand.execute() - starting ...");

		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if (dataSource == null)
		{System.err.println("SchemeSaveCommand.execute(): aContext.getDataSourceInterface() = null. Unable to save scheme.");
	return;
		}
    System.out.println("SchemeSaveCommand.execute() - starting ...");
		// ������� �������� ����� ����������� ( �������� � � ������������ �� ������ )
		mdiMain.scheme.serializable_cell = mdiMain.schemeFrame.graph.getArchiveableState();
		mdiMain.scheme.pack();
		// ����� � ���, ������ ��� ��� ������ �� ������ �� ID ������������ � �����������
		Pool.put( Scheme.typ, mdiMain.scheme.getId(), mdiMain.scheme );
		dataSource.SaveScheme(mdiMain.scheme.getId());

    // ��������� �������������� ���������� � �������
    System.out.println("saving additional info about scheme-decision: starting...");
    InfoToStore additional_info = new InfoToStore( mdiMain );
    additional_info.setTransferableFromLocal();
    Pool.put(InfoToStore.typ, additional_info.id, additional_info.transferable);
    dataSource.SaveSchemeOptimizeInfo(additional_info.id);
    System.out.println("saving additional info about scheme-decision: done");

		dispatcher.notify(new OperationEvent(this, 0, "scheme_saved_event"));
		System.out.println("SchemeSaveCommand.execute() - done");
	}
	//--------------------------------------------------------------------------

}