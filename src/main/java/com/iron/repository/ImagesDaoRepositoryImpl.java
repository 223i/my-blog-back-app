package com.iron.repository;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Repository
public class ImagesDaoRepositoryImpl implements ImagesDaoRepository{

    private final JdbcTemplate jdbcTemplate;

    public ImagesDaoRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void saveImage(Integer postId, MultipartFile file) {
        try {
            int updated = jdbcTemplate.update(
                    "UPDATE images SET image = ? WHERE post_id = ?",
                    file.getBytes(),
                    postId
            );

            if (updated == 0) {
                jdbcTemplate.update(
                        "INSERT INTO images (post_id, image) VALUES (?, ?)",
                        postId,
                        file.getBytes()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getImage(Integer postId) {
        String sql = "SELECT image FROM images WHERE post_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, byte[].class, postId);
        } catch (EmptyResultDataAccessException e) {
            throw new DataRetrievalFailureException("Image for post " + postId + " not found");
        }
    }

    public void saveImage(Integer postId, byte[] imageBytes) {
        jdbcTemplate.update(
                "UPDATE images SET image = ? WHERE post_id = ?",
                imageBytes, postId
        );
    }
}
