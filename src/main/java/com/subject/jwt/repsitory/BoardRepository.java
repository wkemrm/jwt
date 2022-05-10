package com.subject.jwt.repsitory;

import com.mysql.cj.log.Log;
import com.subject.jwt.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b join fetch b.member")
    public List<Board> findByBoardJoinMember();

    @Query("select b from Board b join fetch b.member where b.id = :id")
    public Board findByBoardJoinMemberById(@Param("id") Long id);
}
