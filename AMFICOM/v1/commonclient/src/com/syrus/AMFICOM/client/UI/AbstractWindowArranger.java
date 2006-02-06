
package com.syrus.AMFICOM.client.UI;

import java.beans.PropertyVetoException;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

public abstract class AbstractWindowArranger {

	protected JFrame	mainframe;

	public AbstractWindowArranger(JFrame mainframe) {
		this.mainframe = mainframe;
	}

	public abstract void arrange();

	public static void normalize(JInternalFrame f) {
		setIconified(f, false);
		setMaximized(f, false);
	}

	public static void setIconified(JInternalFrame frame,
									boolean iconify) {
		if (iconify ^ frame.isIcon()) {
			try {
				frame.setIcon(iconify);
			} catch (PropertyVetoException pve) {
				pve.printStackTrace();
			}
		}
	}

	public static void setMaximized(JInternalFrame frame,
									boolean maximize) {
		if (maximize ^ frame.isMaximum()) {
			try {
				frame.setMaximum(maximize);
			} catch (PropertyVetoException pve) {
				pve.printStackTrace();
			}
		}
	}
}
