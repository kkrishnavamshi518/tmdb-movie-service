package com.practice.tmdb.repo;
import com.practice.tmdb.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MovieRepo  extends JpaRepository<Movie,Long> {
}
