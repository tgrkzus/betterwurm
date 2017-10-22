package com.wurmonline.client.renderer.gui;
import com.wurmonline.client.renderer.backend.Queue;
import java.util.ArrayList;
import java.util.HashMap;
import com.wurmonline.client.options.Options;
import com.wurmonline.client.renderer.gui.text.TextFont;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BetterCraftingWindow extends WWindow {
    private InventoryWindow inventoryWindow;
    private List<CraftableItem> createItemList;
    private Map<Short, CategoryItem> catagories;

    public enum Status {
        CRAFTABLE,
        NOT_CRAFTABLE,
        WEIGHT,
    }

    public class CategoryItem extends CraftableItem {
        public CategoryItem(String name) {
            super(name, "", (short)0);
        }

    }

    public class CraftableItem extends TreeListItem {

        private String name;
        private short categoryId;
        private String[] parameters;

        public CraftableItem(String name, String status, short categoryId) {
            this.name = name;
            this.categoryId = categoryId;
            this.parameters = new String[1];
            this.parameters[0] = status;
        }

        @Override
        String getName() {
            return name;
        }

        @Override
        String getParameter(int p0) {
            return parameters[p0];
        }

        @Override
        int compareTo(TreeListItem p0, int p1) {
            return 0;
        }
    }

    private static Logger logger = Logger.getLogger(BetterCraftingWindow.class.getName());
    private WurmTreeList<CraftableItem> createItemTreeList;
    private boolean hasData = false;

    public BetterCraftingWindow(final InventoryWindow inventoryWindow) {
        super("BetterCraftingWindow");
        this.inventoryWindow = inventoryWindow;

        this.setTitle("Better Crafting Recipes");

        final int statusWidth = TextFont.getText().getWidth("Not Available") + 4 * Options.fontSizeDefault.value();
        final int[] colWidths = { statusWidth };
        final String[] colNames = { "Status" };
        this.createItemTreeList = new WurmTreeList<>("item treelist", colWidths, colNames);


        final WurmBorderPanel mainPanel = new WurmBorderPanel("Better crafting");
        mainPanel.setComponent(this.createItemTreeList, 4);
        final WurmArrayPanel<FlexComponent> array = new WurmArrayPanel<>(1);
        mainPanel.setComponent(array, 2);

        this.setComponent(mainPanel);
        this.setInitialSize(300, 300, false, 0.0f, 0.5f);
        layout();
    }

    @Override
    public void gameTick() {

    }

    public void rebuild() {
        if (hasData) {
            createItemTreeList.clear();

            for (Map.Entry<Short, CategoryItem> item : catagories.entrySet()) {
                createItemTreeList.addTreeListItem(item.getValue(), null);
            }

            for (CraftableItem item : createItemList) {
                logger.log(Level.INFO, item.toString());
                createItemTreeList.addTreeListItem(item, catagories.get(item.categoryId));

            }
        }
    }

    public void renderComponent(final Queue queue, final float alpha) {
        super.renderComponent(queue, alpha);
    }

    public void toggle() {
        hud.toggleComponent(this);
    }

    @Override
    void closePressed() {
        hud.toggleComponent(this);
    }

    public void populate(List<CreationListItem> createItemList, Map<Short, CreationListItem> catagories) {
        this.catagories = new HashMap<>();
        this.createItemList = new ArrayList<>();


        for (Map.Entry<Short, CreationListItem> item : catagories.entrySet()) {
            this.catagories.put(item.getKey(), new CategoryItem(item.getValue().getName()));
        }

        for (CreationListItem item : createItemList) {
            String status = null;
            switch (getItemStatus(item)) {
                case CRAFTABLE:
                    status = "Craftable";
                    break;
                case NOT_CRAFTABLE:
                    status = "Not Available";
                    break;
                case WEIGHT:
                    status = "Too Heavy";
                    break;
            }
            this.createItemList.add(new CraftableItem(item.getName(), status, item.getCategoryId()));
        }
        hasData = true;
        rebuild();
    }

    public boolean hasData() {
        return hasData;
    }

    public Status getItemStatus(CreationListItem item) {
        InventoryListComponent list = inventoryWindow.getInventoryListComponent();

        return Status.CRAFTABLE;
    }
}
