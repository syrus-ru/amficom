/*
 * $Id: JObjectResourceList.java,v 1.1 2004/06/24 10:53:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource.Alarm.ui;

import com.syrus.AMFICOM.corba.portable.reflect.*;
import java.awt.*;
import javax.swing.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/24 10:53:57 $
 * @author $Author: bass $
 */
public final class JObjectResourceList extends JList {
	public void updateUI() {
		super.updateUI();
		setBackground(UIManager.getColor("text"));
		setCellRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof AlarmTypeImpl) {
					if (isSelected) {
						setBackground(UIManager.getColor("textText"));
						setForeground(UIManager.getColor("text"));
					} else {
						setBackground(UIManager.getColor("text"));
						setForeground(Color.BLUE);
					}
					setToolTipText(((AlarmTypeImpl) value).getToolTipText());
				} else if (value instanceof UserImpl) {
					if (isSelected) {
						setBackground(UIManager.getColor("textText"));
						setForeground(UIManager.getColor("text"));
					} else {
						setBackground(UIManager.getColor("text"));
						setForeground(Color.BLUE);
					}
					setToolTipText(((UserImpl) value).getToolTipText());
				} else {
					if (isSelected) {
						setBackground(UIManager.getColor("textText"));
						setForeground(UIManager.getColor("text"));
					} else {
						setBackground(UIManager.getColor("text"));
						setForeground(UIManager.getColor("textText"));
					}
					try {
						setToolTipText(value.toString());
					} catch (NullPointerException npe) {
						;
					}
				}
				return this;
			}
		});
	}
}
