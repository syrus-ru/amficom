
package com.syrus.AMFICOM.client.general.ui;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/24 06:54:50 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class LabelCheckBoxRenderer extends JLabel implements ListCellRenderer {

	protected static Border	noFocusBorder;
	
	private static LabelCheckBoxRenderer instance;

	private JComponent	component;
	
	private LabelCheckBoxRenderer() {
		super();
		if (noFocusBorder == null) {
			noFocusBorder = new EmptyBorder(1, 1, 1, 1);
		}
		setOpaque(true);
		setBorder(noFocusBorder);
	}
	
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
		if (value instanceof Icon) {
			setIcon((Icon) value);
			setText("");
		} else {
			if (value instanceof JComponent)
				this.component = (JComponent) value;
			else {
				setIcon(null);
				setText((value == null) ? "" : value.toString());
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