#
# $Id: Makefile,v 1.76 2005/10/09 16:13:05 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := general

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = $(IDLDIR)/general.idl

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,XMLBEANSCLASSPATH)
$(call checkenve,SCOMP)
$(call checkenve,SCOMPFLAGS)


LIBNAME_GENERALXML=generalxml.jar

DEPENDENCIES = \
util \
bugs

EMPTY =
SPACE = $(EMPTY) $(EMPTY)
APPCLASSPATH = $(subst $(SPACE),:,$(foreach DEPENDENCY,$(DEPENDENCIES),$(LIBDIR)/$(DEPENDENCY).jar))


ifeq ($(TROVE4J_HOME),)
EXTCLASSPATH := $(EXTLIBDIR)/trove.jar
else
EXTCLASSPATH := $(TROVE4J_HOME)/lib/trove.jar
endif

ifeq ($(XALANJ_HOME),)
EXTCLASSPATH := $(EXTCLASSPATH):$(EXTLIBDIR)/xalan.jar
else
EXTCLASSPATH := $(EXTCLASSPATH):$(XALANJ_HOME)/bin/xalan.jar
endif

EXTCLASSPATH := $(EXTCLASSPATH):$(XMLBEANSCLASSPATH)

XMLCLASSPATH := $(LIBDIR)/$(LIBNAME_GENERALXML)

SOURCES := $(wildcard $(SRCDIR)/com/syrus/AMFICOM/general/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/general/logic/*.java) \
	$(wildcard $(SRCDIR)/com/syrus/AMFICOM/security/*.java)


$(LIBNAME): .mach
	$(CP) \
		$(SRCDIR)/com/syrus/AMFICOM/general/general.properties \
		$(SRCDIR)/com/syrus/AMFICOM/general/general_ru.properties \
		$(CLASSDIR)/com/syrus/AMFICOM/general/
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach: .corba .xml $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@

.PHONY: idlclean
idlclean:
	cd $(SRCDIR)/com/syrus/AMFICOM/general/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/general/corba/.cvsignore)
	cd $(SRCDIR)/com/syrus/AMFICOM/security/corba && \
		$(RM) -r $(shell cat <$(SRCDIR)/com/syrus/AMFICOM/security/corba/.cvsignore)
	$(RM) .idl

.PHONY: corba
corba: .corba

.corba: $(CLASSDIR) .idl
	$(JAVAC) \
		$(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/general/corba/*.java \
		$(SRCDIR)/com/syrus/AMFICOM/security/corba/*.java
	touch $@
#### XML ####
.PHONY: xml
xml: .xml

.xml: $(LIBNAME_GENERALXML)
	touch $@

.PHONY: xmlinstall
xmlinstall: .xml $(LIBDIR)
	install -m 0644 $(LIBNAME_GENERALXML) $(LIBDIR)

$(LIBNAME_GENERALXML):
	$(SCOMP) $(SCOMPFLAGS) -out $@ \
		$(XMLDIR)/general.xsd \
		$(XMLDIR)/Identifier.xsd \
		$(XMLDIR)/StorableObject.xsd \
		$(XMLDIR)/StorableObjectType.xsd \
		$(XMLDIR)/Characteristic.xsd \
		$(XMLDIR)/CharacteristicType.xsd \
		$(XMLDIR)/general.xsdconfig

.PHONY: clean
clean: cleanmach cleancorba cleanxml
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES)))
	$(RM) .mach

.PHONY: cleancorba
cleancorba: idlclean
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/general/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/security/corba
	$(RM) .corba

.PHONY: cleanxml xmlclean
cleanxml xmlclean:
	$(RM) $(LIBNAME_GENERALXML)
	if [ -f $(XMLSRCDIR)/com/syrus/AMFICOM/general/xml/.cvsignore ]; \
	then \
		cd $(XMLSRCDIR)/com/syrus/AMFICOM/general/xml && $(RM) -r `cat .cvsignore`; \
	else \
		$(RM) -r $(XMLSRCDIR)/com/syrus/AMFICOM/general/xml; \
	fi
	$(RM) -r $(XMLCLASSDIR)/com/syrus/AMFICOM/general/xml
	$(RM) .xml

#### Install ####
.PHONY: install
install: xmlinstall $(LIBNAME) $(LIBDIR)
	install -m 0644 $(LIBNAME) $(LIBDIR)


#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDL_SOURCES) \
		$(SOURCES) \
		$(SRCDIR)/com/syrus/AMFICOM/general/general.properties \
		$(SRCDIR)/com/syrus/AMFICOM/general/general_ru.properties \
		$(XMLDIR)/*.xsd $(XMLDIR)/*.xsdconfig

.javadocs: .idl .xml $(JAVADOCDIR)
	$(JAVADOC) \
		$(JAVADOCFLAGS) \
		-link "http://$(JAVADOCHOST)/apidocs/trove/" \
		-link "http://$(JAVADOCHOST)/apidocs/xalan-j/" \
		-link "http://$(JAVADOCHOST)/apidocs/xmlbeans/" \
		-sourcepath $(SRCDIR) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(EXTCLASSPATH) \
		-group "$(MODULE_NAME)" "com.syrus.AMFICOM.general:com.syrus.AMFICOM.general.logic" \
		-group "$(MODULE_NAME)-IDL" "com.syrus.AMFICOM.general.corba*" \
		-group "$(MODULE_NAME)-XML" "com.syrus.AMFICOM.general.xml*" \
		-group "security" "com.syrus.AMFICOM.security" \
		-group "security-IDL" "com.syrus.AMFICOM.security.corba*" \
		-subpackages "com.syrus.AMFICOM.general:com.syrus.AMFICOM.security" \
		2>&1 | grep -vE '(XMLInputStream|com\.syrus\.AMFICOM\.[a-z]+\.corba\..*@serial)'
	touch $@
