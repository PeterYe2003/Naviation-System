// Peter Ye
// Each Link holds its waypoints with x[] and y[].
public class Link {
    int linkID;
    int startNodeID;
    int endNodeID;
    String name;
    double length;
    boolean isTwoWay;
    int x[];
    int y[];
    int counter;
    int totalPoints;

    Link(int linkID, int startNodeID, int endNodeID, String name, double length,boolean isDoubleSided) {
        this.linkID = linkID;
        this.startNodeID = startNodeID;
        this.endNodeID = endNodeID;
        this.name = name;
        this.length = length;
        this.isTwoWay = isDoubleSided;

    }

    void setWayPointSize(int numWayPoints){
        // Want to account for adding in the first and the last node.
        totalPoints = numWayPoints+2;
        x = new int[totalPoints];
        y = new int[totalPoints];
        counter = 0;
    }
    // This adds a wayPoint to the link.
    void addWayPoint(int x, int y){
        this.x[counter] = x;
        this.y[counter] = y;
        counter++;
    }
}