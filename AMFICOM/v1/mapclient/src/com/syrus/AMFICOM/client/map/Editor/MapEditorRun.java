
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Map.Editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class MapEditorRun
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
		LangModelConfig.initialize();
		LangModelMap.initialize();
		new MapEditor(new DefaultMapEditorApplicationModelFactory());
	}
}

