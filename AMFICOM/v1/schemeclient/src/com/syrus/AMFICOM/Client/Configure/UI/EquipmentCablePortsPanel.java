package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.corba.*;
import oracle.jdeveloper.layout.VerticalFlowLayout;

public class EquipmentCablePortsPanel extends GeneralPanel
{
	protected SchemeElement element;

	private CablePortPane cpgp = new CablePortPane();
	private JPanel mainPanel = new JPanel();
	private JPanel controlsPanel = new JPanel();
	private JPanel labelsPanel = new JPanel();
	private JLabel idLabel = new JLabel();
	private ObjComboBox cportBox;

	protected EquipmentCablePortsPanel()
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

	protected EquipmentCablePortsPanel(SchemeElement element)
	{
		this();
		setObject(element);
	}

	public void setContext(ApplicationContext aContext)
	{
		super.setContext(aContext);
		cpgp.setContext(aContext);
	}

	private void jbInit() throws Exception
	{
		cportBox = new ObjComboBox(
				SchemeCablePortController.getInstance(),
				StorableObjectWrapper.COLUMN_NAME);

		setName(LangModelConfig.getString("label_cableports"));
		this.setLayout(new BorderLayout());

		mainPanel.setLayout(new BorderLayout());
		controlsPanel.setLayout(new VerticalFlowLayout());
		labelsPanel.setLayout(new VerticalFlowLayout());
		labelsPanel.setMinimumSize(new Dimension(145, 125));
		idLabel.setText(LangModelConfig.getString("label_port"));
		idLabel.setPreferredSize(new Dimension(140, DEF_HEIGHT));
		cportBox.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					portBox_actionPerformed(e);
				}
			});
		controlsPanel.add(cportBox, null);
		mainPanel.add(controlsPanel, BorderLayout.CENTER);
		labelsPanel.add(idLabel, null);
		mainPanel.add(labelsPanel, BorderLayout.WEST);
		this.add(mainPanel, BorderLayout.NORTH);
		this.add(cpgp, BorderLayout.CENTER);
		cpgp.setBorder(BorderFactory.createLoweredBevelBorder());
	}

	public Object getObject()
	{
		return element;
	}

	public void setObject(Object or)
	{
		element = (SchemeElement)or;
		cportBox.removeAll();
		cportBox.addElements(SchemeUtils.getCablePorts(element));
		SchemeCablePort cp = (SchemeCablePort)cportBox.getSelectedItem();
		cpgp.setObject(cp);
	}

	public boolean modify()
	{
		if(cportBox.getModel().getSize() > 0)
			return cpgp.save();
		return true;
	}

	void portBox_actionPerformed(ActionEvent e)
	{
		cpgp.setObject(cportBox.getSelectedItem());
	}
}