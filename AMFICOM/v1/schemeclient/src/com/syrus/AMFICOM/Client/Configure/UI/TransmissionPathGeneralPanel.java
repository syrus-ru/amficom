package com.syrus.AMFICOM.Client.Configure.UI;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.administration.*;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.SchemePath;

public class TransmissionPathGeneralPanel extends GeneralPanel
{
	SchemePath path;
	MonitoredElement me;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	JLabel equipLabel = new JLabel();
	JTextField equipField = new JTextField();

	JLabel idLabel = new JLabel();
	JTextField idField = new JTextField();

	JLabel local_addLabel1 = new JLabel();
	private JLabel local_addLabel2 = new JLabel();
	JTextField localAdressField = new JTextField();

	JLabel nameLabel = new JLabel();
	JTextField nameField = new JTextField();

	private JTextField portField = new JTextField();
	private JLabel portLabel = new JLabel();

	private JTextField modifyField = new JTextField();
	private JLabel modifyLabel1 = new JLabel();
	private JLabel modifyLabel2 = new JLabel();

	private JLabel descLabel = new JLabel();
	private JPanel descriptionPanel = new JPanel();
	JScrollPane descriptionScrollPane = new JScrollPane();
	public JTextPane descTextArea = new JTextPane();
	private BorderLayout borderLayout1 = new BorderLayout();

	Domain domain;

	public TransmissionPathGeneralPanel()
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

		try {
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
					getAccessIdentifier().domain_id);
			domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
					domain_id, true);
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}
	}

	public TransmissionPathGeneralPanel(TransmissionPath tp)
	{
		this();
		setObject(tp);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(gridBagLayout1);

		equipLabel.setText(LangModelConfig.getString("menuNetCatEquipmentText"));
		equipLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		idLabel.setText(LangModelConfig.getString("label_id"));
		idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		nameLabel.setText(LangModelConfig.getString("label_name"));
		nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));


		descLabel.setText(LangModelConfig.getString("label_description"));
		descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		portLabel.setText(LangModelConfig.getString("label_port"));
		portLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		local_addLabel1.setText(LangModelConfig.getString("label_local_addr1"));
		local_addLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		local_addLabel2.setText(LangModelConfig.getString("label_local_addr2"));
		local_addLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		modifyField.setEnabled(false);
		modifyLabel1.setText(LangModelConfig.getString("label_modified1"));
		modifyLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		modifyLabel2.setText(LangModelConfig.getString("label_modified2"));
		modifyLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		descriptionPanel.setLayout(borderLayout1);

		idField.setEnabled(false);
		equipField.setEnabled(false);
		portField.setEnabled(false);

		descriptionPanel.setLayout(borderLayout1);
		descriptionScrollPane.getViewport().add(descTextArea, null);
		descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);

		this.add(nameLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(equipLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(portLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(local_addLabel1,      new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(local_addLabel2,           new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(modifyLabel1,      new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(modifyLabel2,           new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
				,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(descLabel,  new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
			this.add(idLabel, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.add(nameField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(equipField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(portField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(localAdressField,       new GridBagConstraints(1, 4, 1, 2, 0.0, 0.0
				,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(modifyField,       new GridBagConstraints(1, 6, 1, 2, 0.0, 0.0
				,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(descriptionPanel,  new GridBagConstraints(1, 8, 1, 1, 1.0, 1.0
				,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		if(Environment.isDebugMode())
			this.add(idField, new GridBagConstraints(1, 9, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

//		this.add(linksLabel,  new XYConstraints(5, 290, 135, 20));
//		this.add(LinksTable,    new XYConstraints(150, 290, 315, 120));
//		this.add(saveButton,    new XYConstraints(200, 424, -1, -1));

//		localAdressField.setEnabled(false);
	}

	public Object getObject()
	{
		return path;
	}

	public void setObject(Object or)
	{
		path = (SchemePath)or;

		idField.setText(path.id().identifierString());
		nameField.setText(path.name());
		equipField.setText(path.startDevice().name());

		if(path.path() != null)
		{
			StorableObjectCondition condition = new DomainCondition(domain, ObjectEntities.ME_ENTITY_CODE);
				try {
					List mes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);
					for (Iterator it = mes.iterator(); it.hasNext(); ) {
						MonitoredElement me = (MonitoredElement)it.next();
						if (me.getMonitoredDomainMemberIds().contains(path.pathImpl().getId())) {
							this.me = me;
							break;
						}
					}
			}
			catch (ApplicationException ex) {
				ex.printStackTrace();
			}
			descTextArea.setText(path.pathImpl().getDescription());
			modifyField.setText(sdf.format(path.pathImpl().getModified()));
		}
		else
			modifyField.setText("");

		if (me != null) {
			portField.setText(me.getMeasurementPortId().getIdentifierString());
			localAdressField.setText(me.getLocalAddress());
		}
		else {
			portField.setText("");
			localAdressField.setText("");
		}
}

	public boolean modify()
	{
		try
		{
			if(MiscUtil.validName(nameField.getText()))
				path.name(nameField.getText());
			else
				return false;

			if (path.path() != null)
			{
				path.pathImpl().setDescription(descTextArea.getText());
			}
			if (me != null)
			{
				me.setLocalAddress(localAdressField.getText());
			}
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}

	public boolean save()
	{
		if(modify())
		{
			if (path.path() != null)
			{
				try {
					ConfigurationStorableObjectPool.putStorableObject(path.pathImpl());
				}
				catch (ApplicationException ex) {
				}
				return true;
			}
		}
		JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				"Неправильно введены данные");
		return false;
	}

	public boolean delete()
	{
		return false;
	}

	void saveButton_actionPerformed(ActionEvent e)
	{
	}
}