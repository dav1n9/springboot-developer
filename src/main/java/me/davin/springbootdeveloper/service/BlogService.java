package me.davin.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.davin.springbootdeveloper.domain.Article;
import me.davin.springbootdeveloper.dto.AddArticleRequest;
import me.davin.springbootdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }
}
