package com.syrus.AMFICOM.Client.Schematics.Report;

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
//import com.syrus.AMFICOM.Client.Survey.Report.AlarmReportModel;
import com.syrus.AMFICOM.Client.Configure.Report.EquipFeaturesReportModel;
import com.syrus.AMFICOM.Client.Schematics.Report.SchemeReportModel;

import com.syrus.AMFICOM.Client.Map.Report.MapReportModel;
import com.syrus.AMFICOM.Client.Optimize.Report.OptimizationReportModel;
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

public class SchemeReportsTreeModel extends ObjectResourceTreeModel
{
	public SchemeReportsTreeModel()
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
			//Нулевой уровень - разделение на отчёты и элементы шаблона

			if (s.equals("root"))
			{
				ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
					"EquipmentChars",
					LangModelConfig.String("label_equipChars"),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				ortn = new ObjectResourceTreeNode(
					"label_repPhysicalScheme",
					LangModelReport.String("label_repPhysicalScheme"),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);
			}

			else if (s.equals("EquipmentChars"))
			{
				ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
					"mufta",
					LangModelConfig.String("mufta"),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				ortn = new ObjectResourceTreeNode(
					"switch",
					LangModelConfig.String("switch"),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				ortn = new ObjectResourceTreeNode(
					"cross",
					LangModelConfig.String("cross"),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				ortn = new ObjectResourceTreeNode(
					"multiplexor",
					LangModelConfig.String("multiplexer"),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				ortn = new ObjectResourceTreeNode(
					"filter",
					LangModelConfig.String("filter"),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				ortn = new ObjectResourceTreeNode(
					"transmitter",
					LangModelConfig.String("transmitter"),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				ortn = new ObjectResourceTreeNode(
					"reciever",
					LangModelConfig.String("reciever"), //поменять на receiver
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);

				ortn = new ObjectResourceTreeNode(
					"tester",
					LangModelConfig.String("tester"),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);
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
					 LangModelReport.String(ObjectResourceReportModel.rt_objectsReport),
				 true,
				 new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/new.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
				 ortn.setDragDropEnabled(true);
				 vec.add(ortn);
				 registerSearchableNode("", ortn);*/

			}

			else if (s.equals("label_repPhysicalScheme"))
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
						String name = LangModelSchematics.String(type);
						if (type.equals(""))
							name = "Схема";

						vec.add(new ObjectResourceTreeNode(type,
							name, true,
							new ImageIcon(Toolkit.
							getDefaultToolkit().getImage("images/folder.gif"))));
					}
				}
			}
			else if ((((ObjectResourceTreeNode) node.getParent()).getObject()instanceof
				String) &&
				((ObjectResourceTreeNode) node.getParent()).getObject().equals(
				"label_repPhysicalScheme"))
			{

				DataSet dSet = new DataSet(Pool.getHash(Scheme.typ));

				/*          ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
				 dSet = filter.filter(dSet);
				 ObjectResourceSorter sorter = Scheme.getDefaultSorter();
				 sorter.setDataSet(dSet);
				 dSet = sorter.default_sort();*/

				for (Enumeration enum = dSet.elements(); enum.hasMoreElements(); )
				{
					Scheme scheme = (Scheme) enum.nextElement();
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
			for (Enumeration enum = scheme.elements.elements();
				enum.hasMoreElements(); )
			{
				SchemeElement element = (SchemeElement) enum.nextElement();
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
				for (Enumeration enum = scheme.elements.elements();
					enum.hasMoreElements(); )
				{
					SchemeElement element = (SchemeElement) enum.nextElement();

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

		return vec;
	}

	public boolean toAskObjects(ObjectResourceTreeNode node)
	{
		return true;
	}
}