package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;

import javax.swing.JTabbedPane;

import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.TransmissionPathType;
import com.syrus.AMFICOM.Client.General.UI.PopupNameFrame;

public class TransmissionPathTypePane extends PropertiesPanel
{
	public ApplicationContext aContext;

	TransmissionPathTypeGeneralPanel gPanel = new TransmissionPathTypeGeneralPanel();

	TransmissionPathType pathType;

	public JTabbedPane tabbedPane = new JTabbedPane();

	public TransmissionPathTypePane()
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

	public TransmissionPathTypePane(TransmissionPathType tp)
	{
		this();
		setObjectResource(tp);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
	}

	public ObjectResource getObjectResource()
	{
		return pathType;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.pathType = (TransmissionPathType)or;

		gPanel.setObjectResource(pathType);
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
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if (dataSource == null)
			return false;

		PopupNameFrame dialog = new PopupNameFrame(Environment.getActiveWindow(), "Новый тип");
		dialog.setSize(dialog.preferredSize);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation((screenSize.width - dialog.getPreferredSize().width) / 2,
											 (screenSize.height - dialog.getPreferredSize().height) / 2);
		dialog.setVisible(true);

		if (dialog.getStatus() == dialog.OK && !dialog.getName().equals(""))
		{
			String name = dialog.getName();
			TransmissionPathType new_type = new TransmissionPathType();
			//new_type.is_modified = true;
			new_type.name = name;
			//new_type.link_class = "cable";
			new_type.modified = System.currentTimeMillis();
			new_type.id = aContext.getDataSourceInterface().GetUId(TransmissionPathType.typ);

			setObjectResource(new_type);

			Pool.put(TransmissionPathType.typ, new_type.getId(), new_type);
			return true;
		}
		return false;
	}
}