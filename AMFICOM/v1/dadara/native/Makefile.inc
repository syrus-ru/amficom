OBJ_EXT = obj
SO_EXT = dll
SO_PREFIX = 

COMMON_DIR = ./common
ANALYSE_DIR = ./analyse
AN2_DIR = ./An2
FIT_DIR = ./fit
GAUSS_DIR = ./gauss

OUT = $(SO_PREFIX)dadara.$(SO_EXT)

LD = link
LDFLAGS = /nologo /dll
LD_OUTPUT_FLAG = /out:

CXX = cl.exe
CXXFLAGS = /nologo /O2 /GX /MT /FD
CXX_NOLINK_FLAG = /c
CXX_OUTPUT_FLAG = /Fo
CPPFLAGS = /D "WIN32" /D "_WINDOWS" /D "DADARA_EXPORTS" /D "_USRDLL" /D "_MBCS"
