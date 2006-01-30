/*-
 * $Id: Notifier.java,v 1.24 2006/01/30 14:51:11 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import static com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent.ALL_DESELECTED;
import static com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent.MULTIPLE;
import static com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent.OTHER_OBJECT;
import static com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent.SCHEME;
import static com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent.SCHEME_CABLELINK;
import static com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent.SCHEME_CABLEPORT;
import static com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent.SCHEME_DEVICE;
import static com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent.SCHEME_ELEMENT;
import static com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent.SCHEME_LINK;
import static com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent.SCHEME_PATH;
import static com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent.SCHEME_PORT;
import static com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent.SCHEME_PROTOELEMENT;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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
import com.syrus.AMFICOM.client_.scheme.graph.objects.IdentifiableCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.Rack;
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
 * @author $Author: stas $
 * @version $Revision: 1.24 $, $Date: 2006/01/30 14:51:11 $
 * @module schemeclient
 */

public class Notifier {
	private Notifier() {
		// empty
	}

	public static void notify(SchemeGraph graph, ApplicationContext aContext, Object object) {
		if (SchemeGraph.getMode().equals(Constants.RACK_MODE)) {
			Log.debugMessage("do not notify in rack mode", Level.FINEST);
			return;
		}
		
		Dispatcher dispatcher = aContext.getDispatcher();
		if (object == null) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, Collections.<Identifiable>emptySet(), null,
					ALL_DESELECTED));
			return;
		}

		if (object instanceof SchemeElement) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemeElementPropertiesManager.getInstance(aContext),
					SCHEME_ELEMENT));
		} else if (object instanceof SchemeDevice) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemeDevicePropertiesManager.getInstance(aContext),
					SCHEME_DEVICE));
		} else if (object instanceof SchemeLink) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemeLinkPropertiesManager.getInstance(aContext),
					SCHEME_LINK));
		} else if (object instanceof SchemeCableLink) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemeCableLinkPropertiesManager.getInstance(aContext),
					SCHEME_CABLELINK));
		} else if (object instanceof SchemePort) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemePortPropertiesManager.getInstance(aContext),
					SCHEME_PORT));
		} else if (object instanceof SchemeCablePort) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemeCablePortPropertiesManager.getInstance(aContext),
					SCHEME_CABLEPORT));
		} else if (object instanceof SchemeProtoElement) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemeProtoElementPropertiesManager.getInstance(aContext),
					SCHEME_PROTOELEMENT));
		} else if (object instanceof SchemePath) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemePathPropertiesManager.getInstance(aContext),
					SCHEME_PATH));
		} else if (object instanceof Scheme) {
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemePropertiesManager.getInstance(aContext), SCHEME));
		}
