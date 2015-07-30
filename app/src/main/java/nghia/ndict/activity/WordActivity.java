package nghia.ndict.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import nghia.ndict.R;
import nghia.ndict.dictionary.DictTextToSpeech;
import nghia.ndict.entities.Word;

/**
 * Created by Jack Huynh on 4/4/2015.
 */
public class WordActivity extends ActionBarActivity{
    public static final String WORD_LIST = "WordList";
    public static final String SELECTED_WORD_INDEX = "SelectedWordIndex";
    private static final String TAG = "WordActivity";
    private static final int TTS_CHECK_CODE = 1;
    private ViewPager mViewPager;
    private ArrayList<Word> mWords;
    private DictTextToSpeech mDictTts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meaning_wipe);
        mWords = (ArrayList<Word>) getIntent().getSerializableExtra(WORD_LIST);
        WordPagerAdapter wordPagerAdapter = new WordPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.word_pager);
        mViewPager.setAdapter(wordPagerAdapter);
        mViewPager.setCurrentItem(getIntent().getIntExtra(SELECTED_WORD_INDEX, 0));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.book);
        actionBar.setTitle("");

        //Initialize TTS
        //Intent checkTts = new Intent();
        //checkTts.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
       // startActivityForResult(checkTts, TTS_CHECK_CODE);
       // mTtsReady = false;

        Log.d(TAG, "On Create called");
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == TTS_CHECK_CODE) {
//            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
//                mTextToSpeech = new TextToSpeech(this, this);
//            } else {
//                Toast.makeText(this, "Voice data is not available.", Toast.LENGTH_LONG).show();
//                startActivity(new Intent().setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA));
//            }
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "On Pause called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "On Resume called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "On Destroy called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dictionary_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.play_sound);
        menuItem.setVisible(mDictTts.isLoaded());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.play_sound:
                    mDictTts.speak(mWords.get(mViewPager.getCurrentItem()).getContent());
                return true;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private class WordPagerAdapter extends FragmentStatePagerAdapter {

        public WordPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Log.d(TAG, "Create Fragment");
            Fragment fragment = new WordFragment();
            Bundle args = new Bundle();
            args.putSerializable(WordFragment.OBJECT_VALUE, mWords.get(i));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mWords.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mWords.get(position).getContent();
        }
    }
}



