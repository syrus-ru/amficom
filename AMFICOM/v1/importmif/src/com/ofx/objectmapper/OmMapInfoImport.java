// Decompiled by Jad v1.5.7f. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: fullnames lnc 

package com.ofx.objectmapper;

import com.ofx.base.SxEnvironment;
import com.ofx.base.SxLogInterface;
import com.ofx.base.SxProperties;
import com.ofx.base.SxStateChange;
import com.ofx.collections.SxLexicographicAscendingStringComparator;
import com.ofx.collections.SxQSort;
import com.ofx.dataAdapter.SxDataAdapter;
import com.ofx.dataAdapter.SxDelimitedFieldDefinition;
import com.ofx.dataAdapter.SxDelimitedRecordDefinition;
import com.ofx.dataAdapter.SxImportException;
import com.ofx.dataAdapter.SxImportFactory;
import com.ofx.dataAdapter.SxRecordDefinition;
import com.ofx.dataAdapter.SxSpatialDataTransferInterface;
import com.ofx.query.SxQueryInterface;
import com.ofx.query.SxQueryRetrievalInterface;
import com.ofx.query.SxQueryTransactionInterface;
import com.ofx.repository.SxClass;
import com.ofx.repository.SxRepositoryException;
import com.ofx.repository.SxSymbology;
import com.ofx.repository.SxTextSpec;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;
import java.util.Vector;
import symantec.itools.awt.BorderPanel;
import symantec.itools.multimedia.ImageViewer;

// Referenced classes of package com.ofx.objectmapper:
//			OmFrame, OmMapInfoDescriptor, FilenameExtensionFilter, OmModifyMapInfoFilter, 
//			OmImportReport, SxObservableDispatcher

