package com.syrus.AMFICOM.Client.Optimize.Report;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

import javax.swing.table.TableColumn;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.CORBA.Scheme.SchemeOptimizeInfo_Transferable;

import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.KISType;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.ReportData;
import com.syrus.AMFICOM.Client.General.Report.DividableTableColumnModel;
import com.syrus.AMFICOM.Client.General.Report.DividableTableModel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */


public class EquipmentCost extends ReportData
{
	public EquipmentCost(ObjectsReport report,int divisionsNumber)
			throws CreateReportException
	{
		tableModel = new EquipmentCostTableModel(divisionsNumber,report);
		columnModel = new EquipmentCostTableColumnModel(divisionsNumber);
	}
}

class EquipmentCostTableColumnModel extends DividableTableColumnModel
{
	public EquipmentCostTableColumnModel(int divisionsNumber)
	{
		super (divisionsNumber);

		for (int j = 0; j < this.getDivisionsNumber(); j++)
		{
			this.addColumn(new TableColumn(j * 7,100));//reflectometer name
			this.addColumn(new TableColumn(j * 7 + 1,90));//its diapazon
			this.addColumn(new TableColumn(j * 7 + 2,30));//its cost
			this.addColumn(new TableColumn(j * 7 + 3,100));//switch name
			this.addColumn(new TableColumn(j * 7 + 4,90));//its port number
			this.addColumn(new TableColumn(j * 7 + 5,30));//its cost
//      this.addColumn(new TableColumn(j * 7 + 6,100));//node cost
		}
	}
}

class EquipmentCostTableModel extends DividableTableModel
{
	SchemeOptimizeInfo_Transferable soInfo = null;

	int length = 0;
	int reflectNumber = 0;
	String[] reflectNames = null;
	int switchNumber = 0;
	String[] switchNames = null;

	public EquipmentCostTableModel (int divisionsNumber,ObjectsReport report)
			throws CreateReportException
	{
		super (divisionsNumber,6);

		String schemeNInfo_id = (String)report.getReserve();
		String scheme_id = "";
		String info_id = "";
		int separatPosit = schemeNInfo_id.indexOf(':');
		if (separatPosit != -1)
		{
			scheme_id = schemeNInfo_id.substring(0,separatPosit);
			info_id = schemeNInfo_id.substring(separatPosit + 1);
		}
		else
			throw new CreateReportException(report.getName(),CreateReportException.cantImplement);

		Hashtable soHash = Pool.getHash("optimized_scheme_info");
		if (soHash == null)
			throw new CreateReportException(report.getName(),CreateReportException.poolObjNotExists);

		Enumeration soEnum = soHash.elements();

		while (soEnum.hasMoreElements())
		{
			SchemeOptimizeInfo_Transferable curSOInfo =
				(SchemeOptimizeInfo_Transferable) soEnum.nextElement();
			if (curSOInfo.solution_compact_id.equals(info_id))
			{
				soInfo = curSOInfo;
				break;
			}
		}

		if (soInfo == null)
			throw new CreateReportException(report.getName(),CreateReportException.poolObjNotExists);

		reflectNumber = soInfo.refl_prices.length;
		switchNumber = soInfo.switch_prices.length;

		//Удаляем свитч по умолчанию
		String[] newSwitchNames = new String[switchNumber];
		double[] newSwitchPortNumbers = new double[switchNumber];
		double[] newSwitchPrices = new double[switchNumber];
		int nspIterator = 0;
		for (int i = 0; i < switchNumber; i++)
			if (!(soInfo.switch_prices[i] == 0))
			{
				newSwitchNames[nspIterator] = soInfo.switch_names[i];
				newSwitchPortNumbers[nspIterator] = soInfo.switch_nports[i];
				newSwitchPrices[nspIterator] = soInfo.switch_prices[i];
				nspIterator++;
			}
		switchNumber--;
		soInfo.switch_names = newSwitchNames;
//		soInfo.
		soInfo.switch_prices = newSwitchPrices;
		//имя его - тоже

		reflectNames = new String[reflectNumber];
		for (int i = 0; i < reflectNumber; i++)
		{
			EquipmentType curReflect = (EquipmentType)Pool.get(EquipmentType.typ,soInfo.refl_names[i]);
			if (curReflect != null)
				reflectNames[i] = curReflect.name;
			else
				reflectNames[i] = "";
		}

		switchNames = new String[switchNumber];
		for (int i = 0; i < switchNumber; i++)
		{
			EquipmentType curSwitch = (EquipmentType)Pool.get(EquipmentType.typ,soInfo.switch_names[i]);
			if (curSwitch != null)
				switchNames[i] = curSwitch.name;
			else
				switchNames[i] = "";
		}

		length = (reflectNumber > switchNumber) ? reflectNumber : switchNumber;
	}

	public int getColumnCount()
	{
		return 6 * this.getDivisionsNumber();
	}
	public int getRowCount()
	{
		// Если данные можно разложить поровну на такое количество столбцов
		if (length % this.getDivisionsNumber() == 0)
			return  (int)(length / this.getDivisionsNumber()) + 1; //+заголовок
		//а если нельзя, то добавляем ещё ряд
		return (int)(length / this.getDivisionsNumber()) + 2;
	}

	public Object getValueAt(int row, int col)
	{
		if (row == 0)
			switch (col % 6)
			{
				case 0: return LangModelReport.getString("label_reflectName");
				case 1: return LangModelReport.getString("label_dinamicDiapazon");
				case 2: return LangModelReport.getString("label_cost");
				case 3: return LangModelReport.getString("label_switchName");
				case 4: return LangModelReport.getString("label_portsNumber");
				case 5: return LangModelReport.getString("label_cost");
//        case 6: return LangModelReport.getString("label_totalNodeCost");
			}

		int index = (this.getRowCount() - 1) * (int) (col / 6) + row - 1;
		if (index >= this.length)
			return "";

		if (soInfo != null)
			switch (col % 6)
			{
				case 0:
					if (index < reflectNumber)
						return reflectNames[index];
					break;
				case 1:
					if (index < reflectNumber)
						return Double.toString(this.soInfo.refl_ranges[index]);
					break;
				case 2:
					if (index < reflectNumber)
						return Double.toString(this.soInfo.refl_prices[index]);
					break;
				case 3:
					if (index < switchNumber)
						return switchNames[index];
					break;
				case 4:
					if (index < switchNumber)
						return Integer.toString((int)this.soInfo.switch_nports[index]);
					break;
				case 5:
					if (index < switchNumber)
						return Double.toString(this.soInfo.switch_prices[index]);
					break;
/*				case 6:
					return "";*/
			}

		return "";
	}
}