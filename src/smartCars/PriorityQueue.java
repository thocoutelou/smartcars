package smartCars;

import java.util.LinkedList;

public class PriorityQueue {
	
	private LinkedList<AbstractEvent> queue = new LinkedList<AbstractEvent>();
	
	public int qsize()
	{
		return queue.size();
	}
	
	public boolean qisEmpty()
	{
		return queue.isEmpty();
	}
	
	public AbstractEvent qelement()
	{
		if(queue.isEmpty())
		{
			throw new IllegalStateException("La file est vide.");
		}
		AbstractEvent event = queue.peek();
		double min = event.date;
		for(AbstractEvent e : queue)
		{
			if(e.date<min)
			{
				min=e.date;
				event=e;
			}
		}
		return event;
	}
	
	public AbstractEvent aqelement()
	{
		if(queue.isEmpty())
		{
			throw new IllegalStateException("La file est vide.");
		}
		AbstractEvent event = queue.peek();
		double max = event.date;
		for(AbstractEvent e : queue)
		{
			if(max<e.date)
			{
				max=e.date;
				event=e;
			}
		}
		return event;
	}
	
	public AbstractEvent qremove()
	{
		if(queue.isEmpty())
		{
			throw new IllegalStateException("La file est vide.");
		}
		AbstractEvent event = queue.peek();
		double min = event.date;
		for(AbstractEvent e : queue)
		{
			if(e.date<min)
			{
				min=e.date;
				event=e;
			}
		}
		queue.remove(event);
		return event;
	}
	
	public AbstractEvent aqremove()
	{
		if(queue.isEmpty())
		{
			throw new IllegalStateException("La file est vide.");
		}
		AbstractEvent event = queue.peek();
		double max = event.date;
		for(AbstractEvent e : queue)
		{
			if(max<e.date)
			{
				max=e.date;
				event=e;
			}
		}
		queue.remove(event);
		return event;
	}
	
	public boolean qremove(AbstractEvent event)
	{
		return queue.remove(event);
	}
	
	public void qadd(AbstractEvent event)
	{
		queue.add(event);
	}
	
	public void qaddAll(PriorityQueue pq)
	{
		LinkedList<AbstractEvent> q = pq.getQueueCopy();
		queue.addAll(q);
	}
	
	public PriorityQueue getCopy()
	{
		PriorityQueue pq = new PriorityQueue();
		for(AbstractEvent e : queue)
		{
			pq.qadd(e);
		}
		return pq;
	}
	
	public LinkedList<AbstractEvent> getQueueCopy()
	{
		LinkedList<AbstractEvent> q = new LinkedList<AbstractEvent>();
		for(AbstractEvent e : queue)
		{
			q.add(e);
		}
		return q;
	}
	
	public void qdates(double d)
	{
		for(AbstractEvent e : queue)
		{
			e.date+=d;
		}
	}
	
	public String toString()
	{
		String newline = System.getProperty("line.separator");
		String result = new String();
		PriorityQueue pq = getCopy();
		AbstractEvent e;
		while(!pq.qisEmpty())
		{
			e=pq.qremove();
			result+=e.toString()+" de date "+e.date+"s"+newline;
		}
		return result;
	}
	
}
