package com.syrus.AMFICOM.Client.Schematics.UI;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;

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
			if (!parent_group.getSchemeProtoElements().isEmpty())
				return SchemeProtoGroup.class;
		}
		else if (node.getObject() instanceof SchemeProtoGroup)
			return SchemeProtoElement.class;
		return SchemeProtoGroup.class;
	}

	public ObjectResourceController getNodeChildController(ObjectResourceTreeNode node)
	{
		if (node.getObject() instanceof SchemeProtoGroup)
		{
			SchemeProtoGroup parent_group = (SchemeProtoGroup)node.getObject();
			if (!parent_group.getSchemeProtoElements().isEmpty())
				return null;
			/**
			 * @todo write SchemeProtoGroupController
			 */
//				return SchemeProtoGroupController.getInstance();
		}
		else if (node.getObject() instanceof SchemeProtoGroup)
			return null;
			/**
			 * @todo write SchemeProtoElementController
			 */
//			return SchemeProtoElementController.getInstance();
		return null;
	}


	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList();
		try {
			if (node.getObject()instanceof String) {
				String s = (String)node.getObject();

			if (s.equals("root")) {
				Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
						getAccessIdentifier().domain_id);
				LinkedIdsCondition condition = new LinkedIdsCondition(domain_id, ObjectEntities.SCHEMEPROTOGROUP_CODE);
				Collection groups = StorableObjectPool.getStorableObjectsByCondition(condition, true);

				for (Iterator it = groups.iterator(); it.hasNext(); )
				{
					SchemeProtoGroup group = (SchemeProtoGroup)it.next();
					if (group.getParentSchemeProtoGroup() == null)
					{
						ImageIcon icon;
						if (group.getSymbol() == null)
							icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));
						else
							icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(
									group.getSymbol().getImage()).getScaledInstance(16, 16, Image.SCALE_SMOOTH));
						vec.add(new ObjectResourceTreeNode(group, group.getName(), true, icon,
								group.getSchemeProtoGroups().isEmpty()));
					}
				}
			}
		}
		else if(node.getObject() instanceof SchemeProtoGroup)
		{
			SchemeProtoGroup parent_group = (SchemeProtoGroup)node.getObject();
			for (final Iterator schemeProtoGroupIterator = parent_group.getSchemeProtoGroups().iterator(); schemeProtoGroupIterator.hasNext();) {
				final SchemeProtoGroup group = (SchemeProtoGroup) schemeProtoGroupIterator.next();
				ImageIcon icon;
				if (group.getSymbol() == null)
					icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));
				else
					icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(
							group.getSymbol().getImage()).getScaledInstance(16, 16, Image.SCALE_SMOOTH));
				vec.add(new ObjectResourceTreeNode(group, group.getName(), true, icon,
						group.getSchemeProtoGroups().isEmpty()));
			}
		}
	}
	catch (ApplicationException ex) {
	}

		return vec;
	}
}


