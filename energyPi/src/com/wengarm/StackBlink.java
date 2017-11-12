package com.wengarm;


import java.util.LinkedList;

public class StackBlink implements Stack{
    private LinkedList<Integer> stack;

    public StackBlink(){
        stack = new LinkedList<>();
    }

    public void insert(Integer newValue){
        if(stack.size() < 20){
            stack.add(newValue);
        }else{
            stack.removeFirst();
            stack.add(newValue);
        }
    }

    public void printStack(){
        for(Integer i : stack){
            System.out.println(i);
        }
    }

    public LinkedList<Integer> getStack(){
        return stack;
    }
}
