/**
 * $Id: MapModeCommand.java,v 1.12 2005/02/08 15:11:10 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Navigate;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import java.util.Iterator;

/**
 * Команда переключения режима работы с картой - режимы фрагмента, линии, пути 
 * @author $Author: krupenn $
 * @version $Revision: 1.12 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
 */
public class MapModeCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	ApplicationModel aModel;

	String modeString;
	int mode;
	
	public MapModeCommand(LogicalNetLayer logicalNetLayer, String modeString, int mode)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.modeString = modeString;
		this.mode = mode;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("logicalNetLayer"))
			this.logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("applicationModel"))
			this.aModel = (ApplicationModel )value;
	}

	public void execute()
	{
		if ( this.aModel.isEnabled(this.modeString))
		{
//			if(!aModel.isSelected(this.modeString))
			{
				this.aModel.setSelected(MapApplicationModel.MODE_NODE_LINK, false);
				this.aModel.setSelected(MapApplicationModel.MODE_LINK, false);
				this.aModel.setSelected(MapApplicationModel.MODE_CABLE_PATH, false);
				this.aModel.setSelected(MapApplicationModel.MODE_PATH, false);
	
				this.aModel.setSelected(this.modeString, true);
				
				if(this.modeString.equals(MapApplicationModel.MODE_PATH))
				{
					for(Iterator it = this.logicalNetLayer.getMapView().getMeasurementPaths().iterator(); it.hasNext();)
					{
						MeasurementPath mpath = 
							(MeasurementPath)it.next();
						mpath.sortPathElements();
					}
				}
	
				this.aModel.fireModelChanged();
	
				this.logicalNetLayer.getMapState().setShowMode(this.mode);
				this.logicalNetLayer.repaint(false);
			}
		}
	}
}
