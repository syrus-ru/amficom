package com.syrus.AMFICOM.Client.General.Scheme;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeDevice;

public class DeviceCell extends DefaultGraphCell
{
	private static final long serialVersionUID = 01L;
	String scheme_device_id;

	public DeviceCell()
	{
		this(null);
	}

	public DeviceCell(Object userObject)
	{
		super(userObject);
	}

	public SchemeDevice getSchemeDevice()
	{
		return (SchemeDevice)Pool.get(SchemeDevice.typ, scheme_device_id);
	}

	public String getSchemeDeviceId()
	{
		return scheme_device_id;
	}

	public void setSchemeDeviceId(String id)
	{
		scheme_device_id = id;
	}
/*
	private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException
	{
		out.writeObject(scheme_device_id);
	}
*/
/*
	public Map changeAttributes(Map change)
	{
		Rectangle _bounds =	(Rectangle)attributes.get(GraphConstants.BOUNDS);

		Map undo = super.changeAttributes(change);

		if (!_bounds.equals(GraphConstants.getBounds(attributes)))
		{
			Rectangle bounds = GraphConstants.getBounds(attributes);

			double u = (double)GraphConstants.PERCENT;

			if (_bounds.height != bounds.height)
			{
				if (_bounds.y != bounds.y)
				{
					//System.out.println("Upside changed");
				}
				else
				{
					//System.out.println("Downside changed");
				}
			}
			else if (_bounds.width != bounds.width)
			{
				if (_bounds.x != bounds.x)
				{
					//System.out.println("Leftside changed");
				}
				else
				{
					//System.out.println("Rightside changed");
				}
			}
			else
				;
				//System.out.println("Vertex moved");
		}
		else
		;
			//System.out.println("Bounds not changed");
		return undo;
	}*/
}

