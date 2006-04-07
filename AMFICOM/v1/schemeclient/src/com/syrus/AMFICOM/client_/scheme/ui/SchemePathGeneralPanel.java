/*-
 * $Id: SchemePathGeneralPanel.java,v 1.16 2006/04/05 13:32:20 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeMonitoringSolution;
import com.syrus.AMFICOM.scheme.SchemeMonitoringSolutionWrapper;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

public class SchemePathGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected SchemePath schemePath;

	JPanel pnPanel0 = new JPanel();
	JPanel pnGeneralPanel = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JLabel lbSolutionLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.SCHEME_MONITORING_SOLUTION));
	WrapperedComboBox<SchemeMonitoringSolution> cmbSolutionCombo = new WrapperedComboBox<SchemeMonitoringSolution>(SchemeMonitoringSolutionWrapper.getInstance(),
			StorableObjectWrapper.COLUMN_NAME,
			StorableObjectWrapper.COLUMN_ID);
	JButton btCommitBut = new JButton();
	JLabel lbStartLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.START_ELEMENT));
	JTextField tfStartText = new JTextField();
	JLabel lbEndLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.END_ELEMENT));
	JTextField tfEndText = new JTextField();
	JLabel lbPesLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.PATH_ELEMENTS));
	JList lsPesList = new JList();
	JLabel lbDescriptionLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescriptionArea = new JTextArea(2, 10);

	protected SchemePathGeneralPanel() {
		super();
		final GridBagLayout gbPanel0 = new GridBagLayout();
		final GridBagConstraints gbcPanel0 = new GridBagConstraints();
		this.pnPanel0.setLayout(gbPanel0);
		
		this.pnGeneralPanel.setBorder(BorderFactory.createTitledBorder(""));
		final GridBagLayout gbGeneralPanel = new GridBagLayout();
		final GridBagConstraints gbcGeneralPanel = new GridBagConstraints();
		this.pnGeneralPanel.setLayout(gbGeneralPanel);
		
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.lbNameLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbNameLabel);
		
		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 7;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.tfNameText, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.tfNameText);
		
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.lbSolutionLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbSolutionLabel);
		
		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 8;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.cmbSolutionCombo, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.cmbSolutionCombo);
		
		gbcGeneralPanel.gridx = 9;
		gbcGeneralPanel.gridy = 0;
		gbcGeneralPanel.gridwidth = 1;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.btCommitBut, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.btCommitBut);
		
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.lbStartLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbStartLabel);
		
		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 8;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.tfStartText, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.tfStartText);
		
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 3;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.lbEndLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbEndLabel);
		
		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 3;
		gbcGeneralPanel.gridwidth = 8;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.tfEndText, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.tfEndText);
		
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 4;
		gbcGeneralPanel.gridwidth = 7;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.lbPesLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbPesLabel);
		
		JScrollPane scpPesList = new JScrollPane(this.lsPesList);
		gbcGeneralPanel.gridx = 1;
		gbcGeneralPanel.gridy = 5;
		gbcGeneralPanel.gridwidth = 9;
		gbcGeneralPanel.gridheight = 3;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 1;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(scpPesList, gbcGeneralPanel);
		this.pnGeneralPanel.add(scpPesList);
		
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 10;
		gbcPanel0.gridheight = 10;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(this.pnGeneralPanel, gbcPanel0);
		this.pnPanel0.add(this.pnGeneralPanel);
		
		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 10;
		gbcPanel0.gridwidth = 8;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(this.lbDescriptionLabel, gbcPanel0);
		this.pnPanel0.add(this.lbDescriptionLabel);
		
		this.taDescriptionArea = new JTextArea(2, 10);
		JScrollPane scpDescriptionArea = new JScrollPane(this.taDescriptionArea);
		gbcPanel0.gridx = 1;
		gbcPanel0.gridy = 11;
		gbcPanel0.gridwidth = 9;
		gbcPanel0.gridheight = 3;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(scpDescriptionArea, gbcPanel0);
		this.pnPanel0.add(scpDescriptionArea);
		
		super.addToUndoableListener(this.tfNameText);
		super.addToUndoableListener(this.cmbSolutionCombo);
		
		this.btCommitBut.setToolTipText(I18N.getString(ResourceKeys.I18N_COMMIT));
		this.btCommitBut.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.btCommitBut.setFocusPainted(false);
		this.btCommitBut.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.btCommitBut.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				SchemePathGeneralPanel.this.commitChanges();
			}
		});
	}

	public void setContext(final ApplicationContext aContext) {
		this.aContext = aContext;
	}

	protected SchemePathGeneralPanel(final SchemePath schemePath) {
		this();
		this.setObject(schemePath);
	}

	public void setObject(final Object or) {
		boolean updateOnly = false;
		if (or.equals(this.schemePath)) {
			updateOnly = true;
		}
		this.schemePath = (SchemePath) or;

		if (this.schemePath != null) {
			if (!updateOnly) {
				this.cmbSolutionCombo.removeAllItems();
				final SchemeMonitoringSolution solution = this.schemePath.getParentSchemeMonitoringSolution();
				final Scheme scheme = solution.getParentScheme();
				try {
					this.cmbSolutionCombo.addElements(scheme.getSchemeMonitoringSolutions(false));
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
				this.cmbSolutionCombo.setSelectedItem(solution);
			}

			this.tfNameText.setText(this.schemePath.getName());
			this.taDescriptionArea.setText(this.schemePath.getDescription());

			SortedSet<PathElement> pathElements;
			try {
				pathElements = this.schemePath.getPathMembers();
			} catch (ApplicationException e) {
				Log.errorMessage(e);
				pathElements = new TreeSet<PathElement>();
			}
			if (!updateOnly || this.lsPesList.getModel().getSize() != pathElements.size()) {
				if (!pathElements.isEmpty()) {
					final PathElement startElement = pathElements.first();
					this.tfStartText.setText(startElement.getName());
					final PathElement endElement = pathElements.last();
					this.tfEndText.setText(endElement.getName());
				} else {
					this.tfStartText.setText(SchemeResourceKeys.EMPTY);
					this.tfEndText.setText(SchemeResourceKeys.EMPTY);
				}

				final Vector<String> peNames = new Vector<String>();
				for (final PathElement pe : pathElements) {
					peNames.add(pe.getName());
				}
				this.lsPesList.setListData(peNames);
			}
		} else {
			this.tfNameText.setText(SchemeResourceKeys.EMPTY);
			this.taDescriptionArea.setText(SchemeResourceKeys.EMPTY);
			this.tfStartText.setText(SchemeResourceKeys.EMPTY);
			this.tfEndText.setText(SchemeResourceKeys.EMPTY);
		}
	}

	public JComponent getGUI() {
		return this.pnPanel0;
	}

	public Object getObject() {
		return this.schemePath;
	}

	@Override
	public void commitChanges() {
		super.commitChanges();
		if (this.schemePath != null && MiscUtil.validName(this.tfNameText.getText())) {
			this.schemePath.setName(this.tfNameText.getText());
			this.schemePath.setDescription(this.taDescriptionArea.getText());
			
			// set name for associated MonitoredElement if any
			try {
				this.schemePath.setParentSchemeMonitoringSolution((SchemeMonitoringSolution) this.cmbSolutionCombo.getSelectedItem(), false);
				SortedSet<PathElement> pathMemebers = this.schemePath.getPathMembers();
				if (!pathMemebers.isEmpty()) {
					AbstractSchemePort startPort = pathMemebers.first().getEndAbstractSchemePort();
					if (startPort != null) {
						Identifier measurementPortId = startPort.getMeasurementPortId();
						if (!measurementPortId.isVoid()) {
							LinkedIdsCondition condition = new LinkedIdsCondition(measurementPortId, ObjectEntities.MONITOREDELEMENT_CODE);
							Set<MonitoredElement> mes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
							if (!mes.isEmpty()) {
								MonitoredElement me = mes.iterator().next();
								me.setName(this.tfNameText.getText());
								StorableObjectPool.flush(me, LoginManager.getUserId(), false);
								Set<Identifier> tpathIds = me.getMonitoredDomainMemberIds();
								if (!tpathIds.isEmpty()) {
									Set<TransmissionPath> tPaths = StorableObjectPool.getStorableObjects(tpathIds, true);
									final TransmissionPath tpath = tPaths.iterator().next();
									this.schemePath.setTransmissionPath(tpath);
									tpath.setName(this.tfNameText.getText());
									StorableObjectPool.flush(tpath, LoginManager.getUserId(), false);
								}
							}
						}
					}
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}

			this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.schemePath.getId(), SchemeEvent.UPDATE_OBJECT));
		}
	}
}
