package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Object.Domain;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MapPanel extends JPanel implements ObjectResourcePropertiesPane
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel domainLabel = new JLabel();
	private ObjectResourceComboBox domainComboBox = new ObjectResourceComboBox(Domain.typ);

	private JLabel descLabel = new JLabel();
	private JTextArea descTextArea = new JTextArea();

	Map map;

	private static MapPanel instance = new MapPanel();

	private MapPanel()
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

		domainLabel.setText(LangModelMap.getString("Domain"));
		domainLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		descLabel.setText(LangModelMap.getString("Description"));
		descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.add(nameLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(nameTextField, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(domainLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(domainComboBox, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(descLabel, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(descTextArea, com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));

		domainComboBox.setEnabled(false);
	}

	public ObjectResource getObjectResource()
	{
		return null;
	}

	public void setObjectResource(ObjectResource objectResource)
	{
		map = (Map )objectResource;

		if(map == null)
		{
			nameTextField.setEnabled(false);
			nameTextField.setText("");
			descTextArea.setEnabled(false);
			descTextArea.setText("");
		}
		else
		{
			nameTextField.setEnabled(true);
			nameTextField.setText(map.getName());

			domainComboBox.setSelected(map.getDomainId());

			descTextArea.setEnabled(true);
			descTextArea.setText(map.getDescription());
		}
	}

	public void setContext(ApplicationContext aContext)
	{
	}

	public boolean modify()
	{
		try 
		{
			map.setName(nameTextField.getText());
			map.setDescription(descTextArea.getText());

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
