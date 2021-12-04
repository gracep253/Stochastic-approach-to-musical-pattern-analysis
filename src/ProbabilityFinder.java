import javax.sound.midi.*;
import java.io.File;
import java.util.*;
import java.util.stream.IntStream;

public class ProbabilityFinder {

    private TreeMap<Integer, TreeMap<Integer, Integer>> probabilityMap = new TreeMap<>();
    private static final Set<Integer> VALUES = Set.of(
            62, 64, 65, 67, 69, 71, 72
    );
    public TreeMap<Integer, TreeMap<Integer, Integer>> getNotes() {
        return probabilityMap;
    }
    public double[][] makeMatrix(boolean doPrint, boolean Jama) {
        if (doPrint) System.out.print("    ");

        ArrayList<Integer> notes = new ArrayList<>();
        for (Map.Entry<Integer, TreeMap<Integer, Integer>> entry : probabilityMap.entrySet()) { // loops thru all 1st numbers
            notes.add(entry.getKey());
            for (Map.Entry<Integer, Integer> entry2 : probabilityMap.get(entry.getKey()).entrySet()) { // loops thru all second numbers for that first number
                notes.add(entry2.getKey());
            }
        }

        //int lowestKey = Collections.min(notes);
        //int highestKey = Collections.max(notes);

        int lowestKey = 62;// hard set min
        int highestKey = 72; // hard set max
        /*
        int KLsize = 0;
        for (int i = lowestKey; i <= highestKey; i++) {
            if (!notes.contains(i)) continue;
            if (doPrint) System.out.print(" [" + i + "]");
            KLsize++;
        }*/
        if (doPrint) System.out.print(" [62] [64] [65] [67] [69] [71] [72]");
        double[][] matrix = new double[7][7];
        if (doPrint) System.out.println();

        int row = 0;
        for (int i = 62; i < 73; i++) {
            if (!VALUES.contains(i)) continue;
            //if (!notes.contains(i)) continue;
            if (doPrint) System.out.print("[" + i + "] ");
            int col = 0;

            for (int j = lowestKey; j <= highestKey; j++) {
                if (!VALUES.contains(j)) continue;
                //if (!notes.contains(j)) continue;
                try {
                    double probs = (double)probabilityMap.get(i).get(j) / getTotal(i);
                    if(!Jama) probs = Math.round(probs * 100.0) / 100.0;
                    if (doPrint) System.out.print(Double.toString(probs).length() < 4 ? probs + "0 " : probs + " ");
                    matrix[row][col] = probs;
                } catch(NullPointerException n) {
                    if (doPrint) System.out.print("0.00 ");
                    matrix[row][col] = 0.00;
                }
                col++;
            }
            if (doPrint) System.out.println();
            row++;
        }
        return matrix;
    }

    public int getTotal(int key) {
        int t = 0;
        for (int second : probabilityMap.get(key).keySet()) {
            t += probabilityMap.get(key).get(second);
        }
        return t;
    }

    public ProbabilityFinder(File f) throws Exception {
        ArrayList<Integer> key_array = new ArrayList<>();
        TreeMap<String, Integer> map = new TreeMap<>();
        Sequence sequence = MidiSystem.getSequence(f);

        for (Track track : sequence.getTracks()) {
            for (int i=0; i < track.size(); i += 2) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == 0x90) {
                        key_array.add(sm.getData1());
                    }
                }
            }
        }

        for (int i = 0; i < key_array.size(); i++) {
            if (i < key_array.size() - 1) {
                String set = key_array.get(i) + " " + key_array.get(i + 1);
                if (map.containsKey(set)) map.put(set, map.get(set) + 1);
                else map.put(set, 1);
            }
        }
        for (String key : map.keySet()) {
            String[] keys = key.split(" ", 2);
            int key1 = Integer.parseInt(keys[0]);
            int key2 = Integer.parseInt(keys[1]);
            boolean isUnique = false;
            if (probabilityMap.containsKey(key1)) {
                if (probabilityMap.get(key1).containsKey(key2)) { //this entire block isnt actually needed since it doesnt do anything but im keeping it since it works and idk if ill break anything
                    isUnique = true;
                    int val = probabilityMap.get(key1).get(key2);
                    val++;
                    probabilityMap.get(key1).put(key2, val);
                } else {
                    isUnique = true;
                    probabilityMap.get(key1).put(key2, map.get(key1 + " " + key2));
                }
            }
            if (!isUnique) {
                TreeMap<Integer, Integer> temp_map = new TreeMap<>();
                temp_map.put(key2, map.get(key1 + " " + key2));
                probabilityMap.put(key1, temp_map);
            }
        }
    }
}
