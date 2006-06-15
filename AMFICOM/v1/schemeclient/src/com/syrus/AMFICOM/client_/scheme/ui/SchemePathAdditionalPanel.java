/*-
 * $Id: SchemePathAdditionalPanel.java,v 1.1 2006/06/15 06:31:22 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

public class SchemePathAdditionalPanel extends DefaultStorableObjectEditor<SchemePath> {
	JPanel pnPanel0 = new JPanel();
	JLabel lbPesLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.PATH_ELEMENTS));
	JList lsPesList = new JList();

	protected SchemePath schemePath;
	
	protected SchemePathAdditionalPanel() {
		final GridBagLayout gbPanel0 = new GridBagLayout();
		final GridBagConstraints gbcPanel0 = new GridBagConstraints();
		this.pnPanel0.setLayout(gbPanel0);
		
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 7;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(this.lbPesLabel, gbcPanel0);
		this.pnPanel0.add(this.lbPesLabel);
		
		JScrollPane scpPesList = new JScrollPane(this.lsPesList);
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 1;
		gbcPanel0.gridwidth = 9;
		gbcPanel0.gridheight = 3;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(scpPesList, gbcPanel0);
		this.pnPanel0.add(scpPesList);
	}
	
	public JComponent getGUI() {
		return this.pnPanel0;
	}

	public void setObject(SchemePath object) {
		this.schemePath = object;
		
		boolean updateOnly = false;
		if (object.equals(this.schemePath)) {
			updateOnly = true;
		}
		
		SortedSet<PathElement> pathElements;
		try {
			pathElements = this.schemePath.getPathMembers();
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			pathElements = new TreeSet<PathElement>();
		}
		if (!updateOnly || this.lsPesList.getModel().getSize() != pathElements.size()) {
			final Vector<String> peNames = new Vector<String>();
			for (final PathElement pe : pathElements) {
				peNames.add(pe.getName());
			}
			this.lsPesList.setListData(peNames);
		}
	}

	public SchemePath getObject() {
		return this.schemePath;
	}
}
