package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.scheme.AbstractSchemePortController;
import com.syrus.AMFICOM.scheme.corba.*;
import oracle.jdeveloper.layout.VerticalFlowLayout;

public class KISPortsPanel extends GeneralPanel
{
	SchemeElement element;

	JLabel idLabel = new JLabel();
	private ObjComboBox portBox = new ObjComboBox(
			 AbstractSchemePortController.getInstance(),
			 AbstractSchemePortController.KEY_NAME);
	private JPanel topPanel = new JPanel();
	private JPanel mainPanel = new JPanel();
	private JPanel controlsPanel = new JPanel();
	private JPanel labelsPanel = new JPanel();
	PortPane pgp = new PortPane();
	private JPanel bottomPanel = new JPanel();
	private JPanel mainPanel1 = new JPanel();
	private JPanel controlsPanel1 = new JPanel();
	private JPanel labelsPanel1 = new JPanel();
	private JLabel jLabel1 = new JLabel();
	private JCheckBox accessPortCheckBox = new JCheckBox();
	MeasurementPortPane apgp = new MeasurementPortPane();
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

	public KISPortsPanel(SchemeElement element)
	{
		this();
		setObject(element);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_ports"));

		this.setLayout(new BorderLayout());

		idLabel.setText(LangModelConfig.getString("label_port"));
		idLabel.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		portBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				portBox_actionPerformed(e);
			}
		});
		topPanel.setLayout(new BorderLayout());
		mainPanel.setLayout(new BorderLayout());
		controlsPanel.setLayout(new VerticalFlowLayout());
		labelsPanel.setLayout(new VerticalFlowLayout());
		labelsPanel.setMinimumSize(new Dimension(DEF_WIDTH + 5, 125));
		pgp.setBorder(BorderFactory.createLoweredBevelBorder());
		bottomPanel.setLayout(new BorderLayout());
		mainPanel1.setLayout(flowLayout1);
		controlsPanel1.setLayout(new VerticalFlowLayout());
		labelsPanel1.setLayout(new VerticalFlowLayout());
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

	public Object getObject()
	{
		return element;
	}

	public void setObject(Object or)
	{
		element = (SchemeElement)or;

		portBox.removeAll();
		portBox.addElements(SchemeUtils.getPorts(element));
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
		SchemePort prt = (SchemePort)portBox.getSelectedItem();
		pgp.setObject(prt);

		if (prt.measurementPort() != null)
		{
			set_AP_Selected(true);
			accessPortCheckBox.setSelected(true);
			apgp.setObject(prt);
		}
		else
		{
			accessPortCheckBox.setSelected(false);
			apgp.setObject(null);
			set_AP_Selected(false);
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