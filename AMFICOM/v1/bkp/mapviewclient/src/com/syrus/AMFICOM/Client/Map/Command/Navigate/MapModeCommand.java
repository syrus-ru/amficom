/**
 * $Id: MapModeCommand.java,v 1.13 2005/02/18 12:19:45 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Navigate;

import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.mapview.MeasurementPath;

/**
 * Команда переключения режима работы с картой - режимы фрагмента, линии, пути 
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/02/18 12:19:45 $
 * @module mapviewclient_v1
 */
public class MapModeCommand extends MapNavigateCommand
{
	String modeString;
	int mode;
	
	public MapModeCommand(LogicalNetLayer logicalNetLayer, String modeString, int mode)
	{
		super(logicalNetLayer);
		this.modeString = modeString;
		this.mode = mode;
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
				try {
					this.logicalNetLayer.repaint(false);
				} catch(MapConnectionException e) {
					setException(e);
					setResult(Command.RESULT_NO);
					e.printStackTrace();
				} catch(MapDataException e) {
					setException(e);
					setResult(Command.RESULT_NO);
					e.printStackTrace();
				}
			}
		}
	}
}
