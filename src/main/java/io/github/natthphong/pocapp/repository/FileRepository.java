package io.github.natthphong.pocapp.repository;

import io.github.natthphong.pocapp.model.entity.FileEntity;
import io.github.natthphong.pocapp.model.entity.key.FileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FileRepository extends JpaRepository<FileEntity, FileId> {
}
