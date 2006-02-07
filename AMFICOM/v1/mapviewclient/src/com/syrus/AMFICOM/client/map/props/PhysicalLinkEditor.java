/*-
 * $$Id: PhysicalLinkEditor.java,v 1.37 2006/02/07 15:27:11 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.UI.ColorChooserComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.LineThicknessComboBox;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.controllers.PhysicalLinkController;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.client.resource.MiscUtil;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.37 $, $Date: 2006/02/07 15:27:11 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class PhysicalLinkEditor extends DefaultStorableObjectEditor {
	PhysicalLink link;

	private JPanel jPanel = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel typeLabel = new JLabel();
	private WrapperedComboBox typeComboBox = null;
	private JLabel topologicalLengthLabel = new JLabel();
	private JTextField topologicalLengthTextField = new JTextField();
	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	private JLabel startLabel = new JLabel();
	private WrapperedComboBox startComboBox = null;
	private JLabel endLabel = new JLabel();
	private WrapperedComboBox endComboBox = null;

	private JPanel streetPanel = new JPanel();
	private JLabel cityLabel = new JLabel();
	private JLabel streetLabel = new JLabel();
	private JLabel buildingLabel = new JLabel();
	private JTextField cityTextField = new JTextField();
	private JTextField streetTextField = new JTextField();
	private JTextField buildingTextField = new JTextField();
	private JLabel addressLabel = new JLabel();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();

	private JLabel colorLabel = new JLabel();
	private ColorChooserComboBox colorComboBox = new ColorChooserComboBox(); 
	
	private JLabel thicknessLabel = new JLabel();
	private LineThicknessComboBox thicknessComboBox = new LineThicknessComboBox(); 
	
	private JLabel collectorLabel = new JLabel();
	private WrapperedComboBox collectorComboBox = null; 

	private JLabel styleLabel = new JLabel();

	private JButton commitButton = new JButton();

	private NetMapViewer netMapViewer;
//	private LineStyleComboBox styleComboBox = new LineStyleComboBox(); 

	public PhysicalLinkEditor() {
		jbInit();
	}

	public void setNetMapViewer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
	}

	private void jbInit() {
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.typeComboBox = new WrapperedComboBox(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);
		this.startComboBox = new WrapperedComboBox(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);
		this.endComboBox = new WrapperedComboBox(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);
		this.collectorComboBox = new WrapperedComboBox(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
//		this.jPanel.setName(I18N.getString(MapEditorResourceKeys.TITLE_PROPERTIES));

		this.nameLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_NAME));
		this.typeLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_TYPE));
		this.startLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_START_NODE));
		this.endLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_END_NODE));
		this.collectorLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_COLLECTOR));
		this.topologicalLengthLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_TOPOLOGICAL_LENGTH));
		this.addressLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_ADDRESS));
		this.descLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_DESCRIPTION));
		this.cityLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_CITY_KURZ));
		this.streetLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_STREET_KURZ));
		this.buildingLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_BUILDING_KURZ));
		this.colorLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_COLOR));
		this.thicknessLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_THICKNESS));
		this.styleLabel.setText(I18N.getString(MapEditorResourceKeys.LABEL_STYLE));

		this.commitButton.setToolTipText(I18N.getString(ResourceKeys.I18N_COMMIT));
		this.commitButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_NULL));
		this.commitButton.setFocusPainted(false);
		this.commitButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_COMMIT));
		this.commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commitChanges();
			}
		});

		GridBagConstraints constraints = new GridBagConstraints();

		this.streetPanel.setLayout(this.gridBagLayout3);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.8;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.streetPanel.add(this.streetTextField, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 15, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.streetPanel.add(this.buildingLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.2;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.streetPanel.add(this.buildingTextField, constraints);


		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.nameLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.nameTextField, constraints);

		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.commitButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.typeLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.typeComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.startLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.startComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.endLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 3;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.endComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.collectorLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 4;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.collectorComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.topologicalLengthLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 5;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.topologicalLengthTextField, constraints);

		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.addressLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.cityLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 6;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.cityTextField, constraints);

		constraints.gridx = 1;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.streetLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 7;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.streetPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 9;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.colorLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 9;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.colorComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 10;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.thicknessLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 10;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.thicknessComboBox, constraints);

//		constraints.gridx = 0;
//		constraints.gridy = 11;
//		constraints.gridwidth = 1;
//		constraints.gridheight = 1;
//		constraints.weightx = 0.0;
//		constraints.weighty = 0.0;
//		constraints.anchor = GridBagConstraints.WEST;
//		constraints.fill = GridBagConstraints.NONE;
//		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
//		constraints.ipadx = 0;
//		constraints.ipady = 0;
//		this.jPanel.add(this.styleLabel, constraints);

//		constraints.gridx = 2;
//		constraints.gridy = 11;
//		constraints.gridwidth = 2;
//		constraints.gridheight = 1;
//		constraints.weightx = 1.0;
//		constraints.weighty = 0.0;
//		constraints.anchor = GridBagConstraints.CENTER;
//		constraints.fill = GridBagConstraints.HORIZONTAL;
//		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
//		constraints.ipadx = 0;
//		constraints.ipady = 0;
//		this.jPanel.add(this.styleComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 12;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.descLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 13;
		constraints.gridwidth = 4;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(new JScrollPane(this.descTextArea), constraints);

		super.addToUndoableListener(this.nameTextField);
		super.addToUndoableListener(this.typeComboBox);
		super.addToUndoableListener(this.cityTextField);
		super.addToUndoableListener(this.streetTextField);
		super.addToUndoableListener(this.buildingTextField);
		super.addToUndoableListener(this.descTextArea);
		super.addToUndoableListener(this.colorComboBox);
		super.addToUndoableListener(this.thicknessComboBox);
//		super.addToUndoableListener(this.styleComboBox);
		
		this.startComboBox.setEnabled(false);
		this.endComboBox.setEnabled(false);
		this.collectorComboBox.setEnabled(false);
		this.topologicalLengthTextField.setEnabled(false);
	}

	public Object getObject() {
		return this.link;
	}

	public void setObject(Object object) {
		this.link = (PhysicalLink )object;

		this.typeComboBox.removeAllItems();
		this.startComboBox.removeAllItems();
		this.endComboBox.removeAllItems();
		this.collectorComboBox.removeAllItems();

		if(this.link == null) {
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText(""); //$NON-NLS-1$
			this.topologicalLengthTextField.setText(""); //$NON-NLS-1$
			this.typeComboBox.setEnabled(false);
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText(""); //$NON-NLS-1$
			this.collectorLabel.setVisible(false);
			this.collectorComboBox.setVisible(false);

			this.cityTextField.setText(""); //$NON-NLS-1$
			this.streetTextField.setText(""); //$NON-NLS-1$
			this.buildingTextField.setText(""); //$NON-NLS-1$

			this.colorComboBox.setEnabled(false);
			this.thicknessComboBox.setEnabled(false);
//			this.styleComboBox.setEnabled(false);
		}
		else {
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.link.getName());
			
			this.topologicalLengthTextField.setText(MapPropertiesManager.getDistanceFormat().format(this.link.getLengthLt()));

			if(this.link.getType().getSort().value() == PhysicalLinkTypeSort._INDOOR) {
				this.typeComboBox.setEnabled(false);
				this.typeComboBox.addElements(Collections.singleton(this.link.getType()));
				this.typeComboBox.setSelectedItem(this.link.getType());
			}
			else {
				long d = System.currentTimeMillis();
				Collection types = LinkTypeController.getTopologicalLinkTypes(this.netMapViewer.getLogicalNetLayer().getMapView().getMap());
				long f = System.currentTimeMillis();
				Log.debugMessage("SiteNodeEditor::LinkTypeController.getTopologicalLinkTypes() -------- " + (f - d) + " ms ---------", Level.FINE); //$NON-NLS-1$ //$NON-NLS-2$
				
				this.typeComboBox.setEnabled(true);
				this.typeComboBox.addElements(types);
				this.typeComboBox.setSelectedItem(this.link.getType());
			}

			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.link.getDescription());

			this.startComboBox.addItem(this.link.getStartNode());
			this.startComboBox.setSelectedItem(this.link.getStartNode());
			this.endComboBox.addItem(this.link.getEndNode());
			this.endComboBox.setSelectedItem(this.link.getEndNode());
			
			final Collector collector = this.netMapViewer.getLogicalNetLayer().getMapView().getMap().getCollector(this.link);
			if(collector != null) {
				this.collectorLabel.setVisible(true);
				this.collectorComboBox.setVisible(true);
				this.collectorComboBox.addItem(collector);
				this.collectorComboBox.setSelectedItem(collector);
			}
			else {
				this.collectorLabel.setVisible(false);
				this.collectorComboBox.setVisible(false);
			}

			this.cityTextField.setText(this.link.getCity());
			this.streetTextField.setText(this.link.getStreet());
			this.buildingTextField.setText(this.link.getBuilding());

			PhysicalLinkController physicalLinkController = 
				(PhysicalLinkController )
					this.netMapViewer.getLogicalNetLayer()
						.getMapViewController().getController(this.link);
			
			this.colorComboBox.setSelectedItem(physicalLinkController.getColor(this.link));
			this.thicknessComboBox.setSelectedValue(physicalLinkController.getLineSize(this.link));
//			this.styleComboBox.setSelectedItem(physicalLinkController.getStyle(this.link);
		}
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	@Override
	public void commitChanges() {
		PhysicalLinkController.getInstance().clearCachedElement(this.link);
		
		final ApplicationContext aContext = this.netMapViewer.getLogicalNetLayer().getContext();
		if(!aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_OPERATION_PROHIBITED_IN_MODULE)));
			return;
		}
		if(!MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_EDIT_TOPOLOGICAL_SCHEME)) {
			aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION)));
			return;
		}
		String name = this.nameTextField.getText();
		if(MiscUtil.validName(name)) {
			try 
			{
				if(!name.equals(this.link.getName()))
					this.link.setName(name);
				this.link.setDescription(this.descTextArea.getText());
	
				this.link.setCity(this.cityTextField.getText());
				this.link.setStreet(this.streetTextField.getText());
				this.link.setBuilding(this.buildingTextField.getText());
	
				PhysicalLinkController physicalLinkController = 
					(PhysicalLinkController )
						this.netMapViewer.getLogicalNetLayer()
							.getMapViewController().getController(this.link);
				Color color = (Color)this.colorComboBox.getSelectedItem();
				if(! color.equals(physicalLinkController.getColor(this.link)))
					physicalLinkController.setColor(this.link, color);
				int size = this.thicknessComboBox.getSelectedValue();
				if(size != physicalLinkController.getLineSize(this.link))
					physicalLinkController.setLineSize(this.link, size);
	//			String style = (String)this.styleComboBox.getSelectedItem();
	//			if(! style.equals(physicalLinkController.getStyle(this.link)))
	//				physicalLinkController.setStyle(this.link, style);
				this.link.setType((PhysicalLinkType )this.typeComboBox.getSelectedItem());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		super.commitChanges();
	}
}
