package com.syrus.AMFICOM.Client.Schematics.UI;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ImageCatalogue;
import com.syrus.AMFICOM.Client.Resource.ImageResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
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

	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		if(node.getObject() instanceof String)
		{
			return MapProtoGroup.class;
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
		return null;
	}

	public Vector getChildNodes(ObjectResourceTreeNode node)
	{
		Vector vec = new Vector();
		Hashtable map_groups = Pool.getHash(MapProtoGroup.typ);

		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			ObjectResource os;

			if(s.equals("root"))
			{
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
		return vec;
	}
}



