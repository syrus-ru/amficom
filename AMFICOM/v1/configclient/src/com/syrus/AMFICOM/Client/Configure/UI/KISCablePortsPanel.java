package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import java.util.Enumeration;

import oracle.jdeveloper.layout.VerticalFlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.Resource.ISM.KIS;
import com.syrus.AMFICOM.Client.Resource.Network.CablePort;
import com.syrus.AMFICOM.Client.Resource.ISM.AccessPort;

import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

public class KISCablePortsPanel extends GeneralPanel
{
	KIS equipment;
	JLabel idLabel = new JLabel();
	private ObjectResourceComboBox cportBox = new ObjectResourceComboBox(CablePort.typ, false);
//	CablePortGeneralPanel cpgp = new CablePortGeneralPanel();
	CablePortPane cpgp = new CablePortPane();
//	AccessPortGeneralPanel apgp = new AccessPortGeneralPanel();
	private JCheckBox accessPortCheckBox = new JCheckBox();
	private JPanel bottomPanel = new JPanel();
	private BorderLayout borderLayout4 = new BorderLayout();
	private JPanel mainPanel1 = new JPanel();
	private FlowLayout flowLayout1 = new FlowLayout();
	private JPanel controlsPanel1 = new JPanel();
	private VerticalFlowLayout verticalFlowLayout3 = new VerticalFlowLayout();
	private JCheckBox accessPortCheckBox1 = new JCheckBox();
	private JPanel labelsPanel1 = new JPanel();
	private VerticalFlowLayout verticalFlowLayout4 = new VerticalFlowLayout();
	private JPanel topPanel = new JPanel();
	private BorderLayout borderLayout2 = new BorderLayout();
	private JPanel mainPanel = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel controlsPanel = new JPanel();
	private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
	private JPanel labelsPanel = new JPanel();
	private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
	AccessPortPane apgp = new AccessPortPane();
	private JLabel jLabel1 = new JLabel();
	private BorderLayout borderLayout3 = new BorderLayout();

	public KISCablePortsPanel()
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

	public KISCablePortsPanel(KIS equipment)
	{
		this();
		setObjectResource(equipment);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_cableports"));
		this.setLayout(borderLayout3);

//		this.setPreferredSize(new Dimension(510, 410));
//		this.setMaximumSize(new Dimension(510, 410));
//		this.setMinimumSize(new Dimension(510, 410));

		idLabel.setText(LangModelConfig.getString("label_port"));
		idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		cportBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				portBox_actionPerformed(e);
			}
		});
		accessPortCheckBox.setEnabled(false);
		bottomPanel.setLayout(borderLayout4);
		mainPanel1.setLayout(flowLayout1);
		flowLayout1.setAlignment(0);
		controlsPanel1.setLayout(verticalFlowLayout3);
		labelsPanel1.setLayout(verticalFlowLayout4);
		labelsPanel1.setMinimumSize(new Dimension(DEF_WIDTH + 5, 125));
		topPanel.setLayout(borderLayout2);
		mainPanel.setLayout(borderLayout1);
		controlsPanel.setLayout(verticalFlowLayout2);
		labelsPanel.setLayout(verticalFlowLayout1);
		labelsPanel.setMinimumSize(new Dimension(DEF_WIDTH + 5, 125));
		apgp.setEnabled(false);
		apgp.setBorder(BorderFactory.createLoweredBevelBorder());
		apgp.setBorder(BorderFactory.createLoweredBevelBorder());
		jLabel1.setText(LangModelConfig.getString("label_evalport"));
		bottomPanel.add(mainPanel1, BorderLayout.NORTH);
		bottomPanel.add(apgp, BorderLayout.CENTER);
		mainPanel1.add(controlsPanel1, null);
		labelsPanel1.add(jLabel1, null);
		mainPanel1.add(labelsPanel1, null);
		controlsPanel1.add(accessPortCheckBox, null);
		topPanel.add(mainPanel, BorderLayout.NORTH);
		topPanel.add(cpgp, BorderLayout.CENTER);
		mainPanel.add(controlsPanel, BorderLayout.CENTER);
		mainPanel.add(labelsPanel, BorderLayout.WEST);
		controlsPanel.add(cportBox, null);
		labelsPanel.add(idLabel, null);
		this.add(topPanel, BorderLayout.NORTH);
		this.add(bottomPanel, BorderLayout.CENTER);
		cpgp.setBorder(BorderFactory.createLoweredBevelBorder());
	}

	public void setContext(ApplicationContext aContext)
	{
		super.setContext(aContext);
		cpgp.setContext(aContext);
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
//			System.out.println("set prop pane to " + equipment.name);

			this.cportBox.setContents(equipment.cports.elements(), false);
			CablePort prt = (CablePort )cportBox.getSelectedObjectResource();
			cpgp.setObjectResource(prt);

			accessPortCheckBox.setSelected(false);
			apgp.setObjectResource(null);
			set_AP_Selected(false);

			apgp.setEnabled(false);
			for(Enumeration enum = equipment.access_ports.elements(); enum.hasMoreElements();)
			{
				AccessPort ap = (AccessPort )enum.nextElement();
				if (ap != null && prt != null)
				{
					if(ap.port_id.equals(prt.getId()))
					{
						apgp.setEnabled(true);
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
		if(cportBox.getModel().getSize() > 0)
			ret = cpgp.save();
		if(!ret)
			return false;
		if(apgp.isEnabled())
			return apgp.save();
		return true;
	}

	void portBox_actionPerformed(ActionEvent e)
	{
		CablePort prt = (CablePort )cportBox.getSelectedObjectResource();
		cpgp.setObjectResource(prt);

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