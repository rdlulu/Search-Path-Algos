import java.util.*;

public class MyClass {
    public static void main(String args[]) {
        int[][] m = new int[][]{
            {0,0,0,0,0,0,0,0,0,0},
            {0,1,0,0,0,0,3,0,0,0},
            {0,0,3,3,3,3,3,3,0,0},
            {0,0,3,0,0,0,0,3,0,0},
            {0,0,3,0,3,0,0,3,0,0},
            {0,3,3,0,3,2,0,3,0,0},
            {0,3,0,0,3,3,3,3,0,0},
            {0,0,0,0,0,0,0,3,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0}
        };
        int w = m[0].length;
        int s = 0;
        int e = 0;
        for(int i = 0; i < m.length; i++) {
            for(int j = 0; j < w; j++) {
                if(m[i][j] == 1) s = i * w + j;
                if(m[i][j] == 2) e = i * w + j;
            }
        }
        MyClass c = new MyClass();
        c.shortPath(m, s, e, "A*");
        for(int i = 0; i < m.length; i++) {
            for(int j = 0; j < m[0].length; j++) {
                System.out.print(m[i][j] + ", ");
            }
            System.out.println("");
        }
    }
    
    public void shortPath(int[][] matrix, int start, int end, String type) {
        int w = matrix[0].length;
        int[][] dir = new int[][]{{0,1},{0,-1},{1,0},{-1,0}};
        Comp c = new Comp(start, end, w, type);
        PriorityQueue<Integer> pq = new PriorityQueue<>(c);
        Map<Integer, Integer> map = new HashMap<>();
        map.put(start, 0);
        pq.offer(start);
        Set<Integer> set = new HashSet<>();
        int[] pre = new int[matrix.length * matrix[0].length];
        Arrays.fill(pre, -1);
        int prev = -1;
        while(!pq.isEmpty()) {
            int next = pq.poll();
            if(set.contains(next)) continue;
            if(next == end) {
                //System.out.println("got here");
                labelpath(matrix, pre, end);
                return;
            }
            for(int i = 0; i < 4; i++) {
                int neighbor = (next / w + dir[i][0]) * w + next % w + dir[i][1];
                if(neighbor < 0 || neighbor / w >= matrix.length || next % w + dir[i][1] < 0 || next % w + dir[i][1] >= w || set.contains(neighbor)) {
                    continue;
                }
                //System.out.println(neighbor);
                if(matrix[neighbor / w][neighbor % w] == 3) continue;
                if(map.get(next) + 1 < map.getOrDefault(neighbor, Integer.MAX_VALUE)){
                    map.put(neighbor, map.get(next) + 1);
                    pre[neighbor] = next;
                    pq.offer(neighbor);
                }
            }
            set.add(next);
        }
    }
    private void labelpath(int[][] matrix, int[] pre, int end) {
        int temp = end;
        while(pre[temp] != -1) {
            matrix[temp / matrix[0].length][temp % matrix[0].length] = 9;
            temp = pre[temp];
        }
    }
}
class Comp implements Comparator<Object>{
    int start;
    int w;
    int end;
    String type;
    public Comp(int start, int end, int w, String type) {
        this.start = start;
        this.end = end;
        this.w = w;
        this.type = type;
    }
    //@override
    public int compare(Object a1, Object b1) {
        Integer a = (Integer)a1;
        Integer b = (Integer)b1;
        int ax = a / w;
        int ay = a % w;
        int bx = b / w;
        int by = b % w;
        int ex = end / w;
        int ey = end % w;
        int sx = start / w;
        int sy = start % w;
        if(type.equals("dik")) {
            return (ax - sx) * (ax - sx) + (ay - sy) * (ay - sy) - (bx - sx) * (bx - sx) - (by - sy) * (by - sy);
        }
        else if(type.equals("A*")) {
            int path1 = (ax - sx) * (ax - sx) + (ay - sy) * (ay - sy) - (bx - sx) * (bx - sx) - (by - sy) * (by - sy);
            int pathe1 = (ax - ex) * (ax - ex) + (ay - ey) * (ay - ey) - (bx - ex) * (bx - ex) - (by - ey) * (by - ey);
            return path1 + pathe1;
        }
        else return -1;
    }
}
