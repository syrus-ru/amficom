package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.Color;

import java.util.Vector;
import java.util.Enumeration;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceDomainFilter;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.SchemeDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.MapDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceSorter;

import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;

import com.syrus.AMFICOM.Client.Resource.Map.*;

public class MapTreeModel extends ObjectResourceTreeModel
{
	DataSourceInterface dsi;

	public MapTreeModel(DataSourceInterface dsi)
	{
		this.dsi = dsi;
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode("root", LangModelConfig.String("label_schemeStructure"), true);
	}

	public ImageIcon getNodeIcon(ObjectResourceTreeNode node)
	{
		return null;
	}

	public Color getNodeTextColor(ObjectResourceTreeNode node)
	{
		return null;
	}

	public void nodeAfterSelected(ObjectResourceTreeNode node)
	{
	}

	public void nodeBeforeExpanded(ObjectResourceTreeNode node)
	{
		if(node.expanded)
			return;
		node.expanded = true;

		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals(Scheme.typ))
			{
				new SchemeDataSourceImage(dsi).LoadSchemes();
			}
			else
			if(s.equals(MapContext.typ))
			{
				new MapDataSourceImage(dsi).LoadMaps();
			}
		}
		else
		{
		}
	}

	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals(MapContext.typ))
				return MapContext.class;
			if(s.equals(MapEquipmentNodeElement.typ))
				return MapEquipmentNodeElement.class;
			if(s.equals(MapKISNodeElement.typ))
				return MapKISNodeElement.class;
			if(s.equals(MapPhysicalNodeElement.typ))
				return MapPhysicalNodeElement.class;
			if(s.equals(MapPhysicalLinkElement.typ))
				return MapPhysicalLinkElement.class;
			if(s.equals(MapTransmissionPathElement.typ))
				return MapTransmissionPathElement.class;
			if(s.equals(MapMarkElement.typ))
				return MapMarkElement.class;
			if(s.equals(Scheme.typ))
				return Scheme.class;
		}
		else
		{
		}
		return null;
	}

	public Vector getChildNodes(ObjectResourceTreeNode node)
	{
		Vector vec = new Vector();
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			ObjectResource os;
			if(s.equals("root"))
			{
				ObjectResourceTreeNode ortn;
				ortn = new ObjectResourceTreeNode(Scheme.typ, LangModelConfig.String("menuSchemeText"), true);
				vec.add(ortn);
				registerSearchableNode(Scheme.typ, ortn);
				ortn = new ObjectResourceTreeNode(MapContext.typ, LangModelConfig.String("label_topologicalScheme"), true);
				vec.add(ortn);
				registerSearchableNode(MapContext.typ, ortn);
			}
			else
			if(s.equals(MapContext.typ))
			{
				if (Pool.getHash(MapContext.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(MapContext.typ));

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = MapContext.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						MapContext mc = (MapContext )enum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(mc, mc.getName(), true);
						vec.add(n);
					}
				}
			}
			else
			if(s.equals(MapEquipmentNodeElement.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MapContext mc = (MapContext )parent.getObject();

				DataSet dSet = new DataSet();
				for(Enumeration enum = mc.getNodes().elements(); enum.hasMoreElements();)
				{
					os = (ObjectResource )enum.nextElement();
					if(os instanceof MapEquipmentNodeElement)
						dSet.add(os);
				}

				ObjectResourceSorter sorter = MapEquipmentNodeElement.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enum = dSet.elements();
				for(; enum.hasMoreElements();)
				{
					MapEquipmentNodeElement me = (MapEquipmentNodeElement )enum.nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(me, me.getName(), true);
					vec.add(n);
				}
			}
			else
			if(s.equals(MapKISNodeElement.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MapContext mc = (MapContext )parent.getObject();

				DataSet dSet = new DataSet();
				for(Enumeration enum = mc.getNodes().elements(); enum.hasMoreElements();)
				{
					os = (ObjectResource )enum.nextElement();
					if(os instanceof MapKISNodeElement)
						dSet.add(os);
				}

				ObjectResourceSorter sorter = MapEquipmentNodeElement.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enum = dSet.elements();
				for(; enum.hasMoreElements();)
				{
					MapKISNodeElement me = (MapKISNodeElement )enum.nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(me, me.getName(), true);
					vec.add(n);
				}
			}
			else
			if(s.equals(MapPhysicalNodeElement.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MapContext mc = (MapContext )parent.getObject();

				DataSet dSet = new DataSet();
				for(Enumeration enum = mc.getNodes().elements(); enum.hasMoreElements();)
				{
					os = (ObjectResource )enum.nextElement();
					if(os instanceof MapPhysicalNodeElement)
						dSet.add(os);
				}

				ObjectResourceSorter sorter = MapEquipmentNodeElement.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enum = dSet.elements();
				for(; enum.hasMoreElements();)
				{
					MapPhysicalNodeElement me = (MapPhysicalNodeElement )enum.nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(me, me.getName(), true);
					vec.add(n);
				}
			}
			else
			if(s.equals(MapPhysicalLinkElement.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MapContext mc = null;
				DataSet dSet = null;
				if(parent.getObject() instanceof MapContext)
				{
					mc = (MapContext )parent.getObject();
					dSet = new DataSet(mc.getPhysicalLinks());
				}
				else
				{
					MapEquipmentNodeElement mene = (MapEquipmentNodeElement )parent.getObject();
					mc = mene.getMapContext();

					dSet = new DataSet();
					for(Enumeration enum = mc.getPhysicalLinks().elements(); enum.hasMoreElements();)
					{
						MapPhysicalLinkElement ml = (MapPhysicalLinkElement )enum.nextElement();
						if(ml.startNode.equals(mene) || ml.endNode.equals(mene))
							dSet.add(ml);
					}
				}

				ObjectResourceSorter sorter = MapPhysicalLinkElement.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enum = dSet.elements();
				for(; enum.hasMoreElements();)
				{
					MapPhysicalLinkElement ml = (MapPhysicalLinkElement )enum.nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(ml, ml.getName(), true);
					vec.add(n);
				}
			}
			else
			if(s.equals(MapTransmissionPathElement.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MapContext mc = (MapContext )parent.getObject();

				DataSet dSet = new DataSet(mc.getTransmissionPath());

				ObjectResourceSorter sorter = MapTransmissionPathElement.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enum = dSet.elements();
				for(; enum.hasMoreElements();)
				{
					MapTransmissionPathElement me = (MapTransmissionPathElement )enum.nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(me, me.getName(), true);
					vec.add(n);
				}
			}
			else
			if(s.equals(MapMarkElement.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MapPhysicalLinkElement ml = (MapPhysicalLinkElement )parent.getObject();
				MapContext mc = ml.getMapContext();

				DataSet dSet = new DataSet();
				for(Enumeration enum = mc.getMapMarkElements().elements(); enum.hasMoreElements();)
				{
					MapMarkElement mme = (MapMarkElement )enum.nextElement();
					if(mme.link_id.equals(ml.getId()))
						dSet.add(mme);
				}

				ObjectResourceSorter sorter = MapMarkElement.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enum = dSet.elements();
				for(; enum.hasMoreElements();)
				{
					MapMarkElement mme = (MapMarkElement )enum.nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(mme, mme.getName(), true);
					vec.add(n);
				}
			}
			else
			if(s.equals(Scheme.typ))
			{
				if (Pool.getHash(Scheme.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(Scheme.typ));

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = Scheme.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						Scheme sc = (Scheme )enum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(sc, sc.getName(), true);
						vec.add(n);
					}
				}
			}
		}
		else
		{
			if(node.getObject() instanceof MapContext)
			{
				ObjectResourceTreeNode ortn;
				ortn = new ObjectResourceTreeNode(MapEquipmentNodeElement.typ, LangModelConfig.String("label_nodes"), true);
				vec.add(ortn);
				registerSearchableNode(MapEquipmentNodeElement.typ, ortn);

//				vec.add(new ObjectResourceTreeNode(MapKISNodeElement.typ, "Узлы с КИС", true));
				ortn = new ObjectResourceTreeNode(MapPhysicalLinkElement.typ, LangModelConfig.String("label_links"), true);
				vec.add(ortn);
				registerSearchableNode(MapPhysicalLinkElement.typ, ortn);
				ortn = new ObjectResourceTreeNode(MapPhysicalNodeElement.typ, LangModelConfig.String("label_topologicalNodes"), true);
				vec.add(ortn);
				registerSearchableNode(MapPhysicalNodeElement.typ, ortn);
				ortn = new ObjectResourceTreeNode(MapTransmissionPathElement.typ, LangModelConfig.String("menuJCatPathText"), true);
				vec.add(ortn);
				registerSearchableNode(MapTransmissionPathElement.typ, ortn);
			}
			else
			if(node.getObject() instanceof MapEquipmentNodeElement)
			{
				vec.add(new ObjectResourceTreeNode(MapPhysicalLinkElement.typ, LangModelConfig.String("label_inputLinks"), true));
			}
			else
			if(node.getObject() instanceof MapPhysicalLinkElement)
			{
				vec.add(new ObjectResourceTreeNode(MapMarkElement.typ, LangModelConfig.String("label_marks"), true));
			}
			else
			if(node.getObject() instanceof MapTransmissionPathElement)
			{
//				vec.add(new ObjectResourceTreeNode("mappathelement", "Линии связи", true));
			}
			else
			if(node.getObject() instanceof Scheme)
			{
				vec.add(new ObjectResourceTreeNode(SchemeElement.typ, LangModelConfig.String("label_nodes"), true));
				vec.add(new ObjectResourceTreeNode(SchemeLink.typ, LangModelConfig.String("label_links"), true));
				vec.add(new ObjectResourceTreeNode(SchemeCableLink.typ, LangModelConfig.String("label_cablelinks"), true));
				vec.add(new ObjectResourceTreeNode(SchemePath.typ, LangModelConfig.String("menuJCatPathText"), true));
			}
		}
		return vec;
	}
}

