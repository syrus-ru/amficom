package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.ReflectometryAnalyseApplicationModelFactory;

public class AnalyseExtRun
{
	public static void main(String[] args)
	{
		Environment.initialize();
//		LangModelAnalyse.initialize();
//		LangModelReport.initialize();

		try {
			//JFrame.setDefaultLookAndFeelDecorated(true);
			//MetalLookAndFeel plaf = (MetalLookAndFeel) MetalLookAndFeel.class.newInstance();
			//SkinLookAndFeel plaf = (SkinLookAndFeel)SkinLookAndFeel.class.newInstance();
			//LiquidLookAndFeel plaf = (LiquidLookAndFeel)LiquidLookAndFeel.class.newInstance();
//			KunststoffLookAndFeel plaf = (KunststoffLookAndFeel)KunststoffLookAndFeel.class.newInstance();
//			KunststoffLookAndFeel.setCurrentTheme(new AMFICOMMetalTheme());

//			UIManager.setLookAndFeel(com.sun.java.swing.plaf.windows.WindowsLookAndFeel.class.getName());
//			UIManager.setLookAndFeel(com.sun.java.swing.plaf.motif.MotifLookAndFeel.class.getName());
			//UIManager.setLookAndFeel(javax.swing.plaf.metal.MetalLookAndFeel.class.getName());
//			UIManager.setLookAndFeel(plaf);

			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		new AnalyseExt(new ReflectometryAnalyseApplicationModelFactory());
	}

}

