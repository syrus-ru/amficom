// класс содержит все ф-ции работы с Dll
package com.syrus.AMFICOM.Client.Optimize;

import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;

import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

public class DllAdapter
{
	int iteration_counter;//счётчик итераций
	double best, average;// лучшее и среднее значения цены ( или качества )
	static { System.loadLibrary("DllNetoptimize");}
	// <NATIVE>
	public static native void     DllRunPressed();
	public static native void     DllSuspendPressed();

	public static native void     DllSetOptimizationParam(int i, double value);
	public static native void     DllSetFixedRTU(double[] arr);
	public static native void     DllSetCrossPricelist(double[] arr);
	public static native void     DllSetReflPricelist(double[] arr);
	public static native void     DllSetNetTopology(double[] arr);
	public static native double[] DllGetMonitoringTopology(int num);
  public static native void     DllSetOptimizeMode(int num); // установить режим оптимизации (односторонний\встречный)

	public static native int      DllGetIterationCounter();
	public static native double   DllGetBestPrice();
  public static native double   DllGetBestUnpingedRibsNumber();
	// </NATIVE>

	public OptimizeMDIMain mdiMain;//глвыный объект, содержит в себе и схему и карту
	public Map2GraphAdapter m2g;   //хранит в себе граф схемы сети
	public Graph2MapAdapter g2m;
	//---------------------------------------------------------------------------
	// создаётся по команде "Начать оптимизацию" в первый раз
	public DllAdapter(OptimizeMDIMain mdiMain)
	{ this.mdiMain = mdiMain;
		m2g = new Map2GraphAdapter(mdiMain);// создаём граф по карте, при создании объекта сразу производим перевод в граф
		g2m = new Graph2MapAdapter(mdiMain);// подготавливаем объект, содержащий ф-ю обратного перевода форматов
	}
	//---------------------------------------------------------------------------
	// вызывает native-функцию RunPressed()
	public void Run()
	{  DllRunPressed();
	}
	//---------------------------------------------------------------------------
	// вызывает native-функцию SuspendPressed()
	public void Suspend()
	{ DllSuspendPressed();
	}
	//---------------------------------------------------------------------------
	// передать в Dll параметр оптимизации
	public void setOptimizationParam(int i, double value)
	{ DllSetOptimizationParam(i, value);
	}
	//---------------------------------------------------------------------------

	// передать в DLL массив double по которым будет инициализирована топология сети
	static public void SetNetTopology(double[] arr)
	{ DllSetNetTopology(arr);
	}
	//---------------------------------------------------------------------------
	// передать в DLL номера вершин, в которых обязательно должны находиться RTU  и те, в которые RTU
        // ставить нельзя ( после номера вершины идёт атрибут, по которому и уточняется тип вершины )
	// формат {n1, att1, n2, att2 ...} n* - номер вершины, att* - аттрибут вершины ( запрещённый, обязательный и т д )
	static public void SetFixedRTU(double[] arr)
	{ DllSetFixedRTU(arr);
	}
	//---------------------------------------------------------------------------
	// передать в DLL прайслист рефлектометров
	static void SetReflPricelist(double[] arr)
	{ DllSetReflPricelist(arr);
	}
	//---------------------------------------------------------------------------
	// передать в DLL прайслист коммутаторов
	static void SetCrossPricelist(double[] arr)
	{ DllSetCrossPricelist(arr);
	}
	//---------------------------------------------------------------------------
	// возвращает показания счётчика итераций
	static int GetIterationCounter()
	{ return DllGetIterationCounter();
	}
	//---------------------------------------------------------------------------
	// возвращает цену лучшего на текущей итерации решения
	static double GetBestPrice()
	{ return  DllGetBestPrice();
	}
	//---------------------------------------------------------------------------
  // возвращает количество непропингованных рёбер в лучшем решении
  static double GetBestUnpingedRibsNumber()
  { return  DllGetBestUnpingedRibsNumber();
  }
  //---------------------------------------------------------------------------
	// забрать num-ое решение топологии мониторинга: 0 - самое лучшее, 1 - следующее по качекстув ( они отсортированы )
	static double[] getNetMonitoringTopology(int num)
	{ double a[] = DllGetMonitoringTopology(num);
		return a;
	}
}