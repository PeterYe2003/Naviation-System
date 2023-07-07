// Peter Ye
import java.util.ArrayList;

public class Node implements Comparable{
    int nodeID;
    int x;
    int y;
    double pathLength;
    // connectedLinks is an Arraylist of links that connected to the Node.
    ArrayList<Link> connectedLinks;

    // linkToPreviousNode is the link that connect the node to the previous node (Sort of like a linked list
    // where the start is the start node).
    Link linkToPreviousNode;

    // previousNode is the Node that the link is connecting to.
    Node previousNode;


    Node(int nodeID, int x, int y) {
        this.nodeID = nodeID;
        this.x = x;
        this.y = y;
        pathLength = Double.POSITIVE_INFINITY;
        connectedLinks = new ArrayList<>();
    }

    void setLength(double length){
        pathLength = length;
    }

    void addLink(Link link){
        connectedLinks.add(link);
    }


    @Override
    public int compareTo(Object value) {
        if (pathLength > ((Node) value).pathLength){
            return 1;
        }
        else if (pathLength < ((Node) value).pathLength){
            return -1;
        }
        else{
            return 0;
        }
    }

    public void setPrevLink(Link link) {
        linkToPreviousNode = link;
    }

    public void setPrevNode(Node node) {
        previousNode = node;
    }

}