package pt.studymatch.service;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.domain.UserPhoto;
import pt.studymatch.repository.UserAccountRepository;
import pt.studymatch.repository.UserPhotoRepository;

@Service
@Transactional
public class PhotoService {
    private final UserPhotoRepository photos;
    private final UserAccountRepository accounts;

    public PhotoService(UserPhotoRepository photos, UserAccountRepository accounts) {
        this.photos = photos;
        this.accounts = accounts;
    }

    @Transactional(readOnly = true)
    public boolean hasPhoto(Long accountId) {
        return photos.existsById(accountId);
    }

    public void update(Long accountId, byte[] content, String contentType) {
        if (!accounts.existsById(accountId)) {
            throw new NoSuchElementException("Utilizador não encontrado.");
        }
        UserPhoto photo = photos.findById(accountId).orElse(null);
        if (photo == null) {
            photos.save(new UserPhoto(accountId, content, contentType));
        } else {
            photo.replace(content, contentType);
        }
    }

    @Transactional(readOnly = true)
    public UserPhoto find(Long accountId) {
        return photos.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Este utilizador não tem foto."));
    }
}
