package com.syrus.AMFICOM.Client.General.UI;

import javax.swing.tree.DefaultMutableTreeNode;

import java.util.Vector;
import java.util.Hashtable;
import java.awt.*;
import javax.swing.*;

public abstract class ObjectResourceTreeModel
{
	public abstract ObjectResourceTreeNode getRoot();
	public abstract Vector getChildNodes(ObjectResourceTreeNode node);
	public abstract Class getNodeChildClass(ObjectResourceTreeNode node);

//	public abstract ImageIcon getNodeIcon(ObjectResourceTreeNode node);
	public abstract Color getNodeTextColor(ObjectResourceTreeNode node);

	public abstract void nodeAfterSelected(ObjectResourceTreeNode node);
	public abstract void nodeBeforeExpanded(ObjectResourceTreeNode node);

	public ObjectResourceCatalogActionModel getNodeActionModel(ObjectResourceTreeNode node)
	{
		return new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.NO_PANEL,
				ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
				ObjectResourceCatalogActionModel.NO_SAVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_PROPS_BUTTON,
				ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);
	}

	Hashtable ht = new Hashtable();
	public void registerSearchableNode(Object criteria, ObjectResourceTreeNode tn)
	{
		Vector vec = (Vector )ht.get(criteria);
		if(vec == null)
			vec = new Vector();
		vec.add(tn);
		ht.put(criteria, vec);
	}

	public Vector getSearchableNodes(Object criteria)
	{
		Vector vec = (Vector )ht.get(criteria);
		if(vec == null)
			vec = new Vector();
		return vec;
	}
}
