package com.syrus.AMFICOM.Client.General.UI;

import javax.swing.*;

public class TimeSpinner extends JSpinner
{
	protected TimeSpinnerEditor editor;
	protected static String pattern = "HH:mm";

	public TimeSpinner()
	{
		super(new SpinnerDateModel());
	}
	
	static public String getPattern()
	{
		return pattern;
	}

	public JComponent getEditor()
	{
		if (editor == null)
			editor = new TimeSpinnerEditor(this, pattern);
		return editor;
	}

	static class TimeSpinnerEditor extends JSpinner.DateEditor
	{
		TimeSpinnerEditor(JSpinner spinner, String pattern)
		{
			super(spinner, pattern);
		}
	}
}


