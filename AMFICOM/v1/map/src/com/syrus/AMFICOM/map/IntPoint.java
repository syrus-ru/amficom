package com.syrus.AMFICOM.map;

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

    public double distanceSq(int px, int py) 
	{
		px -= getX();
		py -= getY();
		return (px * px + py * py);
    }

    public double distanceSq(IntPoint pt) 
	{
		int px = pt.getX() - this.getX();
		int py = pt.getY() - this.getY();
		return (px * px + py * py);
    }

    public double distance(int px, int py) 
	{
		px -= getX();
		py -= getY();
		return Math.sqrt(px * px + py * py);
    }

    public double distance(IntPoint pt) 
	{
		int px = pt.getX() - this.getX();
		int py = pt.getY() - this.getY();
		return Math.sqrt(px * px + py * py);
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
