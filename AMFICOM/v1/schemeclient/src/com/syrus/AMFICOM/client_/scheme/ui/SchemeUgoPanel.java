/*-
 * $Id: SchemeUgoPanel.java,v 1.1 2005/05/26 07:40:52 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.util.List;

import javax.swing.JComponent;

import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/05/26 07:40:52 $
 * @module schemeclient_v1
 */

public class SchemeUgoPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected Scheme scheme;
	
	UgoTabbedPane pane;
		
	protected SchemeUgoPanel() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jbInit() throws Exception {
		pane = new UgoTabbedPane();
	}
	
	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		pane.setContext(aContext);
	}
	
	/**
	 * @return UgoTabbedPane
	 * @see com.syrus.AMFICOM.client.UI.StorableObjectEditor#getGUI()
	 */
	public JComponent getGUI() {
		return pane;
	}

	/**
	 * @param or Scheme
	 * @see com.syrus.AMFICOM.client.UI.StorableObjectEditor#setObject(java.lang.Object)
	 */
	public void setObject(Object or) {
		this.scheme = (Scheme)or;
		GraphActions.clearGraph(pane.getGraph());
		if (this.scheme != null) {
			pane.openScheme(scheme);
		}
	}

	/**
	 * @return Scheme
	 * @see com.syrus.AMFICOM.client.UI.StorableObjectEditor#getObject()
	 */
	public Object getObject() {
		return scheme;
	}

	/**
	 * @see com.syrus.AMFICOM.client.UI.StorableObjectEditor#commitChanges()
	 */
	public void commitChanges() {
		SchemeImageResource image = scheme.getUgoCell();
		if (image == null) {
			Identifier user_id = LoginManager.getUserId();
			try {
				image = new SchemeImageResource(user_id);
			} catch (ApplicationException e) {
				Log.errorException(e);
				return;
			}
			scheme.setUgoCell(image);
		}
		image.setData((List)pane.getGraph().getArchiveableState());
	}
}
