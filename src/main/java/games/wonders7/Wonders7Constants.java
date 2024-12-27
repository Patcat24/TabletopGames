package games.wonders7;

import utilities.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Wonders7Constants {
    // ENUM OF MATERIALS
    public enum Resource { //Another enum for costs
        Wood,
        Stone,
        Clay,
        Ore,
        BasicWild(Wood, Clay, Stone, Ore),
        Wood_Clay(Wood, Clay),
        Stone_Clay(Stone, Clay),
        Ore_Clay(Ore, Clay),
        Wood_Stone(Wood, Stone),
        Wood_Ore(Wood, Ore),
        Stone_Ore(Stone, Ore),
        Glass,
        Papyrus,
        Textile,
        RareWild(Glass, Papyrus, Textile),
        Cog,
        Compass,
        Tablet,
        Shield,
        Victory,
        Coin;

        public final Resource[] resources;

        Resource() {
            resources = new Resource[]{this};
        }

        Resource(Resource... resources) {
            this.resources = resources;
        }

        public boolean isComposite() {
            return resources.length > 1;
        }

        public boolean isBasic() {
            return this == BasicWild || this == Wood || this == Stone || this == Clay || this == Ore;
        }

        public boolean isRare() {
            return this == Glass || this == Papyrus || this == Textile || this == RareWild;
        }

        public boolean isTradeable() {
            return (isBasic() && this != BasicWild) || (isRare() && this != RareWild);
        }

        public boolean includes(Resource resource) {
            for (Resource r : resources) {
                if (r == resource) {
                    return true;
                }
            }
            return false;
        }
    }

    public record TradeSource(Resource resource, int cost, int fromPlayer) {
    }

    @SafeVarargs
    public static List<Map<Resource, Long>> createHashList(Map<Wonders7Constants.Resource, Long>... hashmaps) {
        List<Map<Wonders7Constants.Resource, Long>> list = new ArrayList<>();
        Collections.addAll(list, hashmaps);
        return list;
    }

    public static Map<Resource, Long> createCardHash(Resource... resources) {
        // This will have to create the resource hashmaps for each card and return them
        return Arrays.stream(resources).collect(Collectors.groupingBy(e -> e, Collectors.counting()));
    }

    @SafeVarargs
    public static Map<Resource, Long> createCardHash(Pair<Resource, Integer>... resources) {
        // This will have to create the resource hashmaps for each card and return them
        Map<Resource, Long> map = new HashMap<>();
        for (Pair<Resource, Integer> resource : resources) {
            map.put(resource.a, Long.valueOf(resource.b));
        }
        return map;
    }
}
