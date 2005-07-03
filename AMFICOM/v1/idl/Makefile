#
# $Id: Makefile,v 1.26 2005/07/03 19:16:34 bass Exp $
#
# vim:ft=make:
#

MODULE_NAME = idl_v1

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

.PHONY: idl
idl: .idl

.idl:
	$(MAKE) -C ../event_v1 $@
	$(MAKE) -C ../general_v1 $@
	$(MAKE) -C ../resource_v1 $@
	$(MAKE) -C ../admin_v1 $@
	$(MAKE) -C ../config_v1 $@
	$(MAKE) -C ../measurement_v1 $@
	$(MAKE) -C ../map_v1 $@
	$(MAKE) -C ../scheme_v1 $@
	$(MAKE) -C ../mapview_v1 $@
	$(MAKE) -C ../cmserver_interface $@
	$(MAKE) -C ../mscharserver_interface $@
	$(MAKE) -C ../mcm_interface $@
	$(MAKE) -C ../mserver_interface $@
	$(MAKE) -C ../leserver_interface $@
	touch $@

.PHONY: idlclean
idlclean:
	$(MAKE) -C ../event_v1 $@
	$(MAKE) -C ../general_v1 $@
	$(MAKE) -C ../resource_v1 $@
	$(MAKE) -C ../admin_v1 $@
	$(MAKE) -C ../config_v1 $@
	$(MAKE) -C ../measurement_v1 $@
	$(MAKE) -C ../map_v1 $@
	$(MAKE) -C ../scheme_v1 $@
	$(MAKE) -C ../mapview_v1 $@
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
