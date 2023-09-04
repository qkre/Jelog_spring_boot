package com.example.jelog.web;


import com.example.jelog.domain.article.Article;
import com.example.jelog.repository.ArticleRepository;
import com.example.jelog.service.ArticleService;
import com.example.jelog.web.dto.AddArticleRequestDto;
import com.example.jelog.web.dto.UpdateArticleRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ArticleApiController {

    private final ArticleService articleService;

    @PostMapping("/publish")
    public ResponseEntity<List<Article>> publish(@RequestBody AddArticleRequestDto requestDto) {
        articleService.save(requestDto);
        return ResponseEntity.ok(articleService.findByPublisher(requestDto.getPublisher()));
    }

    @GetMapping("/articles/{publisher}")
    public ResponseEntity<List<Article>> findByPublisher(@PathVariable String publisher) {
        System.out.println("article Result ::: " + articleService.findByPublisher(publisher));
        return ResponseEntity.ok(articleService.findByPublisher(publisher));
    }

    @GetMapping("/articles/{publisher}/{id}")
    public ResponseEntity<Article> findById(@PathVariable String publisher, @PathVariable Long id) {
        Article article = articleService.findByPublisherAndId(publisher, id);
        System.out.println("article = " + article.toString());
        if (article == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }

    @GetMapping("/articles")
    public ResponseEntity<List<Article>> findAll() {
        return ResponseEntity.ok(articleService.findAll());
    }

    @PutMapping("/articles/{publisher}/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable String publisher, @PathVariable Long id, @RequestBody UpdateArticleRequestDto requestDto){
        Article updatedArticle = articleService.update(publisher, id, requestDto);
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/articles/{publisher}/{id}")
    public ResponseEntity<List<Article>> delete(@PathVariable String publisher, @PathVariable Long id) {
        return ResponseEntity.ok(articleService.delete(publisher, id));
    }

    @GetMapping("/articles/clearAll")
    public ResponseEntity<String> clearAll(){
        return ResponseEntity.ok(articleService.clearAll());
    }
}
