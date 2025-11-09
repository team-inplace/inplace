package my.inplace.domain.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SingletonGeometryFactory {

    private static final int WGS84_SRID = 4326;
    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), WGS84_SRID);

    public static Point newPoint(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }

    public static Polygon newPolygon(Coordinate[] coordinates) {
        return geometryFactory.createPolygon(coordinates);
    }
}
