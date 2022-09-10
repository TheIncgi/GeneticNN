package com.theincgi.genetic.nn;

import static java.lang.Math.exp;

import java.util.ArrayList;
import java.util.List;

import com.theincgi.genetic.OptionGene;

public class ActivationFunctions {

	public static final ActivationFunction 
	    sigmoid       = x -> { return (float) (1f / ( 1f + exp( -x ) )); },
	    relu          = x -> { return x <= 0 ? 0 : x; },
	    leakyRelu     = x -> { return x <= 0 ? x * .2f : x; },
	    gelu	      = x -> { return (float) ( x / ( 1 + exp( -x )));  },
	    //identity  = x -> { return x; }, //lol
	    tanh      	  = x -> { return (float) Math.tanh(x); },
	    doubleSigmoid = x -> { return (float) ( 2 / (1 + exp( Math.pow(-x, 3) )) -1 ); };
	    
	    
	
	public static <T extends List<ActivationFunction>> T loadActivationFunctions(T list) {
		list.add(sigmoid);
		list.add(relu);
		list.add(leakyRelu);
		list.add(gelu);
		list.add(tanh);
		list.add(doubleSigmoid);
		return list;
	}
	
	@FunctionalInterface
	public static interface ActivationFunction {
		public float apply( float x );
	}
}
