/*-
 * $Id: ProtoElementsImportCommand.java,v 1.3 2005/10/30 14:49:18 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.xmlbeans.XmlException;

import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.AMFICOM.scheme.xml.SchemeProtoGroupsDocument;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeProtoGroup;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeProtoGroupSeq;
import com.syrus.util.Log;

public class ProtoElementsImportCommand extends ImportExportCommand {
	SchemeTabbedPane pane;
	
	public ProtoElementsImportCommand(SchemeTabbedPane pane) {
		this.pane = pane;
	}
	
	@Override
	public void execute() {
		super.execute();

		final String fileName = openFileForReading( 
				LangModelScheme.getString("Title.open.protos_xml"));
		if(fileName == null)
			return;

		try {
			loadProtosXML(fileName);
		} catch (CreateObjectException e) {
			Log.errorMessage(e.getMessage());
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.scheme_import"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
					JOptionPane.ERROR_MESSAGE);
			return;
		} catch (XmlException e) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
					LangModelScheme.getString("Message.error.parse_xml"),  //$NON-NLS-1$
					LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
					JOptionPane.ERROR_MESSAGE);
			Log.errorMessage(e);
			return;
		} catch (IOException e) {
			Log.errorMessage(e);
			return;
		}		 

		ApplicationModel aModel = this.pane.getContext().getApplicationModel();
		aModel.setEnabled("Menu.import.commit", true);
		aModel.fireModelChanged();
	}
	
	protected void loadProtosXML(String fileName) throws CreateObjectException, XmlException, IOException {
		File xmlfile = new File(fileName);
		SchemeProtoGroupsDocument doc = SchemeProtoGroupsDocument.Factory.parse(xmlfile);
		
		try {
			if(!validateXml(doc)) {
				throw new XmlException("Invalid XML");
			}
		} catch (Exception e) {
			throw new XmlException(e);
		}
		
		String user_dir = System.getProperty(USER_DIR);
		System.setProperty(USER_DIR,  xmlfile.getParent());
		
		XmlSchemeProtoGroupSeq xmlProtoGroups = doc.getSchemeProtoGroups();
		XmlSchemeProtoGroup[] xmlProtoGroupsArray = xmlProtoGroups.getSchemeProtoGroupArray();
		for(int i = 0; i < xmlProtoGroupsArray.length; i++) {
			XmlSchemeProtoGroup xmlSchemeProtoGroup = xmlProtoGroupsArray[i];
			SchemeProtoGroup.createInstance(this.userId, xmlSchemeProtoGroup);
		}
		System.setProperty(USER_DIR,  user_dir);
	}
}
