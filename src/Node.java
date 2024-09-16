public class Node {
    public char value;
    public Node left;
    public Node right;

    public Node(char val, Node leftNode, Node rightNode) {
        this.value = val;
        this.left = leftNode;
        this.right = rightNode;
    }
}