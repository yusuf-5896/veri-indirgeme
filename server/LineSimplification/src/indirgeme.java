import java.util.ArrayList;
import java.util.List;

public class indirgeme {


    private static double perpendicularDistance(Point pt, Point lineStart, Point lineEnd) {
        double lat = lineEnd.getKey() - lineStart.getKey();
        double lng = lineEnd.getValue() - lineStart.getValue();

        // Normalize
        double ortaNokta = Math.sqrt(lat*lat+ lng*lng);
        if (ortaNokta > 0.0) {
            lat /= ortaNokta;
            lng /= ortaNokta;
        }
        double pvx = pt.getKey() - lineStart.getKey();
        double pvy = pt.getValue() - lineStart.getValue();

        // Get dot product (project pv onto normalized direction)
        double pvdot = lat * pvx + lng * pvy;

        // Scale line direction vector and subtract it from pv
        double ax = pvx - pvdot * lat;
        double ay = pvy - pvdot * lng;

        return Math.sqrt(ax*ax + ay*ay);
    }

    public static List<Point> ramerDouglasPeucker(List<Point> pointList, double epsilon, List<Point> out) {
        if (pointList.size() < 2) throw new IllegalArgumentException("Yeterli Eleman Yok");

        // baslangic ve bitise en uzak noktayi bul
        double dmax = 0.0;//baslangic ve bitise en uzak nokta
        int index = 0;
        int end = pointList.size() - 1;


        for (int i = 1; i < end; ++i) {
            double d = perpendicularDistance(pointList.get(i), pointList.get(0), pointList.get(end));
            if (d > dmax) {
                index = i;
                dmax = d;
            }
        }

        // If max distance is greater than epsilon, recursively simplify
        if (dmax > epsilon) {
            List<Point> recResults1 = new ArrayList<>();
            List<Point> recResults2 = new ArrayList<>();
            List<Point> firstLine = pointList.subList(0, index + 1);
            List<Point> lastLine = pointList.subList(index, pointList.size());
            ramerDouglasPeucker(firstLine, epsilon, recResults1);
            ramerDouglasPeucker(lastLine, epsilon, recResults2);

            // build the result list
            out.addAll(recResults1.subList(0, recResults1.size() - 1));
            out.addAll(recResults2);
            if (out.size() < 2) throw new RuntimeException("Problem assembling output");
            return out;
        } else {
            // Just return start and end points
            out.clear();
            out.add(pointList.get(0));
            out.add(pointList.get(pointList.size() - 1));
            return out;
        }

    }
}
