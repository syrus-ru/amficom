package com.syrus.AMFICOM.Client.Map.Props;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceListBox;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainCondition;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MapViewStorableObjectPool;
import com.syrus.AMFICOM.mapview.VoidElement;

public class MapViewPanel
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel mapLabel = new JLabel();
	private ObjComboBox mapComboBox = null;
	private JLabel domainLabel = new JLabel();
	private ObjComboBox domainComboBox = null;

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
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.domainComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);
		this.mapComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);

		this.setLayout(this.gridBagLayout1);
		this.setName(LangModel.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.mapLabel.setText(LangModelMap.getString("Map"));
		this.mapLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.domainLabel.setText(LangModelMap.getString("Domain"));
		this.domainLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.longLabel.setText(LangModelMap.getString("Longitude"));
		this.longLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.latLabel.setText(LangModelMap.getString("Latitude"));
		this.latLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.scaleLabel.setText(LangModelMap.getString("Scale"));
		this.scaleLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.schemesLabel.setText(LangModelMap.getString("Schemes"));
		this.schemesLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.schemesList.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT * 4));

		this.descLabel.setText(LangModelMap.getString("Description"));
		this.descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.add(this.nameLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.nameTextField, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.mapLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.mapComboBox, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.domainLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.domainComboBox, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.longLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.longTextField, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.latLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.latTextField, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.scaleLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.scaleTextField, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.schemesLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.schemesList, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(Box.createHorizontalStrut(5), com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.descLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.descTextArea, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 8, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));

		this.mapComboBox.setEnabled(false);
		this.domainComboBox.setEnabled(false);
	}

	public Object getObject()
	{
		return null;
	}

	public void setObject(Object objectResource)
	{
		if(objectResource instanceof VoidElement)
		{
			this.view = ((VoidElement)objectResource).getMapView();
		}
		else
			this.view = (MapView )objectResource;

		this.domainComboBox.removeAllItems();
		this.mapComboBox.removeAllItems();

		if(this.view == null)
		{
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText("");
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText("");

			this.longTextField.setEnabled(false);
			this.longTextField.setText("");
			this.latTextField.setEnabled(false);
			this.latTextField.setText("");

			this.scaleTextField.setEnabled(false);
			this.scaleTextField.setText("");
			
			this.schemesList.removeAll();
		}
		else
		{
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.view.getName());

			Domain domain = null;
			List domains = null;

			StorableObjectCondition condition = 
				new DomainCondition(null, ObjectEntities.DOMAIN_ENTITY_CODE);
			try
			{
				domains = AdministrationStorableObjectPool.getStorableObjectsByCondition(
						condition,
						true);
			}
			catch (ApplicationException e)
			{
				e.printStackTrace();
			}

			try
			{
				domain = (Domain )AdministrationStorableObjectPool.getStorableObject(
						this.view.getDomainId(),
						false);
			}
			catch (CommunicationException e)
			{
				e.printStackTrace();
			}
			catch (DatabaseException e)
			{
				e.printStackTrace();
			}

			this.domainComboBox.addElements(domains);
			this.domainComboBox.setSelectedItem(domain);

			List maps = null;

			StorableObjectCondition domainCondition = 
				new DomainCondition(domain, ObjectEntities.MAP_ENTITY_CODE);
			try
			{
				maps = MapViewStorableObjectPool.getStorableObjectsByCondition(
						domainCondition,
						true);
			}
			catch (ApplicationException e)
			{
				e.printStackTrace();
			}
			this.mapComboBox.addElements(maps);
			this.mapComboBox.setSelectedItem(this.view.getMap());

			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.view.getDescription());

			this.longTextField.setEnabled(true);
			this.longTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(this.view.getCenter().getX()));
			this.latTextField.setEnabled(true);
			this.latTextField.setText(MapPropertiesManager.getCoordinatesFormat().format(this.view.getCenter().getY()));

			this.scaleTextField.setEnabled(true);
			this.scaleTextField.setText(String.valueOf(this.view.getScale()));
			
			this.schemesList.setContents(this.view.getSchemes());
		}
	}

	public void setContext(ApplicationContext aContext)
	{
	}

	public boolean modify()
	{
		try 
		{
			this.view.setName(this.nameTextField.getText());
			this.view.setDescription(this.descTextArea.getText());

			try 
			{
				double x = Double.parseDouble(this.longTextField.getText());
				double y = Double.parseDouble(this.longTextField.getText());
				
				this.view.setCenter(new DoublePoint(x, y));

				double s = Double.parseDouble(this.scaleTextField.getText());
				
				this.view.setScale(s);
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

	public LogicalNetLayer getLogicalNetLayer()
	{
		return null;
	}

	public void setLogicalNetLayer(LogicalNetLayer lnl)
	{
	}
}
