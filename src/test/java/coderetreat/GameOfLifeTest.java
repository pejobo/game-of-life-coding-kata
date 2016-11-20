package coderetreat;

import coderetreat.conway.ConwaysRules;
import coderetreat.conway.Point2D;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.stream.Collectors;

import static coderetreat.conway.Point2D.at;
import static org.assertj.core.api.Assertions.assertThat;

public class GameOfLifeTest {

    private static final String BLINKER =
            " # \n" +
            " # \n" +
            " # \n";

    private GameOfLife<Point2D> _game;
    private Rules _rules;

    @Before
    public void setUp() throws Exception {
        _rules = new ConwaysRules();
        _game = new GameOfLife<>(_rules, Point2D.class);
    }


    @Test
    public void empty_board() {
        assertThat(_game.getLivingCells().count())
                .isEqualTo(0);
        _game.tick();
        assertThat(_game.getLivingCells().count())
                .isEqualTo(0);
        assertThat(_game.isStatic())
                .describedAs("isStatic")
                .isTrue();
    }


    @Test
    public void empty_board_is_static() {
        assertThat(_game.isStatic())
                .isTrue();
    }


    @Test
    public void block_is_static() {
        final String twoByTwoBlock =
                "##\n" +
                "##\n";
        Point2D.parse(twoByTwoBlock).forEach(_game::setAlive);
        _game.tick(50);
        assertThat(_game.isStatic())
                .isTrue();
        assertThat(_game.calculatePeriod(10))
                .isEqualTo(1);
    }


    @Test
    public void neighbour_cells_in_2d() {
        final Point2D center = at(1, 1);
        assertThat(center.getNeighbours().collect(Collectors.toSet()))
                .doesNotContain(center)
                .containsExactlyInAnyOrder(at(0, 0), at(1, 0), at(2, 0), at(0, 1), at(2, 1), at(0, 2), at(1, 2), at(2, 2));
    }

    @Test
    public void blinker() {
        Point2D.parse(BLINKER).forEach(_game::setAlive);
        assertThat(_rules.willBeAliveInNextGeneration(_game, at(0, 0))).isFalse();
        assertThat(_rules.willBeAliveInNextGeneration(_game, at(1, 0))).isFalse();
        assertThat(_rules.willBeAliveInNextGeneration(_game, at(2, 0))).isFalse();
        assertThat(_rules.willBeAliveInNextGeneration(_game, at(0, 1))).isTrue();
        assertThat(_rules.willBeAliveInNextGeneration(_game, at(1, 1))).isTrue();
        assertThat(_rules.willBeAliveInNextGeneration(_game, at(2, 1))).isTrue();
        assertThat(_rules.willBeAliveInNextGeneration(_game, at(0, 2))).isFalse();
        assertThat(_rules.willBeAliveInNextGeneration(_game, at(1, 2))).isFalse();
        assertThat(_rules.willBeAliveInNextGeneration(_game, at(2, 2))).isFalse();
    }


    @Test
    public void tic_blinker() {
        Point2D.parse(BLINKER).forEach(_game::setAlive);
        _game.tick();
        assertThat(_game.getLivingCells().count()).isEqualTo(3);
        assertThat(_game.isAlive(at(0, 1))).isTrue();
        assertThat(_game.isAlive(at(1, 1))).isTrue();
        assertThat(_game.isAlive(at(2, 1))).isTrue();
    }


    @Test
    public void blinker_period() {
        Point2D.parse(BLINKER).forEach(_game::setAlive);
        assertThat(_game.calculatePeriod(10)).isEqualTo(2);
    }


    @Test
    public void glider() {
        glider(5000);
        assertThat(_game.calculatePeriod(10))
                .describedAs("period")
                .isEqualTo(-1);
    }


    private void glider(int ticCount) {
        final String glider =
                " # \n" +
                "  #\n" +
                "###\n";
        Point2D.parse(glider).forEach(_game::setAlive);
        _game.tick(ticCount);
        assertThat(_game.getLivingCells().count())
                .isEqualTo(5);
        assertThat(_game.isStatic())
                .describedAs("isStatic")
                .isFalse();
    }


    @Test
    public void point2d_parsing() {
        final String point_at_1_1 =
            "    \n" +
            " *  \n" +
            "    \n";
        assertThat(Point2D.parse(point_at_1_1).collect(Collectors.toList()))
                .containsExactly(at(1, 1));
    }


    @Test
    public void move_point2D() {
        assertThat(at(1,2).move(at(-1,-2)))
                .isEqualTo(at(0,0));
    }


    @Test
    public void stressTest() {
        stressTest(100);
    }


    private void stressTest(int numberOfGenerations) {
        final String s =
                "### #\n" +
                "#    \n" +
                "   ##\n" +
                " ## #\n" +
                "# # #\n";
        Point2D.parse(s).forEach(_game::setAlive);
        _game.tick(numberOfGenerations);
        System.out.println("number of living cells: " + _game.getNumberOfLivingCells());
        assertThat(_game.isStatic())
                .describedAs("isStatic")
                .isFalse();
    }


    @Test
    @Ignore
    public void performance() {
        glider(10_000);
        System.out.println("glider finished");
        stressTest(3_000);
    }


    @Test
    public void calculate_period() {
        Point2D.parse("##########").forEach(_game::setAlive);
        _game.tick(50);
        assertThat(_game.calculatePeriod(50))
                .isEqualTo(15);
    }
}
