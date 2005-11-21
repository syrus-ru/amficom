#ifdef _WIN32
#include <windows.h> // for APIENTRY #define
#include "EventP.h"
#include "prf.h"

BOOL APIENTRY DllMain( HANDLE hModule, 
                   DWORD  ul_reason_for_call, 
                   LPVOID lpReserved
				 )
{
    if (ul_reason_for_call == DLL_PROCESS_ATTACH)
	{
//		MF_init();
	}
    if (ul_reason_for_call == DLL_PROCESS_DETACH)
	{
		prf_print();
	}
	return TRUE;
}
#endif
