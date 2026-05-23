package edu.cit.ogue.groupexpensetracker.features.expense;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // 🔥 SIMPLE + WORKING: USER-BASED (NO GROUP DEPENDENCY)
    public List<Expense> getByUser(Long userId) {
        return expenseRepository.findByPaidBy(userId);
    }

    public List<Expense> getByGroup(Long groupId) {
        return expenseRepository.findByGroupId(groupId);
    }

    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }
}