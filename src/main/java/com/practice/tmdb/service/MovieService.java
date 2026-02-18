package com.practice.tmdb.service;

import com.practice.tmdb.model.Movie;
import com.practice.tmdb.repo.MovieRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MovieService {
    @Autowired
    private MovieRepo movieRepo;
    public Movie create(Movie movie){
        if(movie==null){
            throw new RuntimeException("Invalid Movie");
        }
        return movieRepo.save(movie);
    }
    public Movie read(Long id){
       return movieRepo.findById(id).orElseThrow(()-> new RuntimeException("Movie Not Found"));
    }
    public void update(Long id, Movie update){
        if(update==null || id==null){
            throw new RuntimeException("Invalid Movie");
        }
        if (movieRepo.existsById(id)){
            Movie movie = movieRepo.getReferenceById(id);
            movie.setName(update.getName());
            movie.setDirector(update.getDirector());
            movie.setActors(update.getActors());
            movieRepo.save(movie);
        } else {
            throw new RuntimeException("Movie not found");
        }
    }
    public void delete(Long id){
        if (movieRepo.existsById(id)){
            movieRepo.deleteById(id);
        } else {
            throw new RuntimeException("Movie not found");
        }
    }
}