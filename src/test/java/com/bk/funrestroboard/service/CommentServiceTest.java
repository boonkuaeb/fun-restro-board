package com.bk.funrestroboard.service;

import com.bk.funrestroboard.model.Comment;
import com.bk.funrestroboard.model.CommentType;
import com.bk.funrestroboard.repository.CommentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class CommentServiceTest {

    @MockBean
    CommentRepository commentRepository;

    CommentService commentService;

    @Before
    public void setUp() throws Exception {
        commentService = new CommentService(commentRepository);
    }

    @Test
    public void saveAll_HappyPath_ShouldSave2Comments() {
        // Given
        Comment comment1 = new Comment();
        comment1.setComment("comment1");
        comment1.setType(CommentType.PLUS);
        comment1.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        Comment comment2 = new Comment();
        comment2.setComment("comment2");
        comment2.setType(CommentType.PLUS);
        comment2.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        List<Comment> mockComments = Arrays.asList(comment1,comment2);

        when(commentRepository.saveAll(mockComments)).thenReturn(mockComments);

        // When
        List<Comment> actualComments = commentService.saveAll(mockComments);

        // Then
        assertThat(actualComments).hasSize(2);
        assertThat(actualComments.get(0)).hasFieldOrPropertyWithValue("comment","comment1");
        assertThat(actualComments.get(1)).hasFieldOrPropertyWithValue("comment","comment2");

        // verify
        verify(commentRepository,times(1)).saveAll(mockComments);

    }

    @Test
    public void getAllCommentsForToday_HappyPath_ShouldReturn1Comment() {
        // Given
        Comment comment1 = new Comment();
        comment1.setComment("Test");
        comment1.setType(CommentType.PLUS);
        comment1.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        List<Comment> exceptComments = Arrays.asList(comment1);
        LocalDate now = LocalDate.now();

        when(commentRepository.findByCreatedYearAndMonthAndDay(
                now.getYear(),
                now.getMonth().getValue(),
                now.getDayOfMonth()
        )).thenReturn(exceptComments);

        // When
        List<Comment> comments = commentService.getAllCommentsForToday();

        assertThat(comments).isEqualTo(exceptComments);

        verify(commentRepository, times(1)).findByCreatedYearAndMonthAndDay(
                now.getYear(),
                now.getMonth().getValue(),
                now.getDayOfMonth()
        );
    }
}