package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;
import com.syrus.AMFICOM.general.*;

class Marker
{
	int pos;
	private Identifier id;
	String name;
	SimpleGraphPanel parent;
	Color color = Color.BLACK;

	public Marker(String name)
	{
		this(name, 0);
	}

	public Marker (String name, int initial_position)
	{
		pos = initial_position;
		this.name = name;
		try {
			id = LocalIdentifierGenerator.generateIdentifier(ObjectEntities.MARK_ENTITY_CODE);
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}
	}

	public Identifier getId()
	{
		return id;
	}

	public void setId(Identifier id)
	{
		this.id = id;;
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
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}
}
