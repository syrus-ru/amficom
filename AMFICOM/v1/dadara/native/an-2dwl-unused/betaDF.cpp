/***********************************************************/
/*                   ����-�������������                    */
/***********************************************************/
#include <math.h>
#include <assert.h>

#include "betaDF.h"
#include "logGamma.h"

BetaDF::BetaDF(double u, double w):
   a(u), b(w), logBeta(logGamma(a) + logGamma(b) - logGamma(a + b))
{
   assert(a > 0 && b > 0);
}

double
BetaDF::fraction(double a, double b, double x)
//
// ��. Abramowitz & Stegun,
//      Handbook of Mathematical Functions, 1964 [26.5.8]
//  �.���������, �.������
//  ���������� �� ����������� �������� (�: ���, 1979)
//
//   �������� ����-������� ����������� � ������� ���������� � ������ �����
//
//         i_beta(a,b,x) = x^{a}*(1-x)^{b}*fraction / a * beta(a,b),
//  ���
//
//                    1    d1   d2   d3   d4
//        fraction = ---  ---- ---- ---- ---- ....
//                    1+   1+   1+   1+   1+
//
//  ����������� �����: A(n) / B(n)
//
//  ���
//        A(n) = (s(n) * A(n-1) + r(n) * A(n-2)) * factor
//        B(n) = (s(n) * B(n-1) + r(n) * B(n-2)) * factor
//  �
//        A(-1) = 1, B(-1) = 0, A(0) = s(0), B(0) = 1.
//
//  ����� s(0) = 0 � ��� n >= 1 s(n) = 1,
//  � r(1) = 1 � ��� i >= 2
//
//        r(i) =  m(b-m)x / (a+i-1)(a+i)       ����� i = 2m,
//        r(i) = -(a+m)(a+b+m)x / (a+i-1)(a+i) ����� i = 2m+1,
//
//  factor - ����������� ���������, ����������� �������� ������������.
//
//  ����, A(0) = 0 , B(0) = 1,
//        r(1) = -(a+b).x / (a+1)
//        A(1) = A(0) + r(1).A(-1) = r(1) = 1
//        B(1) = B(0) + r(1).B(-1) = 1
//
{
   double old_bta = 0, factor = 1;
   double A0 = 0, A1 = 1, B0 = 1, B1 = 1;
   double bta = 1, am = a, ai = a;
   double iter = 0, r;

   do {
      /* �������� � i = 1, iter = 0, ������ i �������   */
      ai += 1;
      r = -am * (am + b) * x / ((ai - 1) * ai);
      /* �������� A � B � ��� ����           */
      A0 = (A1 + r * A0) * factor;  /* i ������� */
      B0 = (B1 + r * B0) * factor;
      /* ����� �������� � i = 2, iter = 1, ������ i ����� */
      am += 1;
      iter += 1;
      ai += 1;
      r = iter * (b - iter) * x * factor / ((ai - 1) * ai);
      A1 = A0 + r * A1;     /* i �����, A0 � B0 ��� ����������� */
      B1 = B0 + r * B1;
      old_bta = bta;
      factor = 1 / B1;
      bta = A1 * factor;
   } while (fabs(old_bta) != fabs(bta));
   return bta * exp(a * log(x) + b * log(1 - x) - logBeta) / a;
}/*incBeta_fraction*/

double
BetaDF::value(double x)
//
// ��������� Beta(x|a,b):
//      ����������� ����, ��� ��������� ��������,
//      ������������� ����-������������� � ����������� 'a' � 'b',
//      ������ ��� ����� 'x'.
//
{
   if (x <= 0)
      return 0;             /* �� ������!     */
   else if (x >= 1)
      return 1;             /* �� ������!     */
   if (x < (a + 1) / (a + b + 2))
      return fraction(a, b, x);
   else
      return 1 - fraction(b, a, 1 - x);
}/*value*/

double
BetaDF::inv(double p)
//
// ���� ����� �������� 'x', ��� �������� Beta(x|a,b) = p,
//      �.�. ����� 'p' ����������� ����, ��� ��������� ��������,
//      ������������� ����-������������� � ����������� 'a' � 'b',
//      ������ ��� ����� 'x'.
//
{
   double fx, l = 0, r = 1, x = 0.5;

   assert(p >= 0 && p <= 1);
   if (p == 0 || p == 1) return p;

   do {
      fx = value(x);
      if (fx > p) r = x; else
      if (fx < p) l = x; else
         return x;
      x = (l + r)* 0.5;
   } while ((l!=x) && (r!=x));
   return x;
}/*inv*/




