package com.syrus.AMFICOM.Client.General.Command.Map;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.Configure.Map.UI.MapChooserDialog;
import com.syrus.AMFICOM.Client.Configure.Map.UI.Display.*;

//A0A
public class MapJOpenCommand extends VoidCommand
{
	MapMainFrame mapFrame;
	ApplicationContext aContext;
	JDesktopPane desktop;

	public int retCode = 0;
	public String retobj_id;

	public MapJOpenCommand()
	{
	}

	public MapJOpenCommand(JDesktopPane desktop, MapMainFrame myMapFrame, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.mapFrame = myMapFrame;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MapJOpenCommand(desktop, mapFrame, aContext);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		if(dataSource == null)
			return;

		MapChooserDialog mcd = new MapChooserDialog(dataSource);//mapFrame, "Выберите карту", true);

		DataSet dataSet = new DataSet(Pool.getHash("ismmapcontext"));
		ObjectResourceDisplayModel odm = new ISMMapContextDisplayModel();
		mcd.setContents(odm, dataSet);
/*
		Hashtable hash = Pool.getHash("ismmapcontext");
		if(hash != null)
		{
			Enumeration enum = hash.elements();
			if(enum != null)
			{
				ObjectResource or = (ObjectResource)enum.nextElement();
				if(or != null)
					mcd.setContents(or.getColumns(), Pool.getHash("ismmapcontext"));
			}
		}
*/
		mcd.setModal(true);
		mcd.setVisible(true);
		if(mcd.retCode == mcd.RET_CANCEL)
		{
//			statusBar.setText("status", LangModelMain.String("statusCancelled"));
			return;
		}

		if(mcd.retCode == mcd.RET_OK)
		{
//			dataSource.LoadMaps();
//			dataSource.LoadJMaps();

			if(mapFrame == null)
			{
				System.out.println("mapviewer is NULL");
				retCode = 1;
				retobj_id = mcd.retObject.getId();
			}
			else
			{
				mapFrame.setMapContext((MapContext)Pool.get("ismmapcontext", mcd.retObject.getId()));
			}

		}
	}

}
