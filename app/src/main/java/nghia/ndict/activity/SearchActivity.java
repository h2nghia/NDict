package nghia.ndict.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import nghia.ndict.R;
import nghia.ndict.controllers.WordAdapter;
import nghia.ndict.dictionary.Dictionary;
import nghia.ndict.entities.SearchResult;
import nghia.ndict.entities.Word;
import nghia.ndict.utils.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 4/16/13
 * Time: 12:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchActivity extends BaseActivity {

    private static final String SEARCH_FRAGMENT_TAG = "SearchFragment";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_navigation_drawer);
        setupViews();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);


        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        addSearchFragment();
    }

    private void addSearchFragment() {
        SearchFragment searchFragment = new SearchFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content_frame, searchFragment, SEARCH_FRAGMENT_TAG);
        fragmentTransaction.commit();
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SEARCH_FRAGMENT_TAG);
        if (fragment != null && fragment instanceof SearchFragment) {
            ((SearchFragment) fragment).handleIntent(intent);
        }
        super.onNewIntent(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void setupViews() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, new String[]{"Share this Dictionary", "About Creator"}));

    }


    public static class SearchFragment extends Fragment {
        ListView listView;
        boolean isScrolling = false;
        View mRootView;
        private Dictionary dictionary;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mRootView = inflater.inflate(R.layout.search, container, false);
            setupListView();
            initDataForListView();
            handleIntent(getActivity().getIntent());
            return mRootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

            inflater.inflate(R.menu.home_menu, menu);
            SearchManager searchManager = (SearchManager) getActivity().getSystemService(getActivity().SEARCH_SERVICE);
            MenuItem searchItem = menu.findItem(R.id.search);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    try {
                        searchWord(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }

        private void setupListView() {
            listView = (ListView) mRootView.findViewById(R.id.ID_WordList);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), WordActivity.class);
                    intent.putExtra(WordActivity.WORD_LIST, ((WordAdapter) listView.getAdapter()).getWords());
                    intent.putExtra(WordActivity.SELECTED_WORD_INDEX, i);
                    startActivity(intent);
                }
            });
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                    switch (scrollState) {
                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                            isScrolling = true;
                            updateAdapterOfListView();
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                            isScrolling = false;
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                            isScrolling = true;
                            updateAdapterOfListView();

                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }

        private void initDataForListView() {
            dictionary = new Dictionary();
            try {
                dictionary.initDictionary("Anh_Viet");
                searchWord("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void updateListView(SearchResult sResult) throws Exception {
            WordAdapter wordAdapter = new WordAdapter(getActivity(), R.id.ID_WordList, sResult.getWordsList());
            listView.setAdapter(wordAdapter);
            listView.setSelectionFromTop(sResult.getMatchIndex(), 0);
        }

        private void updateAdapterOfListView() {
            if (listView.getFirstVisiblePosition() == 0 && !dictionary.getCurrentHash().isFirstHash()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ArrayList<Word> wordArrayList = dictionary.loadUp(Constants.DEFAULT_LOAD_UP);
                            int lastPosition = listView.getFirstVisiblePosition();
                            ((WordAdapter) listView.getAdapter()).addToTop(wordArrayList);
                            ((WordAdapter) listView.getAdapter()).notifyDataSetChanged();
                            listView.setSelectionFromTop(lastPosition + wordArrayList.size(), 0);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            } else if (listView.getLastVisiblePosition() == listView.getCount() - 1) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ((WordAdapter) listView.getAdapter()).addToBottom(dictionary.loadDown());
                            ((WordAdapter) listView.getAdapter()).notifyDataSetChanged();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }

        public void handleIntent(Intent intent) {
            if (intent.getAction() == Intent.ACTION_SEARCH) {
                String query = intent.getStringExtra(SearchManager.QUERY);
                try {
                    searchWord(query);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void searchWord(String text) throws Exception {
            if (text.length() > 3) {
                int matchedIndex = ((WordAdapter) listView.getAdapter()).findMatchedWord(text, 0, listView.getCount());
                listView.setSelectionFromTop(matchedIndex, 0);
            } else {
                SearchResult sResult = dictionary.search(text);
                updateListView(sResult);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
