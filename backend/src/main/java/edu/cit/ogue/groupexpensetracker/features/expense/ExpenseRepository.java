package edu.cit.ogue.groupexpensetracker.features.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT e FROM Expense e WHERE e.groupId = :groupId")
    List<Expense> findByGroupId(@Param("groupId") Long groupId);

    // Explicit query — no ambiguity with column name mapping
    @Query("SELECT e FROM Expense e WHERE e.paidBy = :userId")
    List<Expense> findByPaidBy(@Param("userId") Long userId);

    @Query("SELECT e FROM Expense e WHERE e.groupId IN :groupIds")
    List<Expense> findByGroupIdIn(@Param("groupIds") List<Long> groupIds);
}