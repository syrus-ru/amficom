package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Map.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.FlowLayout;

public class MapLinkGeneralPanel extends JPanel implements ObjectResourcePropertiesPane
{
	private JLabel nameLabel = new JLabel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JTextField nameTextField = new JTextField();
	private JLabel typeLabel = new JLabel();
	private ObjectResourceComboBox typeComboBox = new ObjectResourceComboBox(MapLinkProtoElement.typ);
	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	private JPanel addressPanel = new JPanel();
	private JLabel cityLabel = new JLabel();
	private JLabel streetLabel = new JLabel();
	private JLabel buildingLabel = new JLabel();
	private JTextField cityTextField = new JTextField();
	private JTextField streetTextField = new JTextField();
	private JTextField buildingTextField = new JTextField();
	private JLabel addressLabel = new JLabel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	MapPhysicalLinkElement link;
	private JLabel dimensionLabel = new JLabel();
	private JPanel dimensionPanel = new JPanel();
	private JLabel xLabel = new JLabel();
	private JTextField mTextField = new JTextField();
	private JTextField nTextField = new JTextField();

	public MapLinkGeneralPanel()
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

	private void jbInit() throws Exception
	{
		this.setLayout(gridBagLayout1);
		this.setName(LangModel.getString("Properties"));

		nameLabel.setText(LangModelMap.getString("Name"));
		nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		typeLabel.setText(LangModelMap.getString("Type"));
		typeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		addressLabel.setText(LangModelMap.getString("Address"));
		addressLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		dimensionLabel.setText(LangModelMap.getString("Dimension"));
		dimensionLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		descLabel.setText(LangModelMap.getString("Description"));
		descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		addressPanel.setLayout(gridBagLayout2);
		cityLabel.setText(LangModelMap.getString("City"));
		streetLabel.setText(LangModelMap.getString("Street"));
		buildingLabel.setText(LangModelMap.getString("Building"));

		xLabel.setText("X");
		mTextField.setPreferredSize(new Dimension(60, 23));
		nTextField.setPreferredSize(new Dimension(60, 23));
		dimensionPanel.add(mTextField, null);
		dimensionPanel.add(xLabel, null);
		dimensionPanel.add(nTextField, null);

		addressPanel.add(cityLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		addressPanel.add(cityTextField, ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		addressPanel.add(streetLabel, ReusedGridBagConstraints.get(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		addressPanel.add(streetTextField, ReusedGridBagConstraints.get(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		addressPanel.add(buildingLabel, ReusedGridBagConstraints.get(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		addressPanel.add(buildingTextField, ReusedGridBagConstraints.get(5, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));

		this.add(nameLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(nameTextField, ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(typeLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(typeComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(addressLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(addressPanel, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(dimensionLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(dimensionPanel, ReusedGridBagConstraints.get(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 0, 0));
		this.add(descLabel, ReusedGridBagConstraints.get(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(descTextArea, ReusedGridBagConstraints.get(1, 4, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));
	}

	public ObjectResource getObjectResource()
	{
		return null;
	}

	public void setObjectResource(ObjectResource objectResource)
	{
		link = (MapPhysicalLinkElement )objectResource;
		if(link == null)
		{
			nameTextField.setEnabled(false);
			nameTextField.setText("");
			typeComboBox.setEnabled(false);
			descTextArea.setEnabled(false);
			descTextArea.setText("");

			mTextField.setText("");
			nTextField.setText("");

			cityTextField.setText("");
			streetTextField.setText("");
			buildingTextField.setText("");
		}
		else
		{
			nameTextField.setEnabled(true);
			nameTextField.setText(link.getName());
			typeComboBox.setEnabled(true);
			typeComboBox.setSelected(link.getMapProtoId());
			descTextArea.setEnabled(true);
			descTextArea.setText(link.getDescription());

			mTextField.setText(String.valueOf(link.getBinding().getDimension().width));
			nTextField.setText(String.valueOf(link.getBinding().getDimension().height));

			cityTextField.setText(link.getCity());
			streetTextField.setText(link.getStreet());
			buildingTextField.setText(link.getBuilding());
		}
	}

	public void setContext(ApplicationContext aContext)
	{
	}

	public boolean modify()
	{
		try 
		{
			link.setName(nameTextField.getText());
			link.setMapProtoId(typeComboBox.getSelectedId());
			link.setDescription(descTextArea.getText());

			link.setCity(cityTextField.getText());
			link.setStreet(streetTextField.getText());
			link.setBuilding(buildingTextField.getText());
			
			int m = Integer.parseInt(mTextField.getText());
			int n = Integer.parseInt(nTextField.getText());
			if(!link.getBinding().getDimension().equals(new Dimension(m, n)))
				link.getBinding().setDimension(new Dimension(m, n));
			return true;
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
		finally 
		{
		}
		
		return false;
	}

	public boolean create()
	{
		return false;
	}

	public boolean delete()
	{
		return false;
	}

	public boolean open()
	{
		return false;
	}

	public boolean save()
	{
		return false;
	}

	public boolean cancel()
	{
		return false;
	}
}
