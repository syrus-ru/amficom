package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import oracle.jdeveloper.layout.VerticalFlowLayout;

public class EquipmentCablePortsPanel extends GeneralPanel
{
	Equipment equipment;

	CablePortPane cpgp = new CablePortPane();
	private JPanel mainPanel = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel controlsPanel = new JPanel();
	private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
	private JPanel labelsPanel = new JPanel();
	private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
	JLabel idLabel = new JLabel();
	private ObjectResourceComboBox cportBox = new ObjectResourceComboBox(CablePort.typ, false);
	private BorderLayout borderLayout2 = new BorderLayout();

	public EquipmentCablePortsPanel()
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

	public EquipmentCablePortsPanel(Equipment equipment)
	{
		this();
		setObjectResource(equipment);
	}

	public void setContext(ApplicationContext aContext)
	{
		super.setContext(aContext);
		cpgp.setContext(aContext);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_cableports"));
		this.setLayout(borderLayout2);

		mainPanel.setLayout(borderLayout1);
		controlsPanel.setLayout(verticalFlowLayout2);
		labelsPanel.setLayout(verticalFlowLayout1);
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

	public ObjectResource getObjectResource()
	{
		return equipment;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.equipment = (Equipment )or;

		if(equipment != null)
		{
			this.cportBox.setContents(equipment.cports.iterator(), false);
			CablePort cp = (CablePort )cportBox.getSelectedObjectResource();
			cpgp.setObjectResource(cp);
		}
	}

	public boolean modify()
	{
		if(cportBox.getModel().getSize() > 0)
			return cpgp.save();
		return true;
	}

	void portBox_actionPerformed(ActionEvent e)
	{
		cpgp.setObjectResource((CablePort )cportBox.getSelectedObjectResource());
	}
}