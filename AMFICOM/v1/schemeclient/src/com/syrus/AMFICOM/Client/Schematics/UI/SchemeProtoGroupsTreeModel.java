package com.syrus.AMFICOM.Client.Schematics.UI;

import java.util.*;
import java.util.List;

import java.awt.*;
import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeProtoGroup;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class SchemeProtoGroupsTreeModel extends ObjectResourceTreeModel
{
	DataSourceInterface dsi;

	public SchemeProtoGroupsTreeModel(DataSourceInterface dsi)
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
		if (node.getObject() instanceof SchemeProtoGroup)
		{
			SchemeProtoGroup parent_group = (SchemeProtoGroup)node.getObject();
			if (!parent_group.getProtoIds().isEmpty())
				return SchemeProtoGroup.class;
		}
		else if (node.getObject() instanceof SchemeProtoGroup)
			return ProtoElement.class;
		return SchemeProtoGroup.class;
	}

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList();
		Map map_groups = Pool.getMap(SchemeProtoGroup.typ);

		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			ObjectResource os;

			if(s.equals("root"))
			{
				if (map_groups != null)
					for (Iterator it = map_groups.values().iterator(); it.hasNext();)
					{
						SchemeProtoGroup map_group = (SchemeProtoGroup)it.next();
						if (map_group.parentId.length() == 0)
						{
							ImageIcon icon;
							if (map_group.getImageID().equals(""))
								icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));
							else
								icon = new ImageIcon(ImageCatalogue.get(map_group.getImageID()).getImage()
										.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
							vec.add(new ObjectResourceTreeNode(map_group, map_group.getName(), true, icon,
									map_group.groupIds.isEmpty()));
						}
					}
			}
		}
		else if(node.getObject() instanceof SchemeProtoGroup)
		{
			SchemeProtoGroup parent_group = (SchemeProtoGroup)node.getObject();
			Iterator it = parent_group.groupIds.iterator();
			for (int i = 0; i < parent_group.groupIds.size(); i++)
			{
				SchemeProtoGroup map_proto = (SchemeProtoGroup)Pool.get(SchemeProtoGroup.typ, (String)it.next());
				ImageIcon icon;
				if (map_proto.getImageID().equals(""))
					icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));
				else
					icon = new ImageIcon(ImageCatalogue.get(map_proto.getImageID()).getImage()
							.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
				vec.add(new ObjectResourceTreeNode(map_proto, map_proto.getName(), true, icon, map_proto.groupIds.isEmpty()));
			}
		}
		return vec;
	}
}


