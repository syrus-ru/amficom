package com.syrus.AMFICOM.Client.Configure.Report;

import com.syrus.AMFICOM.CORBA.Scheme.SchemeOptimizeInfo_Transferable;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.DividableTableColumnModel;
import com.syrus.AMFICOM.Client.General.Report.DividableTableModel;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.ReportData;
import com.syrus.AMFICOM.Client.Resource.ISM.KIS;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.KISType;
import com.syrus.AMFICOM.Client.Resource.Network.CableLink;
import com.syrus.AMFICOM.Client.Resource.General.Characteristic;
import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
import com.syrus.AMFICOM.Client.Resource.Network.Link;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CableLinkType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.LinkType;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */


public class EquipFeatures extends ReportData
{
	public EquipFeatures(ObjectsReport report,int divisionsNumber)
			throws CreateReportException
	{
		tableModel = new EquipmentFeaturesTableModel(divisionsNumber,report);
		columnModel = new EquipmentFeaturesTableColumnModel(divisionsNumber);
	}
}

class EquipmentFeaturesTableColumnModel extends DividableTableColumnModel
{
	public EquipmentFeaturesTableColumnModel(int divisionsNumber)
	{
		super (divisionsNumber);

		for (int j = 0; j < this.getDivisionsNumber(); j++)
		{
			this.addColumn(new TableColumn(j * 2,170));//chars_name + common
			this.addColumn(new TableColumn(j * 2 + 1,100));//value
		}
	}
}

class EquipmentFeaturesTableModel extends DividableTableModel
{
	SchemeOptimizeInfo_Transferable soInfo = null;

	int length = 0;

	List objects = new ArrayList();

	List firstColumn = new ArrayList();
	List secondColumn = new ArrayList();

	public EquipmentFeaturesTableModel (int divisionsNumber,ObjectsReport report)
			throws CreateReportException
	{
		super (divisionsNumber,2);

		String reserve_str = (String)report.getReserve();
		int separatPosit = reserve_str.indexOf(':');
		String type = reserve_str.substring(0,separatPosit);
		String id = reserve_str.substring(separatPosit + 1);

		String name = "";
		String visualType = "";
		String mark = "";
		Map properties = null;

		if (type.equals(KIS.typ) || type.equals(Equipment.typ))
		{
			Equipment obj = (Equipment)Pool.get("kisequipment",id);
			if (obj != null)
			{
				name = obj.name;
				if (obj.eqClass.equals(""))
					visualType = "";
				else
					visualType = LangModelConfig.getString(obj.eqClass);

				mark = ((EquipmentType)Pool.get(EquipmentType.typ,obj.typeId)).name;
				properties = obj.characteristics;
			}
		}
		else if (type.equals(Link.typ))
		{
			Link obj = (Link)Pool.get(type,id);
			if (obj != null)
			{
				name = obj.name;
				visualType = LangModelConfig.getString("label_link");
				mark = ((LinkType)Pool.get(LinkType.typ,obj.typeId)).name;
				properties = obj.characteristics;
			}
		}
		else if (type.equals(CableLink.typ))
		{
			CableLink obj = (CableLink)Pool.get(type,id);
			if (obj != null)
			{
				name = obj.name;
				visualType = LangModelConfig.getString("label_cablelink");
				mark = ((CableLinkType)Pool.get(CableLinkType.typ,obj.typeId)).name;
				properties = obj.characteristics;
			}
		}
		else if (type.equals(LinkType.typ))
		{
			LinkType obj = (LinkType)Pool.get(type,id);
			if (obj != null)
			{
				name = obj.name;
				visualType = LangModelConfig.getString("label_link");
				properties = obj.characteristics;
			}
		}
		else if (type.equals(CableLinkType.typ))
		{
			CableLinkType obj = (CableLinkType)Pool.get(type,id);
			if (obj != null)
			{
				name = obj.name;
				visualType = LangModelConfig.getString("label_cablelink");
				properties = obj.characteristics;
			}
		}
		else if (type.equals(EquipmentType.typ))
		{
			EquipmentType obj = (EquipmentType)Pool.get(type,id);
			if (obj != null)
			{
				name = obj.name;
				visualType = LangModelConfig.getString(obj.eqClass);
				properties = obj.characteristics;
			}
		}
		else if (type.equals(KISType.typ))
		{
			KISType obj = (KISType)Pool.get(type,id);
			if (obj != null)
			{
				name = obj.name;
				visualType = LangModelConfig.getString(obj.eqClass);
				properties = obj.characteristics;
			}
		}

		if (properties == null)
			throw new CreateReportException(report.getName(),CreateReportException.poolObjNotExists);

		length = 0;

		if (!mark.equals(""))
		{
			firstColumn.add(LangModelReport.getString("label_equipName"));
			secondColumn.add(name);

			firstColumn.add(LangModelReport.getString("label_equipClass"));
			secondColumn.add(visualType);

			firstColumn.add(LangModelReport.getString("label_equipMark"));
			secondColumn.add(mark);

			firstColumn.add("");
			secondColumn.add("");

			firstColumn.add(LangModelReport.getString("label_equipChars"));
			secondColumn.add("");

			length += 4;
		}
		else
		{
			firstColumn.add(LangModelReport.getString("label_equipClass"));
			secondColumn.add(visualType);

			firstColumn.add(LangModelReport.getString("label_equipMark"));
			secondColumn.add(name);

			firstColumn.add("");
			secondColumn.add("");

			firstColumn.add(LangModelReport.getString("label_equipChars"));
			secondColumn.add("");

			length += 3;
		}


		for (Iterator propEnum = properties.values().iterator(); propEnum.hasNext();)
		{
			Characteristic curChar = (Characteristic) propEnum.next();
			firstColumn.add(curChar.name);
			secondColumn.add(curChar.value);
			length++;
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
