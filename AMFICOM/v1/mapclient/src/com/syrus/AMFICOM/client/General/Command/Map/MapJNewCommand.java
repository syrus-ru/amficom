package com.syrus.AMFICOM.Client.General.Command.Map;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Configure.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.Configure.Map.UI.*;

//A0A
public class MapJNewCommand extends VoidCommand
{
	ApplicationContext aContext;
	MapMainFrame mapFrame;

	public MapJNewCommand()
	{
	}

	public MapJNewCommand(MapMainFrame myMapFrame, ApplicationContext aContext)
	{
		this.mapFrame = myMapFrame;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MapJNewCommand(mapFrame, aContext);
	}

	public void execute()
	{
          System.out.println("Creating new map context");

		  DataSourceInterface dataSource = aContext.getDataSourceInterface();

		  ISMMapContext mc = new ISMMapContext( mapFrame.lnl());
		  mc.setLongLat(
				mapFrame.lnl().viewer.getCenter()[0],
				mapFrame.lnl().viewer.getCenter()[1] );
//		  MapContext mc = new MapContext( mapFrame.mapPanel.myMapViewer.lnl);
			mc.ISM_id = aContext.getDataSourceInterface().GetUId("ismmapcontext");
			mc.ISM_domain_id = "domain1";
			mc.ISM_user_id = aContext.getSessionInterface().getUserId();

          NewJMapContextDialog dialog = new NewJMapContextDialog( mapFrame.mapJFrame, "Свойства контекста", true, mc);
//          NewMapContextDialog dialog = new NewMapContextDialog( mapFrame.mapJFrame, "Свойства контекста", true, mc);

//			dialog.idTextField.setEnabled(false);
//			dialog.ownerTextField.setEnabled(false);
//			dialog.domainTextField.setEnabled(false);

          Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
          Dimension frameSize =  dialog.getSize();

         if (frameSize.height > screenSize.height)
          {
            frameSize.height = screenSize.height;
          }

         if (frameSize.width > screenSize.width)
          {
            frameSize.width = screenSize.width;
          }

           dialog.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);

           dialog.setVisible(true);

        if ( dialog.ifAccept())
          {
//				dataSource.LoadMaps();
				mc.setNetMapContext((MapContext)(Pool.get("mapcontext", mc.ISM_map_id)));
		        Pool.put( mc.getTyp(), mc.getId(), mc);
				mapFrame.setMapContext(mc);
				mapFrame.setTitle( LangModelMap.String("AppTitle") + " - "
                       + mapFrame.lnl().getMapContext().name);
          }
	}

}