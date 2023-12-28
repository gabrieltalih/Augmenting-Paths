import java.util.ArrayList;

public class PriorityQueue {

    private ArrayList<Integer> values = new ArrayList<>();
    private ArrayList<Integer> keys = new ArrayList<>();

    public void addElement(int value, int key) {

        values.add(value);
        keys.add(key);
    }

    public void updateKey(int value, int key) {

        for (int i = 0; i < values.size(); i++) {

            if (values.get(i) == value)
                keys.set(i, key);
        }
    }

    public boolean isEmpty() {

        return values.size() == 0;
    }

    public int getMinimumElement() {

        int minimumIndex = 0;

        for (int i = 1; i < keys.size(); i++) {

            if (keys.get(i) < keys.get(minimumIndex))
                minimumIndex = i;
        }

        keys.remove(minimumIndex);

        return values.remove(minimumIndex);
    }
}