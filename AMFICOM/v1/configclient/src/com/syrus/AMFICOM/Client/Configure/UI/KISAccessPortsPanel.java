package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Dimension;

import oracle.jdeveloper.layout.VerticalFlowLayout;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.Resource.ISM.KIS;
import com.syrus.AMFICOM.Client.Resource.ISM.AccessPort;

import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

public class KISAccessPortsPanel extends GeneralPanel
{
	KIS equipment;
	JLabel idLabel = new JLabel();
	private ObjectResourceComboBox portBox = new ObjectResourceComboBox(AccessPort.typ, false);
	AccessPortGeneralPanel pgp = new AccessPortGeneralPanel();
	private JPanel mainPanel = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel controlsPanel = new JPanel();
	private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
	private JPanel labelsPanel = new JPanel();
	private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
	private BorderLayout borderLayout2 = new BorderLayout();

	public KISAccessPortsPanel()
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

	public KISAccessPortsPanel(KIS equipment)
	{
		this();
		setObjectResource(equipment);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_accessports"));

		this.setLayout(borderLayout2);

		idLabel.setText(LangModelConfig.getString("label_accessport"));
		idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		portBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				portBox_actionPerformed(e);
			}
		});
		mainPanel.setLayout(borderLayout1);
		controlsPanel.setLayout(verticalFlowLayout2);
		labelsPanel.setLayout(verticalFlowLayout1);
		labelsPanel.setMinimumSize(new Dimension(DEF_WIDTH + 5, 125));
		mainPanel.add(controlsPanel, BorderLayout.CENTER);
		mainPanel.add(labelsPanel, BorderLayout.WEST);
		controlsPanel.add(portBox, null);
		labelsPanel.add(idLabel, null);
		this.add(mainPanel, BorderLayout.NORTH);
		this.add(pgp, BorderLayout.CENTER);
		pgp.setBorder(BorderFactory.createLoweredBevelBorder());
	}

	public ObjectResource getObjectResource()
	{
		return equipment;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.equipment = (KIS )or;

		if(equipment != null)
		{
			this.portBox.setContents(equipment.access_ports.elements(), false);
			AccessPort cp = (AccessPort )portBox.getSelectedObjectResource();
			pgp.setObjectResource(cp);
		}
		return true;
	}

	void portBox_actionPerformed(ActionEvent e)
	{
		pgp.setObjectResource((AccessPort )portBox.getSelectedObjectResource());
	}
}