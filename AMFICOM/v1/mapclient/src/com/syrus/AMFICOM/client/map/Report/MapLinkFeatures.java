package com.syrus.AMFICOM.Client.Map.Report;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.Resource.Network.CableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Network.Characteristic;

import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapEquipmentNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;

import com.syrus.AMFICOM.Client.General.Model.Environment;

import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Report.ReportData;
import com.syrus.AMFICOM.Client.General.Report.DividableTableColumnModel;
import com.syrus.AMFICOM.Client.General.Report.DividableTableModel;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;

/**
 * <p>Title: </p>
 * <p>Description: Реализует отчёт по линку с карты</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */


public class MapLinkFeatures extends ReportData
{
	public MapLinkFeatures(ObjectsReport report,int divisionsNumber)
			throws CreateReportException
	{
		tableModel = new MapLinkFeaturesTableModel(divisionsNumber,report);
		columnModel = new MapLinkFeaturesTableColumnModel(divisionsNumber);
	}
}

class MapLinkFeaturesTableColumnModel extends DividableTableColumnModel
{
	public MapLinkFeaturesTableColumnModel(int divisionsNumber)
	{
		super (divisionsNumber);

		for (int j = 0; j < this.getDivisionsNumber(); j++)
		{
			this.addColumn(new TableColumn(j * 5,140));
			this.addColumn(new TableColumn(j * 5 + 1,80));
			this.addColumn(new TableColumn(j * 5 + 2,120));
			this.addColumn(new TableColumn(j * 5 + 3,120));
			this.addColumn(new TableColumn(j * 5 + 4,100));
		}
	}
}

class MapLinkFeaturesTableModel extends DividableTableModel
{
	int length = 0;

	Vector objects = new Vector();

	Vector firstColumn = new Vector();
	Vector secondColumn = new Vector();
	Vector thirdColumn = new Vector();
	Vector fourthColumn = new Vector();
	Vector fifthColumn = new Vector();

	public MapLinkFeaturesTableModel (int divisionsNumber,ObjectsReport report)
			throws CreateReportException
	{
		super (divisionsNumber,5);

		String reserve_str = (String)report.getReserve();
		int separatPosit = reserve_str.indexOf(':');
		String type = reserve_str.substring(0,separatPosit);
		String id = reserve_str.substring(separatPosit + 1);

		String name = "";
		String visualType = "";
		Hashtable properties = null;

		if (!type.equals(MapPhysicalLinkElement.typ))
			throw new CreateReportException(report.getName(),CreateReportException.cantImplement);

		SchemeCableLink scLink = null;
		MapPhysicalLinkElement obj = (MapPhysicalLinkElement)Pool.get(type,id);
		if (obj != null)
		{
			name = obj.name;
			visualType = LangModelConfig.String("label_link");
			scLink = (SchemeCableLink) Pool.get(SchemeCableLink.typ,obj.LINK_ID);
/*			if (scLink != null)
			{
				CableLink cLink = (CableLink) Pool.get(CableLink.typ,scLink.cable_link_id);
				properties = cLink.characteristics;
			}*/
		}

/*		if (properties == null)
			throw new CreateReportException(report.getName(),CreateReportException.poolObjNotExists);*/

		if (scLink == null)
			throw new CreateReportException(report.getName(),CreateReportException.poolObjNotExists);

		length = 0;

		firstColumn.add(LangModelReport.String("label_equipClass"));
		secondColumn.add(visualType);

		firstColumn.add(LangModelReport.String("label_equipName"));
		secondColumn.add(name);

/*		firstColumn.add("");
		secondColumn.add("");

		firstColumn.add(LangModelReport.String("label_equipChars"));
		secondColumn.add("");*/

//		length += 3;
		length ++;

/*		Enumeration propEnum = properties.elements();
		while (propEnum.hasMoreElements())
		{
			Characteristic curChar = (Characteristic) propEnum.nextElement();
			firstColumn.add(curChar.name);
			secondColumn.add(curChar.value);
			length++;
		}*/

		firstColumn.add("");
		secondColumn.add("");

		firstColumn.add("Схема прокладки кабеля");
		secondColumn.add("");

		firstColumn.add("Название");
		secondColumn.add("Координаты");

		length += 3;

		for (int i = 0; i < length; i++)
		{
			thirdColumn.add("");
			fourthColumn.add("");
			fifthColumn.add("");
		}
		thirdColumn.add("Строительная длина");
		fourthColumn.add("Топологическая длина");
		fifthColumn.add("Оптическая длина");

		Vector sortedNodes = obj.sortNodes();

		for (int i = 0; i < sortedNodes.size(); i++)
		{
			MapNodeElement curNode = (MapNodeElement) sortedNodes.get(i);
			firstColumn.add(curNode.getName());
			secondColumn.add(Double.toString(curNode.getAnchor().x).substring(0,6) +
								  " : " + Double.toString(curNode.getAnchor().y).substring(0,6));

			length++;
		}

		Vector sortedNodeLinks = obj.sortNodeLinks();

		for (int i = 0; i < sortedNodeLinks.size(); i++)
		{
			MapNodeLinkElement mnlElem =
				(MapNodeLinkElement) sortedNodeLinks.get(i);
			thirdColumn.add(Double.toString(mnlElem.getSizeInDoubleLf()).substring(0,6));
			fourthColumn.add(Double.toString(mnlElem.getSizeInDoubleLt()).substring(0,6));

			double ku = scLink.physical_length / scLink.optical_length;
			fifthColumn.add(Double.toString(mnlElem.getSizeInDoubleLf() / ku).substring(0,6));
		}
		thirdColumn.add("");
		fourthColumn.add("");
		fifthColumn.add("");
	}

	public int getColumnCount()
	{
		return 5 * this.getDivisionsNumber();
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
		int index = this.getRowCount() * (int) (col / 5) + row;
		if (index > this.length)
			return "";

		switch (col % 5)
		{
			case 0:
				return firstColumn.get(index);
			case 1:
				return secondColumn.get(index);
			case 2:
				return thirdColumn.get(index);
			case 3:
				return fourthColumn.get(index);
			case 4:
				return fifthColumn.get(index);
		}

		return "";
	}
}
