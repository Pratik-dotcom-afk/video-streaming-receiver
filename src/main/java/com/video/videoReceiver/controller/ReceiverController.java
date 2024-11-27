package com.video.videoReceiver.controller;

import com.video.videoReceiver.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReceiverController {

    @Autowired
    private ReceiverService receiverService;

    @PostMapping("/receiver/upload-chunk")
    public void uploadChunk(@RequestBody ReceiverService.VideoChunk chunk) {
        try {
            receiverService.receiveChunk(chunk);
            System.out.println("Chunk received: Sequence " + chunk.getSequence());
        } catch (Exception e) {
            System.err.println("Error receiving chunk: " + e.getMessage());
        }
    }

    @PostMapping("/receiver/all-chunks-sent")
    public void allChunksSent(@RequestBody String fileExtension) {
        try {
            System.out.println("All chunks received. File extension: " + fileExtension);
            receiverService.assembleVideo();
        } catch (Exception e) {
            System.err.println("Error assembling video: " + e.getMessage());
        }
    }
}
