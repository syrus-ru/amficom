package com.syrus.AMFICOM.Client.Schematics.UI;

import java.util.*;
import java.util.List;

import java.awt.*;
import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.administration.*;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.client_.general.ui_.tree.*;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.*;

public class SchemeProtoGroupsTreeModel extends ObjectResourceTreeModel
{
	ApplicationContext aContext;

	public SchemeProtoGroupsTreeModel(ApplicationContext aContext)
	{
		this.aContext = aContext;
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
			if (parent_group.schemeProtoElements().length != 0)
				return SchemeProtoGroup.class;
		}
		else if (node.getObject() instanceof SchemeProtoGroup)
			return SchemeProtoElement.class;
		return SchemeProtoGroup.class;
	}

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList();
		try {
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
					getAccessIdentifier().domain_id);
			Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
					domain_id, true);
			DomainCondition condition = new DomainCondition(domain, ObjectEntities.LINKTYPE_ENTITY_CODE);
			List groups = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

			if (node.getObject()instanceof String) {
				String s = (String)node.getObject();

			if (s.equals("root")) {
				for (Iterator it = groups.iterator(); it.hasNext(); )
				{
					SchemeProtoGroup group = (SchemeProtoGroup)it.next();
					if (group.parent() == null)
					{
						ImageIcon icon;
						if (group.symbol() == null)
							icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));
						else
							icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(
									group.symbolImpl().getImage()).getScaledInstance(16, 16, Image.SCALE_SMOOTH));
						vec.add(new ObjectResourceTreeNode(group, group.name(), true, icon,
								group.schemeProtoGroups().length == 0));
					}
				}
			}
		}
		else if(node.getObject() instanceof SchemeProtoGroup)
		{
			SchemeProtoGroup parent_group = (SchemeProtoGroup)node.getObject();
			for (int i = 0; i < parent_group.schemeProtoGroups().length; i++)
			{
				SchemeProtoGroup group = parent_group.schemeProtoGroups()[i];
				ImageIcon icon;
				if (group.symbol() == null)
					icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));
				else
					icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(
							group.symbolImpl().getImage()).getScaledInstance(16, 16, Image.SCALE_SMOOTH));
				vec.add(new ObjectResourceTreeNode(group, group.name(), true, icon,
						group.schemeProtoGroups().length == 0));
			}
		}
	}
	catch (ApplicationException ex) {
	}

		return vec;
	}
}


