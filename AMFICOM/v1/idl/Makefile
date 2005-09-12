#
# $Id: Makefile,v 1.29 2005/09/12 20:52:45 arseniy Exp $
#
# vim:ft=make:
#

MODULE_NAME = idl
ARCHFILE := $(MODULE_NAME).tar.gz


include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

.PHONY: idlclean
idlclean:
	$(MAKE) -C ../event $@
	$(MAKE) -C ../general $@
	$(MAKE) -C ../resource $@
	$(MAKE) -C ../admin $@
	$(MAKE) -C ../config $@
	$(MAKE) -C ../measurement $@
	$(MAKE) -C ../map $@
	$(MAKE) -C ../scheme $@
	$(MAKE) -C ../mapview $@
	$(MAKE) -C ../cmserver_interface $@
	$(MAKE) -C ../mscharserver_interface $@
	$(MAKE) -C ../mcm_interface $@
	$(MAKE) -C ../mserver_interface $@
	$(MAKE) -C ../leserver_interface $@
	$(RM) .idl

.PHONY: dist
dist: distclean
	$(RM) ../$(MODULE_NAME)-$(MODULE_VERSION).tar*
	cd .. && tar cf $(MODULE_NAME)-$(MODULE_VERSION).tar $(MODULE_NAME)
	bzip2 -9 ../$(MODULE_NAME)-$(MODULE_VERSION).tar

.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		idl/*.idl \
		Makefile

