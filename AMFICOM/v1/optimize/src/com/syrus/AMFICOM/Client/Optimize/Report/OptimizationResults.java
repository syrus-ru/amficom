package com.syrus.AMFICOM.Client.Optimize.Report;

import java.util.Vector;
import javax.swing.table.TableColumn;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Optimize.SolutionCompact;

import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.ReportData;
import com.syrus.AMFICOM.Client.General.Report.DividableTableColumnModel;
import com.syrus.AMFICOM.Client.General.Report.DividableTableModel;
import com.syrus.AMFICOM.CORBA.Scheme.SchemePath_Transferable;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class OptimizationResults extends ReportData
{
	public OptimizationResults(ObjectsReport report,int divisionsNumber)
			throws CreateReportException
	{
		tableModel = new OptResultsTableModel(divisionsNumber,report);
		columnModel = new OptResultsTableColumnModel(divisionsNumber);
	}
}

class OptResultsTableColumnModel extends DividableTableColumnModel
{
	public OptResultsTableColumnModel(int divisionsNumber)
	{
		super (divisionsNumber);

		for (int j = 0; j < this.getDivisionsNumber(); j++)
		{
			this.addColumn(new TableColumn(j * 2,170));//path
			this.addColumn(new TableColumn(j * 2 + 1,100));//signal_loss
		}
	}
}

class OptResultsTableModel extends DividableTableModel
{
	int length = 0;
	Vector pathChainNames = new Vector();
	Vector pathFalls = new Vector();

	public OptResultsTableModel (int divisionsNumber,ObjectsReport report)
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

		SolutionCompact sc = (SolutionCompact)Pool.get(SolutionCompact.typ,info_id);
		if (sc == null)
			throw new CreateReportException(report.getName(),CreateReportException.poolObjNotExists);

		Vector paths = sc.paths;
		if (paths == null)
			throw new CreateReportException(report.getName(),CreateReportException.cantImplement);

		for (int i = 0; i < paths.size(); i++)
		{
//			SchemePath curPath = new SchemePath((SchemePath_Transferable)paths.get(i));
			SchemePath curPath = (SchemePath)paths.get(i);
			Vector links = curPath.links;

			String pathChainName = "";
			double full_fall = 0;
			for (int j = 0; j < links.size(); j++)
			{
				PathElement pathElem = (PathElement)links.get(j);

				if (j == 0)
					pathChainName = pathElem.getName();
				else
					pathChainName += " - " + pathElem.getName();

				if (pathElem.is_cable)
				{
					SchemeCableLink sLink = (SchemeCableLink)Pool.get(SchemeCableLink.typ,pathElem.link_id);
					if (!sLink.cable_link_id.equals(""))
					{
						CableLink link = (CableLink)Pool.get(CableLink.typ,sLink.cable_link_id);//в каталоге
						Characteristic chars = (Characteristic)link.characteristics.get("Attenuation_1310");
						double fall_value = Float.parseFloat(chars.value);
						full_fall += link.physical_length * fall_value;
					}
					else
					{
						CableLinkType linkType = (CableLinkType)Pool.get(CableLinkType.typ,sLink.cable_link_type_id);
						Characteristic chars = (Characteristic)linkType.characteristics.get("Attenuation_1310");
						double fall_value = Float.parseFloat(chars.value);
						full_fall += sLink.physical_length * fall_value;
					}
				}
				else
				{
					SchemeLink sLink = (SchemeLink)Pool.get(SchemeLink.typ,pathElem.link_id);
					if (!sLink.link_id.equals(""))
					{
						Link link = (Link)Pool.get(Link.typ,sLink.link_id);//в каталоге
						Characteristic chars = (Characteristic)link.characteristics.get("Attenuation_1310");
						double fall_value = Float.parseFloat(chars.value);
						full_fall += link.physical_length * fall_value;
					}
					else
					{
						LinkType linkType = (LinkType)Pool.get(LinkType.typ,sLink.link_type_id);
						Characteristic chars = (Characteristic)linkType.characteristics.get("Attenuation_1310");
						double fall_value = Float.parseFloat(chars.value);
						full_fall += sLink.physical_length * fall_value;
					}
				}
			}
			pathChainNames.add(curPath.getName() + " (" + pathChainName + ")");

			String ret_str = new Float(full_fall / 1000).toString();
			int str_len = ret_str.length();
			if (str_len > 4)
				str_len = 4;
			pathFalls.add(ret_str.substring(0,str_len));
		}
		length = this.pathChainNames.size();
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
				case 0: return LangModelReport.getString("label_path");
				case 1: return LangModelReport.getString("label_fallValue");
			}

		int index = (this.getRowCount() - 1) * (int) (col / 2) + row - 1;
		if (index >= this.length)
			return "";

		switch (col % 2)
		{
			case 0:
				return this.pathChainNames.get(index);
			case 1:
				return this.pathFalls.get(index);
		}

		return "###";
	}
}

