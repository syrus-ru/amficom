/*-
 * $Id: Notifier.java,v 1.13 2005/09/07 18:33:01 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.util.logging.Level;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.TopLevelElement;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeCableLinkPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeCablePortPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeDevicePropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeElementPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeLinkPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemePathPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemePortPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemePropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeProtoElementPropertiesManager;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.13 $, $Date: 2005/09/07 18:33:01 $
 * @module schemeclient
 */

public class Notifier {
	private Notifier() {
		// empty
	}

	public static void notify(SchemeGraph graph, ApplicationContext aContext, Object object) {
		Dispatcher dispatcher = aContext.getDispatcher();
		if (object == null) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object, null,
					ObjectSelectedEvent.ALL_DESELECTED));
			return;
		}

		if (object instanceof SchemeElement) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemeElementPropertiesManager.getInstance(aContext),
					ObjectSelectedEvent.SCHEME_ELEMENT));
		} else if (object instanceof SchemeDevice) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemeDevicePropertiesManager.getInstance(aContext),
					ObjectSelectedEvent.SCHEME_DEVICE));
		} else if (object instanceof SchemeLink) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemeLinkPropertiesManager.getInstance(aContext),
					ObjectSelectedEvent.SCHEME_LINK));
		} else if (object instanceof SchemeCableLink) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemeCableLinkPropertiesManager.getInstance(aContext),
					ObjectSelectedEvent.SCHEME_CABLELINK));
		} else if (object instanceof SchemePort) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemePortPropertiesManager.getInstance(aContext),
					ObjectSelectedEvent.SCHEME_PORT));
		} else if (object instanceof SchemeCablePort) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemeCablePortPropertiesManager.getInstance(aContext),
					ObjectSelectedEvent.SCHEME_CABLEPORT));
		} else if (object instanceof SchemeProtoElement) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemeProtoElementPropertiesManager.getInstance(aContext),
					ObjectSelectedEvent.SCHEME_PROTOELEMENT));
		} else if (object instanceof SchemePath) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemePathPropertiesManager.getInstance(aContext),
					ObjectSelectedEvent.SCHEME_PATH));
		} else if (object instanceof Scheme) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemePropertiesManager.getInstance(aContext), ObjectSelectedEvent.SCHEME));
		}
//		TODO write visual managers
		/*
		else if (object instanceof SchemePath)
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemePathPropertiesManager.getInstance(),
					ObjectSelectedEvent.SCHEME_PATH));
		else
			Log.debugMessage("unsupported object selection: " + object, Log.WARNING);	
					*/
	}
	
	public static void notify(SchemeGraph graph, ApplicationContext aContext, Object[] cells) {
		try {
		Dispatcher dispatcher = aContext.getDispatcher();
		if (cells.length == 0) {
			Log.debugMessage(Notifier.class.getSimpleName() + " | all deselected", Level.FINEST); //$NON-NLS-1$
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, null, null, ObjectSelectedEvent.ALL_DESELECTED));
			return;
		} else if (cells.length == 1) {
			Object selectedObject = null;
			long selectedType = 0;
			VisualManager manager = null;
			Object object = cells[0];
			
			if (object instanceof DeviceGroup) {
				DeviceGroup dev = (DeviceGroup)object;
				if (dev.getType() == DeviceGroup.PROTO_ELEMENT) {
					selectedObject = dev.getProtoElement();
					selectedType = ObjectSelectedEvent.SCHEME_PROTOELEMENT;
					manager = SchemeProtoElementPropertiesManager.getInstance(aContext);
				} else if (dev.getType() == DeviceGroup.SCHEME_ELEMENT) {
					SchemeElement el = dev.getSchemeElement();
					if (el.getKind().value() == IdlSchemeElementKind._SCHEME_CONTAINER) {
						selectedObject = dev.getScheme();
						selectedType = ObjectSelectedEvent.SCHEME;
						manager = SchemePropertiesManager.getInstance(aContext);
					} else {
						selectedObject = dev.getSchemeElement();
						selectedType = ObjectSelectedEvent.SCHEME_ELEMENT;
						manager = SchemeElementPropertiesManager.getInstance(aContext);
					}
				} 
//				else if (dev.getType() == DeviceGroup.SCHEME) {
//					selectedObject = dev.getScheme();
//					selectedType = ObjectSelectedEvent.SCHEME;
//					manager = SchemePropertiesManager.getInstance(aContext);
//				}
			} else if (object instanceof DefaultLink) {
				DefaultLink link = (DefaultLink)object;
				if (link.getSchemeLinkId() != null) {
					selectedObject = link.getSchemeLink();
					selectedType = ObjectSelectedEvent.SCHEME_LINK;
					manager = SchemeLinkPropertiesManager.getInstance(aContext);
				}
			} else if (object instanceof DefaultCableLink) {
				DefaultCableLink link = (DefaultCableLink)object;
				if (link.getSchemeCableLinkId() != null) {
					selectedObject = link.getSchemeCableLink();
					selectedType = ObjectSelectedEvent.SCHEME_CABLELINK;
					manager = SchemeCableLinkPropertiesManager.getInstance(aContext);
				}
			} else if (object instanceof PortCell) {
				PortCell port = (PortCell)object;
				if (port.getSchemePortId() != null) {
					selectedObject = port.getSchemePort();
					selectedType = ObjectSelectedEvent.SCHEME_PORT;
					manager = SchemePortPropertiesManager.getInstance(aContext);
				}
			} else if (object instanceof CablePortCell) {
				CablePortCell port = (CablePortCell)object;
				if (port.getSchemeCablePortId() != null) {
					selectedObject = port.getSchemeCablePort();
					selectedType = ObjectSelectedEvent.SCHEME_CABLEPORT;
					manager = SchemeCablePortPropertiesManager.getInstance(aContext);
				}
			} else if (object instanceof DeviceCell) {
				DeviceCell dev = (DeviceCell)object;
				if (dev.getSchemeDeviceId() != null) {
					selectedObject = dev.getSchemeDevice();
					selectedType = ObjectSelectedEvent.SCHEME_DEVICE;
					manager = SchemeDevicePropertiesManager.getInstance(aContext);
				}
			} else if (object instanceof TopLevelElement) {
				TopLevelElement top = (TopLevelElement)object;
				if (top.getSchemeId() != null) {
					selectedObject = top.getScheme();
					selectedType = ObjectSelectedEvent.SCHEME;
					manager = SchemePropertiesManager.getInstance(aContext);
				}
			}  
			
			if (selectedType == 0 || selectedObject == null) {
				Log.debugMessage(Notifier.class.getSimpleName() + " | selected other object " + object.getClass().getSimpleName(), Level.FINEST); //$NON-NLS-1$
				dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object, null, ObjectSelectedEvent.OTHER_OBJECT));
			} else {
				Log.debugMessage(Notifier.class.getSimpleName() + " | selected object with id " + ((Identifiable)selectedObject).getId() , Level.FINEST); //$NON-NLS-1$
				dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, selectedObject, manager, selectedType));
			}
		} else if (cells.length > 1) {
			// TODO multiple selection
		}
		} catch (Exception e) {
			Log.errorException(e);
			graph.clearSelection();
		}
	}
}
