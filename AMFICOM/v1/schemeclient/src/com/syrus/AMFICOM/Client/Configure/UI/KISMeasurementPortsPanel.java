package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ISM.MeasurementPort;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import oracle.jdeveloper.layout.VerticalFlowLayout;

public class KISMeasurementPortsPanel extends GeneralPanel
{
	SchemeElement element;

	JLabel idLabel = new JLabel();
	private ObjectResourceComboBox portBox = new ObjectResourceComboBox(MeasurementPort.typ, false);
	MeasurementPortGeneralPanel pgp = new MeasurementPortGeneralPanel();
	private JPanel mainPanel = new JPanel();
	private JPanel controlsPanel = new JPanel();
	private JPanel labelsPanel = new JPanel();

	public KISMeasurementPortsPanel()
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

	public KISMeasurementPortsPanel(SchemeElement element)
	{
		this();
		setObjectResource(element);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_accessports"));

		this.setLayout(new BorderLayout());

		idLabel.setText(LangModelConfig.getString("label_accessport"));
		idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		portBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				portBox_actionPerformed(e);
			}
		});
		mainPanel.setLayout(new BorderLayout());
		controlsPanel.setLayout(new VerticalFlowLayout());
		labelsPanel.setLayout(new VerticalFlowLayout());
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
		return element;
	}

	public void setObjectResource(ObjectResource or)
	{
		element = (SchemeElement)element;

		if(element.kis != null)
		{
			portBox.setContents(element.getMeasurementPorts().iterator(), false);
			MeasurementPort cp = (MeasurementPort)portBox.getSelectedObjectResource();
			pgp.setObjectResource(cp);
		}
	}

	void portBox_actionPerformed(ActionEvent e)
	{
		pgp.setObjectResource((MeasurementPort )portBox.getSelectedObjectResource());
	}
}