package edu.cit.ogue.groupexpensetracker.features.expense;

import edu.cit.ogue.groupexpensetracker.features.groups.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final GroupService groupService;

    public ExpenseService(ExpenseRepository expenseRepository, GroupService groupService) {
        this.expenseRepository = expenseRepository;
        this.groupService = groupService;
    }

    @Transactional
    public List<Expense> getByUser(Long userId) {
        if (userId == null) return List.of();
        List<Expense> result = expenseRepository.findByPaidBy(userId);
        return result != null ? result : List.of();
    }

    @Transactional
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

    // ✅ NEW: only the group creator can delete an expense
    @Transactional
    public void deleteExpense(Long expenseId, Long requesterId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));

        if (!groupService.isCreator(expense.getGroupId(), requesterId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the group creator can delete expenses");
        }

        expenseRepository.delete(expense);
    }
}