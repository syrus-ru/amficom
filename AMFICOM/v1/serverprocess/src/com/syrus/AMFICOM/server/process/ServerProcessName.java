/*
 * $Id: ServerProcessName.java,v 1.3 2004/12/23 11:59:51 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.process;

import java.util.*;

/**
 * @version $Revision: 1.3 $, $Date: 2004/12/23 11:59:51 $
 * @module serverprocess
 */
public final class ServerProcessName 
{
	private ServerProcessName()
	{
	}

	public static final String ID_ALERTER = "alerter";
	public static final String ID_USERTRACER = "usertracer";
	public static final String ID_RESULTSETCHECKER = "resultsetchecker";

	private static final String[] PRIVATE_PROCESS_NAMES =
	{
		ID_ALERTER,
		ID_USERTRACER,
		ID_RESULTSETCHECKER
	};

	public static final List PROCESS_NAMES = Collections.unmodifiableList(Arrays.asList(PRIVATE_PROCESS_NAMES));
}
