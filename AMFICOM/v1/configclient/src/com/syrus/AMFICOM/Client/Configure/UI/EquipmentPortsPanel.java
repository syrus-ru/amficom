package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import oracle.jdeveloper.layout.VerticalFlowLayout;

public class EquipmentPortsPanel extends GeneralPanel
{
	Equipment equipment;
	private ObjectResourceComboBox portBox = new ObjectResourceComboBox(Port.typ, false);
//	PortGeneralPanel pgp = new PortGeneralPanel();
	PortPane pgp = new PortPane();
	private JPanel mainPanel = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel controlsPanel = new JPanel();
	private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
	private JPanel labelsPanel = new JPanel();
	private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
	JLabel idLabel = new JLabel();
	private BorderLayout borderLayout2 = new BorderLayout();

	public EquipmentPortsPanel()
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

	public EquipmentPortsPanel(Equipment equipment)
	{
		this();
		setObjectResource(equipment);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_ports"));

		this.setLayout(borderLayout2);

		portBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				portBox_actionPerformed(e);
			}
		});
		mainPanel.setLayout(borderLayout1);
		controlsPanel.setLayout(verticalFlowLayout2);
		labelsPanel.setLayout(verticalFlowLayout1);
		labelsPanel.setMinimumSize(new Dimension(145, 125));
		idLabel.setText(LangModelConfig.getString("label_port"));
		idLabel.setPreferredSize(new Dimension(140, DEF_HEIGHT));
		mainPanel.add(controlsPanel, BorderLayout.CENTER);
		mainPanel.add(labelsPanel, BorderLayout.WEST);
		controlsPanel.add(portBox, null);
		labelsPanel.add(idLabel, null);
		this.add(mainPanel, BorderLayout.NORTH);
		this.add(pgp, BorderLayout.CENTER);
		pgp.setBorder(BorderFactory.createLoweredBevelBorder());
	}

	public void setContext(ApplicationContext aContext)
	{
		super.setContext(aContext);
		pgp.setContext(aContext);
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
			portBox.setContents(equipment.ports.iterator(), false);
			Port cp = (Port )portBox.getSelectedObjectResource();
			pgp.setObjectResource(cp);
		}
	}

	public boolean modify()
	{
		if(portBox.getModel().getSize() > 0)
			return pgp.save();
		return true;
	}

	void portBox_actionPerformed(ActionEvent e)
	{
		pgp.setObjectResource((Port )portBox.getSelectedObjectResource());
	}
}