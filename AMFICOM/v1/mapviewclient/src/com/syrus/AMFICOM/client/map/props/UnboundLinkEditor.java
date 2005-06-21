package com.syrus.AMFICOM.client.map.props;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.mapview.UnboundLink;

public class UnboundLinkEditor extends DefaultStorableObjectEditor {
	UnboundLink link;

	private JPanel jPanel = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel typeLabel = new JLabel();
	private WrapperedComboBox typeComboBox = null;
	private JLabel cableLabel = new JLabel();
	private WrapperedComboBox cableComboBox = null;
	private JLabel topologicalLengthLabel = new JLabel();
	private JTextField topologicalLengthTextField = new JTextField();

	private JLabel startLabel = new JLabel();
	private WrapperedComboBox startComboBox = null;
	private JLabel endLabel = new JLabel();
	private WrapperedComboBox endComboBox = null;

	public UnboundLinkEditor() {
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	private void jbInit() {
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.cableComboBox = new WrapperedComboBox(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);
		this.typeComboBox = new WrapperedComboBox(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);
		this.startComboBox = new WrapperedComboBox(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);
		this.endComboBox = new WrapperedComboBox(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModelGeneral.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.typeLabel.setText(LangModelMap.getString("Type"));
		this.cableLabel.setText(LangModelMap.getString("cable"));
		this.startLabel.setText(LangModelMap.getString("StartNode"));
		this.endLabel.setText(LangModelMap.getString("EndNode"));
		this.topologicalLengthLabel.setText(LangModelMap.getString("TopologicalLength"));

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.nameLabel, constraints);

		constraints.gridx = 1;
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
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.typeLabel, constraints);

		constraints.gridx = 1;
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
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = null;
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
		constraints.insets = null;
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
		constraints.insets = null;
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
		constraints.insets = null;
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
		constraints.insets = null;
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
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.topologicalLengthTextField, constraints);

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
		this.jPanel.add(this.cableLabel, constraints);

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
		this.jPanel.add(this.cableComboBox, constraints);

		this.nameTextField.setEnabled(false);
		this.typeComboBox.setEnabled(false);
		this.cableComboBox.setEnabled(false);
		this.startComboBox.setEnabled(false);
		this.endComboBox.setEnabled(false);
		this.topologicalLengthTextField.setEnabled(false);
	}

	public Object getObject() {
		return this.link;
	}

	public void setObject(Object object) {
		this.link = (UnboundLink )object;

		this.cableComboBox.removeAllItems();
		this.typeComboBox.removeAllItems();
		this.startComboBox.removeAllItems();
		this.endComboBox.removeAllItems();

		if(this.link == null) {
			this.nameTextField.setText("");
			this.topologicalLengthTextField.setText("");
		}
		else {
			this.nameTextField.setText(this.link.getName());
			this.topologicalLengthTextField.setText(String.valueOf(this.link.getLengthLt()));

			this.cableComboBox.addItem(this.link.getCablePath().getSchemeCableLink());
			this.cableComboBox.setSelectedItem(this.link.getCablePath().getSchemeCableLink());
			
			this.typeComboBox.addItem(this.link.getType());
			this.typeComboBox.setSelectedItem(this.link.getType());

			this.startComboBox.addItem(this.link.getStartNode());
			this.startComboBox.setSelectedItem(this.link.getStartNode());
			this.endComboBox.addItem(this.link.getEndNode());
			this.endComboBox.setSelectedItem(this.link.getEndNode());
		}
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	public void commitChanges() {
		// empty nothing to commit
	}
}
