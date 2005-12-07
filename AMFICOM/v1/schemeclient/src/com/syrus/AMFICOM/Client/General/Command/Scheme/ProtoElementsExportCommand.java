/*-
 * $Id: ProtoElementsExportCommand.java,v 1.7 2005/12/07 17:17:36 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.AMFICOM.scheme.xml.SchemeProtoGroupsDocument;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeProtoGroup;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeProtoGroupSeq;
import com.syrus.util.Log;
import com.syrus.util.transport.xml.XmlConversionException;

public class ProtoElementsExportCommand extends ImportExportCommand {
	SchemeTabbedPane pane;
	
	public ProtoElementsExportCommand(SchemeTabbedPane pane) {
		this.pane = pane;
	}
	
	@Override
	public void execute() {
		super.execute();
		
		final String fileName = openFileForWriting(
				LangModelScheme.getString("Title.save.protos_xml")); //$NON-NLS-1$
		if(fileName == null)
			return;

		try {
			StorableObjectCondition condition1 = new LinkedIdsCondition(Identifier.VOID_IDENTIFIER, ObjectEntities.SCHEMEPROTOGROUP_CODE); 
			Collection<SchemeProtoGroup> groups = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
			Set<XmlSchemeProtoGroup> xmlProtoGroups = new HashSet<XmlSchemeProtoGroup>();
			for (SchemeProtoGroup protoGroup : groups) {
				XmlSchemeProtoGroup xmlProto = XmlSchemeProtoGroup.Factory.newInstance();
				protoGroup.getXmlTransferable(xmlProto, AMFICOM_IMPORT, false);
				xmlProtoGroups.add(xmlProto);
			}
			final File protoFile = new File(fileName);
			saveProtoGroupsXML(protoFile, xmlProtoGroups);
		} catch (final ApplicationException ae) {
			Log.errorMessage(ae);
		} catch (final XmlConversionException xce) {
			Log.errorMessage(xce);
		}
	}
	
	private void saveProtoGroupsXML(File f, Set<XmlSchemeProtoGroup> xmlProtoGroups) {
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		java.util.Map prefixes = new HashMap();
		prefixes.put("http://syrus.com/AMFICOM/scheme/xml", "schemeProtoGroups");
		xmlOptions.setSaveSuggestedPrefixes(prefixes);
		
		SchemeProtoGroupsDocument doc = SchemeProtoGroupsDocument.Factory.newInstance(xmlOptions);
		
		XmlSchemeProtoGroupSeq xmlProtoGroupSeq = doc.addNewSchemeProtoGroups();
		xmlProtoGroupSeq.setSchemeProtoGroupArray(xmlProtoGroups.toArray(new XmlSchemeProtoGroup[xmlProtoGroups.size()]));
		
		try {
			// Writing the XML Instance to a file.
			doc.save(f, xmlOptions);
			Log.debugMessage("XML Instance Document saved at : " + f.getPath(), Level.FINER);
		} catch(IOException e) {
			e.printStackTrace();
		}
		Log.debugMessage("Check if XML valid...", Level.FINER);
		boolean isXmlValid = validateXml(doc);
		if(isXmlValid) {
			Log.debugMessage("Done successfully", Level.WARNING);
		} else {
			Log.debugMessage("Done with errors (see logs/error for more)", Level.WARNING);
		}
	}
}
