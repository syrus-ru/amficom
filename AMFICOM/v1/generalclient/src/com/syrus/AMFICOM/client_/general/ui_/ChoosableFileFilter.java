package com.syrus.AMFICOM.client_.general.ui_;

import java.io.File;
import java.util.*;

import javax.swing.filechooser.FileFilter;

public class ChoosableFileFilter extends FileFilter
{
	private static String TYPE_UNKNOWN = "Type Unknown";
	private static String HIDDEN_FILE = "Hidden File";

	private List extentions = null;
	private String description = null;
	private String fullDescription = null;
	private boolean useExtensionsInDescription = true;

	public ChoosableFileFilter()
	{
		this.extentions = new LinkedList();
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
			extentions = new LinkedList();

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
				Iterator extensions = extentions.iterator();
				if(extensions != null)
				{
					fullDescription += "*." + (String) extensions.next();
					while (extensions.hasNext())
						fullDescription += ", *." + (String) extensions.next();
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
