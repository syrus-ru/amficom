package com.syrus.AMFICOM.Client.Configure.UI;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.MyUtil;
import com.syrus.AMFICOM.Client.Resource.Network.Link;
import com.syrus.AMFICOM.Client.Resource.Network.Port;
import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CableLinkType;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.event.ActionEvent;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import java.awt.*;
import java.awt.GridBagConstraints;

public class LinkGeneralPanel extends GeneralPanel
{
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	Link cl;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	JLabel typeLabel = new JLabel();
	ObjectResourceComboBox typeBox = new ObjectResourceComboBox(CableLinkType.typ, true);

	JLabel rnLabel1 = new JLabel();
	private JLabel rnLabel2 = new JLabel();
	JTextField rnField = new JTextField();

	JLabel nameLabel = new JLabel();
	JTextField nameField = new JTextField();

	private JLabel manufacturerLabel = new JLabel();
	private JLabel manufacturerCodeLabel = new JLabel();
	private JTextField manufacturerField = new JTextField();
	private JTextField manufacturerCodeField = new JTextField();

	private JLabel supplierLabel = new JLabel();
	private JLabel supplierCodeLabel = new JLabel();

	private JTextField supplierField = new JTextField();
	private JTextField supplierCodeField = new JTextField();

	private JLabel start_equipmentLabel = new JLabel();
	private JLabel start_equipmentPortLabel1 = new JLabel();
	private JLabel start_equipmentPortLabel2 = new JLabel();
	private ObjectResourceComboBox startEquipmentPortBox = new ObjectResourceComboBox(Port.typ, true);
	private ObjectResourceComboBox startEquipmentBox = new ObjectResourceComboBox("kisequipment", true);//Equipment.typ, true);

	private JLabel end_equipmentLabel = new JLabel();
	private JLabel end_equipmentPortLabel2 = new JLabel();
	private JLabel end_equipmentPortLabel1 = new JLabel();
	private ObjectResourceComboBox endEquipmentBox = new ObjectResourceComboBox("kisequipment", true);//Equipment.typ, true);
	private ObjectResourceComboBox endEquipmentPortBox = new ObjectResourceComboBox(Port.typ, true);

	private JLabel modifyLabel2 = new JLabel();
	private JLabel modifyLabel1 = new JLabel();
	private JTextField modifyField = new JTextField();

	private BorderLayout borderLayout1 = new BorderLayout();
	private JLabel descLabel = new JLabel();
	JPanel descriptionPanel = new JPanel();
	JScrollPane descriptionScrollPane = new JScrollPane();
	public JTextPane descTextArea = new JTextPane();

	private JPanel mainPanel = new JPanel();
	private BorderLayout borderLayout3 = new BorderLayout();

	JTextField idField = new JTextField();
	JLabel idLabel = new JLabel();

	private JLabel optLabel = new JLabel();
	private JLabel physLabel = new JLabel();
	private JTextField optLengthField = new JTextField();
	private JTextField physLengthField = new JTextField();

	public LinkGeneralPanel()
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

	public LinkGeneralPanel(Link cl)
	{
	 this();
	 setObjectResource(cl);
	}

