package com.syrus.AMFICOM.Client.Schematics.UI;

import java.util.*;

import java.awt.*;
import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.MapProtoGroup;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class MapProtoGroupsTreeModel extends ObjectResourceTreeModel
{
	DataSourceInterface dsi;

	public MapProtoGroupsTreeModel(DataSourceInterface dsi)
	{
		this.dsi = dsi;
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode("root", "Группы компонентов", true,
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
		if (node.getObject() instanceof MapProtoGroup)
		{
			MapProtoGroup parent_group = (MapProtoGroup)node.getObject();
			if (!parent_group.mapproto_ids.isEmpty())
				return MapProtoElement.class;
		}
		else if (node.getObject() instanceof MapProtoElement)
			return ProtoElement.class;
		return MapProtoGroup.class;
	}

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList();
		Map map_groups = Pool.getHash(MapProtoGroup.typ);

		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			ObjectResource os;

			if(s.equals("root"))
			{
				if (map_groups != null)
					for (Iterator it = map_groups.keySet().iterator(); it.hasNext();)
					{
						MapProtoGroup map_group = (MapProtoGroup)it.next();
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
			for (int i = 0; i < parent_group.mapproto_ids.size(); i++)
			{
				MapProtoElement map_proto = (MapProtoElement)Pool.get(MapProtoElement.typ, (String)parent_group.mapproto_ids.get(i));
				String image_id = ((map_proto.getImageID() == null || map_proto.getImageID().equals("")) ? "pc" : map_proto.getImageID());
				map_proto.setImageID(image_id);
				ImageResource ir = ImageCatalogue.get(image_id);

				if (ir == null)
					vec.add(new ObjectResourceTreeNode(map_proto, map_proto.getName(), true, true));
				else
					vec.add(new ObjectResourceTreeNode(map_proto, map_proto.getName(), true,
							new ImageIcon(ir.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)), true));
			}
		}
		return vec;
	}
}


