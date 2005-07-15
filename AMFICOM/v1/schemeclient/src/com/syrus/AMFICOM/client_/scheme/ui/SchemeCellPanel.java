/*-
 * $Id: SchemeCellPanel.java,v 1.2 2005/07/15 13:07:57 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.SchemeCellContainer;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/07/15 13:07:57 $
 * @module schemeclient_v1
 */

public class SchemeCellPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected SchemeCellContainer schemeCellContainer;
	
	UgoTabbedPane pane;
	JButton commitButton = new JButton();
		
	protected SchemeCellPanel() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jbInit() throws Exception {
		pane = new UgoTabbedPane() {
			protected JComponent createToolBar() {
				JComponent toolBar = super.createToolBar();
				
				commitButton.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_COMMIT));
				commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
				commitButton.setFocusPainted(false);
				commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
				commitButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						commitChanges();
					}
				});
				toolBar.add(commitButton);
				return toolBar;
			}
		};
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
	 * @param or SchemeCellContainer
	 * @see com.syrus.AMFICOM.client.UI.StorableObjectEditor#setObject(java.lang.Object)
	 */
	public void setObject(Object or) {
		this.schemeCellContainer = (SchemeCellContainer)or;
		GraphActions.clearGraph(pane.getGraph());
		if (this.schemeCellContainer != null) {
			pane.openSchemeCellContainer(schemeCellContainer, false);
		}
	}

	/**
	 * @return SchemeCellContainer
	 * @see com.syrus.AMFICOM.client.UI.StorableObjectEditor#getObject()
	 */
	public Object getObject() {
		return schemeCellContainer;
	}

	/**
	 * @see com.syrus.AMFICOM.client.UI.StorableObjectEditor#commitChanges()
	 */
	public void commitChanges() {
		SchemeImageResource image = schemeCellContainer.getUgoCell();
		if (image == null) {
			try {
				image = SchemeObjectsFactory.createSchemeImageResource();
			} catch (ApplicationException e) {
				Log.errorException(e);
				return;
			}
			schemeCellContainer.setUgoCell(image);
		}
		image.setData((List)pane.getGraph().getArchiveableState());
	}
}
