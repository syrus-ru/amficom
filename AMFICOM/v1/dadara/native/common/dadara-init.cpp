#include <windows.h> // for APIENTRY #define
#include "EventP.h"

BOOL APIENTRY DllMain( HANDLE hModule, 
                   DWORD  ul_reason_for_call, 
                   LPVOID lpReserved
				 )
{
    if (ul_reason_for_call = DLL_PROCESS_ATTACH)
		MF_init();
	return TRUE;
}
