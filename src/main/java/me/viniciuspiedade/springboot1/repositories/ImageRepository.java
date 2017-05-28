package me.viniciuspiedade.springboot1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.viniciuspiedade.springboot1.model.Image;

@Repository("imageRepository")
public interface ImageRepository  extends JpaRepository<Image, Long> {

}
