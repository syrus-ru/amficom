/*
 * $Id: MapSaveCommand.java,v 1.3 2004/06/28 11:47:51 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.MapMainFrame;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import javax.swing.JDesktopPane;

/**
 * Класс $RCSfile: MapSaveCommand.java,v $ используется для сохранения топологической схемы на сервере
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/06/28 11:47:51 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapSaveCommand extends VoidCommand
{
        MapMainFrame mapFrame;
        ApplicationContext aContext;

        public MapSaveCommand()
        {
        }

		/**
		 * 
		 * @param myMapFrame comments
		 * @param aContext comments
		 */
        public MapSaveCommand(MapMainFrame myMapFrame, ApplicationContext aContext)
        {
                this.mapFrame = myMapFrame;
                this.aContext = aContext;
        }

		/**
		 * @deprecated
		 */
        public MapSaveCommand(JDesktopPane desktop, MapMainFrame myMapFrame, ApplicationContext aContext)
        {
                this(myMapFrame, aContext);
        }

        public Object clone()
        {
                return new MapSaveCommand(mapFrame, aContext);
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