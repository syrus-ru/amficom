/*-
 * $Id: LayerDescriptor.java,v 1.3 2005/08/22 06:17:27 max Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/08/22 06:17:27 $
 * @module map
 */

public class LayerDescriptor implements MapFileDescriptor {
	
	private String fileName;
	private String filePathName;
	private long length;
	private long lastModified;
	
	public LayerDescriptor(String fileName, String pathName, long sizeOfFile, long lastModified) {
		this.fileName = fileName;
		this.filePathName = pathName;
		this.length = sizeOfFile;
		this.lastModified = lastModified;
	}
	
	public LayerDescriptor(IdlLayerDescriptor descriptor) {
		this.fileName = descriptor.fileName;
		this.filePathName = descriptor.pathName;
		this.length = descriptor.length;
		this.lastModified = descriptor.lastModified;
	}

	public IdlLayerDescriptor getTransferable() {
		return new IdlLayerDescriptor(this.fileName, this.filePathName, this.length, this.lastModified);
	}

	public String getFileName() {
		return this.fileName;
	}

	public long getLastModified() {
		return this.lastModified;
	}

	public String getFilePathName() {
		return this.filePathName;
	}

	public long getLength() {
		return this.length;
	}
	
}
