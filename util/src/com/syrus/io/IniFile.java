package com.syrus.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

public class IniFile
{
	byte[] data;
	FileInputStream fis;
	ByteArrayInputStream bais;
	IntelDataInputStream idis;
	FileOutputStream fos;
	PrintWriter pw;
	File f;

	Vector keys;
	Vector values;

	private String tmp = " ";
	private int counter = 0;

	public IniFile(String fileName) throws IOException
	{
		this(new File (fileName));
	}

	public IniFile(File file) throws IOException
	{
		f = file;
		if (!f.exists())
			f.createNewFile();
		fis = new FileInputStream(file);
		data = new byte[(int)file.length()];
		fis.read(data);
		bais = new ByteArrayInputStream(data);
		findKeys();
		bais.close();
		fis.close();
	}

	public Vector getKeys()
	{
		return keys;
	}

	public String getValue (String key)
	{
		int n = keys.indexOf(key);
		if (n == -1)
			return null;
		else
			return (String)values.get(n);
	}

	public String getValue (Object key)
	{
		return getValue((String)key);
	}

	public String getValue(String key, String defaultValue)
	{
		int n = keys.indexOf(key);
		if (n == -1)
		{
			System.out.println("No such key: '" + key + "' defined in ini-file '" + f.getName() + "'; setting default value: " + defaultValue);
			return defaultValue;
		}
		else
			return (String)values.get(n);
	}

	public void setValue (String key, Object val)
	{
		int n = keys.indexOf(key);
		if (n == -1)
		{
			keys.add(key);
			values.add(val.toString());
		}
		else
			values.set(n, val.toString());
	}

	public boolean saveKeys()
	{
		try
		{
			fos = new FileOutputStream (f);
			pw = new PrintWriter(fos, true);
			for (int i = 0; i < keys.size(); i++)
			{
				if (values.get(i) == "")
					pw.println((String)keys.get(i));
				else
					pw.println (String.valueOf(keys.get(i) + " = " + values.get(i)));
			}
			pw.close();
			fos.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	int findKeys ()
	{
		idis = new IntelDataInputStream(bais);
		keys = new Vector();
		values = new Vector();

		while (true)
		{
			try
			{
				tmp = idis.readASCIIString();
				if (tmp == null)
				{
					idis.close();
					break;
				}
				else
				{
					if (tmp.length() < 3 || tmp.charAt(0) == ';')
					{
						keys.add(tmp);
						values.add("");
					}
					else
					{
						counter++;
						int spos = tmp.indexOf(" =");
						if (spos != -1)
						{
							keys.add(tmp.substring(0, spos));
							if (tmp.substring(spos, spos + 1).equals(" "))
								values.add(tmp.substring(spos + 3, tmp.length()));
							else
								values.add(tmp.substring(spos + 2, tmp.length()));
						}
						else
						{
							spos = tmp.indexOf("=");
							if (spos != -1)
							{
								keys.add(tmp.substring(0, spos));
								if (tmp.substring(spos, spos + 1).equals(" "))
									values.add(tmp.substring(spos + 2, tmp.length()));
								else
									values.add(tmp.substring(spos + 1, tmp.length()));
							}
							else
							{
								keys.add(tmp);
								values.add("");
							}
						}
					}
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return counter;
	}
}
