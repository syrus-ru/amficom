package com.syrus.AMFICOM.Client.Schematics.UI;

import java.util.*;
import java.util.List;

import java.awt.*;
import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeProtoGroup;
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
/*			if(s.equals(KISType.typ) ||
				 s.equals(EquipmentType.typ))
				return new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.PANEL,
				ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
				ObjectResourceCatalogActionModel.SAVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.PROPS_BUTTON,
				ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);
			else */
			if(s.equals(LinkType.typ) ||
							s.equals(CableLinkType.typ) ||
							s.equals(PortType.typ) ||
							s.equals(CablePortType.typ) ||
							s.equals(MeasurementPortType.typ) ||
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
			if(s.equals(SchemeProtoGroup.typ))
				return SchemeProtoGroup.class;
			if(s.equals(SchemeProtoGroup.typ))
				return SchemeProtoGroup.class;
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
			if(s.equals(MeasurementPortType.typ))
				return MeasurementPortType.class;
		}
		else if (node.getObject() instanceof SchemeProtoGroup)
		{
			if (!((SchemeProtoGroup)node.getObject()).groupIds.isEmpty())
				return SchemeProtoGroup.class;
			else
				return ProtoElement.class;
		}
		else if (node.getObject() instanceof SchemeProtoGroup)
			return ProtoElement.class;
		return null;
	}

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList();
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			ObjectResource os;
			if(s.equals("root"))
			{
				vec.add(new ObjectResourceTreeNode("configure", LangModelConfig.getString("label_configuration"), true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				vec.add(new ObjectResourceTreeNode (SchemeProtoGroup.typ, "Компоненты сети", true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
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
				vec.add(new ObjectResourceTreeNode(MeasurementPortType.typ, LangModelConfig.getString("menuJDirAccessPointText"), true));
				vec.add(new ObjectResourceTreeNode(TransmissionPathType.typ, LangModelConfig.getString("menuJDirPathText"), true));
			}
/*			else if(s.equals(EquipmentType.typ))
			{
				if (Pool.getMap(EquipmentType.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getMap(EquipmentType.typ));

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
				if (Pool.getMap(LinkType.typ) != null)
				{
					Map dSet = Pool.getMap(LinkType.typ);

//					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
//					dSet = filter.filter(dSet);
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
				if (Pool.getMap(CableLinkType.typ) != null)
				{
					Map dSet = Pool.getMap(CableLinkType.typ);

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
				if (Pool.getMap(PortType.typ) != null)
				{
					Map dSet = Pool.getMap(PortType.typ);

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
				if (Pool.getMap(CablePortType.typ) != null)
				{
					Map dSet = Pool.getMap(CablePortType.typ);

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
	/*		else if(s.equals(KISType.typ))
			{
				if (Pool.getMap(EquipmentType.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getMap(EquipmentType.typ));

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
				if (Pool.getMap(TransmissionPathType.typ) != null)
				{
					Map dSet = Pool.getMap(TransmissionPathType.typ);

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
			else if(s.equals(MeasurementPortType.typ))
			{
				if (Pool.getMap(MeasurementPortType.typ) != null)
				{
					Map dSet = Pool.getMap(MeasurementPortType.typ);

//					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
//					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = MeasurementPortType.getDefaultSorter();
					sorter.setDataSet(dSet);

					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						MeasurementPortType apt = (MeasurementPortType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(apt, apt.getName(), true, true);
						vec.add(n);
					}
				}
			}

			else if (s.equals(SchemeProtoGroup.typ))
			{
				Map map_groups = Pool.getMap(SchemeProtoGroup.typ);
				if (map_groups != null)
					for (Iterator it = map_groups.values().iterator(); it.hasNext();)
					{
						SchemeProtoGroup map_group = (SchemeProtoGroup)it.next();
						if (map_group.parentId.equals(""))
						{
							ImageIcon icon;
							if (map_group.getImageID().equals(""))
								icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));
							else
								icon = new ImageIcon(ImageCatalogue.get(map_group.getImageID()).getImage()
										.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
							vec.add(new ObjectResourceTreeNode(map_group, map_group.getName(), true, icon,
									map_group.groupIds.isEmpty() && map_group.getProtoIds().isEmpty()));
						}
					}
			}
		}
		else
		{
			if(node.getObject() instanceof SchemeProtoGroup)
			{
				SchemeProtoGroup parent_group = (SchemeProtoGroup)node.getObject();
				Iterator it = parent_group.groupIds.iterator();
				for (int i = 0; i < parent_group.groupIds.size(); i++)
				{
					SchemeProtoGroup map_group = (SchemeProtoGroup)Pool.get(SchemeProtoGroup.typ, (String)it.next());
					ImageIcon icon;
					if (map_group.getImageID().equals(""))
						icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));
					else
						icon = new ImageIcon(ImageCatalogue.get(map_group.getImageID()).getImage()
								.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
					vec.add(new ObjectResourceTreeNode(map_group, map_group.getName(), true, icon,
							map_group.groupIds.isEmpty() && map_group.getProtoIds().isEmpty()));
				}
				if (vec.isEmpty())
				{
					it = parent_group.getProtoIds().iterator();
					for (int i = 0; i < parent_group.getProtoIds().size(); i++)
					{
						ProtoElement proto = (ProtoElement)Pool.get(ProtoElement.typ, (String)it.next());
						proto.scheme_proto_group = parent_group;
						vec.add(new ObjectResourceTreeNode(proto, proto.getName().length() == 0 ? "Без названия" : proto.getName(), true, true));
					}
				}
			}
		}
		return vec;
	}
}
