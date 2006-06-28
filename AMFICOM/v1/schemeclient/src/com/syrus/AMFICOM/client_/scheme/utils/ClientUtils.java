/*-
 * $Id: ClientUtils.java,v 1.10 2006/06/06 12:49:09 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.utils;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.CableThreadTypeWrapper;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.corba.IdlPortTypePackage.PortTypeKind;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeCablePortWrapper;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.AMFICOM.scheme.SchemeCableThreadWrapper;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemePortWrapper;
import com.syrus.util.Log;

public class ClientUtils {
	private ClientUtils() {
		assert false;
	}

	public static String parseNumberedName(final String name) {
		char[] chars = name.toCharArray();
		int i = chars.length - 1;
		while (i >= 0 && Character.isDigit(chars[i])) {
			i--;
		}
		if (i == chars.length - 1) {
			return name;
		}
		return name.substring(i + 1);
	}
	
	public static List<SchemeCableThread> getSortedCableThreads(SchemeCableLink link) throws ApplicationException {
		Set<SchemeCableThread> schemeCableThreads = link.getSchemeCableThreads(false);
		List<SchemeCableThread> threads = new ArrayList<SchemeCableThread>(schemeCableThreads);
		Collections.sort(threads, new NumberedComparator<SchemeCableThread>(SchemeCableThreadWrapper.getInstance(),
				StorableObjectWrapper.COLUMN_NAME));
		return threads;
	}
	
	public static List<SchemePort> getSortedPorts(Set<SchemePort> schemePorts) {
		List<SchemePort> ports = new ArrayList<SchemePort>(schemePorts);
		Collections.sort(ports, new NumberedComparator<SchemePort>(SchemePortWrapper.getInstance(),
				StorableObjectWrapper.COLUMN_NAME));
		return ports;
	}
	
	public static List<SchemeCablePort> getSortedCablePorts(Set<SchemeCablePort> schemeCablePorts) {
		List<SchemeCablePort> ports = new ArrayList<SchemeCablePort>(schemeCablePorts);
		Collections.sort(ports, new NumberedComparator<SchemeCablePort>(SchemeCablePortWrapper.getInstance(),
				StorableObjectWrapper.COLUMN_NAME));
		return ports;
	}
	
	public static Characteristic getCharacteristic(final Collection<Characteristic> characteristics, final String codename) {
		for (Characteristic characteristic : characteristics) {
			if (characteristic.getType().getCodename().equals(codename)) {
				return characteristic;
			}
		}
		return null;
	}
	
	public static List<CableThreadType> getSortedThreadTypes(CableLinkType type) {
		List<CableThreadType> threads = new ArrayList<CableThreadType>(type.getCableThreadTypes(false));
		Collections.sort(threads, new NumberedComparator<CableThreadType>(CableThreadTypeWrapper.getInstance(),
				StorableObjectWrapper.COLUMN_NAME));
		return threads;
	}
	
	public static boolean hasEqualType(Set<Identifiable> objects) {
		short entity = objects.iterator().next().getId().getMajor();
		for (Identifiable identifiable2 : objects) {
			if (identifiable2.getId().getMajor() != entity) {
				return false;
			}
		}
		return true;
	}
	
	public static long getEventType(Identifiable object) {
		Identifier id = object.getId();
		short major = id.getMajor();
		switch (major) {
		case ObjectEntities.PORT_TYPE_CODE:
			PortType type;
				try {
					type = StorableObjectPool.getStorableObject(id, true);
					if (type.getKind().equals(PortTypeKind.PORT_KIND_SIMPLE))
						return ObjectSelectedEvent.PORT_TYPE;
					return ObjectSelectedEvent.CABLEPORT_TYPE;
				} catch (ApplicationException e) {
					Log.errorMessage(e);
					return ObjectSelectedEvent.OTHER_OBJECT;
				}
		case ObjectEntities.MEASUREMENTPORT_TYPE_CODE:
			return ObjectSelectedEvent.MEASUREMENTPORT_TYPE;
		case ObjectEntities.LINK_TYPE_CODE:
			return ObjectSelectedEvent.LINK_TYPE; 
		case ObjectEntities.CABLELINK_TYPE_CODE:
			return ObjectSelectedEvent.CABLELINK_TYPE;
		case ObjectEntities.PROTOEQUIPMENT_CODE:
			return ObjectSelectedEvent.PROTO_EQUIPMENT;
		case ObjectEntities.SCHEMEPROTOGROUP_CODE:
			return ObjectSelectedEvent.SCHEME_PROTOGROUP;
		case ObjectEntities.SCHEMEPROTOELEMENT_CODE:
			return ObjectSelectedEvent.SCHEME_PROTOELEMENT;
		case ObjectEntities.SCHEME_CODE:
			return ObjectSelectedEvent.SCHEME;
		case ObjectEntities.SCHEMEELEMENT_CODE:
			return ObjectSelectedEvent.SCHEME_ELEMENT;
		case ObjectEntities.SCHEMELINK_CODE:
			return ObjectSelectedEvent.SCHEME_LINK;
		case ObjectEntities.SCHEMECABLELINK_CODE:
			return ObjectSelectedEvent.SCHEME_CABLELINK;
		case ObjectEntities.SCHEMEPATH_CODE:
			return ObjectSelectedEvent.SCHEME_PATH;
		case ObjectEntities.SCHEMEPORT_CODE:
			return ObjectSelectedEvent.SCHEME_PORT;
		case ObjectEntities.SCHEMECABLEPORT_CODE:
			return ObjectSelectedEvent.SCHEME_CABLEPORT;
		default:
			return ObjectSelectedEvent.OTHER_OBJECT;			
		}
	}
	
	static JOptionPane optionPane;
	static JDialog dialog;
	static int result;
	static Object[] buttons;
	public static boolean showConfirmDialog(String text) {
		return showConfirmDialog(null, text);
	}
	
	public static boolean showConfirmDialog(JComponent component, String text) {
		if (optionPane == null) {
			JButton okButton = new JButton();
			okButton.setText(LangModelScheme.getString("Button.OK")); //$NON-NLS-1$)
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ClientUtils.result = JOptionPane.OK_OPTION;
					dialog.dispose();
				}
			});
			JButton cancelButton = new JButton();
			cancelButton.setText(LangModelScheme.getString("Button.Cancel")); //$NON-NLS-1$
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ClientUtils.result = JOptionPane.CANCEL_OPTION;
					dialog.dispose();
				}
			});
			buttons = new Object[] { okButton, cancelButton };
		}
			if (component != null) {
				JPanel panel = new JPanel(new BorderLayout());
				panel.add(component, BorderLayout.CENTER);
				panel.add(new JLabel(text), BorderLayout.SOUTH);
				optionPane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE,
						JOptionPane.OK_CANCEL_OPTION, null, buttons, null);
			} else {
					optionPane = new JOptionPane(text, JOptionPane.QUESTION_MESSAGE,
					JOptionPane.OK_CANCEL_OPTION, null, buttons, null);
			}	
			dialog = optionPane.createDialog(AbstractMainFrame.getActiveMainFrame(), 
					LangModelScheme.getString("Message.confirmation")); //$NON-NLS-1$
			dialog.setModal(true);

		dialog.setVisible(true);
		return result == JOptionPane.OK_OPTION;
	}
}
