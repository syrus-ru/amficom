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
import com.syrus.AMFICOM.Client.Resource.MyUtil;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.LinkType;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;

import oracle.jdeveloper.layout.VerticalFlowLayout;

public class LinkTypeGeneralPanel extends GeneralPanel
{
	LinkType linkType;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	JLabel idLabel = new JLabel();
	JTextField idField = new JTextField();

	JLabel nameLabel = new JLabel();
	JTextField nameField = new JTextField();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel manufacturerLabel = new JLabel();
	private JTextField manufacturerField = new JTextField();

	private JLabel manufacturerCodeLabel = new JLabel();
	private JTextField manufacturerCodeField = new JTextField();

	private JLabel modifyLabel2 = new JLabel();
	private JLabel modifyLabel1 = new JLabel();
	private JTextField ModifyField = new JTextField();

	private JLabel descLabel = new JLabel();
	private JPanel descriptionPanel = new JPanel();
	JScrollPane descriptionScrollPane = new JScrollPane();
	public JTextPane descTextArea = new JTextPane();

	private JButton saveButton = new JButton();

	private BorderLayout borderLayout1 = new BorderLayout();

	public LinkTypeGeneralPanel()
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

	public LinkTypeGeneralPanel(LinkType linkType)
	{
		this();
		setObjectResource(linkType);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_general"));

		idLabel.setText(LangModelConfig.getString("label_id"));
		idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		nameLabel.setText(LangModelConfig.getString("label_name"));
		nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));


		descLabel.setText(LangModelConfig.getString("label_description"));
		descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		manufacturerCodeLabel.setText(LangModelConfig.getString("label_manCode"));
		manufacturerCodeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
//		manufacturerField.setEnabled(false);
		manufacturerLabel.setText(LangModelConfig.getString("label_manufacter"));
		manufacturerLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
//		manufacturerCodeField.setEnabled(false);
		ModifyField.setEnabled(false);
		modifyLabel1.setText(LangModelConfig.getString("label_modified1"));
		modifyLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		modifyLabel2.setText(LangModelConfig.getString("label_modified2"));
		modifyLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		idField.setEnabled(false);
		saveButton.setText(LangModelConfig.getString("menuMapSaveText"));
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButton_actionPerformed(e);
			}
		});

		this.setLayout(gridBagLayout1);

		descriptionPanel.setLayout(borderLayout1);
		descriptionScrollPane.getViewport().add(descTextArea, null);
		descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);

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

	public ObjectResource getObjectResource()
	{
		return linkType;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.linkType = (LinkType)or;

		if(linkType != null)
		{
			idField.setText(linkType.getId());
			nameField.setText(linkType.getName());
			this.descTextArea.setText(linkType.description);
			this.manufacturerField.setText(linkType.manufacturer);
			this.manufacturerCodeField.setText(linkType.manufacturer_code);

			this.ModifyField.setText(sdf.format(new Date(linkType.modified)));
		}
		else
		{
			idField.setText("");
			nameField.setText("");
			this.descTextArea.setText("");
			this.manufacturerField.setText("");
			this.manufacturerCodeField.setText("");

			this.ModifyField.setText("");
		}
		return true;

	}

	public boolean modify()
	{
		try
		{
			if(MyUtil.validName(nameField.getText()))
				 linkType.name = nameField.getText();
			else
				return false;

			linkType.id = idField.getText();
//			link.name = nameField.getText();
			linkType.description = this.descTextArea.getText();
			linkType.manufacturer = this.manufacturerField.getText();
			linkType.manufacturer_code = this.manufacturerCodeField.getText();
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
			String[] s = new String[1];
			s[0] = linkType.getId();
			dataSource.SaveLinkTypes(s);
		}
	}

	public boolean delete()
	{
/*		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogTCediting))
			return false;

		String []s = new String[1];

		s[0] = linkType.id;
		aContext.getDataSourceInterface().RemoveLinks(s);*/

		return true;
	}
}