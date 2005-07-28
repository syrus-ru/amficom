/*-
 * $Id: MapDescriptor.java,v 1.1 2005/07/28 15:39:19 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.map.corba.IdlMapDescriptor;

/**
 * @author max
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/07/28 15:39:19 $
 * @module map_v1
 */

public class MapDescriptor {
	
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
	
	public IdlMapDescriptor getTransferable() {
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
