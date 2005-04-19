package com.syrus.AMFICOM.Client.Map.Props;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.mapview.MeasurementPath;

public class MeasurementPathEditor extends DefaultStorableObjectEditor {

	private JPanel jPanel = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel pathLabel = new JLabel();
	private ObjComboBox pathComboBox = null;

	private JLabel topologicalLengthLabel = new JLabel();
	private JTextField topologicalLengthTextField = new JTextField();
	private JLabel physicalLengthLabel = new JLabel();
	private JTextField physicalLengthTextField = new JTextField();
	private JLabel opticalLengthLabel = new JLabel();
	private JTextField opticalLengthTextField = new JTextField();
	private JLabel startLabel = new JLabel();
	private ObjComboBox startComboBox = null;
	private JLabel endLabel = new JLabel();
	private ObjComboBox endComboBox = null;

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

		this.pathComboBox = new ObjComboBox(
				controller,
				SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModel.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.pathLabel.setText(LangModelMap.getString("path"));
		this.topologicalLengthLabel.setText(LangModelMap.getString("TopologicalLength"));
		this.physicalLengthLabel.setText(LangModelMap.getString("PhysicalLength"));
		this.opticalLengthLabel.setText(LangModelMap.getString("OpticalLength"));
		this.startLabel.setText(LangModelMap.getString("StartNode"));
		this.endLabel.setText(LangModelMap.getString("EndNode"));
		this.descLabel.setText(LangModelMap.getString("Description"));

		this.pathComboBox.setEditable(false);

		this.jPanel.add(this.nameLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel.add(this.nameTextField, ReusedGridBagConstraints.get( 1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel.add(this.pathLabel, ReusedGridBagConstraints.get( 0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel.add(this.pathComboBox, ReusedGridBagConstraints.get( 1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel.add(this.startLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.startComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.endLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.endComboBox, ReusedGridBagConstraints.get(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.topologicalLengthLabel, ReusedGridBagConstraints.get(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.topologicalLengthTextField, ReusedGridBagConstraints.get(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.physicalLengthLabel, ReusedGridBagConstraints.get(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.physicalLengthTextField, ReusedGridBagConstraints.get(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.opticalLengthLabel, ReusedGridBagConstraints.get(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.opticalLengthTextField, ReusedGridBagConstraints.get(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.descLabel, ReusedGridBagConstraints.get( 0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel.add(this.descTextArea, ReusedGridBagConstraints.get( 1, 7, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

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
