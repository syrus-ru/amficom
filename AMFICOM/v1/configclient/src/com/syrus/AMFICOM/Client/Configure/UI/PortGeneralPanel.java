package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;

import java.util.Date;
import java.text.SimpleDateFormat;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Network.Port;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.PortType;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

public class PortGeneralPanel extends GeneralPanel
{
	Port cp;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	JLabel descLabel = new JLabel();
	private BorderLayout borderLayout1 = new BorderLayout();
	JScrollPane descriptionScrollPane = new JScrollPane();
	public JTextPane descTextArea = new JTextPane();

	JLabel idLabel = new JLabel();
	JTextField idField = new JTextField();

	JLabel nameLabel = new JLabel();
	JTextField nameField = new JTextField();

	JLabel typeLabel = new JLabel();
	ObjectResourceComboBox typeBox = new ObjectResourceComboBox(PortType.typ, true);

	private JLabel equipLabel = new JLabel();
	private ObjectResourceComboBox equipBox = new ObjectResourceComboBox("kisequipment", true);

	private JTextField modifyField = new JTextField();
	private JLabel modifyLabel2 = new JLabel();
	private JLabel modifyLabel1 = new JLabel();

	public PortGeneralPanel()
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

	public PortGeneralPanel(Port cp)
	{
	 this();
	 setObjectResource(cp);
	}

	private void jbInit() throws Exception
	{
	 this.setLayout(gridBagLayout1);

	 descLabel.setText(LangModelConfig.getString("label_description"));
	 descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

	 idLabel.setText(LangModelConfig.getString("label_id"));
	 idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 idField.setEnabled(false);

	 nameLabel.setText(LangModelConfig.getString("label_name"));
	 nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

	 typeLabel.setText(LangModelConfig.getString("label_type"));
	 typeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 typeBox.setEnabled(false);

	 equipLabel.setText(LangModelConfig.getString("menuNetCatEquipmentText"));
	 equipLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	 equipBox.setEnabled(false);

	 modifyField.setEnabled(false);
	 modifyLabel1.setText(LangModelConfig.getString("label_modified1"));
	 modifyLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
	 modifyLabel2.setText(LangModelConfig.getString("label_modified2"));
	 modifyLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));

	 descriptionScrollPane.getViewport().add(descTextArea, null);

	 this.add(nameLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(typeLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(equipLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(modifyLabel1,      new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
	 this.add(modifyLabel2,           new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
	 this.add(descLabel,  new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
		 this.add(idLabel, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	 this.add(nameField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(typeBox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(equipBox, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(modifyField,       new GridBagConstraints(1, 4, 1, 2, 0.0, 0.0
				,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	 this.add(descriptionScrollPane,  new GridBagConstraints(1, 6, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
		 this.add(idField, new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

//		this.add(saveButton,    new XYConstraints(200, 213, -1, -1));
	}

	public ObjectResource getObjectResource()
	{
	 return cp;
	}

	public void setObjectResource(ObjectResource or)
	{
		cp = (Port)or;

		if(cp != null)
		{
			typeBox.setSelected(cp.type_id);
			idField.setText(cp.getId());
			nameField.setText(cp.getName());
			this.descTextArea.setText(cp.description);
			this.equipBox.setSelected(cp.equipment_id);
		}
		else
		{
			nameField.setText("");
			idField.setText("");
			typeBox.setSelected("");
			this.descTextArea.setText("");
			this.equipBox.setSelected("");
		}
	}

	public boolean modify()
	{
		try
		{
			if(MiscUtil.validName(nameField.getText()))
				cp.name = nameField.getText();
			else
				return false;
			cp.type_id = (String )typeBox.getSelected();
			cp.id = idField.getText();
			cp.description = this.descTextArea.getText();
			cp.equipment_id = (String )this.equipBox.getSelected();
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
		dataSource.SaveCablePort(cp.getId());
	 }
	}
}