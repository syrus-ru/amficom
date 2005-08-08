/*-
 * $Id: LayerDescriptor.java,v 1.2 2005/08/08 11:35:11 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.map.corba.IdlLayerDescriptor;

/**
 * 
 * @author Maxim Selivanov
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/08/08 11:35:11 $
 * @module map
 */

public class LayerDescriptor {
	
	private String fileName;
	private String pathName;
	private long length;
	private long lastModified;
	
	public LayerDescriptor(String fileName, String pathName, long sizeOfFile, long lastModified) {
		this.fileName = fileName;
		this.pathName = pathName;
		this.length = sizeOfFile;
		this.lastModified = lastModified;
	}
	
	public LayerDescriptor(IdlLayerDescriptor descriptor) {
		this.fileName = descriptor.fileName;
		this.pathName = descriptor.pathName;
		this.length = descriptor.length;
		this.lastModified = descriptor.lastModified;
	}

	public IdlLayerDescriptor getTransferable() {
		return new IdlLayerDescriptor(this.fileName, this.pathName, this.length, this.lastModified);
	}

	public String getFileName() {
		return this.fileName;
	}

	public long getLastModified() {
		return this.lastModified;
	}

	public String getPathName() {
		return this.pathName;
	}

	public long getLength() {
		return this.length;
	}
	
}
