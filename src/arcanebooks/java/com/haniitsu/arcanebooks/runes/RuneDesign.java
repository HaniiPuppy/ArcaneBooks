package com.haniitsu.arcanebooks.runes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RuneDesign
{
    public static class RuneDesignNode
    {
        public RuneDesignNode(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
        
        protected final int x, y;
        
        public int getX()
        { return x; }
        
        public int getY()
        { return y; }

        @Override
        public int hashCode()
        {
            int hash = 5;
            hash = 67 * hash + this.x;
            hash = 67 * hash + this.y;
            return hash;
        }

        @Override
        public boolean equals(Object obj)
        {
            if(obj == null)
                return false;
            if(getClass() != obj.getClass())
                return false;
            final RuneDesignNode other = (RuneDesignNode)obj;
            if(this.x != other.x)
                return false;
            if(this.y != other.y)
                return false;
            return true;
        }
    }
    
    // Stops the nodes immediately before and after it from connecting. Does not connect to any nodes.
    public static class BreakingRuneDesignNode extends RuneDesignNode
    {
        public BreakingRuneDesignNode()
        { super(0, 0); }
        
        @Override
        public int hashCode()
        { return 5; }

        @Override
        public boolean equals(Object obj)
        { return obj != null && getClass() == obj.getClass(); }
    }
    
    public static class RuneDesignMaker
    {
        public RuneDesignMaker(int width, int height)
        {
            maxX = width - 1;
            maxY = height - 1;
        }
        
        int maxX, maxY;
        List<RuneDesignNode> nodes = new ArrayList<RuneDesignNode>();
        final Random random = new Random();
        
        public RuneDesign make()
        { return new RuneDesign(nodes); }
        
        public RuneDesignMaker addNode(RuneDesignNode node)
        { nodes.add(node); return this; }
        
        public RuneDesignMaker addNode(int x, int y)
        { nodes.add(new RuneDesignNode(x > maxX ? maxX : x < 0 ? 0 : x, y > maxY ? maxY : y < 0 ? 0 : y)); return this; }
        
        public RuneDesignMaker addRandomNode()
        { nodes.add(new RuneDesignNode(random.nextInt(maxX + 1), random.nextInt(maxY + 1))); return this; }
        
        public RuneDesignMaker addRandomNodes(int numberToAdd)
        {
            for(int i = 0; i < numberToAdd; i++)
                addRandomNode();
            
            return this;
        }
        
        public RuneDesignMaker addRandomNodes(int minNumberToAdd, int maxNumberToAdd)
        {
            if(maxNumberToAdd < minNumberToAdd)
                throw new IllegalArgumentException("addRandomNodes - minNumberToAdd must be less than maxNumbertoAdd");
            
            int numberToAdd = random.nextInt(maxNumberToAdd - minNumberToAdd + 1) + minNumberToAdd;
            addRandomNodes(numberToAdd);
            
            return this;
        }
        
        public RuneDesignMaker clearNodes()
        { nodes.clear(); return this; }
        
        public RuneDesignMaker removeNode()
        { nodes.remove(nodes.size() - 1); return this; }
        
        public RuneDesignMaker removeNode(RuneDesignNode node)
        { nodes.remove(node); return this; }
        
        public RuneDesignMaker removeNode(int x, int y)
        { nodes.remove(new RuneDesignNode(x, y)); return this; }
        
        public RuneDesignMaker setNodes(List<RuneDesignNode> nodes)
        { this.nodes = new ArrayList<RuneDesignNode>(nodes); return this; }
        
        public RuneDesignMaker setNodes(RuneDesignNode... nodes)
        { setNodes(Arrays.asList(nodes)); return this; }
        
        public RuneDesignMaker mirrorUpDown()
        {
            List<RuneDesignNode> flipped = new ArrayList<RuneDesignNode>(nodes);
            double midpoint = ((((double)maxY) + 1) / 2) + 0.5;
            
            for(int i = 0; i < flipped.size(); i++)
            {
                RuneDesignNode current = flipped.get(i);
                flipped.set(i, new RuneDesignNode(current.x, (int)(((current.y - midpoint) * -1) + midpoint)));
            }
            
            nodes.add(new BreakingRuneDesignNode());
            nodes.addAll(flipped);
            return this;
        }
        
        public RuneDesignMaker mirrorLeftRight()
        {
            List<RuneDesignNode> flipped = new ArrayList<RuneDesignNode>(nodes);
            double midpoint = ((((double)maxX) + 1) / 2) + 0.5;
            
            for(int i = 0; i < flipped.size(); i++)
            {
                RuneDesignNode current = flipped.get(i);
                flipped.set(i, new RuneDesignNode((int)(((current.x - midpoint) * -1) + midpoint), current.y));
            }
            
            nodes.add(new BreakingRuneDesignNode());
            nodes.addAll(flipped);
            return this;
        }
    }
    
    public RuneDesign(List<RuneDesignNode> nodes)
    { this.nodes = Collections.unmodifiableList(new ArrayList<RuneDesignNode>(nodes)); }
    
    public RuneDesign(RuneDesignNode... nodes)
    { this(Arrays.asList(nodes)); }
    
    List<RuneDesignNode> nodes;
    
    public List<RuneDesignNode> getNodes()
    { return nodes; }
}