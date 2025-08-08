package mission.adapter.out;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import mission.application.domain.model.Route;
import mission.application.port.out.RoutePersistence;

public class RouteCsvDatabase implements RoutePersistence {
    private final List<Route> routes;

    public RouteCsvDatabase(String fileName) {
        CsvLoader<Route> csvLoader = new CsvLoader<Route>();
        routes = csvLoader.readCsv(fileName, Route::parseStatic);
    }

    /**
     * 두 장소(place) 간의 최단 소요시간을 예측한다.
     *
     * @param startPlaceId 시작 장소 ID
     * @param endPlaceId   도착 장소 ID
     * @return 최단 소요시간을 HH:mm 형식의 LocalTime으로 반환
     * @throws IllegalArgumentException 경로가 존재하지 않는 경우
     * @throws IllegalStateException    총 소요시간이 24시간 이상인 경우
     */
    @Override
    public LocalTime predictDuration(int startPlaceId, int endPlaceId) {
        if (startPlaceId == endPlaceId) {
            return LocalTime.of(0, 0);
        }
        Map<Integer, List<int[]>> graph = buildGraph(routes);
        int minutes = findShortestPathMinutes(graph, startPlaceId, endPlaceId);
        return convertMinutesToLocalTime(minutes);
    }

    /**
     * CSV에서 읽은 Route 리스트를 기반으로 그래프를 구성한다.
     * 무방향 그래프를 가정하며, 각 간선의 가중치는 분 단위 소요시간이다.
     *
     * @param routes 경로 데이터 리스트
     * @return 그래프 (node -> 인접 노드와 가중치 배열)
     */
    private Map<Integer, List<int[]>> buildGraph(List<Route> routes) {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (Route r : routes) {
            int a = r.place_id_1();
            int b = r.place_id_2();
            int w = r.time().getHour() * 60 + r.time().getMinute();

            graph.computeIfAbsent(a, k -> new ArrayList<>()).add(new int[]{b, w});
            graph.computeIfAbsent(b, k -> new ArrayList<>()).add(new int[]{a, w});
        }
        return graph;
    }

    /**
     * 다익스트라 알고리즘을 이용해 두 노드 간의 최단 경로 소요시간(분)을 계산한다.
     *
     * @param graph        노드별 인접 노드와 가중치 정보
     * @param startPlaceId 시작 노드 ID
     * @param endPlaceId   도착 노드 ID
     * @return 최단 소요시간 (분 단위)
     * @throws IllegalArgumentException 경로가 존재하지 않는 경우
     */
    private int findShortestPathMinutes(Map<Integer, List<int[]>> graph, int startPlaceId, int endPlaceId) {
        Map<Integer, Integer> dist = new HashMap<>();
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[1]));
        pq.add(new int[]{startPlaceId, 0});
        dist.put(startPlaceId, 0);
        Set<Integer> visited = new HashSet<>();

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0];
            int d = cur[1];

            if (!visited.add(u)) continue;
            if (u == endPlaceId) {
                return d;
            }

            for (int[] edge : graph.getOrDefault(u, Collections.emptyList())) {
                int v = edge[0];
                int nd = d + edge[1];
                if (nd < dist.getOrDefault(v, Integer.MAX_VALUE)) {
                    dist.put(v, nd);
                    pq.add(new int[]{v, nd});
                }
            }
        }

        throw new IllegalArgumentException("경로를 찾을 수 없습니다: start=" + startPlaceId + ", end=" + endPlaceId);
    }

    /**
     * 분 단위 소요시간을 LocalTime(HH:mm)으로 변환한다.
     *
     * @param minutes 분 단위 시간
     * @return HH:mm 형식의 LocalTime
     * @throws IllegalStateException 총 소요시간이 24시간 이상인 경우
     */
    private LocalTime convertMinutesToLocalTime(int minutes) {
        if (minutes >= 24 * 60) {
            throw new IllegalStateException("총 소요시간이 24시간 이상입니다: " + minutes + "분");
        }
        return LocalTime.of(minutes / 60, minutes % 60);
    }
}
