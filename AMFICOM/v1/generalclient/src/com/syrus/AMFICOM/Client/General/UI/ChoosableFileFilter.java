package com.syrus.AMFICOM.Client.General.UI;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.filechooser.*;

public class ChoosableFileFilter extends FileFilter
{
	private static String TYPE_UNKNOWN = "Type Unknown";
	private static String HIDDEN_FILE = "Hidden File";

//  private Hashtable filters = null;
	private Vector extentions = null;
	private String description = null;
	private String fullDescription = null;
	private boolean useExtensionsInDescription = true;

	public ChoosableFileFilter()
	{
	//  this.filters = new Hashtable();
		this.extentions = new Vector();
	}

	public ChoosableFileFilter(String extension)
	{
		this (extension,null);
	}

	public ChoosableFileFilter(String extension, String description)
	{
		this();

		if (extension != null)
			addExtension(extension);
		if (description != null)
			setDescription(description);
	}

	public ChoosableFileFilter(String[] filters)
	{
		this (filters, null);
	}

	public ChoosableFileFilter (String[] filters, String description)
	{
		this();
		for (int i = 0; i < filters.length; i++)
			addExtension(filters[i]);

		if (description != null)
			setDescription(description);
	}

	public boolean accept (File f)
	{
		if (f != null)
		{
			if(f.isDirectory())
				return true;

			String extension = getExtension(f);
if (extension != null && extentions.contains(getExtension(f)))
				return true;
		}
		return false;
	}

	public String getExtension (File f)
	{
		if(f != null)
		{
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1)
			{
				return filename.substring(i+1).toLowerCase();
			}
		}
		return null;
	}

	public void addExtension (String extension)
	{
		if(extentions == null)
		{
			extentions = new Vector(5);
		//  filters = new Hashtable(5);
		}
		extentions.add (extension.toLowerCase());
		fullDescription = null;
	}

	public String getDescription()
	{
		if (fullDescription == null)
		{
			if (description == null || isExtensionListInDescription())
			{
				fullDescription = (description == null ? "(" : description + " (");
				// build the description from the extension list
				Enumeration extensions = extentions.elements();
				if(extensions != null)
				{
					fullDescription += "*." + (String) extensions.nextElement();
					while (extensions.hasMoreElements())
						fullDescription += ", *." + (String) extensions.nextElement();
				}
				fullDescription += ")";
			}
			else
				fullDescription = description;
		}
		return fullDescription;
	}

	public void setDescription(String description)
	{
		this.description = description;
		fullDescription = null;
	}

	public void setExtensionListInDescription(boolean b)
	{
		useExtensionsInDescription = b;
		fullDescription = null;
	}

	public boolean isExtensionListInDescription()
	{
		return useExtensionsInDescription;
	}
}
