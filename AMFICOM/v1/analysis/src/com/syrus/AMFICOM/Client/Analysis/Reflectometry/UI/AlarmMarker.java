package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;

class AlarmMarker extends Marker
{
	public AlarmMarker(String name)
	{
		super(name);
	}

	public AlarmMarker (String name, int initial_position)
	{
		super(name, initial_position);
	}

	public boolean canMove()
	{
		return false;
	}

	public Color getColor()
	{
		return Color.pink;
	}
}
