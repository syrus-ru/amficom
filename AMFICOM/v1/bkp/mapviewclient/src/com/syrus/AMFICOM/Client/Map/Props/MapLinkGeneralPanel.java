package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Map.Controllers.LinkTypeController;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.IntDimension;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;

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
		return lnl;
	}

	private void jbInit()
	{
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		typeComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);
		startComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);
		endComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);

		this.setLayout(gridBagLayout1);
		this.setName(LangModel.getString("Properties"));

		nameLabel.setText(LangModelMap.getString("Name"));
		nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		typeLabel.setText(LangModelMap.getString("Type"));
		typeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		startLabel.setText(LangModelMap.getString("StartNode"));
		startLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		endLabel.setText(LangModelMap.getString("EndNode"));
		endLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		addressLabel.setText(LangModelMap.getString("Address"));
		addressLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		dimensionLabel.setText(LangModelMap.getString("Dimension"));
		dimensionLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		descLabel.setText(LangModelMap.getString("Description"));
		descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		addressPanel.setLayout(gridBagLayout2);
		cityLabel.setText(LangModelMap.getString("City"));
		streetLabel.setText(LangModelMap.getString("Street"));
		buildingLabel.setText(LangModelMap.getString("building"));

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
		this.add(startLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(startComboBox, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(endLabel, ReusedGridBagConstraints.get(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(endComboBox, ReusedGridBagConstraints.get(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(addressLabel, ReusedGridBagConstraints.get(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(addressPanel, ReusedGridBagConstraints.get(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(dimensionLabel, ReusedGridBagConstraints.get(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(dimensionPanel, ReusedGridBagConstraints.get(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, null, 0, 0));
		this.add(descLabel, ReusedGridBagConstraints.get(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(descTextArea, ReusedGridBagConstraints.get(1, 6, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));

//		startComboBox.setEnabled(false);
//		endComboBox.setEnabled(false);
	}

	public Object getObject()
	{
		return null;
	}

	public void setObject(Object objectResource)
	{
		link = (PhysicalLink)objectResource;

		typeComboBox.removeAll();

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

			List protos = LinkTypeController.getPens(getLogicalNetLayer().getContext());
			
			typeComboBox.setEnabled(true);
			typeComboBox.addElements(protos);
			typeComboBox.getModel().setSelectedItem(link.getType());

			descTextArea.setEnabled(true);
			descTextArea.setText(link.getDescription());

			startComboBox.addElements(link.getMap().getSiteNodes());
			startComboBox.setSelectedItem(link.getStartNode());
			endComboBox.addElements(link.getMap().getSiteNodes());
			endComboBox.setSelectedItem(link.getEndNode());

			mTextField.setText(String.valueOf(link.getBinding().getDimension().getWidth()));
			nTextField.setText(String.valueOf(link.getBinding().getDimension().getHeight()));

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
			link.setType((PhysicalLinkType )typeComboBox.getSelectedItem());
			link.setDescription(descTextArea.getText());

			link.setCity(cityTextField.getText());
			link.setStreet(streetTextField.getText());
			link.setBuilding(buildingTextField.getText());
			
			int m = Integer.parseInt(mTextField.getText());
			int n = Integer.parseInt(nTextField.getText());
			if(!link.getBinding().getDimension().equals(new IntDimension(m, n)))
				link.getBinding().setDimension(new IntDimension(m, n));
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
