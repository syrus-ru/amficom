/***********************************************************/
/*                   Beta distribution                     */
/***********************************************************/
#ifndef __BETA_H__                     /* To prevent redefinition           */

#define ENTRY   extern
#define LOCAL   static

class BetaDF {
   public:
      BetaDF(double u, double w);
      double value(double x);     // Функция распределения Beta(x|a,b) 
      double inv(double p);       // Обратная функция: Beta(x|a,b)=p
   private:
      double a,b, logBeta;
      double fraction(double a, double b, double x);
};

#define __BETA_H__                     /* Prevents redefinition             */
#endif                                 /* Ends #ifndef __BETA_H__           */


