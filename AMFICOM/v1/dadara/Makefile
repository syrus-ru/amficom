MODULE_NAME := dadara

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

NATIVE_DIR := ./native/common

include ../tools/Makefile.inc

APPCLASSPATH = $(LIBDIR)/util.jar

JAVAH := javah
JAVAH_FLAGS := \
	-classpath $(CLASSDIR) \
	-force

$(call checkenve,JAVA_HOME)


.PHONY:	clean cleanmach cleanhead arch lib


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
	mkdir -p $@


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

#### Archive ####
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(SRCDIR)/com/syrus/AMFICOM/analysis/*.java \
		$(SRCDIR)/com/syrus/AMFICOM/analysis/dadara/*.java

lib: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)
