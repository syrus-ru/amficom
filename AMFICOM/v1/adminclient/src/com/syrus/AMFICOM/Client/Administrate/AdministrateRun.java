/*
 * $Id: AdministrateRun.java,v 1.3 2004/07/13 12:19:10 peskovsky Exp $
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
 * @version $Revision: 1.3 $, $Date: 2004/07/13 12:19:10 $
 * @author $Author: peskovsky $
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
		new Administrate(new DefaultAdminApplicationModelFactory());
	}
}
