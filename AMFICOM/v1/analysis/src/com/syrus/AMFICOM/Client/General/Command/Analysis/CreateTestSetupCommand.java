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
			// �������� �������� - ��� primarytrace
			return null;
		}

		if (bs.monitoredElementId == null) {
			GUIUtil.showErrorMessage("noMonitoredElementError");
			return null;
		}

		if (Heap.getContextMeasurementSetup() == null) {
			// FIXME: exceptions: ������� ���������, ���-�� ��� ��������� �������; � ��������� �� bs.monitoredElementId == null, ��������, ����� � �� ��������(?)
			System.err.println("no testSetup");
			return null;
		}

		String name = JOptionPane.showInputDialog(
				Environment.getActiveWindow(),
				LangModelAnalyse.getString("newname"),
				LangModelAnalyse.getString("testsetup"),
				JOptionPane.QUESTION_MESSAGE);

		// ���� ������ ������ ���
		if (name.equals("")) {
			// FIXME: exceptions/error handling: ������� ��������� �� ������, ��� ������� ������ ���
			return null; // � � ����� ������ ������ �� ������
		}

		// ���� ���� �������
		if (name == null)
			return null;

		// ���� ������� �������� ��� - ������ ��� ��� ������ �������
		return name;

	}

	public void execute()
	{
		String name = getNewMSNameFromDialog();
		if (name != null)
			Heap.setNewMSName(name);
	}
}
