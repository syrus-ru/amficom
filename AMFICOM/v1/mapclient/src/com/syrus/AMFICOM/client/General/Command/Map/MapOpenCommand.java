package com.syrus.AMFICOM.Client.General.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTableModel;
import com.syrus.AMFICOM.Client.Map.MapMainFrame;
import com.syrus.AMFICOM.Client.Map.UI.Display.MapContextDisplayModel;
import com.syrus.AMFICOM.Client.Map.UI.MapChooserDialog;
import com.syrus.AMFICOM.Client.Resource.DataSet;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.Pool;

import javax.swing.JDesktopPane;

public class MapOpenCommand extends VoidCommand
{
	MapMainFrame mapFrame;
	ApplicationContext aContext;
	JDesktopPane desktop;

	public int retCode = 0;
	public String retobj_id;

	public boolean can_delete = false;

	public MapOpenCommand()
	{
	}

	public MapOpenCommand(JDesktopPane desktop, MapMainFrame myMapFrame, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.mapFrame = myMapFrame;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MapOpenCommand(desktop, mapFrame, aContext);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		if(dataSource == null)
			return;

		aContext.getDispatcher().notify(new StatusMessageEvent("�������� �������������� �����"));
		MapChooserDialog mcd = new MapChooserDialog(dataSource);//mapFrame, "�������� �����", true);

		if(can_delete)
			mcd.buttonDelete.setVisible(true);

		DataSet dataSet = new DataSet(Pool.getHash("mapcontext"));
		ObjectResourceDisplayModel odm = new MapContextDisplayModel();
		mcd.setContents(odm, dataSet);

		// ��������������� �� ������
		ObjectResourceTableModel ortm = (ObjectResourceTableModel )mcd.listPane.getTable().getModel();
		ortm.setDomainId(aContext.getSessionInterface().getDomainId());
		ortm.restrictToDomain(true);//�-� ���������� ���� �� ������

		mcd.setModal(true);
		mcd.setVisible(true);
		if(mcd.retCode == mcd.RET_CANCEL)
		{
//			statusBar.setText("status", LangModelMain.String("statusCancelled"));
			aContext.getDispatcher().notify(new StatusMessageEvent("�������� ��������"));
			return;
		}

		if(mcd.retCode == mcd.RET_OK)
		{
			retobj_id = mcd.retObject.getId();
//			new MapDataSourceImage(dataSource).LoadMap(retobj_id);

			if(mapFrame == null)
			{
				System.out.println("mapviewer is NULL");
				retCode = 1;
			}
			else
			{
				mapFrame.setMapContext((MapContext)Pool.get("mapcontext", retobj_id));
				retCode = 2;
			}
			aContext.getDispatcher().notify(new StatusMessageEvent("�������� ���������"));
		}
	}

}
