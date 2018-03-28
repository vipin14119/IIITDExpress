package com.dhcs.vipin.iiitdexpress.mess.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static final List<MessItem> BREAKFAST_ITEMS = new ArrayList<MessItem>();
    public static final List<MessItem> LUNCH_ITEMS = new ArrayList<MessItem>();
    public static final List<MessItem> SNACK_ITEMS = new ArrayList<MessItem>();
    public static final List<MessItem> DINNER_ITEMS = new ArrayList<MessItem>();

    public static final HashMap<String, HashMap<String, ArrayList<String>>> hashMap = new HashMap<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }

        HashMap<String, ArrayList<String>> map = new HashMap<>();

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Cutlet");
        arrayList.add("Sprouts");
        arrayList.add("Bread Butter");
        arrayList.add("Milk/Tea");
        arrayList.add("Milk");
        arrayList.add("2 Bread Slices");
        arrayList.add("Butter");
        arrayList.add("Boiled Eggs/Banana(2 pieces)");
        map.put("Breakfast", arrayList);

        arrayList = new ArrayList<>();
        arrayList.add("Kala Chana");
        arrayList.add("Ghiya kofta");
        arrayList.add("Rice");
        arrayList.add("Roti");
        arrayList.add("Salad");
        arrayList.add("Mix Raita");
        map.put("Lunch", arrayList);

        arrayList = new ArrayList<>();
        arrayList.add("Dahi Bhalla");
        arrayList.add("Rasna/Tang");
        map.put("Snack", arrayList);


        arrayList = new ArrayList<>();
        arrayList.add("Moong sabut");
        arrayList.add("Gobhi Matar");
        arrayList.add("Rice");
        arrayList.add("Roti");
        arrayList.add("Macaroni salad");
        arrayList.add("Suji Halwa");
        map.put("Dinner", arrayList);

        hashMap.put("Monday", map);





        BREAKFAST_ITEMS.add(new MessItem("Bread"));
        BREAKFAST_ITEMS.add(new MessItem("Sandwich"));
        BREAKFAST_ITEMS.add(new MessItem("Samosa"));
        BREAKFAST_ITEMS.add(new MessItem("Sprout"));
        BREAKFAST_ITEMS.add(new MessItem("Butter"));
        BREAKFAST_ITEMS.add(new MessItem("Milk"));
        BREAKFAST_ITEMS.add(new MessItem("Boiled Eggs"));
        BREAKFAST_ITEMS.add(new MessItem("Milk/Tea"));
        BREAKFAST_ITEMS.add(new MessItem("Cutlet"));
        BREAKFAST_ITEMS.add(new MessItem("Samosa"));
        BREAKFAST_ITEMS.add(new MessItem("macroni"));

        LUNCH_ITEMS.add(new MessItem("Kala Chana"));
        LUNCH_ITEMS.add(new MessItem("Ghiya kofta"));
        LUNCH_ITEMS.add(new MessItem("Rice"));
        LUNCH_ITEMS.add(new MessItem("Roti"));
        LUNCH_ITEMS.add(new MessItem("Salad"));
        LUNCH_ITEMS.add(new MessItem("Mix Raita"));
        LUNCH_ITEMS.add(new MessItem("Arhar Dal"));
        LUNCH_ITEMS.add(new MessItem("Dum Aloo"));
        LUNCH_ITEMS.add(new MessItem("Rice"));
        LUNCH_ITEMS.add(new MessItem("Roti"));
        LUNCH_ITEMS.add(new MessItem("Vinegar Onion"));

        SNACK_ITEMS.add(new MessItem("Dahi Bhalla"));
        SNACK_ITEMS.add(new MessItem("Rasna/Tang"));
        SNACK_ITEMS.add(new MessItem("Macaroni"));
        SNACK_ITEMS.add(new MessItem("Coffe"));

        DINNER_ITEMS.add(new MessItem("Moong sabut"));
        DINNER_ITEMS.add(new MessItem("Gobhi Matar"));
        DINNER_ITEMS.add(new MessItem("Rice"));
        DINNER_ITEMS.add(new MessItem("Roti"));
        DINNER_ITEMS.add(new MessItem("Macaroni salad"));
        DINNER_ITEMS.add(new MessItem("Suji Halwa"));
        DINNER_ITEMS.add(new MessItem("Masoor Dal"));
        DINNER_ITEMS.add(new MessItem("Mix Veg"));
        DINNER_ITEMS.add(new MessItem("Roti"));
        DINNER_ITEMS.add(new MessItem("Rice"));


    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public static class MessItem{
        public String name;

        public MessItem(String name){
            this.name = name;
        }
    }
}
