package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceListBox;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.MapView.VoidMapElement;
import com.syrus.AMFICOM.Client.Resource.Object.Domain;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.geom.Point2D;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MapViewPanel extends JPanel implements ObjectResourcePropertiesPane
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel mapLabel = new JLabel();
	private ObjectResourceComboBox mapComboBox = new ObjectResourceComboBox(Map.typ);
	private JLabel domainLabel = new JLabel();
	private ObjectResourceComboBox domainComboBox = new ObjectResourceComboBox(Domain.typ);

	private JLabel longLabel = new JLabel();
	private JTextField longTextField = new JTextField();
	private JLabel latLabel = new JLabel();
	private JTextField latTextField = new JTextField();
	private JLabel scaleLabel = new JLabel();
	private JTextField scaleTextField = new JTextField();

	private JLabel schemesLabel = new JLabel();
	private ObjectResourceListBox schemesList = new ObjectResourceListBox();

	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	MapView view;

	private static MapViewPanel instance = new MapViewPanel();

	private MapViewPanel()
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

	public static ObjectResourcePropertiesPane getInstance()
	{
		return instance;
	}

	private void jbInit()
	{
		this.setLayout(gridBagLayout1);
		this.setName(LangModel.getString("Properties"));

		nameLabel.setText(LangModelMap.getString("Name"));
		nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		mapLabel.setText(LangModelMap.getString("Map"));
		mapLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		domainLabel.setText(LangModelMap.getString("Domain"));
		domainLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		longLabel.setText(LangModelMap.getString("Longitude"));
		longLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		latLabel.setText(LangModelMap.getString("Latitude"));
		latLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		scaleLabel.setText(LangModelMap.getString("Scale"));
		scaleLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		schemesLabel.setText(LangModelMap.getString("Schemes"));
		schemesLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		schemesList.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT * 4));

		descLabel.setText(LangModelMap.getString("Description"));
		descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.add(nameLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(nameTextField, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(mapLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(mapComboBox, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(domainLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(domainComboBox, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(longLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(longTextField, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(latLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(latTextField, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(scaleLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(scaleTextField, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(schemesLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(schemesList, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(Box.createHorizontalStrut(5), com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(descLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(descTextArea, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 8, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));

		mapComboBox.setEnabled(false);
		domainComboBox.setEnabled(false);
	}

	public ObjectResource getObjectResource()
	{
		return null;
	}

	public void setObjectResource(ObjectResource objectResource)
	{
		if(objectResource instanceof VoidMapElement)
		{
			view = ((VoidMapElement )objectResource).getMapView();
		}
		else
			view = (MapView )objectResource;

		if(view == null)
		{
			nameTextField.setEnabled(false);
			nameTextField.setText("");
			descTextArea.setEnabled(false);
			descTextArea.setText("");

			longTextField.setEnabled(false);
			longTextField.setText("");
			latTextField.setEnabled(false);
			latTextField.setText("");

			scaleTextField.setEnabled(false);
			scaleTextField.setText("");
			
			schemesList.removeAll();
		}
		else
		{
			nameTextField.setEnabled(true);
			nameTextField.setText(view.getName());

			mapComboBox.setSelected(view.getMap());
			domainComboBox.setSelected(view.getDomainId());

			descTextArea.setEnabled(true);
			descTextArea.setText(view.getDescription());

			longTextField.setEnabled(true);
			longTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(view.getCenter().x));
			latTextField.setEnabled(true);
			latTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(view.getCenter().y));

			scaleTextField.setEnabled(true);
			scaleTextField.setText(String.valueOf(view.getScale()));
			
			schemesList.setContents(view.getSchemes());
		}
	}

	public void setContext(ApplicationContext aContext)
	{
	}

	public boolean modify()
	{
		try 
		{
			view.setName(nameTextField.getText());
			view.setDescription(descTextArea.getText());

			try 
			{
				double x = Double.parseDouble(longTextField.getText());
				double y = Double.parseDouble(longTextField.getText());
				
				view.setCenter(new Point2D.Double(x, y));

				double s = Double.parseDouble(scaleTextField.getText());
				
				view.setScale(s);
			} 
			catch (NumberFormatException ex) 
			{
				System.out.println(ex.getMessage());
			} 

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
