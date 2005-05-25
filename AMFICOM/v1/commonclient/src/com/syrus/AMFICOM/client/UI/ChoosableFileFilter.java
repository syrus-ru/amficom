
package com.syrus.AMFICOM.client.UI;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

public class ChoosableFileFilter extends FileFilter {

	private List			extentions					= null;
	private String			description					= null;
	private String			fullDescription				= null;
	private boolean			useExtensionsInDescription	= true;

	public ChoosableFileFilter() {
		// this.filters = new Hashtable();
		this.extentions = new LinkedList();
	}

	public ChoosableFileFilter(String extension) {
		this(extension, null);
	}

	public ChoosableFileFilter(String extension, String description) {
		this();

		if (extension != null)
			addExtension(extension);
		if (description != null)
			setDescription(description);
	}

	public ChoosableFileFilter(String[] filters) {
		this(filters, null);
	}

	public ChoosableFileFilter(String[] filters, String description) {
		this();
		for (int i = 0; i < filters.length; i++)
			addExtension(filters[i]);

		if (description != null)
			setDescription(description);
	}

	public boolean accept(File f) {
		if (f != null) {
			if (f.isDirectory())
				return true;

			String extension = getExtension(f);
			if (extension != null && this.extentions.contains(getExtension(f))) {
				return true;
			}
		}
		return false;
	}

	public String getExtension(File f) {
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) { return filename.substring(i + 1).toLowerCase(); }
		}
		return null;
	}

	public void addExtension(String extension) {
		if (this.extentions == null) {
			this.extentions = new LinkedList();
			// filters = new Hashtable(5);
		}
		this.extentions.add(extension.toLowerCase());
		this.fullDescription = null;
	}

	public String getDescription() {
		if (this.fullDescription == null) {
			StringBuffer buffer = new StringBuffer();
			if (this.description == null || isExtensionListInDescription()) {
				buffer.append(this.description == null ? "(" : this.description + " (");
				// build the description from the extension list
				int i = 0;
				for (Iterator iterator = this.extentions.iterator(); iterator.hasNext();i++) {
					String extension = (String) iterator.next();
					if (i == 1) {
						buffer.append(", ");
					}
					buffer.append("*.");
					buffer.append(extension);
				}
				buffer.append(")");
				this.fullDescription = buffer.toString();
			} else
				this.fullDescription = this.description;
		}
		return this.fullDescription;
	}

	public void setDescription(String description) {
		this.description = description;
		this.fullDescription = null;
	}

	public void setExtensionListInDescription(boolean b) {
		this.useExtensionsInDescription = b;
		this.fullDescription = null;
	}

	public boolean isExtensionListInDescription() {
		return this.useExtensionsInDescription;
	}
}
