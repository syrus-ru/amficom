package com.syrus.AMFICOM.Client.Map.Props;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ReusedGridBagConstraints;
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
import com.syrus.AMFICOM.map.Map;

public class MapPanel
		extends JPanel 
		implements ObjectResourcePropertiesPane
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel nameLabel = new JLabel();
	private JTextField nameTextField = new JTextField();
	private JLabel domainLabel = new JLabel();
	private ObjComboBox domainComboBox = null;

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
		SimpleMapElementController controller = 
				SimpleMapElementController.getInstance();

		this.domainComboBox = new ObjComboBox(controller, SimpleMapElementController.KEY_NAME);

		this.setLayout(this.gridBagLayout1);
		this.setName(LangModel.getString("Properties"));

		this.nameLabel.setText(LangModelMap.getString("Name"));
		this.nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.domainLabel.setText(LangModelMap.getString("Domain"));
		this.domainLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.descLabel.setText(LangModelMap.getString("Description"));
		this.descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.add(this.nameLabel, ReusedGridBagConstraints.get(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.nameTextField, ReusedGridBagConstraints.get(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.domainLabel, ReusedGridBagConstraints.get(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.domainComboBox, ReusedGridBagConstraints.get(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, null, 0, 0));
		this.add(this.descLabel, ReusedGridBagConstraints.get(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, null, 0, 0));
		this.add(this.descTextArea, ReusedGridBagConstraints.get(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, null, 0, 0));

		this.domainComboBox.setEnabled(false);
	}

	public Object getObject()
	{
		return null;
	}

	public void setObject(Object objectResource)
	{
		this.map = (Map)objectResource;
		
		this.domainComboBox.removeAllItems();

		if(this.map == null)
		{
			this.nameTextField.setEnabled(false);
			this.nameTextField.setText("");
			this.descTextArea.setEnabled(false);
			this.descTextArea.setText("");
		}
		else
		{
			this.nameTextField.setEnabled(true);
			this.nameTextField.setText(this.map.getName());

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
						this.map.getDomainId(),
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

			this.descTextArea.setEnabled(true);
			this.descTextArea.setText(this.map.getDescription());
		}
	}

	public void setContext(ApplicationContext aContext)
	{
	}

	public boolean modify()
	{
		try 
		{
			this.map.setName(this.nameTextField.getText());
			this.map.setDescription(this.descTextArea.getText());

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
