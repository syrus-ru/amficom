package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.MapDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.SchemeDataSourceImage;

import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapPathElement;

import java.awt.Color;

import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;

public class MapViewTreeModel extends ObjectResourceTreeModel
{
	DataSourceInterface dsi;

	public MapViewTreeModel(DataSourceInterface dsi)
	{
		this.dsi = dsi;
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode("root", LangModelMap.getString("label_schemeStructure"), true);
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
			if(s.equals(com.syrus.AMFICOM.Client.Resource.Map.Map.typ))
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
			if(s.equals(com.syrus.AMFICOM.Client.Resource.Map.Map.typ))
				return com.syrus.AMFICOM.Client.Resource.Map.Map.class;
			if(s.equals(MapSiteNodeElement.typ))
				return MapSiteNodeElement.class;
			if(s.equals(MapPhysicalNodeElement.typ))
				return MapPhysicalNodeElement.class;
			if(s.equals(MapPhysicalLinkElement.typ))
				return MapPhysicalLinkElement.class;
			if(s.equals(MapPathElement.typ))
				return MapPathElement.class;
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

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		Vector vec = new Vector();
/*
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			ObjectResource os;
			if(s.equals("root"))
			{
				ObjectResourceTreeNode ortn;
				ortn = new ObjectResourceTreeNode(Scheme.typ, LangModelConfig.getString("menuSchemeText"), true);
				vec.add(ortn);
				registerSearchableNode(Scheme.typ, ortn);
				ortn = new ObjectResourceTreeNode(MapContext.typ, LangModelConfig.getString("label_topologicalScheme"), true);
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
			if(s.equals(MapSiteNodeElement.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				MapContext mc = (MapContext )parent.getObject();

				DataSet dSet = new DataSet();
				for(Iterator it = mc.getNodes().iterator(); it.hasNext();)
				{
					os = (ObjectResource )it.next();
					if(os instanceof MapSiteNodeElement)
						dSet.add(os);
				}

				ObjectResourceSorter sorter = MapSiteNodeElement.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enum = dSet.elements();
				for(; enum.hasMoreElements();)
				{
					MapSiteNodeElement me = (MapSiteNodeElement )enum.nextElement();
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
				for(Iterator it = mc.getNodes().iterator(); it.hasNext();)
				{
					os = (ObjectResource )it.next();
					if(os instanceof MapPhysicalNodeElement)
						dSet.add(os);
				}

				ObjectResourceSorter sorter = MapSiteNodeElement.getDefaultSorter();
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
					MapSiteNodeElement mene = (MapSiteNodeElement )parent.getObject();
					mc = mene.getMapContext();

					dSet = new DataSet();
					for(Iterator it = mc.getPhysicalLinks().iterator(); it.hasNext();)
					{
						MapPhysicalLinkElement ml = (MapPhysicalLinkElement )it.next();
						if(ml.getStartNode().equals(mene) || ml.getEndNode().equals(mene))
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
			if(s.equals(MapPathElement.typ))
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
				for(Iterator it = mc.getMapMarkElements().iterator(); it.hasNext();)
				{
					MapMarkElement mme = (MapMarkElement )it.next();
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
				ortn = new ObjectResourceTreeNode(MapSiteNodeElement.typ, LangModelConfig.getString("label_nodes"), true);
				vec.add(ortn);
				registerSearchableNode(MapSiteNodeElement.typ, ortn);

//				vec.add(new ObjectResourceTreeNode(MapKISNodeElement.typ, "Узлы с КИС", true));
				ortn = new ObjectResourceTreeNode(MapPhysicalLinkElement.typ, LangModelConfig.getString("label_links"), true);
				vec.add(ortn);
				registerSearchableNode(MapPhysicalLinkElement.typ, ortn);
				ortn = new ObjectResourceTreeNode(MapPhysicalNodeElement.typ, LangModelConfig.getString("label_topologicalNodes"), true);
				vec.add(ortn);
				registerSearchableNode(MapPhysicalNodeElement.typ, ortn);
				ortn = new ObjectResourceTreeNode(MapTransmissionPathElement.typ, LangModelConfig.getString("menuJCatPathText"), true);
				vec.add(ortn);
				registerSearchableNode(MapTransmissionPathElement.typ, ortn);
			}
			else
			if(node.getObject() instanceof MapSiteNodeElement)
			{
				vec.add(new ObjectResourceTreeNode(MapPhysicalLinkElement.typ, LangModelConfig.getString("label_inputLinks"), true));
			}
			else
			if(node.getObject() instanceof MapPhysicalLinkElement)
			{
				vec.add(new ObjectResourceTreeNode(MapMarkElement.typ, LangModelConfig.getString("label_marks"), true));
			}
			else
			if(node.getObject() instanceof MapTransmissionPathElement)
			{
//				vec.add(new ObjectResourceTreeNode("mappathelement", "Линии связи", true));
			}
			else
			if(node.getObject() instanceof Scheme)
			{
				vec.add(new ObjectResourceTreeNode(SchemeElement.typ, LangModelConfig.getString("label_nodes"), true));
				vec.add(new ObjectResourceTreeNode(SchemeLink.typ, LangModelConfig.getString("label_links"), true));
				vec.add(new ObjectResourceTreeNode(SchemeCableLink.typ, LangModelConfig.getString("label_cablelinks"), true));
				vec.add(new ObjectResourceTreeNode(SchemePath.typ, LangModelConfig.getString("menuJCatPathText"), true));
			}
		}
*/
		return vec;
	}
}

