package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.Iterator;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ISM.MeasurementPort;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import oracle.jdeveloper.layout.VerticalFlowLayout;

public class KISCablePortsPanel extends GeneralPanel
{
	SchemeElement element;

	JLabel idLabel = new JLabel();
	private ObjectResourceComboBox cportBox = new ObjectResourceComboBox(SchemeCablePort.typ, false);
//	CablePortGeneralPanel cpgp = new CablePortGeneralPanel();
	CablePortPane cpgp = new CablePortPane();
//	AccessPortGeneralPanel apgp = new AccessPortGeneralPanel();
	private JCheckBox accessPortCheckBox = new JCheckBox();
	private JPanel bottomPanel = new JPanel();
	private JPanel mainPanel1 = new JPanel();
	private FlowLayout flowLayout1 = new FlowLayout();
	private JPanel controlsPanel1 = new JPanel();
	private JCheckBox accessPortCheckBox1 = new JCheckBox();
	private JPanel labelsPanel1 = new JPanel();
	private JPanel topPanel = new JPanel();
	private JPanel mainPanel = new JPanel();
	private JPanel controlsPanel = new JPanel();
	private JPanel labelsPanel = new JPanel();
	MeasurementPortPane apgp = new MeasurementPortPane();
	private JLabel jLabel1 = new JLabel();

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

	public KISCablePortsPanel(SchemeElement element)
	{
		this();
		setObjectResource(element);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_cableports"));
		this.setLayout(new BorderLayout());

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
		bottomPanel.setLayout(new BorderLayout());
		mainPanel1.setLayout(flowLayout1);
		flowLayout1.setAlignment(0);
		controlsPanel1.setLayout(new VerticalFlowLayout());
		labelsPanel1.setLayout(new VerticalFlowLayout());
		labelsPanel1.setMinimumSize(new Dimension(DEF_WIDTH + 5, 125));
		topPanel.setLayout(new BorderLayout());
		mainPanel.setLayout(new BorderLayout());
		controlsPanel.setLayout(new VerticalFlowLayout());
		labelsPanel.setLayout(new VerticalFlowLayout());
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
		return element;
	}

	public void setObjectResource(ObjectResource or)
	{
		element = (SchemeElement)or;

		if(element.kis != null)
		{
			this.cportBox.setContents(element.getCablePorts().iterator(), false);
			SchemeCablePort prt = (SchemeCablePort )cportBox.getSelectedObjectResource();
			cpgp.setObjectResource(prt);

			accessPortCheckBox.setSelected(false);
			apgp.setObjectResource(null);
			set_AP_Selected(false);

			apgp.setEnabled(false);
			for(Iterator it = element.getMeasurementPorts().iterator(); it.hasNext();)
			{
				MeasurementPort ap = (MeasurementPort)it.next();
				if (ap != null && prt != null)
				{
					if(ap.portId.equals(prt.getId()))
					{
						apgp.setEnabled(true);
						set_AP_Selected(true);
						accessPortCheckBox.setSelected(true);
						apgp.setObjectResource(ap);
					}
				}
			}
		}
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
		SchemeCablePort prt = (SchemeCablePort )cportBox.getSelectedObjectResource();
		cpgp.setObjectResource(prt);

		accessPortCheckBox.setSelected(false);
		apgp.setObjectResource(null);
		set_AP_Selected(false);

		for(Iterator it = element.getMeasurementPorts().iterator(); it.hasNext();)
		{
			MeasurementPort ap = (MeasurementPort)it.next();
			if (ap != null && prt != null)
			{
				if(ap.portId.equals(prt.getId()))
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