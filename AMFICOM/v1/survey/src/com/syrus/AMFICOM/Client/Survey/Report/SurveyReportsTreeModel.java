package com.syrus.AMFICOM.Client.Survey.Report;

import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Image;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceDomainFilter;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;

import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ImageCatalogue;
import com.syrus.AMFICOM.Client.Resource.ImageResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceSorter;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;
import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
import com.syrus.AMFICOM.Client.Resource.Network.Link;
import com.syrus.AMFICOM.Client.Resource.Network.CableLink;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.LinkType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CableLinkType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;

import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.APOReportModel;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;

import com.syrus.AMFICOM.Client.General.Report.ObjectResourceReportModel;
import com.syrus.AMFICOM.Client.Survey.Report.AlarmReportModel;
import com.syrus.AMFICOM.Client.Configure.Report.EquipFeaturesReportModel;
import com.syrus.AMFICOM.Client.Schematics.Report.SchemeReportModel;

import com.syrus.AMFICOM.Client.Map.Report.MapReportModel;
import com.syrus.AMFICOM.Client.Analysis.Report.EvaluationReportModel;
import com.syrus.AMFICOM.Client.Analysis.Report.SurveyReportModel;
import com.syrus.AMFICOM.Client.Analysis.Report.AnalysisReportModel;
import com.syrus.AMFICOM.Client.Prediction.Report.PredictionReportModel;
import com.syrus.AMFICOM.Client.Model.Report.ModelingReportModel;

/**
 * <p>Description: Модель дерева с доступными отчётами</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class SurveyReportsTreeModel extends ObjectResourceTreeModel
{
	public SurveyReportsTreeModel()
	{}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode(
			"root",
			LangModelReport.String("label_availableReports"),
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

	public Vector getChildNodes(ObjectResourceTreeNode node)
	{
		Vector vec = new Vector();

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
						(String) ((ObjectResourceTreeNode) node).getObject();

					Vector views = (Vector) curModel.getAvailableViewTypesforField(
						curName);
					if (views == null)
						return new Vector();

					for (int i = 0; i < views.size(); i++)
					{
						String view = (String) views.get(i);
						ObjectsReport curReport = new ObjectsReport(curModel,
							curName, view, toAskObjects(node));

						ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
							curReport,
							LangModelReport.String(curReport.view_type),
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

			Vector fields = orm.getAllColumnIDs();
			Vector fieldNames = orm.getColumnNamesbyIDs(orm.getAllColumnIDs());

			for (int i = 0; i < fields.size(); i++)
			{
				String curField = (String) fields.get(i);
				String curName = (String) fieldNames.get(i);

				//Поля для которых нет стат.отчётов не отображаем
				Vector views = (Vector) orm.getAvailableViewTypesforField(curField);
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
				or.setReserve(new Vector());
			}
			catch (CreateReportException cre)
			{}

			ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
				or,
				LangModelReport.String(ObjectResourceReportModel.rt_objectsReport),
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

				Vector fields = orrm.getAllColumnIDs();
				Vector fieldNames = orrm.getColumnNamesbyIDs(orrm.getAllColumnIDs());

				if (fields == null)
					return new Vector();

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