/*
 * findNoise.h
 *
 * ќпределение общего уровн€ шума рефлектограммы, использу€
 * модель "уровень шума не зависит от сигнала".
 *
 * Ќа входе - рефлектограмма (ргдЅ).
 * на выходе - уровень шума (дЅ) по уровню 3 сигма
 */

#ifndef _findNoise_h
#define _findNoise_h

double findNoise3s(double *data, int size);

#endif

