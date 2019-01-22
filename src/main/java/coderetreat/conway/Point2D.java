package coderetreat.conway;

import coderetreat.model.Coordinate;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Point2D implements Coordinate<Point2D> {

    private static final NeighbourHood[] NEIGHBOUR_HOODS = NeighbourHood.values();

    public static Stream<Point2D> parse(final String s) {
        final AtomicInteger x = new AtomicInteger(-1);
        final AtomicInteger y = new AtomicInteger(0);
        return s.chars().mapToObj(Integer::valueOf).map(c ->  {
            x.incrementAndGet();
            switch ( (char) c.intValue()) {
                case '\n':
                    x.set(-1);
                    y.incrementAndGet();
                    return null;
                case ' ':
                    return null;
                default:
                    return at(x.get(), y.get());
            }
        }).filter(Objects::nonNull);
    }


    public static Point2D at(final int x, final int y) {
        return new Point2D(x, y);
    }


    private final int _x;
    private final int _y;

    private Point2D(final int x, final int y) {
        _x = x;
        _y = y;
    }


    @Override
    public Stream<Point2D> getNeighbours() {
        return Stream
                .of(NEIGHBOUR_HOODS)
                .map(n -> n.getNeighbourFrom(_x, _y));
    }


    public Point2D move(Point2D delta) {
        return at(_x + delta._x, _y + delta._y);
    }


    @Override
    public int hashCode() {
        return _x ^ (_y >>> 16);
    }


    @Override
    public boolean equals(Object o) {
        return (o instanceof Point2D) && sameCoordinates((Point2D) o);
    }


    private boolean sameCoordinates(Point2D p) {
        return (_x == p._x) && (_y == p._y);
    }


    @Override
    public String toString() {
        return "point2d{" + _x + ',' + _y + '}';
    }

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    private enum NeighbourHood {

        NE(-1, -1), N(0, -1), NW(+1, -1), E(-1, 0), W(+1, 0), SE(-1, +1), S(0, +1), SW(+1, +1);

        private final int _deltaX;
        private final int _deltaY;

        NeighbourHood(int deltaX, int deltaY) {
            _deltaX = deltaX;
            _deltaY = deltaY;
        }


        Point2D getNeighbourFrom(int x, int y) {
            return at(x + _deltaX, y + _deltaY);
        }

    }

}
