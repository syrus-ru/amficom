package com.syrus.AMFICOM.Client.ReportBuilder;

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
import com.syrus.AMFICOM.Client.General.UI.UniTreePanel;

import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceDomainFilter;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;

import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ImageCatalogue;
import com.syrus.AMFICOM.Client.Resource.ImageResource;
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

import com.syrus.AMFICOM.Client.Optimize.Report.OptimizationReportModel;
import com.syrus.AMFICOM.Client.Optimize.Report.SelectSolutionFrame;

import com.syrus.AMFICOM.Client.General.Report.ObjectResourceReportModel;
import com.syrus.AMFICOM.Client.Survey.Report.AlarmReportModel;
import com.syrus.AMFICOM.Client.Survey.Report.ObserveReportModel;
import com.syrus.AMFICOM.Client.Configure.Report.EquipFeaturesReportModel;
import com.syrus.AMFICOM.Client.Schematics.Report.SchemeReportModel;
import com.syrus.AMFICOM.Client.Analysis.Report.EvaluationReportModel;
import com.syrus.AMFICOM.Client.Analysis.Report.SurveyReportModel;
import com.syrus.AMFICOM.Client.Analysis.Report.AnalysisReportModel;
import com.syrus.AMFICOM.Client.Prediction.Report.PredictionReportModel;
import com.syrus.AMFICOM.Client.Model.Report.ModelingReportModel;
import com.syrus.AMFICOM.Client.Map.Report.MapReportModel;

import javax.swing.tree.TreePath;

