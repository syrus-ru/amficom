package com.syrus.AMFICOM.Client.Configure.UI;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.KIS;
import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.Object.Domain;


public class KISGeneralPanel extends GeneralPanel
{
	KIS equipment;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	private JButton saveButton = new JButton();

	JLabel nameLabel = new JLabel();
	JTextField nameField = new JTextField();

	private JLabel portsNumberLabel = new JLabel();
	private JTextField portsNumberField = new JTextField();

	private JLabel domainLabel = new JLabel();
	private ObjectResourceComboBox domainBox = new ObjectResourceComboBox(Domain.typ, true);

	JLabel longitudeLabel = new JLabel();
	private JTextField longitudeField = new JTextField();
	private JLabel latitudeLabel = new JLabel();
	private JTextField latitudeField = new JTextField();

	JLabel idLabel = new JLabel();
	JTextField idField = new JTextField();

	private JLabel modifyLabel1 = new JLabel();
	private JLabel modifyLabel2 = new JLabel();
	private JTextField modifyField = new JTextField();

	private JLabel cabelPortsNumberLabel1 = new JLabel();
	private JLabel cabelPortsNumberLabel2 = new JLabel();
	private JTextField cabelPortsNumberField = new JTextField();

	JLabel typeLabel = new JLabel();
	ObjectResourceComboBox typeBox = new ObjectResourceComboBox(EquipmentType.typ, true);

	private JLabel agentLabel = new JLabel();
	private JTextField agentField = new JTextField();

	private BorderLayout borderLayout1 = new BorderLayout();
	private BorderLayout borderLayout4 = new BorderLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel descLabel = new JLabel();
	JScrollPane descriptionScrollPane = new JScrollPane();
	JPanel descriptionPanel = new JPanel();
	public JTextPane descTextArea = new JTextPane();

	public KISGeneralPanel()
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

	public KISGeneralPanel(KIS equipment)
	{
	 this();
	 setObjectResource(equipment);
	}

