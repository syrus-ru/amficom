package com.syrus.AMFICOM.Client.Survey.UI;

import com.ofx.geometry.SxDoublePoint;

import com.syrus.AMFICOM.CORBA.General.AlarmStatus;
import com.syrus.AMFICOM.Client.Resource.Alarm.Alarm;
import com.syrus.AMFICOM.Client.Resource.Alarm.EventSource;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.Map.MapEquipmentNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapTransmissionPathElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ArrayList;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapMainFrame;
import java.util.Iterator;

///Thread для перезагрузки аттрибутов элементов карты
public class ReloadAttributes 
		extends Thread
		implements Runnable
{
	int timeInterval = 5000;
	MapMainFrame mmf;

	private volatile boolean is_running = true;

	public ReloadAttributes(MapMainFrame mmf)
	{
		this.mmf = mmf;
	}

	public LogicalNetLayer lnl()
	{
		return mmf.lnl();
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
				updateAttributes();
			}
			catch (InterruptedException e)
			{
				System.out.println("ReloadAttributes found: " + e);
			}
		}
	}
/*
	public void run1()
	{
		while (true)
		{
			try
			{
				sleep( timeInterval );
				if(lnl().getMapContext() instanceof ISMMapContext)
				{
				aContext.getDataSourceInterface()
							.ReloadJAttributes(((ISMMapContext )lnl().getMapContext()).ISM_id);
				}
				else
				{
					aContext.getDataSourceInterface()
							.ReloadAttributes(lnl().getMapContext().id);
				}
				updateAttributes();
			}
			catch (InterruptedException e)
			{
				System.out.println(e);
			}
		}

	}
*/
	public void updateAttributes()
	{
	try
	{
		ArrayList vec;
		int count;
		int i;
		if(lnl() == null)
			return;
		MapContext mc = lnl().getMapContext();
		if(mc == null)
			return;

		String kis_id = "";
		String me_id;

		if(Pool.getHash(EventSource.typ) == null)
			return;

		Hashtable esrc_ht = new Hashtable();
		for(Enumeration enum1 = Pool.getHash(EventSource.typ).elements(); enum1.hasMoreElements();)
		{
			EventSource esrc = (EventSource )enum1.nextElement();
			esrc_ht.put(esrc.id, esrc.object_source_id);
            
		}

		vec = mc.getTransmissionPath();

		if(vec == null)
			return;
		for(Iterator it = vec.iterator(); it.hasNext();)
		{
			MapTransmissionPathElement path = (MapTransmissionPathElement )it.next();

			ElementAttribute ea = (ElementAttribute )path.attributes.get("alarmed");
			if(ea != null)
			{
				ea.setValue("false");

				if(path.PATH_ID != null && !path.PATH_ID.equals(""))
				{
					SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, path.PATH_ID);
					if(sp != null && sp.path_id != null && !sp.path_id.equals(""))
					{
						TransmissionPath tp = (TransmissionPath )Pool.get(TransmissionPath.typ, sp.path_id);

						Hashtable ht = Pool.getHash(Alarm.typ);
						if(ht != null)
						for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
						{
							Alarm alarm = (Alarm )enum.nextElement();
							me_id = alarm.getMonitoredElementId();

							if(me_id == null)
								continue;

							if(tp.monitored_element_id.equals(me_id))
								if(alarm.status == AlarmStatus.ALARM_STATUS_GENERATED)
								{
									ea.setValue("true");
									break;
								}
						}
					}
				}
			}
		}
/*
		Hashtable ht = Pool.getHash(Alarm.typ);
		for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
		{
			Alarm alarm = (Alarm )enum.nextElement();

			kis_id = (String )esrc_ht.get(alarm.source_id);

			if(kis_id == null)
				continue;

			vec = mc.getTransmissionPath();
			count = vec.size();
			for(i = 0; i < count; i++)
			{
				MapTransmissionPathElement path = (MapTransmissionPathElement )vec.get(i);

				if(path.PATH_ID != null && !path.PATH_ID.equals(""))
				{
					SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, path.PATH_ID);
					if(sp.path_id != null && !sp.path_id.equals(""))
					{
						TransmissionPath tp = (TransmissionPath )Pool.get(TransmissionPath.typ, sp.path_id);
						if(tp.KIS_id.equals(kis_id))
						{
							ElementAttribute ea = (ElementAttribute )path.attributes.get("alarmed");
							if(ea != null)
							{
								if(alarm.status == AlarmStatus.GENERATED)
									ea.setValue("true");
								else
									ea.setValue("false");
							}
						}
					}
				}
			}
		}
*/			
	}
	catch(Exception ex)
	{
		System.out.println("attributes not updated: " + ex.getMessage());
//			ex.printStackTrace();
	}
		
	}
/*
	public void updateAttributes1()
	{
		Vector vec;
		int count;
		int i;
		MapContext mc = lnl().getMapContext();
			
		vec = mc.getNodes();
		count = vec.size();
		for(i = 0; i < count; i++)
		{
			ObjectResource os = (ObjectResource )vec.get(i);
			if(os.getTyp().equals("mapkiselement"))
			{
				MapKISNodeElement kis = (MapKISNodeElement )os;
				kis.updateAttributes();
			}
			else
			if(os.getTyp().equals("mapequipmentelement"))
			{
				MapEquipmentNodeElement equipment = (MapEquipmentNodeElement )os;
				equipment.updateAttributes();
			}
		}

		vec = mc.getPhysicalLinks();
		count = vec.size();
		for(i = 0; i < count; i++)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )vec.get(i);
			link.updateAttributes();
		}

		vec = mc.getTransmissionPath();
		count = vec.size();
		for(i = 0; i < count; i++)
		{
			MapTransmissionPathElement path = (MapTransmissionPathElement )vec.get(i);
			path.updateAttributes();
		}
	}
*/
}
