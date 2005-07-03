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
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
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

public class OptimizationSourceData extends ReportData
{
	public OptimizationSourceData(ObjectsReport report,int divisionsNumber)
			throws CreateReportException
	{
		tableModel = new OptSourceDataTableModel(divisionsNumber,report);
		columnModel = new OptSourceDataTableColumnModel(divisionsNumber);
	}
}

class OptSourceDataTableColumnModel extends DividableTableColumnModel
{
	public OptSourceDataTableColumnModel(int divisionsNumber)
	{
		super (divisionsNumber);

		for (int j = 0; j < this.getDivisionsNumber(); j++)
		{
			this.addColumn(new TableColumn(j * 2,170));//Parameter
			this.addColumn(new TableColumn(j * 2 + 1,100));//Value
		}
	}
}

class OptSourceDataTableModel extends DividableTableModel
{
	int length = 0;
	SchemeOptimizeInfo_Transferable soInfo = null;

	Vector parameter_names = new Vector();
	Vector parameter_values = new Vector();

	public OptSourceDataTableModel (int divisionsNumber,ObjectsReport report)
			throws CreateReportException
	{
		super (divisionsNumber,2);

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
			if ((curSOInfo.solution_compact_id != null) &&
		curSOInfo.solution_compact_id.equals(info_id))
			{
	soInfo = curSOInfo;
	break;
			}
		}

		if (soInfo == null)
			throw new CreateReportException(report.getName(),CreateReportException.poolObjNotExists);

		parameter_names.add(LangModelReport.getString("label_date"));
		parameter_names.add(LangModelReport.getString("label_koefZapasa"));
		parameter_names.add(LangModelReport.getString("label_procentRandom"));
		parameter_names.add(LangModelReport.getString("label_stepenRandom"));
		parameter_names.add(LangModelReport.getString("label_verUdalRTU"));
		parameter_names.add(LangModelReport.getString("label_verSozdRTU"));
		parameter_names.add(LangModelReport.getString("label_verObjedVolokon"));
		parameter_names.add(LangModelReport.getString("label_verRazjedVolokon"));
		parameter_names.add(LangModelReport.getString("label_konserv"));

		parameter_values.add(soInfo.date);
		parameter_values.add(Double.toString(soInfo.len_margin));
		parameter_values.add(Double.toString(soInfo.mutation_rate));
		parameter_values.add(Double.toString(soInfo.mutation_degree));
		parameter_values.add(Double.toString(soInfo.rtu_delete_prob));
		parameter_values.add(Double.toString(soInfo.rtu_create_prob));
		parameter_values.add(Double.toString(soInfo.nodes_splice_prob));
		parameter_values.add(Double.toString(soInfo.nodes_cut_prob));
		parameter_values.add(Double.toString(soInfo.survivor_rate));

		length = 9;
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
		if (row == 0)
			switch (col % 2)
			{
	case 0: return LangModelReport.getString("label_field");
	case 1: return LangModelReport.getString("label_value");
			}

		int index = (this.getRowCount() - 1) * (int) (col / 2) + row - 1;
		if (index >= this.length)
			return "";

		switch (col % 2)
		{
			case 0:
	return this.parameter_names.get(index);
			case 1:
	return this.parameter_values.get(index);
		}

		return "###";
	}
}