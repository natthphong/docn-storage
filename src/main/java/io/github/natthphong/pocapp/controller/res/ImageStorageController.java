package io.github.natthphong.pocapp.controller.res;

import io.github.natthphong.pocapp.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1")
public class ImageStorageController {


    @Value("version")
    private String version = "0.0.1";

    private final ImageStorageService service;

    public ImageStorageController(ImageStorageService service) {
        this.service = service;
    }


    @GetMapping("/versions")
    public Map<String, Object> version() {

        return Map.of("version", version);
    }

    @GetMapping("/test/init/{companyCode}")
    public Map<String, Object> generateTest(@PathVariable String companyCode) {
        service.generatePdfTest(companyCode);
        return Map.of("status", "ok");
    }

    @GetMapping("/get/image/{key}")
    public URL getUrlFile(@PathVariable String key) {
        return service.getImage(key);
    }

    @GetMapping("/get/images/{folder}")
    public List<String> getListName(@PathVariable String folder) {
        return service.getImages(folder);
    }

    @PutMapping("/save/image")
    public void saveImage(@RequestParam(value = "image", required = true) MultipartFile image) throws IOException {
        service.saveFile(image);
    }

    @DeleteMapping("/delete/image/{fileId}")
    public void deleteById(@PathVariable("fileId") Long fileId) throws Exception {
        service.deleteFile(fileId);
    }

}