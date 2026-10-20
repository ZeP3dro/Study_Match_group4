package pt.studymatch.controller;

import java.io.IOException;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pt.studymatch.domain.UserPhoto;
import pt.studymatch.service.CurrentUserService;
import pt.studymatch.service.PhotoService;

@RestController
@RequestMapping("/users/{accountId}/photo")
public class PhotoController {
    private final CurrentUserService currentUser;
    private final PhotoService photos;

    public PhotoController(CurrentUserService currentUser, PhotoService photos) {
        this.currentUser = currentUser;
        this.photos = photos;
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> upload(@PathVariable Long accountId, @RequestParam("photo") MultipartFile file) throws IOException {
        currentUser.checkCanEditPhoto(accountId);
        photos.update(accountId, file.getBytes(), file.getContentType());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<byte[]> photo(@PathVariable Long accountId) {
        currentUser.checkCanViewPhoto(accountId);
        UserPhoto photo = photos.find(accountId);
        return ResponseEntity.ok().cacheControl(CacheControl.noStore())
                .contentType(MediaType.parseMediaType(photo.getContentType())).body(photo.getContent());
    }
}
