package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.Color;

import java.util.Vector;
import java.util.Enumeration;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceDomainFilter;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceCatalogActionModel;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.ConfigDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceSorter;

import com.syrus.AMFICOM.Client.Resource.ISM.KIS;
import com.syrus.AMFICOM.Client.Resource.ISM.AccessPort;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;

import com.syrus.AMFICOM.Client.Resource.ISMDirectory.KISType;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.AccessPortType;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.TransmissionPathType;

import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
import com.syrus.AMFICOM.Client.Resource.Network.Link;
import com.syrus.AMFICOM.Client.Resource.Network.CableLink;
import com.syrus.AMFICOM.Client.Resource.Network.Port;
import com.syrus.AMFICOM.Client.Resource.Network.CablePort;

import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.LinkType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CableLinkType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.PortType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CablePortType;

import com.syrus.AMFICOM.Client.Resource.Object.Domain;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class ConfigTreeModel extends ObjectResourceTreeModel
{
	DataSourceInterface dsi;

	public ConfigTreeModel(DataSourceInterface dsi)
	{
		this.dsi = dsi;
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode("root", LangModelConfig.String("label_configuration"), true);
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

	public Vector getChildNodes(ObjectResourceTreeNode node)
	{
		Vector vec = new Vector();
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals("root"))
			{
				ObjectResourceTreeNode ortn;
				ortn = new ObjectResourceTreeNode("domain", LangModelConfig.String("menuObjectDomainText"), true);
				vec.add(ortn);
				registerSearchableNode("domain", ortn);
				vec.add(new ObjectResourceTreeNode("netdirectory", LangModelConfig.String("menuNetDirText"), true));
				vec.add(new ObjectResourceTreeNode("netcatalogue", LangModelConfig.String("menuNetCatText"), true));
				vec.add(new ObjectResourceTreeNode("jdirectory", LangModelConfig.String("menuJDirText"), true));
				vec.add(new ObjectResourceTreeNode("jcatalogue", LangModelConfig.String("menuJCatText"), true));
			}
			else
			if(s.equals("domain"))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				if(parent.getObject().equals("root"))
				{
					if (Pool.getHash(Domain.typ) != null)
					{
						DataSet dSet = new DataSet(Pool.getHash(Domain.typ));

						ObjectResourceSorter sorter = Domain.getDefaultSorter();
						sorter.setDataSet(dSet);
						dSet = sorter.default_sort();

						Enumeration enum = dSet.elements();
						for(; enum.hasMoreElements();)
						{
							Domain d = (Domain )enum.nextElement();
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

					DataSet dSet = new DataSet(d.domains);

					ObjectResourceSorter sorter = Domain.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						Domain d2 = (Domain )enum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(d2, d2.getName(), true);
						vec.add(n);
					}
				}
			}
			else
			if(s.equals("netdirectory"))
			{
				vec.add(new ObjectResourceTreeNode(EquipmentType.typ, LangModelConfig.String("menuNetDirEquipmentText"), true));
				vec.add(new ObjectResourceTreeNode(LinkType.typ, LangModelConfig.String("menuNetDirLinkText"), true));
				vec.add(new ObjectResourceTreeNode(CableLinkType.typ, LangModelConfig.String("menuNetDirCableText"), true));
				vec.add(new ObjectResourceTreeNode(PortType.typ, LangModelConfig.String("menuNetDirPortText"), true));
				vec.add(new ObjectResourceTreeNode(CablePortType.typ, LangModelConfig.String("menuNetDirCablePortText"), true));
			}
			else
			if(s.equals("netcatalogue"))
			{
				ObjectResourceTreeNode ortn;
				ortn = new ObjectResourceTreeNode(Equipment.typ, LangModelConfig.String("menuNetCatEquipmentText"), true);
				vec.add(ortn);
				registerSearchableNode(Equipment.typ, ortn);
				ortn = new ObjectResourceTreeNode(Link.typ, LangModelConfig.String("menuNetCatLinkText"), true);
				vec.add(ortn);
				registerSearchableNode(Link.typ, ortn);
				ortn = new ObjectResourceTreeNode(CableLink.typ, LangModelConfig.String("menuNetCatCableText"), true);
				vec.add(ortn);
				registerSearchableNode(CableLink.typ, ortn);
			}
			else
			if(s.equals("jdirectory"))
			{
				vec.add(new ObjectResourceTreeNode(KISType.typ, LangModelConfig.String("menuJDirKISText"), true));
				vec.add(new ObjectResourceTreeNode(AccessPortType.typ, LangModelConfig.String("menuJDirAccessPointText"), true));
				vec.add(new ObjectResourceTreeNode(TransmissionPathType.typ, LangModelConfig.String("menuJDirPathText"), true));
			}
			else
			if(s.equals("jcatalogue"))
			{
				ObjectResourceTreeNode ortn;
				ortn = new ObjectResourceTreeNode(KIS.typ, LangModelConfig.String("menuJCatKISText"), true);
				vec.add(ortn);
				registerSearchableNode(KIS.typ, ortn);
				ortn = new ObjectResourceTreeNode(TransmissionPath.typ, LangModelConfig.String("menuJCatPathText"), true);
				vec.add(ortn);
				registerSearchableNode(TransmissionPath.typ, ortn);
			}
			else
			if(s.equals(Equipment.typ))
			{
				if (Pool.getHash(Equipment.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(Equipment.typ));

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = Equipment.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						Equipment eq = (Equipment )enum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(eq, eq.getName(), true);
						vec.add(n);
					}
				}
			}
			else
			if(s.equals(Link.typ))
			{
				if (Pool.getHash(Link.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(Link.typ));

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = Link.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						Link l = (Link )enum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(l, l.getName(), true, true);
						vec.add(n);
					}
				}
			}
			else
			if(s.equals(CableLink.typ))
			{
				if (Pool.getHash(CableLink.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(CableLink.typ));

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = CableLink.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						CableLink l = (CableLink )enum.nextElement();
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

				DataSet dSet = new DataSet(eq.ports);

				ObjectResourceSorter sorter = Port.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enum = dSet.elements();
				for(; enum.hasMoreElements();)
				{
					Port p = (Port )enum.nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(p, p.getName(), true, true);
					vec.add(n);
				}
			}
			else
			if(s.equals(CablePort.typ))
			{
				ObjectResourceTreeNode parent = (ObjectResourceTreeNode )node.getParent();
				Equipment eq = (Equipment )parent.getObject();

				DataSet dSet = new DataSet(eq.cports);

				ObjectResourceSorter sorter = CablePort.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				Enumeration enum = dSet.elements();
				for(; enum.hasMoreElements();)
				{
					CablePort p = (CablePort )enum.nextElement();
					ObjectResourceTreeNode n = new ObjectResourceTreeNode(p, p.getName(), true, true);
					vec.add(n);
				}
			}
			else
			if(s.equals(KIS.typ))
			{
				if (Pool.getHash(KIS.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(KIS.typ));

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = KIS.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						KIS k = (KIS )enum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(k, k.getName(), true);
						vec.add(n);
					}
				}
			}
			else
			if(s.equals(TransmissionPath.typ))
			{
				if (Pool.getHash(TransmissionPath.typ) != null)
				{
					DataSet dSet = new DataSet(Pool.getHash(TransmissionPath.typ));

					ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
					dSet = filter.filter(dSet);
					ObjectResourceSorter sorter = TransmissionPath.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						TransmissionPath tp = (TransmissionPath )enum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(tp, tp.getName(), true, true);
						vec.add(n);
					}
				}
			}
      else
      if(s.equals(EquipmentType.typ))
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
            ObjectResourceTreeNode n = new ObjectResourceTreeNode(eq, eq.getName(), true);
            vec.add(n);
          }
        }
      }
      else
      if(s.equals(LinkType.typ))
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
      else
      if(s.equals(CableLinkType.typ))
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
      else
      if(s.equals(PortType.typ))
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
      else
      if(s.equals(CablePortType.typ))
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
      else
      if(s.equals(KISType.typ))
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
            ObjectResourceTreeNode n = new ObjectResourceTreeNode(k, k.getName(), true);
            vec.add(n);
          }
        }
      }
      else
      if(s.equals(TransmissionPathType.typ))
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
      else
      if(s.equals(AccessPortType.typ))
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

					DataSet dSet = new DataSet(d.domains);

					ObjectResourceSorter sorter = Domain.getDefaultSorter();
					sorter.setDataSet(dSet);
					dSet = sorter.default_sort();

					Enumeration enum = dSet.elements();
					for(; enum.hasMoreElements();)
					{
						Domain d2 = (Domain )enum.nextElement();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(d2, d2.getName(), true);
						vec.add(n);
					}
//				}
			}
			else
			if(node.getObject() instanceof Equipment)
			{
				vec.add(new ObjectResourceTreeNode(Port.typ, LangModelConfig.String("menuNetCatPortText"), true));
				vec.add(new ObjectResourceTreeNode(CablePort.typ, LangModelConfig.String("menuNetCatCablePortText"), true));
			}
		}

		return vec;
	}
}

