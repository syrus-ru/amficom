package com.syrus.AMFICOM.Client.Schematics.UI;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceDomainFilter;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ImageCatalogue;
import com.syrus.AMFICOM.Client.Resource.ImageResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceSorter;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.MapProtoGroup;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
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
		return new ObjectResourceTreeNode ("root", "Элементы схем", true,
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

	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if (s.equals("root"))
				return String.class;
			else if (s.equals("elements"))
				return MapProtoGroup.class;
			else if (s.equals("scheme"))
				return String.class;
			else
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
		{
			return ProtoElement.class;
		}
		else if (node.getObject() instanceof Scheme)
		{
			return SchemeElement.class;
		}
		else if (node.getObject() instanceof SchemeElement)
		{
			return SchemeElement.class;
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
				vec.add(new ObjectResourceTreeNode ("elements", "Компоненты сети", true,
																			 new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				ObjectResourceTreeNode sch = new ObjectResourceTreeNode ("scheme", "Схемы", true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif")));
				vec.add(sch);
				registerSearchableNode(Scheme.typ, sch);
			}
			else if (s.equals("elements"))
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
			else if (s.equals("scheme"))
			{
				Hashtable ht = new Hashtable();
				if (Pool.getHash(Scheme.typ) != null)
				{
					for (Enumeration en = Pool.getHash(Scheme.typ).elements(); en.hasMoreElements();)
					{
						Scheme sch = (Scheme)en.nextElement();
						ht.put(sch.scheme_type, sch.scheme_type);
					}

					for (Enumeration en = ht.elements(); en.hasMoreElements();)
					{
						String type = (String)en.nextElement();
						vec.add(new ObjectResourceTreeNode (type, LangModelSchematics.String(type), true,
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
					}
				}
			}
			else
			{
				DataSet dSet = new DataSet(Pool.getHash(Scheme.typ));

				ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
				dSet = filter.filter(dSet);
				ObjectResourceSorter sorter = Scheme.getDefaultSorter();
				sorter.setDataSet(dSet);
				dSet = sorter.default_sort();

				for (Enumeration enum = dSet.elements(); enum.hasMoreElements();)
				{
					Scheme scheme = (Scheme)enum.nextElement();
					if (scheme.scheme_type.equals(s))
						vec.add(new ObjectResourceTreeNode(scheme, scheme.getName(), true,
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif"))));

				}
			}
		}
		else if(node.getObject() instanceof MapProtoGroup)
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
		else if(node.getObject() instanceof Scheme)
		{
			Scheme scheme = (Scheme)node.getObject();
			for (Enumeration enum = scheme.elements.elements(); enum.hasMoreElements();)
			{
				SchemeElement element = (SchemeElement)enum.nextElement();
				if (element.scheme_id.equals(""))
					vec.add(new ObjectResourceTreeNode(element, element.getName(), true, true));
				else
					vec.add(new ObjectResourceTreeNode(element, element.getName(), true,
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")), false));
			}
		}
		else if(node.getObject() instanceof SchemeElement)
		{
			SchemeElement schel = (SchemeElement)node.getObject();
			if (!schel.scheme_id.equals(""))
			{
				Scheme scheme = (Scheme)Pool.get(Scheme.typ, schel.scheme_id);
				for (Enumeration enum = scheme.elements.elements(); enum.hasMoreElements();)
				{
					SchemeElement element = (SchemeElement)enum.nextElement();
					if (element.scheme_id.equals(""))
						vec.add(new ObjectResourceTreeNode(element, element.getName(), true, true));
					else
						vec.add(new ObjectResourceTreeNode(element, element.getName(), true,
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")), false));
				}
			}
		}

		return vec;
	}
}
