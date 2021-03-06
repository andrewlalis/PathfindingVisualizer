package net.agspace.pathfinding;

import net.agspace.model.PathMap;
import net.agspace.model.tiles.GoalTile;
import net.agspace.model.tiles.PathTile;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Andrew Lalis
 */
public class BreadthFirstPathfinding extends Pathfinding {

    //Specialized queue for this algorithm.
    private Queue<PathTile> frontierQueue = new LinkedList<>();

    public BreadthFirstPathfinding(PathMap map, int solvingStrategy) {
        super(map, solvingStrategy);
    }

    @Override
    protected void init(){
        this.frontierQueue.addAll(Arrays.asList(this.startPoints));
    }

    @Override
    protected void step() {
        if (this.frontierQueue.isEmpty()){
            this.finished = true;
            return;
        }
        PathTile currentTile = this.frontierQueue.remove();
        currentTile.setInFrontier(false);
        if (currentTile instanceof GoalTile){
            this.finished = true;
            return;
        }
        for (PathTile tile : this.map.getAdjacentTiles(currentTile)){
            if (!tile.isTraversed()){
                tile.setTraversed(true);
                this.frontierQueue.add(tile);
                tile.setInFrontier(true);
            }
        }
    }

}
