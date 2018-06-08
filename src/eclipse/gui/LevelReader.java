package eclipse.gui;

import eclipse.gamecomponents.*;
import eclipse.gamecomponents.fire.*;
import eclipse.gamecomponents.path.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LevelReader {

    final static String LEVEL_DIR = "resources/levels/";

    private long wait = System.nanoTime();
    private List<String> commands = new LinkedList<>();
    private boolean levelOver = false;
    private int level;

    public LevelReader(String fileName) {
        level = Integer.parseInt(fileName.substring(5, fileName.indexOf('.')));

        String nextLine;
        try (BufferedReader reader = new BufferedReader(new FileReader(LEVEL_DIR + fileName))) {
            while ((nextLine = reader.readLine()) != null) {
                commands.add(nextLine);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Please make sure the file " + fileName + " exists.");
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public List<GameObject> getNewObjects(long now, Player player) {
        List<GameObject> toAdd = new ArrayList<>();

        if (now <= wait) {
            return toAdd;
        }

        String next;
        String[] tokens;

        // keep reading until the next wait end of stored commands
        while (true) {
            if (commands.isEmpty()) {
                levelOver = true;
                return toAdd;
            }

            next = commands.get(0);
            commands.remove(0);

            if (next.equals("/*")) { // Multiline comment
                while (!(commands.isEmpty() || commands.get(0).equals("*/"))) {
                    commands.remove(0);
                }
                if (!commands.isEmpty()) {
                    commands.remove(0);
                }
                continue;
            }

            tokens = next.split(" ");
            if (tokens[0].equals("") || tokens[0].substring(0, 2).equals("//")) { // reading a comment in the text file
                continue;
            } else if (tokens[0].equals("wait")) {
                wait = now + Long.parseLong(tokens[1]) * 1000000L;
                return toAdd;
            }

            String enemy = tokens[0].toUpperCase();
            int xPos = Integer.parseInt(tokens[1]);
            int yPos = Integer.parseInt(tokens[2]);

            VectorPath vectorPath = null;

            switch (tokens[3]) {
                case "Down":
                    vectorPath = new Down();
                    break;
                case "Left":
                    vectorPath = new Left();
                    break;
                case "Right":
                    vectorPath = new Right();
                    break;
                case "Up":
                    vectorPath = new Up();
                    break;
                case "DownRight":
                    vectorPath = new DownRight();
                    break;
                case "DownLeft":
                    vectorPath = new DownLeft();
                    break;
                case "UShapeLeft":
                    vectorPath = new UShapeLeft();
                    break;
                case "UShapeRight":
                    vectorPath = new UShapeRight();
                    break;
                case "SineDown":
                    vectorPath = new SineDown();
                    break;
                default:
                    System.out.println("No vector path was found.");
                    Thread.dumpStack();
                    System.exit(0);
            }

            FirePattern firePattern = null;

            switch (tokens[4]) {
                case "FireDown":
                    firePattern = new FireDown();
                    break;
                case "FireDownLeft":
                    firePattern = new FireDownLeft();
                    break;
                case "FireDownRight":
                    firePattern = new FireDownRight();
                    break;
                case "FireTwoSplit":
                    firePattern = new FireTwoSplit();
                    break;
                case "FireThreeSplit":
                    firePattern = new FireThreeSplit();
                    break;
                case "FireAtPlayer":
                    firePattern = new FireAtPlayer(player);
                    break;
                default:
                    System.out.println("No fire path was found.");
                    Thread.dumpStack();
                    System.exit(0);
            }

            long startDelay = Long.parseLong(tokens[5]) * 1000000L;

            if (enemy.equals("THROWER")) {
                toAdd.add(new Thrower(xPos, yPos, vectorPath, firePattern, startDelay));
            } else if (enemy.equals("SPAMMER")) {
                toAdd.add(new Spammer(xPos, yPos, vectorPath, firePattern, startDelay));
            } else if (enemy.equals("TURRET")) {
                toAdd.add(new Turret(xPos, yPos, vectorPath, firePattern, startDelay));
            } else {
                System.out.println("No enemy was found of this type. Did you misspell something?");
                Thread.dumpStack();
                System.exit(0);
            }
        }
    }

    public boolean isLevelOver() {
        return levelOver;
    }

    public int getLevel() {
        return level;
    }
}
