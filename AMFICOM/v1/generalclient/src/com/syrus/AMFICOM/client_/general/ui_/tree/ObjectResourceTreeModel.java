package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.util.*;
import java.util.List;

import java.awt.Color;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceCatalogActionModel;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.client_.general.ui_.*;

public abstract class ObjectResourceTreeModel
{
	public abstract ObjectResourceTreeNode getRoot();
	public abstract List getChildNodes(ObjectResourceTreeNode node);
	public abstract Class getNodeChildClass(ObjectResourceTreeNode node);
	public abstract ObjectResourceController getNodeChildController(ObjectResourceTreeNode node);

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

	Map ht = new HashMap();
	public void registerSearchableNode(Object criteria, ObjectResourceTreeNode tn)
	{
		List vec = (List) ht.get(criteria);
		if(vec == null)
			vec = new ArrayList();

		vec.add(tn);

		ht.put(criteria, vec);
	}

	public List getSearchableNodes(Object criteria)
	{
		List vec = (List) ht.get(criteria);
		if(vec == null)
			vec = new ArrayList();

		return vec;
	}
}
