package edu.cit.ogue.groupexpensetracker.features.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByGroupId(Long groupId);

    List<Expense> findByPaidBy(Long userId);

    // 🔥 ADD THIS (THIS FIXES YOUR 500 ERROR)
    List<Expense> findByGroupIdIn(List<Long> groupIds);
}