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
 * Renderer for JCheckBox items based on JLabel.
 * @version $Revision: 1.1 $, $Date: 2005/05/19 14:06:41 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class LabelCheckBoxRenderer extends JLabel implements ListCellRenderer {

	private static final long	serialVersionUID	= 6735690924700450480L;

	protected static Border	noFocusBorder;
	
	private static LabelCheckBoxRenderer instance;

	private JComponent	component;
	
	private Wrapper wrapper;
	
	private String key;
	
	protected LabelCheckBoxRenderer(Wrapper wrapper, String key) {
		this();
		this.wrapper = wrapper;
		this.key = key;
	}
	
	private LabelCheckBoxRenderer() {
		super();
		if (noFocusBorder == null) {
			noFocusBorder = new EmptyBorder(1, 1, 1, 1);
		}
		setOpaque(true);
		setBorder(noFocusBorder);
	}
	
	/**
	 * There is no need in more than one instance of this renderer.
	 * @return LabelCheckBoxRenderer instance. 
	 */
	public static LabelCheckBoxRenderer getInstance(){
		if (instance==null)
			instance = new LabelCheckBoxRenderer();
		return instance;
	}

	public Component getListCellRendererComponent(	JList list,
							Object value,
							int index,
							boolean isSelected,
							boolean cellHasFocus) {
		setComponentOrientation(list.getComponentOrientation());
		this.component = this;

		Object object;
		if (this.wrapper == null && this.key == null) {
			object = value;
		} else {
			object = this.wrapper.getValue(value, this.key);
			if (this.wrapper.getPropertyValue(this.key) instanceof Map) {
				Map map = (Map) this.wrapper.getPropertyValue(this.key);
				Object keyObject = null;
				for (Iterator it = map.keySet().iterator(); it.hasNext();) {
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
			setIcon((Icon) object);
			setText("");
		} else {
			if (object instanceof JComponent)
				this.component = (JComponent) object;
			else {
				setIcon(null);
				setText((object == null) ? "" : object.toString());
			}
		}
		
		if (isSelected) {
			this.component.setBackground(list.getSelectionBackground());
			this.component.setForeground(list.getSelectionForeground());
		} else {
			this.component.setBackground(list.getBackground());
			this.component.setForeground(list.getForeground());
		}
		

		this.component.setEnabled(list.isEnabled());
		this.component.setFont(list.getFont());
		this.component.setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
		
		return (this.component == null) ? this : this.component;
	}
	
}
