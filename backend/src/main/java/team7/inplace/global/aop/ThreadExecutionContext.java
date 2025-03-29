package team7.inplace.global.aop;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class ThreadExecutionContext {

    private static final ThreadLocal<ExecutionTree> threadLocal =
        ThreadLocal.withInitial(ExecutionTree::new);

    public static ExecutionTree get() {
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.remove();
    }

    public static void set(ExecutionTree context) {
        threadLocal.set(context);
    }

    @Getter
    public static class ExecutionTree {

        private final ExecutionNode root = new ExecutionNode("root", "root", -1);
        private final Deque<ExecutionNode> stack = new ArrayDeque<>();

        public ExecutionTree() {
            stack.push(root);
        }

        public void enter(String layer, String method) {
            ExecutionNode node = new ExecutionNode(layer, method, System.currentTimeMillis());
            stack.peek().addChild(node);
            node.setParent(stack.peek());
            stack.push(node);
        }

        public void exit() {
            ExecutionNode node = stack.pop();
            node.setEndTime(System.currentTimeMillis());
        }

        public List<ExecutionNode> getTopLevelNodes() {
            return root.getChildren();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ExecutionNode {

        private final String layer;
        private final String method;
        private final long startTime;
        private final List<ExecutionNode> children = new LinkedList<>();
        @Setter
        private long endTime;
        @Setter
        private ExecutionNode parent;

        public void addChild(ExecutionNode node) {
            children.add(node);
        }

        public long getExecutionTime() {
            return endTime - startTime;
        }
    }
}
