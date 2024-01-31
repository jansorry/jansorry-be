package com.ssafy.jansorry.favorite.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.jansorry.favorite.domain.Favorite;

@Repository
@Primary
public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteCustomRepository {
}
