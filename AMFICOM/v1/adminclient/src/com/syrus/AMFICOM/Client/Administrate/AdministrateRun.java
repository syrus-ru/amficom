package com.syrus.AMFICOM.Client.Administrate;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class AdministrateRun
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
    LangModelAdmin.initialize();
    new Administrate(new DefaultAdminApplicationModelFactory());
  }
}
