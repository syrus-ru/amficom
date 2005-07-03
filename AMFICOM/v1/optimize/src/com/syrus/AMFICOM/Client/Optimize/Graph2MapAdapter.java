package com.syrus.AMFICOM.Client.Optimize;

import com.syrus.AMFICOM.Client.General.Event.*;

//---------------------------------------------------------------------------------------------------------------
// переводит граф из формата, с которым работает Dll в формат, с которым работает карта
public class Graph2MapAdapter
{
	//private ISMMapContext ismMapContext;
	private OptimizeMDIMain mdiMain;
	private Dispatcher dispatcher;
	//-----------------------------------------------------------------------------------------------------------
	public Graph2MapAdapter(OptimizeMDIMain mdiMain)
	{  this.mdiMain = mdiMain;
		// ismMapContext = mdiMain.ismMapContext;// ismMapContext создаётся тогда же, когда и mdiMain
		 this.dispatcher = mdiMain.getInternalDispatcher();
	}
	//-----------------------------------------------------------------------------------------------------------
	public void CreateMapByGraph(Solution solution)
	{	// !!! удалить всё тело ф-ции ?..
		//карта обновлена, теперь надо уведомить всех, кому эта инфа нужна 
		 dispatcher.notify(new OperationEvent (this, 0, "MapUpdatedByNewSolutionEvent"));

	}
	//-----------------------------------------------------------------------------------------------------------
}
