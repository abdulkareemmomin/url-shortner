package com.example.demo1.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
public class UrlDoa {


    private final JdbcTemplate jdbcTemplate;

    public UrlDoa(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveUrl(String originalUrl, String shortUrl) {

        String sql = """
        INSERT INTO url_shortner.tblurldetails
        (original_url, short_url, createdAt)
        VALUES (?, ?, ?)
        """;

        return jdbcTemplate.update(
                sql,
                originalUrl,
                shortUrl,
                Timestamp.valueOf(LocalDateTime.now())
        );
    }

    public String findOriginalUrlByShortCode(String shortUrl) {
        String sql = "SELECT original_url " +
                "FROM url_shortner.tblUrlDetails " +
                "WHERE short_url = ? ";

        return jdbcTemplate.query(
                sql,
                rs -> rs.next() ? rs.getString("original_url") : null,
                shortUrl
        );
    }

    public void deleteUrl(String shortCode) {
        String sql = "DELETE from tblurldetails " +
                "WHERE short_url = ? ";

        jdbcTemplate.update(sql, shortCode);
    }
}
