/*-
 * $Id: SchemeTreeUI.java,v 1.9 2005/08/01 07:52:28 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import javax.swing.JToolBar;

import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.logic.Item;

/**
 * @author $Author: stas $
 * @version $Revision: 1.9 $, $Date: 2005/08/01 07:52:28 $
 * @module schemeclient_v1
 */

public class SchemeTreeUI extends IconedTreeUI {
	ApplicationContext aContext;
	SchemeTreeToolBar toolBar;
	
	public SchemeTreeUI(Item rootItem, ApplicationContext aContext) {
		super(rootItem);
		new SchemeTreeSelectionListener(this, aContext);
		setContext(aContext);
	}
	
	void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	public JToolBar getToolBar() {
		if (this.toolBar == null)
			this.toolBar = new SchemeTreeToolBar();
		return this.toolBar;
	}

	public class SchemeTreeToolBar extends IconedTreeToolBar {
		private static final long serialVersionUID = 3546082449625987129L;
		
		public SchemeTreeToolBar() {
		}
	}
}
