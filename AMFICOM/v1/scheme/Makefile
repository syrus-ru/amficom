#
# $Id: Makefile,v 1.1 2004/11/23 13:17:17 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME = schemecommon_v1

IDLDIR=../idl_v1/idl

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

.corba:	$(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme-vt.idl
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	cleancorba
cleancorba:
	cd $(SRCDIR)/com/syrus/AMFICOM/scheme/corba && $(RM) -r $(shell cat $(SRCDIR)/com/syrus/AMFICOM/scheme/corba/.cvsignore)
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/general/corba
	$(RM) .corba
