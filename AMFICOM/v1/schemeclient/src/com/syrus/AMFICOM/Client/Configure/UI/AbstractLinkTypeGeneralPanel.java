package com.syrus.AMFICOM.Client.Configure.UI;

import java.text.SimpleDateFormat;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.ApplicationException;

public class AbstractLinkTypeGeneralPanel extends GeneralPanel
{
	protected AbstractLinkType linkType;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss"); //$NON-NLS-1$

	private JLabel idLabel = new JLabel();
	private JTextField idField = new JTextField();
	private JLabel nameLabel = new JLabel();
	private JTextField nameField = new JTextField();
	private JLabel manufacturerLabel = new JLabel();
	private JTextField manufacturerField = new JTextField();
	private JLabel manufacturerCodeLabel = new JLabel();
	private JTextField manufacturerCodeField = new JTextField();
	private JLabel modifyLabel2 = new JLabel();
	private JLabel modifyLabel1 = new JLabel();
	private JTextField ModifyField = new JTextField();
	private JLabel descLabel = new JLabel();
	private JPanel descriptionPanel = new JPanel();
	private JScrollPane descriptionScrollPane = new JScrollPane();
	private JTextPane descTextArea = new JTextPane();
	private JButton saveButton = new JButton();

	protected AbstractLinkTypeGeneralPanel()
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

	protected AbstractLinkTypeGeneralPanel(LinkType linkType)
	{
		this();
		setObject(linkType);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_general")); //$NON-NLS-1$

		idLabel.setText(LangModelConfig.getString("label_id")); //$NON-NLS-1$
		idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		nameLabel.setText(LangModelConfig.getString("label_name")); //$NON-NLS-1$
		nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));


		descLabel.setText(LangModelConfig.getString("label_description")); //$NON-NLS-1$
		descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		manufacturerCodeLabel.setText(LangModelConfig.getString("label_manCode")); //$NON-NLS-1$
		manufacturerCodeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		manufacturerLabel.setText(LangModelConfig.getString("label_manufacter")); //$NON-NLS-1$
		manufacturerLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		ModifyField.setEnabled(false);
		modifyLabel1.setText(LangModelConfig.getString("label_modified1")); //$NON-NLS-1$
		modifyLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		modifyLabel2.setText(LangModelConfig.getString("label_modified2")); //$NON-NLS-1$
		modifyLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		idField.setEnabled(false);
		saveButton.setText(LangModelConfig.getString("menuMapSaveText")); //$NON-NLS-1$
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButton_actionPerformed(e);
			}
		});

		descriptionPanel.setLayout(new BorderLayout());
		descriptionScrollPane.getViewport().add(descTextArea, null);
		descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);

		this.setLayout(new GridBagLayout());

		this.add(nameLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(manufacturerLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(manufacturerCodeLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(modifyLabel1,      new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(modifyLabel2,           new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(descLabel,  new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
			this.add(idLabel, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.add(nameField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(manufacturerField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(manufacturerCodeField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(ModifyField,       new GridBagConstraints(1, 4, 1, 2, 0.0, 0.0
						,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(descriptionPanel,  new GridBagConstraints(1, 6, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
			this.add(idField, new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	}

	public Object getObject()
	{
		return linkType;
	}

	public void setObject(Object or)
	{
		this.linkType = (AbstractLinkType)or;

		if(linkType != null)
		{
			idField.setText(linkType.getId().getIdentifierString());
			nameField.setText(linkType.getName());
			this.descTextArea.setText(linkType.getDescription());
			this.manufacturerField.setText(linkType.getManufacturer());
			this.manufacturerCodeField.setText(linkType.getManufacturerCode());

			this.ModifyField.setText(sdf.format(linkType.getModified()));
		}
		else
		{
			idField.setText(""); //$NON-NLS-1$
			nameField.setText(""); //$NON-NLS-1$
			this.descTextArea.setText(""); //$NON-NLS-1$
			this.manufacturerField.setText(""); //$NON-NLS-1$
			this.manufacturerCodeField.setText(""); //$NON-NLS-1$

			this.ModifyField.setText(""); //$NON-NLS-1$
		}
	}

	public boolean modify()
	{
		try
		{
			if(MiscUtil.validName(nameField.getText()))
				 linkType.setName(nameField.getText());
			else
				return false;

			linkType.setDescription(this.descTextArea.getText());
			linkType.setManufacturer(this.manufacturerField.getText());
			linkType.setManufacturerCode(this.manufacturerCodeField.getText());
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
				ConfigurationStorableObjectPool.putStorableObject(linkType);
			}
			catch (ApplicationException ex) {
			}
		}
	}

	public boolean delete()
	{
		return true;
	}
}
