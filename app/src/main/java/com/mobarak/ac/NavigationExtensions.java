package com.mobarak.ac;

import android.content.Intent;
import android.util.SparseArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

/**
 * Created by Mobarak on July 16, 2019
 *
 * @author Sandesh360
 */
public class NavigationExtensions {

    public static LiveData<NavController> setupWithNavController(
            final DrawerNavigationView navigationView,
            List<Integer> navGraphIds,
            final FragmentManager fragmentManager,
            int containerId,
            Intent intent) {
        final SparseArray graphIdToTagMap = new SparseArray();
        final MutableLiveData<NavController> selectedNavController = new MutableLiveData<>();
        int firstFragmentGraphId = 0;
        for (int index = 0; index < navGraphIds.size(); index++) {
            int navGraphId = navGraphIds.get(index);
            String fragmentTag = getFragmentTag(index);
            NavHostFragment navHostFragment = obtainNavHostFragment(fragmentManager,
                    fragmentTag, navGraphId, containerId);
            NavController navController = navHostFragment.getNavController();
            NavGraph navGraph = navController.getGraph();
            int graphId = navGraph.getId();
            if (index == 0) {
                firstFragmentGraphId = graphId;
            }

            graphIdToTagMap.put(graphId, fragmentTag);
            if (navigationView.getSelectedItemId() == graphId) {
                selectedNavController.setValue(navHostFragment.getNavController());
                attachNavHostFragment(fragmentManager, navHostFragment, index == 0);
            } else {
                detachNavHostFragment(fragmentManager, navHostFragment);
            }
        }


        String selectedTag = (String) graphIdToTagMap.get(navigationView.getSelectedItemId());
        final String[] selectedItemTag = {
                selectedTag == null ? "" : selectedTag
        };
        final String firstFragmentTag = (String) graphIdToTagMap.get(firstFragmentGraphId);
        final boolean[] isOnFirstFragment = {
                selectedItemTag[0].equals(firstFragmentTag)
        };
        navigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            navigationView.setSelectedItem(item);
            boolean isStateSave;
            if (fragmentManager.isStateSaved()) {
                isStateSave = false;
            } else {
                String newlySelectedItemTag = (String) graphIdToTagMap.get(item.getItemId());
                if (!selectedItemTag[0].equals(newlySelectedItemTag)) {
                    fragmentManager.popBackStack(firstFragmentTag, 1);
                    Fragment fragment = fragmentManager.findFragmentByTag(newlySelectedItemTag);
                    if (fragment == null) {
//                            throw new TypeCastException("null cannot be cast to non-null type androidx.navigation.fragment.NavHostFragment");
                    }

                    NavHostFragment selectedFragment = (NavHostFragment) fragment;
                    if (!firstFragmentTag.equals(newlySelectedItemTag)) {
                        FragmentTransaction transaction = fragmentManager.beginTransaction()
                                .attach(selectedFragment)
                                .setPrimaryNavigationFragment(selectedFragment);
                        SparseArray $receiver$iv = graphIdToTagMap;
                        SparseArray receiverIv = graphIdToTagMap;
                        int indexIv = 0;

                        for (int i = receiverIv.size(); indexIv < i; ++indexIv) {
                            receiverIv.keyAt(indexIv);
                            String fragmentTagIter = (String) $receiver$iv.valueAt(indexIv);
                            if (fragmentTagIter.equals(newlySelectedItemTag)) {
                                Fragment firstFragment = fragmentManager.findFragmentByTag(firstFragmentTag);
                                if (firstFragment == null) {
                                    //Intrinsics.throwNpe();
                                }

                                transaction.detach(firstFragment);
                            }
                        }

                        transaction.addToBackStack(firstFragmentTag).setReorderingAllowed(true).commit();
                    }

                    selectedItemTag[0] = newlySelectedItemTag;
                    isOnFirstFragment[0] = selectedItemTag[0].equals(firstFragmentTag);
                    selectedNavController.setValue(selectedFragment.getNavController());
                    isStateSave = true;
                } else {
                    isStateSave = false;
                }
            }

            return isStateSave;
        });
        //setupItemReselected(navigationView, graphIdToTagMap, fragmentManager);
        //setupDeepLinks(navigationView, navGraphIds, fragmentManager, containerId, intent);
        int finalFirstFragmentGraphId = firstFragmentGraphId;
        fragmentManager.addOnBackStackChangedListener(() -> {
            if (!isOnFirstFragment[0]) {
                FragmentManager fm = fragmentManager;
                String sft = firstFragmentTag;
                if (isOnBackStack(fm, sft)) {
                    navigationView.setSelectedItem(
                            navigationView.getMenu()
                                    .findItem(finalFirstFragmentGraphId)
                    );
                }
            }

            NavController navC = selectedNavController.getValue();
            if (navC != null) {
                if (navC.getCurrentDestination() == null) {
                    NavGraph navg = navC.getGraph();
                    navC.navigate(navg.getId());
                }
            }

        });
        return selectedNavController;
    }

    private static final void setupDeepLinks(DrawerNavigationView setupDeepLinks, List<Integer> navGraphIds, FragmentManager fragmentManager, int containerId, Intent intent) {
        for (int i = 0; i < navGraphIds.size(); i++) {
            int navGraphId = navGraphIds.get(i);
            String fragmentTag = getFragmentTag(i);
            NavHostFragment navHostFragment = obtainNavHostFragment(fragmentManager,
                    fragmentTag, navGraphId, containerId);
            if (navHostFragment.getNavController().handleDeepLink(intent)) {
                int selectedItemId = setupDeepLinks.getSelectedItemId();
                NavController navController = navHostFragment.getNavController();
                NavGraph navGraph = navController.getGraph();
                if (selectedItemId != navGraph.getId()) {
                    navController = navHostFragment.getNavController();
                    navGraph = navController.getGraph();
                    setupDeepLinks.setSelectedItem(
                            setupDeepLinks.getMenu()
                                    .findItem(navGraph.getId())
                    );
                }
            }
        }

    }

    private static final void setupItemReselected(NavigationView setupItemReselected, final SparseArray graphIdToTagMap, final FragmentManager fragmentManager) {
        setupItemReselected.setNavigationItemSelectedListener(item -> {
            String newlySelectedItemTag = (String) graphIdToTagMap.get(item.getItemId());
            Fragment fragment = fragmentManager.findFragmentByTag(newlySelectedItemTag);
            if (fragment instanceof NavHostFragment) {
                NavHostFragment selectedFragment = (NavHostFragment) fragment;
                NavController navController = selectedFragment.getNavController();
                NavGraph navGraph = navController.getGraph();
                navController.popBackStack(navGraph.getStartDestination(), false);
            }
            return false;
        });
    }

    private static final void detachNavHostFragment(FragmentManager fragmentManager,
                                                    NavHostFragment navHostFragment) {
        fragmentManager.beginTransaction().detach(navHostFragment).commitNow();
    }

    private static final void attachNavHostFragment(FragmentManager fragmentManager,
                                                    NavHostFragment navHostFragment,
                                                    boolean isPrimaryNavFragment) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.attach(navHostFragment);
        if (isPrimaryNavFragment) {
            transaction.setPrimaryNavigationFragment(navHostFragment);
        }
        transaction.commitNow();
    }

    public static NavHostFragment obtainNavHostFragment(FragmentManager fragmentManager, String fragmentTag, int navGraphId, int containerId) {
        Fragment existingFragment = fragmentManager.findFragmentByTag(fragmentTag);
        if (existingFragment instanceof NavHostFragment) {
            return (NavHostFragment) existingFragment;
        }
        // Otherwise, create it and return it.
        NavHostFragment navHostFragment = NavHostFragment.create(navGraphId);
        fragmentManager.beginTransaction()
                .add(containerId, navHostFragment, fragmentTag)
                .commitNow();
        return navHostFragment;
    }

    private static final boolean isOnBackStack(FragmentManager fragmentManager, String backStackName) {
        int backStackCount = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i <= backStackCount; i++) {
            String name = fragmentManager.getBackStackEntryAt(i).getName();
            if (name.equals(backStackName)) {
                return true;
            }
        }
        return false;
    }

    private static final String getFragmentTag(int index) {
        return "bottomNavigation#" + index;
    }
}
