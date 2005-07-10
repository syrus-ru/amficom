#
# $Id: Makefile,v 1.3 2005/07/10 13:41:28 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := mcm_interface
IDL_SOURCES = $(IDLDIR)/mcm.idl

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/mcm/corba
	$(RM) .idl
