package com.syrus.AMFICOM.Client.Prediction.UI.Selector;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import oracle.jdeveloper.layout.VerticalFlowLayout;

public class ParameterSelector extends JInternalFrame
{
	private JPanel jPanel1 = new JPanel();
	private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
	private JRadioButton buttonAttenuation = new JRadioButton();
	private JRadioButton buttonReflection = new JRadioButton();
	private JRadioButton buttonAmplitude = new JRadioButton();
	private Dispatcher dispatcher;

	public boolean amplitude=false;
	public boolean attenuation=true;
	public boolean reflection=false;

	public ParameterSelector(Dispatcher dispatcher)
	{
		setDispatcher(dispatcher);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private void jbInit() throws Exception
	{
		jPanel1.setLayout(verticalFlowLayout1);
		buttonAttenuation.setText("Затухание");
		buttonAttenuation.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonAttenuation_actionPerformed(e);
			}
		});
		buttonReflection.setText("Отражение (амплитуда всплеска)");
		buttonReflection.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonReflection_actionPerformed(e);
			}
		});
		buttonAmplitude.setText("Начальная амплитуда");
		buttonAmplitude.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonAmplitude_actionPerformed(e);
			}
		});
		this.setClosable(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setIconifiable(true);
		this.setResizable(true);
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
		jPanel1.add(buttonAttenuation, null);
		jPanel1.add(buttonReflection, null);
		jPanel1.add(buttonAmplitude, null);

		buttonAttenuation.setSelected(true);
		buttonReflection.setSelected(false);
		buttonAmplitude.setSelected(false);

	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher  = dispatcher;
	}



	void buttonAttenuation_actionPerformed(ActionEvent e)
	{
		buttonAttenuation.setSelected(true);
		buttonAmplitude.setSelected(false);
		buttonReflection.setSelected(false);

		amplitude=false;
		attenuation=true;
		reflection=false;
	}

	void buttonReflection_actionPerformed(ActionEvent e)
	{
		buttonAttenuation.setSelected(false);
		buttonAmplitude.setSelected(false);
		buttonReflection.setSelected(true);

		amplitude=false;
		attenuation=false;
		reflection=true;
	}

	void buttonAmplitude_actionPerformed(ActionEvent e)
	{
		buttonAttenuation.setSelected(false);
		buttonAmplitude.setSelected(true);
		buttonReflection.setSelected(false);

		amplitude=true;
		attenuation=false;
		reflection=false;
	}

}