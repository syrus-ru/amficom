package com.syrus.AMFICOM.Client.General.Command.Map;

import java.awt.*;
import javax.swing.*;
import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;

//A0A
public class MapSaveCommand extends VoidCommand
{
        MapMainFrame mapFrame;
        ApplicationContext aContext;
        JDesktopPane desktop;

        public MapSaveCommand()
        {
        }

        public MapSaveCommand(JDesktopPane desktop, MapMainFrame myMapFrame, ApplicationContext aContext)
        {
                this.desktop = desktop;
                this.mapFrame = myMapFrame;
                this.aContext = aContext;
        }

        public Object clone()
        {
                return new MapSaveCommand(desktop, mapFrame, aContext);
        }

        public void execute()
        {
                DataSourceInterface dataSource = aContext.getDataSourceInterface();

                if(dataSource == null)
                        return;

				aContext.getDispatcher().notify(new StatusMessageEvent("Сохранение топологической схемы..."));
                dataSource.RemoveFromMap(mapFrame.getMapContext().getId());
                dataSource.SaveMap(mapFrame.getMapContext().getId());
				aContext.getDispatcher().notify(new StatusMessageEvent("Схема успешно сохранена"));
        }

}