public class OmMapInfoImport extends com.ofx.objectmapper.OmFrame
	implements java.util.Observer, com.ofx.dataAdapter.SxDataAdapter, java.awt.event.ActionListener, java.awt.event.ItemListener, java.awt.event.WindowListener
{

	public static com.ofx.objectmapper.OmMapInfoImport getFrame()
	{
		if(com.ofx.objectmapper.OmMapInfoImport._theFrame == null)
		{
			com.ofx.objectmapper.OmMapInfoImport._theFrame = new OmMapInfoImport();
		} else
		{
			com.ofx.objectmapper.OmMapInfoImport._theFrame.clearList();
			com.ofx.objectmapper.OmMapInfoImport._theFrame.statusLabel.setText("");
		}
		return com.ofx.objectmapper.OmMapInfoImport._theFrame;
	}

	public static void resetFrame()
	{
		com.ofx.objectmapper.OmMapInfoImport._theFrame = null;
	}

	void mifFilenameFld_Entered(java.awt.event.ActionEvent actionevent)
	{
		java.lang.String s = mifFilenameFld.getText();
		if(!s.equals(""))
		{
			java.io.File file = new File(s);
			if(file.exists())
			{
				if(!s.endsWith("/") && !s.endsWith(java.io.File.separator))
					mifFilenameFld.setText(s + java.io.File.separator);
				selectedDir = mifFilenameFld.getText();
				updateFileList();
			} else
			{
				showWarning("The directory '" + s + "' does not exist.");
			}
		}
	}

	void clearButton_Clicked(java.awt.event.ActionEvent actionevent)
	{
		clearList();
		statusLabel.setText("         ");
	}

	void importButton_Clicked(java.awt.event.ActionEvent actionevent)
	{
		importAll();
	}

	void browseButton_Clicked(java.awt.event.ActionEvent actionevent)
	{
		openMifFileDialog.setFile(".mif");
		openMifFileDialog.setVisible(true);
		selectedDir = openMifFileDialog.getDirectory();
		java.lang.String s = openMifFileDialog.getFile();
		if(selectedDir != null && s != null)
		{
			mifFilenameFld.setText(selectedDir);
			updateFileList();
		}
	}

	void deleteButton_Clicked(java.awt.event.ActionEvent actionevent)
	{
		int i = readyToImpList.getSelectedIndex();
		if(i != -1)
		{
			if(mifFilenamesList.getSelectedIndex() != -1)
				mifFilenamesList.deselect(mifFilenamesList.getSelectedIndex());
			java.lang.String s = fileNames[i];
			desDict.remove(s);
			readyToImpList.remove(i);
			for(int j = i; j < readyToImpList.getItemCount(); j++)
				fileNames[j] = fileNames[j + 1];

			lineCount = lineCount - 1;
			disableButtons();
		}
	}

	void modifyButton_Clicked(java.awt.event.ActionEvent actionevent)
	{
		int i = readyToImpList.getSelectedIndex();
		omModifyFilter = com.ofx.objectmapper.OmModifyMapInfoFilter.getFrame();
		java.lang.String s = fileNames[i];
		omModifyFilter.setDescriptor((com.ofx.objectmapper.OmMapInfoDescriptor)desDict.get(s));
		omModifyFilter.addObserver(this);
		omModifyFilter.setVisible(true);
	}

	void readyToImpList_ListSelect(java.awt.event.ItemEvent itemevent)
	{
		if(itemAdded)
		{
			modifyButton.setEnabled(true);
			deleteButton.setEnabled(true);
		}
	}

	void mifFilenamesList_ListSelect(java.awt.event.ItemEvent itemevent)
	{
		selectedFile = mifFilenamesList.getSelectedItem();
		java.lang.String s = selectedFile.substring(0, selectedFile.length() - 4);
		if(!desDict.containsKey(s))
		{
			statusLabel.setText("           Examining selected MIF/MID files...");
			updateReadyToImportList();
			itemAdded = true;
			statusLabel.setText("           ");
		}
	}

	private void importAll()
	{
		if(lineCount == 0)
		{
			showWarning("Select the files to import.");
		} else
		{
			if(importReport != null)
				importReport.setVisible(false);
			importReport = com.ofx.objectmapper.OmImportReport.getDialog(this, "MID/MIF Import Status Report", false);
			importReport.setVisible(true);
			importAction();
			clearList();
		}
	}

	public com.ofx.base.SxEnvironment environment()
	{
		return com.ofx.base.SxEnvironment.singleton();
	}

	private void importAction()
	{
		com.ofx.dataAdapter.SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition();
		Object obj = null;
		for(int i = 0; i < lineCount; i++)
		{
			java.lang.String s = fileNames[i];
			com.ofx.objectmapper.OmMapInfoDescriptor ommapinfodescriptor = (com.ofx.objectmapper.OmMapInfoDescriptor)desDict.get(s);
			createClass(ommapinfodescriptor);
			com.ofx.dataAdapter.SxRecordDefinition sxrecorddefinition = createRecord(ommapinfodescriptor);
			java.lang.String s1 = ommapinfodescriptor.getClassname();
			com.ofx.repository.SxClass sxclass = null;
			try
			{
				sxclass = com.ofx.repository.SxClass.retrieve(s1, getQuery());
			}
			catch(com.ofx.repository.SxRepositoryException sxrepositoryexception)
			{
				java.lang.String s2 = "The following exception occurred retrieving class " + s1 + ":\r\n" + sxrepositoryexception.getMessage();
				showWarning(s2);
			}
			if(sxclass == null)
			{
				showWarning("Unable to create class " + s1 + " in the database.");
				return;
			}
			statusLabel.setText("                  Importing '" + s1 + "'...");
			importReport.setClassName(s1);
			try
			{
				translator = com.ofx.dataAdapter.SxImportFactory.createMapInfoSpatialDataTransferInterface();
				translator.open(ommapinfodescriptor.getDirname());
				translator.setClass(sxclass);
				translator.setLayer(ommapinfodescriptor.getFilename());
				translator.setIdentifierAttribute(ommapinfodescriptor.getID());
				translator.setLabelAttribute(ommapinfodescriptor.getLabel());
				translator.setProjection(getQuery().getDatabaseProjection());
				int j = environment().getProperties().getInteger("ofx.objectmapper.commitSize", new Integer(0x186a0)).intValue();
				translator.importAll(401, j, importReport);
			}
			catch(com.ofx.dataAdapter.SxImportException sximportexception)
			{
				java.lang.String s3 = sximportexception.getMessage();
				showWarning(s3);
			}
			importReport.complete();
		}

		statusLabel.setText("                  Import done.");
	}

	private com.ofx.dataAdapter.SxRecordDefinition createRecord(com.ofx.objectmapper.OmMapInfoDescriptor ommapinfodescriptor)
	{
		com.ofx.dataAdapter.SxDelimitedRecordDefinition sxdelimitedrecorddefinition = new SxDelimitedRecordDefinition();
		java.lang.String s = ommapinfodescriptor.getID();
		java.lang.String s1 = ommapinfodescriptor.getLabel();
		int i = ommapinfodescriptor.getIdPos() - 1;
		int j = ommapinfodescriptor.getLabelPos() - 1;
		com.ofx.dataAdapter.SxDelimitedFieldDefinition sxdelimitedfielddefinition = new SxDelimitedFieldDefinition(s, 501, i);
		sxdelimitedrecorddefinition.addField(sxdelimitedfielddefinition);
		sxdelimitedrecorddefinition.addField(new SxDelimitedFieldDefinition(s1, 501, j));
		sxdelimitedrecorddefinition.setKeyField(sxdelimitedfielddefinition);
		sxdelimitedrecorddefinition.setEnquotedLiteralCharacter('"');
		return sxdelimitedrecorddefinition;
	}

	private void createClass(com.ofx.objectmapper.OmMapInfoDescriptor ommapinfodescriptor)
	{
		java.lang.String s = ommapinfodescriptor.getClassname();
		try
		{
			if(!com.ofx.repository.SxClass.exists(s, getQuery()))
			{
				com.ofx.repository.SxSymbology sxsymbology = ommapinfodescriptor.getSymbology();
				com.ofx.repository.SxTextSpec sxtextspec = ommapinfodescriptor.getTextSpec();
				java.lang.String s1 = null;
				int i = ommapinfodescriptor.getDataType();
				if(i == 0)
					s1 = "Point";
				if(i == 1)
					s1 = "Polyline";
				if(i == 2)
					s1 = "Polygon";
				if(i == 3)
					s1 = "Text";
				com.ofx.repository.SxClass sxclass = com.ofx.repository.SxClass.create(s, "", s1, getQuery());
				sxclass.setSymbology(sxsymbology);
				sxclass.setLabelSpec(sxtextspec);
				sxclass.setTextMinScale(0.0D);
				sxclass.setTextMaxScale(16000D);
				sxclass.setSymbolMinScale(0.0D);
				sxclass.setSymbolMaxScale(16000D);
				if(ommapinfodescriptor.getID().equals("<Generated ID>"))
					sxclass.setNeedsGeneratedIdValues(true);
				getQuery().commitTransaction();
			}
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log().println(exception);
		}
	}

	public com.ofx.query.SxQueryInterface getQuery()
	{
		return com.ofx.base.SxEnvironment.singleton().getQuery();
	}

	private void clearList()
	{
		if(mifFilenamesList.getSelectedIndex() != -1)
			mifFilenamesList.deselect(mifFilenamesList.getSelectedIndex());
		for(int i = 0; i < lineCount; i++)
			desDict.remove(fileNames[i]);

		if(readyToImpList.getItemCount() > 0)
			readyToImpList.removeAll();
		lineCount = 0;
		popReadyToImportList();
		disableButtons();
	}

	private void updateFileList()
	{
		int i = mifFilenamesList.getItemCount();
		if(i != 0)
			mifFilenamesList.removeAll();
		java.util.Vector vector = new Vector();
		vector = getFilesWithExtension(vector, "mif");
		vector = getFilesWithExtension(vector, "MIF");
		vector = getFilesWithExtension(vector, "Mif");
		if(!vector.isEmpty())
		{
			try
			{
				(new SxQSort()).sort(vector, new SxLexicographicAscendingStringComparator());
			}
			catch(java.lang.Exception exception)
			{
				com.ofx.base.SxEnvironment.log().println(exception);
			}
			for(int j = 0; j < vector.size(); j++)
				mifFilenamesList.add((java.lang.String)vector.elementAt(j));

		}
		int k = mifFilenamesList.getItemCount();
		fileNames = new java.lang.String[k];
	}

	private void updateReadyToImportList()
	{
		java.lang.String s = null;
		int i = 0;
		int j = 0;
		Object obj = null;
		com.ofx.repository.SxSymbology sxsymbology = null;
		com.ofx.repository.SxTextSpec sxtextspec = null;
		boolean flag = false;
		java.lang.String s4 = "defaultDelimiter";
		int k = 0;
		java.lang.String s5 = null;
		java.lang.String s6 = null;
		java.lang.String s7 = null;
		java.lang.String s8 = null;
		java.lang.String s9 = null;
		java.lang.String s10 = null;
		java.lang.String s11 = null;
		java.lang.String s12 = null;
		java.lang.String s13 = null;
		java.lang.String s14 = null;
		int j2 = 0;
		java.util.Vector vector = new Vector();
		try
		{
			java.io.FileInputStream fileinputstream = new FileInputStream(selectedDir + selectedFile);
			java.io.BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(fileinputstream));
			java.lang.String s17 = null;
			Object obj1 = null;
			int l2 = 0;
			int j3 = 0;
			boolean flag2 = false;
			boolean flag3 = false;
			boolean flag4 = false;
			boolean flag5 = false;
			boolean flag6 = true;
			int j4 = 0;
			while((s17 = bufferedreader.readLine()) != null) 
			{
				boolean flag7;
				if(s17.trim().equals(""))
					flag7 = false;
				else
					flag7 = true;
				if(!flag7)
					continue;
				java.util.StringTokenizer stringtokenizer3 = new StringTokenizer(s17, " ");
				java.lang.String s21 = stringtokenizer3.nextToken();
				int k4 = getIntForKeyword(s21);
				switch(k4)
				{
				case 3: // '\003'
					s4 = stringtokenizer3.nextToken();
					if(s4.startsWith("\"") && s4.endsWith("\""))
						s4 = s4.substring(1, s4.length() - 1);
					break;

				case 8: // '\b'
					k = java.lang.Integer.parseInt(stringtokenizer3.nextToken().trim());
					j4 = l2;
					flag2 = true;
					j3 = l2 + 1;
					break;

				case 9: // '\t'
					int i4 = l2 + 1;
					flag4 = true;
					break;

				case 10: // '\n'
					if(flag4)
						j = 0;
					break;

				case 11: // '\013'
					if(flag4)
						j = 1;
					break;

				case 12: // '\f'
					if(flag4)
						j = 1;
					break;

				case 13: // '\r'
					if(flag4)
						j = 2;
					break;

				case 14: // '\016'
					if(flag4)
						j = 1;
					break;

				case 15: // '\017'
					if(flag4)
						j = 3;
					break;

				case 16: // '\020'
					if(flag4)
						j = 2;
					break;

				case 17: // '\021'
					if(flag4)
						j = 2;
					break;

				case 18: // '\022'
					if(flag4)
						j = 2;
					break;

				case 19: // '\023'
					if(flag4)
					{
						java.lang.String s1 = stringtokenizer3.nextToken().toLowerCase();
						java.util.StringTokenizer stringtokenizer = new StringTokenizer(s1, ",");
						s11 = stringtokenizer.nextToken().toLowerCase();
						s11 = s11.substring(1, s11.length());
						s5 = stringtokenizer.nextToken().toLowerCase();
						s6 = s5;
						s9 = stringtokenizer.nextToken().toLowerCase();
						if(!stringtokenizer.hasMoreTokens())
							s9 = s9.substring(0, s9.length() - 1);
						flag = true;
					}
					break;

				case 20: // '\024'
					if(flag4)
					{
						java.lang.String s2 = stringtokenizer3.nextToken().toLowerCase();
						java.util.StringTokenizer stringtokenizer1 = new StringTokenizer(s2, ",");
						s10 = stringtokenizer1.nextToken().toLowerCase();
						s10 = s10.substring(1, s10.length());
						s12 = stringtokenizer1.nextToken().toLowerCase();
						s6 = stringtokenizer1.nextToken().toLowerCase();
						s6 = s6.substring(0, s6.length() - 1);
						s5 = "0";
						if(j == 1)
							flag = true;
					}
					break;

				case 21: // '\025'
					if(flag4)
					{
						java.lang.String s3 = stringtokenizer3.nextToken().toLowerCase();
						java.util.StringTokenizer stringtokenizer2 = new StringTokenizer(s3, ",");
						s13 = stringtokenizer2.nextToken().toLowerCase();
						s13 = s13.substring(1, s13.length());
						s7 = stringtokenizer2.nextToken().toLowerCase();
						if(stringtokenizer2.hasMoreTokens())
						{
							s8 = stringtokenizer2.nextToken().toLowerCase();
							s8 = s8.substring(0, s8.length() - 1);
						} else
						{
							s7 = s7.substring(0, s7.length() - 1);
						}
						flag = true;
					}
					break;
				}
				if(flag2 && j3 == l2)
				{
					s = s21;
					i = l2 - j4;
					s14 = s;
					j2 = i;
				}
				if(l2 > j4 && l2 <= j4 + k)
				{
					if(s21.toLowerCase().startsWith("id"))
					{
						s14 = s21;
						j2 = l2 - j4;
					}
					if(s21.toLowerCase().startsWith("nam"))
					{
						s = s21;
						i = l2 - j4;
					}
					vector.addElement(s21);
				}
				if(flag)
					break;
				l2++;
			}
			bufferedreader.close();
			fileinputstream.close();
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log().println(exception);
		}
		if(s4.equals("defaultDelimiter"))
			s4 = "\t";
		java.lang.String s15 = "true";
		if(lineCount != 0)
		{
			for(int k2 = 0; k2 < lineCount; k2++)
			{
				java.lang.String s18 = fileNames[k2];
				if(s18.equals(selectedFile))
				{
					readyToImpList.select(k2);
					s15 = "false";
				}
			}

		}
		if(s15.equals("true"))
		{
			java.lang.String s16 = selectedFile.substring(0, selectedFile.length() - 4);
			if(s16.endsWith("IN"))
				s16 = s16.substring(0, s16.length() - 2) + "In";
			try
			{
				if(com.ofx.repository.SxClass.exists(s16, getQuery()))
				{
					com.ofx.repository.SxClass sxclass = com.ofx.repository.SxClass.retrieve(s16, getQuery());
					sxsymbology = sxclass.getSymbology();
					sxtextspec = sxclass.getLabelSpec();
					if(sxclass.getNeedsGeneratedIdValues())
						s14 = "<Generated ID>";
				} else
				{
					sxtextspec = com.ofx.repository.SxTextSpec.retrieve("DEFAULT", getQuery());
					java.lang.String s19 = getPointSymForCode(s11);
					if(s19 == null)
						s19 = "Square";
					int l = java.awt.Color.black.getRGB();
					if(s5 != null)
						try
						{
							l = java.lang.Integer.parseInt(s5.trim());
						}
						catch(java.lang.Exception exception2)
						{
							l = java.awt.Color.black.getRGB();
						}
					int i1 = java.awt.Color.white.getRGB();
					if(s6 != null)
						try
						{
							i1 = java.lang.Integer.parseInt(s6.trim());
						}
						catch(java.lang.Exception exception3)
						{
							i1 = java.awt.Color.white.getRGB();
						}
					int j1 = java.awt.Color.black.getRGB();
					if(s7 != null)
						try
						{
							j1 = java.lang.Integer.parseInt(s7.trim());
						}
						catch(java.lang.Exception exception4)
						{
							j1 = java.awt.Color.black.getRGB();
						}
					int k1 = java.awt.Color.white.getRGB();
					if(s8 != null)
						try
						{
							k1 = java.lang.Integer.parseInt(s8.trim());
						}
						catch(java.lang.Exception exception5)
						{
							k1 = java.awt.Color.white.getRGB();
						}
					int l1 = 0;
					if(s12 != null)
						try
						{
							l1 = java.lang.Integer.parseInt(s12.trim());
						}
						catch(java.lang.Exception exception6)
						{
							l1 = 2;
						}
					int i2 = 0;
					if(s13 != null)
						try
						{
							i2 = java.lang.Integer.parseInt(s13.trim());
						}
						catch(java.lang.Exception exception7)
						{
							i2 = 2;
						}
					boolean flag1 = false;
					if(s19.substring(0, 1).equals("*"))
					{
						s19 = s19.substring(1, s19.length());
						flag1 = true;
					}
					java.lang.String s22 = s16;
					int i3 = 0;
					for(; com.ofx.repository.SxSymbology.exists(s22, getQuery()); s22 = s22 + java.lang.String.valueOf(i3++));
					sxsymbology = com.ofx.repository.SxSymbology.create(s22, "for " + s16, getQuery());
					sxsymbology.setShape(s19);
					if(s9 != null)
					{
						int k3 = java.lang.Integer.parseInt(s9.trim());
						if(k3 <= 0)
							k3 = 1;
						sxsymbology.setSymbolSize(k3);
					} else
					{
						sxsymbology.setSymbolSize(0);
					}
					if(s10 != null)
					{
						int l3 = java.lang.Integer.parseInt(s10.trim());
						if(j == 1)
						{
							sxsymbology.setOutlineWidth(0);
							sxsymbology.setLineWidth(l3);
							flag1 = true;
						} else
						{
							sxsymbology.setOutlineWidth(l3);
							sxsymbology.setLineWidth(0);
						}
					} else
					{
						sxsymbology.setOutlineWidth(1);
						sxsymbology.setLineWidth(1);
					}
					if(l1 == 0)
						sxsymbology.setIsLineDashed(false);
					else
					if(l1 == 1)
					{
						sxsymbology.setIsLineDashed(false);
						sxsymbology.setOutlineWidth(0);
						sxsymbology.setLineWidth(0);
					} else
					if(l1 == 2)
						sxsymbology.setIsLineDashed(false);
					else
						sxsymbology.setIsLineDashed(true);
					sxsymbology.setOutlineColorRGB((new Color(l)).getRGB());
					if(i2 != 0)
					{
						flag1 = true;
						if(i2 == 1)
							flag1 = false;
						else
						if(i2 == 2 || i2 >= 9 && i2 <= 18)
							sxsymbology.setFillPatternID(-1);
						else
						if(i2 == 3 || i2 >= 19 && i2 <= 23)
							sxsymbology.setFillPatternID(5);
						else
						if(i2 == 4 || i2 >= 24 && i2 <= 28)
							sxsymbology.setFillPatternID(6);
						else
						if(i2 == 5 || i2 >= 29 && i2 <= 33)
							sxsymbology.setFillPatternID(2);
						else
						if(i2 == 6 || i2 >= 34 && i2 <= 38)
							sxsymbology.setFillPatternID(1);
						else
						if(i2 == 7 || i2 >= 39 && i2 <= 43)
							sxsymbology.setFillPatternID(0);
						else
						if(i2 == 8 || i2 >= 44 && i2 <= 55 || i2 == 67 || i2 == 69)
							sxsymbology.setFillPatternID(9);
						else
						if(i2 == 56 || i2 >= 58 && i2 <= 60 || i2 == 71)
							sxsymbology.setFillPatternID(3);
						else
						if(i2 == 61 || i2 == 64)
							sxsymbology.setFillPatternID(4);
						else
						if(i2 == 57 || i2 == 62 || i2 == 63 || i2 == 65)
							sxsymbology.setFillPatternID(7);
						else
						if(i2 == 66 || i2 == 68 || i2 == 70)
							sxsymbology.setFillPatternID(8);
					}
					if(flag1)
						sxsymbology.setFillColorRGB((new Color(i1)).getRGB());
					if(s7 != null)
					{
						sxsymbology.setFillPatternForeColorRGB((new Color(j1)).getRGB());
						if(s8 == null)
							sxsymbology.setFillPatternBackColorRGB((new Color(0, true)).getRGB());
						else
							sxsymbology.setFillPatternBackColorRGB((new Color(k1)).getRGB());
					}
					sxsymbology.setFilled(flag1);
				}
			}
			catch(java.lang.Exception exception1)
			{
				com.ofx.base.SxEnvironment.log().println(exception1);
			}
			java.lang.String s20 = selectedFile.substring(0, selectedFile.length() - 4);
			com.ofx.objectmapper.OmMapInfoDescriptor ommapinfodescriptor = new OmMapInfoDescriptor(s20);
			ommapinfodescriptor.setClassname(s16);
			ommapinfodescriptor.setDirname(selectedDir);
			ommapinfodescriptor.setDelimiter(s4);
			ommapinfodescriptor.setSymbology(sxsymbology);
			ommapinfodescriptor.setTextSpec(sxtextspec);
			ommapinfodescriptor.setLabel(s);
			ommapinfodescriptor.setID(s14);
			ommapinfodescriptor.setLabelPos(i);
			ommapinfodescriptor.setIdPos(j2);
			ommapinfodescriptor.setDataType(j);
			ommapinfodescriptor.setAttNames(vector);
			ommapinfodescriptor.setNumOfCols(k);
			desDict.put(s20, ommapinfodescriptor);
			popImportListWithDes(ommapinfodescriptor, lineCount);
			lineCount++;
		}
	}

	private void popReadyToImportList()
	{
		if(!desDict.isEmpty())
		{
			for(java.util.Enumeration enumeration = desDict.elements(); enumeration.hasMoreElements();)
			{
				com.ofx.objectmapper.OmMapInfoDescriptor ommapinfodescriptor = (com.ofx.objectmapper.OmMapInfoDescriptor)enumeration.nextElement();
				popImportListWithDes(ommapinfodescriptor, lineCount);
				lineCount++;
			}

		} else
		{
			disableButtons();
			itemAdded = false;
		}
	}

	private void popImportListWithDes(com.ofx.objectmapper.OmMapInfoDescriptor ommapinfodescriptor, int i)
	{
		java.lang.String s = "                  ";
		int j = ommapinfodescriptor.getFilename().length() + 4;
		if(j > s.length())
			j = s.length() - 2;
		java.lang.String s1 = ommapinfodescriptor.getFilename() + ".mif" + s.substring(j);
		j = ommapinfodescriptor.getClassname().length();
		if(j > s.length())
			j = s.length() - 2;
		s1 = s1 + ommapinfodescriptor.getClassname() + s.substring(j);
		s = "              ";
		j = ommapinfodescriptor.getID().length();
		if(j > s.length())
			j = s.length() - 2;
		s1 = s1 + ommapinfodescriptor.getID() + s.substring(j);
		j = ommapinfodescriptor.getLabel().length();
		if(j > s.length())
			j = s.length() - 2;
		s1 = s1 + ommapinfodescriptor.getLabel() + s.substring(j);
		s = "                  ";
		j = ommapinfodescriptor.getSymbology().getID().length();
		if(j > s.length())
			j = s.length() - 2;
		s1 = s1 + ommapinfodescriptor.getSymbology().getID() + s.substring(j);
		s1 = s1 + ommapinfodescriptor.getTextSpec().getID();
		readyToImpList.add(s1, i);
		fileNames[i] = ommapinfodescriptor.getFilename();
	}

	private void replaceImportListWithDes(com.ofx.objectmapper.OmMapInfoDescriptor ommapinfodescriptor, int i)
	{
		java.lang.String s = "                  ";
		int j = ommapinfodescriptor.getFilename().length() + 4;
		if(j > s.length())
			j = s.length() - 2;
		java.lang.String s1 = ommapinfodescriptor.getFilename() + ".mif" + s.substring(j);
		j = ommapinfodescriptor.getClassname().length();
		if(j > s.length())
			j = s.length() - 2;
		s1 = s1 + ommapinfodescriptor.getClassname() + s.substring(j);
		s = "              ";
		j = ommapinfodescriptor.getID().length();
		if(j > s.length())
			j = s.length() - 2;
		s1 = s1 + ommapinfodescriptor.getID() + s.substring(j);
		j = ommapinfodescriptor.getLabel().length();
		if(j > s.length())
			j = s.length() - 2;
		s1 = s1 + ommapinfodescriptor.getLabel() + s.substring(j);
		s = "                  ";
		j = ommapinfodescriptor.getSymbology().getID().length();
		if(j > s.length())
			j = s.length() - 2;
		s1 = s1 + ommapinfodescriptor.getSymbology().getID() + s.substring(j);
		s1 = s1 + ommapinfodescriptor.getTextSpec().getID();
		readyToImpList.replaceItem(s1, i);
		fileNames[i] = ommapinfodescriptor.getFilename();
	}

	private void updateList(com.ofx.objectmapper.OmMapInfoDescriptor ommapinfodescriptor)
	{
		java.lang.String s = ommapinfodescriptor.getFilename();
		for(int i = 0; i < lineCount; i++)
		{
			if(!s.equals(fileNames[i]))
				continue;
			replaceImportListWithDes(ommapinfodescriptor, i);
			readyToImpList.select(i);
			break;
		}

	}

	private java.util.Vector getFilesWithExtension(java.util.Vector vector, java.lang.String s)
	{
		java.io.File file = new File(mifFilenameFld.getText());
		java.io.File file1 = file;
		com.ofx.objectmapper.FilenameExtensionFilter filenameextensionfilter = new FilenameExtensionFilter(s);
		if(file1.isDirectory())
		{
			java.lang.String as[] = file1.list(filenameextensionfilter);
			for(int i = 0; i < as.length; i++)
			{
				java.lang.String s1 = as[i];
				vector.addElement(as[i]);
			}

		}
		return vector;
	}

	private java.util.Hashtable getSymbolDict()
	{
		if(symbolDict == null)
			initSymbolDict();
		return symbolDict;
	}

	private java.lang.String getPointSymForCode(java.lang.String s)
	{
		if(s == null)
			return null;
		if(getSymbolDict().containsKey(s.toLowerCase()))
			return (java.lang.String)getSymbolDict().get(s.toLowerCase());
		else
			return null;
	}

	private java.util.Hashtable getKeywordDict()
	{
		if(keywordDict == null)
			initKeywordDict();
		return keywordDict;
	}

	private int getIntForKeyword(java.lang.String s)
	{
		if(s == null)
			return 0;
		if(getKeywordDict().containsKey(s.toLowerCase()))
		{
			java.lang.String s1 = (java.lang.String)getKeywordDict().get(s.toLowerCase());
			return java.lang.Integer.parseInt(s1.trim());
		} else
		{
			return 0;
		}
	}

	private void disableButtons()
	{
		modifyButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}

	public void update(java.util.Observable observable, java.lang.Object obj)
	{
		com.ofx.base.SxStateChange sxstatechange = (com.ofx.base.SxStateChange)obj;
		int i = sxstatechange.getState();
		int j = sxstatechange.getContext();
		if(i == 104 && j == 206)
		{
			com.ofx.objectmapper.OmMapInfoDescriptor ommapinfodescriptor = omModifyFilter.getDescriptor();
			com.ofx.objectmapper.OmMapInfoDescriptor ommapinfodescriptor1 = (com.ofx.objectmapper.OmMapInfoDescriptor)desDict.get(ommapinfodescriptor.getFilename());
			desDict.remove(ommapinfodescriptor1.getFilename());
			desDict.put(ommapinfodescriptor.getFilename(), ommapinfodescriptor);
			updateList(ommapinfodescriptor);
			omModifyFilter.close();
		}
	}

	private void initSymbolDict()
	{
		symbolDict = new Hashtable(35);
		symbolDict.put("2", "*Square");
		symbolDict.put("31", "Blank");
		symbolDict.put("32", "*Square");
		symbolDict.put("33", "*Diamond");
		symbolDict.put("34", "*Circle");
		symbolDict.put("35", "*Five Point Star");
		symbolDict.put("36", "*Upward Triangle");
		symbolDict.put("37", "*Downward Triangle");
		symbolDict.put("38", "Square");
		symbolDict.put("39", "Diamond");
		symbolDict.put("40", "Circle");
		symbolDict.put("41", "Five Point Star");
		symbolDict.put("42", "Upward Triangle");
		symbolDict.put("43", "Downward Triangle");
		symbolDict.put("44", "*Square");
		symbolDict.put("45", "*Upward Triangle");
		symbolDict.put("46", "*Circle");
		symbolDict.put("47", "*Up Arrowhead");
		symbolDict.put("48", "*Down Arrowhead");
		symbolDict.put("49", "*Cross");
		symbolDict.put("50", "*X");
		symbolDict.put("51", "*Cross And X");
		symbolDict.put("52", "*Plane");
		symbolDict.put("53", "*School");
		symbolDict.put("54", "*Right Arrowhead");
		symbolDict.put("55", "Hospital");
		symbolDict.put("56", "*Hospital");
		symbolDict.put("58", "*Ship");
		symbolDict.put("59", "Cross In Circle");
		symbolDict.put("60", "*Building");
		symbolDict.put("61", "Interstate Shield");
		symbolDict.put("62", "US Highway Shield");
		symbolDict.put("65", "*Church");
	}

	private void initKeywordDict()
	{
		keywordDict = new Hashtable(25);
		keywordDict.put("version", "1");
		keywordDict.put("charset", "2");
		keywordDict.put("delimiter", "3");
		keywordDict.put("unique", "4");
		keywordDict.put("index", "5");
		keywordDict.put("coordsys", "6");
		keywordDict.put("transform", "7");
		keywordDict.put("columns", "8");
		keywordDict.put("data", "9");
		keywordDict.put("point", "10");
		keywordDict.put("line", "11");
		keywordDict.put("pline", "12");
		keywordDict.put("region", "13");
		keywordDict.put("arc", "14");
		keywordDict.put("text", "15");
		keywordDict.put("rect", "16");
		keywordDict.put("roundrect", "17");
		keywordDict.put("ellipse", "18");
		keywordDict.put("symbol", "19");
		keywordDict.put("pen", "20");
		keywordDict.put("brush", "21");
		keywordDict.put("none", "22");
	}

	public void addObserver(java.util.Observer observer)
	{
		com.ofx.objectmapper.SxObservableDispatcher.addObserver(this, observer);
	}

	private void notifyObservers(java.lang.Object obj)
	{
		com.ofx.objectmapper.SxObservableDispatcher.notify(this, obj);
	}

	private OmMapInfoImport()
	{
		lineCount = 0;
		symbolDict = null;
		keywordDict = null;
		itemAdded = false;
		desDict = new Hashtable(40);
		importReport = null;
		com.ofx.objectmapper.SxObservableDispatcher.register(this);
		addWindowListener(this);
		setLayout(new BorderLayout(0, 0));
		addNotify();
		setSize(getInsets().left + getInsets().right + 750, getInsets().top + getInsets().bottom + 480);
		setBackground(new Color(0xc0c0c0));
		descriptionPanel = new Panel();
		java.awt.GridBagLayout gridbaglayout = new GridBagLayout();
		descriptionPanel.setLayout(gridbaglayout);
		descriptionPanel.setBounds(getInsets().left + 0, getInsets().top + 0, 495, 40);
		descriptionPanel.setBackground(new Color(0xc0c0c0));
		add("North", descriptionPanel);
		iconImage = new ImageViewer();
		iconImage.setBounds(0, 1, 30, 30);
		java.awt.GridBagConstraints gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.gridx = 0;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.anchor = 17;
		gridbagconstraints.fill = 0;
		gridbagconstraints.insets = new Insets(3, 10, 0, 10);
		((java.awt.GridBagLayout)descriptionPanel.getLayout()).setConstraints(iconImage, gridbagconstraints);
		descriptionPanel.add(iconImage);
		try
		{
			iconImage.setImageURL(getClass().getResource("/images/mapinfim.gif"));
		}
		catch(java.beans.PropertyVetoException propertyvetoexception) { }
		descriptionLabel = new Label("Use Browse to choose a directory, then select files to import.");
		descriptionLabel.setBounds(15, 5, 121, 23);
		gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.gridx = 3;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.gridwidth = 9;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 1.0D;
		gridbagconstraints.anchor = 17;
		gridbagconstraints.fill = 0;
		gridbagconstraints.insets = new Insets(0, 15, 0, 0);
		((java.awt.GridBagLayout)descriptionPanel.getLayout()).setConstraints(descriptionLabel, gridbagconstraints);
		descriptionPanel.add(descriptionLabel);
		java.awt.Font font = new Font("Dialog", 0, 12);
		borderPanel1 = new Panel();
		borderPanel1.setLayout(new BorderLayout(0, 0));
		borderPanel1.setBounds(0, 0, 468, 427);
		add("Center", borderPanel1);
		panel7 = new Panel();
		panel7.setLayout(new BorderLayout(0, 0));
		panel7.setBounds(0, 0, 451, 382);
		borderPanel1.add("Center", panel7);
		borderPanel2 = new BorderPanel();
		borderPanel2.setLayout(new BorderLayout(0, 2));
		borderPanel2.setBounds(0, 0, 451, 157);
		panel7.add("North", borderPanel2);
		try
		{
			borderPanel2.setBevelStyle(3);
			borderPanel2.setIPadBottom(2);
			borderPanel2.setIPadSides(2);
		}
		catch(java.beans.PropertyVetoException propertyvetoexception1) { }
		panel3 = new Panel();
		panel3.setLayout(new BorderLayout(12, 0));
		panel3.setBounds(0, 0, 434, 24);
		borderPanel2.add("North", panel3);
		label2 = new Label("  Directory:");
		label2.setBounds(0, 0, 82, 24);
		panel3.add("West", label2);
		mifFilenameFld = new TextField();
		mifFilenameFld.setBounds(10, 0, 256, 24);
		mifFilenameFld.addActionListener(this);
		panel3.add(mifFilenameFld);
		browseButton = new Button("Browse >>");
		browseButton.setBounds(276, 0, 76, 24);
		browseButton.setFont(font);
		browseButton.addActionListener(this);
		panel3.add("East", browseButton);
		panel4 = new Panel();
		panel4.setLayout(new BorderLayout(0, 0));
		panel4.setBounds(0, 24, 434, 96);
		borderPanel2.add("Center", panel4);
		label1 = new Label("  MapInfo MIF Files:");
		label1.setBounds(0, 0, 434, 24);
		panel4.add("North", label1);
		mifFilenamesList = new java.awt.List(0, false);
		mifFilenamesList.setBounds(0, 24, 434, 70);
		mifFilenamesList.addItemListener(this);
		panel4.add("Center", mifFilenamesList);
		borderPanel3 = new BorderPanel();
		borderPanel3.setLayout(new BorderLayout(0, 0));
		borderPanel3.setBounds(0, 157, 451, 225);
		panel7.add("Center", borderPanel3);
		try
		{
			borderPanel3.setLabel("Files to be Imported:");
			borderPanel3.setAlignStyle(0);
			borderPanel3.setBevelStyle(0);
			borderPanel3.setIPadBottom(2);
			borderPanel3.setIPadSides(2);
		}
		catch(java.beans.PropertyVetoException propertyvetoexception2) { }
		borderPanel4 = new Panel();
		borderPanel4.setLayout(new BorderLayout(0, 0));
		borderPanel4.setBounds(0, 0, 434, 162);
		borderPanel3.add("Center", borderPanel4);
		panel2 = new Panel();
		gridbaglayout = new GridBagLayout();
		panel2.setLayout(gridbaglayout);
		panel2.setBounds(0, 115, 417, 26);
		borderPanel4.add("South", panel2);
		modifyButton = new Button("Modify...");
		modifyButton.setEnabled(false);
		modifyButton.setBounds(13, 0, 71, 24);
		modifyButton.setFont(font);
		modifyButton.addActionListener(this);
		gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.gridx = 0;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 1.0D;
		gridbagconstraints.fill = 0;
		gridbagconstraints.insets = new Insets(0, 0, 2, 0);
		gridbaglayout.setConstraints(modifyButton, gridbagconstraints);
		panel2.add(modifyButton);
		deleteButton = new Button(" Remove ");
		deleteButton.setEnabled(false);
		deleteButton.setBounds(240, 0, 60, 24);
		deleteButton.setFont(font);
		deleteButton.addActionListener(this);
		gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.gridx = 2;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 1.0D;
		gridbagconstraints.fill = 0;
		gridbagconstraints.insets = new Insets(0, 0, 2, 0);
		gridbaglayout.setConstraints(deleteButton, gridbagconstraints);
		panel2.add(deleteButton);
		clearButton = new Button(" Clear ");
		clearButton.setBounds(324, 1, 79, 24);
		clearButton.setFont(font);
		clearButton.addActionListener(this);
		gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.gridx = 4;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 1.0D;
		gridbagconstraints.fill = 0;
		gridbagconstraints.insets = new Insets(0, 0, 0, 0);
		gridbaglayout.setConstraints(clearButton, gridbagconstraints);
		panel2.add(clearButton);
		borderPanel5 = new BorderPanel();
		borderPanel5.setLayout(new BorderLayout(0, 0));
		borderPanel5.setBounds(0, 0, 417, 115);
		borderPanel4.add("Center", borderPanel5);
		try
		{
			borderPanel5.setIPadBottom(1);
			borderPanel5.setIPadSides(1);
		}
		catch(java.beans.PropertyVetoException propertyvetoexception3) { }
		readyLabel = new Label(" File Name         Class Name       ID Field       Label Field   Symbol            TextSpec");
		readyLabel.setBounds(0, 0, 400, 24);
		readyLabel.setFont(new Font("Monospaced", 1, 12));
		borderPanel5.add("North", readyLabel);
		readyToImpList = new java.awt.List();
		readyToImpList.setBounds(0, 0, 400, 94);
		readyToImpList.setFont(new Font("Monospaced", 0, 12));
		readyToImpList.addItemListener(this);
		borderPanel5.add("Center", readyToImpList);
		panel8 = new Panel();
		gridbaglayout = new GridBagLayout();
		panel8.setLayout(gridbaglayout);
		panel8.setBounds(0, 382, 451, 24);
		borderPanel1.add("South", panel8);
		gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.gridx = 0;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 1.0D;
		gridbagconstraints.anchor = 18;
		gridbagconstraints.fill = 0;
		gridbagconstraints.insets = new Insets(0, 10, 0, 0);
		statusLabel = new Label("");
		statusLabel.setBounds(41, 0, 409, 24);
		statusLabel.setFont(new Font("Dialog", 1, 16));
		gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.gridx = 1;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 1.0D;
		gridbagconstraints.anchor = 18;
		gridbagconstraints.fill = 2;
		gridbagconstraints.insets = new Insets(0, 0, 0, 0);
		gridbaglayout.setConstraints(statusLabel, gridbagconstraints);
		panel8.add(statusLabel);
		panel6 = new BorderPanel();
		gridbaglayout = new GridBagLayout();
		panel6.setLayout(gridbaglayout);
		panel6.setBounds(0, 162, 434, 26);
		add("South", panel6);
		try
		{
			panel6.setPaddingTop(2);
			panel6.setIPadBottom(2);
			panel6.setIPadSides(2);
			panel6.setBevelStyle(0);
		}
		catch(java.beans.PropertyVetoException propertyvetoexception4) { }
		importButton = new Button(" Import ");
		importButton.setBounds(176, 0, 81, 24);
		importButton.setFont(font);
		importButton.addActionListener(this);
		gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.gridx = 0;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 1.0D;
		gridbagconstraints.anchor = 17;
		gridbagconstraints.fill = 0;
		gridbagconstraints.insets = new Insets(0, 75, 0, 50);
		gridbaglayout.setConstraints(importButton, gridbagconstraints);
		panel6.add(importButton);
		closeButton = new Button(" Close ");
		closeButton.setBounds(213, 0, 52, 24);
		closeButton.setFont(font);
		closeButton.addActionListener(this);
		gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.gridx = 0;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 1.0D;
		gridbagconstraints.anchor = 13;
		gridbagconstraints.fill = 0;
		gridbagconstraints.insets = new Insets(0, 50, 0, 75);
		gridbaglayout.setConstraints(closeButton, gridbagconstraints);
		panel6.add(closeButton);
		openMifFileDialog = new FileDialog(this, "Open Directory of MID/MIF Files", 0);
		setTitle("Import MapInfo Data");
	}

	public void setVisible(boolean flag)
	{
		if(flag)
			setLocation(240, 50);
		super.setVisible(flag);
	}

	public void closeWindow()
	{
		if(translator != null)
			try
			{
				if(translator.isOpen())
					translator.close();
			}
			catch(com.ofx.dataAdapter.SxImportException sximportexception)
			{
				java.lang.String s = sximportexception.getMessage();
				showWarning(s);
			}
		if(importReport != null)
		{
			importReport.close();
			importReport = null;
		}
		setVisible(false);
		com.ofx.base.SxStateChange sxstatechange = new SxStateChange(this, 111, 206);
		notifyObservers(sxstatechange);
	}

	public void windowClosing(java.awt.event.WindowEvent windowevent)
	{
		closeWindow();
	}

	public void actionPerformed(java.awt.event.ActionEvent actionevent)
	{
		java.lang.Object obj = actionevent.getSource();
		if(obj == mifFilenameFld)
			mifFilenameFld_Entered(actionevent);
		else
		if(obj == modifyButton)
			modifyButton_Clicked(actionevent);
		else
		if(obj == deleteButton)
			deleteButton_Clicked(actionevent);
		else
		if(obj == browseButton)
			browseButton_Clicked(actionevent);
		else
		if(obj == importButton)
			importButton_Clicked(actionevent);
		else
		if(obj == clearButton)
			clearButton_Clicked(actionevent);
		else
		if(obj == closeButton)
			closeWindow();
	}

	public void itemStateChanged(java.awt.event.ItemEvent itemevent)
	{
		java.lang.Object obj = itemevent.getSource();
		if(obj == mifFilenamesList)
			mifFilenamesList_ListSelect(itemevent);
		else
		if(obj == readyToImpList)
			readyToImpList_ListSelect(itemevent);
	}

	public static final int POINT_TYPE = 0;
	public static final int LINE_TYPE = 1;
	public static final int AREA_TYPE = 2;
	private static com.ofx.objectmapper.OmMapInfoImport _theFrame;
	java.lang.String selectedDir;
	java.lang.String selectedFile;
	java.lang.String fileNames[];
	int lineCount;
	com.ofx.objectmapper.OmModifyMapInfoFilter omModifyFilter;
	java.util.Hashtable symbolDict;
	java.util.Hashtable keywordDict;
	boolean itemAdded;
	java.util.Hashtable desDict;
	com.ofx.dataAdapter.SxSpatialDataTransferInterface translator;
	com.ofx.objectmapper.OmImportReport importReport;
	java.awt.Panel borderPanel1;
	java.awt.Panel panel7;
	symantec.itools.awt.BorderPanel borderPanel2;
	java.awt.Panel panel4;
	java.awt.Label label1;
	java.awt.List mifFilenamesList;
	java.awt.Panel panel3;
	java.awt.Label label2;
	java.awt.Button browseButton;
	java.awt.TextField mifFilenameFld;
	symantec.itools.awt.BorderPanel borderPanel3;
	symantec.itools.awt.BorderPanel panel6;
	java.awt.Button importButton;
	java.awt.Panel borderPanel4;
	java.awt.Panel panel2;
	java.awt.Button modifyButton;
	java.awt.Button deleteButton;
	java.awt.Button clearButton;
	symantec.itools.awt.BorderPanel borderPanel5;
	java.awt.List readyToImpList;
	java.awt.Label readyLabel;
	java.awt.Panel panel8;
	java.awt.Label statusLabel;
	java.awt.FileDialog openMifFileDialog;
	java.awt.Button closeButton;
	java.awt.Panel descriptionPanel;
	java.awt.Label descriptionLabel;
	symantec.itools.multimedia.ImageViewer iconImage;
}
