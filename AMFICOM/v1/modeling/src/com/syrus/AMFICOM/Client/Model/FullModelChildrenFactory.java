/*-
 * $Id: FullModelChildrenFactory.java,v 1.2 2006/03/22 14:39:54 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Model;

import java.util.Collection;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.UI.ModelChildrenFactory;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeTreeModel;
import com.syrus.AMFICOM.logic.AbstractChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;

public class FullModelChildrenFactory extends AbstractChildrenFactory {
	private static final String	ROOT	= "tree.modeling.fullroot";
	
	ApplicationContext aContext;
	PopulatableIconedNode root;
	
	public FullModelChildrenFactory(ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	public void populate(Item node) {
		Collection contents = getChildObjects(node);
		
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(ROOT)) {
				createRootItems(node, contents);
			}
		}
	}
	
	public static Object getRootObject() {
		return ROOT;
	}

	public Item getRoot() {
		if (this.root == null) {
			this.root = new PopulatableIconedNode(this, ROOT, I18N.getString(ROOT),
					UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG));
		}
		return this.root;
	}
	
	private void createRootItems(Item node, Collection contents) {
		if (!contents.contains(SchemeTreeModel.getRootObject())) {
			SchemeTreeModel schemeTreeModel = new SchemeTreeModel(this.aContext);
			node.addChild(schemeTreeModel.getRoot());
		}
		if (!contents.contains(ModelChildrenFactory.getRootObject())) {
			ModelChildrenFactory resultTreeModel = new ModelChildrenFactory();
			resultTreeModel.getRoot().setIcon(UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG));
			node.addChild(resultTreeModel.getRoot());
		}
	}
}
