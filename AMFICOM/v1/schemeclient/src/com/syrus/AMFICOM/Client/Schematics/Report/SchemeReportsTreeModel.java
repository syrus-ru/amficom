package com.syrus.AMFICOM.Client.Schematics.Report;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.util.List;
import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.Configure.Report.EquipFeaturesReportModel;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Report.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

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
			//Нулевой уровень - разделение на отчёты и элементы шаблона

			if (s.equals("root"))
			{
				ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(
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
			}

			else if (s.equals("EquipmentChars"))
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
					LangModelConfig.getString("multiplexer"),
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

			else if (s.equals("mufta") ||
				s.equals("switch") ||
				s.equals("cross") ||
				s.equals("multiplexor") ||
				s.equals("filter") ||
				s.equals("transmitter") ||
				s.equals("reciever") ||
				s.equals("tester"))
			{
				Map equipTypeHash = Pool.getMap(EquipmentType.typ);
				if (equipTypeHash == null)
					return new Vector();

				Iterator equipTypeEnum = equipTypeHash.values().iterator();
				while (equipTypeEnum.hasNext())
				{
					EquipmentType eqType = (EquipmentType) equipTypeEnum.next();
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
				Set ht = new HashSet();
				if (Pool.getMap(Scheme.typ) != null)
				{
					for(Iterator it = Pool.getMap(Scheme.typ).values().iterator(); it.hasNext();)
					{
						Scheme sch = (Scheme)it.next();
						ht.add(sch.scheme_type);
					}

					for (Iterator it = ht.iterator(); it.hasNext(); )
					{
						String type = (String)it.next();
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
			else if ((((ObjectResourceTreeNode) node.getParent()).getObject()instanceof
				String) &&
				((ObjectResourceTreeNode) node.getParent()).getObject().equals(
				"label_repPhysicalScheme"))
			{

				Map dSet = Pool.getMap(Scheme.typ);

				/*          ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
				 dSet = filter.filter(dSet);
				 ObjectResourceSorter sorter = Scheme.getDefaultSorter();
				 sorter.setDataSet(dSet);
				 dSet = sorter.default_sort();*/

				for (Iterator it = dSet.values().iterator(); it.hasNext(); )
				{
					Scheme scheme = (Scheme)it.next();
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
			for (Iterator it = scheme.elements.iterator(); it.hasNext(); )
			{
				SchemeElement element = (SchemeElement)it.next();
				if (element.getInternalSchemeId().length() == 0)
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
						or.setReserve(element.getInternalSchemeId());
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
			for (Iterator it = scheme.links.iterator(); it.hasNext();)
			{
				SchemeLink link = (SchemeLink)it.next();
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
			for (Iterator it = scheme.cablelinks.iterator(); it.hasNext();)
			{
				SchemeCableLink link = (SchemeCableLink)it.next();
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
			if (schel.getInternalSchemeId().length() != 0)
			{
				Scheme scheme = schel.getInternalScheme();
				for (Iterator it = scheme.elements.iterator(); it.hasNext(); )
				{
					SchemeElement element = (SchemeElement)it.next();

					if (element.getInternalSchemeId().length() == 0)
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

