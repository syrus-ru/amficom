package com.syrus.AMFICOM.Client.Resource.Map;

public class IntPoint implements Cloneable
{
	public int x;

	public int y;

	public IntPoint() 
	{
	}

	public IntPoint(int x, int y) 
	{
	    this.x = x;
	    this.y = y;
	}

	public int getX() 
	{
	    return x;
	}

	public int getY() 
	{
	    return y;
	}

	public void setLocation(int x, int y) 
	{
	    this.x = x;
	    this.y = y;
	}

	public String toString() 
	{
	    return "IntPoint[" + x + ", " + y + "]";
	}

    public void setLocation(IntPoint p) 
	{
		setLocation(p.getX(), p.getY());
    }

    public static double distanceSq(
			int X1, 
			int Y1,
			int X2, 
			int Y2) 
	{
		X1 -= X2;
		Y1 -= Y2;
		return (X1 * X1 + Y1 * Y1);
    }

    public static double distance(
			int X1, 
			int Y1,
			int X2, 
			int Y2) 
	{
		X1 -= X2;
		Y1 -= Y2;
		return Math.sqrt(X1 * X1 + Y1 * Y1);
    }

    public double distanceSq(int PX, int PY) 
	{
		PX -= getX();
		PY -= getY();
		return (PX * PX + PY * PY);
    }

    public double distanceSq(IntPoint pt) 
	{
		int PX = pt.getX() - this.getX();
		int PY = pt.getY() - this.getY();
		return (PX * PX + PY * PY);
    }

    public double distance(int PX, int PY) 
	{
		PX -= getX();
		PY -= getY();
		return Math.sqrt(PX * PX + PY * PY);
    }

    public double distance(IntPoint pt) 
	{
		int PX = pt.getX() - this.getX();
		int PY = pt.getY() - this.getY();
		return Math.sqrt(PX * PX + PY * PY);
    }

    public Object clone() 
	{
		try 
		{
			return super.clone();
		} catch (CloneNotSupportedException e) 
		{
			throw new InternalError();
		}
    }

    public boolean equals(Object obj) 
	{
		if (obj instanceof IntPoint) 
		{
			IntPoint p2d = (IntPoint )obj;
			return (getX() == p2d.getX()) && (getY() == p2d.getY());
		}
		return super.equals(obj);
    }
}
