package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Dimension;

import oracle.jdeveloper.layout.VerticalFlowLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.BorderFactory;

import java.util.Date;
import java.text.SimpleDateFormat;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.MyUtil;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Network.CablePort;
import com.syrus.AMFICOM.Client.Resource.Network.Equipment;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

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
		setName(LangModelConfig.String("label_cableports"));
		this.setLayout(borderLayout2);

		mainPanel.setLayout(borderLayout1);
		controlsPanel.setLayout(verticalFlowLayout2);
		labelsPanel.setLayout(verticalFlowLayout1);
		labelsPanel.setMinimumSize(new Dimension(145, 125));
		idLabel.setText(LangModelConfig.String("label_port"));
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

	public boolean setObjectResource(ObjectResource or)
	{
		this.equipment = (Equipment )or;

		if(equipment != null)
		{
			this.cportBox.setContents(equipment.cports.elements(), false);
			CablePort cp = (CablePort )cportBox.getSelectedObjectResource();
			cpgp.setObjectResource(cp);
		}
		return true;
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