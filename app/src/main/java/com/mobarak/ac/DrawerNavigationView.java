package com.mobarak.ac;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

/**
 * Created by Mobarak on July 16, 2019
 *
 * @author Sandesh360
 */
public class DrawerNavigationView extends NavigationView {
    private int selectedItemId;
    private MenuItem selectedItem;

    public DrawerNavigationView(Context context) {
        super(context);
    }

    public DrawerNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawerNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MenuItem getSelectedItem() {
        return selectedItem == null ? getMenu().getItem(0) : selectedItem;
    }

    public void setSelectedItem(MenuItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    public int getSelectedItemId() {
        return getSelectedItem().getItemId();
    }

    public void setSelectedItemId(int selectedItemId) {
        this.selectedItemId = selectedItemId;
    }
}
