package com.syrus.AMFICOM.Client.Optimize;

/**
 * <p>Title: NetOptimisation</p>
 * <p>Description: Net topology optimization</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: SYRUS</p>
 * @author Vitaliy V. Shiriaev
 * @version 1.0
 */
// Copyright (c) Syrus Systems 2000-2003 Syrus Systems

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class OptimizeRun
{
  //-------------------------------------------------------------------------------------------------------------
  public static void main(String[] args)
  {	Environment.initialize();
    try
    { //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      //UIManager.setLookAndFeel(javax.swing.plaf.metal.MetalLookAndFeel.class.getName());
      UIManager.setLookAndFeel(Environment.getLookAndFeel());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    LangModelOptimize.initialize();
    LangModelSchematics.initialize();
    new Optimize(new DefaultOptimizeApplicationModelFactory());
  }
  //-------------------------------------------------------------------------------------------------------------

}