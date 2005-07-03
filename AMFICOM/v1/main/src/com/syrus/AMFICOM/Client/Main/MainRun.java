/*
 * $Id: MainRun.java,v 1.1 2004/07/20 12:57:15 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Main;

import com.syrus.AMFICOM.Client.General.Model.*;
import javax.swing.UIManager;

/**
 * @version $Revision: 1.1 $
 * @author $Author: krupenn $
 * @module main_v1
 */
public class MainRun
{
	private MainRun()
	{
	}

	public static void main(String[] args)
	{
		Environment.initialize();
		try
		{
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		new Main(new DefaultMainApplicationModelFactory());
	}
}
