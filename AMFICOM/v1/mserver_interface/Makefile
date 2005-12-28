#
# $Id: Makefile,v 1.1.1.1 2005/07/03 16:54:02 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := mserver_interface

IDLDIR = ../idl_v1/idl

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,IDLJ)

.PHONY: idl
idl: .idl

.idl: $(SRCDIR)
	$(IDLJ) $(IDLJFLAGS) \
		-pkgPrefix general com.syrus.AMFICOM \
		-pkgPrefix security com.syrus.AMFICOM \
		-pkgPrefix administration com.syrus.AMFICOM \
		-pkgPrefix configuration com.syrus.AMFICOM \
		-pkgPrefix measurement com.syrus.AMFICOM \
		-pkgPrefix mserver com.syrus.AMFICOM \
		$(IDLDIR)/mserver.idl
	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/mserver/corba
	$(RM) .idl

$(SRCDIR):
$(CLASSDIR):
$(LIBDIR):
	mkdir -p $@
