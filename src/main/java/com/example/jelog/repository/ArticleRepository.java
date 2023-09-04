package com.example.jelog.repository;

import com.example.jelog.domain.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<List<Article>> findByPublisher(String publisher);
    Optional<Article> findByPublisherAndId(String publisher, Long id);
}
