package com.syrus.AMFICOM.newFilter;

import javax.swing.JComponent;

public class DateSpinner extends TimeSpinner
{
	private static final long	serialVersionUID	= 3618697509011076407L;
	protected static String pattern1 = "dd  MMMMMMMMM  yyyy";
	public JComponent getEditor()
	{
		if (super.editor == null)
			super.editor = new TimeSpinnerEditor(this, pattern1);
		return super.editor;
	}

	public static String getPattern()
	{
		return pattern1;
	}

}
