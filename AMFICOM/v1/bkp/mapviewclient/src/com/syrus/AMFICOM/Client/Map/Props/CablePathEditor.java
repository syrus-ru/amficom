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
import com.syrus.AMFICOM.client_.general.ui_.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.mapview.CablePath;

public class CablePathEditor extends DefaultStorableObjectEditor {

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jPanel = new JPanel();

	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel cableLabel = new JLabel();
	private ObjComboBox cableComboBox = null;

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

	CablePath cablePath;

	public CablePathEditor() {
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() {
		SimpleMapElementController controller = SimpleMapElementController
				.getInstance();

		this.cableComboBox = new ObjComboBox(
				controller,
				SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModel.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.cableLabel.setText(LangModelMap.getString("cable"));
		this.topologicalLengthLabel.setText(LangModelMap.getString("TopologicalLength"));
		this.physicalLengthLabel.setText(LangModelMap.getString("PhysicalLength"));
		this.opticalLengthLabel.setText(LangModelMap.getString("OpticalLength"));
		this.startLabel.setText(LangModelMap.getString("StartNode"));
		this.endLabel.setText(LangModelMap.getString("EndNode"));
		this.descLabel.setText(LangModelMap.getString("Description"));

		this.jPanel.add(this.nameLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel.add(this.nameTextField, ReusedGridBagConstraints.get( 1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel.add(this.cableLabel, ReusedGridBagConstraints.get( 0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel.add(this.cableComboBox, ReusedGridBagConstraints.get( 1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
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
		this.cableComboBox.setEditable(false);
		this.startComboBox.setEnabled(false);
		this.endComboBox.setEnabled(false);
		this.topologicalLengthTextField.setEnabled(false);

		super.addToUndoableListener(this.physicalLengthTextField);
		super.addToUndoableListener(this.opticalLengthTextField);
		super.addToUndoableListener(this.descTextArea);
	}

	public Object getObject() {
		return this.cablePath;
	}

	public void setObject(Object objectResource) {
		this.cablePath = (CablePath )objectResource;

		this.cableComboBox.removeAllItems();
		this.startComboBox.removeAllItems();
		this.endComboBox.removeAllItems();

		if(this.cablePath == null) {
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText("");
			this.cableComboBox.setEnabled(false);
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText("");

			this.topologicalLengthTextField.setText("");
			this.physicalLengthTextField.setText("");
			this.opticalLengthTextField.setText("");
		}
		else {
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.cablePath.getName());
			this.cableComboBox.addItem(this.cablePath.getSchemeCableLink());
			this.cableComboBox.setSelectedItem(this.cablePath.getSchemeCableLink());
			this.topologicalLengthTextField.setText(String.valueOf(this.cablePath.getLengthLt()));
			this.physicalLengthTextField.setText(String.valueOf(this.cablePath.getLengthLf()));
			this.opticalLengthTextField.setText(String.valueOf(this.cablePath.getLengthLo()));
			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.cablePath.getDescription());
			this.startComboBox.addItem(this.cablePath.getStartNode());
			this.startComboBox.setSelectedItem(this.cablePath.getStartNode());
			this.endComboBox.addItem(this.cablePath.getEndNode());
			this.endComboBox.setSelectedItem(this.cablePath.getEndNode());
		}
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	public void commitChanges() {
		try {
			this.cablePath.setDescription(this.descTextArea.getText());

			double physicalLength = Double.parseDouble(this.physicalLengthTextField.getText());
			double opticalLength = Double.parseDouble(this.opticalLengthTextField.getText());
			
			this.cablePath.setLengthLf(physicalLength);
			this.cablePath.setLengthLo(opticalLength);
		} 
		catch (NumberFormatException ex) 
		{
			System.out.println(ex.getMessage());
		} 
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
