/*-
 * $Id: SchemePortGeneralPanel.java,v 1.27 2005/10/30 14:49:20 bass Exp $
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.ColorChooserComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.PortTypeWrapper;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.measurement.MeasurementPort;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.AMFICOM.measurement.MeasurementPortTypeWrapper;
import com.syrus.AMFICOM.measurement.MeasurementPortWrapper;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.27 $, $Date: 2005/10/30 14:49:20 $
 * @module schemeclient
 */

public class SchemePortGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	protected Set<SchemePort> schemePorts;
	protected SchemePort schemePort;
	protected SchemeElement parent;

	static JColorChooser tcc;
	JPanel pnPanel0 = new JPanel();
	JPanel pnGeneralPanel = new JPanel();
	JPanel pnPortPanel = new JPanel();
	JLabel lbNameLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.NAME));
	JTextField tfNameText = new JTextField();
	JButton btCommitBut = new JButton();
	JLabel lbTypeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.TYPE));
	WrapperedComboBox<PortType> cmbTypeCombo = new WrapperedComboBox<PortType>(PortTypeWrapper.getInstance(),
			StorableObjectWrapper.COLUMN_NAME,
			StorableObjectWrapper.COLUMN_ID);
	JLabel lbPortLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.INSTANCE));
	JLabel lbMarkLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.LABEL));
	JTextField tfMarkText = new JTextField();
	JLabel lbColorLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.COLOR));
	JComboBox cmbColorCombo = new ColorChooserComboBox();
	JCheckBox cbMpBox = new JCheckBox(LangModelScheme.getString(SchemeResourceKeys.MEASUREMENT_PORT));
	JLabel lbMpTypeLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.TYPE));
	JLabel lbDescrLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.DESCRIPTION));
	JTextArea taDescrArea = new JTextArea(2, 10);
	JPanel pnMpPanel = new JPanel();
	JRadioButton rbNewMPBut = new JRadioButton(LangModelScheme.getString(SchemeResourceKeys.NEW_MEASUREMENT_PORT));
	JRadioButton rbExistMPBut = new JRadioButton(LangModelScheme.getString(SchemeResourceKeys.EXISTING_MEASUREMENT_PORT));
	WrapperedComboBox<MeasurementPort> cmbExistMPCombo = new WrapperedComboBox<MeasurementPort>(MeasurementPortWrapper.getInstance(),
			StorableObjectWrapper.COLUMN_NAME,
			StorableObjectWrapper.COLUMN_ID);
	WrapperedComboBox<MeasurementPortType> cmbMpTypeCombo = new WrapperedComboBox<MeasurementPortType>(MeasurementPortTypeWrapper.getInstance(),
			StorableObjectWrapper.COLUMN_NAME,
			StorableObjectWrapper.COLUMN_ID);
	JTextField tfNewMPText = new JTextField();
	JLabel lbLocalAddressLabel = new JLabel(LangModelScheme.getString(SchemeResourceKeys.LOCAL_ADDRESS));
	JTextField tfLocalAddressText = new JTextField();

	protected SchemePortGeneralPanel(final SchemePort schemePort) {
		this();
		this.setObject(schemePort);
	}

	protected SchemePortGeneralPanel() {
		super();
		try {
			this.jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setContext(final ApplicationContext aContext) {
		this.aContext = aContext;
	}

	private void jbInit() throws Exception {
		final GridBagLayout gbPanel0 = new GridBagLayout();
		final GridBagConstraints gbcPanel0 = new GridBagConstraints();
		this.pnPanel0.setLayout(gbPanel0);

		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 8;
		gbcPanel0.gridwidth = 3;
		gbcPanel0.gridheight = 1;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 0;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(this.lbDescrLabel, gbcPanel0);
		this.pnPanel0.add(this.lbDescrLabel);

		final JScrollPane scpDescrArea = new JScrollPane(this.taDescrArea);
		gbcPanel0.gridx = 1;
		gbcPanel0.gridy = 9;
		gbcPanel0.gridwidth = 9;
		gbcPanel0.gridheight = 2;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 1;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(scpDescrArea, gbcPanel0);
		this.pnPanel0.add(scpDescrArea);

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
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 2;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.lbTypeLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbTypeLabel);

		gbcGeneralPanel.gridx = 2;
		gbcGeneralPanel.gridy = 1;
		gbcGeneralPanel.gridwidth = 8;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.cmbTypeCombo, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.cmbTypeCombo);

		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 2;
		gbcGeneralPanel.gridwidth = 9;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 0;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.lbPortLabel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.lbPortLabel);

		this.pnPortPanel.setBorder(BorderFactory.createTitledBorder(""));
		final GridBagLayout gbPortPanel = new GridBagLayout();
		final GridBagConstraints gbcPortPanel = new GridBagConstraints();
		this.pnPortPanel.setLayout(gbPortPanel);

		gbcPortPanel.gridx = 0;
		gbcPortPanel.gridy = 0;
		gbcPortPanel.gridwidth = 2;
		gbcPortPanel.gridheight = 1;
		gbcPortPanel.fill = GridBagConstraints.BOTH;
		gbcPortPanel.weightx = 0;
		gbcPortPanel.weighty = 0;
		gbcPortPanel.anchor = GridBagConstraints.NORTH;
		gbPortPanel.setConstraints(this.lbMarkLabel, gbcPortPanel);
		this.pnPortPanel.add(this.lbMarkLabel);

		gbcPortPanel.gridx = 2;
		gbcPortPanel.gridy = 0;
		gbcPortPanel.gridwidth = 8;
		gbcPortPanel.gridheight = 1;
		gbcPortPanel.fill = GridBagConstraints.BOTH;
		gbcPortPanel.weightx = 1;
		gbcPortPanel.weighty = 0;
		gbcPortPanel.anchor = GridBagConstraints.NORTH;
		gbPortPanel.setConstraints(this.tfMarkText, gbcPortPanel);
		this.pnPortPanel.add(this.tfMarkText);

		gbcPortPanel.gridx = 0;
		gbcPortPanel.gridy = 1;
		gbcPortPanel.gridwidth = 2;
		gbcPortPanel.gridheight = 1;
		gbcPortPanel.fill = GridBagConstraints.BOTH;
		gbcPortPanel.weightx = 0;
		gbcPortPanel.weighty = 0;
		gbcPortPanel.anchor = GridBagConstraints.NORTH;
		gbPortPanel.setConstraints(this.lbColorLabel, gbcPortPanel);
		this.pnPortPanel.add(this.lbColorLabel);

		gbcPortPanel.gridx = 2;
		gbcPortPanel.gridy = 1;
		gbcPortPanel.gridwidth = 8;
		gbcPortPanel.gridheight = 1;
		gbcPortPanel.fill = GridBagConstraints.BOTH;
		gbcPortPanel.weightx = 1;
		gbcPortPanel.weighty = 0;
		gbcPortPanel.anchor = GridBagConstraints.NORTH;
		gbPortPanel.setConstraints(this.cmbColorCombo, gbcPortPanel);
		this.pnPortPanel.add(this.cmbColorCombo);

		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 3;
		gbcGeneralPanel.gridwidth = 10;
		gbcGeneralPanel.gridheight = 2;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.pnPortPanel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.pnPortPanel);

		this.pnMpPanel.setBorder(BorderFactory.createTitledBorder(""));
		ButtonGroup rbgMpPanel = new ButtonGroup();
		GridBagLayout gbMpPanel = new GridBagLayout();
		GridBagConstraints gbcMpPanel = new GridBagConstraints();
		this.pnMpPanel.setLayout(gbMpPanel);

		rbgMpPanel.add(this.rbExistMPBut);
		gbcMpPanel.gridx = 0;
		gbcMpPanel.gridy = 0;
		gbcMpPanel.gridwidth = 2;
		gbcMpPanel.gridheight = 1;
		gbcMpPanel.fill = GridBagConstraints.BOTH;
		gbcMpPanel.weightx = 0;
		gbcMpPanel.weighty = 0;
		gbcMpPanel.anchor = GridBagConstraints.NORTH;
		gbMpPanel.setConstraints(this.rbExistMPBut, gbcMpPanel);
		this.pnMpPanel.add(this.rbExistMPBut);

		rbgMpPanel.add(this.rbNewMPBut);
		gbcMpPanel.gridx = 0;
		gbcMpPanel.gridy = 1;
		gbcMpPanel.gridwidth = 2;
		gbcMpPanel.gridheight = 1;
		gbcMpPanel.fill = GridBagConstraints.BOTH;
		gbcMpPanel.weightx = 0;
		gbcMpPanel.weighty = 0;
		gbcMpPanel.anchor = GridBagConstraints.NORTH;
		gbMpPanel.setConstraints(this.rbNewMPBut, gbcMpPanel);
		this.pnMpPanel.add(this.rbNewMPBut);

		gbcMpPanel.gridx = 2;
		gbcMpPanel.gridy = 0;
		gbcMpPanel.gridwidth = 8;
		gbcMpPanel.gridheight = 1;
		gbcMpPanel.fill = GridBagConstraints.BOTH;
		gbcMpPanel.weightx = 1;
		gbcMpPanel.weighty = 0;
		gbcMpPanel.anchor = GridBagConstraints.NORTH;
		gbMpPanel.setConstraints(this.cmbExistMPCombo, gbcMpPanel);
		this.pnMpPanel.add(this.cmbExistMPCombo);

		gbcMpPanel.gridx = 2;
		gbcMpPanel.gridy = 1;
		gbcMpPanel.gridwidth = 8;
		gbcMpPanel.gridheight = 1;
		gbcMpPanel.fill = GridBagConstraints.BOTH;
		gbcMpPanel.weightx = 1;
		gbcMpPanel.weighty = 0;
		gbcMpPanel.anchor = GridBagConstraints.NORTH;
		gbMpPanel.setConstraints(this.tfNewMPText, gbcMpPanel);
		this.pnMpPanel.add(this.tfNewMPText);

		gbcMpPanel.gridx = 0;
		gbcMpPanel.gridy = 2;
		gbcMpPanel.gridwidth = 2;
		gbcMpPanel.gridheight = 1;
		gbcMpPanel.fill = GridBagConstraints.BOTH;
		gbcMpPanel.weightx = 0;
		gbcMpPanel.weighty = 0;
		gbcMpPanel.anchor = GridBagConstraints.NORTH;
		gbMpPanel.setConstraints(this.lbMpTypeLabel, gbcMpPanel);
		this.pnMpPanel.add(this.lbMpTypeLabel);

		gbcMpPanel.gridx = 2;
		gbcMpPanel.gridy = 2;
		gbcMpPanel.gridwidth = 8;
		gbcMpPanel.gridheight = 1;
		gbcMpPanel.fill = GridBagConstraints.BOTH;
		gbcMpPanel.weightx = 1;
		gbcMpPanel.weighty = 0;
		gbcMpPanel.anchor = GridBagConstraints.NORTH;
		gbMpPanel.setConstraints(this.cmbMpTypeCombo, gbcMpPanel);
		this.pnMpPanel.add(this.cmbMpTypeCombo);
		
		gbcMpPanel.gridx = 0;
		gbcMpPanel.gridy = 3;
		gbcMpPanel.gridwidth = 2;
		gbcMpPanel.gridheight = 1;
		gbcMpPanel.fill = GridBagConstraints.BOTH;
		gbcMpPanel.weightx = 0;
		gbcMpPanel.weighty = 0;
		gbcMpPanel.anchor = GridBagConstraints.NORTH;
		gbMpPanel.setConstraints(this.lbLocalAddressLabel, gbcMpPanel);
		this.pnMpPanel.add(this.lbLocalAddressLabel);

		gbcMpPanel.gridx = 2;
		gbcMpPanel.gridy = 3;
		gbcMpPanel.gridwidth = 8;
		gbcMpPanel.gridheight = 1;
		gbcMpPanel.fill = GridBagConstraints.BOTH;
		gbcMpPanel.weightx = 1;
		gbcMpPanel.weighty = 0;
		gbcMpPanel.anchor = GridBagConstraints.NORTH;
		gbMpPanel.setConstraints(this.tfLocalAddressText, gbcMpPanel);
		this.pnMpPanel.add(this.tfLocalAddressText);
		
		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 6;
		gbcGeneralPanel.gridwidth = 10;
		gbcGeneralPanel.gridheight = 2;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.pnMpPanel, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.pnMpPanel);

		gbcGeneralPanel.gridx = 0;
		gbcGeneralPanel.gridy = 5;
		gbcGeneralPanel.gridwidth = 9;
		gbcGeneralPanel.gridheight = 1;
		gbcGeneralPanel.fill = GridBagConstraints.BOTH;
		gbcGeneralPanel.weightx = 1;
		gbcGeneralPanel.weighty = 0;
		gbcGeneralPanel.anchor = GridBagConstraints.NORTH;
		gbGeneralPanel.setConstraints(this.cbMpBox, gbcGeneralPanel);
		this.pnGeneralPanel.add(this.cbMpBox);

		gbcPanel0.gridx = 0;
		gbcPanel0.gridy = 0;
		gbcPanel0.gridwidth = 10;
		gbcPanel0.gridheight = 8;
		gbcPanel0.fill = GridBagConstraints.BOTH;
		gbcPanel0.weightx = 1;
		gbcPanel0.weighty = 0;
		gbcPanel0.anchor = GridBagConstraints.NORTH;
		gbPanel0.setConstraints(this.pnGeneralPanel, gbcPanel0);
		this.pnPanel0.add(this.pnGeneralPanel);

		this.rbExistMPBut.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent e) {
				SchemePortGeneralPanel.this.radioButton_stateChanged();
			}
		});

		this.rbNewMPBut.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent e) {
				SchemePortGeneralPanel.this.radioButton_stateChanged();
			}
		});

		this.cbMpBox.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent e) {
				SchemePortGeneralPanel.this.mpBox_stateChanged();
			}
		});
		this.cmbExistMPCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				SchemePortGeneralPanel.this.mpCombo_itemChanged(e);
			}
		});
		
		this.pnGeneralPanel.setBorder(BorderFactory.createTitledBorder(SchemeResourceKeys.EMPTY));
		this.taDescrArea.setPreferredSize(SchemeResourceKeys.DIMENSION_TEXTAREA);

		super.addToUndoableListener(this.tfNameText);
		super.addToUndoableListener(this.cmbTypeCombo);
		super.addToUndoableListener(this.tfMarkText);
		super.addToUndoableListener(this.cmbColorCombo);
		super.addToUndoableListener(this.cbMpBox);
		super.addToUndoableListener(this.cmbMpTypeCombo);
		super.addToUndoableListener(this.taDescrArea);

		this.btCommitBut.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_COMMIT));
		this.btCommitBut.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.btCommitBut.setFocusPainted(false);
		this.btCommitBut.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.btCommitBut.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				SchemePortGeneralPanel.this.commitChanges();
			}
		});
	}

	void setPortEnabled(final boolean b) {
		this.lbPortLabel.setVisible(b);
		this.pnPortPanel.setVisible(b);

		if (b && this.parent != null && this.parent.getKis() != null) {
			this.setMPTypeEnabled(true);
		} else {
			this.setMPTypeEnabled(false);
		}
	}

	void setMPTypeEnabled(final boolean b) {
		this.cbMpBox.setVisible(b);
		if (!b) {
			this.pnMpPanel.setVisible(false);
		}
	}

	void mpBox_stateChanged() {
		this.pnMpPanel.setVisible(this.cbMpBox.isSelected());
	}
	
	void mpCombo_itemChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			MeasurementPort mp =  (MeasurementPort)e.getItem();
			
			LinkedIdsCondition condition = new LinkedIdsCondition(mp.getId(), ObjectEntities.MONITOREDELEMENT_CODE);
			try {
				Set<MonitoredElement> mes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
				if (!mes.isEmpty()) {
					this.tfLocalAddressText.setText(mes.iterator().next().getLocalAddress());
				} else {
					this.tfLocalAddressText.setText("");
				}
			} catch (ApplicationException e1) {
				Log.errorMessage(e1);
			}
		}
	}

	void radioButton_stateChanged() {
		final boolean b = this.rbExistMPBut.isSelected();
		this.cmbExistMPCombo.setEnabled(b);
		this.tfNewMPText.setEnabled(!b);
	}

	public JComponent getGUI() {
		return this.pnPanel0;
	}

	public void setObject(final Object or) {
		boolean multiplePorts = (or instanceof Set);
		this.schemePorts = multiplePorts ? (Set<SchemePort>)or : Collections.singleton((SchemePort)or);
		this.schemePort = multiplePorts ? ((Set<SchemePort>)or).iterator().next() : (SchemePort)or;
		
		MeasurementPort mPort = null;
		Port port = null;
		this.cmbTypeCombo.removeAllItems();
		this.cmbMpTypeCombo.removeAllItems();
		this.cmbExistMPCombo.removeAllItems();
		this.rbExistMPBut.setEnabled(false);
		this.rbNewMPBut.doClick();

		if (this.schemePort != null) {
			try {
				if (!multiplePorts) {
					this.parent = this.schemePort.getParentSchemeDevice().getParentSchemeElement();
					port = this.schemePort.getPort();
					mPort = this.schemePort.getMeasurementPort();
					KIS kis = null;
					if (this.parent != null) {
						kis = this.parent.getKis();
					}
					
					if (kis != null) {
						final Set<MeasurementPort> mPorts = kis.getMeasurementPorts(false);
						if (!mPorts.isEmpty()) {
							this.rbExistMPBut.setEnabled(true);
							this.rbExistMPBut.doClick();
							this.cmbExistMPCombo.addElements(mPorts);
							
							final MeasurementPort mp = this.schemePort.getMeasurementPort();
							if (mp != null) {
								this.cmbExistMPCombo.setSelectedItem(mp);
							}
						}
					}
					
					this.tfNameText.setText(this.schemePort.getName());
					this.taDescrArea.setText(this.schemePort.getDescription());
				} else {
					this.tfNameText.setText("...");
					this.taDescrArea.setText("");
				}
			} catch (IllegalStateException e1) {
				Log.debugMessage(this.getClass().getName() + ": SchemeDevice has no parent SchemeElement yet", Level.FINEST); //$NON-NLS-1$
				this.parent = null;
			}
			final TypicalCondition condition1 = new TypicalCondition(PortTypeKind._PORT_KIND_SIMPLE,
					0,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PORT_TYPE_CODE,
					PortTypeWrapper.COLUMN_KIND);
			try {
				final Set<PortType> portTypes = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
				this.cmbTypeCombo.addElements(portTypes);
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
			final EquivalentCondition condition2 = new EquivalentCondition(ObjectEntities.MEASUREMENTPORT_TYPE_CODE);
			try {
				final Set<MeasurementPortType> measurementPortTypes = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
				this.cmbMpTypeCombo.addElements(measurementPortTypes);
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
			this.cmbTypeCombo.setSelectedItem(this.schemePort.getPortType());
		}
		if (port != null) {
			this.setPortEnabled(true);
			// TODO add mark and color fields to Port
			// markText.setText(port.getMark());
			// Color color = new Color(port.getColor());
			// if (!isConatainsColor(color))
			// colorCombo.addItem(color);
			// colorCombo.setSelectedItem(color);
		} else {
			this.setPortEnabled(false);
			this.tfMarkText.setText(SchemeResourceKeys.EMPTY);
		}
		if (mPort != null) {
			this.cbMpBox.setSelected(true);
			this.cmbMpTypeCombo.setSelectedItem(mPort.getType());
		} else {
			this.cbMpBox.setSelected(false);
		}
	}

	public Object getObject() {
		return this.schemePort;
	}

	@Override
	public void commitChanges() {
		super.commitChanges();
		if (this.schemePorts.size() > 1) {
			PortType ptype = (PortType) this.cmbTypeCombo.getSelectedItem();
			for (SchemePort schemePort1 : this.schemePorts) {
				schemePort1.setPortType(ptype);
			}
		} else if (this.schemePort != null && MiscUtil.validName(this.tfNameText.getText())) {
			this.schemePort.setName(this.tfNameText.getText());
			this.schemePort.setDescription(this.taDescrArea.getText());
			this.schemePort.setPortType((PortType) this.cmbTypeCombo.getSelectedItem());

			final Port port = this.schemePort.getPort();
			if (port != null) {
				port.setDescription(this.schemePort.getDescription());
				// TODO add mark and color fields to Port
				// port.setMark(markText.getText());
				// port.setColor(((Color) colorCombo.getSelectedItem()).getRGB());
			}

			if (this.cbMpBox.isSelected()) {
				MeasurementPort mp = null;
				if (this.rbExistMPBut.isSelected()) {
					mp = (MeasurementPort) this.cmbExistMPCombo.getSelectedItem();
					this.schemePort.setMeasurementPort(mp);
				} else {
					if (this.parent != null && this.parent.getKis() != null) {
						try {
							final MeasurementPortType mpType = (MeasurementPortType) this.cmbMpTypeCombo.getSelectedItem();
							mp = SchemeObjectsFactory.createMeasurementPort(mpType, this.schemePort);
							mp.setName(this.tfNewMPText.getText());
							mp.setDescription(this.schemePort.getDescription());
							mp.setType(mpType);
							this.schemePort.setMeasurementPort(mp);
						} catch (ApplicationException e) {
							Log.errorMessage(e);
						}
					} else {
						Log.debugMessage("Parent KIS not created. Cannot create MeasurementPort", Level.FINEST); //$NON-NLS-1$
					}
				}
				
				try {
					if (mp != null) {
						// if no MonitoredElement associated - create it 
						LinkedIdsCondition condition = new LinkedIdsCondition(mp.getId(), ObjectEntities.MONITOREDELEMENT_CODE);
						Set<MonitoredElement> mes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
						MonitoredElement me = null;
						if (mes.isEmpty()) {
							// if exists parent Path
							LinkedIdsCondition condition1 = new LinkedIdsCondition(this.parent.getId(), ObjectEntities.PATHELEMENT_CODE);
							Set<PathElement> pes = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
							if (!pes.isEmpty()) {
								SchemePath path = pes.iterator().next().getParentPathOwner();
								// create MonitoredElement
								me = SchemeObjectsFactory.createMonitoredElement(path, mp);
								StorableObjectPool.flush(me.getId(), LoginManager.getUserId(), false);
							}
						} else {
							me = mes.iterator().next();
						}
						if (me != null) {
							me.setLocalAddress(this.tfLocalAddressText.getText());
						}
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			} else {
				this.schemePort.setMeasurementPort(null);
			}
			this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, this.schemePort.getId(), SchemeEvent.UPDATE_OBJECT));
		}
	}
}
