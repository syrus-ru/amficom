package com.syrus.AMFICOM.Client.Configure.UI;

import java.text.SimpleDateFormat;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.Equipment;

public class EquipmentGeneralPanelAdd extends GeneralPanel
{
	Equipment equipment;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel mainPanel = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

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

	public EquipmentGeneralPanelAdd()
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

	public EquipmentGeneralPanelAdd(Equipment equipment)
	{
		this();
		setObjectResource(equipment);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_additional"));

		this.setLayout(borderLayout1);
	 mainPanel.setLayout(gridBagLayout1);

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

	public ObjectResource getObjectResource()
	{
		return equipment;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.equipment = (Equipment )or;

		if(equipment != null)
		{
//			System.out.println("set prop pane to " + equipment.name);

			this.hw_serialField.setText(equipment.hw_serial);
			this.sw_serialField.setText(equipment.sw_serial);
			this.hw_versionField.setText(equipment.hw_version);
			this.sw_versionField.setText(equipment.sw_version);
			this.rnField.setText(equipment.inventory_nr);
			this.manufacturerField.setText(equipment.manufacturer);
			this.manufacturerCodeField.setText(equipment.manufacturer_code);
			this.supplierField.setText(equipment.supplier);
			this.supplierCodeField.setText(equipment.supplier_code);
		}
		else
		{
			this.hw_serialField.setText("");
			this.sw_serialField.setText("");
			this.hw_versionField.setText("");
			this.sw_versionField.setText("");
			this.rnField.setText("");
			this.manufacturerField.setText("");
			this.manufacturerCodeField.setText("");
			this.supplierField.setText("");
			this.supplierCodeField.setText("");
//			imageLabel.setIcon(new ImageIcon());
		}
	}

	public boolean modify()
	{
		try
		{
			equipment.hw_serial = this.hw_serialField.getText();
			equipment.sw_serial = this.sw_serialField.getText();
			equipment.hw_version = this.hw_versionField.getText();
			equipment.sw_version = this.sw_versionField.getText();
			equipment.inventory_nr = this.rnField.getText();
			equipment.manufacturer = this.manufacturerField.getText();
			equipment.manufacturer_code = this.manufacturerCodeField.getText();
			equipment.supplier = this.supplierField.getText();
			equipment.supplier_code = this.supplierCodeField.getText();
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}

	void saveButton_actionPerformed(ActionEvent e)
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogTCediting))
		{
			return;
		}

		if(modify())
		{
			DataSourceInterface dataSource = aContext.getDataSourceInterface();
			dataSource.SaveEquipment(equipment.getId());
		}
	}
}