package com.syrus.AMFICOM.Client.Survey.UI;

import java.util.*;

import com.syrus.AMFICOM.Client.Survey.*;
import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.Client.Schematics.General.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

///Thread для перезагрузки аттрибутов элементов схемы
public class SchemeAlarmUpdater extends Thread  implements Runnable
{
	int timeInterval = 5000;
	SchemePanel panel;
	public boolean flag = false;
	ApplicationContext aContext;

	public SchemeAlarmUpdater(ApplicationContext aContext, SchemePanel panel)
	{
		this.panel = panel;
		this.aContext = aContext;
	}

	public void run()
	{
		flag = true;
		while (flag)
		{
			SurveyMDIMain s;
			try
			{
				updateAttributes();
				sleep( timeInterval );
			}
			catch (InterruptedException e)
			{
				System.out.println("SchemeAlarmUpdater found: " + e);
			}
		}
	}

	public void updateAttributes()
	{
		try
		{
			Scheme scheme = panel.scheme;

			for(int i = 0; i < scheme.paths.size(); i++)
			{
				SchemePath sp = (SchemePath )scheme.paths.get(i);
				ElementAttribute ea = (ElementAttribute)sp.attributes.get("alarmed");

				if(ea != null)
				{
					ea.setValue("false");
					if(!sp.path_id.equals(""))
					{
						TransmissionPath tp = (TransmissionPath )Pool.get(TransmissionPath.typ, sp.path_id);

						Hashtable ht = Pool.getHash(Alarm.typ);
						if(ht != null)
							for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
							{
								Alarm alarm = (Alarm )enum.nextElement();
								String me_id = alarm.getMonitoredElementId();

								if(me_id == null)
									continue;

								if(tp.monitored_element_id.equals(me_id))
									if(alarm.status == AlarmStatus.ALARM_STATUS_GENERATED)
										ea.setValue("true");
							}
					}
				}
				else
				{
					ea = new ElementAttribute(aContext.getDataSourceInterface().GetUId(ElementAttribute.typ), "", "false", "alarmed");
					sp.attributes.put(ea.type_id, ea);
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println("attributes not updated: " + ex.getMessage());
		}
	}
}