	private void jbInit() throws Exception
	{
	 typeLabel.setText(LangModelConfig.getString("label_type"));
	 typeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 rnLabel1.setText(LangModelConfig.getString("label_inventory_nr1"));
	 rnLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
	 rnLabel2.setText(LangModelConfig.getString("label_inventory_nr2"));
	 rnLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
	 nameLabel.setText(LangModelConfig.getString("label_name"));
	 nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 manufacturerLabel.setText(LangModelConfig.getString("label_manufacter"));
	 manufacturerLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 manufacturerCodeLabel.setText(LangModelConfig.getString("label_manCode"));
	 manufacturerCodeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 manufacturerField.setEnabled(false);
	 manufacturerCodeField.setEnabled(false);
	 supplierLabel.setText(LangModelConfig.getString("label_supplier"));
	 supplierLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 supplierCodeLabel.setText(LangModelConfig.getString("label_supCode"));
	 supplierCodeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 start_equipmentLabel.setText(LangModelConfig.getString("link_start_equipment_id"));
	 start_equipmentLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 start_equipmentPortLabel1.setText(LangModelConfig.getString("label_port"));
	 start_equipmentPortLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
	 start_equipmentPortLabel2.setText(LangModelConfig.getString("link_start_equipment_id1"));
	 start_equipmentPortLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
	 end_equipmentLabel.setText(LangModelConfig.getString("link_end_equipment_id"));
	 end_equipmentLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 end_equipmentPortLabel1.setText(LangModelConfig.getString("label_port"));
	 end_equipmentPortLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
	 end_equipmentPortLabel2.setText(LangModelConfig.getString("link_end_equipment_id1"));
	 end_equipmentPortLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
	 modifyLabel1.setText(LangModelConfig.getString("label_modified1"));
	 modifyLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
	 modifyLabel2.setText(LangModelConfig.getString("label_modified2"));
	 modifyLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
	 modifyField.setEnabled(false);

	 descLabel.setText(LangModelConfig.getString("label_description"));
	 descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

	 idLabel.setText(LangModelConfig.getString("label_id"));
	 idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

	 idField.setEnabled(false);
	 optLabel.setText(LangModelConfig.getString("link_optical_length"));
	 optLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 physLabel.setText(LangModelConfig.getString("link_physical_length"));
	 physLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 typeBox.setEnabled(false);
	 startEquipmentBox.setEnabled(false);
	 startEquipmentPortBox.setEnabled(false);
	 endEquipmentBox.setEnabled(false);
	 endEquipmentPortBox.setEnabled(false);

	descriptionPanel.setLayout(borderLayout1);
	descriptionScrollPane.getViewport().add(descTextArea, null);
	descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);

	 this.setLayout(gridBagLayout1);

