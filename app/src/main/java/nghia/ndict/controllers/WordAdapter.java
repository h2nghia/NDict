package nghia.ndict.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nghia.ndict.R;
import nghia.ndict.entities.Word;
import nghia.ndict.utils.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 4/16/13
 * Time: 12:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class WordAdapter extends ArrayAdapter<Word> {
    private Context context;
    private ArrayList<Word> wordList;

    public WordAdapter(Context context, int textViewResourceId, ArrayList<Word> objects) {
        super(context, textViewResourceId, objects);
        this.wordList = objects;
        this.context = context;
    }

    public ArrayList<Word> getWords() {
        return wordList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position > wordList.size() - 1) {
            return super.getView(position, convertView, parent);
        }
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.word_item, null);
            TextView textView = (TextView) view.findViewById(R.id.WordContent);

            viewHolder = new ViewHolder();
            viewHolder.textView = textView;
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Word word = wordList.get(position);
        if (word != null) {
            viewHolder.textView.setText(word.getContent());
        }

        return view;
    }

    public int findMatchedWord(String key, int left, int right) {
        if (left > right || Utils.isNullOrEmpty(key)) {
            return 0;
        }
        int middle = (left + right) / 2;
        int cmp = Utils.compareTwoStrings(key, wordList.get(middle).getContent());
        if (cmp == -1) {
            return findMatchedWord(key, left, middle - 1);
        } else if (cmp == 0) {
            return middle;
        } else {
            return findMatchedWord(key, middle + 1, right);
        }
    }

    public void addToTop(ArrayList<Word> w) {
        wordList.addAll(0, w);
    }

    public void addToBottom(ArrayList<Word> wordArrayList) {
        wordList.addAll(wordArrayList);
    }

    public static class ViewHolder {
        TextView textView;
    }
}
