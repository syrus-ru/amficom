/*-
 * $Id: Cable.java,v 1.1 2005/08/29 13:04:21 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.syrus.amficom.general.xml.UID;
import com.syrus.amficom.scheme.xml.CableChannelingItem;
import com.syrus.amficom.scheme.xml.CableChannelingItems;
import com.syrus.amficom.scheme.xml.SchemeCableLink;
import com.syrus.amficom.scheme.xml.SchemeCableThread;
import com.syrus.amficom.scheme.xml.SchemeCableThreads;
import com.syrus.impexp.unicablemap.TextWriter;
import com.syrus.impexp.unicablemap.UniCableMapType;

public class Cable {
	public static final String TYPE = UniCableMapType.UCM_CABLE_LINEAR;
	
	private String id;
	private String name;
	private Integer typeId;
	private Integer codenameId;
	private String startId; // порт
	private String endId; // порт
	private Integer layoutId;
	private Collection<ChannelingItem> channelingItems = new LinkedList<ChannelingItem>(); 
	private Collection<CableThread> threads = new LinkedList<CableThread>();

	public Cable(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setCodenameId(Integer codenameId) {
		this.codenameId = codenameId;
	}

	public void setEndPortId(String endId) {
		this.endId = endId;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public void setStartPortId(String startId) {
		this.startId = startId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	
	public void setLayoutId(Integer layoutId) {
		this.layoutId = layoutId;
	}
	
	public Collection<CableThread> getThreads() {
		return this.threads;
	}

	public void setThreads(Collection<CableThread> threads) {
		this.threads = threads;
	}
	
	public void addCableThread(CableThread thread) {
		this.threads.add(thread);
	}
	
	public void addChannelingItem(ChannelingItem item) {
		item.setNumber(this.channelingItems.size());
		this.channelingItems.add(item);
	}
	
	public void write(TextWriter writer) {
		writer.startObject(TYPE);
		writer.put("id", String.valueOf(this.id));
		writer.put("name", this.name);
		writer.put("typeid", String.valueOf(this.typeId));
		writer.put("codenameid", String.valueOf(this.codenameId));
		writer.put("startid", String.valueOf(this.startId)); // муфта или кабельный ввод
		writer.put("endid", String.valueOf(this.endId)); // муфта или кабельный ввод
		writer.put("layoutid", String.valueOf(this.layoutId));
		writer.endObject();
	}

	int counter = 0;
	public SchemeCableLink toXMLObject() {
		SchemeCableLink xmlCL = SchemeCableLink.Factory.newInstance();
		UID uid = xmlCL.addNewUid();
		uid.setStringValue(String.valueOf(this.id));
		xmlCL.setName(this.name);
		xmlCL.setDescription("");
		if (this.typeId == null) {
			System.err.println("Link typeIs is null");
			this.typeId = 0;
		}
		xmlCL.setLinktype(this.typeId.toString());
		
		if (this.startId != null) {
			UID suid = xmlCL.addNewSourceportuid();
			suid.setStringValue(this.startId);
		} else {
			System.out.println("Cable (" + (++this.counter) + "): startId is null for " + this.name);
		}

		if (this.endId != null) {
			UID euid = xmlCL.addNewTargetportuid();
			euid.setStringValue(this.endId);
		} else {
			System.out.println("Cable (" + (++this.counter) + "): endId is null for " + this.name);
		}
		
		double length = 0;
		for (ChannelingItem item : this.channelingItems) {
			length += item.getLength();
		}
		xmlCL.setPhysicalLength(length);
		xmlCL.setOpticalLength(length);
		
		if (!this.threads.isEmpty()) {
			SchemeCableThreads xmlSchemeThreads = xmlCL.addNewSchemecablethreads();

			Collection<SchemeCableThread> scts = new ArrayList<SchemeCableThread>(this.threads.size());
			for (Object thread : this.threads) {
				scts.add(((CableThread)thread).toXMLObject());
			}
			xmlSchemeThreads.setSchemecablethreadArray(scts.toArray(new SchemeCableThread[scts.size()]));	
		}
		
		if (!this.channelingItems.isEmpty()) {
			CableChannelingItems xmlChannelingItems = xmlCL.addNewCablechannelingitems();

			Collection<CableChannelingItem> cis = new ArrayList<CableChannelingItem>(this.channelingItems.size());
			for (Object channelingItem : this.channelingItems) {
				cis.add(((ChannelingItem)channelingItem).toXMLObject());
			}
			xmlChannelingItems.setCablechannelingitemArray(cis.toArray(new CableChannelingItem[cis.size()]));	
		}
		return xmlCL;
	}
}
