// Decompiled by Jad v1.5.7f. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: fullnames lnc 

package com.ofx.objectmapper;

import com.ofx.base.SxEnvironment;
import com.ofx.base.SxException;
import com.ofx.base.SxFont;
import com.ofx.base.SxStateChange;
import com.ofx.dataAdapter.SxSpatialDataReader;
import com.ofx.mapViewer.SxDisplayHint;
import com.ofx.mapViewer.SxMapText;
import com.ofx.query.SxQueryInterface;
import com.ofx.query.SxQueryResultInterface;
import com.ofx.query.SxQueryRetrievalInterface;
import com.ofx.query.SxQueryTransactionInterface;
import com.ofx.repository.SxClass;
import com.ofx.repository.SxDataSource;
import com.ofx.repository.SxExternalDataDef;
import com.ofx.repository.SxMap;
import com.ofx.repository.SxSymbology;
import com.ofx.repository.SxTextSpec;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import symantec.itools.awt.BaseTabbedPanel;
import symantec.itools.awt.BorderPanel;
import symantec.itools.awt.TabPanel;
import symantec.itools.multimedia.ImageViewer;

// Referenced classes of package com.ofx.objectmapper:
//			OmFrame, OmSymbolCanvas, OmBorderedPanel, OmDialogRequest, 
//			OmAvailableTextSpecs, OmAvailableSymbols, OmAssociateEdfo, SxObservableDispatcher

