package com.syrus.AMFICOM.newFilter;

import javax.swing.*;

public class TimeSpinner extends JSpinner
{
	
	private static final long	serialVersionUID	= 3690198763000051254L;
	protected TimeSpinnerEditor editor;
	protected static String pattern = "HH:mm";

	public TimeSpinner()
	{
		super(new SpinnerDateModel());
	}
	
	public static String getPattern()
	{
		return pattern;
	}

	public JComponent getEditor()
	{
		if (this.editor == null)
			this.editor = new TimeSpinnerEditor(this, pattern);
		return this.editor;
	}

	static class TimeSpinnerEditor extends JSpinner.DateEditor
	{
		private static final long serialVersionUID = 3258128063895449650L;

		TimeSpinnerEditor(JSpinner spinner, String pattern)
		{
			super(spinner, pattern);
		}
	}
}
