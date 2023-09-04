package com.example.jelog.service;

import com.example.jelog.domain.article.Article;
import com.example.jelog.repository.ArticleRepository;
import com.example.jelog.web.dto.AddArticleRequestDto;
import com.example.jelog.web.dto.UpdateArticleRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Long save(AddArticleRequestDto requestDto){
        return articleRepository.save(
                Article.builder()
                        .publisher(requestDto.getPublisher())
                        .title(requestDto.getTitle())
                        .content(requestDto.getContent())
                        .tags(requestDto.getTags())
                        .build()
        ).getId();
    }

    public List<Article> findByPublisher(String publisher){
        return articleRepository.findByPublisher(publisher).get();
    }

    public Article findByPublisherAndId(String publisher, Long id) {
        return articleRepository.findByPublisherAndId(publisher, id).orElse(null);
    }

    public List<Article> findAll(){
        return articleRepository.findAll();
    }

    @Transactional
    public Article update(String publisher, Long id, UpdateArticleRequestDto requestDto){
        Article article = articleRepository.findByPublisherAndId(publisher, id).orElseThrow(() -> new IllegalArgumentException("Article is not exist."));
        article.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getTags());

        return article;
    }

    public List<Article> delete(String publisher, Long id) {
        articleRepository.delete(findByPublisherAndId(publisher, id));
        return articleRepository.findAll();
    }

    public String clearAll(){
        articleRepository.deleteAll();
        return "Cleared";
    }
}
