package com.syrus.AMFICOM.Client.Optimize;

import com.syrus.AMFICOM.Client.General.Event.*;

//---------------------------------------------------------------------------------------------------------------
// ��������� ���� �� �������, � ������� �������� Dll � ������, � ������� �������� �����
public class Graph2MapAdapter
{
	//private ISMMapContext ismMapContext;
	private OptimizeMDIMain mdiMain;
	private Dispatcher dispatcher;
	//-----------------------------------------------------------------------------------------------------------
	public Graph2MapAdapter(OptimizeMDIMain mdiMain)
	{  this.mdiMain = mdiMain;
		// ismMapContext = mdiMain.ismMapContext;// ismMapContext �������� ����� ��, ����� � mdiMain
		 this.dispatcher = mdiMain.getInternalDispatcher();
	}
	//-----------------------------------------------------------------------------------------------------------
	public void CreateMapByGraph(Solution solution)
	{	// !!! ������� �� ���� �-��� ?..
		//����� ���������, ������ ���� ��������� ����, ���� ��� ���� ����� 
		 dispatcher.notify(new OperationEvent (this, 0, "MapUpdatedByNewSolutionEvent"));

	}
	//-----------------------------------------------------------------------------------------------------------
}
