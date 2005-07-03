// ����� �������� ��� �-��� ������ � Dll
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
	int iteration_counter;//������� ��������
	double best, average;// ������ � ������� �������� ���� ( ��� �������� )
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
  public static native void     DllSetOptimizeMode(int num); // ���������� ����� ����������� (�������������\���������)

	public static native int      DllGetIterationCounter();
	public static native double   DllGetBestPrice();
  public static native double   DllGetBestUnpingedRibsNumber();
	// </NATIVE>

	public OptimizeMDIMain mdiMain;//������� ������, �������� � ���� � ����� � �����
	public Map2GraphAdapter m2g;   //������ � ���� ���� ����� ����
	public Graph2MapAdapter g2m;
	//---------------------------------------------------------------------------
	// �������� �� ������� "������ �����������" � ������ ���
	public DllAdapter(OptimizeMDIMain mdiMain)
	{ this.mdiMain = mdiMain;
		m2g = new Map2GraphAdapter(mdiMain);// ������ ���� �� �����, ��� �������� ������� ����� ���������� ������� � ����
		g2m = new Graph2MapAdapter(mdiMain);// �������������� ������, ���������� �-� ��������� �������� ��������
	}
	//---------------------------------------------------------------------------
	// �������� native-������� RunPressed()
	public void Run()
	{  DllRunPressed();
	}
	//---------------------------------------------------------------------------
	// �������� native-������� SuspendPressed()
	public void Suspend()
	{ DllSuspendPressed();
	}
	//---------------------------------------------------------------------------
	// �������� � Dll �������� �����������
	public void setOptimizationParam(int i, double value)
	{ DllSetOptimizationParam(i, value);
	}
	//---------------------------------------------------------------------------

	// �������� � DLL ������ double �� ������� ����� ���������������� ��������� ����
	static public void SetNetTopology(double[] arr)
	{ DllSetNetTopology(arr);
	}
	//---------------------------------------------------------------------------
	// �������� � DLL ������ ������, � ������� ����������� ������ ���������� RTU  � ��, � ������� RTU
        // ������� ������ ( ����� ������ ������� ��� �������, �� �������� � ���������� ��� ������� )
	// ������ {n1, att1, n2, att2 ...} n* - ����� �������, att* - �������� ������� ( �����������, ������������ � � � )
	static public void SetFixedRTU(double[] arr)
	{ DllSetFixedRTU(arr);
	}
	//---------------------------------------------------------------------------
	// �������� � DLL ��������� ��������������
	static void SetReflPricelist(double[] arr)
	{ DllSetReflPricelist(arr);
	}
	//---------------------------------------------------------------------------
	// �������� � DLL ��������� ������������
	static void SetCrossPricelist(double[] arr)
	{ DllSetCrossPricelist(arr);
	}
	//---------------------------------------------------------------------------
	// ���������� ��������� �������� ��������
	static int GetIterationCounter()
	{ return DllGetIterationCounter();
	}
	//---------------------------------------------------------------------------
	// ���������� ���� ������� �� ������� �������� �������
	static double GetBestPrice()
	{ return  DllGetBestPrice();
	}
	//---------------------------------------------------------------------------
  // ���������� ���������� ���������������� ���� � ������ �������
  static double GetBestUnpingedRibsNumber()
  { return  DllGetBestUnpingedRibsNumber();
  }
  //---------------------------------------------------------------------------
	// ������� num-�� ������� ��������� �����������: 0 - ����� ������, 1 - ��������� �� ��������� ( ��� ������������� )
	static double[] getNetMonitoringTopology(int num)
	{ double a[] = DllGetMonitoringTopology(num);
		return a;
	}
}