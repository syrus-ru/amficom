package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.scheme.SchemePath;

public class TransmissionPathPane extends JPanel implements ObjectResourcePropertiesPane
{
	protected SchemePath path;
	private static ObjectResourcePropertiesPane instance;

	private TransmissionPathGeneralPanel gPanel = new TransmissionPathGeneralPanel();
	private TransmissionPathFibrePanel fPanel = new TransmissionPathFibrePanel();
	private JTabbedPane tabbedPane = new JTabbedPane();

	protected TransmissionPathPane()
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

	protected TransmissionPathPane(SchemePath path)
	{
		this();
		setObject(path);
	}

	public static ObjectResourcePropertiesPane getInstance()
	{
		if (instance == null)
			instance = new TransmissionPathPane();
		return instance;
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(SwingConstants.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(fPanel.getName(), fPanel);

		tabbedPane.setEnabledAt(1, false);
	}

	public Object getObject()
	{
		return path;
	}

	public void setObject(Object or)
	{
		path = (SchemePath)or;

		gPanel.setObject(path);
		fPanel.setObject(path);
	}

	public void setContext(ApplicationContext aContext)
	{
		gPanel.setContext(aContext);
		fPanel.setContext(aContext);
	}

	public boolean modify()
	{
		if(gPanel.modify())
			return true;
		return false;
	}

	public boolean save() {
		if (modify()) {
			if (path.path() != null) {
				try {
					ConfigurationStorableObjectPool.putStorableObject(path.pathImpl());
					ConfigurationStorableObjectPool.flush(true);
					return true;
				} 
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
		}
		JOptionPane.showMessageDialog(Environment.getActiveWindow(),
				LangModelConfig.getString("err_incorrect_data_input"));
		return false;
	}

	public boolean open()
	{
		return false;
	}

	public boolean cancel()
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
}