package com.syrus.AMFICOM.Client.Map.Props;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.UI.SimpleMapElementController;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.mapview.CablePath;

public class MapCablePathGeneralPanel
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	private JLabel nameLabel = new JLabel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JTextField nameTextField = new JTextField();
	private JLabel cableLabel = new JLabel();
	private ObjComboBox cableComboBox = null;
	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	CablePath path;

	private LogicalNetLayer lnl;

	public MapCablePathGeneralPanel()
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

		this.cableComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);

		this.setLayout(this.gridBagLayout1);
		this.setName(LangModel.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.cableLabel.setText(LangModelMap.getString("cable"));
		this.cableLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.descLabel.setText(LangModelMap.getString("Description"));
		this.descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		
		this.cableComboBox.setEditable(false);

		this.add(this.nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.nameTextField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.cableLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.cableComboBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.descLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.descTextArea, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	public Object getObject()
	{
		return null;
	}

	public void setObject(Object objectResource)
	{
		this.path = (CablePath)objectResource;
		
		this.cableComboBox.removeAllItems();
		
		if(this.path == null)
		{
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText("");
			this.cableComboBox.setEnabled(false);
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText("");
		}
		else
		{
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.path.getName());
			this.cableComboBox.setEnabled(true);
			this.cableComboBox.addItem(this.path.getSchemeCableLink());
			this.cableComboBox.setSelectedItem(this.path.getSchemeCableLink());
			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.path.getDescription());
		}
	}

	public void setContext(ApplicationContext aContext)
	{
	}

	public boolean modify()
	{
		try 
		{
			this.path.setName(this.nameTextField.getText());
			this.path.setDescription(this.descTextArea.getText());
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
