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

import com.syrus.AMFICOM.client.UI.ColorChooserComboBox;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.LineThicknessComboBox;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.controllers.PhysicalLinkController;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MiscUtil;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.util.Log;

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

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModelGeneral.getString("Properties")); //$NON-NLS-1$

		this.nameLabel.setText(LangModelMap.getString("Name")); //$NON-NLS-1$
		this.typeLabel.setText(LangModelMap.getString("Type")); //$NON-NLS-1$
		this.startLabel.setText(LangModelMap.getString("StartNode")); //$NON-NLS-1$
		this.endLabel.setText(LangModelMap.getString("EndNode")); //$NON-NLS-1$
		this.topologicalLengthLabel.setText(LangModelMap.getString("TopologicalLength")); //$NON-NLS-1$
		this.addressLabel.setText(LangModelMap.getString("Address")); //$NON-NLS-1$
		this.descLabel.setText(LangModelMap.getString("Description")); //$NON-NLS-1$
		this.cityLabel.setText(LangModelMap.getString("CityKurz")); //$NON-NLS-1$
		this.streetLabel.setText(LangModelMap.getString("StreetKurz")); //$NON-NLS-1$
		this.buildingLabel.setText(LangModelMap.getString("BuildingKurz")); //$NON-NLS-1$
		this.addressLabel.setText(LangModelMap.getString("Address")); //$NON-NLS-1$
		this.colorLabel.setText(LangModelMap.getString("Color")); //$NON-NLS-1$
		this.thicknessLabel.setText(LangModelMap.getString("Thickness")); //$NON-NLS-1$
		this.styleLabel.setText(LangModelMap.getString("Style")); //$NON-NLS-1$

		this.commitButton.setToolTipText(LangModelGeneral.getString(ResourceKeys.I18N_COMMIT));
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
		this.jPanel.add(this.topologicalLengthLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 4;
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
		constraints.gridy = 5;
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
		constraints.gridy = 5;
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
		this.jPanel.add(this.cityTextField, constraints);

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
		this.jPanel.add(this.streetLabel, constraints);

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
		this.jPanel.add(this.streetPanel, constraints);
//		this.jPanel.add(this.streetTextField, ReusedGridBagConstraints.get(2, 6, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
//		this.jPanel.add(this.buildingLabel, ReusedGridBagConstraints.get(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
//		this.jPanel.add(this.buildingTextField, ReusedGridBagConstraints.get(2, 7, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));

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

		if(this.link == null) {
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText(""); //$NON-NLS-1$
			this.topologicalLengthTextField.setText(""); //$NON-NLS-1$
			this.typeComboBox.setEnabled(false);
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText(""); //$NON-NLS-1$

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

	public void commitChanges() {
		String name = this.nameTextField.getText();
		if(MiscUtil.validName(name))
		try 
		{
			if(!name.equals(this.link.getName()))
				this.link.setName(name);
			this.link.setType((PhysicalLinkType )this.typeComboBox.getSelectedItem());
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
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
	}
}
