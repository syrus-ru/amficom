package com.syrus.AMFICOM.Client.General.UI;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

public class HourSpinner extends JSpinner {

	protected TimeSpinnerEditor	editor;
	protected static String		pattern	= "HH:mm";

	public HourSpinner() {
		super(new SpinnerDateModel());
	}

	public JComponent getEditor() {
		if (editor == null) editor = new TimeSpinnerEditor(this, pattern);
		return editor;
	}

	static class TimeSpinnerEditor extends JSpinner.DateEditor {

		TimeSpinnerEditor(JSpinner spinner, String pattern) {
			super(spinner, pattern);
		}
	}
}

