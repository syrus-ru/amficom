/*
 * $Id: Entry.java,v 1.1 2004/06/01 14:09:14 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util;

import java.nio.channels.FileLock;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/01 14:09:14 $
 */
class Entry implements CacheLockObject {
	public static final int READ_LOCK = 0;

	public static final int WRITE_LOCK = 1;

	int type;

	FileLock lock;

	Object stream;

	String filename;

	Entry(int type, FileLock lock, String filename, Object stream) {
		this.type = type;
		this.lock = lock;
		this.filename = filename;
		this.stream = stream;
	}

	public Object getResource() {
		return stream;
	}
}
