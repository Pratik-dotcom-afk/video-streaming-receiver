package com.video.videoReceiver.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ReceiverService {

    @Value("${output.video.directory}")
    private String outputVideoDirectory;

    private final List<VideoChunk> receivedChunks = new ArrayList<>();
    private String fileExtension = ""; // To store the file extension

    public void receiveChunk(VideoChunk chunk) {
        receivedChunks.add(chunk);
        System.out.println("Received chunk: " + chunk.getSequence());
        if (chunk.getFileExtension() != null && !chunk.getFileExtension().isEmpty()) {
            fileExtension = chunk.getFileExtension(); // Update file extension if provided
        }
    }

    public void assembleVideo() {
        if (fileExtension.isEmpty()) {
            throw new RuntimeException("File extension not received. Cannot assemble video.");
        }

        receivedChunks.sort(Comparator.comparingInt(VideoChunk::getSequence));

        String outputFilePath = outputVideoDirectory + File.separator + "reassembled-video." + fileExtension;

        File outputFile = new File(outputFilePath);
        try {
            File parentDir = outputFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                for (VideoChunk chunk : receivedChunks) {
                    fos.write(chunk.getData());
                }
            }
            System.out.println("Video assembled at: " + outputFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Error assembling video file", e);
        }
    }

    public static class VideoChunk {
        private String id;
        private int sequence;
        private byte[] data;
        private String fileExtension;

        public VideoChunk() {}

        public VideoChunk(String id, int sequence, byte[] data, String fileExtension) {
            this.id = id;
            this.sequence = sequence;
            this.data = data;
            this.fileExtension = fileExtension;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        public String getFileExtension() {
            return fileExtension;
        }

        public void setFileExtension(String fileExtension) {
            this.fileExtension = fileExtension;
        }
    }
}
