package com.syrus.AMFICOM.Client.Map.Props;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeTypeController;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;

public class MapSiteGeneralPanel
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

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

	private JPanel addressPanel = new JPanel();
	private JLabel cityLabel = new JLabel();
	private JLabel streetLabel = new JLabel();
	private JLabel buildingLabel = new JLabel();
	private JTextField cityTextField = new JTextField();
	private JTextField streetTextField = new JTextField();
	private JTextField buildingTextField = new JTextField();
	private JLabel addressLabel = new JLabel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	SiteNode site;

	private LogicalNetLayer lnl;

	public MapSiteGeneralPanel()
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

	public void setLogicalNetLayer(LogicalNetLayer lnl)
	{
		this.lnl = lnl;
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return this.lnl;
	}

	private void jbInit()
	{
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.typeComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);

		this.setLayout(this.gridBagLayout1);
		this.setName(LangModel.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.typeLabel.setText(LangModelMap.getString("Type"));
		this.typeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.longLabel.setText(LangModelMap.getString("Longitude"));
		this.longLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.latLabel.setText(LangModelMap.getString("Latitude"));
		this.latLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.descLabel.setText(LangModelMap.getString("Description"));
		this.descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.addressPanel.setLayout(this.gridBagLayout2);
		this.cityLabel.setText(LangModelMap.getString("City"));
		this.streetLabel.setText(LangModelMap.getString("Street"));
		this.buildingLabel.setText(LangModelMap.getString("Building"));
		this.addressLabel.setText(LangModelMap.getString("Address"));

		this.addressPanel.add(this.cityLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		this.addressPanel.add(this.cityTextField, ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.addressPanel.add(this.streetLabel, ReusedGridBagConstraints.get(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.addressPanel.add(this.streetTextField, ReusedGridBagConstraints.get(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.addressPanel.add(this.buildingLabel, ReusedGridBagConstraints.get(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
		this.addressPanel.add(this.buildingTextField, ReusedGridBagConstraints.get(5, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));

		this.add(this.nameLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.nameTextField, ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.typeLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.typeComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.longLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.longTextField, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.latLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.latTextField, ReusedGridBagConstraints.get(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.addressLabel, ReusedGridBagConstraints.get(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.addressPanel, ReusedGridBagConstraints.get(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.descLabel, ReusedGridBagConstraints.get(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.descTextArea, ReusedGridBagConstraints.get(1, 5, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));
	}

	public Object getObject()
	{
		return null;
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

			List protos = NodeTypeController.getTopologicalProtos(getLogicalNetLayer().getContext());

			this.typeComboBox.setEnabled(true);
			this.typeComboBox.addElements(protos);
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

	public void setContext(ApplicationContext aContext)
	{
	}

	public boolean modify()
	{
		try 
		{
			this.site.setName(this.nameTextField.getText());
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
			return true;
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
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
