package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JLabel;

import com.syrus.AMFICOM.CORBA.General.TestStatus;

public class ObjectResourceLabel extends JLabel implements Comparable 
{
	private ObjectResource or;
	private int	status;

	public ObjectResourceLabel(ObjectResource or) 
	{
		super((or == null) ? "" : or.getName());
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

	public ObjectResource getOR()
	{
		return or;
	}
}
