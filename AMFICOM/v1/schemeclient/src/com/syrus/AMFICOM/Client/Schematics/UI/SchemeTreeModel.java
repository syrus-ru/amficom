package com.syrus.AMFICOM.Client.Schematics.UI;

import java.util.*;

import java.awt.*;
import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.AccessPort;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class SchemeTreeModel extends ObjectResourceTreeModel
{
	DataSourceInterface dsi;

	public SchemeTreeModel(DataSourceInterface dsi)
	{
		this.dsi = dsi;
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode(
				"root",
				"Сеть",
				true,
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif")));
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
			if(s.equals("root"))
			{
				new ConfigDataSourceImage(dsi).LoadISM();
				new ConfigDataSourceImage(dsi).LoadNet();
			}
		}
		else
		{
		}
	}

	public ObjectResourceCatalogActionModel getNodeActionModel(ObjectResourceTreeNode node)
	{
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
/*			if(s.equals(KISType.typ) ||
							s.equals(EquipmentType.typ))
				return new ObjectResourceCatalogActionModel(
						ObjectResourceCatalogActionModel.PANEL,
						ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
						ObjectResourceCatalogActionModel.SAVE_BUTTON,
						ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
						ObjectResourceCatalogActionModel.PROPS_BUTTON,
						ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);*/
			if(s.equals(LinkType.typ) ||
							s.equals(CableLinkType.typ) ||
							s.equals(PortType.typ) ||
							s.equals(CablePortType.typ) ||
							s.equals(AccessPortType.typ) ||
							s.equals(TransmissionPathType.typ))
				return new ObjectResourceCatalogActionModel(
						ObjectResourceCatalogActionModel.PANEL,
						ObjectResourceCatalogActionModel.ADD_BUTTON,
						ObjectResourceCatalogActionModel.SAVE_BUTTON,
						ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
						ObjectResourceCatalogActionModel.PROPS_BUTTON,
						ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);
			else if(s.equals(SchemeCableLink.typ) ||
							s.equals(SchemeLink.typ) ||
							s.equals(SchemePath.typ) ||
							s.equals(SchemePort.typ) ||
							s.equals(SchemeCablePort.typ) ||
							s.equals(AccessPort.typ) ||
							s.equals(Scheme.typ) ||
							s.equals(SchemeElement.typ))
				return new ObjectResourceCatalogActionModel(
						ObjectResourceCatalogActionModel.PANEL,
						ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
						ObjectResourceCatalogActionModel.SAVE_BUTTON,
						ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
						ObjectResourceCatalogActionModel.PROPS_BUTTON,
						ObjectResourceCatalogActionModel.CANCEL_BUTTON);
			}
		return new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.NO_PANEL,
				ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
				ObjectResourceCatalogActionModel.NO_SAVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_PROPS_BUTTON,
				ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);
	}

	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals(MapProtoGroup.typ))
				return MapProtoGroup.class;
			if(s.equals(Scheme.typ))
				return Scheme.class;
			if(s.equals(SchemeElement.typ))
				return SchemeElement.class;
			if(s.equals(SchemeLink.typ))
				return SchemeLink.class;
			if(s.equals(SchemeCableLink.typ))
				return SchemeCableLink.class;
			if(s.equals(SchemePath.typ))
				return SchemePath.class;
			if(s.equals(MapProtoElement.typ))
				return MapProtoElement.class;
//			if(s.equals(EquipmentType.typ))
//				return EquipmentType.class;
			if(s.equals(LinkType.typ))
				return LinkType.class;
			if(s.equals(CableLinkType.typ))
				return CableLinkType.class;
			if(s.equals(PortType.typ))
				return PortType.class;
			if(s.equals(CablePortType.typ))
				return CablePortType.class;
