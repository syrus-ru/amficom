/*
 * $Id: ServerProcessName.java,v 1.1 2004/06/22 09:57:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.process;

import java.util.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 09:57:10 $
 * @module serverprocess
 */
public final class ServerProcessName 
{
	private ServerProcessName()
	{
	}

	public static final String ID_KISTRACER = "kistracer";
	public static final String ID_EVENTTRACER = "eventtracer";
	public static final String ID_REPORTER = "reporter";
	public static final String ID_ALERTER = "alerter";
	public static final String ID_USERTRACER = "usertracer";
	public static final String ID_RESULTSETCHECKER = "resultsetchecker";
	public static final String ID_SCHEDULER = "scheduler";

	private static final String[] PRIVATE_PROCESS_NAMES =
	{
		ID_KISTRACER,
		ID_EVENTTRACER,
		ID_REPORTER,
		ID_ALERTER,
		ID_USERTRACER,
		ID_RESULTSETCHECKER,
		ID_SCHEDULER
	};

	public static final List PROCESS_NAMES = Collections.unmodifiableList(Arrays.asList(PRIVATE_PROCESS_NAMES));
}
