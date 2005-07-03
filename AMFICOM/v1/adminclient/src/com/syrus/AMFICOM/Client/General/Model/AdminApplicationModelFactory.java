/*
 * $Id: AdminApplicationModelFactory.java,v 1.2 2004/09/27 16:26:52 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Model;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/09/27 16:26:52 $
 * @module admin_v1
 */
public class AdminApplicationModelFactory implements ApplicationModelFactory {
	public ApplicationModel create() {
		return new AdminApplicationModel();
	}
}
