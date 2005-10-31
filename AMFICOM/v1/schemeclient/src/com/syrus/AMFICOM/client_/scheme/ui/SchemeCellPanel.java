/*-
 * $Id: SchemeCellPanel.java,v 1.10 2005/10/31 12:30:28 bass Exp $
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
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.SchemeCellContainer;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2005/10/31 12:30:28 $
 * @module schemeclient
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
		this.pane = new UgoTabbedPane() {
			private static final long serialVersionUID = 6725936773878805596L;

			protected JComponent createToolBar() {
				JComponent toolBar1 = super.createToolBar();
				
				SchemeCellPanel.this.commitButton.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_COMMIT));
				SchemeCellPanel.this.commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
				SchemeCellPanel.this.commitButton.setFocusPainted(false);
				SchemeCellPanel.this.commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
				SchemeCellPanel.this.commitButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						commitChanges();
					}
				});
				toolBar1.add(SchemeCellPanel.this.commitButton);
				return toolBar1;
			}
		};
	}
	
	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		this.pane.setContext(aContext);
	}
	
	/**
	 * @return UgoTabbedPane
	 * @see com.syrus.AMFICOM.client.UI.StorableObjectEditor#getGUI()
	 */
	public JComponent getGUI() {
		return this.pane;
	}

	/**
	 * @param or SchemeCellContainer
	 * @see com.syrus.AMFICOM.client.UI.StorableObjectEditor#setObject(java.lang.Object)
	 */
	public void setObject(Object or) {
		this.schemeCellContainer = (SchemeCellContainer)or;
		GraphActions.clearGraph(this.pane.getGraph());
		if (this.schemeCellContainer != null) {
			SchemeActions.openSchemeImageResource(this.pane.getGraph(), this.schemeCellContainer.getUgoCell(), false);
		}
	}

	/**
	 * @return SchemeCellContainer
	 * @see com.syrus.AMFICOM.client.UI.StorableObjectEditor#getObject()
	 */
	public Object getObject() {
		return this.schemeCellContainer;
	}

	/**
	 * @see com.syrus.AMFICOM.client.UI.StorableObjectEditor#commitChanges()
	 */
	@Override
	public void commitChanges() {
		super.commitChanges();
		SchemeImageResource image = this.schemeCellContainer.getUgoCell();
		if (image == null) {
			try {
				image = SchemeObjectsFactory.createSchemeImageResource();
			} catch (ApplicationException e) {
				Log.errorMessage(e);
				return;
			}
			this.schemeCellContainer.setUgoCell(image);
		}
		image.setData((List)this.pane.getGraph().getArchiveableState());
	}
}
