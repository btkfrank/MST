import java.util.*;

public class MST {

    private static Tuple makeTuple(int u, int v) {
        int x = u, y = v;
        if (x > y) {
            int t = x;
            x = y;
            y = t;
        }
        return new Tuple(x, y);
    }

    /**
     * This function takes in a graph, with weights classified as light, medium or heavy
     * It should output a list of all edges in the graph that must be a part of
     * any minimum spanning tree
     * <p>
     * You should use the Tuple class provided to output edges.  For instance, to output
     * the edge (u,v), you would add "new Tuple(u,v)" to your list
     *
     * @param g
     * @return
     */
    public static Set<Tuple> mustExist(WeightedUndirectedGraph<Weight> g) {
        Set<Tuple> result = new TreeSet<>();
        for (int u = 0; u < g.size(); ++u) {
            List<Integer> neighbors = g.neighbors(u);
            if (neighbors.size() == 1) {
                result.add(makeTuple(u, neighbors.get(0)));
            } else {
                int numberLight = 0, numberMedium = 0;
                for (int v : neighbors) {
                    Weight weight = g.weight(u, v);
                    if (weight == Weight.LIGHT) {
                        ++numberLight;
                    } else if (weight == Weight.MEDIUM) {
                        ++numberMedium;
                    }
                }
                if (numberLight == 1) {
                    for (int v : neighbors) {
                        if (v < u) {
                            continue;
                        }
                        Weight weight = g.weight(u, v);
                        if (weight == Weight.LIGHT) {
                            result.add(makeTuple(u, v));

                            break;
                        }
                    }
                } else if (numberLight == 0 && numberMedium == 1) {
                    for (int v : neighbors) {
                        if (v < u) {
                            continue;
                        }
                        Weight weight = g.weight(u, v);
                        if (weight == Weight.MEDIUM) {
                            result.add(makeTuple(u, v));
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * This function takes in a graph, with weights classified as light, medium or heavy
     * It should output a list of all edges in the graph that must NOT be a part of
     * any minimum spanning tree
     *
     * @param g
     * @return the set of edges (unordered tuples of vertices) that must not
     * exist in any MST
     */
    public static Set<Tuple> mustNotExist(WeightedUndirectedGraph<Weight> g) {
        Set<Tuple> result = new HashSet<>();
        for (int u = 0; u < g.size(); ++u) {
            List<Integer> neighbors = g.neighbors(u);
            if (neighbors.size() > 1) {
                int numberLight = 0, numberMedium = 0;
                for (int v : neighbors) {
                    Weight weight = g.weight(u, v);
                    if (weight == Weight.LIGHT) {
                        ++numberLight;
                    } else if (weight == Weight.MEDIUM) {
                        ++numberMedium;
                    }
                }
                if (numberLight > 0) {
                    for (int v : neighbors) {
                        if (v < u) {
                            continue;
                        }
                        Weight weight = g.weight(u, v);
                        if (weight != Weight.LIGHT) {
                            result.add(makeTuple(u, v));
                        }
                    }
                } else if (numberMedium > 0) {
                    for (int v : neighbors) {
                        if (v < u) {
                            continue;
                        }
                        Weight weight = g.weight(u, v);
                        if (weight == Weight.HEAVY) {
                            result.add(makeTuple(u, v));
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Edges in this MST are classified as light, medium or heavy
     * (their actual edge weights are not known)
     */
    public enum Weight {
        LIGHT, MEDIUM, HEAVY
    }


}