package com.syrus.AMFICOM.Client.Configure.Map;

import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapKISNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapTransmissionPathElement;

import java.util.Enumeration;

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
				Enumeration e = myLogicalNetLayer.mapContext.getNodeLinks().elements();
				while (e.hasMoreElements())
				{
					MapNodeLinkElement nodeLink = (MapNodeLinkElement)e.nextElement();

					//Меняем режим показа alarma
					if ( nodeLink.getAlarmState())
						nodeLink.setShowAlarmed(!nodeLink.getShowAlarmed());
				}

				e = myLogicalNetLayer.mapContext.getPhysicalLinks().elements();
				while (e.hasMoreElements())
				{
					MapPhysicalLinkElement link = (MapPhysicalLinkElement )e.nextElement();

					//Меняем режим показа alarma
					if (link.getAlarmState())
						link.setShowAlarmed(!link.getShowAlarmed());
				}

				e = myLogicalNetLayer.mapContext.getTransmissionPath().elements();
				while (e.hasMoreElements())
				{
					MapTransmissionPathElement path = (MapTransmissionPathElement)e.nextElement();

					//Меняем режим показа alarma
					if (path.getAlarmState())
						path.setShowAlarmed(!path.getShowAlarmed());
				}

				e = myLogicalNetLayer.mapContext.getMapEquipmentNodeElements().elements();
				while (e.hasMoreElements())
				{
					MapEquipmentNodeElement equip = (MapEquipmentNodeElement)e.nextElement();

					//Меняем режим показа alarma
					if ( equip.getAlarmState())
						equip.setShowAlarmed(!equip.getShowAlarmed());
				}

				e = myLogicalNetLayer.mapContext.getMapKISNodeElements().elements();
				while (e.hasMoreElements())
				{
					MapKISNodeElement kis = (MapKISNodeElement)e.nextElement();

					//Меняем режим показа alarma
//					if ( kis.getAlarmState())
//						kis.setShowAlarmed(!kis.getShowAlarmed());
				}

				e = myLogicalNetLayer.mapContext.markers.elements();
				while (e.hasMoreElements())
				{
					MapMarker mm = (MapMarker )e.nextElement();

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
