package com.example.brandon.defachelleclouetbrandon_2916832_ass3_journeytracker;

/**
 * Created by Brandon on 23/04/2016.
 */
import android.location.Location;
public class CircularQueue
{
    int size;
    Node head;
    Node tail;
    float average;

    public class Node
    {
        Location key;
        Node next;

        public Node(Location val)
        {
            key = val;
            next=null;
        }
    }

    public CircularQueue()
    {
        size=0;
        head= null;
        tail = null;
        average = 0;

    }

    public void add(Location val)
    {
        size++;
        average += val.getSpeed();      //add to average
        if(size<30)    //if not full
        {
            if(head==null)      //if empty
            {
                head = new Node(val);
                tail = head;
                head.next = tail;
                tail.next = head;
            }
            else
            {
                Node node = new Node(val);
                tail.next = node;
                tail = node;
                tail.next = head;
            }
        }
        else            //queue is full
        {
            average-=head.key.getSpeed();   //delete from average

            Node node = new Node(val);
            head = head.next;
            tail.next =node;
            tail = node;
            tail.next = head;

            average += head.key.getSpeed();
        }
    }
    public float getSpeed()
    {
        return tail.key.getSpeed()* 3.6f;
    }
    public float getAverage()
    {
        if(size>100)
        {
            return (average/100) * 3.6f;
        }
        return (average/size) * 3.6f;
    }
    public float getTime()
    {
        return size;
    }
    public void reset()
    {
        average=0;
        size=0;
        tail = null;
        head=null;
    }

}
