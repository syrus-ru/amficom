package com.syrus.AMFICOM.newFilter;

import javax.swing.*;

public class DateSpinner extends TimeSpinner
{
	private static final long	serialVersionUID	= 3618697509011076407L;
	protected static String pattern = "dd  MMMMMMMMM  yyyy";
	public JComponent getEditor()
	{
		if (editor == null)
			editor = new TimeSpinnerEditor(this, pattern);
		return editor;
	}

	public static String getPattern()
	{
		return pattern;
	}

}
