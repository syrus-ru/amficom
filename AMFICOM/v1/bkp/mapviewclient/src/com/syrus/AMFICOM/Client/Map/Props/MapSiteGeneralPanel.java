package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.Controllers.SiteNodeController;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.geom.Point2D;

import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
		return lnl;
	}

	private void jbInit()
	{
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		typeComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);

		this.setLayout(gridBagLayout1);
		this.setName(LangModel.getString("Properties"));

		nameLabel.setText(LangModelMap.getString("Name"));
		nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		typeLabel.setText(LangModelMap.getString("Type"));
		typeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		longLabel.setText(LangModelMap.getString("Longitude"));
		longLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		latLabel.setText(LangModelMap.getString("Latitude"));
		latLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		descLabel.setText(LangModelMap.getString("Description"));
		descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		addressPanel.setLayout(gridBagLayout2);
		cityLabel.setText(LangModelMap.getString("City"));
		streetLabel.setText(LangModelMap.getString("Street"));
		buildingLabel.setText(LangModelMap.getString("Building"));
		addressLabel.setText(LangModelMap.getString("Address"));

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
		this.add(longLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(longTextField, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(latLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(latTextField, ReusedGridBagConstraints.get(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(addressLabel, ReusedGridBagConstraints.get(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(addressPanel, ReusedGridBagConstraints.get(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(descLabel, ReusedGridBagConstraints.get(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(descTextArea, ReusedGridBagConstraints.get(1, 5, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));
	}

	public Object getObject()
	{
		return null;
	}

	public void setObject(Object objectResource)
	{
		site = (SiteNode)objectResource;
		
		typeComboBox.removeAll();

		if(site == null)
		{
			nameTextField.setEnabled(false);
			nameTextField.setText("");
			typeComboBox.setEnabled(false);
			descTextArea.setEnabled(false);
			descTextArea.setText("");

			longTextField.setEnabled(false);
			longTextField.setText("");
			latTextField.setEnabled(false);
			latTextField.setText("");

			cityTextField.setText("");
			streetTextField.setText("");
			buildingTextField.setText("");
		}
		else
		{
			nameTextField.setEnabled(true);
			nameTextField.setText(site.getName());

			List protos = getLogicalNetLayer().getTopologicalProtos();
			
			typeComboBox.setEnabled(true);
			typeComboBox.addElements(protos);
			typeComboBox.setSelectedItem(site.getType());

			descTextArea.setEnabled(true);
			descTextArea.setText(site.getDescription());

			longTextField.setEnabled(true);
			longTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(site.getLocation().getX()));
			latTextField.setEnabled(true);
			latTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(site.getLocation().getY()));

			cityTextField.setText(site.getCity());
			streetTextField.setText(site.getStreet());
			buildingTextField.setText(site.getBuilding());
		}
	}

	public void setContext(ApplicationContext aContext)
	{
	}

	public boolean modify()
	{
		try 
		{
			site.setName(nameTextField.getText());
			site.setType((SiteNodeType )typeComboBox.getSelectedItem());
			site.setDescription(descTextArea.getText());
			
//			LogicalNetLayer lnl = (LogicalNetLayer )(site.getMap().getConverter());
			
//			SiteNodeController snc = (SiteNodeController )lnl.getMapViewController().getController(site);
//			snc.updateScaleCoefficient(site);
//			site.setScaleCoefficient(lnl.getDefaultScale() / lnl.getCurrentScale());

			try 
			{
				double x = Double.parseDouble(longTextField.getText());
				double y = Double.parseDouble(latTextField.getText());
				
				site.setLocation(new DoublePoint(x, y));
			} 
			catch (NumberFormatException ex) 
			{
				System.out.println(ex.getMessage());
			} 

			site.setCity(cityTextField.getText());
			site.setStreet(streetTextField.getText());
			site.setBuilding(buildingTextField.getText());
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
