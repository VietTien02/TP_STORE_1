package com.pts.dao;

import com.pts.entity.FavoriteCrouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteCrouseDAO extends JpaRepository<FavoriteCrouse,Integer> {

}
