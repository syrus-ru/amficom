OBJ_EXT = o
SO_EXT = so
SO_PREFIX = 

COMMON_DIR = ./Common
ANALYSE_DIR = ./analyse
AN2_DIR = ./an2
FIT_DIR = ./fit
JNI_DIR = ./JNI
BREAKL_DIR = ./BreakL
CLASS_DIR = ../classes

OUT = $(SO_PREFIX)dadara.$(SO_EXT)

LD = ld
LDFLAGS = /nologo /dll
LD_OUTPUT_FLAG = /out:

CXX = g++
CXXFLAGS = -O2
CXX_NOLINK_FLAG = -c 
CXX_OUTPUT_FLAG = -o 
CPPFLAGS = -DDADARA_EXPORTS

JAVAH = javah
