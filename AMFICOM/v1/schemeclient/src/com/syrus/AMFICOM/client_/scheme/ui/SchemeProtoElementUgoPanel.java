/*-
 * $Id: SchemeProtoElementUgoPanel.java,v 1.2 2005/05/26 08:42:42 stas Exp $
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
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/05/26 08:42:42 $
 * @module schemeclient_v1
 */

public class SchemeProtoElementUgoPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected SchemeProtoElement schemeProtoElement;
	
	UgoTabbedPane pane;
		
	SchemeProtoElementUgoPanel() {
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
	 * @see com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor#getGUI()
	 */
	public JComponent getGUI() {
		return pane;
	}

	/**
	 * @param or SchemeProtoElement
	 * @see com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor#setObject(java.lang.Object)
	 */
	public void setObject(Object or) {
		this.schemeProtoElement = (SchemeProtoElement)or;
		GraphActions.clearGraph(pane.getGraph());
		if (this.schemeProtoElement != null) {
			pane.openSchemeProtoElement(schemeProtoElement);
		}
	}

	/**
	 * @return SchemeProtoElement
	 * @see com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor#getObject()
	 */
	public Object getObject() {
		return schemeProtoElement;
	}

	/**
	 * @see com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor#commitChanges()
	 */
	public void commitChanges() {
		SchemeImageResource image = schemeProtoElement.getUgoCell();
		if (image == null) {
			try {
				image = SchemeImageResource.createInstance(LoginManager.getUserId());
			} catch (ApplicationException e) {
				Log.errorException(e);
				return;
			}
			schemeProtoElement.setUgoCell(image);
		}
		image.setData((List)pane.getGraph().getArchiveableState());
	}
}

