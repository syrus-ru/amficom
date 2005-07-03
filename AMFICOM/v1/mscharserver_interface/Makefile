#
# $Id: Makefile,v 1.1.1.1 2005/07/03 16:56:05 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := mscharserver_interface

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
		-pkgPrefix resource com.syrus.AMFICOM \
		-pkgPrefix map com.syrus.AMFICOM \
		-pkgPrefix scheme com.syrus.AMFICOM \
		-pkgPrefix mapview com.syrus.AMFICOM \
		-pkgPrefix mscharserver com.syrus.AMFICOM \
		$(IDLDIR)/mscharserver.idl
	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/mscharserver/corba
	$(RM) .idl

$(SRCDIR):
$(CLASSDIR):
$(LIBDIR):
	mkdir -p $@
