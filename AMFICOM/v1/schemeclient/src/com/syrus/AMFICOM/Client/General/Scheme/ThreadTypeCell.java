/*
 * $Id: ThreadTypeCell.java,v 1.1 2005/02/21 14:14:56 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Scheme;

import com.jgraph.pad.EllipseCell;
import com.syrus.AMFICOM.configuration.CableThreadType;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/02/21 14:14:56 $
 * @module schemeclient_v1
 */

public class ThreadTypeCell extends EllipseCell
{
	private static final long serialVersionUID = 01L;
	private CableThreadType threadType;

	public ThreadTypeCell()
	{
		this(null);
	}

	public ThreadTypeCell(Object userObject)
	{
		super(userObject);
	}

	public CableThreadType getCableThreadType()
	{
		return threadType;
	}

	public void setCableThreadType(CableThreadType threadType)
	{
		this.threadType = threadType;
	}
}