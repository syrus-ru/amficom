package com.syrus.AMFICOM.Client.Survey.Report;

import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Image;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;

import com.syrus.AMFICOM.Client.General.Report.ObjectResourceReportModel;
import com.syrus.AMFICOM.Client.Survey.Report.AlarmReportModel;

/**
 * <p>Description: Модель дерева с доступными отчётами</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class SurveyReportsTreeModel extends ObjectResourceTreeModel
{
	public SurveyReportsTreeModel(){
//	  empty constuctor	
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode(
			"root",
			LangModelReport.getString("label_availableReports"),
			true);
	}

	public ImageIcon getNodeIcon(ObjectResourceTreeNode node)
	{
		return null;
	}

	public Color getNodeTextColor(ObjectResourceTreeNode node)
	{
		if (((String) node.getObject()).equals("root"))
			return Color.magenta;
		if (node.getObject()instanceof ObjectResourceReportModel)
			return Color.magenta;
		return Color.black;
	}

	public void nodeAfterSelected(ObjectResourceTreeNode node)
	{
	}

	public void nodeBeforeExpanded(ObjectResourceTreeNode node)
	{
	}

	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		return null;
	}

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList();

		//для строки - общая часть для дерева отчётов + деревья топологоии и схемы
		if (node.getObject()instanceof String)
		{
			String s = (String) node.getObject();
			//Первый уровень - названия типов объектов для отчёта

			if (s.equals("reportRoot"))
			{
				AlarmReportModel alarmModel = new AlarmReportModel();
				ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
					alarmModel,
					alarmModel.getObjectsName(),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);
			}
			else
			{
				if (((ObjectResourceTreeNode) node.getParent()).getObject()instanceof
					ObjectResourceReportModel)
				{
					ObjectResourceReportModel curModel = (ObjectResourceReportModel)
						((ObjectResourceTreeNode) node.getParent()).getObject();

					String curName =
						(String) node.getObject();

					List views = curModel.getAvailableViewTypesforField(
						curName);
					if (views == null)
						return new ArrayList();

					for (int i = 0; i < views.size(); i++)
					{
						String view = (String) views.get(i);
						ObjectsReport curReport = new ObjectsReport(curModel,
							curName, view, toAskObjects(node));

						ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
							curReport,
							LangModelReport.getString(curReport.view_type),
							true,
							new ImageIcon(Toolkit.getDefaultToolkit().getImage(
							"images/new.gif").getScaledInstance(16, 16,
							Image.SCALE_SMOOTH)));

						ortn.setDragDropEnabled(true);
						ortn.setFinal(true);

						vec.add(ortn);
					}
				}
			}
		}

		else if (node.getObject()instanceof ObjectResourceReportModel)
		//Второй уровень - для каждой модели выводим доступные поля
		{
			ObjectResourceReportModel orm =
				(ObjectResourceReportModel) node.getObject();

			List fields = orm.getAllColumnIDs();
			List fieldNames = orm.getColumnNamesbyIDs(orm.getAllColumnIDs());

			for (int i = 0; i < fields.size(); i++)
			{
				String curField = (String) fields.get(i);
				String curName = (String) fieldNames.get(i);

				//Поля для которых нет стат.отчётов не отображаем
				List views = orm.getAvailableViewTypesforField(curField);
				if (views.size() == 0)
					continue;

				ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
					curField,
					curName,
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);
			}

			ObjectsReport or = new ObjectsReport(orm,
				"",
				ObjectResourceReportModel.
				rt_objectsReport, toAskObjects(node));
			try
			{
				or.setReserve(new ArrayList());
			}
			catch (CreateReportException cre){
				cre.printStackTrace();
			}

			ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
				or,
				LangModelReport.getString(ObjectResourceReportModel.rt_objectsReport),
				true,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/new.gif").
				getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
			ortn.setDragDropEnabled(true);

			vec.add(ortn);
			registerSearchableNode("", ortn);
		}

		//Поля для "Отчёта по объектам" (у которых renderer галочки поставит)
		if (node.getObject()instanceof ObjectsReport)
		{
			ObjectsReport curReport = (ObjectsReport) node.getObject();
			if (curReport.view_type.equals(ObjectResourceReportModel.
				rt_objectsReport))
			{
				ObjectResourceReportModel orrm = (ObjectResourceReportModel)
					curReport.model;

				List fields = orrm.getAllColumnIDs();
				List fieldNames = orrm.getColumnNamesbyIDs(orrm.getAllColumnIDs());

				if (fields == null)
					return new ArrayList();

				for (int i = 0; i < fields.size(); i++)
				{
					ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
						fields.get(i),
						(String) fieldNames.get(i),
						true);
					ortn.setFinal(true);

					vec.add(ortn);
				}
			}
		}

		return vec;
	}

	public boolean toAskObjects(ObjectResourceTreeNode node)
	{
		return true;
	}
}

