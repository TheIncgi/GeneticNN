package com.theincgi.genetic.nn.visualize;

import java.util.List;
import java.util.Optional;

import com.theincgi.commons.MathUtils;
import com.theincgi.commons.Pair;

public class NodeSpacer {
	
	public List<Node> nodes;
	public List<Link> links;
	float prefDist = 350;
	
	/**
	 * if directional is set then link sources will try to be placed more in -direction and<br>
	 * link destinations more in the +direction<br>
	 * @param directional (2d vector)
	 * */
	public NodeSpacer(List<Node>nodes, List<Link> links, Optional<Pair<Float, Float>> directional) {
		this.nodes = nodes;
		this.links = links;
	}
	
	public void itterate() {
		for( Link link : links ) {
			float dist = link.a.distTo(link.b);
			dist = Math.max(dist, 1f);
			float force = springForce( dist );
			float dx = link.b.x - link.a.x;
			float dy = link.b.y - link.a.y;
			dx /= dist;
			dy /= dist;
			dx *= force;
			dy *= force;
			link.b.applyForce( dx,  dy);
			link.a.applyForce(-dx, -dy);
		}
		
		for( Node node : nodes ) 
			node.move();
	}
	
	//result, negative = pull, positive = push
	public float springForce( float dist ) {
		float d = prefDist - dist;
		return d;//Math.abs(d*d*d)/d;
	}
	
	public static class Link {
		Node a,b;
		float weight;
		public Link(Node a, Node b) {this(a,b,1);}
		public Link(Node a, Node b, float weight) {
			this.a=a; this.b=b;this.weight=weight;
		}
		public Node getA() { return a; }
		public Node getB() { return b; }
		public float getWeight() { return weight; }
	};
	
	
	public static class Node {
		public float x, y;
		protected float fx, fy;
		private boolean moveable = true;
		
		public Node setMoveable(boolean canMove) { moveable = canMove; return this; }
		public Node anchor() { return setMoveable( false ); }
		boolean moveable() {
			return moveable;
		}
		public void move() {
			if(!moveable()) return;
			x += fx;
			y += fy;
			resetForce();
		}
		public void resetForce() {
			fx = fy = 0f;
		}
		public void applyForce( float fx, float fy ) {
			this.fx += fx;
			this.fy += fy;
		}
		public float distTo( Node other ) {
			return MathUtils.distance(x, y, other.x, other.y);
		}
	}
}
