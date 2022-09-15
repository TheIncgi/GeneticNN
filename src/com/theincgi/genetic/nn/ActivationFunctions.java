package com.theincgi.genetic.nn;

import static java.lang.Math.exp;

import java.util.ArrayList;
import java.util.List;

import com.theincgi.genetic.OptionGene;

public class ActivationFunctions {

	public static final NamedActivationFunction 
	    sigmoid       = new NamedActivationFunction("sigmoid",       x -> { return (float) (1f / ( 1f + exp( -x ) )); }),
	    relu          = new NamedActivationFunction("relu",          x -> { return x <= 0 ? 0 : x; }),
	    leakyRelu     = new NamedActivationFunction("leaky relu",    x -> { return x <= 0 ? x * .2f : x; }),
	    gelu	      = new NamedActivationFunction("gelu",          x -> { return (float) ( x / ( 1 + exp( -x )));  }),
	    //identity  = x -> { return x; }, //lol
	    tanh      	  = new NamedActivationFunction("tanh",          x -> { return (float) Math.tanh(x); }),
	    doubleSigmoid = new NamedActivationFunction("double sigmoid",x -> { return (float) ( 2 / (1 + exp( Math.pow(-x, 3) )) -1 ); });
	    
	    
	
	public static <T extends List<NamedActivationFunction>> T loadActivationFunctions(T list) {
		list.add(sigmoid);
		list.add(relu);
		list.add(leakyRelu);
		list.add(gelu);
		list.add(tanh);
		list.add(doubleSigmoid);
		return list;
	}
	
	public static class NamedActivationFunction implements ActivationFunction { 
		private String name;
		private ActivationFunction func;

		public NamedActivationFunction( String name, ActivationFunction func ) {
			this.name = name;
			this.func = func;
		} 
		
		@Override
		public float apply(float x) {
			return func.apply(x);
		}
		
		@Override
		public String toString() {
			return "ActivationFunction<%s>".formatted(name);
		}
		
	}
	
	@FunctionalInterface
	public static interface ActivationFunction {
		public float apply( float x );
	}
}
