/*-
 * $Id: ChoosableFileFilter.java,v 1.3 2005/09/09 18:54:27 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/09/09 18:54:27 $
 * @module commonclient
 */

public class ChoosableFileFilter extends FileFilter {

	private List<String> extentions = null;
	private String description = null;
	private String fullDescription = null;
	private boolean useExtensionsInDescription = true;

	public ChoosableFileFilter() {
		// this.filters = new Hashtable();
		this.extentions = new LinkedList<String>();
	}

	public ChoosableFileFilter(final String extension) {
		this(extension, null);
	}

	public ChoosableFileFilter(final String extension, final String description) {
		this();

		if (extension != null) {
			addExtension(extension);
		}
		if (description != null) {
			setDescription(description);
		}
	}

	public ChoosableFileFilter(final String[] filters) {
		this(filters, null);
	}

	public ChoosableFileFilter(final String[] filters, final String description) {
		this();
		for (int i = 0; i < filters.length; i++) {
			addExtension(filters[i]);
		}

		if (description != null) {
			setDescription(description);
		}
	}

	@Override
	public boolean accept(final File f) {
		if (f != null) {
			if (f.isDirectory()) {
				return true;
			}

			final String extension = getExtension(f);
			if (extension != null && this.extentions.contains(getExtension(f))) {
				return true;
			}
		}
		return false;
	}

	public String getExtension(final File f) {
		if (f != null) {
			final String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) {
				return filename.substring(i + 1).toLowerCase();
			}
		}
		return null;
	}

	public void addExtension(final String extension) {
		if (this.extentions == null) {
			this.extentions = new LinkedList<String>();
			// filters = new Hashtable(5);
		}
		this.extentions.add(extension.toLowerCase());
		this.fullDescription = null;
	}

	@Override
	public String getDescription() {
		if (this.fullDescription == null) {
			final StringBuffer buffer = new StringBuffer();
			if (this.description == null || isExtensionListInDescription()) {
				buffer.append(this.description == null ? "(" : this.description + " (");
				// build the description from the extension list
				int i = 0;
				for (final Iterator<String> iterator = this.extentions.iterator(); iterator.hasNext(); i++) {
					final String extension = iterator.next();
					if (i == 1) {
						buffer.append(", ");
					}
					buffer.append("*.");
					buffer.append(extension);
				}
				buffer.append(")");
				this.fullDescription = buffer.toString();
			} else {
				this.fullDescription = this.description;
			}
		}
		return this.fullDescription;
	}

	public void setDescription(final String description) {
		this.description = description;
		this.fullDescription = null;
	}

	public void setExtensionListInDescription(final boolean b) {
		this.useExtensionsInDescription = b;
		this.fullDescription = null;
	}

	public boolean isExtensionListInDescription() {
		return this.useExtensionsInDescription;
	}
}
