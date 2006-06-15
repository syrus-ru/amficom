/*-
 * $$Id: CablePathAddEditor.java,v 1.41 2006/06/15 06:40:46 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.41 $, $Date: 2006/06/15 06:40:46 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CablePathAddEditor extends DefaultStorableObjectEditor<CablePath> {
	JPanel pnPanel0 = new JPanel();
	JLabel lbPesLabel = new JLabel(I18N.getString(MapEditorResourceKeys.LABEL_CABLE_ROUTE));
	JList lsPesList = new JList();

	protected CablePath cablePath;
	
	protected CablePathAddEditor() {
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

	public void setObject(CablePath object) {
		this.cablePath = object;
		
		List<PhysicalLink> pathElements;
		try {
			pathElements = this.cablePath.getLinks();
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			pathElements = new LinkedList<PhysicalLink>();
		}
		final Vector<String> peNames = new Vector<String>();
		for (final PhysicalLink pe : pathElements) {
			peNames.add(pe.getStartNode().getName());
		}
		if (!pathElements.isEmpty()) {
			peNames.add(pathElements.listIterator(pathElements.size()).previous().getEndNode().getName());
		}
		this.lsPesList.setListData(peNames);
	}

	public CablePath getObject() {
		return this.cablePath;
	}
}