/**
 * <p>Description: Модель дерева с доступными отчётами</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class AvailableReportsTreeModel extends ObjectResourceTreeModel
	implements OperationListener
{
	private ApplicationContext aContext = null;

	public AvailableReportsTreeModel(ApplicationContext aContext)
	{
		this.aContext = aContext;
		this.aContext.getDispatcher().register(this,TreeDataSelectionEvent.type);
	}

	public void operationPerformed (OperationEvent oe)
	{
		if (oe.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			UniTreePanel reportsTreePanel = (UniTreePanel) oe.getSource();
			TreePath treePath = reportsTreePanel.getTree().getSelectionPath();
			if (treePath == null)
				return;

			ObjectResourceTreeNode lastElem =
				(ObjectResourceTreeNode) treePath.getLastPathComponent();

			if (lastElem.getObject() instanceof OptimizationReportModel)
			{
				SelectSolutionFrame ssFrame = new SelectSolutionFrame (this.aContext);
			}
		}
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

	public Vector getChildNodes(ObjectResourceTreeNode node)
	{
		Vector vec = new Vector();

		//для строки - общая часть для дерева отчётов + деревья топологоии и схемы
		if (node.getObject()instanceof String)
		{
			String s = (String) node.getObject();
			//Нулевой уровень - разделение на отчёты и элементы шаблона

			if (s.equals("root"))
			{
				ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
					"reportRoot",
					LangModelReport.getString("label_availableReports"), //Отчёты без привязки к конкретному оборудованию
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				ortn = new ObjectResourceTreeNode(
					"templateElementRoot",
					LangModelReport.getString("label_availableTemplElems"), //Отчёты с привязкой к конкретному оборудованию,
					true); //тесту, карте итд

				vec.add(ortn);
				registerSearchableNode("", ortn);
			}

			//Первый уровень - названия типов объектов для отчёта

			else if (s.equals("reportRoot"))
			{
				AlarmReportModel alarmModel = new AlarmReportModel();
				ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
					alarmModel,
					alarmModel.getObjectsName(),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				/*        ortn = new ObjectResourceTreeNode(
				 "",
				 LangModelReport.getString("label_repSboiSistemy"),
				 true);
				 vec.add(ortn);
				 registerSearchableNode("", ortn);*/

				/*        ortn = new ObjectResourceTreeNode(
				 "",
				 LangModelReport.getString("label_repStat"),
				 true);
				 vec.add(ortn);
				 registerSearchableNode("", ortn);*/

				OptimizationReportModel optModel = new OptimizationReportModel();
				ortn = new ObjectResourceTreeNode(
					optModel,
					optModel.getObjectsName(),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				ortn = new ObjectResourceTreeNode(
					"EquipmentChars",
					LangModelConfig.getString("label_equipChars"),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				ortn = new ObjectResourceTreeNode(
					"label_repPhysicalScheme",
					LangModelReport.getString("label_repPhysicalScheme"),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

						  ortn = new ObjectResourceTreeNode(
				 "label_repTopologicalScheme",
				 LangModelReport.getString("label_repTopologicalScheme"),
				 true);
				 vec.add(ortn);
				 registerSearchableNode("", ortn);

				/*        ortn = new ObjectResourceTreeNode(
				 "",
				 LangModelReport.getString("label_repAbonentsPassports"),
				 true);
				 vec.add(ortn);
				 registerSearchableNode("", ortn);*/

				/*        ortn = new ObjectResourceTreeNode(
				 "",
				 LangModelReport.getString("label_repMetrologicalPoverka"),
				 true);
				 vec.add(ortn);
				 registerSearchableNode("", ortn);*/
			}

			else if (s.equals("templateElementRoot"))
			{
				SchemeReportModel schemeModel = new SchemeReportModel();
				ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
					schemeModel,
					schemeModel.getObjectsName(),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				ortn = new ObjectResourceTreeNode(
					"EquipmentChars",
					LangModelConfig.getString("label_equipChars"),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				MapReportModel mapModel = new MapReportModel();
				ortn = new ObjectResourceTreeNode(
					mapModel,
					mapModel.getObjectsName(),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				/*        ortn = new ObjectResourceTreeNode(
				 "",
				 LangModelReport.getString("label_repSboiSistemy"),
				 true);
				 vec.add(ortn);
				 registerSearchableNode("", ortn);*/

				/*        ortn = new ObjectResourceTreeNode(
				 "",
				 LangModelReport.getString("label_repStat"),
				 true);
				 vec.add(ortn);
				 registerSearchableNode("", ortn);*/

				AnalysisReportModel arModel = new AnalysisReportModel();
				ortn = new ObjectResourceTreeNode(
					arModel,
					arModel.getObjectsName(),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				SurveyReportModel srModel = new SurveyReportModel();
				ortn = new ObjectResourceTreeNode(
					srModel,
					srModel.getObjectsName(),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				EvaluationReportModel erModel = new EvaluationReportModel();
				ortn = new ObjectResourceTreeNode(
					erModel,
					erModel.getObjectsName(),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				PredictionReportModel predModel = new PredictionReportModel();
				ortn = new ObjectResourceTreeNode(
					predModel,
					predModel.getObjectsName(),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				ModelingReportModel modelModel = new ModelingReportModel();
				ortn = new ObjectResourceTreeNode(
					modelModel,
					modelModel.getObjectsName(),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				ObserveReportModel observeModel = new ObserveReportModel();
				ortn = new ObjectResourceTreeNode(
					observeModel,
					observeModel.getObjectsName(),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				/*        ortn = new ObjectResourceTreeNode(
				 new OptimizationReportModel(),
				 LangModelReport.getString("label_repOptimizationResults"),
				 true);
				 vec.add(ortn);
				 registerSearchableNode("", ortn);*/

				/*        ortn = new ObjectResourceTreeNode(
				 "",
				 LangModelReport.getString("label_repAbonentsPassports"),
				 true);
				 vec.add(ortn);
				 registerSearchableNode("", ortn);*/

				/*        ortn = new ObjectResourceTreeNode(
				 "",
				 LangModelReport.getString("label_repMetrologicalPoverka"),
				 true);
				 vec.add(ortn);
				 registerSearchableNode("", ortn);*/
			}

			else if (s.equals("EquipmentChars"))
			{
				if (toAskObjects(node))
				{
					ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
						"mufta",
						LangModelConfig.getString("mufta"),
						true);

					vec.add(ortn);
					registerSearchableNode("", ortn);

					ortn = new ObjectResourceTreeNode(
						"switch",
						LangModelConfig.getString("switch"),
						true);

					vec.add(ortn);
					registerSearchableNode("", ortn);

					ortn = new ObjectResourceTreeNode(
						"cross",
						LangModelConfig.getString("cross"),
						true);

					vec.add(ortn);
					registerSearchableNode("", ortn);

					ortn = new ObjectResourceTreeNode(
						"multiplexor",
						LangModelConfig.getString("multiplexor"),
						true);

					vec.add(ortn);
					registerSearchableNode("", ortn);

					ortn = new ObjectResourceTreeNode(
						"filter",
						LangModelConfig.getString("filter"),
						true);

					vec.add(ortn);
					registerSearchableNode("", ortn);

					ortn = new ObjectResourceTreeNode(
						"transmitter",
						LangModelConfig.getString("transmitter"),
						true);

					vec.add(ortn);
					registerSearchableNode("", ortn);

					ortn = new ObjectResourceTreeNode(
						"reciever",
						LangModelConfig.getString("reciever"), //поменять на receiver
						true);

					vec.add(ortn);
					registerSearchableNode("", ortn);

					ortn = new ObjectResourceTreeNode(
						"tester",
						LangModelConfig.getString("tester"),
						true);

					vec.add(ortn);
					registerSearchableNode("", ortn);
				}
				else
				{
					ObjectsReport rep = new ObjectsReport(new
						EquipFeaturesReportModel(),
						"",
						ObjectResourceReportModel.
						rt_objProperies,
						toAskObjects(node));

					ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
						rep,
						rep.getName(),
						true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage(
						"images/new.gif")),
						true);
					ortn.setDragDropEnabled(true);
					vec.add(ortn);
				}
			}

			else if (s.equals("mufta") ||
				s.equals("switch") ||
				s.equals("cross") ||
				s.equals("multiplexor") ||
				s.equals("filter") ||
				s.equals("transmitter") ||
				s.equals("reciever") ||
				s.equals("tester"))
			{
				Hashtable equipTypeHash = Pool.getHash(EquipmentType.typ);
				if (equipTypeHash == null)
					return new Vector();

				Enumeration equipTypeEnum = equipTypeHash.elements();
				while (equipTypeEnum.hasMoreElements())
				{
					EquipmentType eqType = (EquipmentType) equipTypeEnum.nextElement();
					if (eqType.eq_class.equals(s))
					{
						ObjectsReport rep = new ObjectsReport(new
							EquipFeaturesReportModel(),
							"",
							ObjectResourceReportModel.
							rt_objProperies,
							toAskObjects(node));
						try
						{
							rep.setReserve(EquipmentType.typ + ":" + eqType.id);
						}
						catch (CreateReportException cre)
						{}

						ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
							rep,
							eqType.getName(),
							true,
							new ImageIcon(Toolkit.getDefaultToolkit().getImage(
							"images/new.gif")),
							true);
						ortn.setDragDropEnabled(true);
						vec.add(ortn);
					}
				}

				/*        ObjectsReport or = new ObjectsReport (orm.getTyp(),"",ObjectResourceReportModel.rt_objectsReport);
				 try
				 {
				 or.setReserve(new Vector());
				 }
				 catch (CreateReportException cre){}
				 ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
				 or,
					 LangModelReport.getString(ObjectResourceReportModel.rt_objectsReport),
				 true,
				 new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/new.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
				 ortn.setDragDropEnabled(true);
				 vec.add(ortn);
				 registerSearchableNode("", ortn);*/

			}

			else if (   s.equals("label_repPhysicalScheme")
						&& this.toAskObjects(node))
			{
				Hashtable ht = new Hashtable();
				if (Pool.getHash(Scheme.typ) != null)
				{
					for (Enumeration en = Pool.getHash(Scheme.typ).elements();
						en.hasMoreElements(); )
					{
						Scheme sch = (Scheme) en.nextElement();
						ht.put(sch.scheme_type, sch.scheme_type);
					}

					for (Enumeration en = ht.elements(); en.hasMoreElements(); )
					{
						String type = (String) en.nextElement();
						String name = LangModelSchematics.getString(type);
						if (type.equals(""))
							name = "Схема";

						vec.add(new ObjectResourceTreeNode(type,
							name, true,
							new ImageIcon(Toolkit.
							getDefaultToolkit().getImage("images/folder.gif"))));
					}
				}
			}
			else if (s.equals("label_repTopologicalScheme"))
			{
				if (Pool.getHash(MapContext.typ) != null)
				{
					ObjectResourceFilter filter = new ObjectResourceDomainFilter(
						aContext.getDataSourceInterface().getSession().getDomainId());
            
          Hashtable mcHash = Pool.getHash(MapContext.typ);
					filter.filtrate(mcHash);
          
/*					ObjectResourceSorter sorter = MapContext.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();*/

          Enumeration mcEnum = mcHash.elements();
					for (; mcEnum.hasMoreElements(); )
					{
						MapContext mc = (MapContext) mcEnum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(mc,
							mc.getName(), true);
						vec.add(n);
					}
				}
			}
			else if (s.equals(MapEquipmentNodeElement.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode) node.
					getParent();
				MapContext mc = (MapContext) parent.getObject();
////////////////////////////////////
				DataSet dSet = new DataSet();
				for (int i = 0; i < mc.getNodes().size(); i++)
				{
					ObjectResource os = (ObjectResource) mc.getNodes().get(i);
					if (os instanceof MapEquipmentNodeElement)
						dSet.add(os);
				}

				ObjectResourceSorter sorter = MapEquipmentNodeElement.
					getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enumerer = dSet.elements();
/////////////////////////////////////////        
				for (; enumerer.hasMoreElements(); )
				{
					MapEquipmentNodeElement me = (MapEquipmentNodeElement) enumerer.
						nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(me,
						me.getName(), true);
					vec.add(n);
				}
			}
/*			else if (s.equals(MapKISNodeElement.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode) node.
					getParent();
				MapContext mc = (MapContext) parent.getObject();

				DataSet dSet = new DataSet();
				for (Enumeration enumer = mc.getNodes().elements();
					enumer.hasMoreElements(); )
				{
					ObjectResource os = (ObjectResource) enumer.nextElement();
					if (os instanceof MapKISNodeElement)
						dSet.add(os);
				}

				ObjectResourceSorter sorter = MapEquipmentNodeElement.
					getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enumer = dSet.elements();
				for (; enumer.hasMoreElements(); )
				{
					MapKISNodeElement me = (MapKISNodeElement) enumer.nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(me,
						me.getName(), true);
					vec.add(n);
				}
			}*/
			else if (s.equals(MapPhysicalNodeElement.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode) node.
					getParent();
				MapContext mc = (MapContext) parent.getObject();

				DataSet dSet = new DataSet();
				for (int i = 0; i < mc.getNodes().size();	i++)
				{
					ObjectResource os = (ObjectResource) mc.getNodes().get(i);
					if (os instanceof MapPhysicalNodeElement)
						dSet.add(os);
				}

				ObjectResourceSorter sorter = MapPhysicalNodeElement.
					getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enumerer = dSet.elements();
				for (; enumerer.hasMoreElements(); )
				{
					MapPhysicalNodeElement me = (MapPhysicalNodeElement) enumerer.
						nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(me,
						me.getName(), true);
					vec.add(n);
				}
			}
			else if (s.equals(MapPhysicalLinkElement.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode) node.
					getParent();
				MapContext mc = null;
				DataSet dSet = null;
				if (parent.getObject()instanceof MapContext)
				{
					mc = (MapContext) parent.getObject();
					dSet = new DataSet(mc.getPhysicalLinks());
				}
				else
				{
					MapEquipmentNodeElement mene = (MapEquipmentNodeElement) parent.
getObject();
					mc = mene.getMapContext();

					dSet = new DataSet();

					for (int i = 0; i < mc.getPhysicalLinks().size();	i++)
					{
						MapPhysicalLinkElement ml = (MapPhysicalLinkElement) mc.getPhysicalLinks().get(i);

						if (ml.startNode.equals(mene) || ml.endNode.equals(mene))
							dSet.add(ml);
					}
				}

				ObjectResourceSorter sorter = MapPhysicalLinkElement.
					getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enumerer = dSet.elements();
				for (; enumerer.hasMoreElements(); )
				{
					MapPhysicalLinkElement ml = (MapPhysicalLinkElement) enumerer.
						nextElement();

					String curName = ml.getName();
					ObjectsReport curReport = new ObjectsReport(new MapReportModel(),
						MapReportModel.rep_linkChars,"", toAskObjects(node));

					try
					{
						curReport.setReserve(MapPhysicalLinkElement.typ + ":" + ml.id);
					}
					catch (CreateReportException cre)
					{}

					ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
						curReport,
						curName,
						true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage(
						"images/new.gif").getScaledInstance(16, 16,
						Image.SCALE_SMOOTH)));

					ortn.setDragDropEnabled(true);
					ortn.setFinal(true);

/*					ObjectResourceTreeNode n = new ObjectResourceTreeNode(ml,
						ml.getName(), true);*/
					vec.add(ortn);
				}
			}
			else if (s.equals(MapTransmissionPathElement.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode) node.
					getParent();
				MapContext mc = (MapContext) parent.getObject();

				DataSet dSet = new DataSet(mc.getTransmissionPath());

				ObjectResourceSorter sorter = MapTransmissionPathElement.
					getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enumerer = dSet.elements();
				for (; enumerer.hasMoreElements(); )
				{
					MapTransmissionPathElement me = (MapTransmissionPathElement)
						enumerer.
						nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(me,
						me.getName(), true);
					vec.add(n);
				}
			}
			else if (s.equals(MapMarkElement.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode) node.
					getParent();
				MapPhysicalLinkElement ml = (MapPhysicalLinkElement) parent.
					getObject();
				MapContext mc = ml.getMapContext();

				DataSet dSet = new DataSet();
				for (int i = 0; i < mc.getMapMarkElements().size(); i++)
				{
					MapMarkElement mme = (MapMarkElement) mc.getMapMarkElements().get(i);
					if (mme.link_id.equals(ml.getId()))
						dSet.add(mme);
				}

				ObjectResourceSorter sorter = MapMarkElement.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enumerer = dSet.elements();
				for (; enumerer.hasMoreElements(); )
				{
					MapMarkElement mme = (MapMarkElement) enumerer.nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(mme,
						mme.getName(), true);
					vec.add(n);
				}
			}
			else if ((((ObjectResourceTreeNode) node.getParent()).getObject()instanceof
				String) &&
				((ObjectResourceTreeNode) node.getParent()).getObject().equals(
				"label_repPhysicalScheme"))
			{

				DataSet dSet = new DataSet(Pool.getHash(Scheme.typ));

				ObjectResourceFilter filter = new ObjectResourceDomainFilter(
					aContext.getDataSourceInterface().getSession().getDomainId());
				dSet = filter.filter(dSet);
				ObjectResourceSorter sorter = Scheme.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				for (Enumeration enumer = dSet.elements(); enumer.hasMoreElements(); )
				{
					Scheme scheme = (Scheme) enumer.nextElement();
					if (scheme.scheme_type.equals(s))
					{
						ObjectsReport or = new ObjectsReport (
							new SchemeReportModel(),
							SchemeReportModel.scheme,
							"",
							true);
						try
						{
							or.setReserve(scheme.id);
						}
						catch(Exception exc)
						{}

						ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
							or,
							scheme.getName(),
							true,
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")));

						ortn.setDragDropEnabled(true);
						vec.add(ortn);
					}
				}
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
		//для схемы
		else if ((node.getObject()instanceof Scheme)
			|| ((node.getObject() instanceof ObjectsReport)
			&& (((ObjectsReport)node.getObject()).getReserve() != null)
			&& (((ObjectsReport)node.getObject()).getReserve() instanceof String)
			&& (Pool.get(Scheme.typ,(String)((ObjectsReport)node.getObject()).getReserve()) != null)))
		{
			Scheme scheme = null;
			if (node.getObject()instanceof Scheme)
				scheme = (Scheme) node.getObject();
			else
				scheme = (Scheme)Pool.get(Scheme.typ,(String)((ObjectsReport)node.getObject()).getReserve());
			/*		 else if ((node.getObject()instanceof ObjectsReport)
			 && (((ObjectsReport)node.getObject()).getReserve() instanceof ))
			 {
			 Scheme scheme = (Scheme) node.getObject();*/

			//Элементы
			for (Enumeration enumer = scheme.elements.elements();
				enumer.hasMoreElements(); )
			{
				SchemeElement element = (SchemeElement) enumer.nextElement();
				if (element.scheme_id.equals(""))
				{
					ObjectsReport rep = new ObjectsReport(new
						EquipFeaturesReportModel(),
						"",
						ObjectResourceReportModel.rt_objProperies,
						toAskObjects(node));
					if (!element.equipment_id.equals(""))
					{
						try
						{
							rep.setReserve(Equipment.typ + ":" + element.equipment_id);
						}
						catch (CreateReportException cre)
						{}
					}
					else if (!element.proto_element_id.equals(""))
					{
						ProtoElement pe = (ProtoElement) Pool.get(ProtoElement.typ,
							element.proto_element_id);
						try
						{
							rep.setReserve(EquipmentType.typ + ":"
								+ pe.equipment_type_id);
						}
						catch (CreateReportException cre)
						{}
					}
					else
						continue;

					ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
						rep,
						element.getName(),
						true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage(
						"images/new.gif")),
						true);
					ortn.setDragDropEnabled(true);
					vec.add(ortn);
				}
				else
				{
					ObjectsReport or = new ObjectsReport (
						new SchemeReportModel(),
						SchemeReportModel.scheme,
						"",
						true);
					try
					{
						or.setReserve(element.scheme_id);
					}
					catch(Exception exc)
					{}

					ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
						or,
						element.getName(),
						true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")));

					ortn.setDragDropEnabled(true);
					vec.add(ortn);
				}
			}

			//Линки
			for (int i = 0; i < scheme.links.size(); i++)
			{
				SchemeLink link = (SchemeLink) scheme.links.get(i);
				ObjectsReport rep = new ObjectsReport(new EquipFeaturesReportModel(),
					"",
					ObjectResourceReportModel.rt_objProperies,
					toAskObjects(node));
				if (!link.link_id.equals(""))
				{
					try
					{
						rep.setReserve(Link.typ + ":" + link.link_id);
					}
					catch (CreateReportException cre)
					{}
				}
				else if (!link.link_type_id.equals(""))
				{
					try
					{
						rep.setReserve(LinkType.typ + ":" + link.link_type_id);
					}
					catch (CreateReportException cre)
					{}
				}
				else
					continue;

				ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
					rep,
					link.getName(),
					true,
					new ImageIcon(Toolkit.getDefaultToolkit().getImage(
					"images/new.gif")),
					true);
				ortn.setDragDropEnabled(true);
				vec.add(ortn);
			}

			//Кабельные линки
			for (int i = 0; i < scheme.cablelinks.size(); i++)
			{
				SchemeCableLink link = (SchemeCableLink) scheme.cablelinks.get(i);
				ObjectsReport rep = new ObjectsReport(new EquipFeaturesReportModel(),
					"",
					ObjectResourceReportModel.rt_objProperies,
					toAskObjects(node));
				if (!link.cable_link_id.equals(""))
				{
					try
					{
						rep.setReserve(CableLink.typ + ":" + link.cable_link_id);
					}
					catch (CreateReportException cre)
					{}
				}
				else if (!link.cable_link_type_id.equals(""))
				{
					try
					{
						rep.setReserve(CableLinkType.typ + ":"
							+ link.cable_link_type_id);
					}
					catch (CreateReportException cre)
					{}
				}
				else
					continue;

				ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
					rep,
					link.getName(),
					true,
					new ImageIcon(Toolkit.getDefaultToolkit().getImage(
					"images/new.gif")),
					true);
				ortn.setDragDropEnabled(true);
				vec.add(ortn);
			}

		}
		else if (node.getObject()instanceof SchemeElement)
		{
			SchemeElement schel = (SchemeElement) node.getObject();
			if (!schel.scheme_id.equals(""))
			{
				Scheme scheme = (Scheme) Pool.get(Scheme.typ, schel.scheme_id);
				for (Enumeration enumer = scheme.elements.elements();
					enumer.hasMoreElements(); )
				{
					SchemeElement element = (SchemeElement) enumer.nextElement();

					if (element.scheme_id.equals(""))
					{
						ObjectsReport rep = new ObjectsReport(new
							EquipFeaturesReportModel(),
							"",
							ObjectResourceReportModel.rt_objProperies,
							toAskObjects(node));

						if (!element.equipment_id.equals(""))
						{
							try
							{
								rep.setReserve(Equipment.typ + ":"
									+ element.equipment_id);
							}
							catch (CreateReportException cre)
							{
								System.out.println(
									"Jokarnyj nasos pri realizatsii objecta otcheta!");
							}
						}
						else if (!element.proto_element_id.equals(""))
						{
							ProtoElement pe = (ProtoElement) Pool.get(ProtoElement.typ,
								element.proto_element_id);
							try
							{
								rep.setReserve(EquipmentType.typ + ":"
									+ pe.equipment_type_id);
							}
							catch (CreateReportException cre)
							{
								System.out.println(
									"Jokarnyj nasos pri realizatsii objecta otcheta!");
							}
						}
						else
							continue;

						ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
							rep,
							element.getName(),
							true,
							new ImageIcon(Toolkit.getDefaultToolkit().getImage(
							"images/new.gif")),
							true);
						ortn.setDragDropEnabled(true);
						vec.add(ortn);

					}
					else
						vec.add(new ObjectResourceTreeNode(element, element.getName(), true,
							new ImageIcon(Toolkit.
							getDefaultToolkit().getImage("images/scheme.gif")), false));
				}
			}
		}
