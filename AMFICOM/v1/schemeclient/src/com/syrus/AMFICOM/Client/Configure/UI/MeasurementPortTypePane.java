package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.PopupNameFrame;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;

public class MeasurementPortTypePane extends JPanel implements ObjectResourcePropertiesPane
{
	private ApplicationContext aContext;
	protected MeasurementPortType portType;
	private static ObjectResourcePropertiesPane instance;

	private MeasurementPortTypeGeneralPanel gPanel = new MeasurementPortTypeGeneralPanel();
	private MeasurementPortTypeCharacteristicsPanel chPanel = new MeasurementPortTypeCharacteristicsPanel();
	private JTabbedPane tabbedPane = new JTabbedPane();

	protected MeasurementPortTypePane()
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

	protected MeasurementPortTypePane(MeasurementPortType p)
	{
		this();
		setObject(p);
	}

	public static ObjectResourcePropertiesPane getInstance()
	{
		if (instance == null)
			instance = new MeasurementPortTypePane();
		return instance;
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(SwingConstants.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(chPanel.getName(), chPanel);
	}

	public Object getObject()
	{
		return portType;
	}

	public void setObject(Object or)
	{
		this.portType = (MeasurementPortType)or;

		gPanel.setObject(portType);
		chPanel.setObject(portType);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		chPanel.setContext(aContext);
	}

	public boolean modify()
	{
		if (gPanel.modify() &&
				chPanel.modify())
			return true;

		return false;
	}

	public boolean save() {
		if (modify()) {
			if (chPanel.save()) {
				if (portType != null) {
					try {
						ConfigurationStorableObjectPool.putStorableObject(portType);
						ConfigurationStorableObjectPool.flush(true);
						return true;
					} 
					catch (ApplicationException ex) {
						ex.printStackTrace();
					}
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
		return true;
	}

	public boolean create()
	{
		PopupNameFrame dialog = new PopupNameFrame(Environment.getActiveWindow(), "Новый тип");
		dialog.setSize(dialog.preferredSize);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation((screenSize.width - dialog.getPreferredSize().width) / 2,
											 (screenSize.height - dialog.getPreferredSize().height) / 2);
		dialog.setVisible(true);

		if (dialog.getStatus() == PopupNameFrame.OK && !dialog.getName().equals(""))
		{
			Identifier user_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);
			String name = dialog.getName();
			try {
				MeasurementPortType new_type = MeasurementPortType.createInstance(
						user_id,
						"",
						"",
						name);

				setObject(new_type);
				return true;
			}
			catch (CreateObjectException ex) {
				ex.printStackTrace();
				return false;
			}
		}
		return false;
	}
}
