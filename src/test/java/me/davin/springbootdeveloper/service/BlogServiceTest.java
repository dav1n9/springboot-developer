package me.davin.springbootdeveloper.service;

import me.davin.springbootdeveloper.domain.Article;
import me.davin.springbootdeveloper.dto.AddArticleRequest;
import me.davin.springbootdeveloper.dto.UpdateArticleRequest;
import me.davin.springbootdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BlogServiceTest {

    @InjectMocks
    private BlogService blogService;

    @Mock
    private BlogRepository blogRepository;

    @DisplayName("save: 작성한 블로그 글 저장에 성공한다.")
    @Test
    void save() {
        // given
        final String title = "title";
        final String content = "content";
        final String author = "davin";
        AddArticleRequest request = new AddArticleRequest(title, content);
        Article mockArticle = new Article(author, title, content);

        // Mocking the behavior of blogRepository.save()
        given(blogRepository.save(any())).willReturn(mockArticle);

        // When
        Article savedArticle = blogService.save(request, author);

        // Then
        verify(blogRepository).save(any());

        assertThat(savedArticle.getTitle()).isEqualTo(mockArticle.getTitle());
        assertThat(savedArticle.getContent()).isEqualTo(mockArticle.getContent());
    }

    @DisplayName("findAll: 블로그 글 전체 조회에 성공한다.")
    @Test
    void findAll() {
        // given
        List<Article> mockArticles = List.of(
                new Article("author1", "title1", "content1"),
                new Article("author2", "title2", "content2")
        );
        given(blogRepository.findAll()).willReturn(mockArticles);

        // when
        List<Article> articles = blogService.findAll();

        // then
        assertThat(articles.size()).isEqualTo(2);
        assertThat(articles.get(0).getTitle()).isEqualTo("title1");
        assertThat(articles.get(0).getContent()).isEqualTo("content1");
        assertThat(articles.get(1).getTitle()).isEqualTo("title2");
        assertThat(articles.get(1).getContent()).isEqualTo("content2");
    }

    @DisplayName("findById: 블로그 글 조회에 성공한다.")
    @Test
    void findById() {
        // given
        long articleId = 1L;
        final String title = "Title";
        final String content = "Content";
        final String author = "davin";
        Article mockArticle = new Article(author, title, content);

        given(blogRepository.findById(articleId)).willReturn(Optional.of(mockArticle));

        // when
        Article article = blogService.findById(articleId);

        // then
        assertThat(article.getTitle()).isEqualTo(title);
        assertThat(article.getContent()).isEqualTo(content);
    }

    @DisplayName("findByIdNotFound: 해당 글이 없으면 에러를 반환한다.")
    @Test
    void findByIdNotFound() {
        // given
        long articleId = 1L;
        given(blogRepository.findById(articleId)).willReturn(Optional.empty());

        // when, then
        assertThrows(IllegalArgumentException.class, () -> blogService.findById(articleId));
    }

//    @DisplayName("delete: 블로그 글 삭제에 성공한다.")
//    @Test
//    void delete() {
//        // Given
//        long articleIdToDelete = 1L;
//
//        // When
//        blogService.delete(articleIdToDelete);
//
//        // Then
//        verify(blogRepository).deleteById(articleIdToDelete);
//    }
//
//    @DisplayName("update: 블로그 글 수정에 성공한다.")
//    @Transactional
//    @Test
//    void update() {
//        // given
//        long articleId = 1L;
//        UpdateArticleRequest request = new UpdateArticleRequest("newTitle", "newContent");
//        Article mockArticle = new Article("Title", "Content");
//
//        given(blogRepository.findById(articleId)).willReturn(Optional.of(mockArticle));
//
//        // when
//        Article updatedArticle = blogService.update(articleId, request);
//
//        // then
//        verify(blogRepository).findById(articleId);
//
//        assertThat(updatedArticle.getTitle()).isEqualTo(request.getTitle());
//        assertThat(updatedArticle.getContent()).isEqualTo(request.getContent());
//    }

    @DisplayName("UpdateArticleNotFound: 해당 아이디가 없으면 에러를 발생한다.")
    @Transactional
    @Test
    void UpdateArticleNotFound() {
        // given
        long articleId = 1L;
        UpdateArticleRequest request = new UpdateArticleRequest("newTitle", "newContent");

        given(blogRepository.findById(articleId)).willReturn(Optional.empty());

        // when and then
        assertThrows(IllegalArgumentException.class, () -> blogService.update(articleId, request));
    }
}