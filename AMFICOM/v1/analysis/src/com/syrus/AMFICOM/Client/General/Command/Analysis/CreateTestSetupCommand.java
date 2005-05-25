package com.syrus.AMFICOM.Client.General.Command.Analysis;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.io.BellcoreStructure;

public class CreateTestSetupCommand extends AbstractCommand
{
	private ApplicationContext aContext;

	public CreateTestSetupCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new CreateTestSetupCommand(aContext);
	}

	public static String getNewMSNameFromDialog()
	{
		BellcoreStructure bs = Heap.getBSPrimaryTrace();
		if (bs == null) {
			// странная ситуация - нет primarytrace
			return null;
		}

		if (bs.monitoredElementId == null) {
			GUIUtil.showErrorMessage("noMonitoredElementError");
			return null;
		}

		if (Heap.getContextMeasurementSetup() == null) {
			// FIXME: exceptions: вывести сообщение, что-де нет исходного шаблона; а сообщение по bs.monitoredElementId == null, наверное, можно и не выводить(?)
			System.err.println("no testSetup");
			return null;
		}

		String name = JOptionPane.showInputDialog(
				Environment.getActiveWindow(),
				LangModelAnalyse.getString("newname"),
				LangModelAnalyse.getString("testsetup"),
				JOptionPane.QUESTION_MESSAGE);

		// если введен пустое имя
		if (name.equals("")) {
			// FIXME: exceptions/error handling: вывести сообщение об ошибке, что введено пустое имя
			return null; // и в любом случае ничего не делаем
		}

		// если ввод отменен
		if (name == null)
			return null;

		// если введено непустое имя - задаем имя для нового шаблона
		return name;

	}

	public void execute()
	{
		String name = getNewMSNameFromDialog();
		if (name != null)
			Heap.setNewMSName(name);
	}
}
