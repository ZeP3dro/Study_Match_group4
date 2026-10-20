package pt.studymatch.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * Profile photo of a user (team extra). It has its own table so that loading
 * the list of students does not load every photo.
 */
@Entity
@Table(name = "user_photos")
public class UserPhoto {

    public static final int MAX_SIZE_IN_BYTES = 2 * 1024 * 1024;
    private static final Map<String, String> IMAGE_FORMAT_BY_TYPE = Map.of(
            "image/jpeg", "jpeg",
            "image/png", "png");

    @Id
    private Long accountId;

    @Lob
    @Column(nullable = false, length = MAX_SIZE_IN_BYTES)
    private byte[] content;

    @Column(nullable = false)
    private String contentType;

    protected UserPhoto() {
    }

    public UserPhoto(Long accountId, byte[] content, String contentType) {
        this.accountId = accountId;
        replace(content, contentType);
    }

    public void replace(byte[] newContent, String newContentType) {
        if (newContent == null || newContent.length == 0) {
            throw new IllegalArgumentException("Escolhe uma imagem.");
        }
        if (newContentType == null || !IMAGE_FORMAT_BY_TYPE.containsKey(newContentType)) {
            throw new IllegalArgumentException("A foto tem de ser JPEG ou PNG.");
        }
        if (newContent.length > MAX_SIZE_IN_BYTES) {
            throw new IllegalArgumentException("A foto não pode ter mais de 2 MB.");
        }
        if (!isImageOfFormat(newContent, IMAGE_FORMAT_BY_TYPE.get(newContentType))) {
            throw new IllegalArgumentException("O ficheiro não contém uma imagem JPEG ou PNG válida.");
        }
        this.content = newContent.clone();
        this.contentType = newContentType;
    }

    /**
     * Checks the real format of the file (not only the type sent by the browser).
     * Only the header is read, so a large image is never decoded into memory.
     */
    private static boolean isImageOfFormat(byte[] bytes, String expectedFormat) {
        try (ImageInputStream input = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            return readers.hasNext() && readers.next().getFormatName().equalsIgnoreCase(expectedFormat);
        } catch (IOException exception) {
            return false;
        }
    }

    public byte[] getContent() {
        return content.clone();
    }

    public String getContentType() {
        return contentType;
    }
}
