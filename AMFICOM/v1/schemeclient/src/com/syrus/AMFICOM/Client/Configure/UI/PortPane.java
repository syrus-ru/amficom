package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.scheme.corba.SchemePort;
import oracle.jdeveloper.layout.XYConstraints;

public class PortPane extends JPanel implements ObjectResourcePropertiesPane
{
	public ApplicationContext aContext;

	AbstractPortGeneralPanel gPanel = new AbstractPortGeneralPanel();
	PortCharacteristicsPanel chPanel;

	SchemePort port;

	public JTabbedPane tabbedPane = new JTabbedPane();

	private JButton saveButton = new JButton();
	private JPanel buttonsPanel = new JPanel();

	public PortPane()
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

	public PortPane(SchemePort p)
	{
		this();
		setObject(p);
	}

	private void jbInit() throws Exception
	{
		chPanel = new PortCharacteristicsPanel();

		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(chPanel.getName(), chPanel);

		saveButton.setText(LangModelConfig.getString("menuMapSaveText"));
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButton_actionPerformed(e);
			}
		});

		buttonsPanel.add(saveButton, new XYConstraints(200, 487, -1, -1));
	}

	public Object getObject()
	{
		return port;
	}

	public void setObject(Object or)
	{
		port = (SchemePort)or;

		gPanel.setObject(port);
		chPanel.setObject(port);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		chPanel.setContext(aContext);
	}

	public boolean modify()
	{
		if(	gPanel.modify() &&
				chPanel.modify())
			return true;
		return false;
	}

	public boolean save()
	{
		if(modify())
		{
			try {
				ConfigurationStorableObjectPool.putStorableObject(port.portImpl());
			}
			catch (ApplicationException ex) {
			}
			return true;
		}
		else
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelConfig.getString("err_incorrect_data_input"));
		}
		return false;
	}

	public boolean open()
	{
		return false;
	}

	public boolean delete()
	{
		return false;
	}

	public boolean create()
	{
		return false;
	}

	public boolean cancel()
	{
		return false;
	}


	void saveButton_actionPerformed(ActionEvent e)
	{
	}

}