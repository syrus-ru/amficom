#
# $Id: Makefile,v 1.5 2005/04/13 20:40:58 arseniy Exp $
#
# vim:ft=make:
#

MODULE_NAME = corbaportable_v1

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2
IDLDIR = idl
IDLDIR_EXTRA = ../idl_v1/idl
IDLJFLAGS = -i $(IDLDIR) -i $(IDLDIR_EXTRA)

include ../tools/Makefile.inc

.PHONY: idl
idl: .idl

.idl:
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR_EXTRA)/alarm.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/alarm.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/client.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/common.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/reflect.idl
	touch $@

.PHONY: idl-clean
idl-clean:
	find $(SRCDIR) -name '*DefaultFactory\.java' -exec $(RM) \{\} \;
	find $(SRCDIR) -name '*Helper\.java' -exec $(RM) \{\} \;
	find $(SRCDIR) -name '*Holder\.java' -exec $(RM) \{\} \;
	find $(SRCDIR) -name '*Operations\.java' -exec $(RM) \{\} \;
	find $(SRCDIR) -name '*POA\.java' -exec $(RM) \{\} \;
	find $(SRCDIR) -name '*POATie\.java' -exec $(RM) \{\} \;
	find $(SRCDIR) -name '_*Stub\.java' -exec $(RM) \{\} \;
	find $(SRCDIR) -name 'Client\.java' -exec $(RM) \{\} \;
	find $(SRCDIR) -name 'DatabaseAccessException\.java' -exec $(RM) \{\} \;
	find $(SRCDIR) -name 'Identifier\.java' -exec $(RM) \{\} \;
	find $(SRCDIR) -name 'Message\.java' -exec $(RM) \{\} \;
	find $(SRCDIR) -name 'MessageDeliveryFailedException\.java' -exec $(RM) \{\} \;
	find $(SRCDIR) -name 'PortableStackTraceElement\.java' -exec $(RM) \{\} \;
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/CORBA
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/event
	$(RM) .idl
