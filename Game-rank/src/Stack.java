import java.util.Stack;

class MyQueue {
    Stack<Integer> stackIn;
    Stack<Integer> stackOut;


    public MyQueue() {
        stackIn=new Stack<>(); //负责入栈：用来模拟队列的元素进入
        stackOut=new Stack<>();//负责出栈：用来模拟队列的元素弹出


    }

    public void push(int x) {
        stackIn.push(x);
    }

    public int pop() {
        dumpstackIn();
        return stackOut.pop();

    }

    public int peek() {
        dumpstackIn();
        return stackOut.peek();

    }

    public boolean empty() {
        return stackIn.isEmpty()&&stackOut.isEmpty();

    }
    //将入栈的元素输出到出栈中，用于实现元素的弹出
    private void dumpstackIn(){
        if(!stackOut.isEmpty()) return;
        while (!stackIn.isEmpty()){
            stackOut.push(stackIn.pop());
        }
    }
}