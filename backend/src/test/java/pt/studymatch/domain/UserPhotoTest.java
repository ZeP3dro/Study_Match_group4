package pt.studymatch.domain;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserPhotoTest {

    @Test
    void validPngShouldBeAcceptedAndStoredWithDefensiveCopies() throws Exception {
        byte[] image = png();
        UserPhoto photo = new UserPhoto(1L, image, "image/png");
        byte original = image[0];
        image[0] = 0;
        byte[] returned = photo.getContent();
        returned[0] = 0;
        assertThat(photo.getContent()[0]).isEqualTo(original);
    }

    @Test
    void fakeImageWithAllowedMimeTypeShouldBeRejected() {
        byte[] html = "<html>not an image</html>".getBytes(StandardCharsets.UTF_8);
        assertThatThrownBy(() -> new UserPhoto(1L, html, "image/png"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void unsupportedTypeShouldBeRejected() throws Exception {
        byte[] image = png();
        assertThatThrownBy(() -> new UserPhoto(1L, image, "image/gif"))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("JPEG ou PNG");
        assertThatThrownBy(() -> new UserPhoto(1L, image, null))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("JPEG ou PNG");
    }

    @Test
    void imageLargerThanTwoMegabytesShouldBeRejected() {
        assertThatThrownBy(() -> new UserPhoto(1L,
                new byte[UserPhoto.MAX_SIZE_IN_BYTES + 1], "image/png"))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("2 MB");
    }

    private byte[] png() throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB), "png", bytes);
        return bytes.toByteArray();
    }
}
