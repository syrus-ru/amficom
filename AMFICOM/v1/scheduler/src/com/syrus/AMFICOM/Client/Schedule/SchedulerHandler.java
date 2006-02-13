/*-
* $Id: SchedulerHandler.java,v 1.1 2006/02/13 12:22:54 bob Exp $
*
* Copyright ¿ 2006 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Schedule;

import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.Client.Schedule.UI.ParametersTestPanel;
import com.syrus.AMFICOM.extensions.AbstractExtensionHandler;
import com.syrus.AMFICOM.extensions.scheduler.ParameterHandler;
import com.syrus.AMFICOM.extensions.scheduler.ParametersHandler;
import com.syrus.AMFICOM.extensions.scheduler.SchedulerExtensions;
import com.syrus.AMFICOM.extensions.scheduler.SchedulerResource;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.1 $, $Date: 2006/02/13 12:22:54 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public class SchedulerHandler extends AbstractExtensionHandler<SchedulerExtensions> {

	private SchedulerExtensions	resources;

	private Map<String, ParametersTestPanel> parametersTestPanels;
	
	private final static String PARAMETERS_HANDLER_NAME = ParametersHandler.type.getName().getLocalPart();

	public SchedulerHandler() {
		this.parametersTestPanels = new HashMap<String, ParametersTestPanel>();
	}
	
	public void addHandlerData(final SchedulerExtensions resources) {
		this.resources = resources;
	}
	
	public ParametersTestPanel getParametersTestPanel(final String id) {
		final ParametersTestPanel parametersTestPanel = this.parametersTestPanels.get(id);
		if (parametersTestPanel != null) {
			return parametersTestPanel;
		}
		
		final SchedulerResource[] schedulerResourceArray = this.resources.getSchedulerResourceArray();
		
		for (final SchedulerResource resource : schedulerResourceArray) {
			if (resource.getId().equals(PARAMETERS_HANDLER_NAME)) {
				ParametersHandler parametersHandler = (ParametersHandler) resource;
				ParameterHandler parameterHandler = parametersHandler.getParameterHandler();
				final String id2 = parameterHandler.getId();
				assert Log.debugMessage("Id:" + id2 + ", id need:" + id, Log.DEBUGLEVEL03);
				if (id2.equals(id)) {						
					ParametersTestPanel testPanel = (ParametersTestPanel) 
						super.loadHandler(parameterHandler.getHandlerClass(), 
							new Class[] {}, 
							new Object[] {});
					this.parametersTestPanels.put(id, testPanel);
					return testPanel;
				}
			}
		}
		
		return null;
	}
}

