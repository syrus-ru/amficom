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
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.mapview.CablePath;

public class CablePathEditor implements StorableObjectEditor {
	private JPanel jPanel = new JPanel();

	private JLabel nameLabel = new JLabel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JTextField nameTextField = new JTextField();

	private JLabel cableLabel = new JLabel();

	private ObjComboBox cableComboBox = null;

	private JLabel descLabel = new JLabel();

	private JTextArea descTextArea = new JTextArea();

	CablePath path;

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
		// this.nameLabel.setPreferredSize(new Dimension(DEF_WIDTH,
		// DEF_HEIGHT));

		this.cableLabel.setText(LangModelMap.getString("cable"));
		// this.cableLabel.setPreferredSize(new Dimension(DEF_WIDTH,
		// DEF_HEIGHT));

		this.descLabel.setText(LangModelMap.getString("Description"));
		// this.descLabel.setPreferredSize(new Dimension(DEF_WIDTH,
		// DEF_HEIGHT));

		this.cableComboBox.setEditable(false);

		this.jPanel.add(this.nameLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel.add(this.nameTextField, ReusedGridBagConstraints.get( 1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel.add(this.cableLabel, ReusedGridBagConstraints.get( 0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel.add(this.cableComboBox, ReusedGridBagConstraints.get( 1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel.add(this.descLabel, ReusedGridBagConstraints.get( 0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanel.add(this.descTextArea, ReusedGridBagConstraints.get( 1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	public Object getObject() {
		return this.path;
	}

	public void setObject(Object objectResource) {
		this.path = (CablePath )objectResource;

		this.cableComboBox.removeAllItems();

		if(this.path == null) {
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText("");
			this.cableComboBox.setEnabled(false);
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText("");
		}
		else {
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.path.getName());
			this.cableComboBox.setEnabled(true);
			this.cableComboBox.addItem(this.path.getSchemeCableLink());
			this.cableComboBox.setSelectedItem(this.path.getSchemeCableLink());
			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.path.getDescription());
		}
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	public void commitChanges() {
		if(MiscUtil.validName(this.nameTextField.getText()))
			try {
				this.path.setName(this.nameTextField.getText());
				this.path.setDescription(this.descTextArea.getText());
			} catch(Exception ex) {
				ex.printStackTrace();
			}
	}
}
