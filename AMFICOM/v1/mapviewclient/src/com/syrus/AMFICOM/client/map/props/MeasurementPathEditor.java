package com.syrus.AMFICOM.client.map.props;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MiscUtil;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.mapview.MeasurementPath;

public class MeasurementPathEditor extends DefaultStorableObjectEditor {

	private JPanel jPanel = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel pathLabel = new JLabel();
	private WrapperedComboBox pathComboBox = null;

	private JLabel topologicalLengthLabel = new JLabel();
	private JTextField topologicalLengthTextField = new JTextField();
	private JLabel physicalLengthLabel = new JLabel();
	private JTextField physicalLengthTextField = new JTextField();
	private JLabel opticalLengthLabel = new JLabel();
	private JTextField opticalLengthTextField = new JTextField();
	private JLabel startLabel = new JLabel();
	private WrapperedComboBox startComboBox = null;
	private JLabel endLabel = new JLabel();
	private WrapperedComboBox endComboBox = null;

	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	MeasurementPath measurementPath;

	public MeasurementPathEditor() {
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() {
		SimpleMapElementController controller = SimpleMapElementController
				.getInstance();

		this.pathComboBox = new WrapperedComboBox(
				controller,
				SimpleMapElementController.KEY_NAME,
				SimpleMapElementController.KEY_NAME);
		this.startComboBox = new WrapperedComboBox(
				controller,
				SimpleMapElementController.KEY_NAME,
				SimpleMapElementController.KEY_NAME);
		this.endComboBox = new WrapperedComboBox(
				controller,
				SimpleMapElementController.KEY_NAME,
				SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModelGeneral.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.pathLabel.setText(LangModelMap.getString("path"));
		this.topologicalLengthLabel.setText(LangModelMap.getString("TopologicalLength"));
		this.physicalLengthLabel.setText(LangModelMap.getString("PhysicalLength"));
		this.opticalLengthLabel.setText(LangModelMap.getString("OpticalLength"));
		this.startLabel.setText(LangModelMap.getString("StartNode"));
		this.endLabel.setText(LangModelMap.getString("EndNode"));
		this.descLabel.setText(LangModelMap.getString("Description"));

		this.pathComboBox.setEditable(false);

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.nameLabel, constraints);

		constraints.gridx =  1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.nameTextField, constraints);

		constraints.gridx =  0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.pathLabel, constraints);

		constraints.gridx =  1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.pathComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.startLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
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
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.endLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
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
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.topologicalLengthLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
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
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.physicalLengthLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.physicalLengthTextField, constraints);

		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.opticalLengthLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 6;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.opticalLengthTextField, constraints);

		constraints.gridx =  0;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.descLabel, constraints);

		constraints.gridx =  1;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.descTextArea, constraints);

		this.nameTextField.setEnabled(false);
		this.pathComboBox.setEditable(false);
		this.startComboBox.setEnabled(false);
		this.endComboBox.setEnabled(false);
		this.topologicalLengthTextField.setEnabled(false);

		super.addToUndoableListener(this.physicalLengthTextField);
		super.addToUndoableListener(this.opticalLengthTextField);
		super.addToUndoableListener(this.descTextArea);
	}

	public Object getObject() {
		return this.measurementPath;
	}

	public void setObject(Object object) {
		this.measurementPath = (MeasurementPath )object;

		this.pathComboBox.removeAllItems();
		this.startComboBox.removeAllItems();
		this.endComboBox.removeAllItems();

		if(this.measurementPath == null) {
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText("");
			this.pathComboBox.setEnabled(false);
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText("");

			this.topologicalLengthTextField.setText("");
			this.physicalLengthTextField.setText("");
			this.opticalLengthTextField.setText("");
		}
		else {
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.measurementPath.getName());
			this.pathComboBox.setEnabled(true);
			this.pathComboBox.addItem(this.measurementPath.getSchemePath());
			this.pathComboBox.setSelectedItem(this.measurementPath.getSchemePath());
			this.topologicalLengthTextField.setText(String.valueOf(this.measurementPath.getLengthLt()));
			this.physicalLengthTextField.setText(String.valueOf(this.measurementPath.getLengthLf()));
			this.opticalLengthTextField.setText(String.valueOf(this.measurementPath.getLengthLo()));
			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.measurementPath.getDescription());
			this.startComboBox.addItem(this.measurementPath.getStartNode());
			this.startComboBox.setSelectedItem(this.measurementPath.getStartNode());
			this.endComboBox.addItem(this.measurementPath.getEndNode());
			this.endComboBox.setSelectedItem(this.measurementPath.getEndNode());
		}
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	public void commitChanges() {
		if(MiscUtil.validName(this.nameTextField.getText()))
			try {
				this.measurementPath.setName(this.nameTextField.getText());
				this.measurementPath.setDescription(this.descTextArea.getText());
			} catch(Exception ex) {
				ex.printStackTrace();
			}
	}
}
