#
# $Id: Makefile,v 1.4 2005/10/09 12:58:12 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := cmserver_interface
IDL_SOURCES = $(IDLDIR)/cmserver.idl

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/cmserver/corba
	$(RM) .idl
