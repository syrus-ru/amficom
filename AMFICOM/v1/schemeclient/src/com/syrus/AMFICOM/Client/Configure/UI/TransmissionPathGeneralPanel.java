package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;

import java.util.*;
import java.text.SimpleDateFormat;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;
import com.syrus.AMFICOM.Client.Resource.ISM.KIS;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.MessageBox;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;

public class TransmissionPathGeneralPanel extends GeneralPanel
{
	SchemePath path;
	MonitoredElement me;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	JLabel equipLabel = new JLabel();
	ObjectResourceComboBox equipBox = new ObjectResourceComboBox(KIS.typ, true);

	JLabel idLabel = new JLabel();
	JTextField idField = new JTextField();

	JLabel local_addLabel1 = new JLabel();
	private JLabel local_addLabel2 = new JLabel();
	JTextField localAdressField = new JTextField();

	JLabel nameLabel = new JLabel();
	JTextField nameField = new JTextField();

	private ObjectResourceComboBox portBox = new ObjectResourceComboBox(MeasurementPort.typ, true);
	private JLabel portLabel = new JLabel();

	private JTextField modifyField = new JTextField();
	private JLabel modifyLabel1 = new JLabel();
	private JLabel modifyLabel2 = new JLabel();

	private JLabel descLabel = new JLabel();
	private JPanel descriptionPanel = new JPanel();
	JScrollPane descriptionScrollPane = new JScrollPane();
	public JTextPane descTextArea = new JTextPane();
	private BorderLayout borderLayout1 = new BorderLayout();

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
	}

	public TransmissionPathGeneralPanel(TransmissionPath tp)
	{
		this();
		setObjectResource(tp);
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
		equipBox.setEnabled(false);
		portBox.setEnabled(false);

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
		this.add(equipBox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(portBox, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
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

	public ObjectResource getObjectResource()
	{
		return path;
	}

	public void setObjectResource(ObjectResource or)
	{
		path = (SchemePath)or;

		idField.setText(path.getId());
		nameField.setText(path.getName());
		equipBox.setSelected(path.startDeviceId);

		if(path.path != null)
		{
			Map melements = Pool.getMap(MonitoredElement.typ);
			for (Iterator it = melements.values().iterator(); it.hasNext(); )
			{
				MonitoredElement me = (MonitoredElement) it.next();
				if (me.elementType.equals("path") && me.elementId.equals(path.path.getId()))
				{
					this.me = me;
					break;
				}
			}
			descTextArea.setText(path.path.description);
			modifyField.setText(sdf.format(new Date(path.path.modified)));
		}
		else
			modifyField.setText("");

		if (me != null)
		{
			portBox.setSelected(me.measurementPortId);
			localAdressField.setText(me.localAddress);
		}
		else
		{
			portBox.setSelected("");
			localAdressField.setText("");
		}
	}

	public boolean modify()
	{
		try
		{
			if(MiscUtil.validName(nameField.getText()))
				path.name = nameField.getText();
			else
				return false;

			if (path.path != null)
			{
				path.path.name = nameField.getText();
				path.path.description = descTextArea.getText();
				path.startDeviceId = (String)equipBox.getSelected();
			}
			if (me != null)
			{
				me.measurementPortId = (String)portBox.getSelected();
				me.localAddress = localAdressField.getText();
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
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogCMediting))
		{
			return false;
		}

		if(modify())
		{
			if (path.path != null)
			{
				DataSourceInterface dataSource = aContext.getDataSourceInterface();
				dataSource.SavePath(path.path.getId());
				return true;
			}
		}
		else
		{
			new MessageBox("Неправильно введены данные").show();
		}
		return false;
	}

	public boolean delete()
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogCMediting))
			return false;

		if (path.path != null)
		{
			String []s = new String[1];
			s[0] = path.path.getId();
			aContext.getDataSourceInterface().RemovePaths(s);
		}

		return true;
	}

	void saveButton_actionPerformed(ActionEvent e)
	{
	}
}