package com.syrus.AMFICOM.client.map.props;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.controllers.MarkerController;
import com.syrus.AMFICOM.client.map.ui.SimpleMapElementController;
import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.mapview.Marker;

public class MarkerEditor extends DefaultStorableObjectEditor {
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JPanel jPanel = new JPanel();

	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel typeLabel = new JLabel();
	private JTextField typeTextField = new JTextField();
	private JLabel longLabel = new JLabel();
	private JTextField longTextField = new JTextField();
	private JLabel latLabel = new JLabel();
	private JTextField latTextField = new JTextField();
	private JLabel pathLabel = new JLabel();
	private WrapperedComboBox pathComboBox = null;
	private JLabel distanceLabel = new JLabel();
	private JTextField distanceTextField = new JTextField();

	Marker marker;

	public MarkerEditor() {
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	private void jbInit() {
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.pathComboBox = new WrapperedComboBox(controller, SimpleMapElementController.KEY_NAME, SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModelGeneral.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.typeLabel.setText(LangModelMap.getString("Type"));
		this.distanceLabel.setText(LangModelMap.getString("Distance"));
		this.pathLabel.setText(LangModelMap.getString("PhysicalLink"));
		this.longLabel.setText(LangModelMap.getString("Longitude"));
		this.latLabel.setText(LangModelMap.getString("Latitude"));

		this.jPanel.add(this.nameLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.nameTextField, ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.pathLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.pathComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.distanceLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.distanceTextField, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.longLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.longTextField, ReusedGridBagConstraints.get(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.latLabel, ReusedGridBagConstraints.get(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.latTextField, ReusedGridBagConstraints.get(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));

		super.addToUndoableListener(this.distanceTextField);

		this.longTextField.setEnabled(false);
		this.latTextField.setEnabled(false);
		this.pathComboBox.setEnabled(false);
		this.nameTextField.setEnabled(false);
		this.typeTextField.setEnabled(false);
	}

	public Object getObject() {
		return this.marker;
	}

	public void setObject(Object object) {
		this.marker = (Marker )object;

		this.pathComboBox.removeAllItems();

		if(this.marker == null) {
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText("");

			this.longTextField.setEnabled(false);
			this.longTextField.setText("");
			this.latTextField.setEnabled(false);
			this.latTextField.setText("");
			this.typeTextField.setEnabled(false);
			this.typeTextField.setText("");
			this.distanceTextField.setEnabled(false);
			this.distanceTextField.setText("");
		}
		else {
			MarkerController markerController = (MarkerController)MarkerController.getInstance();

			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.marker.getName());

			this.typeTextField.setEnabled(true);
			this.typeTextField.setText(LangModelGeneral.getString("node" + MapViewController.getMapElementType(this.marker)));
			this.distanceTextField.setEnabled(true);
			this.distanceTextField.setText(MapPropertiesManager.getDistanceFormat().format(markerController.getFromStartLengthLf(this.marker)));

			this.pathComboBox.addItem(this.marker.getMeasurementPath());
			this.pathComboBox.setSelectedItem(this.marker.getMeasurementPath());

			this.longTextField.setEnabled(true);
			this.longTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(this.marker.getLocation().getX()));
			this.latTextField.setEnabled(true);
			this.latTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(this.marker.getLocation().getY()));
		}
	}

	public JComponent getGUI() {
		return this.jPanel;
	}

	public void commitChanges() {
		try {
			double distance = Double.parseDouble(this.distanceTextField.getText());
			MarkerController markerController = (MarkerController )MarkerController.getInstance();
			markerController.moveToFromStartLf(this.marker, distance);
		} catch(NumberFormatException ex) {
			System.out.println(ex.getMessage());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
