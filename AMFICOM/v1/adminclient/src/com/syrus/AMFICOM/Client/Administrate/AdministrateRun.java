/*
 * $Id: AdministrateRun.java,v 1.4 2004/08/05 12:04:15 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate;

import com.syrus.AMFICOM.Client.General.Model.*;
import javax.swing.UIManager;

/**
 * @version $Revision: 1.4 $, $Date: 2004/08/05 12:04:15 $
 * @author $Author: bass $
 * @module admin_v1
 */
public class AdministrateRun
{
	private AdministrateRun()
	{
	}

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		new Administrate(new DefaultAdminApplicationModelFactory());
	}
}
