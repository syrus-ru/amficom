package com.syrus.AMFICOM.Client.Map.Props;

import javax.swing.JLabel;

public class ObjectResourceLabel extends JLabel implements Comparable 
{
	private Object or;
	private int	status;

	public ObjectResourceLabel(Object or, String text) 
	{
		super(text);
		this.or = or;
		setOpaque(true);
	}

	public int compareTo(Object o) 
	{
		int result = 0;
		if (o instanceof ObjectResourceLabel)
		{
			ObjectResourceLabel orl = (ObjectResourceLabel )o;
			result = this.getText().compareTo(orl.getText());
		}
		return result;
	}

	public Object getOR()
	{
		return or;
	}
}
