package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;

public class EquipmentGeneralPanelAdd extends GeneralPanel
{
	protected SchemeElement element;

	private JPanel mainPanel = new JPanel();
	private JLabel sw_versionLabel1 = new JLabel();
	private JLabel sw_versionLabel2 = new JLabel();
	private JTextField sw_versionField = new JTextField();
	private JLabel rnLabel1 = new JLabel();
	private JLabel rnLabel2 = new JLabel();
	private JTextField rnField = new JTextField();
	private JLabel sw_serialLabel1 = new JLabel();
	private JLabel sw_serialLabel2 = new JLabel();
	private JTextField sw_serialField = new JTextField();
	private JLabel hw_serialLabel1 = new JLabel();
	private JLabel hw_serialLabel2 = new JLabel();
	private JTextField hw_serialField = new JTextField();
	private JLabel supplierLabel = new JLabel();
	private JTextField supplierField = new JTextField();
	private JLabel supplierCodeLabel = new JLabel();
	private JTextField supplierCodeField = new JTextField();
	private JLabel hw_versionLabel1 = new JLabel();
	private JLabel hw_versionLabel2 = new JLabel();
	private JTextField hw_versionField = new JTextField();
	private JLabel manufacturerCodeLabel = new JLabel();
	private JTextField manufacturerCodeField = new JTextField();
	private JLabel manufacturerLabel = new JLabel();
	private JTextField manufacturerField = new JTextField();

	protected EquipmentGeneralPanelAdd()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected EquipmentGeneralPanelAdd(SchemeElement element)
	{
		this();
		setObject(element);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_additional"));

		this.setLayout(new BorderLayout());
		mainPanel.setLayout(new GridBagLayout());

		manufacturerLabel.setText(LangModelConfig.getString("label_manufacter"));
		manufacturerLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		sw_versionLabel1.setText(LangModelConfig.getString("equip_hVer1"));
		sw_versionLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		sw_versionLabel2.setText(LangModelConfig.getString("equip_soft2"));
		sw_versionLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		rnLabel1.setText(LangModelConfig.getString("label_inventory_nr1"));
		rnLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		rnLabel2.setText(LangModelConfig.getString("label_inventory_nr2"));
		rnLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));

		hw_serialLabel1.setText(LangModelConfig.getString("equip_SN1"));
		hw_serialLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		hw_serialLabel2.setText(LangModelConfig.getString("equip_2"));
		hw_serialLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));

		supplierCodeLabel.setText(LangModelConfig.getString("label_supCode"));
		supplierCodeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		hw_versionLabel1.setText(LangModelConfig.getString("equip_hVer1"));
		hw_versionLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		hw_versionLabel2.setText(LangModelConfig.getString("equip_2"));
		hw_versionLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		sw_serialLabel1.setText(LangModelConfig.getString("equip_SN1"));
		sw_serialLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		sw_serialLabel2.setText(LangModelConfig.getString("equip_soft2"));
		sw_serialLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		manufacturerCodeLabel.setText(LangModelConfig.getString("label_manCode"));
		manufacturerCodeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		supplierLabel.setText(LangModelConfig.getString("label_supplier"));
		supplierLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		manufacturerCodeField.setEnabled(false);
		manufacturerField.setEnabled(false);


	 mainPanel.add(hw_serialLabel1,      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
	 mainPanel.add(hw_serialLabel2,           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));

	 mainPanel.add(sw_serialLabel1,      new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
	 mainPanel.add(sw_serialLabel2,           new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));

	 mainPanel.add(hw_versionLabel1,      new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
	 mainPanel.add(hw_versionLabel2,           new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));

	 mainPanel.add(sw_versionLabel1,      new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
	 mainPanel.add(sw_versionLabel2,           new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));

	 mainPanel.add(rnLabel1,      new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
	 mainPanel.add(rnLabel2,           new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));

	 mainPanel.add(manufacturerLabel, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 mainPanel.add(manufacturerCodeLabel, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 mainPanel.add(supplierLabel, new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 mainPanel.add(supplierCodeLabel, new GridBagConstraints(0, 13, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));


	 mainPanel.add(hw_serialField, new GridBagConstraints(1, 0, 1, 2, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 mainPanel.add(sw_serialField, new GridBagConstraints(1, 2, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 mainPanel.add(hw_versionField, new GridBagConstraints(1, 4, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 mainPanel.add(sw_versionField, new GridBagConstraints(1, 6, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 mainPanel.add(rnField, new GridBagConstraints(1, 8, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 mainPanel.add(manufacturerField, new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 mainPanel.add(manufacturerCodeField,       new GridBagConstraints(1, 11, 1, 2, 0.0, 0.0
				,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 mainPanel.add(supplierField, new GridBagConstraints(1, 12, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 mainPanel.add(supplierCodeField, new GridBagConstraints(1, 13, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

	 this.add(mainPanel,BorderLayout.NORTH);
	}

	public Object getObject()
	{
		return element;
	}

	public void setObject(Object or)
	{
		element = (SchemeElement)or;

		if(element.equipmentType() != null)
		{
			manufacturerField.setText(element.equipmentTypeImpl().getManufacturer());
			manufacturerCodeField.setText(element.equipmentTypeImpl().getManufacturerCode());
		}
		else
		{
			manufacturerField.setText("");
			manufacturerCodeField.setText("");
		}

		if(element.equipment() != null)
		{
			hw_serialField.setText(element.equipmentImpl().getHwSerial());
			sw_serialField.setText(element.equipmentImpl().getSwSerial());
			hw_versionField.setText(element.equipmentImpl().getHwVersion());
			sw_versionField.setText(element.equipmentImpl().getSwVersion());
			rnField.setText(element.equipmentImpl().getInventoryNumber());
			supplierField.setText(element.equipmentImpl().getSupplier());
			supplierCodeField.setText(element.equipmentImpl().getSupplierCode());
		}
		else
		{
			hw_serialField.setText("");
			sw_serialField.setText("");
			hw_versionField.setText("");
			sw_versionField.setText("");
			rnField.setText("");
			supplierField.setText("");
			supplierCodeField.setText("");
		}
	}

	public boolean modify()
	{
		try
		{
			if (element.equipmentType() != null)
			{
				element.equipmentTypeImpl().setManufacturer(manufacturerField.getText());
				element.equipmentTypeImpl().setManufacturerCode(manufacturerCodeField.getText());
			}

			if (element.equipment() != null)
			{
				element.equipmentImpl().setHwSerial(hw_serialField.getText());
				element.equipmentImpl().setHwSerial(sw_serialField.getText());
				element.equipmentImpl().setHwVersion(hw_versionField.getText());
				element.equipmentImpl().setSwVersion(sw_versionField.getText());
				element.equipmentImpl().setInventoryNumber(rnField.getText());
				element.equipmentImpl().setSupplier(supplierField.getText());
				element.equipmentImpl().setSupplierCode(supplierCodeField.getText());
			}
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}

	void saveButton_actionPerformed(ActionEvent e)
	{
		if(modify())
		{
			if (element.equipment() != null)
			{
				try {
					ConfigurationStorableObjectPool.putStorableObject(element.equipmentImpl());
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}