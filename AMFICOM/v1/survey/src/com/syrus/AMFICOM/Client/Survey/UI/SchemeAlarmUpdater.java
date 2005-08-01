package com.syrus.AMFICOM.Client.Survey.UI;

import java.util.Iterator;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

///Thread для перезагрузки аттрибутов элементов схемы
public class SchemeAlarmUpdater extends Thread  implements Runnable {
	public static final int TIME_INTERVAL = 5000;
	
	SchemeTabbedPane pane;
	ApplicationContext aContext;

	private boolean flag = false;
	public SchemeAlarmUpdater(ApplicationContext aContext, SchemeTabbedPane pane) {
		this.pane = pane;
		this.aContext = aContext;
	}

	public void run() {
		this.flag = true;
		while (this.flag) {
			try {
				updateAttributes();
				sleep( TIME_INTERVAL );
			}
			catch (InterruptedException e) {
				Log.errorMessage(e.getMessage());
			}
		}
	}

	public void updateAttributes() {
		try {
			Scheme scheme = this.pane.getCurrentPanel().getSchemeResource().getScheme();
			
			if (scheme != null) {

			for(Iterator it = scheme.getCurrentSchemeMonitoringSolution().getSchemePaths().iterator(); it.hasNext();) {
				SchemePath sp = (SchemePath)it.next();
				
				for (Iterator it2 = sp.getCharacteristics().iterator(); it.hasNext();) {
					Characteristic ch = (Characteristic)it.next();
					if(ch.getType().getCodename().equals("alarmed"))
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
			}
		}
		catch(Exception ex)
		{
			System.out.println("attributes not updated: " + ex.getMessage());
		}
	}
}

