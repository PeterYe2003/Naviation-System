// Written By Peter Ye. I talked with Seth on this Project.
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class NavigationSystem {
    String dataFolder = "US-primary-2020";
    HashMap<Integer, Node> nodeHashMap = new HashMap<>();
    HashMap<Integer, Link> linkHashmap = new HashMap<>();
    PriorityQueue<Node> frontier = new PriorityQueue<>();
    int maxXCoordinate = 0;
    int maxYCoordinate = 0;
    double minDistanceAway = Double.POSITIVE_INFINITY;
    double timeTook;

    NavigationSystem() {
        processNodes();
        processLinks();
        processWaypoints();
    }

    private void processNodes() {
        try {
            DataInputStream inStream = new DataInputStream(new BufferedInputStream(new FileInputStream(dataFolder + "/nodes.bin")));
            int numNodes = inStream.readInt();
            for (int i = 0; i < numNodes; i++) {
                int nodeID = inStream.readInt();
                int x = inStream.readInt();
                int y = inStream.readInt();
                if (x > maxXCoordinate) {
                    maxXCoordinate = x;
                }
                if (y > maxYCoordinate) {
                    maxYCoordinate = y;
                }
                Node node = new Node(nodeID, x, y);
                nodeHashMap.put(nodeID, node);
            }
            inStream.close();
        } catch (IOException e) {
            System.err.println("error on nodes: " + e.getMessage());
        }
    }

    private void processLinks() {
        try {
            DataInputStream linksStream = new DataInputStream(new BufferedInputStream(new FileInputStream(dataFolder + "/links.bin")));
            int numLinks = linksStream.readInt();
            for (int i = 0; i < numLinks; i++) {
                int linkID = linksStream.readInt();
                int startNodeID = linksStream.readInt();
                int endNodeID = linksStream.readInt();
                String name = linksStream.readUTF();
                double length = linksStream.readDouble();
                byte oneway = linksStream.readByte();
                Link link = new Link(linkID, startNodeID, endNodeID, name, length, false);
                if (oneway == 1) {
                    nodeHashMap.get(startNodeID).addLink(link);
                } else if (oneway == 2) {
                    link = new Link(linkID, startNodeID, endNodeID, name, length, true);
                    nodeHashMap.get(startNodeID).addLink(link);
                    nodeHashMap.get(endNodeID).addLink(link);
                }
                link.setWayPointSize(0);
                link.addWayPoint(nodeHashMap.get(startNodeID).x, nodeHashMap.get(startNodeID).y);
                link.addWayPoint(nodeHashMap.get(endNodeID).x, nodeHashMap.get(endNodeID).y);
                linkHashmap.put(linkID, link);
            }
            linksStream.close();
        } catch (IOException e) {
            System.err.println("error on links: " + e.getMessage());
        }
    }

    private void processWaypoints() {
        try {
            DataInputStream waypointsStream = new DataInputStream(new BufferedInputStream(new FileInputStream(dataFolder + "/links-waypoints.bin")));
            int numLinks = waypointsStream.readInt();
            for (int i = 0; i < numLinks; i++) {
                int linkID = waypointsStream.readInt();
                int numWaypoints = waypointsStream.readInt();
                Link wayPointLink = linkHashmap.get(linkID);
                wayPointLink.setWayPointSize(numWaypoints);
                wayPointLink.addWayPoint(nodeHashMap.get(wayPointLink.startNodeID).x, nodeHashMap.get(wayPointLink.startNodeID).y);
                for (int p = 0; p < numWaypoints; p++) {
                    int x = waypointsStream.readInt();
                    int y = waypointsStream.readInt();
                    wayPointLink.addWayPoint(x, y);
                }
                wayPointLink.addWayPoint(nodeHashMap.get(wayPointLink.endNodeID).x, nodeHashMap.get(wayPointLink.endNodeID).y);
            }
            waypointsStream.close();
        } catch (IOException e) {
            System.err.println("error on waypoints: " + e.getMessage());
        }
    }

    public String pathFindingDijkstra(Node startNode, Node endNode) {
        frontier.clear();
        ArrayList<Link> updatedPath = new ArrayList<>();
        for (Node node : nodeHashMap.values()) {
            node.setLength(Double.POSITIVE_INFINITY);
            node.setPrevNode(null);
            node.setPrevLink(null);
        }
        startNode.setLength(0);
        frontier.add(startNode);
        double startTime = System.currentTimeMillis();
        while (frontier.size() > 0 && !frontier.peek().equals(endNode)) {
            Node currentNode = frontier.poll();
            for (Link link : currentNode.connectedLinks) {
                if (link.startNodeID == currentNode.nodeID) {
                    Node adjacentNode = nodeHashMap.get(link.endNodeID);
                    if (currentNode.pathLength + link.length < adjacentNode.pathLength) {
                        adjacentNode.setLength(currentNode.pathLength + link.length);
                        adjacentNode.setPrevLink(link);
                        adjacentNode.setPrevNode(currentNode);
                        frontier.add(adjacentNode);
                    }
                } else if (link.endNodeID == currentNode.nodeID) {
                    Node adjacentNode = nodeHashMap.get(link.startNodeID);
                    if (currentNode.pathLength + link.length < adjacentNode.pathLength) {
                        adjacentNode.setLength(currentNode.pathLength + link.length);
                        adjacentNode.setLength(currentNode.pathLength + link.length);
                        adjacentNode.setPrevLink(link);
                        adjacentNode.setPrevNode(currentNode);
                        frontier.add(adjacentNode);
                    }
                }
            }
        }
        timeTook = System.currentTimeMillis() - startTime;
        return new DecimalFormat("#.##").format(endNode.pathLength) + " miles";
    }

    public double pathFindingDijkstraTime() {
        return timeTook;
    }

    public int giveMaxX() {
        return maxXCoordinate;
    }

    public int giveMaxY() {
        return maxYCoordinate;
    }

    public Node findNearestNode(int x, int y) {
        minDistanceAway = Double.POSITIVE_INFINITY;
        Node minNode = null;

        for (Node node : nodeHashMap.values()) {
            if (Math.pow(node.x - x, 2) + Math.pow(node.y - y, 2) < minDistanceAway) {
                minDistanceAway = Math.pow(node.x - x, 2) + Math.pow(node.y - y, 2);
                minNode = node;
            }
        }
        return minNode;
    }
}