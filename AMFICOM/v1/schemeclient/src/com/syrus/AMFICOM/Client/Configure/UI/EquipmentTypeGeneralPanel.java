package com.syrus.AMFICOM.Client.Configure.UI;

import java.text.SimpleDateFormat;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.general.ui_.GeneralPanel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.SchemeProtoElement;

public class EquipmentTypeGeneralPanel extends GeneralPanel
{
	protected EquipmentType equipmentType;
	protected SchemeProtoElement proto;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	private JLabel idLabel = new JLabel();
	private JTextField idField = new JTextField();
	private JLabel nameLabel = new JLabel();
	private JTextField nameField = new JTextField();
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
	private JScrollPane descriptionScrollPane = new JScrollPane();
	private JTextPane descTextArea = new JTextPane();
	private JLabel portsNumberLabel = new JLabel();
	private JTextField portsNumberField = new JTextField();
	private JLabel cabelPortsNumberLabel1 = new JLabel();
	private JLabel cabelPortsNumberLabel2 = new JLabel();
	private JTextField cabelPortsNumberField = new JTextField();
	private BorderLayout borderLayout1 = new BorderLayout();

	protected EquipmentTypeGeneralPanel()
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

	protected EquipmentTypeGeneralPanel(EquipmentType eqType)
	{
		this();
		setObject(eqType);
	}

	private void jbInit() throws Exception
	{
		setName("�����");

		idLabel.setText("�������������");
		idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		idField.setEnabled(false);

		nameLabel.setText("��������");
		nameLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		descLabel.setText("��������");
		descLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		manufacturerCodeLabel.setText("��� �������������");
		manufacturerCodeLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		manufacturerField.setEnabled(false);

		manufacturerLabel.setText("�������������");
		manufacturerLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		manufacturerCodeField.setEnabled(false);

		modifyLabel1.setText("�����");
		modifyLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		modifyLabel2.setText("���������� ���������");
		modifyLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		ModifyField.setEnabled(false);

		cabelPortsNumberLabel1.setText("�����");
		cabelPortsNumberLabel1.setPreferredSize(new Dimension(DEF_WIDTH, 10));
		cabelPortsNumberLabel2.setText("��������� ������");
		cabelPortsNumberLabel2.setPreferredSize(new Dimension(DEF_WIDTH, 10));

		portsNumberLabel.setText("����� ������");
		portsNumberLabel.setPreferredSize(new Dimension(DEF_WIDTH, 10));

		this.setLayout(gridBagLayout1);

		descriptionPanel.setLayout(borderLayout1);
		descriptionScrollPane.getViewport().add(descTextArea, null);
		descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);

		this.add(nameLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(manufacturerLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(manufacturerCodeLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(cabelPortsNumberLabel1,      new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(cabelPortsNumberLabel2,      new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(portsNumberLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(modifyLabel1,      new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(modifyLabel2,           new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
		this.add(descLabel,  new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
			this.add(idLabel, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.add(nameField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(manufacturerField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(manufacturerCodeField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(cabelPortsNumberField,       new GridBagConstraints(1, 4, 1, 2, 0.0, 0.0
						,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(portsNumberField, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(ModifyField,       new GridBagConstraints(1, 7, 1, 2, 0.0, 0.0
						,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(descriptionPanel,  new GridBagConstraints(1, 9, 1, 1, 1.0, 1.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	if(Environment.isDebugMode())
			this.add(idField, new GridBagConstraints(1, 10, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	}

	public Object getObject()
	{
		return proto;
	}

	public void setObject(Object or)
	{
		proto = (SchemeProtoElement)or;
		equipmentType = proto.equipmentTypeImpl();

		int portsNumber = SchemeUtils.getPorts(proto).size();
		int cablePortsNumber = SchemeUtils.getCablePorts(proto).size();

		portsNumberField.setText(Long.toString(portsNumber));
		cabelPortsNumberField.setText(Long.toString(cablePortsNumber));

		nameField.setText(proto.name());
		descTextArea.setText(proto.description());
		if (equipmentType != null)
		{
			idField.setText(equipmentType.getId().getIdentifierString());
			ModifyField.setText(sdf.format(equipmentType.getModified()));
		}
		else
		{
			idField.setText("");
			ModifyField.setText("");
		}
	}

	public boolean modify()
	{
		try
		{
			if(MiscUtil.validName(nameField.getText()))
			{
				proto.name(nameField.getText());
				proto.description(descTextArea.getText());
				if (equipmentType != null)
				{
					equipmentType.setName(nameField.getText());
					equipmentType.setDescription(descTextArea.getText());
				}
			}
			else
				return false;
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
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
