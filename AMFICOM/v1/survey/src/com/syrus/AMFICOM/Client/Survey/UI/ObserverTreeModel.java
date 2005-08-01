/*-
 * $Id: ObserverTreeModel.java,v 1.1 2005/08/01 08:37:21 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Survey.UI;

import java.util.Collection;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.UI.ResultChildrenFactory;
import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeTreeModel;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.resource.LangModelSurvey;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.resource.SurveyResourceKeys;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/08/01 08:37:21 $
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
			if (s.equals(SurveyResourceKeys.ROOT)) {
				createRootItems(node, contents);
			}
		}
	}
	
	public static Object getRootObject() {
		return SurveyResourceKeys.ROOT;
	}

	public Item getRoot() {
		if (this.root == null) {
			this.root = new PopulatableIconedNode(this, SurveyResourceKeys.ROOT, LangModelSurvey.getString(SurveyResourceKeys.ROOT),
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
