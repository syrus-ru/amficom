package com.syrus.AMFICOM.Client.Optimize.Report;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.CORBA.Scheme.SchemeOptimizeInfo_Transferable;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.Network.Characteristic;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.KISType;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import com.syrus.AMFICOM.Client.General.Report.ReportData;
import com.syrus.AMFICOM.Client.General.Report.DividableTableColumnModel;
import com.syrus.AMFICOM.Client.General.Report.DividableTableModel;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */


public class OptEquipmentFeatures extends ReportData
{
	public OptEquipmentFeatures(ObjectsReport report,int divisionsNumber)
			throws CreateReportException
	{
		tableModel = new OptEquipmentFeaturesTableModel(divisionsNumber,report);
		columnModel = new OptEquipmentFeaturesTableColumnModel(divisionsNumber);
	}
}

class OptEquipmentFeaturesTableColumnModel extends DividableTableColumnModel
{
	public OptEquipmentFeaturesTableColumnModel(int divisionsNumber)
	{
		super (divisionsNumber);

		for (int j = 0; j < this.getDivisionsNumber(); j++)
		{
			this.addColumn(new TableColumn(j * 2,170));//chars_name + common
			this.addColumn(new TableColumn(j * 2 + 1,100));//value
		}
	}
}

class OptEquipmentFeaturesTableModel extends DividableTableModel
{
	SchemeOptimizeInfo_Transferable soInfo = null;

	int length = 0;
	int reflectNumber = 0;
	int reflectCharsNumber = 0;
	int switchNumber = 0;
	int switchCharsNumber = 0;

	Vector objects = new Vector();

	Vector firstColumn = new Vector();
	Vector secondColumn = new Vector();

	public OptEquipmentFeaturesTableModel (int divisionsNumber,ObjectsReport report)
			throws CreateReportException
	{
		super (divisionsNumber,2);

		String schemeNInfo_id = ((String)report.getReserve());
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

		for (int i = 0; i < reflectNumber; i++)
		{
			EquipmentType curReflect = (EquipmentType)Pool.get(EquipmentType.typ,soInfo.refl_names[i]);
			if (curReflect != null)
			{
				objects.add(curReflect);
				if (i == 0)
					reflectCharsNumber = curReflect.characteristics.size();
			}
		}

		for (int i = 0; i < switchNumber; i++)
		{
			EquipmentType curSwitch = (EquipmentType)Pool.get(EquipmentType.typ,soInfo.switch_names[i]);
			if (curSwitch != null)
			{
				objects.add(curSwitch);
				if (i == 0)
					switchCharsNumber = curSwitch.characteristics.size();
			}

		}

		length = 0;

		firstColumn.add(LangModelReport.getString("label_reflectChars"));
		secondColumn.add("");
		length++;

		firstColumn.add("");
		secondColumn.add("");
		length++;

		for (int i = 0; i < reflectNumber; i++)
		{
			firstColumn.add("");
			secondColumn.add("");
			length++;

			EquipmentType curReflect = (EquipmentType)objects.get(i);
			firstColumn.add(curReflect.name);
			secondColumn.add("");
			length++;

			firstColumn.add("");
			secondColumn.add("");
			length++;

			Enumeration reflProp = curReflect.characteristics.values();
			while (reflProp.hasMoreElements())
			{
				Characteristic curChar = (Characteristic) reflProp.nextElement();
				firstColumn.add(curChar.name);
				secondColumn.add(curChar.value);
				length++;
			}
		}

		firstColumn.add("");
		secondColumn.add("");
		length++;

		firstColumn.add(LangModelReport.getString("label_switchChars"));
		secondColumn.add("");
		length++;

		firstColumn.add("");
		secondColumn.add("");
		length++;

		for (int i = 0; i < switchNumber; i++)
		{
			firstColumn.add("");
			secondColumn.add("");
			length++;

			EquipmentType curSwitch = (EquipmentType)objects.get(i + reflectNumber);
			firstColumn.add(curSwitch.name);
			secondColumn.add("");
			length++;

			firstColumn.add("");
			secondColumn.add("");
			length++;

			Enumeration switchProp = curSwitch.characteristics.values();
			while (switchProp.hasMoreElements())
			{
				Characteristic curChar = (Characteristic) switchProp.nextElement();
				firstColumn.add(curChar.name);
				secondColumn.add(curChar.value);
				length++;
			}
		}
	}

	public int getColumnCount()
	{
		return 2 * this.getDivisionsNumber();
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
		int index = this.getRowCount() * (int) (col / 2) + row;
		if (index >= this.length)
			return "";

		switch (col % 2)
		{
			case 0:
				return firstColumn.get(index);
			case 1:
				return secondColumn.get(index);
		}

		return "";
	}
}