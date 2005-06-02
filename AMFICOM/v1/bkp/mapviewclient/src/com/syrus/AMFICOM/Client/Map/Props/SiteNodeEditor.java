package com.syrus.AMFICOM.Client.Map.Props;

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

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeTypeController;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;

public class SiteNodeEditor extends DefaultStorableObjectEditor
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JPanel jPanel = new JPanel();
	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel typeLabel = new JLabel();
	private ObjComboBox typeComboBox = null;

	private JLabel longLabel = new JLabel();
	private JTextField longTextField = new JTextField();
	private JLabel latLabel = new JLabel();
	private JTextField latTextField = new JTextField();

	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	private JPanel streetPanel = new JPanel();
	private JLabel cityLabel = new JLabel();
	private JLabel streetLabel = new JLabel();
	private JLabel buildingLabel = new JLabel();
	private JTextField cityTextField = new JTextField();
	private JTextField streetTextField = new JTextField();
	private JTextField buildingTextField = new JTextField();
	private JLabel addressLabel = new JLabel();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();

	SiteNode site;

	public SiteNodeEditor()
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

		this.typeComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);

		this.jPanel.setLayout(this.gridBagLayout1);
		this.jPanel.setName(LangModel.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.typeLabel.setText(LangModelMap.getString("Type"));
		this.longLabel.setText(LangModelMap.getString("Longitude"));
		this.latLabel.setText(LangModelMap.getString("Latitude"));
		this.descLabel.setText(LangModelMap.getString("Description"));
		this.cityLabel.setText(LangModelMap.getString("CityKurz"));
		this.streetLabel.setText(LangModelMap.getString("StreetKurz"));
		this.buildingLabel.setText(LangModelMap.getString("BuildingKurz"));
		this.addressLabel.setText(LangModelMap.getString("Address"));

		this.streetPanel.setLayout(this.gridBagLayout3);
		this.streetPanel.add(this.streetTextField, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.8, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.streetPanel.add(this.buildingLabel, ReusedGridBagConstraints.get(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 15, 0, 5), 0, 0));
		this.streetPanel.add(this.buildingTextField, ReusedGridBagConstraints.get(2, 0, 1, 1, 0.2, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));

		this.jPanel.add(this.nameLabel, ReusedGridBagConstraints.get(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.nameTextField, ReusedGridBagConstraints.get(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.typeLabel, ReusedGridBagConstraints.get(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.typeComboBox, ReusedGridBagConstraints.get(2, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.longLabel, ReusedGridBagConstraints.get(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.longTextField, ReusedGridBagConstraints.get(2, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.latLabel, ReusedGridBagConstraints.get(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.latTextField, ReusedGridBagConstraints.get(2, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.addressLabel, ReusedGridBagConstraints.get(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(this.cityLabel, ReusedGridBagConstraints.get(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.jPanel.add(this.cityTextField, ReusedGridBagConstraints.get(2, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.streetLabel, ReusedGridBagConstraints.get(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.jPanel.add(this.streetPanel, ReusedGridBagConstraints.get(2, 6, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
//		this.jPanel.add(this.streetTextField, ReusedGridBagConstraints.get(2, 6, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
//		this.jPanel.add(this.buildingLabel, ReusedGridBagConstraints.get(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
//		this.jPanel.add(this.buildingTextField, ReusedGridBagConstraints.get(2, 7, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.jPanel.add(this.descLabel, ReusedGridBagConstraints.get(0, 8, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.jPanel.add(new JScrollPane(this.descTextArea), ReusedGridBagConstraints.get(0, 9, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));

		super.addToUndoableListener(this.nameTextField);
		super.addToUndoableListener(this.typeComboBox);
		super.addToUndoableListener(this.longTextField);
		super.addToUndoableListener(this.latTextField);
		super.addToUndoableListener(this.cityTextField);
		super.addToUndoableListener(this.streetTextField);
		super.addToUndoableListener(this.buildingTextField);
		super.addToUndoableListener(this.descTextArea);
		
}

	public Object getObject()
	{
		return this.site;
	}

	public void setObject(Object objectResource)
	{
		this.site = (SiteNode)objectResource;
		
		this.typeComboBox.removeAllItems();

		if(this.site == null)
		{
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText("");
			this.typeComboBox.setEnabled(false);
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText("");

			this.longTextField.setEnabled(false);
			this.longTextField.setText("");
			this.latTextField.setEnabled(false);
			this.latTextField.setText("");

			this.cityTextField.setText("");
			this.streetTextField.setText("");
			this.buildingTextField.setText("");
		}
		else
		{
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.site.getName());

			Collection types = NodeTypeController.getTopologicalNodeTypes();

			this.typeComboBox.setEnabled(true);
			this.typeComboBox.addElements(types);
			this.typeComboBox.setSelectedItem(this.site.getType());

			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.site.getDescription());

			this.longTextField.setEnabled(true);
			this.longTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(this.site.getLocation().getX()));
			this.latTextField.setEnabled(true);
			this.latTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(this.site.getLocation().getY()));

			this.cityTextField.setText(this.site.getCity());
			this.streetTextField.setText(this.site.getStreet());
			this.buildingTextField.setText(this.site.getBuilding());
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
			this.site.setName(name);
			this.site.setType((SiteNodeType )this.typeComboBox.getSelectedItem());
			this.site.setDescription(this.descTextArea.getText());
			
			try 
			{
				double x = Double.parseDouble(this.longTextField.getText());
				double y = Double.parseDouble(this.latTextField.getText());
				
				this.site.setLocation(new DoublePoint(x, y));
			} 
			catch (NumberFormatException ex) 
			{
				System.out.println(ex.getMessage());
			} 

			this.site.setCity(this.cityTextField.getText());
			this.site.setStreet(this.streetTextField.getText());
			this.site.setBuilding(this.buildingTextField.getText());
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
	}
}
