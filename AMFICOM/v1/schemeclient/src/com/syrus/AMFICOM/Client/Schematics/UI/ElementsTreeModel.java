package com.syrus.AMFICOM.Client.Schematics.UI;

import java.awt.*;
import java.util.*;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.TransmissionPathType;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.MapProtoGroup;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class ElementsTreeModel extends ObjectResourceTreeModel
{
	DataSourceInterface dsi;

	public ElementsTreeModel(DataSourceInterface dsi)
	{
		this.dsi = dsi;
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode ("root", "Компоненты сети", true,
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
	}

	public ObjectResourceCatalogActionModel getNodeActionModel(ObjectResourceTreeNode node)
	{
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals(KISType.typ) ||
				 s.equals(EquipmentType.typ))
				return new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.PANEL,
				ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
				ObjectResourceCatalogActionModel.SAVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.PROPS_BUTTON,
				ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);
			else if(s.equals(LinkType.typ) ||
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
			if(s.equals(MapProtoElement.typ))
				return MapProtoElement.class;
			if(s.equals(EquipmentType.typ))
				return EquipmentType.class;
			if(s.equals(LinkType.typ))
				return LinkType.class;
			if(s.equals(CableLinkType.typ))
				return CableLinkType.class;
			if(s.equals(PortType.typ))
				return PortType.class;
			if(s.equals(CablePortType.typ))
				return CablePortType.class;
			if(s.equals(KISType.typ))
				return KISType.class;
			if(s.equals(TransmissionPathType.typ))
				return TransmissionPathType.class;
			if(s.equals(AccessPortType.typ))
				return AccessPortType.class;
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
				vec.add(new ObjectResourceTreeNode("configure", LangModelConfig.String("label_configuration"), true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				vec.add(new ObjectResourceTreeNode (MapProtoGroup.typ, "Компоненты сети", true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
			}
			else if(s.equals("configure"))
			{
				vec.add(new ObjectResourceTreeNode("netdirectory", LangModelConfig.String("menuNetDirText"), true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				vec.add(new ObjectResourceTreeNode("jdirectory", LangModelConfig.String("menuJDirText"), true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
			}
			else if(s.equals("netdirectory"))
			{
				vec.add(new ObjectResourceTreeNode(EquipmentType.typ, LangModelConfig.String("menuNetDirEquipmentText"), true));
				vec.add(new ObjectResourceTreeNode(LinkType.typ, LangModelConfig.String("menuNetDirLinkText"), true));
				vec.add(new ObjectResourceTreeNode(CableLinkType.typ, LangModelConfig.String("menuNetDirCableText"), true));
				vec.add(new ObjectResourceTreeNode(PortType.typ, LangModelConfig.String("menuNetDirPortText"), true));
				vec.add(new ObjectResourceTreeNode(CablePortType.typ, LangModelConfig.String("menuNetDirCablePortText"), true));
			}
			else if(s.equals("jdirectory"))
			{
				vec.add(new ObjectResourceTreeNode(KISType.typ, LangModelConfig.String("menuJDirKISText"), true));
				vec.add(new ObjectResourceTreeNode(AccessPortType.typ, LangModelConfig.String("menuJDirAccessPointText"), true));
				vec.add(new ObjectResourceTreeNode(TransmissionPathType.typ, LangModelConfig.String("menuJDirPathText"), true));
			}
			else if(s.equals(EquipmentType.typ))
			{
				if (Pool.getHash(EquipmentType.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(EquipmentType.typ));

					for (Enumeration en = dSet.elements(); en.hasMoreElements();)
						System.out.println(((EquipmentType)en.nextElement()).getId());

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
			}
			else if(s.equals(LinkType.typ))
			{
				if (Pool.getHash(LinkType.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(LinkType.typ));

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = LinkType.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						LinkType l = (LinkType)enum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(l, l.getName(), true, true);
						vec.add(n);
					}
				}
			}
			else if(s.equals(CableLinkType.typ))
			{
				if (Pool.getHash(CableLinkType.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(CableLinkType.typ));

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = CableLinkType.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						CableLinkType l = (CableLinkType)enum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(l, l.getName(), true, true);
						vec.add(n);
					}
				}
			}
			else if(s.equals(PortType.typ))
			{
				if (Pool.getHash(PortType.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(PortType.typ));

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = PortType.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						PortType pt = (PortType)enum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(pt, pt.getName(), true, true);
						vec.add(n);
					}
				}
			}
			else if(s.equals(CablePortType.typ))
			{
				if (Pool.getHash(CablePortType.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(CablePortType.typ));

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = CablePortType.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						CablePortType cpT = (CablePortType)enum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(cpT, cpT.getName(), true, true);
						vec.add(n);
					}
				}
			}
			else if(s.equals(KISType.typ))
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
			}
			else if(s.equals(TransmissionPathType.typ))
			{
				if (Pool.getHash(TransmissionPathType.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(TransmissionPathType.typ));

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = TransmissionPathType.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						TransmissionPathType tp = (TransmissionPathType)enum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(tp, tp.getName(), true, true);
						vec.add(n);
					}
				}
			}
			else if(s.equals(AccessPortType.typ))
			{
				if (Pool.getHash(AccessPortType.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(AccessPortType.typ));

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = AccessPortType.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						AccessPortType apt = (AccessPortType)enum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(apt, apt.getName(), true, true);
						vec.add(n);
					}
				}
			}

			else if (s.equals(MapProtoGroup.typ))
			{
				Hashtable map_groups = Pool.getHash(MapProtoGroup.typ);
				if (map_groups != null)
					for (Enumeration enum = map_groups.elements(); enum.hasMoreElements();)
					{
						MapProtoGroup map_group = (MapProtoGroup)enum.nextElement();
						if (map_group.parent_id == null || map_group.parent_id.equals(""))
							vec.add(new ObjectResourceTreeNode(map_group, map_group.getName(), true,
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
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
					vec.add(new ObjectResourceTreeNode(proto, proto.getName(), true, true));
				}
			}
		}
		return vec;
	}
}
