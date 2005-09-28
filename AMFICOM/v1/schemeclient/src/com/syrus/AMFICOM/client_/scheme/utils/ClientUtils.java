/*-
 * $Id: ClientUtils.java,v 1.5 2005/09/28 07:53:10 stas Exp $
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

import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.CableThreadTypeWrapper;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.AMFICOM.scheme.SchemeCableThreadWrapper;

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
	
	public static List<SchemeCableThread> getSortedCableThreads(SchemeCableLink link) {
		Set<SchemeCableThread> schemeCableThreads = link.getSchemeCableThreads();
		List<SchemeCableThread> threads = new ArrayList<SchemeCableThread>(schemeCableThreads);
		Collections.sort(threads, new NumberedComparator<SchemeCableThread>(SchemeCableThreadWrapper.getInstance(),
				StorableObjectWrapper.COLUMN_NAME));
		return threads;
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
			okButton.setText(LangModelGeneral.getString("Button.OK")); //$NON-NLS-1$)
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ClientUtils.result = JOptionPane.OK_OPTION;
					dialog.dispose();
				}
			});
			JButton cancelButton = new JButton();
			cancelButton.setText(LangModelGeneral.getString("Button.Cancel")); //$NON-NLS-1$
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
			dialog = optionPane.createDialog(Environment.getActiveWindow(), 
					LangModelScheme.getString("Message.confirmation")); //$NON-NLS-1$
			dialog.setModal(true);

		dialog.setVisible(true);
		return result == JOptionPane.OK_OPTION;
	}
}
