/*
 * $Id: IniFile.java,v 1.6 2004/07/30 11:29:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.io;

import java.io.*;
import java.util.Vector;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2004/07/30 11:29:21 $
 * @deprecated java.util.prefs will be used instead.
 * @module util
 */
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

	/**
	 * @param fileName
	 * @throws java.io.IOException
	 */
	public IniFile(String fileName) throws IOException
	{
		this(new File (fileName));
	}

	/**
	 * @param file
	 * @throws java.io.IOException
	 */
	public IniFile(File file) throws IOException
	{
		this.f = file;
		if (!this.f.exists())
			this.f.createNewFile();
		this.fis = new FileInputStream(file);
		this.data = new byte[(int)file.length()];
		this.fis.read(this.data);
		this.bais = new ByteArrayInputStream(this.data);
		findKeys();
		this.bais.close();
		this.fis.close();
	}

	/**
	 * @return 
	 */
	public Vector getKeys()
	{
		return this.keys;
	}

	/**
	 * @param key
	 * @return 
	 */
	public String getValue(String key)
	{
		int n = this.keys.indexOf(key);
		if (n == -1)
			return null;
		return (String)this.values.get(n);
	}

	/**
	 * @param key
	 * @return 
	 */
	public String getValue (Object key)
	{
		return getValue((String)key);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return 
	 */
	public String getValue(String key, String defaultValue)
	{
		int n = this.keys.indexOf(key);
		if (n == -1)
		{
			System.out.println("No such key: '" + key + "' defined in ini-file '" + this.f.getName() + "'; setting default value: " + defaultValue);
			return defaultValue;
		}
		return (String)this.values.get(n);
	}

	/**
	 * @param key
	 * @param val
	 */
	public void setValue(String key, Object val)
	{
		int n = this.keys.indexOf(key);
		if (n == -1)
		{
			this.keys.add(key);
			this.values.add(val.toString());
		}
		else
			this.values.set(n, val.toString());
	}

	/**
	 * @return 
	 */
	public boolean saveKeys()
	{
		try
		{
			this.fos = new FileOutputStream (this.f);
			this.pw = new PrintWriter(this.fos, true);
			for (int i = 0; i < this.keys.size(); i++)
			{
				if (this.values.get(i) == "")
					this.pw.println((String)this.keys.get(i));
				else
					this.pw.println (String.valueOf(this.keys.get(i) + " = " + this.values.get(i)));
			}
			this.pw.close();
			this.fos.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @return 
	 */
	int findKeys ()
	{
		this.idis = new IntelDataInputStream(this.bais);
		this.keys = new Vector();
		this.values = new Vector();

		while (true)
		{
			try
			{
				this.tmp = this.idis.readASCIIString();
				if (this.tmp == null)
				{
					this.idis.close();
					break;
				}
				if (this.tmp.length() < 3 || this.tmp.charAt(0) == ';')
				{
					this.keys.add(this.tmp);
					this.values.add("");
				}
				else
				{
					this.counter++;
					int spos = this.tmp.indexOf(" =");
					if (spos != -1)
					{
						this.keys.add(this.tmp.substring(0, spos));
						if (this.tmp.substring(spos, spos + 1).equals(" "))
							this.values.add(this.tmp.substring(spos + 3, this.tmp.length()));
						else
							this.values.add(this.tmp.substring(spos + 2, this.tmp.length()));
					}
					else
					{
						spos = this.tmp.indexOf("=");
						if (spos != -1)
						{
							this.keys.add(this.tmp.substring(0, spos));
							if (this.tmp.substring(spos, spos + 1).equals(" "))
								this.values.add(this.tmp.substring(spos + 2, this.tmp.length()));
							else
								this.values.add(this.tmp.substring(spos + 1, this.tmp.length()));
						}
						else
						{
							this.keys.add(this.tmp);
							this.values.add("");
						}
					}
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return this.counter;
	}
}
