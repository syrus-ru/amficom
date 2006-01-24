/*-
 * $Id: Cable.java,v 1.15 2006/01/24 07:44:18 stas Exp $
 *
 * Copyright њ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.xml.XmlCableChannelingItem;
import com.syrus.AMFICOM.scheme.xml.XmlCableChannelingItemSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableLink;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableThread;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableThreadSeq;
import com.syrus.impexp.unicablemap.map.Link;

public class Cable {
//	public static final String TYPE = UniCableMapType.UCM_CABLE_LINEAR;
	
	private String id;
	private String name;
	private Integer typeId;
	private Integer codenameId;
	private String startPortId; // порт
	private String endPortId; // порт
	private Integer layoutId;
	private List<ChannelingItem> channelingItems = new LinkedList<ChannelingItem>();
	private ChannelingItem first;
	private ChannelingItem last;
	private Collection<CableThread> threads = new LinkedList<CableThread>();
	
	private String startSiteId;
	private String endSiteId;

	private Map<String, ChannelingItem> channelingItemsMap = new HashMap<String, ChannelingItem>();
	
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
		this.endPortId = endId;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public void setStartPortId(String startId) {
		this.startPortId = startId;
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
		this.channelingItemsMap.put(item.getTunnelId(), item);
	}
	
	public void setFirstChannelingItem(ChannelingItem item) {
		this.first = item;
		this.channelingItemsMap.put(item.getTunnelId(), item);
	}

	public void setLastChannelingItem(ChannelingItem item) {
		this.last = item;
		this.channelingItemsMap.put(item.getTunnelId(), item);
	}

	int counter = 0;
	public XmlSchemeCableLink toXMLObject(XmlIdentifier parentId) {
		XmlSchemeCableLink xmlCL = XmlSchemeCableLink.Factory.newInstance();
		XmlIdentifier uid = xmlCL.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		
		if (this.name.length() > 32) {
			System.out.println("cable name (" + this.name + ") length greater then 32 symbols. coping to description and cutting...");
			xmlCL.setDescription(this.name);
			this.name = this.name.substring(0, 31);
		}
		
		xmlCL.setName(this.name);
//		xmlCL.setDescription("");
		
		if (this.typeId == null) {
			System.err.println("Link typeIs is null");
			this.typeId = Integer.valueOf(0);
		}
		
		XmlIdentifier linkTypeId = xmlCL.addNewCableLinkTypeId();
		linkTypeId.setStringValue(String.valueOf(this.typeId));
		xmlCL.setCableLinkTypeId(linkTypeId);
		
		if (this.startPortId != null) {
			XmlIdentifier suid = xmlCL.addNewSourceSchemeCablePortId();
			suid.setStringValue(this.startPortId);
		} else {
			System.out.println("Cable (" + (++this.counter) + "): startId is null for " + this.name);
		}

		if (this.endPortId != null) {
			XmlIdentifier euid = xmlCL.addNewTargetSchemeCablePortId();
			euid.setStringValue(this.endPortId);
		} else {
			System.out.println("Cable (" + (++this.counter) + "): endId is null for " + this.name);
		}
		
		double length = 0;
		for (ChannelingItem item : this.channelingItems) {
			length += item.getLength();
		}
		
		xmlCL.setParentSchemeId(parentId);
		
		xmlCL.setPhysicalLength(length);
		xmlCL.setOpticalLength(length);
	
//		if (false) {
		if (!this.threads.isEmpty()) {
			XmlSchemeCableThreadSeq xmlSchemeThreads = xmlCL.addNewSchemeCableThreads();

			Collection<XmlSchemeCableThread> scts = new ArrayList<XmlSchemeCableThread>(this.threads.size());
			for (Object thread : this.threads) {
				scts.add(((CableThread)thread).toXMLObject());
			}
			xmlSchemeThreads.setSchemeCableThreadArray(scts.toArray(new XmlSchemeCableThread[scts.size()]));	
		}

//		if (false) {
		if (!this.channelingItems.isEmpty()) {
			XmlCableChannelingItemSeq xmlChannelingItems = xmlCL.addNewCableChannelingItems();

			Collection<XmlCableChannelingItem> cis = new ArrayList<XmlCableChannelingItem>(this.channelingItems.size());
			for (ChannelingItem channelingItem : this.channelingItems) {
				cis.add(channelingItem.toXMLObject());
			}
			xmlChannelingItems.setCableChannelingItemArray(cis.toArray(new XmlCableChannelingItem[cis.size()]));	
		}
		return xmlCL;
	}

	public void sortChannelingItems(List<Link> links) {

		List<Link> tempLinks = new LinkedList<Link>(links);
		LinkedList<ChannelingItem> tempChannelingItems = new LinkedList<ChannelingItem>(this.channelingItems);
		LinkedList<ChannelingItem> newChannelingItemsFromStart = new LinkedList<ChannelingItem>();
		LinkedList<ChannelingItem> newChannelingItemsFromEnd = new LinkedList<ChannelingItem>();

		String bufStartSiteId = this.startSiteId;
		String tempEndSiteId = "";
		String bufEndSiteId = this.endSiteId;
		String tempStartSiteId = "";

		if(this.first != null) {
			newChannelingItemsFromStart.add(this.first);
			bufStartSiteId = this.first.getEndSiteId();
		}
		if(this.last != null) {
			newChannelingItemsFromEnd.add(this.last);
			bufEndSiteId = this.last.getStartSiteId();
		}

		while(tempLinks.size() != 0) {
			boolean foundFromStart = false;
			boolean foundFromEnd = false;
			for(Link link : tempLinks) {
				if(link.getStartNodeId().equals(bufStartSiteId)) {
					tempEndSiteId = link.getEndNodeId();
					foundFromStart = true;
				}
				if(link.getEndNodeId().equals(bufStartSiteId)) {
					tempEndSiteId = link.getStartNodeId();
					foundFromStart = true;
				}
				if(link.getStartNodeId().equals(bufEndSiteId)) {
					tempStartSiteId = link.getEndNodeId();
					foundFromEnd = true;
				}
				if(link.getEndNodeId().equals(bufEndSiteId)) {
					tempStartSiteId = link.getStartNodeId();
					foundFromEnd = true;
				}
				if(foundFromStart) {
					ChannelingItem channelingItem = this.channelingItemsMap.get(link.getId());
					if(channelingItem == null) {
						System.out.println("¬нимание! не задано место кабел€ дл€ кабел€ '"
								+ this.name + "' (" + this.id
								+ "), тоннель '"
								+ link.getName() + "' (" + link.getId()
								+ ")!");
						channelingItem = new ChannelingItem("cci" + this.id + "tunnel" + link.getId());
						channelingItem.setEndSiteId(tempEndSiteId);
						channelingItem.setStartSiteId(bufStartSiteId);
						channelingItem.setParentId(this.id);
						channelingItem.setTunnelId(link.getId());
						newChannelingItemsFromStart.add(channelingItem);
					}
					else {
						tempChannelingItems.remove(channelingItem);
						channelingItem.setEndSiteId(tempEndSiteId);
						channelingItem.setStartSiteId(bufStartSiteId);
						newChannelingItemsFromStart.add(channelingItem);
					}
					tempLinks.remove(link);
					bufStartSiteId = tempEndSiteId;
					break;
				}
				else if(foundFromEnd) {
					ChannelingItem channelingItem = this.channelingItemsMap.get(link.getId());
					if(channelingItem == null) {
						System.out.println("¬нимание! не задано место кабел€ дл€ кабел€ '"
								+ this.name + "' (" + this.id
								+ "), тоннель '"
								+ link.getName() + "' (" + link.getId()
								+ ")!");
						channelingItem = new ChannelingItem("cci" + this.id + "tunnel" + link.getId());
						channelingItem.setEndSiteId(bufEndSiteId);
						channelingItem.setStartSiteId(tempStartSiteId);
						channelingItem.setParentId(this.id);
						channelingItem.setTunnelId(link.getId());
						newChannelingItemsFromEnd.add(channelingItem);
					}
					else {
						tempChannelingItems.remove(channelingItem);
						channelingItem.setEndSiteId(bufEndSiteId);
						channelingItem.setStartSiteId(tempStartSiteId);
						newChannelingItemsFromEnd.add(channelingItem);
					}
					tempLinks.remove(link);
					bufEndSiteId = tempStartSiteId;
					break;
				}
			}
			if(!foundFromStart && !foundFromEnd) {
				System.out.println("ќшибка! отсутствует св€зность прокладки кабел€ '"
						+ this.name + "' (" + this.id
						+ "), начало " + this.startSiteId
						+ ", конец " + this.endSiteId
						+ "\n		св€зь отсутствует на участке между " + bufStartSiteId
						+ " и " + bufEndSiteId);
				System.out.print("		кабель проходит по тоннел€м ");
				for(Link tempLink : links) {
					System.out.print(" " + tempLink.getId());
				}
				System.out.println("");
				System.out.print("		места кабел€ заданы в тоннел€х ");
				for(ChannelingItem channelingItem : this.channelingItems) {
					System.out.print(" " + channelingItem.getTunnelId());
				}
				System.out.println("");
				break;
			}
		}
		if(tempChannelingItems.size() != 0) {
			System.out.print("¬нимание! задано место кабел€ '"
					+ this.name + "' (" + this.id
					+ ") без информации о прохождении кабел€ по тоннелю! ");
			for(ChannelingItem channelingItem : tempChannelingItems) {
				System.out.print(" " + channelingItem.getTunnelId());
			}
			System.out.println("");
		}
		this.channelingItems.clear();
		this.channelingItems.addAll(newChannelingItemsFromStart);
		Collections.reverse(newChannelingItemsFromEnd);
		this.channelingItems.addAll(newChannelingItemsFromEnd);
		int i = 0;
		for(ChannelingItem channelingItem : this.channelingItems) {
			channelingItem.setNumber(i++);
		}
	}

	public String getEndSiteId() {
		return this.endSiteId;
	}

	public void setEndSiteId(String endSiteId) {
		assert endSiteId != null;
		this.endSiteId = endSiteId;
	}

	public String getStartSiteId() {
		return this.startSiteId;
	}

	public void setStartSiteId(String startSiteId) {
		assert startSiteId != null;
		this.startSiteId = startSiteId;
	}

}