//		TODO write visual managers
		/*
		else if (object instanceof SchemePath)
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object,
					SchemePathPropertiesManager.getInstance(),
					SCHEME_PATH));
		else
			Log.debugMessage("unsupported object selection: " + object, Log.WARNING);	
					*/
	}
	
	public static void notify(SchemeGraph graph, ApplicationContext aContext, Object[] cells) {
		try {
		Dispatcher dispatcher = aContext.getDispatcher();
		if (cells.length == 0) {
			Log.debugMessage(Notifier.class.getSimpleName() + " | all deselected", Level.FINEST); //$NON-NLS-1$
			dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, Collections.<Identifiable>emptySet(), null, ALL_DESELECTED));
			return;
		} else if (cells.length == 1) {
			Identifiable selectedObject = null;
			long selectedType = 0;
			VisualManager manager = null;
			Object object = cells[0];
			
			if (object instanceof DeviceGroup) {
				DeviceGroup dev = (DeviceGroup)object;
				if (dev.getType() == DeviceGroup.PROTO_ELEMENT) {
					selectedObject = dev.getProtoElement();
					selectedType = SCHEME_PROTOELEMENT;
					manager = SchemeProtoElementPropertiesManager.getInstance(aContext);
				} else if (dev.getType() == DeviceGroup.SCHEME_ELEMENT) {
					SchemeElement el = dev.getSchemeElement();
					if (el.getKind() == IdlSchemeElementKind.SCHEME_CONTAINER) {
						selectedObject = dev.getScheme();
						selectedType = SCHEME;
						manager = SchemePropertiesManager.getInstance(aContext);
					} else {
						selectedObject = dev.getSchemeElement();
						selectedType = SCHEME_ELEMENT;
						manager = SchemeElementPropertiesManager.getInstance(aContext);
					}
				} 
				// check use of this case
//				else if (dev.getType() == DeviceGroup.SCHEME) {
//					selectedObject = dev.getScheme();
//					selectedType = SCHEME;
//					manager = SchemePropertiesManager.getInstance(aContext);
//				}
			}	else if (object instanceof Rack) {
				Rack rack = (Rack)object;
				selectedObject = rack.getSchemeElement();
				selectedType = SCHEME_ELEMENT;
				manager = SchemeElementPropertiesManager.getInstance(aContext);
			} else if (object instanceof DefaultLink) {
				DefaultLink link = (DefaultLink)object;
				if (link.getSchemeLinkId() != null) {
					selectedObject = link.getSchemeLink();
					selectedType = SCHEME_LINK;
					manager = SchemeLinkPropertiesManager.getInstance(aContext);
				}
			} else if (object instanceof DefaultCableLink) {
				DefaultCableLink link = (DefaultCableLink)object;
				if (link.getSchemeCableLinkId() != null) {
					selectedObject = link.getSchemeCableLink();
					selectedType = SCHEME_CABLELINK;
					manager = SchemeCableLinkPropertiesManager.getInstance(aContext);
				}
			} else if (object instanceof PortCell) {
				PortCell port = (PortCell)object;
				if (port.getSchemePortId() != null) {
					selectedObject = port.getSchemePort();
					selectedType = SCHEME_PORT;
					manager = SchemePortPropertiesManager.getInstance(aContext);
				}
			} else if (object instanceof CablePortCell) {
				CablePortCell port = (CablePortCell)object;
				if (port.getSchemeCablePortId() != null) {
					selectedObject = port.getSchemeCablePort();
					selectedType = SCHEME_CABLEPORT;
					manager = SchemeCablePortPropertiesManager.getInstance(aContext);
				}
			} else if (object instanceof DeviceCell) {
				DeviceCell dev = (DeviceCell)object;
				if (dev.getSchemeDeviceId() != null) {
					selectedObject = dev.getSchemeDevice();
					selectedType = SCHEME_DEVICE;
					manager = SchemeDevicePropertiesManager.getInstance(aContext);
				}
			} else if (object instanceof TopLevelElement) {
				TopLevelElement top = (TopLevelElement)object;
				if (top.getSchemeId() != null) {
					selectedObject = top.getScheme();
					selectedType = SCHEME;
					manager = SchemePropertiesManager.getInstance(aContext);
				}
			}  
			
			if (selectedType == 0 || selectedObject == null) {
				String message = Notifier.class.getSimpleName() + " | selected other object " + 
						object.getClass().getSimpleName();
				if (object instanceof IdentifiableCell) {
					message += " (identifier " + ((IdentifiableCell)object).getId() + ")";
				}
				Log.debugMessage(message, Level.FINEST);
				dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, object, null, OTHER_OBJECT));
			} else {
				if (SchemeGraph.getMode().equals(Constants.RACK_MODE)) {
					selectedType += ObjectSelectedEvent.INRACK;
				}
				Log.debugMessage(Notifier.class.getSimpleName() + " | selected object with id " + selectedObject.getId() , Level.FINEST); //$NON-NLS-1$
				dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, selectedObject, manager, selectedType));
			}
		} else if (cells.length > 1) {
			// only Ports
			long selectedType = 0;
			VisualManager manager = null;
			Set<Object> selectedObjects = new HashSet<Object>();
			for (Object cell : cells) {
				if (cell instanceof PortCell) {
					selectedType = ObjectSelectedEvent.SCHEME_PORT;
					manager = SchemePortPropertiesManager.getInstance(aContext);
					selectedObjects.add(((PortCell)cell).getSchemePort());
				} else {
					selectedType = 0;
					break;
				}
			}
			if (selectedType == 0) {
				for (Object cell : cells) {
					if (cell instanceof CablePortCell) {
						selectedType = ObjectSelectedEvent.SCHEME_CABLEPORT;
						manager = SchemeCablePortPropertiesManager.getInstance(aContext);
						selectedObjects.add(((CablePortCell)cell).getSchemeCablePort());
					} else {
						selectedType = 0;
						break;
					}
				}
			}
			if (selectedType == 0) {
				for (Object cell : cells) {
					if (cell instanceof DeviceGroup && ((DeviceGroup)cell).getType() == DeviceGroup.SCHEME_ELEMENT) {
						selectedType = ObjectSelectedEvent.SCHEME_ELEMENT;
						manager = SchemeElementPropertiesManager.getInstance(aContext);
						selectedObjects.add(((DeviceGroup)cell).getSchemeElement());
					} else {
						selectedType = 0;
						break;
					}
				}
			}
			if (selectedType != 0) {
				if (SchemeGraph.getMode().equals(Constants.RACK_MODE)) {
					selectedType += ObjectSelectedEvent.INRACK;
				}
				dispatcher.firePropertyChange(new ObjectSelectedEvent(graph, selectedObjects, 
						manager, MULTIPLE + selectedType));
			}
		}
		} catch (Exception e) {
			Log.errorMessage(e);
		}
	}
}
