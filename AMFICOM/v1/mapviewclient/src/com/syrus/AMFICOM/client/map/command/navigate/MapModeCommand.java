/**
 * $Id: MapModeCommand.java,v 1.11 2005/02/01 11:34:56 krupenn Exp $
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
 * 
 * 
 * 
 * @version $Revision: 1.11 $, $Date: 2005/02/01 11:34:56 $
 * @module
 * @author $Author: krupenn $
 * @see
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
			logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("applicationModel"))
			aModel = (ApplicationModel )value;
	}

	public void execute()
	{
		if ( aModel.isEnabled(modeString))
		{
//			if(!aModel.isSelected(modeString))
			{
				aModel.setSelected(MapApplicationModel.MODE_NODE_LINK, false);
				aModel.setSelected(MapApplicationModel.MODE_LINK, false);
				aModel.setSelected(MapApplicationModel.MODE_CABLE_PATH, false);
				aModel.setSelected(MapApplicationModel.MODE_PATH, false);
	
				aModel.setSelected(modeString, true);
				
				if(modeString.equals(MapApplicationModel.MODE_PATH))
				{
					for(Iterator it = logicalNetLayer.getMapView().getMeasurementPaths().iterator(); it.hasNext();)
					{
						MeasurementPath mpath = 
							(MeasurementPath)it.next();
						mpath.sortPathElements();
					}
				}
	
				aModel.fireModelChanged();
	
				logicalNetLayer.getMapState().setShowMode(mode);
				logicalNetLayer.repaint(false);
			}
		}
	}
}
