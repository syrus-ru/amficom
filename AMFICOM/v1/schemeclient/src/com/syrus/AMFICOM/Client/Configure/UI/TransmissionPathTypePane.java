package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.PopupNameFrame;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.configuration.TransmissionPathType;
import com.syrus.AMFICOM.general.*;

public class TransmissionPathTypePane extends JPanel implements ObjectResourcePropertiesPane
{
	private ApplicationContext aContext;
	protected TransmissionPathType pathType;

	private TransmissionPathTypeGeneralPanel gPanel = new TransmissionPathTypeGeneralPanel();
	private JTabbedPane tabbedPane = new JTabbedPane();

	protected TransmissionPathTypePane()
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

	protected TransmissionPathTypePane(TransmissionPathType tp)
	{
		this();
		setObject(tp);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(SwingConstants.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
	}

	public Object getObject()
	{
		return pathType;
	}

	public void setObject(Object or)
	{
		this.pathType = (TransmissionPathType)or;

		gPanel.setObject(pathType);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
	}

	public boolean modify()
	{
		if(gPanel.modify())
			return true;
		return false;
	}

	public boolean save()
	{
/*		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogCMediting))
		{
			return false;
		}

		if(modify())
		{
			DataSourceInterface dataSource = aContext.getDataSourceInterface();
			dataSource.SavePath(pathType.getId());
			return true;
//			MonitoredElement me = (MonitoredElement )Pool.get(MonitoredElement.typ, tp.monitored_element_id);
//			dataSource.savem
		}
		else
		{
			new MessageBox("Неправильно введены данные").show();
		}*/
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
/*		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogCMediting))
			return false;

		String []s = new String[1];

		s[0] = path.id;
		aContext.getDataSourceInterface().RemovePaths(s);
*/
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
			String name = dialog.getName();
			Identifier user_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);
			try {
				TransmissionPathType new_type = TransmissionPathType.createInstance(
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