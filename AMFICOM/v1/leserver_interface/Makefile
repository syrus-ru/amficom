#
# $Id: Makefile,v 1.3 2005/07/10 13:41:29 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := leserver_interface
IDL_SOURCES = $(IDLDIR)/leserver.idl

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/leserver/corba
	$(RM) .idl
