package com.subject.jwt.repsitory;

import com.mysql.cj.log.Log;
import com.subject.jwt.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
