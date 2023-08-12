import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionResult;

public class SpeechRecognition {

    public static void main(String[] args) throws IOException {
        // Create a SpeechClient
        try (SpeechClient speechClient = SpeechClient.create()) {

            // Create a RecognitionConfig
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .build();

            // Get the audio content from the user
            byte[] audioContent = getAudioContentFromUser();

            // Create a RecognitionAudio
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioContent)
                    .build();

            // Start a recognition request
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            speechClient.longRunningRecognizeAsync(config, audio, executorService, (response) -> {
                for (SpeechRecognitionResult result : response.getResults()) {
                    System.out.println("Transcript: " + result.getAlternatives(0).getTranscript());
                }
            });
        }
    }

    private static byte[] getAudioContentFromUser() throws IOException {
        // Replace the file path with the actual path to your audio file
        return Files.readAllBytes(Paths.get("path/to/your/audio/file.wav"));
    }
}
