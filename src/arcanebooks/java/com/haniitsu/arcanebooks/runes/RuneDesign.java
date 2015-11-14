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
        
        public void addNode(RuneDesignNode node)
        { nodes.add(node); }
        
        public void addNode(int x, int y)
        { nodes.add(new RuneDesignNode(x > maxX ? maxX : x < 0 ? 0 : x, y > maxY ? maxY : y < 0 ? 0 : y)); }
        
        public void addRandomNode()
        { nodes.add(new RuneDesignNode(random.nextInt(maxX + 1), random.nextInt(maxY + 1))); }
        
        public void clearNodes()
        { nodes.clear(); }
        
        public void removeNode()
        { nodes.remove(nodes.size() - 1); }
        
        public void removeNode(RuneDesignNode node)
        { nodes.remove(node); }
        
        public void removeNode(int x, int y)
        { nodes.remove(new RuneDesignNode(x, y)); }
        
        public void setNodes(List<RuneDesignNode> nodes)
        { this.nodes = new ArrayList<RuneDesignNode>(nodes); }
        
        public void setNodes(RuneDesignNode... nodes)
        { setNodes(Arrays.asList(nodes)); }
    }
    
    public RuneDesign(List<RuneDesignNode> nodes)
    { this.nodes = Collections.unmodifiableList(new ArrayList<RuneDesignNode>(nodes)); }
    
    public RuneDesign(RuneDesignNode... nodes)
    { this(Arrays.asList(nodes)); }
    
    List<RuneDesignNode> nodes;
    
    public List<RuneDesignNode> getNodes()
    { return nodes; }
}