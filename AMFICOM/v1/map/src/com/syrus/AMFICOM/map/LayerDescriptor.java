/*-
 * $Id: LayerDescriptor.java,v 1.5 2005/12/07 17:17:18 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.map.corba.IdlLayerDescriptor;
import com.syrus.util.transport.idl.IdlTransferableObject;

/**
 * @author Maxim Selivanov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/12/07 17:17:18 $
 * @module map
 */
public class LayerDescriptor implements MapFileDescriptor,
		IdlTransferableObject<IdlLayerDescriptor> {
	private static final long serialVersionUID = -1252210481592291979L;

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

	/**
	 * @param orb
	 * @see IdlTransferableObject#getIdlTransferable(ORB)
	 */
	public IdlLayerDescriptor getIdlTransferable(final ORB orb) {
		return this.getIdlTransferable();
	}

	public IdlLayerDescriptor getIdlTransferable() {
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
