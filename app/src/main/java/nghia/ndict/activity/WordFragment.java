package nghia.ndict.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;

import nghia.ndict.R;
import nghia.ndict.dictionary.DataHandler;
import nghia.ndict.entities.Word;
import nghia.ndict.utils.Utils;

public class WordFragment extends Fragment {
    public static final String OBJECT_VALUE = "object_value";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.word_fragment, container, false);
        TextView tv = (TextView) view.findViewById(R.id.tv_WordContent);
        Bundle arg = getArguments();
        Word w = (Word) arg.getSerializable(OBJECT_VALUE);
        ReadWordMeaningTask readMeaningTask = new ReadWordMeaningTask(tv, w);
        readMeaningTask.execute();
        return view;
    }


}

class ReadWordMeaningTask extends AsyncTask<Void, Void, SpannableStringBuilder> {
    private WeakReference<TextView> mTextView;
    private Word mWord;

    public ReadWordMeaningTask(TextView textView, Word word) {
        mTextView = new WeakReference<TextView>(textView);
        mWord = word;
    }


    @Override
    protected SpannableStringBuilder doInBackground(Void... params) {
        return formatMeaning(getMeaning(mWord));
    }

    @Override
    protected void onPreExecute() {
        mTextView.get().setText("Loading...");
    }

    @Override
    protected void onPostExecute(SpannableStringBuilder formmattedMeaning) {
        TextView tv = mTextView.get();
        if (tv != null) {
            tv.setText(formmattedMeaning);
        }
    }


    private String getMeaning(Word w) {
        String meaning = "";
        if (w != null) {
            DataHandler dataHandler = new DataHandler("Anh_Viet");
            try {
                meaning = dataHandler.readMeaning(w);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return meaning;
    }

    private SpannableStringBuilder formatMeaning(String meaning) {
        if (Utils.isNullOrEmpty(meaning)) {
            return new SpannableStringBuilder("");
        }
        SpannableStringBuilder sbuider = new SpannableStringBuilder();

        int idx = 0;
        int count = 1;
        int plusIndex = -1;
        while (idx < meaning.length()) {
            int endLine = meaning.indexOf('\n', idx);
            StringBuilder stringBuilder = new StringBuilder();
            char curChar = meaning.charAt(idx);
            String subString = meaning.substring(idx + 1, endLine + 1);
            if (curChar == '=') {
                stringBuilder.append("   -->");
                plusIndex = subString.indexOf('+', 0);
                if (plusIndex != -1) {
                    subString = subString.replace('+', ':');
                }
            } else if (curChar == '*') {
                stringBuilder.append(count).append(". ");
                count++;
            } else if (curChar == '-') {
                stringBuilder.append('-');
            }
            stringBuilder.append(subString);
            SpannableString line = new SpannableString(stringBuilder);
            if (line.length() > 0) {
                if (curChar == '@') {
                    line.setSpan(new ForegroundColorSpan(Color.parseColor("#EC255A")), 0, line.length(), 0);
                } else if (curChar == '*') {
                    line.setSpan(new StyleSpan(Typeface.BOLD), 0, line.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    line.setSpan(new ForegroundColorSpan(Color.parseColor("#70DAD6")), 0, line.length(), 0);
                } else if (curChar == '=') {
                    line.setSpan(new ForegroundColorSpan(Color.parseColor("#7082DA")), 0, line.length(), 0);
                    line.setSpan(new StyleSpan(Typeface.ITALIC), stringBuilder.indexOf(":"), line.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    line.setSpan(new ForegroundColorSpan(Color.parseColor("#78797C")), 0, line.length(), 0);
                }

                sbuider.append(line);
            }
            idx = endLine + 1;
        }
        return sbuider;
    }
}
