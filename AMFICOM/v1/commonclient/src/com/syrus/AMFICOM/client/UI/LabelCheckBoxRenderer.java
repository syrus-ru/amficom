/*-
* $Id: LabelCheckBoxRenderer.java,v 1.9 2006/06/21 08:15:42 saa Exp $
*
* Copyright © 2004-2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI;

import java.awt.Component;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.syrus.util.Wrapper;

/**
 * Renderer for JCheckBox items 
 * @version $Revision: 1.9 $, $Date: 2006/06/21 08:15:42 $
 * @author $Author: saa $
 * @module commonclient
 */
public class LabelCheckBoxRenderer<T> implements ListCellRenderer {

	private static final long serialVersionUID = 6735690924700450480L;

	protected static Border noFocusBorder;

	protected JLabel label;
	
	private static LabelCheckBoxRenderer instance;

	private JComponent component;

	private Wrapper<? super T> wrapper;

	private String key;

	protected LabelCheckBoxRenderer(final Wrapper<? super T> wrapper, final String key) {
		this();
		this.wrapper = wrapper;
		this.key = key;
	}

	private LabelCheckBoxRenderer() {
		super();
		if (noFocusBorder == null) {
			noFocusBorder = new EmptyBorder(1, 1, 1, 1);
		}
		this.label = new JLabel();
		this.label.setOpaque(true);
		this.label.setBorder(noFocusBorder);
	}

	/**
	 * There is no need in more than one instance of this renderer.
	 * 
	 * @return LabelCheckBoxRenderer instance.
	 */
	public static LabelCheckBoxRenderer getInstance() {
		if (instance == null) {
			instance = new LabelCheckBoxRenderer();
		}
		return instance;
	}

	public Component getListCellRendererComponent(final JList list,
			final Object value,
			final int index,
			final boolean isSelected,
			final boolean cellHasFocus) {
		this.label.setComponentOrientation(list.getComponentOrientation());
		this.component = this.label;

		Object object;
		if (this.wrapper == null && this.key == null) {
			object = value;
		} else {
			object = this.wrapper.getValue((T) value, this.key);
			if (this.wrapper.getPropertyValue(this.key) instanceof Map) {
				final Map map = (Map) this.wrapper.getPropertyValue(this.key);
				Object keyObject = null;
				for (final Iterator it = map.keySet().iterator(); it.hasNext();) {
					Object keyObj = it.next();
					if (map.get(keyObj).equals(object)) {
						keyObject = keyObj;
						break;
					}
				}
				object = keyObject;
			}
		}

		if (object instanceof Icon) {
			this.label.setIcon((Icon) object);
			this.label.setText(" ");
		} else {
			if (object instanceof JComponent) {
				this.component = (JComponent) object;
			}
			else {
				this.label.setIcon(null);
				this.label.setText((object == null) ? " " : object.toString());
			}
		}

		if (isSelected) {
			this.label.setBackground(list.getSelectionBackground());
			this.label.setForeground(list.getSelectionForeground());
		} else {
			this.label.setBackground(list.getBackground());
			this.label.setForeground(list.getForeground());
		}

		this.component.setEnabled(list.isEnabled());
		this.label.setFont(list.getFont());
		this.component.setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);

		return (this.component == null) ? this.label : this.component;
	}

}
