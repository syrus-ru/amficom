
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Schedule;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import java.awt.*;
import javax.swing.*;

public class ScheduleRun
{
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

		new Schedule(new DefaultScheduleApplicationModelFactory());
	}
}

