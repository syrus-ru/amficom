package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import oracle.jdeveloper.layout.VerticalFlowLayout;

public class EquipmentPortsPanel extends GeneralPanel
{
	SchemeElement element;

	private ObjectResourceComboBox portBox = new ObjectResourceComboBox(SchemePort.typ, false);
//	PortGeneralPanel pgp = new PortGeneralPanel();
	PortPane pgp = new PortPane();
	private JPanel mainPanel = new JPanel();
	private JPanel controlsPanel = new JPanel();
	private JPanel labelsPanel = new JPanel();
	JLabel idLabel = new JLabel();

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

	public EquipmentPortsPanel(SchemeElement element)
	{
		this();
		setObjectResource(element);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_ports"));

		this.setLayout(new BorderLayout());

		portBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				portBox_actionPerformed(e);
			}
		});
		mainPanel.setLayout(new BorderLayout());
		controlsPanel.setLayout(new VerticalFlowLayout());
		labelsPanel.setLayout(new VerticalFlowLayout());
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
		return element;
	}

	public void setObjectResource(ObjectResource or)
	{
		element = (SchemeElement)element;
		portBox.setContents(element.getPorts().iterator(), false);

		SchemePort port = (SchemePort)portBox.getSelectedObjectResource();
		pgp.setObjectResource(port);
	}

	public boolean modify()
	{
		if(portBox.getModel().getSize() > 0)
			return pgp.save();
		return true;
	}

	void portBox_actionPerformed(ActionEvent e)
	{
		pgp.setObjectResource((SchemePort)portBox.getSelectedObjectResource());
	}
}