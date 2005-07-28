/*-
 * $Id: LayerDescriptor.java,v 1.1 2005/07/28 15:39:19 max Exp $
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
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/07/28 15:39:19 $
 * @module map_v1
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
