package com.syrus.io;

public class Rewriter 
{
//	public static final const long serialVersionUID = 01L;

	private Rewriter()
	{
	}

	public static byte write(byte b)
	{
		return b;
	}

	public static char write(char c)
	{
		short s1 = (short )c;
		short s2 = 0;
		short bit;
		
		for(int i = 0; i < 16; i++)
		{
			bit = (short )(s1 & 0x0001);
			s1 >>= 1;
			s2 <<= 1;
			s2 |= bit;
		}
		
		return (char )s2;
	}

	public static int write(int i)
	{
		return i;
	}

	public static long write(long l)
	{
		return l;
	}

	public static byte[] write(String s)
	{
    	return s.getBytes();
/*
		boolean order = false;
		String s2 = "";
		for(int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			char c2 = write(c);
			if(order)
				s2 = String.valueOf(c2) + s2;
			else
				s2 = s2 + String.valueOf(c2);
			order = !order;
		}
        byte[] bytes = s2.getBytes();
        return bytes;
*/
//		return s2;
	}
	
	public static byte read(byte b)
	{
		return b;
	}

	public static char read(char c)
	{
		return write(c);
	}

	public static int read(int i)
	{
		return i;
	}

	public static long read(long l)
	{
		return l;
	}

	public static String read(byte[] bytes)
	{
        String s = new String(bytes);
        return s;
/*
		boolean order = ((s.length() % 2) == 0);
		String s2 = "";
		int count = s.length();
		for(int i = 0; i < count; i++)
		{
			char c;
			if(order)
			{
				c = s.charAt(0);
				s = s.substring(1, s.length());
			}
			else
			{
				c = s.charAt(s.length() - 1);
				s = s.substring(0, s.length() - 1);
			}

			char c2 = read(c);
			System.out.println("decode '" + String.valueOf(c) + "' [" +
					String.valueOf((int )c) + "] as '" +
					c2 + "' [" + String.valueOf((int )c2) + "]");
			s2 = String.valueOf(c2) + s2;
			order = !order;
		}
		return s2;
*/
	}
}
