/*
 * $Id: AdministrateRun.java,v 1.2 2004/06/29 11:34:56 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAdmin;
import com.syrus.AMFICOM.Client.General.Model.*;
import javax.swing.UIManager;

/**
 * @version $Revision: 1.2 $, $Date: 2004/06/29 11:34:56 $
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
		Environment.initialize();
		try
		{
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		LangModelAdmin.initialize();
		new Administrate(new DefaultAdminApplicationModelFactory());
	}
}
