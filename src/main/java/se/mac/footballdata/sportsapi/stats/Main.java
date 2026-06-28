package se.mac.footballdata.sportsapi.stats;

import se.mac.footballdata.sportsapi.stats.EventStatsClient;
import se.mac.footballdata.sportsapi.stats.AveragePositions;
import se.mac.footballdata.sportsapi.stats.EventStats;
import se.mac.footballdata.sportsapi.stats.ShotMapEntry;

public class Main {

    public static void main(String[] args) throws Exception {
        EventStatsClient client = new EventStatsClient();
        EventStats event = client.fetchEventStats(46388);

        System.out.println("Event ID: " + event.eventId);
        System.out.println();

        // Possession
        System.out.printf("Possession  — Home: %d%%  Away: %d%%%n",
                event.stats.home.ballPossession,
                event.stats.away.ballPossession);

        // Shots
        System.out.printf("Total shots — Home: %d  Away: %d%n",
                event.stats.home.totalShots,
                event.stats.away.totalShots);

        // xG
        System.out.printf("xG          — Home: %.2f  Away: %.2f%n",
                event.stats.home.expectedGoals,
                event.stats.away.expectedGoals);

        // Goals from shotmap
        long homeGoals = event.shotmap.stream()
                .filter(s -> s.home && "goal".equals(s.type))
                .count();
        long awayGoals = event.shotmap.stream()
                .filter(s -> !s.home && "goal".equals(s.type))
                .count();
        System.out.printf("Goals       — Home: %d  Away: %d%n", homeGoals, awayGoals);

        // Average positions
        System.out.println("\n--- Home lineup ---");
        for (AveragePositions.PlayerPosition p : event.averagePositions.home) {
            System.out.printf("  [%s] %s%n", p.pos, p.name);
        }

        System.out.println("\n--- Away lineup ---");
        for (AveragePositions.PlayerPosition p : event.averagePositions.away) {
            System.out.printf("  [%s] %s%n", p.pos, p.name);
        }

        // Shot log
        System.out.println("\n--- Shot map ---");
        for (ShotMapEntry shot : event.shotmap) {
            System.out.printf("  min %2d%s  %-5s  %-4s  xG=%.3f  %s%n",
                    shot.min,
                    shot.added != null ? "+" + shot.added : "  ",
                    shot.home ? "HOME" : "AWAY",
                    shot.type,
                    shot.xg,
                    shot.body);
        }
    }
}
