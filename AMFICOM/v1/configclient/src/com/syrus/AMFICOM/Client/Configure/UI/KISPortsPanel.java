package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import java.util.Enumeration;

import oracle.jdeveloper.layout.VerticalFlowLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.MyUtil;

import com.syrus.AMFICOM.Client.Resource.Network.Port;
import com.syrus.AMFICOM.Client.Resource.ISM.KIS;
import com.syrus.AMFICOM.Client.Resource.ISM.AccessPort;

import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

public class KISPortsPanel extends GeneralPanel
{
	KIS equipment;
	JLabel idLabel = new JLabel();
	private ObjectResourceComboBox portBox = new ObjectResourceComboBox(Port.typ, false);
//	PortGeneralPanel pgp = new PortGeneralPanel();
//	AccessPortGeneralPanel apgp = new AccessPortGeneralPanel();
	private JPanel topPanel = new JPanel();
	private JPanel mainPanel = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel controlsPanel = new JPanel();
	private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
	private JPanel labelsPanel = new JPanel();
	private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
	private BorderLayout borderLayout2 = new BorderLayout();
	PortPane pgp = new PortPane();
	private JPanel bottomPanel = new JPanel();
	private JPanel mainPanel1 = new JPanel();
	private JPanel controlsPanel1 = new JPanel();
	private VerticalFlowLayout verticalFlowLayout3 = new VerticalFlowLayout();
	private JPanel labelsPanel1 = new JPanel();
	private VerticalFlowLayout verticalFlowLayout4 = new VerticalFlowLayout();
	private JLabel jLabel1 = new JLabel();
	private JCheckBox accessPortCheckBox = new JCheckBox();
	AccessPortPane apgp = new AccessPortPane();
	private BorderLayout borderLayout4 = new BorderLayout();
	private BorderLayout borderLayout5 = new BorderLayout();
	private FlowLayout flowLayout1 = new FlowLayout();

	public KISPortsPanel()
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

	public KISPortsPanel(KIS equipment)
	{
		this();
		setObjectResource(equipment);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_ports"));

		this.setLayout(borderLayout5);

		idLabel.setText(LangModelConfig.getString("label_port"));
		idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		portBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				portBox_actionPerformed(e);
			}
		});
		topPanel.setLayout(borderLayout2);
		mainPanel.setLayout(borderLayout1);
		controlsPanel.setLayout(verticalFlowLayout2);
		labelsPanel.setLayout(verticalFlowLayout1);
		labelsPanel.setMinimumSize(new Dimension(DEF_WIDTH + 5, 125));
		pgp.setBorder(BorderFactory.createLoweredBevelBorder());
		bottomPanel.setLayout(borderLayout4);
		mainPanel1.setLayout(flowLayout1);
		controlsPanel1.setLayout(verticalFlowLayout3);
		labelsPanel1.setLayout(verticalFlowLayout4);
		labelsPanel1.setMinimumSize(new Dimension(DEF_WIDTH + 5, 125));
		jLabel1.setText(LangModelConfig.getString("label_evalport"));
		accessPortCheckBox.setEnabled(false);
		apgp.setEnabled(false);
		apgp.setBorder(BorderFactory.createLoweredBevelBorder());
		flowLayout1.setAlignment(0);
		controlsPanel1.add(accessPortCheckBox, null);
		labelsPanel1.add(jLabel1, null);
		mainPanel1.add(controlsPanel1, null);
		mainPanel1.add(labelsPanel1, null);
		mainPanel.add(controlsPanel, BorderLayout.CENTER);
		mainPanel.add(labelsPanel, BorderLayout.WEST);
		controlsPanel.add(portBox, null);
		labelsPanel.add(idLabel, null);
		topPanel.add(mainPanel, BorderLayout.NORTH);
		topPanel.add(pgp, BorderLayout.CENTER);
		bottomPanel.add(apgp, BorderLayout.CENTER);
		bottomPanel.add(mainPanel1, BorderLayout.NORTH);
		this.add(bottomPanel, BorderLayout.CENTER);
		this.add(topPanel, BorderLayout.NORTH);
	}

	public void setContext(ApplicationContext aContext)
	{
		super.setContext(aContext);
		pgp.setContext(aContext);
		apgp.setContext(aContext);
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
			this.portBox.setContents(equipment.ports.elements(), false);
			Port prt = (Port )portBox.getSelectedObjectResource();
			pgp.setObjectResource(prt);
			accessPortCheckBox.setSelected(false);
			apgp.setObjectResource(null);
			set_AP_Selected(false);

			for(Enumeration enum = equipment.access_ports.elements(); enum.hasMoreElements();)
			{
				AccessPort ap = (AccessPort )enum.nextElement();
				if (ap != null && prt != null)
				{
					if(ap.port_id.equals(prt.getId()))
					{
						set_AP_Selected(true);
						accessPortCheckBox.setSelected(true);
						apgp.setObjectResource(ap);
					}
				}
			}
		}
		return true;
	}

	public boolean modify()
	{
		boolean ret = true;
		if(portBox.getModel().getSize() > 0)
			ret = pgp.save();
		if(!ret)
			return false;
		if(apgp.isEnabled())
			return apgp.save();
		return true;
	}

	void portBox_actionPerformed(ActionEvent e)
	{
		Port prt = (Port )portBox.getSelectedObjectResource();
		pgp.setObjectResource(prt);

		accessPortCheckBox.setSelected(false);
		apgp.setObjectResource(null);
		set_AP_Selected(false);

		for(Enumeration enum = equipment.access_ports.elements(); enum.hasMoreElements();)
		{
			AccessPort ap = (AccessPort )enum.nextElement();
			if (ap != null && prt != null)
			{
				if(ap.port_id.equals(prt.getId()))
				{
					set_AP_Selected(true);
					accessPortCheckBox.setSelected(true);
					apgp.setObjectResource(ap);
				}
			}
		}
	}

	void set_AP_Selected(boolean bool)
	{
		/*apgp.idLabel.setEnabled(bool);
		apgp.portLabel.setEnabled(bool);
		apgp.nameLabel.setEnabled(bool);
		apgp.typeLabel.setEnabled(bool);
		apgp.kisLabel.setEnabled(bool);
		apgp.localLabel.setEnabled(bool);
		apgp.localLabel1.setEnabled(bool);
		apgp.idField.setEnabled(bool);
		apgp.nameField.setEnabled(bool);
		apgp.typeBox.setEnabled(bool);
		apgp.portBox.setEnabled(bool);
		apgp.KISBox.setEnabled(bool);
		apgp.localField.setEnabled(bool);*/
//		apgp.saveButton.setEnabled(bool);
	}
}