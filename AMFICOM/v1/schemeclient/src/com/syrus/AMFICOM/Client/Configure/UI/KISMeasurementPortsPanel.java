package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.SchemeElement;
import oracle.jdeveloper.layout.VerticalFlowLayout;

public class KISMeasurementPortsPanel extends GeneralPanel
{
	private SchemeElement schemeElement;

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
		return this.schemeElement;
	}

	public void setObject(final Object schemeElement)
	{
		this.schemeElement = (SchemeElement) schemeElement;
		this.portBox.removeAll();

		final KIS kis = this.schemeElement.rtuImpl();
		if (kis != null)
			this.portBox.addElements(kis.getMeasurementPorts());
	}

	void portBox_actionPerformed(ActionEvent e)
	{
		pgp.setObject(portBox.getSelectedItem());
	}
}