package com.syrus.AMFICOM.client.map.props;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MiscUtil;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.map.IntDimension;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;

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

	private JLabel dimensionLabel = new JLabel();
	private JPanel dimensionPanel = new JPanel();
	private JLabel xLabel = new JLabel();
	private JTextField mTextField = new JTextField();
	private JTextField nTextField = new JTextField();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	public PhysicalLinkEditor() {
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	private void jbInit() {
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.typeComboBox = new WrapperedComboBox(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);
		this.startComboBox = new WrapperedComboBox(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);
		this.endComboBox = new WrapperedComboBox(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModelGeneral.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.typeLabel.setText(LangModelMap.getString("Type"));
		this.startLabel.setText(LangModelMap.getString("StartNode"));
		this.endLabel.setText(LangModelMap.getString("EndNode"));
		this.topologicalLengthLabel.setText(LangModelMap.getString("TopologicalLength"));
		this.addressLabel.setText(LangModelMap.getString("Address"));
		this.dimensionLabel.setText(LangModelMap.getString("Dimension"));
		this.descLabel.setText(LangModelMap.getString("Description"));
		this.cityLabel.setText(LangModelMap.getString("CityKurz"));
		this.streetLabel.setText(LangModelMap.getString("StreetKurz"));
		this.buildingLabel.setText(LangModelMap.getString("BuildingKurz"));
		this.addressLabel.setText(LangModelMap.getString("Address"));

		this.xLabel.setText("X");
//		this.mTextField.setPreferredSize(new Dimension(60, 23));
//		this.nTextField.setPreferredSize(new Dimension(60, 23));
		this.dimensionPanel.setLayout(this.gridBagLayout2);
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.5;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.dimensionPanel.add(this.mTextField, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 5, 0, 5);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.dimensionPanel.add(this.xLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.5;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.dimensionPanel.add(this.nTextField, constraints);

		this.streetPanel.setLayout(this.gridBagLayout3);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.8;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = null;
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
		constraints.insets = null;
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
		constraints.insets = null;
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
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.nameTextField, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.typeLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = null;
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
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.startLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = null;
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
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.endLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = null;
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
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.topologicalLengthLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = null;
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
		constraints.insets = null;
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
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = null;
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
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.streetPanel, constraints);
//		this.jPanel.add(this.streetTextField, ReusedGridBagConstraints.get(2, 6, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
//		this.jPanel.add(this.buildingLabel, ReusedGridBagConstraints.get(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
//		this.jPanel.add(this.buildingTextField, ReusedGridBagConstraints.get(2, 7, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));

		constraints.gridx = 0;
		constraints.gridy = 8;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.dimensionLabel, constraints);

		constraints.gridx = 2;
		constraints.gridy = 8;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.dimensionPanel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 9;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.descLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 10;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(new JScrollPane(this.descTextArea), constraints);

		super.addToUndoableListener(this.nameTextField);
		super.addToUndoableListener(this.typeComboBox);
		super.addToUndoableListener(this.cityTextField);
		super.addToUndoableListener(this.streetTextField);
		super.addToUndoableListener(this.buildingTextField);
		super.addToUndoableListener(this.mTextField);
		super.addToUndoableListener(this.nTextField);
		super.addToUndoableListener(this.descTextArea);
		
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
			this.nameTextField.setText("");
			this.topologicalLengthTextField.setText("");
			this.typeComboBox.setEnabled(false);
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText("");

			this.mTextField.setText("");
			this.nTextField.setText("");

			this.cityTextField.setText("");
			this.streetTextField.setText("");
			this.buildingTextField.setText("");
		}
		else {
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.link.getName());
			
			this.topologicalLengthTextField.setText(String.valueOf(this.link.getLengthLt()));

			Collection types = LinkTypeController.getTopologicalLinkTypes();
			
			this.typeComboBox.setEnabled(true);
			this.typeComboBox.addElements(types);
			this.typeComboBox.setSelectedItem(this.link.getType());

			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.link.getDescription());

			this.startComboBox.addItem(this.link.getStartNode());
			this.startComboBox.setSelectedItem(this.link.getStartNode());
			this.endComboBox.addItem(this.link.getEndNode());
			this.endComboBox.setSelectedItem(this.link.getEndNode());

			this.mTextField.setText(String.valueOf(this.link.getBinding().getDimension().getWidth()));
			this.nTextField.setText(String.valueOf(this.link.getBinding().getDimension().getHeight()));

			this.cityTextField.setText(this.link.getCity());
			this.streetTextField.setText(this.link.getStreet());
			this.buildingTextField.setText(this.link.getBuilding());
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

			int m = Integer.parseInt(this.mTextField.getText());
			int n = Integer.parseInt(this.nTextField.getText());
			if(!this.link.getBinding().getDimension().equals(new IntDimension(m, n)))
				this.link.getBinding().setDimension(new IntDimension(m, n));
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
	}
}
