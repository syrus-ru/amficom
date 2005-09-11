/*-
 * $Id: ObserverTreeModel.java,v 1.1 2005/09/11 17:39:24 krupenn Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.observer.ui;

import java.util.Collection;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.UI.ResultChildrenFactory;
import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeTreeModel;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.resource.LangModelObserver;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.resource.ObserverResourceKeys;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/09/11 17:39:24 $
 * @module surveyclient_v1
 */

public class ObserverTreeModel implements ChildrenFactory{
	ApplicationContext aContext;
	PopulatableIconedNode root;
	
	public ObserverTreeModel(ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	public void populate(Item node) {
		Collection contents = CommonUIUtilities.getChildObjects(node);
		
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(ObserverResourceKeys.ROOT)) {
				createRootItems(node, contents);
			}
		}
	}
	
	public static Object getRootObject() {
		return ObserverResourceKeys.ROOT;
	}

	public Item getRoot() {
		if (this.root == null) {
			this.root = new PopulatableIconedNode(this, ObserverResourceKeys.ROOT, LangModelObserver.getString(ObserverResourceKeys.ROOT),
					UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG));
		}
		return this.root;
	}
	
	private void createRootItems(Item node, Collection contents) {
		if (!contents.contains(SchemeTreeModel.getRootObject())) {
			SchemeTreeModel schemeTreeModel = new SchemeTreeModel(this.aContext);
			node.addChild(schemeTreeModel.getRoot());
		}
		if (!contents.contains(ResultChildrenFactory.getRootObject())) {
			ResultChildrenFactory resultTreeModel = new ResultChildrenFactory();
			resultTreeModel.getRoot().setIcon(UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG));
			node.addChild(resultTreeModel.getRoot());
		}
	}
}
