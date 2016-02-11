package com.example.android.projectauto;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

public class PocketSphinxService extends Service implements RecognitionListener {

    private static final String KEYPHRASE = "oh mighty computer";
    private SpeechRecognizer recognizer;

    public PocketSphinxService() {

        try {
            Assets assets = new Assets(PocketSphinxService.this);
            File assetDir = assets.syncAssets();
            setupRecognizer(assetDir);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        if (text.equals(KEYPHRASE)) {
            //LLAMAR A LA ACTIVITY PRINCIPAL
        }
        recognizer.stop();
    }

    @Override
    public void onResult(Hypothesis hypothesis) {

    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onTimeout() {

    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                        // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                .setRawLogDir(assetsDir)
                        // Threshold to tune for keyphrase to balance between false alarms and misses
                .setKeywordThreshold(1e-45f)
                        // Use context-independent phonetic search, context-dependent is too slow for mobile
                .setBoolean("-allphone_ci", true)
                .getRecognizer();

        recognizer.addListener(this);

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch("PALABRA MAGICA", KEYPHRASE);

    }
}