//			if(s.equals(KISType.typ))
//				return KISType.class;
			if(s.equals(TransmissionPathType.typ))
				return TransmissionPathType.class;
			if(s.equals(AccessPortType.typ))
				return AccessPortType.class;

			if(s.equals("scheme_types"))
				return String.class;
			if(s.equals(Scheme.NETWORK) ||
				 s.equals(Scheme.CABLESUBNETWORK) ||
				 s.equals(Scheme.BUILDING) ||
				 s.equals(Scheme.FLOOR) ||
				 s.equals(Scheme.ROOM) ||
				 s.equals(Scheme.BAY) ||
				 s.equals(Scheme.CARDCAGE) ||
				 s.equals(Scheme.RACK))
				return Scheme.class;
		}
		else if (node.getObject() instanceof MapProtoGroup)
		{
			if (!((MapProtoGroup)node.getObject()).group_ids.isEmpty())
				return MapProtoGroup.class;
			else
				return MapProtoElement.class;
		}
		else if (node.getObject() instanceof MapProtoElement)
			return ProtoElement.class;
		else if (node.getObject() instanceof Scheme)
			return Scheme.class;
		else if (node.getObject() instanceof SchemeElement)
			return SchemeElement.class;
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
				vec.add(new ObjectResourceTreeNode("configure", LangModelConfig.getString("label_configuration"), true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				vec.add(new ObjectResourceTreeNode (MapProtoGroup.typ, "Компоненты сети", true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				ObjectResourceTreeNode sch = new ObjectResourceTreeNode ("scheme_types", "Схемы", true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif")));
				vec.add(sch);
				registerSearchableNode(Scheme.typ, sch);
			}
			else if(s.equals("configure"))
			{
				vec.add(new ObjectResourceTreeNode("netdirectory", LangModelConfig.getString("menuNetDirText"), true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				vec.add(new ObjectResourceTreeNode("jdirectory", LangModelConfig.getString("menuJDirText"), true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
			}
			else if(s.equals("netdirectory"))
			{
//				vec.add(new ObjectResourceTreeNode(EquipmentType.typ, LangModelConfig.getString("menuNetDirEquipmentText"), true));
				vec.add(new ObjectResourceTreeNode(LinkType.typ, LangModelConfig.getString("menuNetDirLinkText"), true));
				vec.add(new ObjectResourceTreeNode(CableLinkType.typ, LangModelConfig.getString("menuNetDirCableText"), true));
				vec.add(new ObjectResourceTreeNode(PortType.typ, LangModelConfig.getString("menuNetDirPortText"), true));
				vec.add(new ObjectResourceTreeNode(CablePortType.typ, LangModelConfig.getString("menuNetDirCablePortText"), true));
			}
			else if(s.equals("jdirectory"))
			{
//				vec.add(new ObjectResourceTreeNode(KISType.typ, LangModelConfig.getString("menuJDirKISText"), true));
				vec.add(new ObjectResourceTreeNode(AccessPortType.typ, LangModelConfig.getString("menuJDirAccessPointText"), true));
				vec.add(new ObjectResourceTreeNode(TransmissionPathType.typ, LangModelConfig.getString("menuJDirPathText"), true));
			}
			else if(s.equals("scheme_types"))
			{
				Map ht = new HashMap();
				if (Pool.getHash(Scheme.typ) != null)
				{
					Map dSet = Pool.getHash(Scheme.typ);
					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);

					for (Iterator it = dSet.values().iterator(); it.hasNext();)
					{
						Scheme sch = (Scheme)it.next();
						ht.put(sch.scheme_type, sch.scheme_type);
					}
					for (Iterator it = ht.keySet().iterator(); it.hasNext();)
					{
						String type = (String)it.next();
						vec.add(new ObjectResourceTreeNode (type, LangModelSchematics.getString(type), true,
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
					}
				}
			}
			else if(s.equals(Scheme.NETWORK) ||
							s.equals(Scheme.CABLESUBNETWORK) ||
							s.equals(Scheme.BUILDING) ||
							s.equals(Scheme.FLOOR) ||
							s.equals(Scheme.ROOM) ||
							s.equals(Scheme.BAY) ||
							s.equals(Scheme.CARDCAGE) ||
							s.equals(Scheme.RACK))
			{
				if (Pool.getHash(Scheme.typ) != null)
				{
					Map dSet = Pool.getHash(Scheme.typ);

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = Scheme.getSorter();
					sorter.setDataSet(dSet);

					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						Scheme sc = (Scheme )it.next();
						if (sc.scheme_type.equals(s))
						{
							vec.add(new ObjectResourceTreeNode(sc, sc.getName(), true,
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif"))));
						}
					}
				}
			}
/*			else if(s.equals(EquipmentType.typ))
			{
				if (Pool.getHash(EquipmentType.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(EquipmentType.typ));

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = EquipmentType.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						EquipmentType eq = (EquipmentType)enum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(eq, eq.getName(), true, true);
						vec.add(n);
					}
				}
			}*/
			else if(s.equals(LinkType.typ))
			{
				if (Pool.getHash(LinkType.typ) != null)
				{
					Map dSet = Pool.getHash(LinkType.typ);

					//ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					//dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = LinkType.getDefaultSorter();
					sorter.setDataSet(dSet);

					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						LinkType l = (LinkType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(l, l.getName(), true, true);
						vec.add(n);
					}
				}
			}
			else if(s.equals(CableLinkType.typ))
			{
				if (Pool.getHash(CableLinkType.typ) != null)
				{
					Map dSet = Pool.getHash(CableLinkType.typ);

//					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
//					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = CableLinkType.getDefaultSorter();
					sorter.setDataSet(dSet);

					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						CableLinkType l = (CableLinkType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(l, l.getName(), true, true);
						vec.add(n);
					}
				}
			}
			else if(s.equals(PortType.typ))
			{
				if (Pool.getHash(PortType.typ) != null)
				{
					Map dSet = Pool.getHash(PortType.typ);

//					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
//					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = PortType.getDefaultSorter();
					sorter.setDataSet(dSet);

					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						PortType pt = (PortType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(pt, pt.getName(), true, true);
						vec.add(n);
					}
				}
			}
			else if(s.equals(CablePortType.typ))
			{
				if (Pool.getHash(CablePortType.typ) != null)
				{
					Map dSet = Pool.getHash(CablePortType.typ);

//					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
//					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = CablePortType.getDefaultSorter();
					sorter.setDataSet(dSet);

					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						CablePortType cpT = (CablePortType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(cpT, cpT.getName(), true, true);
						vec.add(n);
					}
				}
			}
/*			else if(s.equals(KISType.typ))
			{
				if (Pool.getHash(EquipmentType.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(EquipmentType.typ));

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = KISType.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						EquipmentType k = (EquipmentType)enum.nextElement();
						if (!k.eq_class.equals("tester"))
							continue;
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(k, k.getName(), true, true);
						vec.add(n);
					}
				}
			}*/
			else if(s.equals(TransmissionPathType.typ))
			{
				if (Pool.getHash(TransmissionPathType.typ) != null)
				{
					Map dSet = Pool.getHash(TransmissionPathType.typ);

//					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
//					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = TransmissionPathType.getDefaultSorter();
					sorter.setDataSet(dSet);

					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						TransmissionPathType tp = (TransmissionPathType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(tp, tp.getName(), true, true);
						vec.add(n);
					}
				}
			}
			else if(s.equals(AccessPortType.typ))
			{
				if (Pool.getHash(AccessPortType.typ) != null)
				{
					Map dSet = Pool.getHash(AccessPortType.typ);

//					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
//					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = AccessPortType.getDefaultSorter();
					sorter.setDataSet(dSet);

					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						AccessPortType apt = (AccessPortType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(apt, apt.getName(), true, true);
						vec.add(n);
					}
				}
			}

			else if (s.equals(MapProtoGroup.typ))
			{
				Map map_groups = Pool.getHash(MapProtoGroup.typ);
				if (map_groups != null)
					for (Iterator it = map_groups.keySet().iterator(); it.hasNext();)
					{
						MapProtoGroup map_group = (MapProtoGroup)it.next();
						if (map_group.parent_id == null || map_group.parent_id.equals(""))
							vec.add(new ObjectResourceTreeNode(map_group, map_group.getName(), true,
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
					}
			}
			else if (s.equals(Scheme.typ))
			{
				Scheme parent = (Scheme)((ObjectResourceTreeNode)node.getParent()).getObject();
				ArrayList ds = new ArrayList();
				for (Iterator it = parent.elements.iterator(); it.hasNext();)
				{
					SchemeElement el = (SchemeElement)it.next();
					if (!el.scheme_id.equals(""))
					{
						Scheme sc = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
						if (sc != null)
							ds.add(sc);
					}
				}
				if (ds.size() > 0)
				{
					ObjectResourceSorter sorter = Scheme.getSorter();
					sorter.setDataSet(ds);
					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						Scheme sch = (Scheme)it.next();
						vec.add(new ObjectResourceTreeNode(sch, sch.getName(), true,
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")), false));
					}
				}
			}
			else if (s.equals(SchemeElement.typ))
			{
				Object parent = ((ObjectResourceTreeNode)node.getParent()).getObject();
				ArrayList ds = new ArrayList();
				if (parent instanceof Scheme)
				{
					Scheme scheme = (Scheme)parent;
					for (Iterator it = scheme.elements.iterator(); it.hasNext();)
					{
						SchemeElement element = (SchemeElement)it.next();
						if (element.scheme_id.equals(""))
							ds.add(element);
					}
				}
				else if (parent instanceof SchemeElement)
				{
					SchemeElement el = (SchemeElement)parent;
					for (Iterator it = el.element_ids.iterator(); it.hasNext();)
					{
						SchemeElement element = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
						if (element != null)
							ds.add(element);
					}
				}
				if (ds.size() > 0)
				{
					ObjectResourceSorter sorter = SchemeElement.getSorter();
					sorter.setDataSet(ds);
					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						SchemeElement element = (SchemeElement)it.next();
						if (!element.links.isEmpty() || !element.element_ids.isEmpty())
							vec.add(new ObjectResourceTreeNode(element, element.getName(), true, false));
						else
							vec.add(new ObjectResourceTreeNode(element, element.getName(), true, true));
					}
				}
			}
			else if (s.equals(SchemeLink.typ))
			{
				Object parent = ((ObjectResourceTreeNode)node.getParent()).getObject();
				if (parent instanceof Scheme)
				{
					Scheme scheme = (Scheme)parent;
					ObjectResourceSorter sorter = SchemeLink.getSorter();
					sorter.setDataSet(scheme.links);
					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						SchemeLink link = (SchemeLink)it.next();
						vec.add(new ObjectResourceTreeNode(link, link.getName(), true, true));
					}
				}
				else if (parent instanceof SchemeElement)
				{
					SchemeElement el = (SchemeElement)parent;
					ObjectResourceSorter sorter = SchemeLink.getSorter();
					sorter.setDataSet(el.links);
					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						SchemeLink link = (SchemeLink)it.next();
						vec.add(new ObjectResourceTreeNode(link, link.getName(), true, true));
					}
				}
			}
			else if (s.equals(SchemeCableLink.typ))
			{
				Scheme parent = (Scheme)((ObjectResourceTreeNode)node.getParent()).getObject();
				ObjectResourceSorter sorter = SchemeCableLink.getSorter();
				sorter.setDataSet(parent.cablelinks);
				for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
				{
					SchemeCableLink link = (SchemeCableLink)it.next();
					vec.add(new ObjectResourceTreeNode(link, link.getName(), true, true));
				}
			}
			else if (s.equals(SchemePath.typ))
			{
				Scheme parent = (Scheme)((ObjectResourceTreeNode)node.getParent()).getObject();
				ObjectResourceSorter sorter = SchemePath.getSorter();
				sorter.setDataSet(parent.paths);
				for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
				{
					SchemePath path = (SchemePath)it.next();
					vec.add(new ObjectResourceTreeNode(path, path.getName(), true, true));
				}
			}
		}
		else
		{
			if(node.getObject() instanceof MapProtoGroup)
			{
				MapProtoGroup parent_group = (MapProtoGroup)node.getObject();
				for (int i = 0; i < parent_group.group_ids.size(); i++)
				{
					MapProtoGroup map_group = (MapProtoGroup)Pool.get(MapProtoGroup.typ, (String)parent_group.group_ids.get(i));
					vec.add(new ObjectResourceTreeNode(map_group, map_group.getName(), true,
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				}
				if (vec.isEmpty())
					for (int i = 0; i < parent_group.mapproto_ids.size(); i++)
					{
						MapProtoElement map_proto = (MapProtoElement)Pool.get(MapProtoElement.typ, (String)parent_group.mapproto_ids.get(i));
						String image_id = (map_proto.getImageID().equals("") ? "pc" : map_proto.getImageID());
						map_proto.setImageID(image_id);
						ImageResource ir = ImageCatalogue.get(image_id);

						if (ir == null)
							vec.add(new ObjectResourceTreeNode(map_proto, map_proto.getName(), true));
						else
							vec.add(new ObjectResourceTreeNode(map_proto, map_proto.getName(), true,
									new ImageIcon(ir.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH))));
					}
			}
			else if(node.getObject() instanceof MapProtoElement)
			{
				MapProtoElement map_proto = (MapProtoElement)node.getObject();
				for (int i = 0; i < map_proto.pe_ids.size(); i++)
				{
					ProtoElement proto = (ProtoElement)Pool.get(ProtoElement.typ, (String)map_proto.pe_ids.get(i));
					proto.map_proto = map_proto;
					ObjectResourceTreeNode ortn = new ObjectResourceTreeNode(proto, proto.getName(), true, true);
					ortn.setDragDropEnabled(true);
					vec.add(ortn);
				}
			}
			else if(node.getObject() instanceof Scheme)
			{
				Scheme s = (Scheme)node.getObject();
				if (!s.elements.isEmpty())
				{
					boolean has_schemes = false;
					boolean has_elements = false;
					for (Iterator it = s.elements.iterator(); it.hasNext();)
					{
						SchemeElement el = (SchemeElement)it.next();
						if (el.scheme_id.length() == 0)
						{
							has_elements = true;
							break;
						}
					}
					for (Iterator it = s.elements.iterator(); it.hasNext();)
					{
						SchemeElement el = (SchemeElement)it.next();
						if (el.scheme_id.length() != 0)
						{
							has_schemes = true;
							break;
						}
					}
					if (has_schemes)
						vec.add(new ObjectResourceTreeNode(Scheme.typ, "Вложенные схемы", true));
					if (has_elements)
						vec.add(new ObjectResourceTreeNode(SchemeElement.typ, "Узлы", true));
				}
				if (!s.links.isEmpty())
					vec.add(new ObjectResourceTreeNode(SchemeLink.typ, "Линии", true));
				if (!s.cablelinks.isEmpty())
					vec.add(new ObjectResourceTreeNode(SchemeCableLink.typ, "Кабели", true));
				if (!s.paths.isEmpty())
					vec.add(new ObjectResourceTreeNode(SchemePath.typ, "Пути", true));
			}
			else if(node.getObject() instanceof SchemeElement)
			{
				SchemeElement schel = (SchemeElement)node.getObject();
				if (!schel.scheme_id.equals(""))
				{
					Scheme scheme = (Scheme)Pool.get(Scheme.typ, schel.scheme_id);
					for (Iterator it = scheme.elements.iterator(); it.hasNext();)
					{
						SchemeElement element = (SchemeElement)it.next();
						if (element.scheme_id.equals(""))
							vec.add(new ObjectResourceTreeNode(element, element.getName(), true, true));
						else
							vec.add(new ObjectResourceTreeNode(element, element.getName(), true,
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")), false));
					}
				}
				else
				{
					if (!schel.element_ids.isEmpty())
						vec.add(new ObjectResourceTreeNode(SchemeElement.typ, "Вложенные элементы", true));
				 if (!schel.links.isEmpty())
						vec.add(new ObjectResourceTreeNode(SchemeLink.typ, "Линии", true));
				}
			}
		}
		return vec;
	}
}
