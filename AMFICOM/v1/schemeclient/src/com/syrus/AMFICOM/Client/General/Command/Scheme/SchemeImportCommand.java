/*-
 * $Id: SchemeImportCommand.java,v 1.33 2005/10/30 14:49:18 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.apache.xmlbeans.XmlException;

import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.utils.ClientUtils;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.AMFICOM.scheme.xml.SchemesDocument;
import com.syrus.AMFICOM.scheme.xml.XmlScheme;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeSeq;
import com.syrus.util.Log;

public class SchemeImportCommand extends ImportExportCommand {
	SchemeTabbedPane pane;
	
	private Map<SchemeCablePort, Set<SchemeCableThread>> portThreadsCount = new HashMap<SchemeCablePort, Set<SchemeCableThread>>();
	private static final Dimension SCHEME_SIZE = new Dimension(6720, 9520);
	
	public SchemeImportCommand(SchemeTabbedPane pane) {
		this.pane = pane;
	}
	
	@Override
	public void execute() {
		super.execute();
//
//		try {
//			ImportUCMConverter converter = new ImportUCMConverter(this.pane.getContext(), this.pane);
//			converter.parseSchemeElements(null);
//		} catch (ApplicationException e) {
//			Log.errorException(e);
//		}
			
		final String fileName = openFileForReading( 
				LangModelScheme.getString("Title.open.scheme_xml"));
		if(fileName == null)
			return;

		try {
			Scheme scheme = loadSchemeXML(fileName);
			
			ApplicationModel aModel = this.pane.getContext().getApplicationModel();
			aModel.setEnabled("Menu.import.commit", true);
			aModel.fireModelChanged();
		} catch (CreateObjectException e) {
			Log.errorMessage(e.getMessage());
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.scheme_import"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
					JOptionPane.ERROR_MESSAGE);
			return;
		} catch (XmlException e) {
			Log.errorMessage(e);
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelScheme.getString("Message.error.xml_format_incorrect"), //$NON-NLS-1$
					LangModelScheme.getString("Message.error"),  //$NON-NLS-1$
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		catch (ApplicationException e) {
			Log.errorMessage(e);
		} catch (IOException e) {
			Log.errorMessage(e);
		}
	}
	
	protected Scheme loadSchemeXML(String fileName) throws XmlException, IOException, ApplicationException {
		Scheme scheme = null;
		File xmlfile = new File(fileName);
		
		SchemesDocument doc = SchemesDocument.Factory.parse(xmlfile);
		
		try {
			if(!validateXml(doc)) {
				throw new XmlException("Invalid XML");
			}
		} catch (Exception e) {
			throw new XmlException(e);
		}
		
		String user_dir = System.getProperty(USER_DIR);
		System.setProperty(USER_DIR,  xmlfile.getParent());
		
		XmlSchemeSeq xmlSchemes = doc.getSchemes();
		XmlScheme[] xmlSchemesArray = xmlSchemes.getSchemeArray();
		for(int i = 0; i < xmlSchemesArray.length; i++) {
			XmlScheme xmlScheme = xmlSchemesArray[i];
			scheme = Scheme.createInstance(this.userId, xmlScheme);
//			scheme.setName(scheme.getName()	+ "(imported " + " from \'" + xmlfile.getName() + "\')");
			
			List<String> errorMessages = new LinkedList<String>();
			for (SchemeCableLink schemeCableLink : scheme.getSchemeCableLinks(false)) {
				if (schemeCableLink.getSchemeCableThreads(false).size() == 0) {
					errorMessages.add(LangModelScheme.getString("Message.warning.cable_no_threads") + schemeCableLink.getName()); //$NON-NLS-1$
				}
				SchemeCablePort sourcePort = schemeCableLink.getSourceAbstractSchemePort();
				if (sourcePort != null) {
					this.portThreadsCount.put(sourcePort, schemeCableLink.getSchemeCableThreads(false));
				} else {
					errorMessages.add(LangModelScheme.getString("Message.warning.cable_no_source") + schemeCableLink.getName()); //$NON-NLS-1$
				}
				SchemeCablePort targetPort = schemeCableLink.getTargetAbstractSchemePort();
				if (targetPort != null) {
					this.portThreadsCount.put(targetPort, schemeCableLink.getSchemeCableThreads(false));
				} else {
					errorMessages.add(LangModelScheme.getString("Message.warning.cable_no_target") + schemeCableLink.getName()); //$NON-NLS-1$
				}
			}
			if (errorMessages.size() > 0) {
				if (!ClientUtils.showConfirmDialog(new JScrollPane(new JList(errorMessages.toArray())),
						LangModelScheme.getString("Message.confirmation.continue_parse"))) { //$NON-NLS-1$
					throw new CreateObjectException("incorrect input data");
				}
			}
			
			if (xmlScheme.getImportType().equals(UCM_IMPORT)) {
				try {
					ImportUCMConverter converter = new ImportUCMConverter(this.pane.getContext(), this.pane);
					converter.parseSchemeCableLinks(scheme);
					converter.parseSchemeElements(scheme);
					
					scheme.setWidth(SCHEME_SIZE.width);
					scheme.setHeight(SCHEME_SIZE.height);
					SchemeActions.putToGraph(scheme, this.pane);
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
			break;
		}
		System.setProperty(USER_DIR,  user_dir);
		return scheme;
	}
}
