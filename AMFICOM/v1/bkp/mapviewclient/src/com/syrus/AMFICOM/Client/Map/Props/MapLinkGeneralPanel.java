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
import com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.map.IntDimension;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;

public class MapLinkGeneralPanel
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	private JLabel nameLabel = new JLabel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JTextField nameTextField = new JTextField();
	private JLabel typeLabel = new JLabel();
	private ObjComboBox typeComboBox = null;
	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	private JLabel startLabel = new JLabel();
	private ObjComboBox startComboBox = null;
	private JLabel endLabel = new JLabel();
	private ObjComboBox endComboBox = null;

	private JPanel addressPanel = new JPanel();
	private JLabel cityLabel = new JLabel();
	private JLabel streetLabel = new JLabel();
	private JLabel buildingLabel = new JLabel();
	private JTextField cityTextField = new JTextField();
	private JTextField streetTextField = new JTextField();
	private JTextField buildingTextField = new JTextField();
	private JLabel addressLabel = new JLabel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	PhysicalLink link;

	private LogicalNetLayer lnl;

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
		this.startComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);
		this.endComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);

		this.setLayout(this.gridBagLayout1);
		this.setName(LangModel.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.typeLabel.setText(LangModelMap.getString("Type"));
		this.typeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.startLabel.setText(LangModelMap.getString("StartNode"));
		this.startLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.endLabel.setText(LangModelMap.getString("EndNode"));
		this.endLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.addressLabel.setText(LangModelMap.getString("Address"));
		this.addressLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.dimensionLabel.setText(LangModelMap.getString("Dimension"));
		this.dimensionLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.descLabel.setText(LangModelMap.getString("Description"));
		this.descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.addressPanel.setLayout(this.gridBagLayout2);
		this.cityLabel.setText(LangModelMap.getString("City"));
		this.streetLabel.setText(LangModelMap.getString("Street"));
		this.buildingLabel.setText(LangModelMap.getString("building"));

		this.xLabel.setText("X");
		this.mTextField.setPreferredSize(new Dimension(60, 23));
		this.nTextField.setPreferredSize(new Dimension(60, 23));
		this.dimensionPanel.add(this.mTextField, null);
		this.dimensionPanel.add(this.xLabel, null);
		this.dimensionPanel.add(this.nTextField, null);

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
		this.add(this.startLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.startComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.endLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.endComboBox, ReusedGridBagConstraints.get(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.addressLabel, ReusedGridBagConstraints.get(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.addressPanel, ReusedGridBagConstraints.get(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.dimensionLabel, ReusedGridBagConstraints.get(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.dimensionPanel, ReusedGridBagConstraints.get(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.descLabel, ReusedGridBagConstraints.get(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.descTextArea, ReusedGridBagConstraints.get(1, 6, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));

		this.startComboBox.setEnabled(false);
		this.endComboBox.setEnabled(false);
	}

	public Object getObject()
	{
		return null;
	}

	public void setObject(Object objectResource)
	{
		this.link = (PhysicalLink)objectResource;

		this.typeComboBox.removeAllItems();
		this.startComboBox.removeAllItems();
		this.endComboBox.removeAllItems();

		if(this.link == null)
		{
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText("");
			this.typeComboBox.setEnabled(false);
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText("");

			this.mTextField.setText("");
			this.nTextField.setText("");

			this.cityTextField.setText("");
			this.streetTextField.setText("");
			this.buildingTextField.setText("");
		}
		else
		{
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.link.getName());

			List protos = LinkTypeController.getPens(getLogicalNetLayer().getContext());
			
			this.typeComboBox.setEnabled(true);
			this.typeComboBox.addElements(protos);
			this.typeComboBox.setSelectedItem(this.link.getType());

			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.link.getDescription());

			this.startComboBox.addElements(this.link.getMap().getSiteNodes());
			this.startComboBox.setSelectedItem(this.link.getStartNode());
			this.endComboBox.addElements(this.link.getMap().getSiteNodes());
			this.endComboBox.setSelectedItem(this.link.getEndNode());

			this.mTextField.setText(String.valueOf(this.link.getBinding().getDimension().getWidth()));
			this.nTextField.setText(String.valueOf(this.link.getBinding().getDimension().getHeight()));

			this.cityTextField.setText(this.link.getCity());
			this.streetTextField.setText(this.link.getStreet());
			this.buildingTextField.setText(this.link.getBuilding());
		}
	}

	public void setContext(ApplicationContext aContext)
	{
	}

	public boolean modify()
	{
		try 
		{
			this.link.setName(this.nameTextField.getText());
			this.link.setType((PhysicalLinkType )this.typeComboBox.getSelectedItem());
			this.link.setDescription(this.descTextArea.getText());

			this.link.setCity(this.cityTextField.getText());
			this.link.setStreet(this.streetTextField.getText());
			this.link.setBuilding(this.buildingTextField.getText());
			
			int m = Integer.parseInt(this.mTextField.getText());
			int n = Integer.parseInt(this.nTextField.getText());
			if(!this.link.getBinding().getDimension().equals(new IntDimension(m, n)))
				this.link.getBinding().setDimension(new IntDimension(m, n));
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
