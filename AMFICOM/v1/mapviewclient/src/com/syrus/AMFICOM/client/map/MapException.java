/*-
 * $$Id: MapException.java,v 1.8 2005/10/11 08:56:11 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;

/**
 * @version $Revision: 1.8 $, $Date: 2005/10/11 08:56:11 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapException extends Exception {
	private static final long serialVersionUID = -3759623595362764062L;

	public static final String DEFAULT_STRING = I18N.getString(
			MapEditorResourceKeys.ERROR_MAP_EXCEPTION_SERVER_CONNECTION);
    public MapException() {
		super();
	    }

	public MapException(String message) {
		super(message);
	}

	public MapException(String message, Throwable cause) {
		super(message, cause);
	}

	public MapException(Throwable cause) {
		super(cause);
	}
}
