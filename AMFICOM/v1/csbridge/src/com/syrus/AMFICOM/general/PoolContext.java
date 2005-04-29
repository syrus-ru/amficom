/*
 * $Id: PoolContext.java,v 1.1 2005/04/29 12:11:28 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/29 12:11:28 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public abstract class PoolContext {

	public abstract void init();

	public abstract void deserialize();

	public abstract void serialize();}
