package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.scheme.corba.SchemeCablePort;
import oracle.jdeveloper.layout.XYConstraints;

public class CablePortPane extends JPanel implements ObjectResourcePropertiesPane
{
	protected SchemeCablePort port;
	private static ObjectResourcePropertiesPane instance;

	AbstractPortGeneralPanel gPanel = new AbstractPortGeneralPanel();
	CablePortCharacteristicsPanel chPanel;
	JTabbedPane tabbedPane = new JTabbedPane();
	JButton saveButton = new JButton();
	JPanel buttonsPanel = new JPanel();

	protected CablePortPane()
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

	protected CablePortPane(SchemeCablePort p)
	{
		this();
		setObject(p);
	}

	public static ObjectResourcePropertiesPane getInstance()
	{
		if (instance == null)
			instance = new CablePortPane();
		return instance;
	}


	private void jbInit() throws Exception
	{
		chPanel = new CablePortCharacteristicsPanel();

		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(SwingConstants.TOP);

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
		port = (SchemeCablePort)or;

		gPanel.setObject(port);
		chPanel.setObject(port);
	}

	public void setContext(ApplicationContext aContext)
	{
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
				return true;
			}
			catch (ApplicationException ex) {
				
			}
		}
		JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModelConfig.getString("err_incorrect_data_input"));
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
