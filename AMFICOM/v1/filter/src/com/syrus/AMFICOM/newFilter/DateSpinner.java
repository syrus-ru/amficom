package com.syrus.AMFICOM.newFilter;

import javax.swing.*;

public class DateSpinner extends TimeSpinner
{
	protected static String pattern = "dd  MMMMMMMMM  yyyy";
	public JComponent getEditor()
	{
		if (editor == null)
			editor = new TimeSpinnerEditor(this, pattern);
		return editor;
	}

	static public String getPattern()
	{
		return pattern;
	}

}
