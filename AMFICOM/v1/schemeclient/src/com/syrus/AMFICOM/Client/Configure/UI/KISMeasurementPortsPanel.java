package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.Collection;
import java.util.List;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;
import oracle.jdeveloper.layout.VerticalFlowLayout;

public class KISMeasurementPortsPanel extends GeneralPanel
{
	protected SchemeElement element;

	private JLabel idLabel = new JLabel();
	private ObjComboBox portBox = new ObjComboBox(MeasurementPortController.getInstance(),
			StorableObjectWrapper.COLUMN_NAME);
	private MeasurementPortGeneralPanel pgp = new MeasurementPortGeneralPanel();
	private JPanel mainPanel = new JPanel();
	private JPanel controlsPanel = new JPanel();
	private JPanel labelsPanel = new JPanel();

	protected KISMeasurementPortsPanel()
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

	protected KISMeasurementPortsPanel(SchemeElement element)
	{
		this();
		setObject(element);
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

	public Object getObject()
	{
		return element;
	}

	public void setObject(Object or)
	{
		element = (SchemeElement)or;
		portBox.removeAll();

		if(element.rtu() != null)
		{
			try {
				Collection portIds = element.rtuImpl().getMeasurementPortIds();
				Collection ports = ConfigurationStorableObjectPool.getStorableObjects(portIds, true);
				portBox.addElements(ports);
			}
			catch (ApplicationException ex) {
			}
		}
	}

	void portBox_actionPerformed(ActionEvent e)
	{
		pgp.setObject(portBox.getSelectedItem());
	}
}