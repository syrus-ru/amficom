package com.syrus.AMFICOM.Client.Survey.UI;

import java.util.*;

import com.syrus.AMFICOM.Client.Survey.*;
import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.Client.General.Scheme.SchemePanel;
import com.syrus.AMFICOM.Client.Analysis.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;

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
			Scheme scheme = panel.getGraph().getScheme();

			for(Iterator it = scheme.solution.paths.iterator(); it.hasNext();)
			{
				SchemePath sp = (SchemePath)it.next();
				Characteristic ch = (Characteristic)sp.attributes.get("alarmed");

				if(ch != null)
				{
					ch.setValue("false");
					/**
					 * @todo remove comment when SchemePath moves to new TransmissionPath
					 */
					 /*
//					if(sp.path != null)
					{
						Hashtable ht = Pool.getHash(Alarm.typ);
						if(ht != null)
							for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
							{
								Alarm alarm = (Alarm )enum.nextElement();
								Identifier meId = alarm.getMonitoredElementId();

								if(meId == null)
									continue;

								if(sp.path.monitored_element_id.equals(meId))
									if(alarm.status == AlarmStatus.ALARM_STATUS_GENERATED)
										ch.setValue("true");
							}
					}
					*/
				}
				else
				{
					Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getUserId());
					ch = Characteristic.createInstance(
							IdentifierPool.generateId(ObjectEntities.CHARACTERISTIC_ENTITY_CODE),
							userId,
							AnalysisUtil.getCharacteristicType(userId, "alarmed", CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL),
							"alarmed",
							"",
							CharacteristicSort._CHARACTERISTIC_SORT_TRANSMISSIONPATH,
							"true",
							null);
					sp.attributes.put("alarmed", ch);
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println("attributes not updated: " + ex.getMessage());
		}
	}
}

