package org.arastreju.sge.repl;

import java.math.BigDecimal;
import java.util.*;

import org.arastreju.sge.model.*;
import org.arastreju.sge.model.nodes.*;

/**
 * <p>
 *  soon-to-be unit test for arastreju.sge's replication facilities 
 * </p>
 *
 * Created: 27.11.2012
 *
 * @author Timo Buhrmester
 */
public class ReplTest {
	static long randSeed = System.currentTimeMillis();
	
	static List<GraphOp> generateGraphOps(int N) {
		List<GraphOp> res = new LinkedList<GraphOp>();
		
		MyRandom r = new MyRandom(randSeed);

		while(N-- > 0) {
			boolean add = r.nextBoolean();
			if (r.nextBoolean()) { //generate a node op
				ResourceID node = new SimpleResourceID("http://example.org/"+r.nextAlphaString(r.nextInt(32)));
				res.add(new NodeOp(add, node));
			} else {
				ResourceID sub = new SimpleResourceID("http://example.org/"+r.nextAlphaString(r.nextInt(32)));
				ResourceID pred = new SimpleResourceID("http://example.org/"+r.nextAlphaString(r.nextInt(32)));
				SemanticNode obj;
				if (r.nextBoolean()) { //a value object
					Object[] typeValPair = r.nextSemanticValueObject();
					obj = new SNValue((ElementaryDataType)typeValPair[0], typeValPair[1]);
				} else { //a resource object
					obj = new SimpleResourceID("http://example.org/"+r.nextAlphaString(r.nextInt(32)));
				}
				res.add(new RelOp(add, new DetachedStatement(sub,  pred, obj)));
			}
		}
		return res;
	}


	
	public static void main(String[] args) throws InterruptedException {
		MyReplicator rep1 = new MyReplicator("rep1", false);
		MyReplicator rep2 = new MyReplicator("rep2", true);
		rep1.init(null, 12345, "localhost", 12346);
		rep2.init(null, 12346, "localhost", 12345);
		
		List<GraphOp> input = generateGraphOps(4);
		for(GraphOp o : input) {
			if (o instanceof RelOp)
				rep1.queueRelOp(((RelOp)o).isAdded(), ((RelOp)o).getStatement());
			else
				rep1.queueNodeOp(((NodeOp)o).isAdded(), ((NodeOp)o).getNode());
		}
		rep1.dispatch();
		
		int lastres = -1;
		for(int rescnt = rep2.countResults(); rescnt < input.size(); rescnt = rep2.countResults()) {
			if (lastres != rescnt) {
				System.err.println("results so far: " + rescnt);
				lastres = rescnt;
			}
			Thread.sleep(100);
		}
		rep2.dispatch();
		lastres = -1;
		for(int rescnt = rep1.countResults(); rescnt < input.size(); rescnt = rep1.countResults()) {
			if (lastres != rescnt) {
				System.err.println("results so far: " + rescnt);
				lastres = rescnt;
			}
			Thread.sleep(100);
		}
		
		List<GraphOp> result = rep1.getResultList();
		
		for(int i = 0; i < input.size(); i++) {
			if (!input.get(i).equals(result.get(i))) {
				System.err.println("error: " + input.get(i) + " != " + result.get(i));
			}
		}
	}
}


class MyReplicator extends ArasLiveReplicator {
	private String name;
	private boolean bounce;
	private List<GraphOp> incoming = new LinkedList<GraphOp>();
	
	public MyReplicator(String name, boolean bounce) {
		this.name = name;
		this.bounce = bounce;
	}
	
    protected void onRelOp(boolean added, Statement stmt) {
    	System.err.println("onRelOp() in "+name+", "+(bounce?"bouncing":"adding to inQ"));
    	if (bounce)
    		queueRelOp(added, stmt);
    	
		synchronized (this) {
			incoming.add(new RelOp(added, stmt));
		}
    }

    protected void onNodeOp(boolean added, ResourceID node) {
    	System.err.println("onNodeOp() in "+name+", "+(bounce?"bouncing":"adding to inQ"));
    	if (bounce)
    		queueNodeOp(added, node);
    	
		synchronized (this) {
			incoming.add(new NodeOp(added, node));
		}
    }
    
    public synchronized List<GraphOp> getResultList() {
    	return incoming;
    }
    
    public synchronized int countResults() {
   		return incoming.size();
    }
}

class MyRandom extends Random {
	MyRandom(long seed) {
		setSeed(seed);
	}
	
	String nextAlphaString(int len) {
		char[] c = new char[len];
		while(len-- > 0) {
			c[len] = (char)((nextBoolean()?'a':'A') + nextInt(26));
		}
		return new String(c);
	}
	
	String nextURI(int pathLen) {
		return "http://example.org/"+nextAlphaString(pathLen);
	}
	
	Date nextDate() {
		return new Date(nextLong());
	}
	
	Object[] nextSemanticValueObject() {
		ElementaryDataType type = null;
		Object val = null;
		// TERM and PROPER_NAME not supported in SNValue (?) XXX 
		switch(nextInt(8)) {
		case 0:
			type = ElementaryDataType.BOOLEAN;
			val = new Boolean(nextBoolean());
			break;
		case 1:
			type = ElementaryDataType.INTEGER;
			val = new Integer(nextInt());
			break;
		case 2:
			type = ElementaryDataType.DECIMAL;
			val = (new BigDecimal(Double.MAX_VALUE)).multiply(new BigDecimal(nextInt(100)+1));
			break; 
		case 3:
			type = ElementaryDataType.STRING;
			val = nextAlphaString(nextInt(32));
			break;
		case 4:
			type = ElementaryDataType.URI;
			val = nextURI(nextInt(32));
			break;
		case 5:
			type = ElementaryDataType.TIMESTAMP;
		case 6:
			if (type == null)
				type = ElementaryDataType.DATE;
		case 7:
			if (type == null)
				type = ElementaryDataType.TIME_OF_DAY;
			val = nextDate();
			break;
		}
		return new Object[]{type, val};
	}
}
