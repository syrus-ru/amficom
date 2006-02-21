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

	@Override
	public boolean canMove()
	{
		return false;
	}

	@Override
	public Color getColor()
	{
		return Color.RED;
	}
}
