/*-
 * $Id: Notifier.java,v 1.1 2005/04/18 09:55:03 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.scheme.ui.*;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeElementPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.graph.objects.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 09:55:03 $
 * @module schemeclient_v1
 */

public class Notifier {
	private Notifier() {
		// empty
	}

	public static void notify(SchemeGraph graph, ApplicationContext aContext, Object object) {
		Dispatcher dispatcher = aContext.getDispatcher();
		if (object == null) {
			dispatcher.notify(new ObjectSelectedEvent(graph, object, null,
					ObjectSelectedEvent.ALL_DESELECTED));
			return;
		}

		if (object instanceof SchemeElement)
			dispatcher.notify(new ObjectSelectedEvent(graph, object,
					SchemeElementPropertiesManager.getInstance(aContext),
					ObjectSelectedEvent.SCHEME_ELEMENT));
		else if (object instanceof SchemeDevice)
			dispatcher.notify(new ObjectSelectedEvent(graph, object,
					SchemeDevicePropertiesManager.getInstance(aContext),
					ObjectSelectedEvent.SCHEME_DEVICE));
		else if (object instanceof SchemeLink)
			dispatcher.notify(new ObjectSelectedEvent(graph, object,
					SchemeLinkPropertiesManager.getInstance(aContext),
					ObjectSelectedEvent.SCHEME_LINK));
		else if (object instanceof SchemeCableLink)
			dispatcher.notify(new ObjectSelectedEvent(graph, object,
					SchemeCableLinkPropertiesManager.getInstance(aContext),
					ObjectSelectedEvent.SCHEME_CABLELINK));
		else if (object instanceof SchemePort)
			dispatcher.notify(new ObjectSelectedEvent(graph, object,
					SchemePortPropertiesManager.getInstance(aContext),
					ObjectSelectedEvent.SCHEME_PORT));
		else if (object instanceof SchemeCablePort)
			dispatcher.notify(new ObjectSelectedEvent(graph, object,
					SchemeCablePortPropertiesManager.getInstance(aContext),
					ObjectSelectedEvent.SCHEME_CABLEPORT));
		else if (object instanceof SchemeProtoElement)
			dispatcher.notify(new ObjectSelectedEvent(graph, object,
					SchemeProtoElementPropertiesManager.getInstance(aContext),
					ObjectSelectedEvent.SCHEME_PROTOELEMENT));
		
		//TODO write visual managers
/*		
		else if (object instanceof Scheme)
			dispatcher.notify(new ObjectSelectedEvent(graph, object,
					SchemePropertiesManager.getInstance(), ObjectSelectedEvent.SCHEME));
		else if (object instanceof SchemePath)
			dispatcher.notify(new ObjectSelectedEvent(graph, object,
					SchemePathPropertiesManager.getInstance(),
					ObjectSelectedEvent.SCHEME_PATH));
		else
			Log.debugMessage("unsupported object selection: " + object, Log.WARNING);	
					*/
	}
	
	public static void notify(SchemeGraph graph, ApplicationContext aContext, Object[] cells) {
		Dispatcher dispatcher = aContext.getDispatcher();
		if (cells.length == 0) {
			dispatcher.notify(new ObjectSelectedEvent(graph, null, null,
					ObjectSelectedEvent.ALL_DESELECTED));
			return;
		}
		else if (cells.length == 1) {
			Object selectedObject = null;
			long selectedType = 0;
			VisualManager manager = null;
			Object object = cells[0];
			
			if (object instanceof DeviceGroup) {
				DeviceGroup dev = (DeviceGroup)object;
				if (dev.getScheme() != null) {
					selectedObject = dev.getScheme();
					selectedType = ObjectSelectedEvent.SCHEME;
//					manager = SchemePropertiesManager.getInstance();
				} 
				else if (dev.getSchemeElementId() != null) {
					selectedObject = dev.getSchemeElement();
					selectedType = ObjectSelectedEvent.SCHEME_ELEMENT;
					manager = SchemeElementPropertiesManager.getInstance(aContext);
				} 
				else if (dev.getProtoElementId() != null) {
					selectedObject = dev.getProtoElement();
					selectedType = ObjectSelectedEvent.SCHEME_PROTOELEMENT;
					manager = SchemeProtoElementPropertiesManager.getInstance(aContext);
				}
			}
			else if (object instanceof DeviceCell) {
				DeviceCell dev = (DeviceCell)object;
				if (dev.getSchemeDeviceId() != null) {
					selectedObject = dev.getSchemeDevice();
					selectedType = ObjectSelectedEvent.SCHEME_DEVICE;
					manager = SchemeDevicePropertiesManager.getInstance(aContext);
				}
			}
			else if (object instanceof DefaultLink) {
				DefaultLink link = (DefaultLink)object;
				if (link.getSchemeLinkId() != null) {
					selectedObject = link.getSchemeLink();
					selectedType = ObjectSelectedEvent.SCHEME_LINK;
					manager = SchemeLinkPropertiesManager.getInstance(aContext);
				}
			}
			else if (object instanceof DefaultCableLink) {
				DefaultCableLink link = (DefaultCableLink)object;
				if (link.getSchemeCableLinkId() != null) {
					selectedObject = link.getSchemeCableLink();
					selectedType = ObjectSelectedEvent.SCHEME_CABLELINK;
					manager = SchemeCableLinkPropertiesManager.getInstance(aContext);
				}
			}
			else if (object instanceof PortCell) {
				PortCell port = (PortCell)object;
				if (port.getSchemePortId() != null) {
					selectedObject = port.getSchemePort();
					selectedType = ObjectSelectedEvent.SCHEME_PORT;
					manager = SchemePortPropertiesManager.getInstance(aContext);
				}
			}
			else if (object instanceof CablePortCell) {
				CablePortCell port = (CablePortCell)object;
				if (port.getSchemeCablePortId() != null) {
					selectedObject = port.getSchemeCablePort();
					selectedType = ObjectSelectedEvent.SCHEME_CABLEPORT;
					manager = SchemeCablePortPropertiesManager.getInstance(aContext);
				}
			}
			if (selectedType == 0) {
				Log.debugMessage("unsupported object selection: " + object, Log.WARNING);
				dispatcher.notify(new ObjectSelectedEvent(graph, null, null,
						ObjectSelectedEvent.ALL_DESELECTED));
			}
			else {
				dispatcher.notify(new ObjectSelectedEvent(graph, selectedObject, manager,
						selectedType));
			}
		}
		else if (cells.length > 1) {
			// TODO multiple selection
		}
	}
}
