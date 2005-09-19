/*-
 * $Id: FullSchemeTreeModel.java,v 1.2 2005/09/19 13:10:29 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.util.Collection;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.configuration.ui.ConfigurationTreeModel;
import com.syrus.AMFICOM.logic.AbstractChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;

public class FullSchemeTreeModel extends AbstractChildrenFactory {

	ApplicationContext aContext;
	PopulatableIconedNode root;
	
	public FullSchemeTreeModel(ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	public void populate(Item node) {
		Collection<Object> contents = super.getChildObjects(node);
		
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(SchemeResourceKeys.ROOT)) {
				createRootItems(node, contents);
			}
		}
	}
	
	public Item getRoot() {
		if (this.root == null) {
			this.root = new PopulatableIconedNode(this, SchemeResourceKeys.ROOT, LangModelScheme.getString(SchemeResourceKeys.ROOT),
					UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG));
		}
		return this.root;
	}
	
	private void createRootItems(Item node, Collection contents) {
		if (!contents.contains(ConfigurationTreeModel.getRootObject())) {
			ConfigurationTreeModel configurationTreeModel = new ConfigurationTreeModel(this.aContext);
			node.addChild(configurationTreeModel.getRoot());
		}
		if (!contents.contains(SchemeTreeModel.getRootObject())) {
			SchemeTreeModel schemeTreeModel = new SchemeTreeModel(this.aContext);
			node.addChild(schemeTreeModel.getRoot());
		}
		if (!contents.contains(ProtoGroupTreeModel.getRootObject())) {
			ProtoGroupTreeModel 	protoTreeModel = new ProtoGroupTreeModel(this.aContext);
			node.addChild(protoTreeModel.getRoot());
		}
	}
}