	private void jbInit() throws Exception
	{
	 setName(LangModelConfig.getString("menuNetCatEquipmentText"));

	 this.setLayout(gridBagLayout1);

	 saveButton.setText(LangModelConfig.getString("menuMapSaveText"));
	 saveButton.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(ActionEvent e) {
			saveButton_actionPerformed(e);
		}
	 });
	 latitudeLabel.setText(LangModelConfig.getString("equip_latitude"));
	 latitudeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

	 longitudeLabel.setText(LangModelConfig.getString("equip_longitude"));
	 longitudeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

	 nameLabel.setText(LangModelConfig.getString("label_name"));
	 nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

	 portsNumberLabel.setText(LangModelConfig.getString("equip_portsNumber"));
	 portsNumberLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 portsNumberField.setEnabled(false);

	 domainLabel.setText(LangModelConfig.getString("equip_domen"));
	 domainLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 domainBox.setEnabled(false);

	 modifyLabel1.setText(LangModelConfig.getString("label_modified1"));
	 modifyLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
	 modifyLabel2.setText(LangModelConfig.getString("label_modified2"));
	 modifyLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
	 modifyField.setEnabled(false);

	 typeLabel.setText(LangModelConfig.getString("label_type"));
	 typeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 typeBox.setEnabled(false);

	 idLabel.setText(LangModelConfig.getString("label_id"));
	 idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 idField.setEnabled(false);

	 cabelPortsNumberLabel1.setText(LangModelConfig.getString("equip_cablePortsNumber1"));
	 cabelPortsNumberLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
	 cabelPortsNumberLabel2.setText(LangModelConfig.getString("equip_cablePortsNumber2"));
	 cabelPortsNumberLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
	 cabelPortsNumberField.setEnabled(false);

	 agentLabel.setText(LangModelConfig.getString("equip_agent"));
	 agentLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

	 descLabel.setText(LangModelConfig.getString("label_description"));
	 descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

	 descriptionPanel.setLayout(borderLayout1);
	 descriptionScrollPane.getViewport().add(descTextArea, null);
	 descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);

	 this.add(nameLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(typeLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(longitudeLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(latitudeLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(domainLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(modifyLabel1,      new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
	 this.add(modifyLabel2,           new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
	 this.add(portsNumberLabel, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(cabelPortsNumberLabel1,      new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
	 this.add(cabelPortsNumberLabel2,           new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
	 this.add(agentLabel, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(descLabel,  new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
		 this.add(idLabel, new GridBagConstraints(0, 13, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	 this.add(nameField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(typeBox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(longitudeField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(latitudeField, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(domainBox, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(modifyField,       new GridBagConstraints(1, 6, 1, 2, 0.0, 0.0
				,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(portsNumberField, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(cabelPortsNumberField, new GridBagConstraints(1, 9, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(agentField, new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(descriptionPanel,  new GridBagConstraints(1, 12, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
		 this.add(idField, new GridBagConstraints(1, 13, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

//		this.add(saveButton, new XYConstraints(200, 380, -1, -1));
	}

	public ObjectResource getObjectResource()
	{
	 return equipment;
	}

	public void setObjectResource(ObjectResource or)
	{
	 this.equipment = (KIS )or;

	 if(equipment != null)
	 {
		idField.setText(equipment.getId());
		nameField.setText(equipment.getName());
		this.descTextArea.setText(equipment.description);
		typeBox.setSelected(equipment.type_id);

		double d1 = 0.0;
		try { d1 = Double.parseDouble(equipment.longitude); } catch(Exception ex) { }
		d1 = MiscUtil.fourdigits(d1);
		this.longitudeField.setText(String.valueOf(d1));
		double d2 = 0.0;
		try { d2 = Double.parseDouble(equipment.latitude); } catch(Exception ex) { }
		d2 = MiscUtil.fourdigits(d1);
		this.latitudeField.setText(String.valueOf(d2));

//			this.longitudeField.setText(equipment.longitude);
//			this.latitudeField.setText(equipment.latitude);

		if(equipment instanceof KIS)
		{
			this.agentField.setEnabled(true);
			this.agentField.setText(equipment.agent_id);
		}
		else
		if(equipment instanceof Equipment)
		{
			this.agentField.setEnabled(false);
			this.agentField.setText("");
		}
		this.domainBox.setSelected(equipment.domain_id);
		this.modifyField.setText(sdf.format(new Date(equipment.modified)));
		this.portsNumberField.setText(Long.toString(equipment.ports.size()));
		this.cabelPortsNumberField.setText(Long.toString(equipment.cports.size()));
	 }
	 else
	 {
		idField.setText("");
		nameField.setText("");
		this.descTextArea.setText("");
		typeBox.setSelected("");
		this.longitudeField.setText("");
		this.latitudeField.setText("");
		this.agentField.setText("");
		this.domainBox.setSelected("");
		this.modifyField.setText("");
		this.portsNumberField.setText("");
		this.cabelPortsNumberField.setText("");
	}
	}

	public boolean modify()
	{
	 try
	 {
		try
		{
			double d1 = Double.parseDouble(this.longitudeField.getText());
			d1 = MiscUtil.fourdigits(d1);
			double d2 = Double.parseDouble(this.latitudeField.getText());
			d2 = MiscUtil.fourdigits(d2);
			equipment.longitude = String.valueOf(d1);
			equipment.latitude = String.valueOf(d2);
		}
		catch (Exception ex)
		{
			System.out.println("Unknown coordinates' format!");
			return false;
		}

		if(MiscUtil.validName(nameField.getText()))
			equipment.name = nameField.getText();
		else
			return false;

		equipment.id = idField.getText();
//			equipment.name = nameField.getText();
		equipment.description = this.descTextArea.getText();
		equipment.type_id = (String )typeBox.getSelected();
//			equipment.longitude = this.longitudeField.getText();
//			equipment.latitude = this.latitudeField.getText();
		equipment.agent_id = this.agentField.getText();
		equipment.domain_id = (String )this.domainBox.getSelected();
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
			Checker.catalogCMediting))
	 {
		return;
	 }

	 if(modify())
	 {
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		dataSource.SaveKIS(equipment.getId());
	 }
	}
}