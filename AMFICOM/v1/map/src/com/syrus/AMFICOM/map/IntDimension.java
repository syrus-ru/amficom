package com.syrus.AMFICOM.map;

public class IntDimension implements java.io.Serializable 
{
    
    public int width;

    public int height;


    public IntDimension() 
	{
		this(0, 0);
    }

    public IntDimension(IntDimension d) 
	{
		this(d.width, d.height);
    }

    public IntDimension(int width, int height) 
	{
		this.width = width;
		this.height = height;
    }

    /**
     * Returns the width of this dimension in double precision.
     * @return the width of this dimension in double precision
     */
    public double getWidth() 
	{
		return width;
    }

    public double getHeight() 
	{
		return height;
    }

    public void setSize(double width, double height) 
	{
		this.width = (int) Math.ceil(width);
		this.height = (int) Math.ceil(height);
    }

    public IntDimension getSize() 
	{
		return new IntDimension(width, height);
    }	

    public void setSize(IntDimension d) 
	{
		setSize(d.width, d.height);
    }	

    public void setSize(int width, int height) 
	{
    	this.width = width;
    	this.height = height;
    }	

    public boolean equals(Object obj) 
	{
		if (obj instanceof IntDimension) {
			IntDimension d = (IntDimension)obj;
			return (width == d.width) && (height == d.height);
		}
		return false;
    }

    public int hashCode() 
	{
        int sum = width + height;
        return sum * (sum + 1)/2 + width;
    }

    public String toString() 
	{
		return getClass().getName() + "[width=" + width + ",height=" + height + "]";
    }
}