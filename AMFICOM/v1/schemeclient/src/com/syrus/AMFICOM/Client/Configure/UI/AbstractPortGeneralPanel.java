package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.Client.General.*;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePort;

public class AbstractPortGeneralPanel extends GeneralPanel
{
	AbstractSchemePort port;

	JLabel descLabel = new JLabel();
	JScrollPane descriptionScrollPane = new JScrollPane();
	public JTextPane descTextArea = new JTextPane();

	JLabel idLabel = new JLabel();
	JTextField idField = new JTextField();

	JLabel nameLabel = new JLabel();
	JTextField nameField = new JTextField();

	JLabel typeLabel = new JLabel();
	ObjComboBox typeBox;

	private JLabel equipLabel = new JLabel();
	private JTextField equipField = new JTextField();

	private JTextField modifyField = new JTextField();
	private JLabel modifyLabel2 = new JLabel();
	private JLabel modifyLabel1 = new JLabel();

	public AbstractPortGeneralPanel()
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

	public AbstractPortGeneralPanel(AbstractSchemePort port)
	{
		this();
		setObject(port);
	}

	private void jbInit() throws Exception
	{
		Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
		Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
				domain_id, true);
		DomainCondition condition = new DomainCondition(domain, ObjectEntities.PORTTYPE_ENTITY_CODE);
		typeBox = new ObjComboBox(
					PortTypeController.getInstance(),
					ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true),
					PortTypeController.KEY_NAME);

		this.setLayout(new GridBagLayout());

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
		equipField.setEnabled(false);

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
		this.add(equipField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(modifyField,       new GridBagConstraints(1, 4, 1, 2, 0.0, 0.0
				,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(descriptionScrollPane,  new GridBagConstraints(1, 6, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
			this.add(idField, new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

//		this.add(saveButton,    new XYConstraints(200, 213, -1, -1));
	}

	public Object getObject()
	{
		return port;
	}

	public void setObject(Object or)
	{
		port = (AbstractSchemePort)port;

		idField.setText(port.id().identifierString());
		nameField.setText(port.name());

		typeBox.setSelectedItem(port.portType());
		descTextArea.setText(port.description());

		if(port.port() != null)
		{
			Port cp = port.portImpl();
			equipField.setText(cp.getEquipmentId().getIdentifierString());
		}
		else
		{
			typeBox.setSelectedItem("");
			descTextArea.setText("");
			equipField.setText("");
		}
	}

	public boolean modify()
	{
		try
		{
			if(MiscUtil.validName(nameField.getText()))
				port.name(nameField.getText());
			else
				return false;

			port.portTypeImpl((PortType)typeBox.getSelectedItem());
			port.description(descTextArea.getText());
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
			try {
				ConfigurationStorableObjectPool.putStorableObject(port.portImpl());
			}
			catch (ApplicationException ex) {
			}
		}
	}
}