/*-
 * $Id: MapDescriptor.java,v 1.6 2006/03/27 11:21:42 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.io.Serializable;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.map.corba.IdlMapDescriptor;
import com.syrus.util.transport.idl.IdlTransferableObject;

/**
 * @author max
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2006/03/27 11:21:42 $
 * @module map
 */
public class MapDescriptor implements MapFileDescriptor, Serializable,
		IdlTransferableObject<IdlMapDescriptor> {
	private static final long serialVersionUID = -2322785940339779652L;

	private String mapName;
	private String fileName;
	private String filePathName;
	private long length;
	private long lastModified;
	
	public MapDescriptor(String mapName, String fileName, String filePathName, long length, long lastModified) {
		this.mapName = mapName;
		this.fileName = fileName;
		this.filePathName = filePathName;
		this.length = length;
		this.lastModified = lastModified;
	}
	
	public MapDescriptor(IdlMapDescriptor idlMapDescriptor) {
		this.mapName = idlMapDescriptor.name;
		this.fileName = idlMapDescriptor.fileName;
		this.filePathName = idlMapDescriptor.filePathName;
		this.length = idlMapDescriptor.length;
		this.lastModified = idlMapDescriptor.lastModified;
	}

	/**
	 * @param orb
	 * @see IdlTransferableObject#getIdlTransferable(ORB)
	 */
	public IdlMapDescriptor getIdlTransferable(final ORB orb) {
		return this.getIdlTransferable();
	}
	
	public IdlMapDescriptor getIdlTransferable() {
		return new IdlMapDescriptor(this.mapName, this.fileName, this.filePathName, this.length, this.lastModified);
	}

	public String getMapName() {
		return this.mapName;
	}

	public String getFileName() {
		return this.fileName;
	}
	
	public String getFilePathName() {
		return this.filePathName;
	}
	
	public long getLength() {
		return this.length;
	}
	
	public long getLastModified() {
		return this.lastModified;
	}
}
