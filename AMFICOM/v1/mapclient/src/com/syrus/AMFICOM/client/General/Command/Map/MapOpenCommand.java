package com.syrus.AMFICOM.Client.General.Command.Map;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.Map.UI.*;
import com.syrus.AMFICOM.Client.Map.UI.Display.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;

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

		aContext.getDispatcher().notify(new StatusMessageEvent("Открытие топологической схемы"));
		MapChooserDialog mcd = new MapChooserDialog(dataSource);//mapFrame, "Выберите карту", true);

		if(can_delete)
			mcd.buttonDelete.setVisible(true);

		DataSet dataSet = new DataSet(Pool.getHash("mapcontext"));
		ObjectResourceDisplayModel odm = new MapContextDisplayModel();
		mcd.setContents(odm, dataSet);

		// отфильтровываем по домену
		ObjectResourceTableModel ortm = (ObjectResourceTableModel )mcd.listPane.getTable().getModel();
		ortm.setDomainId(aContext.getSessionInterface().getDomainId());
		ortm.restrictToDomain(true);//ф-я фильтрации схем по домену

		mcd.setModal(true);
		mcd.setVisible(true);
		if(mcd.retCode == mcd.RET_CANCEL)
		{
//			statusBar.setText("status", LangModelMain.String("statusCancelled"));
			aContext.getDispatcher().notify(new StatusMessageEvent("Операция отменена"));
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
			aContext.getDispatcher().notify(new StatusMessageEvent("Операция завершена"));
		}
	}

}