public class OmMapObjectClasses extends com.ofx.objectmapper.OmFrame
	implements java.util.Observer, java.awt.event.ActionListener, java.beans.PropertyChangeListener, java.awt.event.ItemListener, java.awt.event.WindowListener
{

	public static com.ofx.objectmapper.OmMapObjectClasses getFrame()
	{
		if(com.ofx.objectmapper.OmMapObjectClasses._theFrame == null)
		{
			com.ofx.objectmapper.OmMapObjectClasses._theFrame = new OmMapObjectClasses();
		} else
		{
			com.ofx.objectmapper.OmMapObjectClasses._theFrame.statusLabel.setText("");
			com.ofx.objectmapper.OmMapObjectClasses._theFrame.currentIndex = -1;
			com.ofx.objectmapper.OmMapObjectClasses._theFrame.currentList = -1;
		}
		return com.ofx.objectmapper.OmMapObjectClasses._theFrame;
	}

	public static void resetFrame()
	{
		com.ofx.objectmapper.OmMapObjectClasses._theFrame = null;
	}

	void nameFld_EnterHit(java.awt.event.ActionEvent actionevent)
	{
		try
		{
			enableGenerateIdCheck();
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log(exception);
		}
	}

	void deleteButton_Clicked(java.awt.event.ActionEvent actionevent)
	{
		if(selectedClassName != null && selectedClassName.startsWith("MARKER"))
			showWarning("Special MARKER class for internal use only; cannot delete.");
		else
		if(selectedClassName != null && !selectedClassName.equals(""))
		{
			byte byte0 = 0;
			try
			{
				classToDelete = com.ofx.repository.SxClass.retrieve(selectedClassName, getQuery());
				if(classToDelete != null)
					if(isClassInUse(classToDelete.getID()))
					{
						if(classToDelete.isPointType())
						{
							byte0 = 0;
							updatePointList();
						}
						if(classToDelete.isPolylineType())
						{
							byte0 = 1;
							updateLineList();
						}
						if(classToDelete.isPolygonType())
						{
							byte0 = 2;
							updateAreaList();
						}
						if(classToDelete.isTextType())
						{
							byte0 = 3;
							updateAreaList();
						}
						classTabPanel.showTabPanel(byte0);
						classToDelete = null;
						clearFields(byte0);
						currentIndex = -1;
						setStatus("");
						showWarning("Class will not be deleted; used in map(s).");
					} else
					{
						deleteAction = "class";
						reqDlg = com.ofx.objectmapper.OmDialogRequest.getDialog(this, true);
						reqDlg.setMessage("Are you sure you wish to delete the class '" + classToDelete.getID() + "' ?");
						reqDlg.addObserver(this);
						reqDlg.setVisible(true);
					}
			}
			catch(java.lang.Exception exception)
			{
				com.ofx.base.SxEnvironment.log(exception);
			}
		} else
		{
			showWarning("Select a map object class first.");
		}
	}

	void deleteInstancesButton_Clicked(java.awt.event.ActionEvent actionevent)
	{
		if(selectedClassName != null && selectedClassName.startsWith("MARKER"))
			showWarning("Special MARKER class for internal use only; cannot delete.");
		else
		if(selectedClassName != null && !selectedClassName.equals(""))
		{
			boolean flag = false;
			try
			{
				classToDelete = com.ofx.repository.SxClass.retrieve(selectedClassName, getQuery());
				if(classToDelete != null)
				{
					deleteAction = "instances";
					reqDlg = com.ofx.objectmapper.OmDialogRequest.getDialog(this, true);
					reqDlg.setMessage("Are you sure you wish to delete ALL instances of the class '" + classToDelete.getID() + "' ?");
					reqDlg.addObserver(this);
					reqDlg.setVisible(true);
				}
			}
			catch(java.lang.Exception exception)
			{
				com.ofx.base.SxEnvironment.log(exception);
			}
		} else
		{
			showWarning("Select a map object class first.");
		}
	}

	void addButton_Clicked(java.awt.event.ActionEvent actionevent)
	{
		java.lang.String s = nameFld.getText().trim();
		if(s.equals(""))
		{
			showWarning("Enter a map object class name to create.");
		} else
		{
			if(s.endsWith("IN"))
				s = s.substring(0, s.length() - 2) + "In";
			try
			{
				if(com.ofx.repository.SxClass.exists(s, getQuery()))
				{
					showWarning("The map object class '" + s + "' already exists.");
				} else
				{
					setStatus("Creating map object class '" + s + "'...");
					java.lang.String s1 = getGeomTypeFromChoice();
					if(s1 == null)
						s1 = "";
					com.ofx.repository.SxClass sxclass = com.ofx.repository.SxClass.create(s, descField.getText(), s1, getQuery());
					setClassProperties(sxclass);
					getQuery().commitTransaction();
					populateListWithClass(sxclass);
					int i = sxclass.getDimension();
					classTabPanel.showTabPanel(i);
					symbolCanvas.setDimension(i);
					symbolCanvas.update();
					setLabelFont(textSpec);
					setStatus("");
				}
			}
			catch(java.lang.Exception exception)
			{
				com.ofx.base.SxEnvironment.log(exception);
			}
		}
	}

	void updateButton_Clicked(java.awt.event.ActionEvent actionevent)
	{
		if(selectedClassName != null && !selectedClassName.equals(""))
		{
			setStatus("Updating map object class '" + selectedClassName + "'...");
			int i = -1;
			int j = classTabPanel.getCurrentPanelNdx();
			if(j == 0)
			{
				i = pointsList.getSelectedIndex();
				selectedClassName = pointNames[i];
			} else
			if(j == 1)
			{
				i = linesList.getSelectedIndex();
				selectedClassName = lineNames[i];
			} else
			if(j == 2)
			{
				i = areasList.getSelectedIndex();
				selectedClassName = areaNames[i];
			} else
			if(j == 3)
			{
				i = textsList.getSelectedIndex();
				selectedClassName = textNames[i];
			}
			getQuery().lockToChange(selectedClass);
			setClassProperties(selectedClass);
			getQuery().commitTransaction();
			replaceListWithClass(selectedClass);
			if(j == 0)
				pointsList.select(i);
			else
			if(j == 1)
				linesList.select(i);
			else
			if(j == 2)
				areasList.select(i);
			else
			if(j == 3)
				textsList.select(i);
			setStatus("");
		} else
		{
			showWarning("Select a map object class to edit.");
		}
	}

	void textSpecButton_Clicked(java.awt.event.ActionEvent actionevent)
	{
		textSpecDlg = com.ofx.objectmapper.OmAvailableTextSpecs.getDialog(this, true);
		textSpecButton.setEnabled(false);
		textSpecDlg.addObserver(this);
		textSpecDlg.setVisible(true);
	}

	void symbolButton_Clicked(java.awt.event.ActionEvent actionevent)
	{
		symbolDlg = com.ofx.objectmapper.OmAvailableSymbols.getDialog(this, true);
		symbolDlg.setSymbolType("point");
		symbolButton.setEnabled(false);
		symbolDlg.addObserver(this);
		symbolDlg.setVisible(true);
	}

	void edfoButton_Clicked(java.awt.event.ActionEvent actionevent)
	{
		if(edfoCheck.getState())
		{
			setStatus("Setting up external data source ...");
			edfoDlg = com.ofx.objectmapper.OmAssociateEdfo.getFrame();
			java.lang.String s = nameFld.getText().trim();
			edfoDlg.setup(s, edfoDataSource, extDataDef);
			edfoButton.setEnabled(false);
			edfoDlg.addObserver(this);
			edfoDlg.setVisible(true);
		} else
		{
			showWarning("Check need to associate with external source first.");
		}
	}

	void pointsList_ListSelect(java.awt.event.ItemEvent itemevent)
	{
		int i = pointsList.getSelectedIndex();
		if(i == -1)
			return;
		if(i == currentIndex && currentList == 0)
		{
			pointsList.deselect(i);
			clearFields(0);
			selectedClassName = null;
			selectedClass = null;
			currentList = -1;
			currentIndex = -1;
		} else
		{
			currentIndex = i;
			currentList = 0;
			selectedClassName = pointNames[i];
			retrieveSelectedClass();
			updateFieldsWithClass(selectedClass);
			linesList.deselect(linesList.getSelectedIndex());
			areasList.deselect(areasList.getSelectedIndex());
			textsList.deselect(textsList.getSelectedIndex());
		}
	}

	void linesList_ListSelect(java.awt.event.ItemEvent itemevent)
	{
		int i = linesList.getSelectedIndex();
		if(i == -1)
			return;
		if(i == currentIndex && currentList == 1)
		{
			linesList.deselect(i);
			clearFields(1);
			selectedClassName = null;
			selectedClass = null;
			currentList = -1;
			currentIndex = -1;
		} else
		{
			currentIndex = i;
			currentList = 1;
			selectedClassName = lineNames[i];
			retrieveSelectedClass();
			updateFieldsWithClass(selectedClass);
			pointsList.deselect(pointsList.getSelectedIndex());
			areasList.deselect(areasList.getSelectedIndex());
			textsList.deselect(textsList.getSelectedIndex());
		}
	}

	void areasList_ListSelect(java.awt.event.ItemEvent itemevent)
	{
		int i = areasList.getSelectedIndex();
		if(i == -1)
			return;
		if(i == currentIndex && currentList == 2)
		{
			areasList.deselect(i);
			clearFields(2);
			selectedClassName = null;
			selectedClass = null;
			currentList = -1;
			currentIndex = -1;
		} else
		{
			currentIndex = i;
			currentList = 2;
			selectedClassName = areaNames[i];
			retrieveSelectedClass();
			updateFieldsWithClass(selectedClass);
			pointsList.deselect(pointsList.getSelectedIndex());
			linesList.deselect(linesList.getSelectedIndex());
			textsList.deselect(textsList.getSelectedIndex());
		}
	}

	void textsList_ListSelect(java.awt.event.ItemEvent itemevent)
	{
		int i = textsList.getSelectedIndex();
		if(i == -1)
			return;
		if(i == currentIndex && currentList == 2)
		{
			textsList.deselect(i);
			clearFields(2);
			selectedClassName = null;
			selectedClass = null;
			currentList = -1;
			currentIndex = -1;
		} else
		{
			currentIndex = i;
			currentList = 2;
			selectedClassName = textNames[i];
			retrieveSelectedClass();
			updateFieldsWithClass(selectedClass);
			pointsList.deselect(pointsList.getSelectedIndex());
			linesList.deselect(linesList.getSelectedIndex());
			areasList.deselect(areasList.getSelectedIndex());
		}
	}

	void geomChoice_Action(java.awt.event.ItemEvent itemevent)
	{
		boolean flag = false;
		if(pointsList.getSelectedIndex() < 0 && linesList.getSelectedIndex() < 0 && areasList.getSelectedIndex() < 0 && textsList.getSelectedIndex() < 0)
			flag = true;
		if(geomChoice.getSelectedItem().equals("Text"))
		{
			classTabPanel.showTabPanel(3);
			if(flag)
				clearFields(0);
		} else
		if(geomChoice.getSelectedItem().equals("Point"))
		{
			classTabPanel.showTabPanel(0);
			if(flag)
				clearFields(0);
		} else
		if(geomChoice.getSelectedItem().equals("Line"))
		{
			classTabPanel.showTabPanel(1);
			if(flag)
				clearFields(1);
		} else
		{
			classTabPanel.showTabPanel(2);
			if(flag)
				clearFields(2);
		}
	}

	void classTabPanel_propertyChange(java.beans.PropertyChangeEvent propertychangeevent)
	{
		boolean flag = false;
		if(pointsList.getSelectedIndex() < 0 && linesList.getSelectedIndex() < 0 && areasList.getSelectedIndex() < 0 && textsList.getSelectedIndex() < 0)
			flag = true;
		if(classTabPanel.getCurrentPanelNdx() == 3)
		{
			if(flag)
			{
				clearFields(0);
				geomChoice.select("Text");
			}
		} else
		if(classTabPanel.getCurrentPanelNdx() == 0)
		{
			if(flag)
			{
				clearFields(0);
				geomChoice.select("Point");
			}
		} else
		if(classTabPanel.getCurrentPanelNdx() == 1)
		{
			if(flag)
			{
				clearFields(1);
				geomChoice.select("Line");
			}
		} else
		if(flag)
		{
			clearFields(2);
			geomChoice.select("Area");
		}
	}

	public com.ofx.query.SxQueryInterface getQuery()
	{
		return com.ofx.base.SxEnvironment.singleton().getQuery();
	}

	private void setClassProperties(com.ofx.repository.SxClass sxclass)
	{
		sxclass.setDescription(descField.getText());
		sxclass.setTextMinScale((new Double(labelLowScaleFld.getText().trim())).doubleValue());
		sxclass.setTextMaxScale((new Double(labelHighScaleFld.getText().trim())).doubleValue());
		sxclass.setSymbolMinScale((new Double(symLowScaleFld.getText().trim())).doubleValue());
		sxclass.setSymbolMaxScale((new Double(symHighScaleFld.getText().trim())).doubleValue());
		sxclass.setNeedsGeneratedIdValues(generateIdCheck.getState());
		if(edfoCheck.getState() && edfoDataSource != null && extDataDef != null)
		{
			sxclass.setDataSource(edfoDataSource);
			sxclass.setExternalDataDef(extDataDef);
			getQuery().lockToChange(extDataDef);
		} else
		{
			sxclass.setDataSource((com.ofx.repository.SxDataSource)null);
			sxclass.setExternalDataDef((com.ofx.repository.SxExternalDataDef)null);
		}
		try
		{
			sxclass.setLabelSpec(textSpec);
			com.ofx.repository.SxTextSpec sxtextspec = sxclass.getLabelSpec();
			getQuery().lockToChange(sxtextspec);
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log(exception);
		}
		sxclass.setSymbology(newSymbology);
		com.ofx.repository.SxSymbology sxsymbology = sxclass.getSymbology();
		getQuery().lockToChange(sxsymbology);
	}

	private void populateListWithClass(com.ofx.repository.SxClass sxclass)
	{
		if(sxclass.isPointType())
		{
			updatePointList();
			for(int i = 0; i < pointNamesSize; i++)
				if(pointNames[i].equals(sxclass.getID()))
				{
					pointsList.select(i);
					currentIndex = i;
					currentList = 0;
					selectedClassName = pointNames[i];
					retrieveSelectedClass();
				}

		}
		if(sxclass.isPolylineType())
		{
			updateLineList();
			for(int j = 0; j < lineNamesSize; j++)
				if(lineNames[j].equals(sxclass.getID()))
				{
					linesList.select(j);
					currentIndex = j;
					currentList = 1;
					selectedClassName = lineNames[j];
					retrieveSelectedClass();
				}

		}
		if(sxclass.isPolygonType())
		{
			updateAreaList();
			for(int k = 0; k < areaNamesSize; k++)
				if(areaNames[k].equals(sxclass.getID()))
				{
					areasList.select(k);
					currentIndex = k;
					currentList = 2;
					selectedClassName = areaNames[k];
					retrieveSelectedClass();
				}

		}
		if(sxclass.isTextType())
		{
			updateTextList();
			for(int i = 0; i < textNamesSize; i++)
				if(textNames[i].equals(sxclass.getID()))
				{
					textsList.select(i);
					currentIndex = i;
					currentList = 0;
					selectedClassName = textNames[i];
					retrieveSelectedClass();
				}

		}
	}

	private void replaceListWithClass(com.ofx.repository.SxClass sxclass)
	{
		if(sxclass.isPointType())
			replaceList(pointsList, pointNames, sxclass, pointRows);
		if(sxclass.isPolylineType())
			replaceList(linesList, lineNames, sxclass, lineRows);
		if(sxclass.isPolygonType())
			replaceList(areasList, areaNames, sxclass, areaRows);
		if(sxclass.isTextType())
			replaceList(textsList, textNames, sxclass, textRows);
	}

	private void updateFieldsWithClass(com.ofx.repository.SxClass sxclass)
	{
		setStatus("               Examining existing class definition...");
		try
		{
			nameFld.setText(sxclass.getID());
			descField.setText(sxclass.getDescription());
			if(sxclass.isPointType())
				geomChoice.select("Point");
			else
			if(sxclass.isPolylineType())
				geomChoice.select("Line");
			else
			if(sxclass.isPolygonType())
				geomChoice.select("Area");
			else
			if(sxclass.isTextType())
				geomChoice.select("Text");
			newSymbology = sxclass.getSymbology();
			symbolCanvas.setSymbology(newSymbology);
			if(sxclass.isPointType())
				symbolCanvas.setDimension(0);
			else
			if(sxclass.isPolylineType())
				symbolCanvas.setDimension(1);
			else
			if(sxclass.isPolygonType())
				symbolCanvas.setDimension(2);
			else
			if(sxclass.isTextType())
				symbolCanvas.setDimension(3);
			symbolCanvas.update();
			symbolSize = (new Integer(newSymbology.getSymbolSize())).toString();
			textSpec = sxclass.getLabelSpec();
			setLabelFont(textSpec);
			labelLowScaleFld.setText(java.lang.String.valueOf(sxclass.getTextMinScale()));
			labelHighScaleFld.setText(java.lang.String.valueOf(sxclass.getTextMaxScale()));
			symLowScaleFld.setText(java.lang.String.valueOf(sxclass.getSymbolMinScale()));
			symHighScaleFld.setText(java.lang.String.valueOf(sxclass.getSymbolMaxScale()));
			generateIdCheck.setState(sxclass.getNeedsGeneratedIdValues());
			edfoDataSource = sxclass.getDataSource();
			extDataDef = sxclass.getExternalDataDef();
			edfoCheck.setState(edfoDataSource != null);
			enableGenerateIdCheck();
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log(exception);
		}
		setStatus(" ");
	}

	private void enableGenerateIdCheck()
		throws java.lang.Exception
	{
		java.lang.String s = nameFld.getText();
		if(com.ofx.repository.SxClass.exists(s, getQuery()))
		{
			int i = 0;
			if(!edfoCheck.getState() && getQuery().getObjects(s).elements().hasMoreElements())
				i = 1;
			if(s.startsWith("MARKER") || i > 0)
			{
				generateIdCheck.setEnabled(false);
				geomChoice.setEnabled(false);
				edfoCheck.setEnabled(false);
				edfoButton.setEnabled(false);
			} else
			{
				generateIdCheck.setEnabled(true);
				geomChoice.setEnabled(true);
				edfoCheck.setEnabled(true);
				edfoButton.setEnabled(true);
			}
		} else
		{
			generateIdCheck.setEnabled(true);
			geomChoice.setEnabled(true);
			edfoCheck.setEnabled(true);
			edfoButton.setEnabled(true);
		}
	}

	private void clearFields(int i)
	{
		nameFld.setText("");
		descField.setText("");
		if(i == 3)
		{
			geomChoice.select("Text");
			newSymbology = new SxSymbology("DEFAULT", "DEFAULT");
			newSymbology.setFillColorRGB(java.awt.Color.white.getRGB());
			newSymbology.setFillPatternID(-1);
			newSymbology.setOutlineColorRGB(java.awt.Color.black.getRGB());
			newSymbology.setOutlinePatternID(-1);
		} else
		if(i == 0)
		{
			geomChoice.select("Point");
			newSymbology = new SxSymbology("DEFAULT", "DEFAULT");
			newSymbology.setFillColorRGB(java.awt.Color.white.getRGB());
			newSymbology.setFillPatternID(-1);
			newSymbology.setOutlineColorRGB(java.awt.Color.black.getRGB());
			newSymbology.setOutlinePatternID(-1);
		} else
		if(i == 1)
		{
			geomChoice.select("Line");
			newSymbology = new SxSymbology("DEFAULT LINE", "DEFAULT LINE");
			newSymbology.setFillColorRGB(java.awt.Color.black.getRGB());
			newSymbology.setFillPatternID(-1);
			newSymbology.setOutlineColorRGB(java.awt.Color.black.getRGB());
			newSymbology.setOutlinePatternID(-1);
			newSymbology.setLineWidth(1);
			newSymbology.setOutlineWidth(0);
		} else
		{
			geomChoice.select("Area");
			newSymbology = new SxSymbology("DEFAULT AREA", "DEFAULT AREA");
			newSymbology.setFillColorRGB(java.awt.Color.white.getRGB());
			newSymbology.setFillPatternID(-1);
			newSymbology.setOutlineColorRGB(java.awt.Color.black.getRGB());
			newSymbology.setOutlinePatternID(-1);
			newSymbology.setLineWidth(0);
			newSymbology.setOutlineWidth(1);
		}
		geomChoice.setEnabled(true);
		generateIdCheck.setEnabled(true);
		generateIdCheck.setState(false);
		symbolCanvas.setSymbology(newSymbology);
		symbolCanvas.setDimension(0);
		symbolCanvas.update();
		if(i == 0)
		{
			textSpec = new SxTextSpec("DEFAULT", "DEFAULT");
			textSpec.setDefaultValues();
		} else
		{
			textSpec = new SxTextSpec("DEFAULT_CENTERED", "DEFAULT_CENTERED");
			textSpec.setDefaultValues();
			textSpec.setHorizontalOffset(com.ofx.repository.SxTextSpec.getHorizontalOffsetForOn());
			textSpec.setVerticalOffset(com.ofx.repository.SxTextSpec.getVerticalOffsetForAbove());
		}
		setLabelFont(textSpec);
		labelLowScaleFld.setText("0");
		labelHighScaleFld.setText("32000");
		symLowScaleFld.setText("0");
		symHighScaleFld.setText("32000");
		edfoCheck.setEnabled(true);
		edfoCheck.setState(false);
		edfoButton.setEnabled(true);
		edfoDataSource = null;
		extDataDef = null;
	}

	private void setLabelFont(com.ofx.repository.SxTextSpec sxtextspec)
	{
		int i = (labelImage.getSize().width - 6) / 2;
		int j = (labelImage.getSize().height - 2) / 2;
		java.awt.Rectangle rectangle = new Rectangle(i + 3, j + 1, 0, 0);
		com.ofx.mapViewer.SxMapText sxmaptext = new SxMapText("Label", sxtextspec, rectangle, com.ofx.base.SxFont.getFont(sxtextspec, false), getQuery());
		java.awt.Image image = createImage(i * 2 + 4, j * 2);
		if(image != null)
		{
			java.awt.Graphics g = image.getGraphics();
			sxmaptext.draw(g, new SxDisplayHint());
			try
			{
				labelImage.setImage(image);
			}
			catch(java.beans.PropertyVetoException propertyvetoexception) { }
			labelImage.repaint();
		}
	}

	private int getStyle(com.ofx.repository.SxTextSpec sxtextspec)
	{
		if(sxtextspec.getBold() && !sxtextspec.getItalic())
			return 1;
		if(!sxtextspec.getBold() && !sxtextspec.getItalic())
			return 0;
		if(!sxtextspec.getBold() && sxtextspec.getItalic())
			return 2;
		return !sxtextspec.getBold() || !sxtextspec.getItalic() ? 0 : 3;
	}

	private void deleteClass()
	{
		byte byte0 = 0;
		setStatus("Deleting map object class '" + selectedClassName + "'...");
		boolean flag = classToDelete.isPointType();
		boolean flag1 = classToDelete.isPolylineType();
		boolean flag2 = classToDelete.isPolygonType();
		boolean flag3 = classToDelete.isTextType();
		com.ofx.repository.SxExternalDataDef sxexternaldatadef = classToDelete.getExternalDataDef();
		if(sxexternaldatadef != null)
		{
			getQuery().lockToChange(sxexternaldatadef);
			try
			{
				sxexternaldatadef.delete(getQuery());
			}
			catch(java.lang.Exception exception)
			{
				com.ofx.base.SxEnvironment.log(exception);
			}
		}
		getQuery().lockToChange(classToDelete);
		try
		{
			com.ofx.repository.SxClass.delete(selectedClassName, getQuery());
		}
		catch(java.lang.Exception exception1)
		{
			com.ofx.base.SxEnvironment.log(exception1);
		}
		getQuery().commitTransaction();
		if(flag)
		{
			byte0 = 0;
			updatePointList();
		}
		if(flag1)
		{
			byte0 = 1;
			updateLineList();
		}
		if(flag2)
		{
			byte0 = 2;
			updateAreaList();
		}
		if(flag3)
		{
			byte0 = 3;
			updateTextList();
		}
		classTabPanel.showTabPanel(byte0);
		setStatus("");
	}

	private void deleteInstances()
	{
		setStatus("Deleting ALL instances of '" + selectedClassName + "'...");
		getQuery().lockToChange(classToDelete);
		try
		{
			classToDelete.deleteInstances(getQuery());
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log(exception);
		}
		getQuery().commitTransaction();
		setStatus("");
	}

	private boolean isClassInUse(java.lang.String s)
	{
		try
		{
			for(java.util.Enumeration enumeration = com.ofx.repository.SxMap.objectIDs(getQuery()); enumeration.hasMoreElements();)
			{
				com.ofx.repository.SxMap sxmap = com.ofx.repository.SxMap.retrieve((java.lang.String)enumeration.nextElement(), getQuery());
				java.util.Vector vector = sxmap.getForegroundClassNames();
				if(vector.contains(s))
					return true;
				vector = sxmap.getBackgroundClassNames();
				if(vector.contains(s))
					return true;
			}

		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log(exception);
		}
		return false;
	}

	private void initialize()
	{
		symbology = new SxSymbology("DEFAULT", "DEFAULT");
		symbology.setFillColorRGB(java.awt.Color.white.getRGB());
		symbology.setOutlineColorRGB(java.awt.Color.black.getRGB());
		classTabPanel.showTabPanel(0);
	}

	private void readMapObjects()
	{
		setStatus("                  Loading Map Object Classes...");
		updateList();
		classTabPanel.showTabPanel(0);
		classTabPanel.addCurrentTabListener(this);
		clearFields(0);
		setStatus("");
	}

	private void updateList()
	{
		updatePointList();
		updateLineList();
		updateAreaList();
		updateTextList();
	}

	private void updatePointList()
	{
		pointsList.setVisible(false);
		if(pointsList.getItemCount() > 0)
			pointsList.removeAll();
		try
		{
			java.util.Enumeration enumeration = com.ofx.repository.SxClass.objectIDs(getQuery());
			int i = com.ofx.repository.SxClass.objects(getQuery()).size();
			pointNames = new java.lang.String[i];
			pointRows = 0;
			while(enumeration.hasMoreElements()) 
			{
				com.ofx.repository.SxClass sxclass = com.ofx.repository.SxClass.retrieve((java.lang.String)enumeration.nextElement(), getQuery());
				if(sxclass.isPointType() && (routingInstalled() || sxclass.getIsUserDefined()))
				{
					populateList(pointsList, pointNames, sxclass, pointRows);
					pointRows++;
				}
			}
			pointNamesSize = pointRows;
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log(exception);
		}
		pointsList.setVisible(true);
	}

	private void updateLineList()
	{
		linesList.setVisible(false);
		if(linesList.getItemCount() > 0)
			linesList.removeAll();
		try
		{
			java.util.Enumeration enumeration = com.ofx.repository.SxClass.objectIDs(getQuery());
			int i = com.ofx.repository.SxClass.objects(getQuery()).size();
			lineNames = new java.lang.String[i];
			lineRows = 0;
			while(enumeration.hasMoreElements()) 
			{
				com.ofx.repository.SxClass sxclass = com.ofx.repository.SxClass.retrieve((java.lang.String)enumeration.nextElement(), getQuery());
				if(sxclass.isPolylineType())
				{
					populateList(linesList, lineNames, sxclass, lineRows);
					lineRows++;
				}
			}
			lineNamesSize = lineRows;
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log(exception);
		}
		linesList.setVisible(true);
	}

	private void updateAreaList()
	{
		areasList.setVisible(false);
		if(areasList.getItemCount() > 0)
			areasList.removeAll();
		try
		{
			java.util.Enumeration enumeration = com.ofx.repository.SxClass.objectIDs(getQuery());
			int i = com.ofx.repository.SxClass.objects(getQuery()).size();
			areaNames = new java.lang.String[i];
			areaRows = 0;
			while(enumeration.hasMoreElements()) 
			{
				com.ofx.repository.SxClass sxclass = com.ofx.repository.SxClass.retrieve((java.lang.String)enumeration.nextElement(), getQuery());
				if(sxclass.isPolygonType())
				{
					populateList(areasList, areaNames, sxclass, areaRows);
					areaRows++;
				}
			}
			areaNamesSize = areaRows;
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log(exception);
		}
		areasList.setVisible(true);
	}

	private void updateTextList()
	{
		textsList.setVisible(false);
		if(textsList.getItemCount() > 0)
			textsList.removeAll();
		try
		{
			java.util.Enumeration enumeration = com.ofx.repository.SxClass.objectIDs(getQuery());
			int i = com.ofx.repository.SxClass.objects(getQuery()).size();
			textNames = new java.lang.String[i];
			textRows = 0;
			while(enumeration.hasMoreElements()) 
			{
				com.ofx.repository.SxClass sxclass = com.ofx.repository.SxClass.retrieve((java.lang.String)enumeration.nextElement(), getQuery());
				if(sxclass.isTextType() && (routingInstalled() || sxclass.getIsUserDefined()))
				{
					populateList(textsList, textNames, sxclass, textRows);
					textRows++;
				}
			}
			textNamesSize = textRows;
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log(exception);
		}
		textsList.setVisible(true);
	}

	private void populateList(java.awt.List list, java.lang.String as[], com.ofx.repository.SxClass sxclass, int i)
	{
		java.lang.String s = buildEntryString(sxclass);
		list.add(s, i);
		as[i] = sxclass.getID();
	}

	private void replaceListItem(java.awt.List list, java.lang.String as[], com.ofx.repository.SxClass sxclass, int i)
	{
		java.lang.String s = buildEntryString(sxclass);
		list.replaceItem(s, i);
		as[i] = sxclass.getID();
	}

	private java.lang.String buildEntryString(com.ofx.repository.SxClass sxclass)
	{
		try
		{
			java.lang.String s = sxclass.getID();
			symbology = sxclass.getSymbology();
			java.lang.String s1 = symbology.getID();
			com.ofx.repository.SxTextSpec sxtextspec = sxclass.getLabelSpec();
			java.lang.String s2 = sxtextspec.getID();
			java.lang.String s3 = "                         ";
			int i = s.length();
			if(i > s3.length())
				i = s3.length() - 2;
			java.lang.String s4 = s + s3.substring(i);
			i = s1.length();
			if(i > s3.length())
				i = s3.length() - 2;
			s4 = s4 + s1 + s3.substring(i);
			s4 = s4 + s2;
			return s4;
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log(exception);
		}
		return " ";
	}

	private void replaceList(java.awt.List list, java.lang.String as[], com.ofx.repository.SxClass sxclass, int i)
	{
		for(int j = 0; j < i; j++)
		{
			selectedClassName = as[j];
			if(!selectedClassName.equals(sxclass.getID()))
				continue;
			replaceListItem(list, as, sxclass, j);
			break;
		}

	}

	public void update(java.util.Observable observable, java.lang.Object obj)
	{
		com.ofx.base.SxStateChange sxstatechange = (com.ofx.base.SxStateChange)obj;
		int i = sxstatechange.getState();
		int j = sxstatechange.getContext();
		if(i == 111 && j == 257)
		{
			edfoDataSource = edfoDlg.getDataSource();
			extDataDef = edfoDlg.getExternalDataDef();
			if(edfoDataSource != null && extDataDef != null)
			{
				java.lang.String s = descField.getText();
				int k;
				if(s.trim().equals(""))
					k = 0;
				else
					k = s.indexOf(" [EDFO = ");
				if(k == -1)
					k = s.length();
				descField.setText(s.substring(0, k) + " [EDFO = " + edfoDataSource.getID() + ", " + extDataDef.getThemeName() + ", " + extDataDef.getLayerName() + ", " + extDataDef.getIdentifierAttributeName() + ", " + extDataDef.getLabelAttributeName() + ", " + extDataDef.getAttributeQuery() + "]");
			}
			edfoButton.setEnabled(true);
			edfoDlg.close();
			edfoDlg = null;
			setStatus(" ");
		}
		if(i == 104 && j == 207)
		{
			java.lang.String s1 = symbolDlg.getSymbologyName();
			if(s1 != null)
				try
				{
					newSymbology = com.ofx.repository.SxSymbology.retrieve(s1, getQuery());
					symbolCanvas.setSymbology(newSymbology);
					symbolCanvas.update();
				}
				catch(java.lang.Exception exception)
				{
					com.ofx.base.SxEnvironment.log(exception);
				}
			symbolButton.setEnabled(true);
			symbolDlg.close();
			symbolDlg = null;
		}
		if(i == 104 && j == 256)
		{
			java.lang.String s2 = textSpecDlg.getTextSpecName();
			if(s2 != null)
				try
				{
					textSpec = com.ofx.repository.SxTextSpec.retrieve(s2, getQuery());
					setLabelFont(textSpec);
				}
				catch(java.lang.Exception exception1)
				{
					com.ofx.base.SxEnvironment.log(exception1);
				}
			textSpecButton.setEnabled(true);
			textSpecDlg.close();
			textSpecDlg = null;
		}
		if(i == 104 && j == 252)
		{
			delete = reqDlg.getRequest();
			if(delete)
			{
				if(deleteAction.equals("class"))
				{
					deleteClass();
					classToDelete = null;
					clearFields(0);
					selectedClassName = null;
					selectedClass = null;
					currentList = -1;
					currentIndex = -1;
				} else
				if(deleteAction.equals("instances"))
				{
					deleteInstances();
					classToDelete = null;
				}
				deleteAction = null;
			}
			reqDlg.close();
			reqDlg = null;
		}
	}

	public void addObserver(java.util.Observer observer)
	{
		com.ofx.objectmapper.SxObservableDispatcher.addObserver(this, observer);
	}

	private void notifyObservers(java.lang.Object obj)
	{
		com.ofx.objectmapper.SxObservableDispatcher.notify(this, obj);
	}

	private java.lang.String getGeomTypeFromChoice()
	{
		java.lang.String s = geomChoice.getSelectedItem();
		if(s.equals("Point"))
			return "Point";
		if(s.equals("Line"))
			return "Polyline";
		if(s.equals("Area"))
			return "Polygon";
		if(s.equals("Text"))
			return "Text";
		return null;
	}

	private void setStatus(java.lang.String s)
	{
		statusLabel.setText(s);
	}

	private boolean edfoInstalled()
	{
		boolean flag = true;
		try
		{
			if(edfoMarker == null)
				edfoMarker = com.ofx.dataAdapter.SxSpatialDataReader.getEdfoMarkerClass();
			java.lang.Class.forName(edfoMarker);
		}
		catch(java.lang.ClassNotFoundException classnotfoundexception)
		{
			flag = false;
		}
		catch(com.ofx.base.SxException sxexception)
		{
			flag = false;
		}
		return flag;
	}

	private boolean routingInstalled()
	{
		boolean flag = true;
		try
		{
			java.lang.Class.forName("com.ofx.routing.objectfx.SxRoutingEngine");
		}
		catch(java.lang.ClassNotFoundException classnotfoundexception)
		{
			flag = false;
		}
		return flag;
	}

	private void retrieveSelectedClass()
	{
		try
		{
			selectedClass = com.ofx.repository.SxClass.retrieve(selectedClassName, getQuery());
			extDataDef = com.ofx.repository.SxExternalDataDef.retrieve(selectedClass.getExternalDataDefPrim(), getQuery());
			selectedClass.setExternalDataDef(extDataDef);
			edfoDataSource = com.ofx.repository.SxDataSource.retrieve(selectedClass.getDataSourcePrim(), getQuery());
			selectedClass.setDataSource(edfoDataSource);
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log(exception);
		}
	}

	private OmMapObjectClasses()
	{
		pointNamesSize = 0;
		lineNamesSize = 0;
		areaNamesSize = 0;
		textNamesSize = 0;
		selectedClassName = null;
		edfoDataSource = null;
		extDataDef = null;
		symbology = null;
		symbolCanvas = new OmSymbolCanvas();
		pointRows = 0;
		lineRows = 0;
		areaRows = 0;
		textRows = 0;
		selectedClass = null;
		currentIndex = -1;
		currentList = -1;
		classToDelete = null;
		delete = false;
		deleteAction = null;
		action = null;
		edfoMarker = null;
		com.ofx.objectmapper.SxObservableDispatcher.register(this);
		addWindowListener(this);
		java.awt.GridBagLayout gridbaglayout = new GridBagLayout();
		setLayout(gridbaglayout);
		addNotify();
		setSize(getInsets().left + getInsets().right + 550, getInsets().top + getInsets().bottom + 595);
		setBackground(new Color(0xc0c0c0));
		java.awt.Font font = new Font("Dialog", 0, 12);
		java.awt.Font font1 = new Font("Monospaced", 1, 12);
		java.awt.Font font2 = new Font("Monospaced", 0, 12);
		descriptionPanel = new OmBorderedPanel(this, 99F, 7F, 2, 1);
		java.awt.GridBagLayout gridbaglayout1 = new GridBagLayout();
		descriptionPanel.setLayout(gridbaglayout1);
		descriptionPanel.setBackground(new Color(0xc0c0c0));
		iconImage = new ImageViewer();
		iconImage.setBounds(0, 1, 30, 30);
		java.awt.GridBagConstraints gridbagconstraints1 = new GridBagConstraints();
		gridbagconstraints1.gridx = 0;
		gridbagconstraints1.gridy = 0;
		gridbagconstraints1.anchor = 17;
		gridbagconstraints1.fill = 0;
		gridbagconstraints1.insets = new Insets(3, 10, 0, 10);
		((java.awt.GridBagLayout)descriptionPanel.getLayout()).setConstraints(iconImage, gridbagconstraints1);
		descriptionPanel.add(iconImage);
		try
		{
			iconImage.setImageURL(getClass().getResource("/images/definobj.gif"));
		}
		catch(java.beans.PropertyVetoException propertyvetoexception) { }
		descriptionLabel = new Label("Create, modify, or delete map object classes.");
		gridbagconstraints1 = new GridBagConstraints();
		gridbagconstraints1.gridx = 3;
		gridbagconstraints1.gridy = 0;
		gridbagconstraints1.gridwidth = 9;
		gridbagconstraints1.weightx = 1.0D;
		gridbagconstraints1.weighty = 1.0D;
		gridbagconstraints1.anchor = 17;
		gridbagconstraints1.fill = 0;
		gridbagconstraints1.insets = new Insets(0, 15, 0, 0);
		((java.awt.GridBagLayout)descriptionPanel.getLayout()).setConstraints(descriptionLabel, gridbagconstraints1);
		descriptionPanel.add(descriptionLabel);
		java.awt.GridBagConstraints gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.fill = 0;
		gridbagconstraints.gridwidth = 0;
		gridbagconstraints.gridx = 0;
		gridbagconstraints.gridy = 0;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 1.0D;
		gridbaglayout.setConstraints(descriptionPanel, gridbagconstraints);
		add(descriptionPanel);
		panel1 = new OmBorderedPanel(this, 99F, 30F);
		panel1.setLayout(new GridLayout(1, 1, 0, 0));
		classTabPanel = new TabPanel();
		classTabPanel.setLayout(null);
		panel1.add(classTabPanel);
		java.lang.String as[] = new java.lang.String[4];
		as[0] = new String("Points");
		as[1] = new String("Lines");
		as[2] = new String("Areas");
		as[3] = new String("Texts");
		try
		{
			classTabPanel.setPanelLabels(as);
		}
		catch(java.beans.PropertyVetoException propertyvetoexception3) { }
		panel3 = new Panel();
		panel3.setLayout(new BorderLayout(0, 0));
		classTabPanel.add(panel3);
		pointsLabel = new Label("  Class Name              Symbol Info              Label Info");
		pointsLabel.setFont(font1);
		panel3.add("North", pointsLabel);
		pointsList = new List();
		pointsList.setFont(font2);
		pointsList.addItemListener(this);
		panel3.add("Center", pointsList);
		panel4 = new Panel();
		panel4.setLayout(new BorderLayout(0, 0));
		classTabPanel.add(panel4);
		linesLabel = new Label("  Class Name              Symbol Info              Label Info");
		linesLabel.setFont(font1);
		panel4.add("North", linesLabel);
		linesList = new List();
		linesList.setFont(font2);
		linesList.addItemListener(this);
		panel4.add("Center", linesList);
		panel5 = new Panel();
		panel5.setLayout(new BorderLayout(0, 0));
		classTabPanel.add(panel5);
		areasLabel = new Label("  Class Name              Symbol Info              Label Info");
		areasLabel.setFont(font1);
		panel5.add("North", areasLabel);
		areasList = new List();
		areasList.addItemListener(this);
		areasList.setFont(font2);
		panel5.add("Center", areasList);

		panel6 = new Panel();
		panel6.setLayout(new BorderLayout(0, 0));
		classTabPanel.add(panel6);
		textsLabel = new Label("  Class Name              Symbol Info              Label Info");
		textsLabel.setFont(font1);
		panel6.add("North", textsLabel);
		textsList = new List();
		textsList.setFont(font2);
		textsList.addItemListener(this);
		panel6.add("Center", textsList);


		gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.fill = 0;
		gridbagconstraints.gridwidth = 0;
		gridbagconstraints.gridx = 0;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 1.0D;
		gridbaglayout.setConstraints(panel1, gridbagconstraints);
		add(panel1);
		infoBorder1 = new OmBorderedPanel(this, 99F, 50F);
		infoBorder1.setLayout(new BorderLayout(0, 4));
		infoPanel17 = new Panel();
		infoPanel17.setLayout(new GridLayout(3, 1, 0, 4));
		infoBorder1.add("North", infoPanel17);
		infoPanel19 = new Panel();
		infoPanel19.setLayout(new GridLayout(1, 2, 8, 0));
		infoPanel17.add(infoPanel19);
		infoPanel3 = new Panel();
		infoPanel3.setLayout(new BorderLayout(10, 0));
		infoPanel19.add(infoPanel3);
		infoLabel1 = new Label("Name:     ");
		infoPanel3.add("West", infoLabel1);
		nameFld = new TextField();
		nameFld.addActionListener(this);
		infoPanel3.add("Center", nameFld);
		emptyPanel = new Panel();
		emptyPanel.setLayout(null);
		infoPanel19.add(emptyPanel);
		infoPanel20 = new Panel();
		infoPanel20.setLayout(new BorderLayout(0, 0));
		infoPanel17.add(infoPanel20);
		infoLabel7 = new Label("Description:");
		infoPanel20.add("West", infoLabel7);
		descField = new TextField();
		infoPanel20.add("Center", descField);
		infoPanel21 = new Panel();
		infoPanel21.setLayout(new GridLayout(1, 2, 8, 0));
		infoPanel17.add(infoPanel21);
		infoPanel4 = new Panel();
		infoPanel4.setLayout(new BorderLayout(0, 0));
		infoPanel21.add(infoPanel4);
		infoLabel2 = new Label("Geometry type:    ");
		infoPanel4.add("West", infoLabel2);
		geomChoice = new Choice();
		geomChoice.addItem("Point");
		geomChoice.addItem("Line");
		geomChoice.addItem("Area");
		geomChoice.addItem("Text");
		geomChoice.setBackground(java.awt.Color.white);
		geomChoice.addItemListener(this);
		infoPanel4.add("Center", geomChoice);
		chkPanel2 = new Panel();
		chkPanel2.setLayout(new BorderLayout(0, 0));
		infoPanel21.add(chkPanel2);
		generateIdCheck = new Checkbox(" Generate identifiers automatically");
		chkPanel2.add("Center", generateIdCheck);
		fillLabel2 = new Label("    ");
		chkPanel2.add("East", fillLabel2);
		infoPanel18 = new Panel();
		infoPanel18.setLayout(new GridLayout(1, 2, 8, 0));
		infoBorder1.add("Center", infoPanel18);
		infoPanel1 = new Panel();
		infoPanel1.setLayout(new BorderLayout(0, 0));
		infoPanel18.add(infoPanel1);
		borderPanel2 = new BorderPanel();
		try
		{
			borderPanel2.setIPadBottom(4);
			borderPanel2.setLabel("Symbology");
			borderPanel2.setPaddingRight(2);
			borderPanel2.setPaddingBottom(2);
			borderPanel2.setPaddingTop(8);
			borderPanel2.setIPadTop(4);
			borderPanel2.setPaddingLeft(2);
			borderPanel2.setAlignStyle(0);
		}
		catch(java.beans.PropertyVetoException propertyvetoexception1) { }
		borderPanel2.setLayout(new GridLayout(2, 1, 0, 4));
		infoPanel1.add("Center", borderPanel2);
		infoPanel5 = new Panel();
		infoPanel5.setLayout(new GridLayout(2, 1, 0, 4));
		borderPanel2.add(infoPanel5);
		infoPanel7 = new Panel();
		infoPanel7.setLayout(new BorderLayout(0, 0));
		infoPanel5.add(infoPanel7);
		infoLabel3 = new Label("    Low scale:  ");
		infoPanel7.add("West", infoLabel3);
		symLowScaleFld = new TextField();
		infoPanel7.add("Center", symLowScaleFld);
		infoPanel8 = new Panel();
		infoPanel8.setLayout(new BorderLayout(0, 0));
		infoPanel5.add(infoPanel8);
		infoLabel4 = new Label("    High scale: ");
		infoPanel8.add("West", infoLabel4);
		symHighScaleFld = new TextField();
		infoPanel8.add("Center", symHighScaleFld);
		infoPanel6 = new Panel();
		infoPanel6.setLayout(new GridLayout(1, 2, 6, 0));
		borderPanel2.add(infoPanel6);
		infoPanel9 = new Panel();
		infoPanel9.setLayout(new GridLayout(1, 1, 0, 0));
		infoPanel6.add(infoPanel9);
		symbolCanvas.setBounds(0, 0, 40, 40);
		symbolCanvas.setBackground(new Color(0xc0c0c0));
		infoPanel9.add(symbolCanvas);
		infoPanel10 = new Panel();
		infoPanel10.setLayout(new GridLayout(2, 1, 0, 0));
		infoPanel6.add(infoPanel10);
		symbolButton = new Button();
		symbolButton.setLabel("Configure  >>");
		symbolButton.addActionListener(this);
		infoPanel10.add(symbolButton);
		infoPanel2 = new Panel();
		infoPanel2.setLayout(new BorderLayout(0, 0));
		infoPanel18.add(infoPanel2);
		borderPanel3 = new BorderPanel();
		try
		{
			borderPanel3.setIPadBottom(4);
			borderPanel3.setLabel("Label");
			borderPanel3.setPaddingRight(2);
			borderPanel3.setPaddingBottom(2);
			borderPanel3.setPaddingTop(8);
			borderPanel3.setIPadTop(4);
			borderPanel3.setPaddingLeft(2);
			borderPanel3.setAlignStyle(0);
		}
		catch(java.beans.PropertyVetoException propertyvetoexception2) { }
		borderPanel3.setLayout(new GridLayout(2, 1, 0, 4));
		infoPanel2.add("Center", borderPanel3);
		infoPanel11 = new Panel();
		infoPanel11.setLayout(new GridLayout(2, 1, 0, 4));
		borderPanel3.add(infoPanel11);
		infoPanel12 = new Panel();
		infoPanel12.setLayout(new BorderLayout(0, 0));
		infoPanel11.add(infoPanel12);
		infoLabel5 = new Label("    Low scale:  ");
		infoPanel12.add("West", infoLabel5);
		labelLowScaleFld = new TextField();
		infoPanel12.add("Center", labelLowScaleFld);
		infoPanel13 = new Panel();
		infoPanel13.setLayout(new BorderLayout(0, 0));
		infoPanel11.add(infoPanel13);
		infoLabel6 = new Label("    High scale: ");
		infoPanel13.add("West", infoLabel6);
		labelHighScaleFld = new TextField();
		infoPanel13.add("Center", labelHighScaleFld);
		infoPanel14 = new Panel();
		infoPanel14.setLayout(new GridLayout(1, 2, 6, 0));
		borderPanel3.add(infoPanel14);
		infoPanel15 = new Panel();
		infoPanel15.setLayout(new GridLayout(1, 1, 0, 0));
		infoPanel14.add(infoPanel15);
		labelImage = new ImageViewer();
		labelImage.setBounds(0, 19, 130, 10);
		infoPanel15.add(labelImage);
		infoPanel16 = new Panel();
		infoPanel16.setLayout(new GridLayout(2, 1, 0, 0));
		infoPanel14.add(infoPanel16);
		textSpecButton = new Button();
		textSpecButton.setLabel("Configure  >>");
		textSpecButton.addActionListener(this);
		infoPanel16.add(textSpecButton);
		ePanel = new Panel();
		ePanel.setLayout(new BorderLayout(8, 0));
		if(edfoInstalled())
			infoBorder1.add("South", ePanel);
		chkPanel = new Panel();
		chkPanel.setLayout(new BorderLayout(8, 0));
		if(edfoInstalled())
			ePanel.add("Center", chkPanel);
		edfoCheck = new Checkbox(" Associate with External Data Source");
		edfoCheck.setState(false);
		if(edfoInstalled())
			chkPanel.add("Center", edfoCheck);
		fillLabel = new Label("      ");
		if(edfoInstalled())
			chkPanel.add("East", fillLabel);
		edfoButton = new Button();
		edfoButton.setLabel("Select External Data...");
		edfoButton.addActionListener(this);
		if(edfoInstalled())
			ePanel.add("East", edfoButton);
		gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.fill = 0;
		gridbagconstraints.gridwidth = 0;
		gridbagconstraints.gridx = 0;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 1.0D;
		gridbaglayout.setConstraints(infoBorder1, gridbagconstraints);
		add(infoBorder1);
		borderPanel1 = new OmBorderedPanel(this, 99F, 6F);
		borderPanel1.setLayout(new GridLayout(1, 1, 1, 1));
		statusLabel = new Label("");
		statusLabel.setFont(new Font("Dialog", 1, 16));
		borderPanel1.add("South", statusLabel);
		gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.fill = 0;
		gridbagconstraints.gridwidth = 0;
		gridbagconstraints.gridx = 0;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 1.0D;
		gridbaglayout.setConstraints(borderPanel1, gridbagconstraints);
		add(borderPanel1);
		borderPanel6 = new OmBorderedPanel(this, 99F, 7F, 2, 1);
		gridbaglayout1 = new GridBagLayout();
		borderPanel6.setLayout(gridbaglayout1);
		addButton = new Button("  New  ");
		addButton.setFont(font);
		addButton.addActionListener(this);
		gridbagconstraints1 = new GridBagConstraints();
		gridbagconstraints1.gridx = 1;
		gridbagconstraints1.gridy = 0;
		gridbagconstraints1.weightx = 1.0D;
		gridbagconstraints1.weighty = 1.0D;
		gridbagconstraints1.fill = 0;
		gridbagconstraints1.insets = new Insets(2, 0, 0, 0);
		gridbaglayout1.setConstraints(addButton, gridbagconstraints1);
		borderPanel6.add(addButton);
		updateButton = new Button("Modify");
		updateButton.setFont(font);
		updateButton.addActionListener(this);
		gridbagconstraints1 = new GridBagConstraints();
		gridbagconstraints1.gridx = 2;
		gridbagconstraints1.gridy = 0;
		gridbagconstraints1.weightx = 1.0D;
		gridbagconstraints1.weighty = 1.0D;
		gridbagconstraints1.fill = 0;
		gridbagconstraints1.insets = new Insets(2, 0, 0, 0);
		gridbaglayout1.setConstraints(updateButton, gridbagconstraints1);
		borderPanel6.add(updateButton);
		deleteButton = new Button("Delete");
		deleteButton.setFont(font);
		deleteButton.addActionListener(this);
		gridbagconstraints1 = new GridBagConstraints();
		gridbagconstraints1.gridx = 3;
		gridbagconstraints1.gridy = 0;
		gridbagconstraints1.weightx = 1.0D;
		gridbagconstraints1.weighty = 1.0D;
		gridbagconstraints1.fill = 0;
		gridbagconstraints1.insets = new Insets(2, 0, 0, 0);
		gridbaglayout1.setConstraints(deleteButton, gridbagconstraints1);
		borderPanel6.add(deleteButton);
		deleteInstancesButton = new Button("Delete Instances");
		deleteInstancesButton.setFont(font);
		deleteInstancesButton.addActionListener(this);
		gridbagconstraints1 = new GridBagConstraints();
		gridbagconstraints1.gridx = 4;
		gridbagconstraints1.gridy = 0;
		gridbagconstraints1.weightx = 1.0D;
		gridbagconstraints1.weighty = 1.0D;
		gridbagconstraints1.fill = 0;
		gridbagconstraints1.insets = new Insets(2, 0, 0, 0);
		gridbaglayout1.setConstraints(deleteInstancesButton, gridbagconstraints1);
		borderPanel6.add(deleteInstancesButton);
		closeButton = new Button(" Close ");
		closeButton.setFont(font);
		closeButton.addActionListener(this);
		gridbagconstraints1 = new GridBagConstraints();
		gridbagconstraints1.gridx = 5;
		gridbagconstraints1.gridy = 0;
		gridbagconstraints1.weightx = 1.0D;
		gridbagconstraints1.weighty = 1.0D;
		gridbagconstraints1.fill = 0;
		gridbagconstraints1.insets = new Insets(2, 0, 0, 0);
		gridbaglayout1.setConstraints(closeButton, gridbagconstraints1);
		borderPanel6.add(closeButton);
		gridbagconstraints = new GridBagConstraints();
		gridbagconstraints.fill = 0;
		gridbagconstraints.gridwidth = 0;
		gridbagconstraints.gridx = 0;
		gridbagconstraints.weightx = 1.0D;
		gridbagconstraints.weighty = 1.0D;
		gridbaglayout.setConstraints(borderPanel6, gridbagconstraints);
		add(borderPanel6);
		setTitle("Define Map Objects");
		initialize();
	}

	public void setVisible(boolean flag)
	{
		if(flag)
			setLocation(240, 50);
		super.setVisible(flag);
		if(flag)
			readMapObjects();
	}

	public void closeWindow()
	{
		if(textSpecDlg != null)
		{
			textSpecDlg.close();
			textSpecDlg = null;
		}
		if(symbolDlg != null)
		{
			symbolDlg.close();
			symbolDlg = null;
		}
		if(edfoDlg != null)
		{
			edfoDlg.close();
			edfoDlg = null;
		}
		setVisible(false);
		com.ofx.base.SxStateChange sxstatechange = new SxStateChange(this, 111, 204);
		notifyObservers(sxstatechange);
	}

	public void windowClosing(java.awt.event.WindowEvent windowevent)
	{
		closeWindow();
	}

	public void actionPerformed(java.awt.event.ActionEvent actionevent)
	{
		java.lang.Object obj = actionevent.getSource();
		if(obj == updateButton)
			updateButton_Clicked(actionevent);
		else
		if(obj == textSpecButton)
			textSpecButton_Clicked(actionevent);
		else
		if(obj == symbolButton)
			symbolButton_Clicked(actionevent);
		else
		if(obj == edfoButton)
			edfoButton_Clicked(actionevent);
		else
		if(obj == addButton)
			addButton_Clicked(actionevent);
		else
		if(obj == deleteButton)
			deleteButton_Clicked(actionevent);
		else
		if(obj == deleteInstancesButton)
			deleteInstancesButton_Clicked(actionevent);
		else
		if(obj == closeButton)
			closeWindow();
		else
		if(obj == nameFld)
			nameFld_EnterHit(actionevent);
	}

	public void itemStateChanged(java.awt.event.ItemEvent itemevent)
	{
		java.lang.Object obj = itemevent.getSource();
		if(obj == pointsList)
			pointsList_ListSelect(itemevent);
		else
		if(obj == linesList)
			linesList_ListSelect(itemevent);
		else
		if(obj == areasList)
			areasList_ListSelect(itemevent);
		else
		if(obj == textsList)
			textsList_ListSelect(itemevent);
		else
		if(obj == geomChoice)
			geomChoice_Action(itemevent);
	}

	public void propertyChange(java.beans.PropertyChangeEvent propertychangeevent)
	{
		java.lang.Object obj = propertychangeevent.getSource();
		if(obj == classTabPanel)
			classTabPanel_propertyChange(propertychangeevent);
	}

	private static com.ofx.objectmapper.OmMapObjectClasses _theFrame;
	java.lang.String pointNames[];
	java.lang.String lineNames[];
	java.lang.String areaNames[];
	java.lang.String textNames[];
	int pointNamesSize;
	int lineNamesSize;
	int areaNamesSize;
	int textNamesSize;
	java.lang.String selectedClassName;
	com.ofx.repository.SxDataSource edfoDataSource;
	com.ofx.repository.SxExternalDataDef extDataDef;
	com.ofx.repository.SxSymbology symbology;
	com.ofx.repository.SxSymbology newSymbology;
	com.ofx.objectmapper.OmSymbolCanvas symbolCanvas;
	java.lang.String symbolSize;
	com.ofx.objectmapper.OmAssociateEdfo edfoDlg;
	com.ofx.objectmapper.OmAvailableSymbols symbolDlg;
	com.ofx.objectmapper.OmAvailableTextSpecs textSpecDlg;
	int pointRows;
	int lineRows;
	int areaRows;
	int textRows;
	com.ofx.repository.SxClass selectedClass;
	int currentIndex;
	int currentList;
	com.ofx.repository.SxTextSpec textSpec;
	com.ofx.repository.SxClass classToDelete;
	com.ofx.objectmapper.OmDialogRequest reqDlg;
	boolean delete;
	java.lang.String deleteAction;
	java.lang.String action;
	private java.lang.String edfoMarker;
	com.ofx.objectmapper.OmBorderedPanel borderPanel1;
	com.ofx.objectmapper.OmBorderedPanel panel1;
	java.awt.Panel panel4;
	java.awt.Panel panel5;
	java.awt.Panel panel6;
	java.awt.Panel panel9;
	symantec.itools.awt.TabPanel classTabPanel;
	java.awt.Panel panel3;
	java.awt.List pointsList;
	java.awt.Label pointsLabel;
	java.awt.List linesList;
	java.awt.Label linesLabel;
	java.awt.List areasList;
	java.awt.Label areasLabel;
	java.awt.List textsList;
	java.awt.Label textsLabel;
	com.ofx.objectmapper.OmBorderedPanel infoBorder1;
	symantec.itools.awt.BorderPanel borderPanel2;
	symantec.itools.awt.BorderPanel borderPanel3;
	symantec.itools.multimedia.ImageViewer labelImage;
	java.awt.Panel infoPanel17;
	java.awt.Panel infoPanel19;
	java.awt.Panel infoPanel3;
	java.awt.Label infoLabel1;
	java.awt.TextField nameFld;
	java.awt.Panel emptyPanel;
	java.awt.Panel ePanel;
	java.awt.Panel chkPanel;
	java.awt.Label fillLabel;
	java.awt.Button edfoButton;
	java.awt.Checkbox edfoCheck;
	java.awt.Panel infoPanel20;
	java.awt.Label infoLabel7;
	java.awt.TextField descField;
	java.awt.Panel infoPanel21;
	java.awt.Checkbox generateIdCheck;
	java.awt.Panel chkPanel2;
	java.awt.Label fillLabel2;
	java.awt.Panel infoPanel4;
	java.awt.Label infoLabel2;
	java.awt.Choice geomChoice;
	java.awt.Panel infoPanel18;
	java.awt.Panel infoPanel1;
	java.awt.Panel infoPanel5;
	java.awt.Panel infoPanel7;
	java.awt.Label infoLabel3;
	java.awt.TextField symLowScaleFld;
	java.awt.Panel infoPanel8;
	java.awt.Label infoLabel4;
	java.awt.TextField symHighScaleFld;
	java.awt.Panel infoPanel6;
	java.awt.Panel infoPanel9;
	java.awt.Panel infoPanel10;
	java.awt.Button symbolButton;
	java.awt.Panel infoPanel2;
	java.awt.Panel infoPanel11;
	java.awt.Panel infoPanel12;
	java.awt.Label infoLabel5;
	java.awt.TextField labelLowScaleFld;
	java.awt.Panel infoPanel13;
	java.awt.Label infoLabel6;
	java.awt.TextField labelHighScaleFld;
	java.awt.Panel infoPanel14;
	java.awt.Panel infoPanel15;
	java.awt.Panel infoPanel16;
	java.awt.Button textSpecButton;
	java.awt.Label statusLabel;
	com.ofx.objectmapper.OmBorderedPanel borderPanel6;
	java.awt.Button addButton;
	java.awt.Button updateButton;
	java.awt.Button deleteButton;
	java.awt.Button deleteInstancesButton;
	java.awt.Button closeButton;
	com.ofx.objectmapper.OmBorderedPanel descriptionPanel;
	java.awt.Label descriptionLabel;
	symantec.itools.multimedia.ImageViewer iconImage;
}
