/****************************************************/
/*         ������������� ���������                  */
/****************************************************/

#include <assert.h>
#include <math.h>

#include "betaDF.h"

ENTRY double
studentDF(double n, double x)
/*
 * ����������� ����������� ����, ��� ��������� ��������,
 * ������������� ������������� ��������� (T-�������������)
 * c n ��������� �������, �� ����������� (������ ��� �����) x.
 * ������������ ��������� ����� t- � ����-�������������.
 */
{
   assert(n > 0);

   if (x == 0.0)
      return 0.5;
   BetaDF b=BetaDF(0.5 * n, 0.5);
   double z = 0.5 * b.value(n / (n + x * x));
   return (x > 0.0) ? 1.0 - z : z;
}/*studentDF*/

ENTRY double
inv_studentDF(double n, double p)
/*
 * �� ������ ����������� p ����������� �������� q,
 * ��� �������� studentDF(n,q) ������ p.
 */
{
   if (p == 0.5) return 0.0;
   double z = 1.0 - 2.0 * p;
   BetaDF b=BetaDF(0.5, 0.5*n);
   //z = b.quantile(fabs(z));
   z = b.inv(fabs(z));
   double q = sqrt(n*z/(1.0-z));
   return (p < 0.5) ? -q : q;
}