//конец для схемы
//Для топологии
		else if (node.getObject()instanceof MapContext)
		{
			ObjectResourceTreeNode ortn;
			ortn = new ObjectResourceTreeNode(MapEquipmentNodeElement.typ,
				LangModelConfig.getString("label_nodes"), true);
			vec.add(ortn);
			registerSearchableNode(MapEquipmentNodeElement.typ, ortn);

//				vec.add(new ObjectResourceTreeNode(MapKISNodeElement.typ, "Узлы с КИС", true));
			ortn = new ObjectResourceTreeNode(MapPhysicalLinkElement.typ,
				LangModelConfig.getString("label_links"), true);
			vec.add(ortn);
			registerSearchableNode(MapPhysicalLinkElement.typ, ortn);
			ortn = new ObjectResourceTreeNode(MapPhysicalNodeElement.typ,
				LangModelConfig.getString(
				"label_topologicalNodes"), true);
			vec.add(ortn);
			registerSearchableNode(MapPhysicalNodeElement.typ, ortn);
			ortn = new ObjectResourceTreeNode(MapTransmissionPathElement.typ,
				LangModelConfig.getString(
				"menuJCatPathText"), true);
			vec.add(ortn);
			registerSearchableNode(MapTransmissionPathElement.typ, ortn);
		}
		else if (node.getObject()instanceof MapEquipmentNodeElement)
		{
			vec.add(new ObjectResourceTreeNode(MapPhysicalLinkElement.typ,
				LangModelConfig.getString(
				"label_inputLinks"), true));
		}
		else if (node.getObject()instanceof MapPhysicalLinkElement)
		{
//      vec.add(new ObjectResourceTreeNode(MapMarkElement.typ, "Метки", true));
		}
		else if (node.getObject()instanceof MapTransmissionPathElement)
		{
//				vec.add(new ObjectResourceTreeNode("mappathelement", "Линии связи", true));
		}
		else if (node.getObject()instanceof Scheme)
		{
			vec.add(new ObjectResourceTreeNode(SchemeElement.typ,
				LangModelConfig.getString("label_nodes"), true));
			vec.add(new ObjectResourceTreeNode(SchemeLink.typ,
				LangModelConfig.getString("label_links"), true));
			vec.add(new ObjectResourceTreeNode(SchemeCableLink.typ,
				LangModelConfig.getString("label_cablelinks"),
				true));
			vec.add(new ObjectResourceTreeNode(SchemePath.typ,
				LangModelConfig.getString("menuJCatPathText"), true));
		}
///конец топологии

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

		if (node.getObject()instanceof APOReportModel)
		{
			APOReportModel rm =
				(APOReportModel) node.getObject();

			Vector fields = rm.getAvailableReports();

			for (int i = 0; i < fields.size(); i++)
			{
				String curName = (String) fields.get(i);
				String langName = rm.getLangForField(curName);

				ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
					new ObjectsReport((APOReportModel) node.getObject(),
					curName,
					APOReportModel.rt_tableReport,
					toAskObjects(node)),
					langName,
					true,
					new ImageIcon(Toolkit.getDefaultToolkit().getImage(
					"images/new.gif").
					getScaledInstance(16, 16, Image.SCALE_SMOOTH)));

				ortn.setDragDropEnabled(true);
				ortn.setFinal(true);

				vec.add(ortn);
			}
		}

		return vec;
	}

	public boolean toAskObjects(ObjectResourceTreeNode node)
	{
		boolean toFillImmediately = false;
		TreeNode[] curPath = node.getPath();
		if ((curPath != null) && curPath.length > 2)
		{
			String objType = (String) ((ObjectResourceTreeNode) curPath[1]).
				getObject();
			if (objType.equals("reportRoot"))
				toFillImmediately = true;
		}

		return toFillImmediately;
	}
}
