package com.syrus.AMFICOM.server;

import org.omg.CORBA.portable.*;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.Principal;
import org.omg.CORBA.Any;

public class ServerTrafficReporterStream extends OutputStream
{
	public int length = 0;

	public ServerTrafficReporterStream()
	{
		super();
	}

	public ServerTrafficReporterStream reset()
	{
		length = 0;
		return this;
	}

	public void set(org.omg.CORBA.portable.InputStream _input)
	{
		try
		{
			length = _input.available();
		}
		catch(Exception e)
		{
		}
	}

	public void set(org.omg.CORBA.portable.OutputStream _output)
	{
		try
		{
			length = _output.create_input_stream().available();
		}
		catch(Exception e)
		{
		}
	}

	public InputStream create_input_stream()
	{
		return null;
	}

	public void write_boolean(boolean value)
	{
		this.length += 1;
	}

	public void write_char(char value)
	{
		this.length += 1;
	}

	public void write_wchar(char value)
	{
		this.length += 2;
	}

	public void write_octet(byte value)
	{
		this.length += 1;
	}

	public void write_short(short value)
	{
		this.length += 2;
	}

	public void write_ushort(short value)
	{
		this.length += 2;
	}

	public void write_long(int value)
	{
		this.length += 4;
	}

	public void write_ulong(int value)
	{
		this.length += 4;
	}

	public void write_longlong(long value)
	{
		this.length += 8;
	}

	public void write_ulonglong(long value)
	{
		this.length += 8;
	}

	public void write_float(float value)
	{
		this.length += 4;
	}

	public void write_double(double value)
	{
		this.length += 8;
	}

	public void write_string(String value)
	{
		this.length += 1 * value.length();
	}

	public void write_wstring(String value)
	{
		this.length += 2 * value.length();
	}


	public void write_boolean_array(boolean[] value, int offset,
								int length)
	{
		this.length += 1 * length;
	}

	public void write_char_array(char[] value, int offset,
								int length)
	{
		this.length += 1 * length;
	}

	public void write_wchar_array(char[] value, int offset,
								int length)
	{
		this.length += 2 * length;
	}

	public void write_octet_array(byte[] value, int offset,
								int length)
	{
		this.length += 1 * length;
	}

	public void write_short_array(short[] value, int offset,
								int length)
	{
		this.length += 2 * length;
	}

	public void write_ushort_array(short[] value, int offset,
								int length)
	{
		this.length += 2 * length;
	}

	public void write_long_array(int[] value, int offset,
								int length)
	{
		this.length += 4 * length;
	}

	public void write_ulong_array(int[] value, int offset,
								int length)
	{
		this.length += 4 * length;
	}

	public void write_longlong_array(long[] value, int offset,
								int length)
	{
		this.length += 8 * length;
	}

	public void write_ulonglong_array(long[] value, int offset,
								int length)
	{
		this.length += 8 * length;
	}

	public void write_float_array(float[] value, int offset,
								int length)
	{
		this.length += 4 * length;
	}

	public void write_double_array(double[] value, int offset,
								int length)
	{
		this.length += 8 * length;
	}


	public void write_Object(org.omg.CORBA.Object value)
	{
	}

	public void write_TypeCode(TypeCode value)
	{
	}

	public void write_any(Any value)
	{
	}


	public void write_Principal(Principal value)
	{
	}


}

