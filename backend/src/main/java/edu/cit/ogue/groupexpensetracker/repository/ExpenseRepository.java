package edu.cit.ogue.groupexpensetracker.repository;

import edu.cit.ogue.groupexpensetracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByGroupId(Long groupId);
}
