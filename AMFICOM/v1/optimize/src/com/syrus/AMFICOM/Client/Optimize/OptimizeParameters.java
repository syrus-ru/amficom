// класс, содержащий в себе все параметры оптимизации
package com.syrus.AMFICOM.Client.Optimize;

import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

public class OptimizeParameters
{
  public int np = 8;// количество параметров (обновл€ть руками)
  double len_margin        = 1;    // коэффициент запаса по волокнам
  double mutation_rate     = 0.03; // по умолчанию уровень мутации равен 3%
  double mutation_degree   = 2;    // количество промутировавших генов в среднем в той особи , котора€ подверглась мутации
  double rtu_delete_prob   = 0.2;  // веро€тность при мутации выкинуть пингалку (от 0 до 1)
  double rtu_create_prob   = 0.2;  // веро€тность при мутации создать пингалку (от 0 до 1)
  double nodes_splice_prob = 1;    // веро€тность при мутации объединить разъединЄнные рЄбра (от 0 до 1)
  double nodes_cut_prob    = 0.05; // веро€тность при мутации разъединить соединЄнные рЄбра (от 0 до 1)
  double survivor_rate     = 0.2;  // часть от попул€ции котора€ выживет, чтобы дать потомство(от 0 до 1)

	//---------------------------------------------------------------------------------------------------------------
	public OptimizeParameters(){}
  //---------------------------------------------------------------------------------------------------------------
  public void setOptimizationParam(int row , double val)
  { if(row == 0) { len_margin = val;}
    if(row == 1) { mutation_rate = val;}
    if(row == 2) { mutation_degree = val;}
    if(row == 3) { rtu_delete_prob = val;}
    if(row == 4) { rtu_create_prob = val;}
    if(row == 5) { nodes_splice_prob = val;}
    if(row == 6) { nodes_cut_prob = val;}
    if(row == 7) { survivor_rate = val;}
  }
	//---------------------------------------------------------------------------------------------------------------
  public double getOptimizationParam(int row)
  { double res = -1;
    if(row == 0) { res = len_margin;}
    if(row == 1) { res = mutation_rate;}
    if(row == 2) { res = mutation_degree;}
    if(row == 3) { res = rtu_delete_prob;}
    if(row == 4) { res = rtu_create_prob;}
    if(row == 5) { res = nodes_splice_prob;}
    if(row == 6) { res = nodes_cut_prob;}
    if(row == 7) { res = survivor_rate;}
  return res;
  }
	//---------------------------------------------------------------------------------------------------------------
}