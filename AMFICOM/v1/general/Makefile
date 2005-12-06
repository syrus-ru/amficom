#
# $Id: Makefile,v 1.82 2005/12/06 12:58:49 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := general

LIBNAME := $(MODULE_NAME).jar
LIBNAMEXML := $(MODULE_NAME)xml.jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/$(MODULE_NAME).idl

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,TROVE4JCLASSPATH)
$(call checkenve,XALANJCLASSPATH)
$(call checkenve,XMLBEANSCLASSPATH)
$(call checkenve,SCOMP)
$(call checkenve,SCOMPFLAGS)


DEPENDENCIES = \
util \
bugs

EXTCLASSPATH := $(TROVE4JCLASSPATH):$(XMLBEANSCLASSPATH):$(XALANJCLASSPATH)
XMLCLASSPATH := $(LIBDIR)/$(LIBNAMEXML)

SOURCES := $(shell find $(SRCDIR) -name '*.java')
XSD_SOURCES := $(wildcard $(XMLDIR)/*.xsd)
XSD_CONFIG := $(XMLDIR)/$(MODULE_NAME).xsdconfig


$(LIBNAME): .mach
	$(CP) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/*.properties) \
		$(CLASSDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/
	$(JAR) cf $@ \
		-C $(CLASSDIR) com

.mach: $(CLASSDIR) xml .corba
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl xml
	$(JAVAC) \
		$(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/corba/*.java \
		$(SRCDIR)/com/syrus/AMFICOM/security/corba/*.java
	touch $@

.PHONY: xml
xml: $(LIBNAMEXML)

$(LIBNAMEXML): $(XSD_SOURCES) $(XSD_CONFIG)
	$(RM) -r $(XMLCLASSDIR)/*
	$(SCOMP) $(SCOMPFLAGS) \
		-cp $(XMLBEANSCLASSPATH) \
		-out $@ \
		$(XSD_SOURCES) \
		$(XSD_CONFIG)

.PHONY: xmlclean
xmlclean:
	$(RM) $(LIBNAMEXML)
	if [ -f $(XMLSRCDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/xml/.cvsignore ]; \
	then \
		cd $(XMLSRCDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/xml && $(RM) -r `cat .cvsignore`; \
	else \
		$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/xml; \
	fi
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/xml

.PHONY: clean
clean: cleanmach cleancorba xmlclean
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES))) \
		.mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/security/corba
	$(RM) .corba

.PHONY: idlclean
idlclean:
	cd $(SRCDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/corba/.cvsignore | grep .java)
	cd $(SRCDIR)/com/syrus/AMFICOM/security/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/security/corba/.cvsignore | grep .java)
	$(RM) .idl


.PHONY: xmlinstall
xmlinstall: $(LIBDIR) $(LIBDIR)/$(LIBNAMEXML)

.PHONY: install
install: $(LIBDIR) $(LIBDIR)/$(LIBNAME) xmlinstall

$(LIBDIR)/$(LIBNAMEXML): $(LIBNAMEXML)
	install -m 0644 -p $(LIBNAMEXML) $(LIBDIR)

$(LIBDIR)/$(LIBNAME): $(LIBNAME)
	install -m 0644 -p $(LIBNAME) $(LIBDIR)

.PHONY: xmluninstall
xmluninstall:
	$(RM) $(LIBDIR)/$(LIBNAMEXML)

.PHONY: uninstall
uninstall: xmluninstall
	$(RM) $(LIBDIR)/$(LIBNAME)

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDL_SOURCES) \
		$(SOURCES) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/$(MODULE_NAME)/*.properties) \
		$(wildcard $(XMLDIR)/*.xsd) \
		$(XMLDIR)/$(MODULE_NAME).xsdconfig

.javadocs: .idl xml $(JAVADOCDIR)
	$(JAVADOC) \
		$(JAVADOCFLAGS) \
		-link "http://$(JAVADOCHOST)/apidocs/trove/" \
		-link "http://$(JAVADOCHOST)/apidocs/xalan-j/" \
		-link "http://$(JAVADOCHOST)/apidocs/xmlbeans/" \
		-sourcepath $(SRCDIR) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(EXTCLASSPATH) \
		-group "$(MODULE_NAME)" "com.syrus.AMFICOM.$(MODULE_NAME):com.syrus.AMFICOM.$(MODULE_NAME).logic" \
		-group "$(MODULE_NAME)-IDL" "com.syrus.AMFICOM.$(MODULE_NAME).corba*" \
		-group "$(MODULE_NAME)-XML" "com.syrus.AMFICOM.$(MODULE_NAME).xml*" \
		-group "security" "com.syrus.AMFICOM.security" \
		-group "security-IDL" "com.syrus.AMFICOM.security.corba*" \
		-subpackages "com.syrus.AMFICOM.$(MODULE_NAME):com.syrus.AMFICOM.security" \
		2>&1 | grep -vE '(XMLInputStream|com\.syrus\.AMFICOM\.[a-z]+\.corba\..*@serial)'
	touch $@
