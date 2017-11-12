package com.wengarm;


import java.util.LinkedList;

public class StackName implements Stack{
    private LinkedList<String> stack;

    public StackName(){
        stack = new LinkedList<>();
    }

    public void insert(String newValue){
        if(stack.size() < 20){
            stack.add(newValue);
        }else{
            stack.removeFirst();
            stack.add(newValue);
        }
    }

    public void printStack(){
        for(String s : stack){
            System.out.println(s);
        }
    }

    public LinkedList<String> getStack(){
        return stack;
    }
}
