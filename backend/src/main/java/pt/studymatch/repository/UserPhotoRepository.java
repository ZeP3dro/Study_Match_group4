package pt.studymatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.studymatch.domain.UserPhoto;

public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {
}
