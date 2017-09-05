package com.example.hyemin.blinkling.Book_Viewer;

import android.os.AsyncTask;
import android.text.TextPaint;

import com.example.hyemin.blinkling.Book_Viewer.TextViewFragment;


/**
 * Created by gkoros on 12/03/2017.
 */

public class PagerTask extends AsyncTask<TextViewFragment.ViewAndPaint, TextViewFragment.ProgressTracker, Void> {

    private TextViewFragment textviewfragment;

    public PagerTask(TextViewFragment fragment){
        this.textviewfragment = fragment;
    }

    protected Void doInBackground(TextViewFragment.ViewAndPaint... vps) {

        TextViewFragment.ViewAndPaint vp = vps[0];
        TextViewFragment.ProgressTracker progress = new TextViewFragment.ProgressTracker();
        TextPaint paint = vp.paint;
        int numChars = 0;
        int lineCount = 0;
        int maxLineCount = vp.maxLineCount;
        int totalCharactersProcessedSoFar = 0;

        // contentString is the whole string of the book
        int totalPages = 0;
        while (vp.contentString != null && vp.contentString.length() != 0 )
        {
            while ((lineCount < maxLineCount) && (numChars < vp.contentString.length())) {
                numChars = numChars + paint.breakText(vp.contentString.substring(numChars), true, vp.screenWidth, null);
                lineCount ++;
            }

            // Retrieve the String to be displayed in the current textview
            String stringToBeDisplayed = vp.contentString.substring(0, numChars);
            int nextIndex = numChars;
            char nextChar = nextIndex < vp.contentString.length() ? vp.contentString.charAt(nextIndex) : ' ';
            if (!Character.isWhitespace(nextChar)) {
                stringToBeDisplayed = stringToBeDisplayed.substring(0, stringToBeDisplayed.lastIndexOf(" "));
            }
            numChars = stringToBeDisplayed.length();
            vp.contentString = vp.contentString.substring(numChars);

            // publish progress
            progress.totalPages = totalPages;
            progress.addPage(totalPages, totalCharactersProcessedSoFar, totalCharactersProcessedSoFar + numChars);
            publishProgress(progress);

            totalCharactersProcessedSoFar += numChars;

            // reset per page items
            numChars = 0;
            lineCount = 0;

            // increment  page counter
            totalPages ++;
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(TextViewFragment.ProgressTracker... values) {
        textviewfragment.onPageProcessedUpdate(values[0]);
    }

}
