package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;

class Marker
{
	int pos;
	String id;
	String name;
	SimpleGraphPanel parent;

	public Marker(String name)
	{
		this(name, 0);
	}

	public Marker (String name, int initial_position)
	{
		pos = initial_position;
		this.name = name;
		id = "marker"+String.valueOf(System.currentTimeMillis());
	}

	void move (int new_position)
	{
		pos = new_position;
	}

	void checkBounds(int min, int max)
	{
		if (pos < min)
			pos = min;
		if (pos > max)
			pos = max;
	}

	public boolean canMove()
	{
		return true;
	}

	public Color getColor()
	{
		return Color.black;
	}
}
