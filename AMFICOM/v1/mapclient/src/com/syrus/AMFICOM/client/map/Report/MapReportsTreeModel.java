package com.syrus.AMFICOM.Client.Map.Report;

import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceDomainFilter;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Image;

import java.util.List;
import java.util.ArrayList;

import java.util.ListIterator;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;

import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceSorter;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;

import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

import com.syrus.AMFICOM.Client.General.Report.ObjectsReport;
import com.syrus.AMFICOM.Client.General.Report.APOReportModel;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;

import com.syrus.AMFICOM.Client.Map.Report.MapReportModel;
import java.util.Iterator;

/**
 * <p>Description: Модель дерева с доступными отчётами</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class MapReportsTreeModel extends ObjectResourceTreeModel
{
	public MapReportsTreeModel()
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
					"label_repTopologicalScheme",
					LangModelReport.getString("label_repTopologicalScheme"),
					true);

				vec.add(ortn);
				registerSearchableNode("", ortn);
			}

			else if (s.equals("label_repTopologicalScheme"))
			{
				Map map = Pool.getMap(MapContext.typ);      
				if (map != null)
				{
/*					  ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					  filter.filter(map);
					  ObjectResourceSorter sorter = MapContext.getDefaultSorter();
					  sorter.setDataSet(map);
					  sorter.default_sort();
*/
					for(Iterator it = map.values().iterator(); it.hasNext();)
					{
						MapContext mc = (MapContext) it.next();
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

				List dSet = new ArrayList();
				for (ListIterator it = mc.getNodes().listIterator(); it.hasNext();)
				{
					ObjectResource os = (ObjectResource) it.next();
					if (os instanceof MapEquipmentNodeElement)
						dSet.add(os);
				}

				ObjectResourceSorter sorter = MapEquipmentNodeElement.
					getDefaultSorter();
				sorter.setDataSet(dSet);
				sorter.default_sort();

				for(Iterator it = dSet.iterator(); it.hasNext();)
				{
					MapEquipmentNodeElement me = (MapEquipmentNodeElement)it.next();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(me,
						me.getName(), true);
					vec.add(n);
				}
			}
//         else if (s.equals(MapKISNodeElement.typ))
//         {
//            ObjectResourceTreeNode parent = (ObjectResourceTreeNode) node.
//               getParent();
//            MapContext mc = (MapContext) parent.getObject();
//            DataSet dSet = new DataSet();
//            for (Enumeration enum = mc.getNodes().elements();
//                 enum.hasMoreElements(); )
//            {
//               ObjectResource os = (ObjectResource) enum.nextElement();
//               if (os instanceof MapKISNodeElement)
//                  dSet.add(os);
//            }
//            ObjectResourceSorter sorter = MapEquipmentNodeElement.
//               getDefaultSorter();
//            sorter.setDataSet(dSet);
//            dSet = sorter.default_sort();
//            Enumeration enum = dSet.elements();
//            for (; enum.hasMoreElements(); )
//            {
//               MapKISNodeElement me = (MapKISNodeElement) enum.nextElement();
//               ObjectResourceTreeNode n = new ObjectResourceTreeNode(me,
//                  me.getName(), true);
//               vec.add(n);
//            }
//         }

			else if (s.equals(MapPhysicalNodeElement.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode) node.
					getParent();
				MapContext mc = (MapContext) parent.getObject();
        
        List dSet = new ArrayList();
        
				for (ListIterator it = mc.getNodes().listIterator(); it.hasNext();)
				{
					ObjectResource os = (ObjectResource) it.hasNext();
					if (os instanceof MapPhysicalNodeElement)
						dSet.add(os);
				}

				ObjectResourceSorter sorter = MapPhysicalNodeElement.
					getDefaultSorter();
				sorter.setDataSet(dSet);
				sorter.default_sort();

				for(Iterator it = dSet.iterator(); it.hasNext();)
				{
					MapPhysicalNodeElement me = (MapPhysicalNodeElement)it.next();
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
				List dSet = null;
				if (parent.getObject()instanceof MapContext)
				{
					mc = (MapContext) parent.getObject();
					dSet = mc.getPhysicalLinks();
				}
				else
				{
					MapEquipmentNodeElement mene = (MapEquipmentNodeElement) parent.
						getObject();
					mc = mene.getMapContext();

					dSet = new ArrayList();

          for (ListIterator it = mc.getPhysicalLinks().listIterator(); it.hasNext();)
					{
						MapPhysicalLinkElement ml = (MapPhysicalLinkElement) it.next();

						if (ml.startNode.equals(mene) || ml.endNode.equals(mene))
							dSet.add(ml);
					}
				}

				ObjectResourceSorter sorter = MapPhysicalLinkElement.
					getDefaultSorter();
				sorter.setDataSet(dSet);
				sorter.default_sort();

				for(Iterator it = dSet.iterator(); it.hasNext();)
				{
					MapPhysicalLinkElement ml = (MapPhysicalLinkElement)it.next();

					String curName = ml.getName();
					ObjectsReport curReport = new ObjectsReport(new MapReportModel(),
						curName, MapReportModel.rep_topology, toAskObjects(node));

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

				List dSet = mc.getTransmissionPath();

				ObjectResourceSorter sorter = MapTransmissionPathElement.
					getDefaultSorter();
				sorter.setDataSet(dSet);
				sorter.default_sort();

				for(Iterator it = dSet.iterator(); it.hasNext();)
				{
					MapTransmissionPathElement me = (MapTransmissionPathElement)it.next();
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

				List dSet = new ArrayList();
				for (ListIterator it = mc.getMapMarkElements().listIterator(); it.hasNext();)
				{
					MapMarkElement mme = (MapMarkElement) it.next();
					if (mme.link_id.equals(ml.getId()))
						dSet.add(mme);
				}

				ObjectResourceSorter sorter = MapMarkElement.getDefaultSorter();
				sorter.setDataSet(dSet);
				sorter.default_sort();

				for(Iterator it = dSet.iterator(); it.hasNext();)
				{
					MapMarkElement mme = (MapMarkElement )it.next();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(mme,
						mme.getName(), true);
					vec.add(n);
				}
			}
		}
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


		return vec;
	}

	public boolean toAskObjects(ObjectResourceTreeNode node)
	{
		return true;
	}
}