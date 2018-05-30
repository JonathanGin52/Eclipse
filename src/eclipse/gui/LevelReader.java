package eclipse.gui;

import eclipse.gamecomponents.GameObject;
import eclipse.gamecomponents.SmallEnemy;
import eclipse.gamecomponents.fire.*;
import eclipse.gamecomponents.path.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LevelReader {

    final static String LEVEL_DIR = "file:src/eclipse/levels/";

    private static BufferedReader reader;
    private static long wait = System.nanoTime();
    private static List<String> commands = new LinkedList<>();

    public LevelReader(String fileName) {
        try {
            System.out.println(LEVEL_DIR + fileName);
            reader = new BufferedReader(new FileReader(new File(fileName)));

            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                commands.add(nextLine);
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Please make sure the file " + fileName + " exists.");
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public List<GameObject> getNewObjects(long now) {
        List<GameObject> toAdd = new ArrayList<>();

        if (now <= wait) return toAdd;

        String next;
        String[] tokens;

        // keep reading until the next wait end of stored commands
        while (true) {
            if (commands.isEmpty()) return toAdd;

            next = commands.get(0);
            commands.remove(0);

            tokens = next.split(" ");
            if (tokens[0].equals("") || tokens[0].substring(0, 2).equals("//")) { // reading a comment in the text file
                continue;
            } else if (tokens[0].equals("wait")) {
                wait = now + Long.parseLong(tokens[1]) * 1000000;
                return toAdd;
            }
            String enemy = tokens[0];
            int xPos = Integer.parseInt(tokens[1]);
            int yPos = Integer.parseInt(tokens[2]);

            VectorPath vectorPath = null;

            switch(tokens[3]) {
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
                default:
                    System.out.println("No fire path was found.");
                    Thread.dumpStack();
                    System.exit(0);
            }

            if (enemy.equals("SmallEnemy")) {
                toAdd.add(new SmallEnemy(xPos, yPos, vectorPath, firePattern));
            }
        }
    }
}