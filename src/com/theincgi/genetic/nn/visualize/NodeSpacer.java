package com.theincgi.genetic.nn.visualize;

import java.util.List;
import java.util.Optional;

import com.theincgi.commons.MathUtils;
import com.theincgi.commons.Pair;

public class NodeSpacer {
	
	public List<Node> nodes;
	public List<Link> links;
	float prefDist = 3;
	
	/**
	 * if directional is set then link sources will try to be placed more in -direction and<br>
	 * link destinations more in the +direction
	 * */
	public NodeSpacer(List<Node>nodes, List<Link> links, Optional<Pair<Float, Float>> directional) {
		this.nodes = nodes;
		this.links = links;
	}
	
	public void itterate() {
		for( Link link : links ) {
			float dist = link.a.distTo(link.b);
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
		return Math.abs(d*d*d)/d;
	}
	
	public static record Link (Node a, Node b, float weight) {};
	
	
	public static class Node {
		public float x, y;
		protected float fx, fy;
		boolean moveable() {
			return true;
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
