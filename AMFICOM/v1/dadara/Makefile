MODULE_NAME := dadara

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

NATIVE_DIR := ./native/common

include ../tools/Makefile.inc

APPCLASSPATH = $(LIBDIR)/util.jar

#JAVAC=javac
#JAVACFLAGS=-O \
	-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar \
	-d $(CLASSDIR) \
	-deprecation \
	-encoding $(ENCODING) \
	-extdirs $(JAVA_HOME)/jre/lib/ext \
	-g

JAVAC=jikes
JAVACFLAGS := -O \
	+E \
	+P \
	+Pno-switchcheck \
	+T=2 \
	-bootclasspath $(JAVA_HOME)/jre/lib/rt.jar \
	-d $(CLASSDIR) \
	-deprecation \
	-extdirs $(JAVA_HOME)/jre/lib/ext \
	-g

JAVAH := javah
JAVAH_FLAGS := \
	-classpath $(CLASSDIR) \
	-force

$(call checkenve,JAVA_HOME)


.PHONY:	clean cleanmach cleanhead arch install


$(LIBNAME):	.mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.head:	.mach
	$(JAVAH) $(JAVAH_FLAGS) \
		-d $(NATIVE_DIR) \
		com.syrus.AMFICOM.analysis.CoreAnalysisManager
	$(JAVAH) $(JAVAH_FLAGS) \
		-d $(NATIVE_DIR) \
		com.syrus.AMFICOM.analysis.dadara.ModelFunction
	$(JAVAH) $(JAVAH_FLAGS) \
		-d $(NATIVE_DIR) \
		com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent
	touch $@

.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/analysis/*.java) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/analysis/dadara/*.java)
	touch $@

$(CLASSDIR):
	mkdir $@


clean:	cleanmach cleanhead
	$(RM) $(LIBNAME)

cleanmach:
	$(RM) \
		$(wildcard $(CLASSDIR)/com/syrus/AMFICOM/analysis/*.class) \
		$(wildcard $(CLASSDIR)/com/syrus/AMFICOM/analysis/dadara/*.class) \
	$(RM) .mach

cleanhead:
	$(RM) \
		$(NATIVE_DIR)/com_syrus_AMFICOM_analysis_CoreAnalysisManager.h \
		$(NATIVE_DIR)/com_syrus_AMFICOM_analysis_dadara_ModelFunction.h \
		$(NATIVE_DIR)/com_syrus_AMFICOM_analysis_dadara_ReflectogramEvent.h
	$(RM) .head

#### Install ####
.PHONY:	install
install: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)

#### Archive ####
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(SRCDIR)/com/syrus/AMFICOM/analysis/*.java \
		$(SRCDIR)/com/syrus/AMFICOM/analysis/dadara/*.java

