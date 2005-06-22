package com.syrus.AMFICOM.client.map.props;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.TopologicalNode;

public class TopologicalNodeEditor extends DefaultStorableObjectEditor {
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JPanel jPanel = new JPanel();

	private JLabel longLabel = new JLabel();
	private JTextField longTextField = new JTextField();
	private JLabel latLabel = new JLabel();
	private JTextField latTextField = new JTextField();
	private JLabel linkLabel = new JLabel();
	private WrapperedComboBox linkComboBox = null;

	TopologicalNode node;

	public TopologicalNodeEditor()
	{
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	private void jbInit()
	{
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.linkComboBox = new WrapperedComboBox(
				controller, 
				SimpleMapElementController.KEY_NAME,
				SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModelGeneral.getString("Properties"));

		this.linkLabel.setText(LangModelMap.getString("Physical_link_id"));
		this.longLabel.setText(LangModelMap.getString("Longitude"));
		this.latLabel.setText(LangModelMap.getString("Latitude"));

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.linkLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.linkComboBox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.longLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.longTextField, constraints);

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
		this.jPanel.add(this.latLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(this.latTextField, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
		constraints.ipadx = 0;
		constraints.ipady = 0;
		this.jPanel.add(Box.createGlue(), constraints);

		super.addToUndoableListener(this.longTextField);
		super.addToUndoableListener(this.latTextField);
		this.linkComboBox.setEnabled(false);
	}

	public Object getObject() {
		return this.node;
	}

	public void setObject(Object objectResource) {
		this.node = (TopologicalNode )objectResource;

		this.linkComboBox.removeAllItems();

		if(this.node == null) {
			this.longTextField.setEnabled(false);
			this.longTextField.setText("");
			this.latTextField.setEnabled(false);
			this.latTextField.setText("");
		}
		else {
			this.linkComboBox.addItem(this.node.getPhysicalLink());
			this.linkComboBox.setSelectedItem(this.node.getPhysicalLink());

			this.longTextField.setEnabled(true);
			this.longTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(this.node.getLocation().getX()));
			this.latTextField.setEnabled(true);
			this.latTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(this.node.getLocation().getY()));
		}
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	public void commitChanges() {
		try {
			double x = Double.parseDouble(this.longTextField.getText());
			double y = Double.parseDouble(this.latTextField.getText());

			this.node.setLocation(new DoublePoint(x, y));
		} catch(NumberFormatException ex) {
			System.out.println(ex.getMessage());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
