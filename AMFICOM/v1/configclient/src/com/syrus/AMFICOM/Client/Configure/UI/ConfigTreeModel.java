package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import java.awt.Color;
import java.util.Map;
import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Object.Domain;

public class ConfigTreeModel extends ObjectResourceTreeModel
{
	DataSourceInterface dsi;

	public ConfigTreeModel(DataSourceInterface dsi)
	{
		this.dsi = dsi;
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode("root", LangModelConfig.getString("label_configuration"), true);
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
				new ObjectDataSourceImage(dsi).GetObjects();
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
			if(	s.equals(Domain.typ))
				return new ObjectResourceCatalogActionModel(
						ObjectResourceCatalogActionModel.PANEL,
						ObjectResourceCatalogActionModel.ADD_BUTTON,
						ObjectResourceCatalogActionModel.SAVE_BUTTON,
						ObjectResourceCatalogActionModel.REMOVE_BUTTON,
						ObjectResourceCatalogActionModel.PROPS_BUTTON,
						ObjectResourceCatalogActionModel.CANCEL_BUTTON);
			else
			if(s.equals(Equipment.typ))
				return new ObjectResourceCatalogActionModel(
						ObjectResourceCatalogActionModel.PANEL,
						ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
						ObjectResourceCatalogActionModel.SAVE_BUTTON,
						ObjectResourceCatalogActionModel.REMOVE_BUTTON,
						ObjectResourceCatalogActionModel.PROPS_BUTTON,
						ObjectResourceCatalogActionModel.CANCEL_BUTTON);
			else
			if(s.equals(Link.typ))
				return new ObjectResourceCatalogActionModel(
						ObjectResourceCatalogActionModel.PANEL,
						ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
						ObjectResourceCatalogActionModel.SAVE_BUTTON,
						ObjectResourceCatalogActionModel.REMOVE_BUTTON,
						ObjectResourceCatalogActionModel.PROPS_BUTTON,
						ObjectResourceCatalogActionModel.CANCEL_BUTTON);
			else
			if(s.equals(CableLink.typ))
				return new ObjectResourceCatalogActionModel(
						ObjectResourceCatalogActionModel.PANEL,
						ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
						ObjectResourceCatalogActionModel.SAVE_BUTTON,
						ObjectResourceCatalogActionModel.REMOVE_BUTTON,
						ObjectResourceCatalogActionModel.PROPS_BUTTON,
						ObjectResourceCatalogActionModel.CANCEL_BUTTON);
			else
			if(s.equals(KIS.typ))
				return new ObjectResourceCatalogActionModel(
						ObjectResourceCatalogActionModel.PANEL,
						ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
						ObjectResourceCatalogActionModel.SAVE_BUTTON,
						ObjectResourceCatalogActionModel.REMOVE_BUTTON,
						ObjectResourceCatalogActionModel.PROPS_BUTTON,
						ObjectResourceCatalogActionModel.CANCEL_BUTTON);
			else
			if(s.equals(TransmissionPath.typ))
				return new ObjectResourceCatalogActionModel(
						ObjectResourceCatalogActionModel.PANEL,
						ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
						ObjectResourceCatalogActionModel.SAVE_BUTTON,
						ObjectResourceCatalogActionModel.REMOVE_BUTTON,
						ObjectResourceCatalogActionModel.PROPS_BUTTON,
						ObjectResourceCatalogActionModel.CANCEL_BUTTON);
			else
			if(s.equals(AccessPort.typ))
				return new ObjectResourceCatalogActionModel(
						ObjectResourceCatalogActionModel.PANEL,
						ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
						ObjectResourceCatalogActionModel.SAVE_BUTTON,
						ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
						ObjectResourceCatalogActionModel.PROPS_BUTTON,
						ObjectResourceCatalogActionModel.CANCEL_BUTTON);
			else
			if(s.equals(Port.typ))
				return new ObjectResourceCatalogActionModel(
						ObjectResourceCatalogActionModel.PANEL,
						ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
						ObjectResourceCatalogActionModel.SAVE_BUTTON,
						ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
						ObjectResourceCatalogActionModel.PROPS_BUTTON,
						ObjectResourceCatalogActionModel.CANCEL_BUTTON);
			else
			if(s.equals(CablePort.typ))
				return new ObjectResourceCatalogActionModel(
						ObjectResourceCatalogActionModel.PANEL,
						ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
						ObjectResourceCatalogActionModel.SAVE_BUTTON,
						ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
						ObjectResourceCatalogActionModel.PROPS_BUTTON,
						ObjectResourceCatalogActionModel.CANCEL_BUTTON);
		}
		else
		if(node.getObject() instanceof Domain)
			return new ObjectResourceCatalogActionModel(
					ObjectResourceCatalogActionModel.PANEL,
					ObjectResourceCatalogActionModel.ADD_BUTTON,
					ObjectResourceCatalogActionModel.SAVE_BUTTON,
					ObjectResourceCatalogActionModel.REMOVE_BUTTON,
					ObjectResourceCatalogActionModel.PROPS_BUTTON,
					ObjectResourceCatalogActionModel.CANCEL_BUTTON);

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
			if(s.equals(Domain.typ))
				return Domain.class;
			else
			if(s.equals(Equipment.typ))
				return Equipment.class;
			else
			if(s.equals(Link.typ))
				return Link.class;
			else
			if(s.equals(CableLink.typ))
				return CableLink.class;
			else
			if(s.equals(Port.typ))
				return Port.class;
			else
			if(s.equals(CablePort.typ))
				return CablePort.class;
			else
			if(s.equals(KIS.typ))
				return KIS.class;
			else
			if(s.equals(TransmissionPath.typ))
				return TransmissionPath.class;
			else
			if(s.equals(EquipmentType.typ))
				return EquipmentType.class;
			else
			if(s.equals(LinkType.typ))
				return LinkType.class;
			else
			if(s.equals(CableLinkType.typ))
				return CableLinkType.class;
			else
			if(s.equals(PortType.typ))
				return PortType.class;
			else
			if(s.equals(CablePortType.typ))
				return CablePortType.class;
			else
			if(s.equals(KISType.typ))
				return KISType.class;
			else
			if(s.equals(TransmissionPathType.typ))
				return TransmissionPathType.class;
			else
			if(s.equals(AccessPortType.typ))
				return AccessPortType.class;
		}
		else
		{
			if(node.getObject() instanceof Domain)
				return Domain.class;
		}
		return null;
	}

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList();
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals("root"))
			{
				ObjectResourceTreeNode ortn;
				ortn = new ObjectResourceTreeNode("domain", LangModelConfig.getString("menuObjectDomainText"), true);
				vec.add(ortn);
				registerSearchableNode("domain", ortn);
				vec.add(new ObjectResourceTreeNode("netdirectory", LangModelConfig.getString("menuNetDirText"), true));
				vec.add(new ObjectResourceTreeNode("netcatalogue", LangModelConfig.getString("menuNetCatText"), true));
				vec.add(new ObjectResourceTreeNode("jdirectory", LangModelConfig.getString("menuJDirText"), true));
				vec.add(new ObjectResourceTreeNode("jcatalogue", LangModelConfig.getString("menuJCatText"), true));
			}
			else
			if(s.equals("domain"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				if(parent.getObject().equals("root"))
				{
					if (Pool.getMap(Domain.typ) != null)
					{
						Map dSet = Pool.getMap(Domain.typ);

						ObjectResourceSorter sorter = Domain.getDefaultSorter();
						sorter.setDataSet(dSet);

						for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
						{
							Domain d = (Domain )it.next();
							if(	d.domain_id == null ||
								d.domain_id.equals(""))
							{
								ObjectResourceTreeNode n = new ObjectResourceTreeNode(d, d.getName(), true);
								vec.add(n);
							}
						}
					}
				}
				else
				{
					Domain d = (Domain )parent.getObject();

					Map dSet = d.domains;

					ObjectResourceSorter sorter = Domain.getDefaultSorter();
					sorter.setDataSet(dSet);

					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						Domain d2 = (Domain )it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(d2, d2.getName(), true);
						vec.add(n);
					}
				}
			}
			else
			if(s.equals("netdirectory"))
			{
				vec.add(new ObjectResourceTreeNode(EquipmentType.typ, LangModelConfig.getString("menuNetDirEquipmentText"), true));
				vec.add(new ObjectResourceTreeNode(LinkType.typ, LangModelConfig.getString("menuNetDirLinkText"), true));
				vec.add(new ObjectResourceTreeNode(CableLinkType.typ, LangModelConfig.getString("menuNetDirCableText"), true));
				vec.add(new ObjectResourceTreeNode(PortType.typ, LangModelConfig.getString("menuNetDirPortText"), true));
				vec.add(new ObjectResourceTreeNode(CablePortType.typ, LangModelConfig.getString("menuNetDirCablePortText"), true));
			}
			else
			if(s.equals("netcatalogue"))
			{
				ObjectResourceTreeNode ortn;
				ortn = new ObjectResourceTreeNode(Equipment.typ, LangModelConfig.getString("menuNetCatEquipmentText"), true);
				vec.add(ortn);
				registerSearchableNode(Equipment.typ, ortn);
				ortn = new ObjectResourceTreeNode(Link.typ, LangModelConfig.getString("menuNetCatLinkText"), true);
				vec.add(ortn);
				registerSearchableNode(Link.typ, ortn);
				ortn = new ObjectResourceTreeNode(CableLink.typ, LangModelConfig.getString("menuNetCatCableText"), true);
				vec.add(ortn);
				registerSearchableNode(CableLink.typ, ortn);
			}
			else
			if(s.equals("jdirectory"))
			{
				vec.add(new ObjectResourceTreeNode(KISType.typ, LangModelConfig.getString("menuJDirKISText"), true));
				vec.add(new ObjectResourceTreeNode(AccessPortType.typ, LangModelConfig.getString("menuJDirAccessPointText"), true));
				vec.add(new ObjectResourceTreeNode(TransmissionPathType.typ, LangModelConfig.getString("menuJDirPathText"), true));
			}
			else
			if(s.equals("jcatalogue"))
			{
				ObjectResourceTreeNode ortn;
				ortn = new ObjectResourceTreeNode(KIS.typ, LangModelConfig.getString("menuJCatKISText"), true);
				vec.add(ortn);
				registerSearchableNode(KIS.typ, ortn);
				ortn = new ObjectResourceTreeNode(TransmissionPath.typ, LangModelConfig.getString("menuJCatPathText"), true);
				vec.add(ortn);
				registerSearchableNode(TransmissionPath.typ, ortn);
			}
			else
			if(s.equals(Equipment.typ))
			{
				if (Pool.getMap(Equipment.typ) != null)
				{
					Map dSet = Pool.getMap(Equipment.typ);

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = Equipment.getDefaultSorter();
					sorter.setDataSet(dSet);

					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						Equipment eq = (Equipment)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(eq, eq.getName(), true);
						vec.add(n);
					}
				}
			}
			else
			if(s.equals(Link.typ))
			{
				if (Pool.getMap(Link.typ) != null)
				{
					Map dSet = Pool.getMap(Link.typ);

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);

					ObjectResourceSorter sorter = Link.getDefaultSorter();
					sorter.setDataSet(dSet);

					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						Link l = (Link )it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(l, l.getName(), true, true);
						vec.add(n);
					}
				}
			}
			else
			if(s.equals(CableLink.typ))
			{
				if (Pool.getMap(CableLink.typ) != null)
				{
					Map dSet = Pool.getMap(CableLink.typ);

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = CableLink.getDefaultSorter();
					sorter.setDataSet(dSet);

					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						CableLink l = (CableLink )it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(l, l.getName(), true, true);
						vec.add(n);
					}
				}
			}
			else
			if(s.equals(Port.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				Equipment eq = (Equipment )parent.getObject();

				Collection dSet = eq.ports;

				ObjectResourceSorter sorter = Port.getDefaultSorter();
				sorter.setDataSet(dSet);

				for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
				{
					Port p = (Port )it.next();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(p, p.getName(), true, true);
					vec.add(n);
				}
			}
			else
			if(s.equals(CablePort.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				Equipment eq = (Equipment )parent.getObject();

				Collection dSet = eq.cports;

				ObjectResourceSorter sorter = CablePort.getDefaultSorter();
				sorter.setDataSet(dSet);

				for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
				{
					CablePort p = (CablePort )it.next();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(p, p.getName(), true, true);
					vec.add(n);
				}
			}
			else
			if(s.equals(KIS.typ))
			{
				if (Pool.getMap(KIS.typ) != null)
				{
					Map dSet = Pool.getMap(KIS.typ);

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = KIS.getDefaultSorter();
					sorter.setDataSet(dSet);

					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						KIS k = (KIS )it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(k, k.getName(), true);
						vec.add(n);
					}
				}
			}
			else
			if(s.equals(TransmissionPath.typ))
			{
				if (Pool.getMap(TransmissionPath.typ) != null)
				{
					Map dSet = Pool.getMap(TransmissionPath.typ);

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = TransmissionPath.getDefaultSorter();
					sorter.setDataSet(dSet);

					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						TransmissionPath tp = (TransmissionPath )it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(tp, tp.getName(), true, true);
						vec.add(n);
					}
				}
			}
		else
		if(s.equals(EquipmentType.typ))
		{
			if (Pool.getMap(EquipmentType.typ) != null)
			{
			 Map dSet = Pool.getMap(EquipmentType.typ);

			 ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
			 dSet = filter.filter(dSet);
			 ObjectResourceSorter sorter = EquipmentType.getDefaultSorter();
			 sorter.setDataSet(dSet);

			 for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
			 {
				EquipmentType eq = (EquipmentType)it.next();
				ObjectResourceTreeNode n = new ObjectResourceTreeNode(eq, eq.getName(), true);
				vec.add(n);
			 }
			}
		}
		else
		if(s.equals(LinkType.typ))
		{
			if (Pool.getMap(LinkType.typ) != null)
			{
			 Map dSet = Pool.getMap(LinkType.typ);

			 ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
			 dSet = filter.filter(dSet);
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
		else
		if(s.equals(CableLinkType.typ))
		{
			if (Pool.getMap(CableLinkType.typ) != null)
			{
			 Map dSet = Pool.getMap(CableLinkType.typ);

			 ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
			 dSet = filter.filter(dSet);
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
		else
		if(s.equals(PortType.typ))
		{
			if (Pool.getMap(PortType.typ) != null)
			{
			 Map dSet = Pool.getMap(PortType.typ);

			 ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
			 dSet = filter.filter(dSet);
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
		else
		if(s.equals(CablePortType.typ))
		{
			if (Pool.getMap(CablePortType.typ) != null)
			{
			 Map dSet = Pool.getMap(CablePortType.typ);

			 ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
			 dSet = filter.filter(dSet);
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
		else
		if(s.equals(KISType.typ))
		{
			if (Pool.getMap(EquipmentType.typ) != null)
			{
			 Map dSet = Pool.getMap(EquipmentType.typ);

			 ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
			 dSet = filter.filter(dSet);
			 ObjectResourceSorter sorter = KISType.getDefaultSorter();
			 sorter.setDataSet(dSet);

			 for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
			 {
				EquipmentType k = (EquipmentType)it.next();
				if (!k.eq_class.equals("tester"))
					continue;
				ObjectResourceTreeNode n = new ObjectResourceTreeNode(k, k.getName(), true);
				vec.add(n);
			 }
			}
		}
		else
		if(s.equals(TransmissionPathType.typ))
		{
			if (Pool.getMap(TransmissionPathType.typ) != null)
			{
			 Map dSet = Pool.getMap(TransmissionPathType.typ);

			 ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
			 dSet = filter.filter(dSet);
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
		else
		if(s.equals(AccessPortType.typ))
		{
			if (Pool.getMap(AccessPortType.typ) != null)
			{
			 Map dSet = Pool.getMap(AccessPortType.typ);

			 ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
			 dSet = filter.filter(dSet);
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

		}
		else
		{
			if(node.getObject() instanceof Domain)
			{
//				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
//				if(parent.getObject().equals("domain"))
//				{
//					ObjectResourceTreeNode n = new ObjectResourceTreeNode("domain", "Дочерние домены", true);
//					vec.add(n);

					Domain d = (Domain )node.getObject();
					d.updateLocalFromTransferable();

					Map dSet = d.domains;

					ObjectResourceSorter sorter = Domain.getDefaultSorter();
					sorter.setDataSet(dSet);

					for(Iterator it = sorter.default_sort().iterator(); it.hasNext();)
					{
						Domain d2 = (Domain )it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(d2, d2.getName(), true);
						vec.add(n);
					}
//				}
			}
			else
			if(node.getObject() instanceof Equipment)
			{
				vec.add(new ObjectResourceTreeNode(Port.typ, LangModelConfig.getString("menuNetCatPortText"), true));
				vec.add(new ObjectResourceTreeNode(CablePort.typ, LangModelConfig.getString("menuNetCatCablePortText"), true));
			}
		}

		return vec;
	}
}

