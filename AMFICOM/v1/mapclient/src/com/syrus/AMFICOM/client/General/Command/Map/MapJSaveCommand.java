package com.syrus.AMFICOM.Client.General.Command.Map;

import java.awt.*;
import javax.swing.*;
import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.General.Command.*;

//A0A
public class MapJSaveCommand extends VoidCommand
{
        MapMainFrame mapFrame;
        ApplicationContext aContext;
        JDesktopPane desktop;

        public MapJSaveCommand()
        {
        }

        public MapJSaveCommand(JDesktopPane desktop, MapMainFrame myMapFrame, ApplicationContext aContext)
        {
                this.desktop = desktop;
                this.mapFrame = myMapFrame;
                this.aContext = aContext;
        }

        public Object clone()
        {
                return new MapJSaveCommand(desktop, mapFrame, aContext);
        }

        public void execute()
        {
                DataSourceInterface dataSource = aContext.getDataSourceInterface();

                if(dataSource == null)
                        return;

                dataSource.RemoveFromJMap(mapFrame.getMapContext().getId());
                dataSource.SaveJMap(mapFrame.getMapContext().getId());

        }

}