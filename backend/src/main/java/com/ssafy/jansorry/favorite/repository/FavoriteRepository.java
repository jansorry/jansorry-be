package com.ssafy.jansorry.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.jansorry.favorite.domain.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

}
