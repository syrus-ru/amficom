package com.syrus.AMFICOM.Client.Map;

import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapTransmissionPathElement;

import java.util.*;

//thread который отвечает за из менение резима показа alarm у nodeLInk

	public class AnimateThread 
			extends Thread
			implements Runnable
	{
		boolean alarmState = false;
		int timeInterval = 1000;
		LogicalNetLayer logicalNetLayer;

		private boolean is_running = true;

		public AnimateThread(LogicalNetLayer logical)
		{
			logicalNetLayer = logical;
		}

		public void stop_running()
		{
			is_running = false;
		}
		
		public void run()
		{
			while (is_running)
			{
				try
				{
					sleep( timeInterval );
/*
					if(logicalNetLayer != null)
						if(logicalNetLayer.mapMainFrame != null)
							if(logicalNetLayer.mapMainFrame.aContext != null)
						if ( logicalNetLayer.mapMainFrame.aContext.getApplicationModel().isEnabled("mapActionIndication"))
						{
*/
					checkAlarmState (logicalNetLayer);
				}
				catch (Exception e)
				{
					System.out.println("AnimateThread found: " + e);
				}
			}

		}

		public void checkAlarmState(LogicalNetLayer myLogicalNetLayer)
		{
			if ( myLogicalNetLayer.mapContext != null)
			{
				Iterator e = myLogicalNetLayer.mapContext.getNodeLinks().iterator();
				while (e.hasNext())
				{
					MapNodeLinkElement nodeLink = (MapNodeLinkElement)e.next();

					//Меняем режим показа alarma
					if ( nodeLink.getAlarmState())
						nodeLink.setShowAlarmed(!nodeLink.getShowAlarmed());
				}

				e = myLogicalNetLayer.mapContext.getPhysicalLinks().iterator();
				while (e.hasNext())
				{
					MapPhysicalLinkElement link = (MapPhysicalLinkElement )e.next();

					//Меняем режим показа alarma
					if (link.getAlarmState())
						link.setShowAlarmed(!link.getShowAlarmed());
				}

				e = myLogicalNetLayer.mapContext.getTransmissionPath().iterator();
				while (e.hasNext())
				{
					MapTransmissionPathElement path = (MapTransmissionPathElement)e.next();

					//Меняем режим показа alarma
					if (path.getAlarmState())
						path.setShowAlarmed(!path.getShowAlarmed());
				}

				e = myLogicalNetLayer.mapContext.getMapEquipmentNodeElements().iterator();
				while (e.hasNext())
				{
					MapEquipmentNodeElement equip = (MapEquipmentNodeElement)e.next();

					//Меняем режим показа alarma
					if ( equip.getAlarmState())
						equip.setShowAlarmed(!equip.getShowAlarmed());
				}

				e = myLogicalNetLayer.mapContext.markers.iterator();
				while (e.hasNext())
				{
					MapMarker mm = (MapMarker )e.next();

					//Меняем режим показа alarma
					if ( mm instanceof MapAlarmMarker)
					{
						MapAlarmMarker mam = (MapAlarmMarker )mm;
						mam.setShowAlarmed(!mam.getShowAlarmed());
					}
				}

				myLogicalNetLayer.postDirtyEvent();
				myLogicalNetLayer.postPaintEvent();
			}

		}

	}
