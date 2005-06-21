package com.syrus.AMFICOM.client.map.props;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.mapview.UnboundNode;

public class UnboundNodeEditor extends DefaultStorableObjectEditor {
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JPanel jPanel = new JPanel();
	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel typeLabel = new JLabel();
	private WrapperedComboBox typeComboBox = null;

	private JLabel elementLabel = new JLabel();
	private WrapperedComboBox elementComboBox = null;

	private JLabel longLabel = new JLabel();
	private JTextField longTextField = new JTextField();
	private JLabel latLabel = new JLabel();
	private JTextField latTextField = new JTextField();

	UnboundNode unbound;

	public UnboundNodeEditor() {
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
		this.elementComboBox = new WrapperedComboBox(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModelGeneral.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.typeLabel.setText(LangModelMap.getString("Type"));
		this.longLabel.setText(LangModelMap.getString("Longitude"));
		this.latLabel.setText(LangModelMap.getString("Latitude"));
		this.elementLabel.setText(LangModelMap.getString("SchemeElement"));

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
		this.jPanel.add(this.longLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.longTextField, constraints);

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
		this.jPanel.add(this.latLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.latTextField, constraints);

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
		this.jPanel.add(this.elementLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = null;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.elementComboBox, constraints);

		this.nameTextField.setEnabled(false);
		this.typeComboBox.setEnabled(false);
		this.elementComboBox.setEnabled(false);
		super.addToUndoableListener(this.longTextField);
		super.addToUndoableListener(this.latTextField);
	}

	public Object getObject() {
		return this.unbound;
	}

	public void setObject(Object object) {
		this.unbound = (UnboundNode )object;

		this.typeComboBox.removeAllItems();
		this.elementComboBox.removeAllItems();

		if(this.unbound == null) {
			this.nameTextField.setText("");

			this.longTextField.setEnabled(false);
			this.longTextField.setText("");
			this.latTextField.setEnabled(false);
			this.latTextField.setText("");
		}
		else {
			this.nameTextField.setText(this.unbound.getName());

			this.elementComboBox.addItem(this.unbound.getSchemeElement());
			this.elementComboBox.setSelectedItem(this.unbound.getSchemeElement());

			this.typeComboBox.addItem(this.unbound.getType());
			this.typeComboBox.setSelectedItem(this.unbound.getType());

			this.longTextField.setEnabled(true);
			this.longTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(this.unbound.getLocation().getX()));
			this.latTextField.setEnabled(true);
			this.latTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(this.unbound.getLocation().getY()));
		}
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	public void commitChanges() {
		try {
			double x = Double.parseDouble(this.longTextField.getText());
			double y = Double.parseDouble(this.latTextField.getText());

			this.unbound.setLocation(new DoublePoint(x, y));
		} catch(NumberFormatException ex) {
			System.out.println(ex.getMessage());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
