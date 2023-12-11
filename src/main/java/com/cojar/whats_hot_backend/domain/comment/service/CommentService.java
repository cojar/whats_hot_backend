package com.cojar.whats_hot_backend.domain.comment.service;

import com.cojar.whats_hot_backend.domain.comment.entity.Comment;
import com.cojar.whats_hot_backend.domain.comment.repository.CommentRepository;
import com.cojar.whats_hot_backend.domain.member.entity.Member;
import com.cojar.whats_hot_backend.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment getCommentById(Long id) {
        return this.commentRepository.findById(id)
                .orElse(null);
    }

    public Comment create(Member author, Review review, String content, Comment tag) {

        Comment comment = Comment.builder()
                .author(author)
                .review(review)
                .content(content)
                .tag(tag != null ? tag : null)
                .build();

        this.commentRepository.save(comment);

        return comment;
    }
}