	 this.add(typeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(nameLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(physLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(optLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(rnLabel1,      new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(rnLabel2,           new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(manufacturerLabel, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(manufacturerCodeLabel, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(supplierLabel, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(supplierCodeLabel, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(start_equipmentLabel, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(start_equipmentPortLabel1,      new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(start_equipmentPortLabel2,           new GridBagConstraints(0, 13, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(end_equipmentLabel, new GridBagConstraints(0, 14, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(end_equipmentPortLabel1,      new GridBagConstraints(0, 15, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(end_equipmentPortLabel2,           new GridBagConstraints(0, 16, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(modifyLabel1,      new GridBagConstraints(0, 17, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(modifyLabel2,           new GridBagConstraints(0, 18, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(descLabel,   new GridBagConstraints(0, 19, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
		 mainPanel.add(idLabel, new GridBagConstraints(0, 20, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	 this.add(typeBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(nameField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(physLengthField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(optLengthField, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(rnField,      new GridBagConstraints(1, 5, 1, 2, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(manufacturerField, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(manufacturerCodeField, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(supplierField, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(supplierCodeField, new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(startEquipmentBox, new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(startEquipmentPortBox,       new GridBagConstraints(1, 12, 1, 2, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(endEquipmentBox, new GridBagConstraints(1, 14, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(endEquipmentPortBox,      new GridBagConstraints(1, 15, 1, 2, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(modifyField,      new GridBagConstraints(1, 17, 1, 2, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(descriptionPanel,  new GridBagConstraints(1, 19, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
		 mainPanel.add(idField, new GridBagConstraints(1, 20, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

//		this.add(saveButton, BorderLayout.SOUTH);
	}

	public ObjectResource getObjectResource()
	{
	 return cl;
	}

	public void setObjectResource(ObjectResource or)
	{
	 this.cl = (Link) or;

	 if(cl != null)
	 {
//			System.out.println("set prop pane to " + cl.name);

		this.typeBox.setSelected(cl.type_id);
		this.idField.setText(cl.getId());
		this.nameField.setText(cl.getName());
		this.descTextArea.setText(cl.description);
		this.rnField.setText(cl.inventory_nr);
		this.manufacturerField.setText(cl.manufacturer);
		this.manufacturerCodeField.setText(cl.manufacturer_code);
		this.supplierField.setText(cl.supplier);
		this.supplierCodeField.setText(cl.supplier_code);

		this.physLengthField.setText(String.valueOf(cl.physical_length));
		this.optLengthField.setText(String.valueOf(cl.optical_length));

		this.startEquipmentBox.setSelected(cl.start_equipment_id);
		Equipment se = (Equipment )Pool.get("kisequipment", cl.start_equipment_id);
		if (se != null)
			this.startEquipmentPortBox.setContents(se.cports.iterator(), true);
		this.startEquipmentPortBox.setSelected(cl.start_port_id);

		this.endEquipmentBox.setSelected(cl.end_equipment_id);
		Equipment ee = (Equipment )Pool.get("kisequipment", cl.end_equipment_id);
		if (ee != null)
			this.endEquipmentBox.setContents(ee.cports.iterator(), true);
		this.endEquipmentPortBox.setSelected(cl.end_port_id);
		this.modifyField.setText(sdf.format(new Date(cl.modified)));
/*
		DataSet ds = new DataSet(cl.threads.elements());
		ObjectResourceSorter sorter = CableLinkThread.getDefaultSorter();
		sorter.setDataSet(ds);
		ds = sorter.default_sort();
		this.LinksList.setContents(ds.elements());
*/
	 }
	 else
	 {
		this.typeBox.setSelected("");
		this.idField.setText("");
		this.nameField.setText("");
		this.descTextArea.setText("");
		this.rnField.setText("");
		this.manufacturerField.setText("");
		this.manufacturerCodeField.setText("");
		this.supplierField.setText("");
		this.supplierCodeField.setText("");
		this.startEquipmentBox.setSelected("");
		this.startEquipmentPortBox.setSelected("");
		this.endEquipmentBox.setSelected("");
		this.endEquipmentPortBox.setSelected("");
		this.modifyField.setText("");

		this.physLengthField.setText("");
		this.optLengthField.setText("");

/*
		this.LinksList.setContents("");
		this.linksIdField.setText("");
		this.linksNameField.setText("");
		this.linksMarkLabel.setText("");
		this.linksTypeBox.setSelected("");
*/
//			imageLabel.setIcon(new ImageIcon());
	 }
	}

	public boolean modify()
	{
	 try
	 {
		try
		{
			double d1 = Double.parseDouble(physLengthField.getText());
			double d2 = Double.parseDouble(optLengthField.getText());

			cl.physical_length = d1;
			cl.optical_length = d2;
		}
		catch(Exception ex)
		{
			System.out.println("Incorrect length format!");
			return false;
		}

		if(MyUtil.validName(nameField.getText()))
			cl.name = nameField.getText();
		else
			return false;

		cl.type_id = (String )this.typeBox.getSelected();
		cl.id = this.idField.getText();
//			cl.name = this.nameField.getText();
		cl.description = this.descTextArea.getText();
		cl.inventory_nr = this.rnField.getText();
		cl.manufacturer = this.manufacturerField.getText();
		cl.manufacturer_code = this.manufacturerCodeField.getText();
		cl.supplier = this.supplierField.getText();
		cl.supplier_code = this.supplierCodeField.getText();
		cl.start_equipment_id = (String )this.startEquipmentBox.getSelected();
		cl.start_port_id = (String )this.startEquipmentPortBox.getSelected();
		cl.end_equipment_id = (String )this.endEquipmentBox.getSelected();
		cl.end_port_id = (String )this.endEquipmentPortBox.getSelected();
/*
		clt.id = this.linksIdField.getText();
		clt.name = this.linksNameField.getText();
		clt.mark = this.linksMarkLabel.getText();
		clt.link_type_id = (String )this.linksTypeBox.getSelected();
*/
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
		dataSource.SaveCableLink(cl.getId());
	 }
	}

	public boolean delete()
	{
	 if(!Checker.checkCommandByUserId(
			aContext.getSessionInterface().getUserId(),
			Checker.catalogTCediting))
		return false;

	 String []s = new String[1];

	 s[0] = cl.id;
	 aContext.getDataSourceInterface().RemoveCableLinks(s);

	 return true;
	}
}