#
# $Id: Makefile,v 1.4 2005/11/11 12:46:15 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := manager

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

XMLDIR = schemas
include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)
$(call checkenve,SCOMP)
$(call checkenve,SCOMPFLAGS)

LIBNAME_MANAGER_SCHEMAS = $(MODULE_NAME)_schemas.jar

DEPENDENCIES = \
util \
general \
administration \
configuration \
measurement \
event \
filter \
csbridge \
commonclient \
filterclient \
resource

EXTCLASSPATH := $(TROVE4JCLASSPATH):$(EXTLIBDIR)/jgraph.jar

$(LIBNAME): .mach
	mkdir -p $(CLASSDIR)/com/syrus/AMFICOM/manager/resources/icons
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/manager/resources/icons/*.[pg][ni][gf] \
		$(CLASSDIR)/com/syrus/AMFICOM/manager/resources/icons
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/manager/resources/*.[pgp][nir][gfo]* \
		$(CLASSDIR)/com/syrus/AMFICOM/manager/resources/
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(EXTCLASSPATH) \
		$(shell find . -name *java)
	touch $@

.PHONY: xml
xml: .xml

.xml: $(LIBNAME_MANAGER_SCHEMAS)
	touch $@

.PHONY: xmlinstall
xmlinstall: .xml $(LIBDIR)
	install -m 0644 $(LIBNAME_MANAGER_SCHEMAS) $(LIBDIR)

$(LIBNAME_MANAGER_SCHEMAS): 
	$(SCOMP) $(SCOMPFLAGS) -out $@ \
		$(XMLDIR)/$(MODULE_NAME).xsd \
		$(XMLDIR)/$(MODULE_NAME).xsdconfig
	$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/extensions/impl
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/extensions/impl
	$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/extensions/*.java
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/extensions/*.class

.PHONY: xmlclean
xmlclean:
	$(RM) $(LIBNAME_SCHEMEXML)
	if [ -f $(XMLSRCDIR)/com/syrus/AMFICOM/extensions/.cvsignore ]; \
	then \
		cd $(XMLSRCDIR)/com/syrus/AMFICOM/extensions && $(RM) -r `cat .cvsignore`; \
	else \
		$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/extensions; \
	fi
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/extensions
	$(RM) .xml

.PHONY:	clean
clean:	cleanmach xmlclean
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) -r $(CLASSDIR)/com
	$(RM) .mach

#### Install ####
.PHONY:	install
install: $(LIBNAME) $(LIBDIR) xmlinstall
	$(CP) $^
