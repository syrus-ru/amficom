package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import oracle.jdeveloper.layout.VerticalFlowLayout;

public class EquipmentCablePortsPanel extends GeneralPanel
{
	SchemeElement element;

	CablePortPane cpgp = new CablePortPane();
	private JPanel mainPanel = new JPanel();
	private JPanel controlsPanel = new JPanel();
	private JPanel labelsPanel = new JPanel();
	JLabel idLabel = new JLabel();
	private ObjectResourceComboBox cportBox = new ObjectResourceComboBox(SchemeCablePort.typ, false);

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

	public EquipmentCablePortsPanel(SchemeElement element)
	{
		this();
		setObjectResource(element);
	}

	public void setContext(ApplicationContext aContext)
	{
		super.setContext(aContext);
		cpgp.setContext(aContext);
	}

	private void jbInit() throws Exception
	{
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

	public ObjectResource getObjectResource()
	{
		return element;
	}

	public void setObjectResource(ObjectResource or)
	{
		element = (SchemeElement)or;
		cportBox.setContents(element.getCablePorts().iterator(), false);
		SchemeCablePort cp = (SchemeCablePort)cportBox.getSelectedObjectResource();
		cpgp.setObjectResource(cp);
	}

	public boolean modify()
	{
		if(cportBox.getModel().getSize() > 0)
			return cpgp.save();
		return true;
	}

	void portBox_actionPerformed(ActionEvent e)
	{
		cpgp.setObjectResource((SchemeCablePort )cportBox.getSelectedObjectResource());
	}
}