package edu.cit.ogue.groupexpensetracker.features.expense;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Transactional  // ← no readOnly
    public List<Expense> getByUser(Long userId) {
        if (userId == null) return List.of();
        List<Expense> result = expenseRepository.findByPaidBy(userId);
        return result != null ? result : List.of();
    }

    @Transactional  // ← no readOnly
    public List<Expense> getByGroup(Long groupId) {
        if (groupId == null) return List.of();
        List<Expense> result = expenseRepository.findByGroupId(groupId);
        return result != null ? result : List.of();
    }

    @Transactional
    public Expense addExpense(Expense expense) {
        if (expense == null) throw new RuntimeException("Expense cannot be null");
        return expenseRepository.save(expense);
    }
}